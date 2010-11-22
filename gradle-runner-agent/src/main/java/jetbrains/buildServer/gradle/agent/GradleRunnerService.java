/*
 * Copyright 2000-2010 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jetbrains.buildServer.gradle.agent;

import com.intellij.openapi.util.SystemInfo;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;
import jetbrains.buildServer.ComparisonFailureUtil;
import jetbrains.buildServer.RunBuildException;
import jetbrains.buildServer.agent.AgentRuntimeProperties;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.agent.ClasspathUtil;
import jetbrains.buildServer.agent.runner.*;
import jetbrains.buildServer.gradle.GradleRunnerConstants;
import jetbrains.buildServer.messages.ErrorData;
import jetbrains.buildServer.messages.serviceMessages.ServiceMessage;
import jetbrains.buildServer.runner.JavaRunnerConstants;
import jetbrains.buildServer.util.FileUtil;
import org.jetbrains.annotations.NotNull;

public class GradleRunnerService extends BuildServiceAdapter
{
  private final String exePath;
  private final String wrapperName;
  private List<ProcessListener> myListenerList;

  public GradleRunnerService(final String exePath, final String wrapperName) {
    this.exePath = exePath;
    this.wrapperName = wrapperName;
  }

  @Override
  @NotNull public ProgramCommandLine makeProgramCommandLine() throws RunBuildException
  {
    boolean useWrapper = ConfigurationParamsUtil.isParameterEnabled(getRunnerParameters(),
                                                                    GradleRunnerConstants.GRADLE_WRAPPER_FLAG);


    List<String> params = new LinkedList<String>();
    Map<String,String> env = new HashMap<String,String>(getEnvironmentVariables());
    File gradleExe;
    String exePath;

    if (!useWrapper) {
      File gradleHome = getGradleHome();
      gradleExe = new File(gradleHome, this.exePath);
      exePath = gradleExe.getAbsolutePath();

      if(!gradleHome.exists())
        throw new RunBuildException("Gradle home path ("+gradleHome+") is invalid.");
      if(!gradleExe.exists())
        throw new RunBuildException("Gradle home path ("+gradleHome+") does not contain a Gradle installation.  Cannot find "+
                                    this.exePath +".");
//      if(!gradleExe.canExecute())   // todo: this is a 1.6 method that is not available in TeamCity 4.5.6, maybe in later versions?
//         throw new RunBuildException("Cannot execute Gradle located at \""+gradleExe+"\".  Check file permissions.");
      env.put("GRADLE_HOME", gradleHome.getAbsolutePath());

    } else {
      gradleExe = new File(getWorkingDirectory(), wrapperName);
      exePath = gradleExe.getAbsolutePath();
      if (!gradleExe.exists())
        throw new RunBuildException("Gradle wrapper script " + wrapperName + " can not be found. " +
                                    "Please, provide wrapper script in the root project directory");
    }

    if (SystemInfo.isUnix) {
      exePath = "bash";
      params.add(gradleExe.getAbsolutePath());
    }

    params.addAll(getParams());

    env.put("GRADLE_EXIT_CONSOLE", "true");
    env.put(JavaRunnerConstants.JAVA_HOME, getJavaHome());
    env.put(GradleRunnerConstants.ENV_JAVA_OPTS, getJavaArgs());
    env.put(GradleRunnerConstants.ENV_TEAMCITY_BUILD_INIT_PATH, buildInitScriptClassPath());

    return new SimpleProgramCommandLine(env, getWorkingDirectory().getPath(), exePath, params);
  }

  private String getJavaHome() throws RunBuildException {
    String javaHome = JavaRunnerUtil.findJavaHome(getRunnerParameters().get(JavaRunnerConstants.TARGET_JDK_HOME),
                                                  getBuildParameters().getAllParameters(),
                                                  AgentRuntimeProperties.getCheckoutDir(getRunnerParameters()));
    if (javaHome == null) throw new RunBuildException("Unable to find Java home");
    javaHome = FileUtil.getCanonicalFile(new File(javaHome)).getPath();
    return javaHome;
  }

  private File getGradleHome() {
    final String gradlePath = getRunnerContext().getToolPath(GradleToolProvider.GRADLE_TOOL);
    return new File(gradlePath);
  }

  private String getJavaArgs()
  {
    return ConfigurationParamsUtil.getJavaArgs(getRunnerParameters());
  }

  private List<String> getParams()
  {
    List<String> params = new ArrayList<String>();
    insertInitScript(params);

    String gradleParams = ConfigurationParamsUtil.getGradleParams(getRunnerParameters());
    if (gradleParams.length() > 0)
      params.addAll(Arrays.asList(gradleParams.split(" ")));

    if (ConfigurationParamsUtil.isParameterEnabled(getRunnerParameters(), GradleRunnerConstants.DEBUG))
      params.add("-d");

    if (ConfigurationParamsUtil.isParameterEnabled(getRunnerParameters(), GradleRunnerConstants.STACKTRACE))
      params.add("-s");

    String gradleTasks = ConfigurationParamsUtil.getGradleTasks(getRunnerParameters());
    if (gradleTasks.length() > 0)
      params.addAll(Arrays.asList(gradleTasks.split(" ")));

    return params;
  }

  private void insertInitScript(List<String> params)
  {
    final String scriptPath = ConfigurationParamsUtil.getGradleInitScript(getRunnerParameters());
    File initScript;

    if (scriptPath.length() > 0) {
      initScript = new File(scriptPath);
    } else {
      File pluginsDirectory = getBuild().getAgentConfiguration().getAgentPluginsDirectory();
      File runnerPluginDir = new File(pluginsDirectory, GradleRunnerConstants.RUNNER_TYPE);
      initScript = new File(runnerPluginDir, GradleRunnerConstants.INIT_SCRIPT_SUFFIX);
    }

    params.add("--init-script");
    params.add(initScript.getAbsolutePath());
  }

  private String buildInitScriptClassPath() throws RunBuildException {
    try {
      File serviceMessagesLib;
      File runtimeUtil;

      serviceMessagesLib = new File(ClasspathUtil.getClasspathEntry(ServiceMessage.class));
      runtimeUtil = new File(ClasspathUtil.getClasspathEntry(ComparisonFailureUtil.class));
      return serviceMessagesLib.getAbsolutePath() + File.pathSeparator + runtimeUtil.getAbsolutePath();

    } catch (IOException e) {
      throw new RunBuildException("Failed to create init script classpath", e);
    }
  }

  @NotNull
  @Override
  public List<ProcessListener> getListeners() {
    if (null == myListenerList) {
      ProcessListener listener = new GradleVersionErrorsListener(getLogger());
      myListenerList = new ArrayList<ProcessListener>(super.getListeners().size() + 1);
      myListenerList.add(listener);
      myListenerList.addAll(super.getListeners());
    }
    return myListenerList;
  }

  public static class GradleVersionErrorsListener implements ProcessListener {

    public static final String WRONG_GRADLE_VERSION = "Incompatible Gradle initialization script API.\n" +
                                                         "Please, make sure you are running correct Gradle versoin. TeamCity requires Gradle ver. 0.9-rc-1 at least.";
    private final Pattern initScriptFailure = Pattern.compile("Could not load compiled classes for initialization script(.*?)init\\.gradle" +
                                                              "|Could not compile initialization script(.*?)init\\.gradle" +
                                                              "|'init-script' is not a recognized option" +
                                                              "|A problem occurred evaluating initialization script");

    private final BuildProgressLogger myLogger;

    private GradleVersionErrorsListener(@NotNull final BuildProgressLogger logger) {
      myLogger = logger;
    }

    public void onStandardOutput(@NotNull final String text) {
      parseAndLog(text);
    }

    public void onErrorOutput(@NotNull final String text) {
      parseAndLog(text);
    }

    private void parseAndLog(final String text) {
      if (initScriptFailure.matcher(text).find()) {
        myLogger.internalError(ErrorData.BUILD_RUNNER_ERROR_TYPE,WRONG_GRADLE_VERSION,null);
      }
    }

    public void processStarted(@NotNull final String programCommandLine, @NotNull final File workingDirectory) {
      // do nothing
    }

    public void processFinished(final int exitCode) {
    }
  }
}

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

package jetbrains.buildServer.gradle;

public class GradleRunnerConstants
{
  public static final String RUNNER_TYPE = "gradle-runner";

  public static final String GRADLE_INIT_SCRIPT = "ui.gradleRunner.gradle.init.script";
  public static final String GRADLE_HOME = "ui.gradleRunner.gradle.home";
  public static final String GRADLE_TASKS = "ui.gradleRunner.gradle.tasks.names";
  public static final String GRADLE_PARAMS = "ui.gradleRunner.additional.gradle.cmd.params";
  public static final String STACKTRACE = "ui.gradleRunner.gradle.stacktrace.enabled";
  public static final String DEBUG = "ui.gradleRunner.gradle.debug.enabled";
  public static final String GRADLE_WRAPPER_FLAG = "ui.gradleRunner.gradle.wrapper.useWrapper";

  // todo: it would be nice to autoconfigure some of the params based on type of build
  public static final String GRADLE_MODE_KEY = "GRADLE_RUNNER_MODE";
  public static final String GRADLE_CI_MODE = "continuous-integration";
  public static final String GRADLE_CD_MODE = "create-cd";

  public static final String ENV_TEAMCITY_BUILD_INIT_PATH = "TEAMCITY_BUILD_INIT_PATH";
  public static final String ENV_JAVA_OPTS = "JAVA_OPTS";
  public static final String INIT_SCRIPT_NAME = "init.gradle";
  public static final String INIT_SCRIPT_SUFFIX = "scripts/"+INIT_SCRIPT_NAME;
}
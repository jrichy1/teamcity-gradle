package test;

import org.junit.Test;

public class TestClass {

  @Test
  public void testSimpleOutput() throws Exception {
    System.out.println("1:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "2:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "3:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "4:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "5:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "6:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "7:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "8:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "9:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "0:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +

                       "11:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "12:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "13:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "14:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "15:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "16:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "17:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "18:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "19:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567");
    System.err.println("1:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "2:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "3:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "4:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "5:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "6:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "7:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "8:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "9:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       "0:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +

                       "11:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "12:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "13:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "14:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "15:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "16:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "17:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "18:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567" +
                       "19:1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567");
  }

  @Test
  public void testServiceMessages() throws Exception {
    System.out.println("            ##teamcity[testStdOut name='test.TestClass.serviceMessages' out='1:1234567890']          " +
                       "            ##teamcity[testStdOut name='test.TestClass.serviceMessages' out='2:1234567890']          " +

                       " ##teamcity[testStdOut name='test.TestClass.serviceMessages' out='12|||]34567890']" +
                       "12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                       " ##teamcity[testStdOut name='test.TestClass.serviceMessages' out='3:1234567890']" +
                       "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890" +

                       " ##teamcity[testStdOut name='a.b' out='a'] ##teamcity[testStdOut name='a.b' out='a']" +
                       " ##teamcity[testStdOut name='a.b' out='a'] ##teamcity[testStdOut name='a.b' out='b']");
  }

  @Test
  public void testBrokenServiceMessages1() throws Exception {
    System.out.println("1:12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                       " ##teamcity[testStdOut name='a.b' out=']']          " +
                       "2:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678");
  }

  @Test
  public void testBrokenServiceMessages2() throws Exception {
    System.out.println("3:12345678901234567890123456789012345678901234567890123456789012345678901234567890" +
                       " ##teamcity[testStdOut name='a.b' out='1234567890'|]" +
                       "4:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678");
  }

  @Test
  public void testBrokenServiceMessages3() throws Exception {
    System.out.println("5:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678" +
                       " ##teamcity[testStdOut name='a.b' out='1234567890'  ##teamcity[testStdOut name='a.b' out='1234567890'] " +
                       "6:12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678");
  }
}
package info.bliki.html;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllHTMLTests extends TestCase {
  public AllHTMLTests(String name) {
    super(name);
  }

  public static Test suite() {
    TestSuite s = new TestSuite();
    s.addTestSuite(HTML2WikipediaTest.class);
    s.addTestSuite(HTML2GoogleCodeTest.class);
    s.addTestSuite(HTML2JSPWikiTest.class);
    s.addTestSuite(HTML2TracTest.class);
    s.addTestSuite(HTML2MoinMoinTest.class);
    return s;
  }

}

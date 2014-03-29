package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;
 
public class EntityFilterTest extends FilterTestSupport {
  public EntityFilterTest(String name) {
    super(name);
  }

  public static Test suite() {
    return new TestSuite(EntityFilterTest.class);
  }

  public void testEntity0() {
    assertEquals("\n" + 
    		"<p>jklöäüpoißutz</p>", wikiModel.render("jklöäüpoißutz", false));
  }

  public void testEntity1() {
    assertEquals("\n" + 
    		"<p>jklöäüpoißutz</p>", wikiModel.render("jkl&ouml;&auml;&uuml;poi&szlig;utz", false));
  }
  public void testEntity2() {
    assertEquals("\n" + 
    		"<p>jklöutz</p>", wikiModel.render("jkl&#246;utz", false));
  }
}

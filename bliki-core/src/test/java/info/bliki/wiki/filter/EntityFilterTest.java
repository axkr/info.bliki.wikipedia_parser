package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class EntityFilterTest extends FilterTestSupport {

  @Test public void testEntity0() {
    assertEquals("\n" +
            "<p>jklöäüpoißutz</p>", wikiModel.render("jklöäüpoißutz", false));
  }

  @Test public void testEntity1() {
    assertEquals("\n" +
            "<p>jklöäüpoißutz</p>", wikiModel.render("jkl&ouml;&auml;&uuml;poi&szlig;utz", false));
  }
  @Test public void testEntity2() {
    assertEquals("\n" +
            "<p>jklöutz</p>", wikiModel.render("jkl&#246;utz", false));
  }
}

package info.bliki.wiki.filter;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class EntityFilterTest extends FilterTestSupport {

  @Test public void testEntity0() {
      assertThat(wikiModel.render("jklöäüpoißutz", false)).isEqualTo("\n" +
              "<p>jklöäüpoißutz</p>");
  }

  @Test public void testEntity1() {
      assertThat(wikiModel.render("jkl&ouml;&auml;&uuml;poi&szlig;utz", false)).isEqualTo("\n" +
              "<p>jklöäüpoißutz</p>");
  }
  @Test public void testEntity2() {
      assertThat(wikiModel.render("jkl&#246;utz", false)).isEqualTo("\n" +
              "<p>jklöutz</p>");
  }
}

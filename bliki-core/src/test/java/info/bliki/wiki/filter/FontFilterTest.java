package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FontFilterTest extends FilterTestSupport {

  @Test public void testFont1() {
      assertThat(wikiModel.render("<font color=\"red\">Text</font>", false)).isEqualTo("\n" +
              "<p><font color=\"red\">Text</font></p>");
  }

  @Test public void testFont4() {
      assertThat(wikiModel.render("<font color=red>Text</font>", false)).isEqualTo("\n" +
              "<p><font color=\"red\">Text</font></p>");
  }
}

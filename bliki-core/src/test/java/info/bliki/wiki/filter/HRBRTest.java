package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HRBRTest extends FilterTestSupport {

  @Test public void testNewline0() {
      assertThat(wikiModel.render("<br ===", false)).isEqualTo("\n" +
              "<p>&#60;br ===</p>");
  }

  @Test public void testNewline1() {
      assertThat(wikiModel.render("<br>", false)).isEqualTo("\n" +
              "<p><br /></p>");
  }
  @Test public void testNewline2() {
      assertThat(wikiModel.render("<br/>", false)).isEqualTo("\n" +
              "<p><br /></p>");
  }
  @Test public void testNewline3() {
      assertThat(wikiModel.render("< br >", false)).isEqualTo("\n" +
              "<p><br /></p>");
  }
  @Test public void testNewline4() {
      assertThat(wikiModel.render("< br / >", false)).isEqualTo("\n" +
              "<p><br /></p>");
  }

  @Test public void testHR01() {
      assertThat(wikiModel.render("\n----\ntest", false)).isEqualTo("<hr />\n" +
              "<p>test</p>");
  }

  @Test public void testHR02() {
      assertThat(wikiModel.render("\n<blockquote>\n----\ntest\n</blockquote>", false)).isEqualTo("<blockquote><hr />\n" +
              "<p>test\n" +
              "</p>\n</blockquote>");
  }

  @Test public void testHR03() {
      assertThat(wikiModel.render("\n------  \ntest", false)).isEqualTo("<hr />\n" +
              "<p>test</p>");
  }

  @Test public void testHR04() {
      assertThat(wikiModel.render("Start of text<HR>  \ntest", false)).isEqualTo("\n" +
              "<p>Start of text</p><hr />  \n" +
              "<p>test</p>");
  }
}

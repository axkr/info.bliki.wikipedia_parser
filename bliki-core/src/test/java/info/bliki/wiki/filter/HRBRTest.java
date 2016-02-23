package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HRBRTest extends FilterTestSupport {

  @Test public void testNewline0() throws Exception {
      assertThat(wikiModel.render("<br ===", false)).isEqualTo("\n" +
              "<p>&#60;br ===</p>");
  }

  @Test public void testNewline1() throws Exception {
      assertThat(wikiModel.render("<br>", false)).isEqualTo("\n" +
              "<p><br /></p>");
  }
  @Test public void testNewline2() throws Exception {
      assertThat(wikiModel.render("<br/>", false)).isEqualTo("\n" +
              "<p><br /></p>");
  }
  @Test public void testNewline3() throws Exception {
      assertThat(wikiModel.render("< br >", false)).isEqualTo("\n" +
              "<p><br /></p>");
  }
  @Test public void testNewline4() throws Exception {
      assertThat(wikiModel.render("< br / >", false)).isEqualTo("\n" +
              "<p><br /></p>");
  }

  @Test public void testHR01() throws Exception {
      assertThat(wikiModel.render("\n----\ntest", false)).isEqualTo("<hr />\n" +
              "<p>test</p>");
  }

  @Test public void testHR02() throws Exception {
      assertThat(wikiModel.render("\n<blockquote>\n----\ntest\n</blockquote>", false)).isEqualTo("<blockquote><hr />\n" +
              "<p>test\n" +
              "</p>\n</blockquote>");
  }

  @Test public void testHR03() throws Exception {
      assertThat(wikiModel.render("\n------  \ntest", false)).isEqualTo("<hr />\n" +
              "<p>test</p>");
  }

  @Test public void testHR04() throws Exception {
      assertThat(wikiModel.render("Start of text<HR>  \ntest", false)).isEqualTo("\n" +
              "<p>Start of text</p><hr />  \n" +
              "<p>test</p>");
  }
}

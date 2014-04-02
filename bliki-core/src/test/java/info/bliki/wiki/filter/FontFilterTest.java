package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class FontFilterTest extends FilterTestSupport {

  @Test public void testFont1() {
    assertEquals("\n" +
            "<p><font color=\"red\">Text</font></p>", wikiModel.render("<font color=\"red\">Text</font>", false));
  }

  @Test public void testFont4() {
    assertEquals("\n" +
            "<p><font color=\"red\">Text</font></p>", wikiModel.render("<font color=red>Text</font>", false));
  }
}

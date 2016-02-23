package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BoldFilterTest extends FilterTestSupport {

    @Test public void testBoldItalic() throws Exception {
        // close tags if user forget it:
        assertThat(wikiModel
                .render("'''''kursiv und fett'''''<br />\n" + "<br />\n" + "test", false)).isEqualTo("\n" + "<p><b><i>kursiv und fett</i></b><br />\n" + "<br />\n" + "test</p>");
    }

    @Test public void testBold00() throws Exception {
        assertThat(wikiModel.render("'''&amp;'''", false)).isEqualTo("\n" + "<p><b>&#38;</b></p>");
    }

    @Test public void testBold01() throws Exception {
        assertThat(wikiModel.render("'''Text'''", false)).isEqualTo("\n<p><b>Text</b></p>");
    }

    @Test public void testBold02() throws Exception {
        assertThat(wikiModel.render("'''Text1''''''&amp;'''", false)).isEqualTo("\n" + "<p><b>Text1</b><i>&#39;&#38;</i></p>");
    }

    @Test public void testBold03() throws Exception {
        // close tags if user forget it:
        assertThat(wikiModel.render("'''Text''", false)).isEqualTo("\n" + "<p><b>Text</b></p>");
    }

    @Test public void testBold04() throws Exception {
        // see bug #1860386 - bug in emphasize with 4 apostrophes
        assertThat(wikiModel.render("some''''emphasized''''text", false)).isEqualTo("\n" + "<p>some<b>&#39;emphasized</b>&#39;text</p>");
    }

    @Test public void testBold05() throws Exception {
        assertThat(wikiModel.render("some''''''emphasized''''''text", false)).isEqualTo("\n" + "<p>some<b><i>&#39;emphasized</i></b>&#39;text</p>");
    }

    @Test public void testBoldItalicStack() throws Exception {
        // close tags if user forget it:
        assertThat(wikiModel.render("<b>Text<i>hallo", false)).isEqualTo("\n" + "<p><b>Text<i>hallo</i></b></p>");
    }

    @Test public void testBoldItalic2() throws Exception {
        // close tags if user forget it:
        assertThat(wikiModel
                .render("Ein kleiner '''''Text'' 2 3 4 ''' .", false)).isEqualTo("\n" + "<p>Ein kleiner <b><i>Text</i></b><b> 2 3 4 </b> .</p>");
    }

    @Test public void testBoldItalic3() throws Exception {
        // close tags if user forget it:
        assertThat(wikiModel
                .render("Ein kleiner '''''Text''' 2 3 4 '' .", false)).isEqualTo("\n" + "<p>Ein kleiner <b><i>Text</i></b><i> 2 3 4 </i> .</p>");
    }

    @Test public void testBoldItalic4() throws Exception {
        // close tags if user forget it:
        assertThat(wikiModel.render("Ein kleiner '''Text'' 2 3 4 ''''' .", false)).isEqualTo("\n" + "<p>Ein kleiner <b>Text<i> 2 3 4 </i></b> .</p>");
    }

    @Test public void testBoldWithPunctuation() throws Exception {
        assertThat(wikiModel.render("'''Text''':", false)).isEqualTo("\n" + "<p><b>Text</b>:</p>");
    }

    @Test public void testOpenBold() throws Exception {
        assertThat(wikiModel.render("para test '''bold\n" +
                "next para", false)).isEqualTo("\n" +
                "<p>para test <b>bold</b>\n" +
                "next para</p>");
    }
}

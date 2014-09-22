package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItalicFilterTest extends FilterTestSupport {

    @Test public void testEM() {
        assertThat(wikiModel.render("a <em> project </em>.", false)).isEqualTo("\n" + "<p>a <em> project </em>.</p>");
    }

    @Test public void testItalic01() {
        assertThat(wikiModel.render("''Text''", false)).isEqualTo("\n" + "<p><i>Text</i></p>");
    }

//    @Test public void testItalic02() {
//        assertEquals(
//                "",
//                wikiModel
//                        .render("Terry Mattingly&#32;({{FormatDate}}).&#32;''[http://www.pottsmerc.com/articles/2009/04/12/opinion/srv0000005095974.txt Actor Tom Hanks talks about religion]''.&#32;"));
//    }

    @Test public void testItalicWithPunctuation() {
        assertThat(wikiModel.render("''Text'':", false)).isEqualTo("\n" + "<p><i>Text</i>:</p>");
    }
}

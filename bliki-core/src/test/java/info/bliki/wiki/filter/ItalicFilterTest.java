package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class ItalicFilterTest extends FilterTestSupport {

    @Test public void testEM() {
        assertEquals("\n" + "<p>a <em> project </em>.</p>", wikiModel.render("a <em> project </em>.", false));
    }

    @Test public void testItalic01() {
        assertEquals("\n" + "<p><i>Text</i></p>", wikiModel.render("''Text''", false));
    }

//    @Test public void testItalic02() {
//        assertEquals(
//                "",
//                wikiModel
//                        .render("Terry Mattingly&#32;({{FormatDate}}).&#32;''[http://www.pottsmerc.com/articles/2009/04/12/opinion/srv0000005095974.txt Actor Tom Hanks talks about religion]''.&#32;"));
//    }

    @Test public void testItalicWithPunctuation() {
        assertEquals("\n" + "<p><i>Text</i>:</p>", wikiModel.render("''Text'':", false));
    }
}

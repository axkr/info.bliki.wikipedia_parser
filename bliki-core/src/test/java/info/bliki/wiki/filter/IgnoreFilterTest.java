package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class IgnoreFilterTest extends FilterTestSupport {

    /**
     * see Issue94
     */
    @Test public void testInputbox001() {
        assertEquals("\n" +
                "<p>start  end</p>", wikiModel.render("start <inputbox>\n" + "type=search\n" + "width=31\n" + "buttonlabel=Go\n"
                + "searchbuttonlabel=Search\n" + "break=no\n" + "</inputbox> end", false));
    }

    @Test public void testImagemap001() {
        assertEquals("\n" +
                "<p>start  end</p>", wikiModel.render("start <imagemap>\n" +
                "Image:Example2.png|150px|alt=Alt text\n" +
                "default [[Main Page|Go to main page]]\n" +
                "</imagemap> end", false));
    }

}

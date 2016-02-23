package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IgnoreFilterTest extends FilterTestSupport {

    /**
     * see Issue94
     */
    @Test public void testInputbox001() throws Exception {
        assertThat(wikiModel.render("start <inputbox>\n" + "type=search\n" + "width=31\n" + "buttonlabel=Go\n"
                + "searchbuttonlabel=Search\n" + "break=no\n" + "</inputbox> end", false)).isEqualTo("\n" +
                "<p>start  end</p>");
    }

    @Test public void testImagemap001() throws Exception {
        assertThat(wikiModel.render("start <imagemap>\n" +
                "Image:Example2.png|150px|alt=Alt text\n" +
                "default [[Main Page|Go to main page]]\n" +
                "</imagemap> end", false)).isEqualTo("\n" +
                "<p>start  end</p>");
    }

}

package info.bliki.wiki.filter;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class DivFilterTest extends FilterTestSupport {

    @Test public void testDivTag1() {
        assertThat(wikiModel
                .render("<div class=\"TabelleFeldListe\">\n"
                        + "<div class=\"TabelleFeldListeHeader\">AuftragsKopf Felder</div>\n"
                        + "*[[item1]]\n" + "*[[item2]]\n" + "*...\n"
                        + "</div>", false)).isEqualTo("\n" +
                "<div class=\"TabelleFeldListe\">\n" +
                "<div class=\"TabelleFeldListeHeader\">AuftragsKopf Felder</div>\n" +
                "\n" +
                "<ul>\n" +
                "<li><a href=\"http://www.bliki.info/wiki/Item1\" title=\"Item1\">item1</a></li>\n" +
                "<li><a href=\"http://www.bliki.info/wiki/Item2\" title=\"Item2\">item2</a></li>\n" +
                "<li>...</li>\n</ul></div>");
    }
}

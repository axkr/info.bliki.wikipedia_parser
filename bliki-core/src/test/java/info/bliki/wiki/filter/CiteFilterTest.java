package info.bliki.wiki.filter;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class CiteFilterTest extends FilterTestSupport {

    @Test public void testDivTag1() {
        assertThat(wikiModel
                .render("<cite id=\"Test\">\n"
                        + "a cite text\n"
                        + "</cite>", false)).isEqualTo("\n" +
                "<p><cite id=\"Test\">\n" +
                "a cite text\n" +
                "</cite></p>");
    }
}

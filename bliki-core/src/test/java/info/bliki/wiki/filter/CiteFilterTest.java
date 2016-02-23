package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CiteFilterTest extends FilterTestSupport {

    @Test public void testDivTag1() throws Exception {
        assertThat(wikiModel
                .render("<cite id=\"Test\">\n"
                        + "a cite text\n"
                        + "</cite>", false)).isEqualTo("\n" +
                "<p><cite id=\"Test\">\n" +
                "a cite text\n" +
                "</cite></p>");
    }
}

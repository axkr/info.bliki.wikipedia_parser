package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CiteFilterTest extends FilterTestSupport {

    @Test public void testDivTag1() {
        assertEquals(
                "\n" +
                        "<p><cite id=\"Test\">\n" +
                        "a cite text\n" +
                        "</cite></p>",
                wikiModel
                        .render("<cite id=\"Test\">\n"
                                + "a cite text\n"
                                + "</cite>", false)
        );
    }
}

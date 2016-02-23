package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ItalicFilterTest extends FilterTestSupport {

    @Test public void testEM() throws Exception {
        assertThat(wikiModel.render("a <em> project </em>.", false)).isEqualTo("\n" + "<p>a <em> project </em>.</p>");
    }

    @Test public void testItalic01() throws Exception {
        assertThat(wikiModel.render("''Text''", false)).isEqualTo("\n" + "<p><i>Text</i></p>");
    }

    @Test public void testItalicWithPunctuation() throws Exception {
        assertThat(wikiModel.render("''Text'':", false)).isEqualTo("\n" + "<p><i>Text</i>:</p>");
    }
}

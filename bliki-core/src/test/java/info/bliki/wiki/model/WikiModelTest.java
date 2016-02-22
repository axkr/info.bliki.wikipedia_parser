package info.bliki.wiki.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WikiModelTest {
    private WikiModel subject;

    @Before
    public void setUp() throws Exception {
        subject = new WikiModel("image", "link");
    }

    @Test
    public void testIsInterWiki() throws Exception {
        assertThat(subject.isInterWiki("w")).isTrue();
        assertThat(subject.isInterWiki("s")).isTrue();

        assertThat(subject.isInterWiki("Talk")).isFalse();
    }

    @Test
    public void testAppendRawNamespaceLinksWithInterWikLink() throws Exception {
        assertThat(subject.appendRawNamespaceLinks("s:foo", "foo", true)).isTrue();
    }

    @Test
    public void testAppendRawNamespaceLinksWithInterLanguageLink() throws Exception {
        assertThat(subject.appendRawNamespaceLinks(":kk:", "foo", true)).isTrue();
    }
}

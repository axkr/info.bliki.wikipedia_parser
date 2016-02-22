package info.bliki.wiki.model;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InterWikiMapTest {
    private InterWikiMap subject;

    @Before
    public void setUp() throws Exception {
        subject = new InterWikiMap(new Configuration().getInterWikiMapping(), "enwiki");
    }

    @Test
    public void testGetInterWikiReturnsNullIfNotFound() throws Exception {
        assertThat(subject.getInterWiki("not-found")).isNull();
    }

    @Test
    public void testGetInterWikiWithLangCode() throws Exception {
        final InterWiki interWiki = subject.getInterWiki("fr");
        assertThat(interWiki).isNotNull();
        assertThat(interWiki.pattern).isEqualTo("//fr.wikipedia.org/wiki/$1");
        assertThat(interWiki.local).isTrue();
    }

    @Test
    public void testGetInterWikiWithShortCode() throws Exception {
        final InterWiki interWiki = subject.getInterWiki("s");
        assertThat(interWiki).isNotNull();
        assertThat(interWiki.pattern).isEqualTo("//en.wikisource.org/wiki/$1");
        assertThat(interWiki.local).isTrue();
    }

    @Test
    public void testGetInterWikiIsCaseInsensitive() throws Exception {
        final InterWiki interWiki = subject.getInterWiki("S");
        assertThat(interWiki).isNotNull();
        assertThat(interWiki.pattern).isEqualTo("//en.wikisource.org/wiki/$1");
        assertThat(interWiki.local).isTrue();
    }

    @Test
    public void testGetInterWikiWithGlobalCode() throws Exception {
        final InterWiki interWiki = subject.getInterWiki("rfc");
        assertThat(interWiki).isNotNull();
        assertThat(interWiki.pattern).isEqualTo("//tools.ietf.org/html/rfc$1");
        assertThat(interWiki.local).isFalse();
    }

    @Test
    public void testInterWikiLinksAreRelativeToCurrentInstallation() throws Exception {
        subject = new InterWikiMap(new Configuration().getInterWikiMapping(), "frwiktionary");
        final InterWiki interWiki = subject.getInterWiki("s");
        assertThat(interWiki.pattern).isEqualTo("//fr.wikisource.org/wiki/$1");
    }
}

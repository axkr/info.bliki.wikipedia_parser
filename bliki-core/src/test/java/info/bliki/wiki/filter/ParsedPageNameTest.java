package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ParsedPageNameTest extends FilterTestSupport {
    @Test public void testParsePageName() throws Exception {
        ParsedPageName name = ParsedPageName.parsePageName(wikiModel, "foo", null, false, false);
        assertThat(name.pagename).isEqualTo("foo");
    }

    @Test public void testParsePageNameWithNamespace() throws Exception {
        ParsedPageName name = ParsedPageName.parsePageName(wikiModel, "Talk:foo", null, false, false);
        assertThat(name.pagename).isEqualTo("foo");
        assertThat(name.namespace).isEqualTo(wikiModel.getNamespace().getTalk());
    }

    @Test public void testParsePageWithEmptyStringParsesAsEmpty() throws Exception {
        ParsedPageName name = ParsedPageName.parsePageName(wikiModel, "", null, false, false);
        assertThat(name.pagename).isEqualTo("");
        assertThat(name.namespace).isNull();
    }

    @Test(expected = NullPointerException.class) public void testParsePageWithNullThrowsNullPointerException() throws Exception {
        ParsedPageName.parsePageName(wikiModel, null, null, false, false);
    }
}

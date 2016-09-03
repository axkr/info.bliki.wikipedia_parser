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

    @Test public void testParsePageNameWithNamespaceArgument() throws Exception {
        ParsedPageName name = ParsedPageName.parsePageName(wikiModel, "foo", wikiModel.getNamespace().getModule(), false, false);
        assertThat(name.fullPagename()).isEqualTo("Module:foo");
    }

    @Test public void testParsePageNameWithNamespaceArgumentAlreadyContainingNamespace() throws Exception {
        ParsedPageName name = ParsedPageName.parsePageName(wikiModel, "Module:foo", wikiModel.getNamespace().getModule(), false, false);
        assertThat(name.fullPagename()).isEqualTo("Module:foo");
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

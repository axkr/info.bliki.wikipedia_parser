package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.wiki.tags.WPATag;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static info.bliki.wiki.tags.WPATag.HREF;
import static info.bliki.wiki.tags.WPATag.TITLE;
import static info.bliki.wiki.tags.WPATag.WIKILINK;
import static org.assertj.core.api.Assertions.assertThat;

public class MarkdownConverterTest extends FilterTestSupport  {
    private MarkdownConverter markdownConverter;

    @Override public void setUp() throws Exception {
        super.setUp();
        markdownConverter = new MarkdownConverter(true);
    }

    @Test public void testConvertItalic() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "This is ''italic''", false)).isEqualTo("\nThis is *italic*");
    }

    @Test public void testConvertBold() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "This is '''bold'''", false)).isEqualTo("\nThis is **bold**");
    }

    @Test public void testConvertBoldItalic() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "This is '''''bold italic'''''", false)).isEqualTo("\nThis is **bold italic**");
    }

    @Test public void testConvertItalicWithoutEmphasis() throws Exception {
        markdownConverter = new MarkdownConverter(false);
        assertThat(wikiModel.render(markdownConverter, "This is ''italic''", false)).isEqualTo("\nThis is italic");
    }

    @Test public void testConvertBoldWithoutEmphasis() throws Exception {
        markdownConverter = new MarkdownConverter(false);
        assertThat(wikiModel.render(markdownConverter, "This is '''bold'''", false)).isEqualTo("\nThis is bold");
    }

    @Test public void testConvertBoldItalicWithoutEmphasis() throws Exception {
        markdownConverter = new MarkdownConverter(false);
        assertThat(wikiModel.render(markdownConverter, "This is '''''bold italic'''''", false)).isEqualTo("\nThis is bold italic");
    }

    @Test public void testConvertLink() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[foo]]", false)).isEqualTo("\n[foo](foo)");
    }

    @Test public void testConvertLinkWithTitle() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[foo|other text]]", false)).isEqualTo("\n[other text](foo)");
    }

    @Test public void testConvertLinkWithAnchor() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[foo#anchor]]", false)).isEqualTo("\n[foo](foo#anchor)");
    }

    @Test public void testConvertLinkWithAnchorAndParens() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[foo (bar)#anchor]]", false)).isEqualTo("\n[foo (bar)](foo_\\(bar\\)#anchor)");
    }

    @Test public void testConvertLinkWithPrecedingDashThroughModelWithRenderLinks() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "-[[foo]]", false)).isEqualTo("\n-[foo](foo)");
    }

    @Test public void testConvertPipeLink() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[#English|headword]]", false)).isEqualTo("\n[headword](headword#English)");
    }

    @Test public void testConvertQuotedLink() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[Foo#Baz Space|*headword]]", false)).isEqualTo("\n[\\*headword](Foo#Baz_Space)");
    }

    @Test public void testConvertQuotedLinkWithParens() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[bla (blub)|text]]", false)).isEqualTo("\n[text](bla_\\(blub\\))");
    }

    @Test public void testConvertLinkNodeWithRenderLinks() throws Exception {
        WPATag aTag = new WPATag();
        aTag.addAttribute(HREF, "some-href", false);
        aTag.addAttribute(TITLE, "title", false);
        aTag.addObjectAttribute(WIKILINK, "foo-link");
        aTag.addChild(new ContentToken("foo"));

        assertThat(convert(Collections.singletonList(aTag))).isEqualTo("[foo](foo-link)");
    }

    protected String convert(List<?> nodes) throws IOException {
        StringBuffer buffer = new StringBuffer();
        markdownConverter.nodesToText(nodes, buffer, wikiModel);
        return buffer.toString();
    }
}

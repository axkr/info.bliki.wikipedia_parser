package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.wiki.MarkdownConverter;
import info.bliki.wiki.tags.WPATag;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static info.bliki.wiki.tags.WPATag.HREF;
import static info.bliki.wiki.tags.WPATag.TITLE;
import static info.bliki.wiki.tags.WPATag.WIKILINK;
import static org.assertj.core.api.Assertions.assertThat;

public class MarkdownConverterText extends FilterTestSupport  {
    private MarkdownConverter markdownConverter;

    @Override public void setUp() throws Exception {
        super.setUp();
        markdownConverter = new MarkdownConverter();
    }

    @Test
    public void testConvertItalic() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "This is ''italic''", false)).isEqualTo("\nThis is *italic*");
    }

    @Test public void testConvertBold() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "This is '''bold'''", false)).isEqualTo("\nThis is **bold**");
    }

    @Test public void testConvertBoldItalic() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "This is '''''bold italic'''''", false)).isEqualTo("\nThis is **bold italic**");
    }

    @Test public void testConvertLinkThroughModelWithRenderLinks() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[foo]]", false)).isEqualTo("\n[foo][]");
    }

    @Test public void testConvertLinkWithTitleThroughModelWithRenderLinks() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[foo|other text]]", false)).isEqualTo("\n[other text](foo)");
    }

    @Test public void testConvertLinkWithAnchorThroughModelWithRenderLinks() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "[[foo#anchor]]", false)).isEqualTo("\n[foo](foo#anchor)");
    }

    @Test public void testConvertLinkWithPrecedingDashThroughModelWithRenderLinks() throws Exception {
        assertThat(wikiModel.render(markdownConverter, "-[[foo]]", false)).isEqualTo("\n-[foo][]");
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

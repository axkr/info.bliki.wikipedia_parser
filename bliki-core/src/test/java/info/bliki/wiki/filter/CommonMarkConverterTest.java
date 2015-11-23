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

public class CommonMarkConverterTest extends FilterTestSupport  {
    private CommonMarkConverter commonMarkConverter;

    @Override public void setUp() throws Exception {
        super.setUp();
        commonMarkConverter = new CommonMarkConverter(true);
    }

    @Test public void testConvertItalic() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "This is ''italic''", false)).isEqualTo("\nThis is *italic*");
    }

    @Test public void testConvertBold() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "This is '''bold'''", false)).isEqualTo("\nThis is **bold**");
    }

    @Test public void testConvertBoldItalic() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "This is '''''bold italic'''''", false)).isEqualTo("\nThis is **bold italic**");
    }

    @Test public void testConvertItalicWithoutEmphasis() throws Exception {
        commonMarkConverter = new CommonMarkConverter(false);
        assertThat(wikiModel.render(commonMarkConverter, "This is ''italic''", false)).isEqualTo("\nThis is italic");
    }

    @Test public void testConvertBoldWithoutEmphasis() throws Exception {
        commonMarkConverter = new CommonMarkConverter(false);
        assertThat(wikiModel.render(commonMarkConverter, "This is '''bold'''", false)).isEqualTo("\nThis is bold");
    }

    @Test public void testConvertBoldItalicWithoutEmphasis() throws Exception {
        commonMarkConverter = new CommonMarkConverter(false);
        assertThat(wikiModel.render(commonMarkConverter, "This is '''''bold italic'''''", false)).isEqualTo("\nThis is bold italic");
    }

    @Test public void testConvertLink() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[foo]]", false)).isEqualTo("\n[foo](foo)");
    }

    @Test public void testConvertLinkWithTitle() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[foo|other text]]", false)).isEqualTo("\n[other text](foo)");
    }

    @Test public void testConvertLinkWithAnchor() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[foo#anchor]]", false)).isEqualTo("\n[foo](foo#anchor)");
    }

    @Test public void testConvertLinkWithAnchorAndParens() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[foo (bar)#anchor]]", false)).isEqualTo("\n[foo (bar)](<foo (bar)#anchor>)");
    }

    @Test public void testConvertLinkWithNestedParens() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[((bar))]]", false)).isEqualTo("\n[((bar))](<((bar))>)");
    }

    @Test public void testConvertLinkWithPrecedingDashThroughModelWithRenderLinks() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "-[[foo]]", false)).isEqualTo("\n-[foo](foo)");
    }

    @Test public void testConvertPipeLink() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[#English|headword]]", false)).isEqualTo("\n[headword](headword#English)");
    }

    @Test public void testConvertPipeTrick() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[foo|]]", false)).isEqualTo("\n[foo](foo)");
    }

    @Test public void testConvertQuotedLink() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[Foo#Baz Space|*headword]]", false)).
                isEqualTo("\n[*headword](<Foo#Baz Space>)");
    }

    @Test public void testConvertQuotedLinkWithParens() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[(blub)|text]]", false)).isEqualTo("\n[text]((blub))");
        assertThat(wikiModel.render(commonMarkConverter, "[[()|text]]", false)).isEqualTo("\n[text](())");
    }

    @Test public void testConvertQuotedLinkWithNestedParens() throws Exception {
        assertThat(wikiModel.render(commonMarkConverter, "[[bla ((blub))|text]]", false)).isEqualTo("\n[text](<bla ((blub))>)");
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
        commonMarkConverter.nodesToText(nodes, buffer, wikiModel);
        return buffer.toString();
    }
}

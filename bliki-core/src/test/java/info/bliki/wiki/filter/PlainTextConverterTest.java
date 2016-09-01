package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.wiki.tags.PTag;
import info.bliki.wiki.tags.WPATag;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static info.bliki.wiki.tags.WPATag.HREF;
import static info.bliki.wiki.tags.WPATag.TITLE;
import static info.bliki.wiki.tags.WPATag.WIKILINK;
import static org.assertj.core.api.Assertions.assertThat;

public class PlainTextConverterTest extends FilterTestSupport {
    private PlainTextConverter plainTextConverter;

    @Override public void setUp() throws Exception {
        super.setUp();
        plainTextConverter = new PlainTextConverter();
    }

    @Test public void testConvertEmptyList() throws Exception {
        assertThat(convert(Collections.EMPTY_LIST)).isEmpty();
    }

    @Test public void testConvertSingleNode() throws Exception {
        PTag pTag = new PTag();
        pTag.addChild(new ContentToken("text"));
        assertThat(convert(Collections.singletonList(pTag))).isEqualTo("\ntext");
    }

    @Test public void testConvertLinkThroughModel() throws Exception {
        assertThat(wikiModel.render(plainTextConverter, "[[foo]]", false)).isEqualTo("\nfoo");
    }

    @Test public void testConvertSentenceEndingWithPeriod() throws Exception {
        assertThat(wikiModel.render(plainTextConverter, "Foo Bar.", false)).isEqualTo("\nFoo Bar.");
    }

    @Test public void testConvertLinkWithTitleThroughModel() throws Exception {
        assertThat(wikiModel.render(plainTextConverter, "[[foo|other text]]", false)).isEqualTo("\nother text");
    }

    @Test public void testConvertItalic() throws Exception {
        assertThat(wikiModel.render(plainTextConverter, "This is ''italic''", false)).isEqualTo("\nThis is italic");
    }

    @Test public void testConvertBold() throws Exception {
        assertThat(wikiModel.render(plainTextConverter, "This is '''bold'''", false)).isEqualTo("\nThis is bold");
    }

    @Test public void testConvertBoldItalic() throws Exception {
        assertThat(wikiModel.render(plainTextConverter, "This is '''''bold italic'''''", false)).isEqualTo("\nThis is bold italic");
    }

    @Test public void testConvertAnchoredLinkThroughModel() throws Exception {
        assertThat(wikiModel.render(plainTextConverter, "[[foo#anchored]]", false)).isEqualTo("\nfoo#anchored");
    }

    @Test public void testConvertLinkThroughModelWithRenderLinks() throws Exception {
        plainTextConverter = new PlainTextConverter(true);
        assertThat(wikiModel.render(plainTextConverter, "[[foo]]", false)).isEqualTo("\nfoo");
    }

    @Test public void testConvertLinkWithTitleThroughModelWithRenderLinks() throws Exception {
        plainTextConverter = new PlainTextConverter(true);
        assertThat(wikiModel.render(plainTextConverter, "[[foo|other text]]", false)).isEqualTo("\nother text");
    }

    @Test public void testConvertLinkWithAnchorThroughModelWithRenderLinks() throws Exception {
        plainTextConverter = new PlainTextConverter(true);
        assertThat(wikiModel.render(plainTextConverter, "[[foo#anchor]]", false)).isEqualTo("\nfoo#anchor");
    }

    @Test public void testConvertLinkWithPrecedingDashThroughModelWithRenderLinks() throws Exception {
        plainTextConverter = new PlainTextConverter(true);
        assertThat(wikiModel.render(plainTextConverter, "-[[foo]]", false)).isEqualTo("\n-foo");
    }

    @Test public void testConvertPipeLink() throws Exception {
        assertThat(wikiModel.render(plainTextConverter, "[[#English|headword]]", false)).isEqualTo("\nheadword");
    }

    @Test public void testConvertHorizontalRule() throws Exception {
        plainTextConverter = new PlainTextConverter(true);
        assertThat(wikiModel.render(plainTextConverter, "----\n", false)).isEqualTo("");
    }

    @Test public void testRefTagsGetFilteredOut() throws Exception {
        plainTextConverter = new PlainTextConverter(true);
        assertThat(wikiModel.render(plainTextConverter, "foo<ref>bar</ref>baz", false)).isEqualTo("\nfoobaz");
    }

    @Test public void testConvertLinkNode() throws Exception {
        WPATag aTag = new WPATag();
        aTag.addAttribute(HREF, "some-href", false);
        aTag.addAttribute(TITLE, "title", false);
        aTag.addObjectAttribute(WIKILINK, "foo-link");
        aTag.addChild(new ContentToken("foo"));

        assertThat(convert(Collections.singletonList(aTag))).isEqualTo("foo");
    }

    protected String convert(List<?> nodes) throws IOException {
        StringBuffer buffer = new StringBuffer();
        plainTextConverter.nodesToText(nodes, buffer, wikiModel);
        return buffer.toString();
    }
}

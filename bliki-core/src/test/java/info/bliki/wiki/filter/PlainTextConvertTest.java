package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.wiki.tags.PTag;
import info.bliki.wiki.tags.WPATag;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PlainTextConvertTest extends FilterTestSupport {
    private PlainTextConverter plainTextConverter;

    @Override
    public void setUp() throws Exception {
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

    @Test public void testConvertLinkNodeThroughModel() throws Exception {
        assertThat(wikiModel.render(plainTextConverter, "[[foo]]", false)).isEqualTo("\nfoo");
    }

    @Test public void testConvertLinkNode() throws Exception {
        WPATag aTag = new WPATag();
        aTag.addAttribute("href", "some-href", false);
        aTag.addAttribute("title", "title", false);
        aTag.addObjectAttribute("wikilink", "foo");
        aTag.addChild(new ContentToken("foo"));

        assertThat(convert(Collections.singletonList(aTag))).isEqualTo("foo");
    }

    private String convert(List<?> nodes) throws IOException {
        StringBuffer buffer = new StringBuffer();
        plainTextConverter.nodesToText(nodes, buffer, wikiModel);
        return buffer.toString();
    }
}

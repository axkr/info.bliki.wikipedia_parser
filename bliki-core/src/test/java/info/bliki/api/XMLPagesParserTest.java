package info.bliki.api;

import org.junit.Test;

import static info.bliki.api.Fixtures.xml;
import static org.assertj.core.api.Assertions.assertThat;

public class XMLPagesParserTest {
    @Test public void testParsePageParsesTitleCorrectly() throws Exception {
        XMLPagesParser parser = new XMLPagesParser(xml("fooPage"));
        parser.parse();
        assertThat(assertAndReturnOnePage(parser).getTitle()).isEqualTo("foo");
    }

    @Test public void testParseValidPageParsesDoesNotHaveInvalidFlagSet() throws Exception {
        XMLPagesParser parser = new XMLPagesParser(xml("fooPage"));
        parser.parse();
        assertThat(assertAndReturnOnePage(parser).isInvalid()).isFalse();
    }

    @Test public void testParseValidPageParsesDoesNotHaveMissingFlagSet() throws Exception {
        XMLPagesParser parser = new XMLPagesParser(xml("fooPage"));
        parser.parse();
        assertThat(assertAndReturnOnePage(parser).isMissing()).isFalse();
    }

    @Test public void testParseParsesMissingPageCorrectly() throws Exception {
        XMLPagesParser parser = new XMLPagesParser(xml("missingPage"));
        parser.parse();
        assertThat(assertAndReturnOnePage(parser).isMissing()).isTrue();
    }

    @Test public void testParseParsesInvalidPageCorrectly() throws Exception {
        XMLPagesParser parser = new XMLPagesParser(xml("invalidPage"));
        parser.parse();
        assertThat(assertAndReturnOnePage(parser).isInvalid()).isTrue();
    }

    @Test public void testParsePageWithWarnings() throws Exception {
        XMLPagesParser parser = new XMLPagesParser(xml("fooPageWithWarning"));
        parser.parse();
        assertThat(parser.getWarnings()).containsExactly("This is a warning");
    }
    private Page assertAndReturnOnePage(XMLPagesParser parser) {
        assertThat(parser.getPagesList()).hasSize(1);
        return  parser.getPagesList().get(0);
    }
}

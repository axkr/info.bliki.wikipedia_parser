package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.tags.HTMLBlockTag;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

/**
 * Support class for defining JUnit FilterTests.
 *
 */
public class FilterTestSupport {
    public static final String WINDOWS_NEWLINE = "\r\n";

    public static final String UNIX_NEWLINE = "\n";

    public static final String NEWLINE = WINDOWS_NEWLINE;

    protected WikiModel wikiModel = null;

    static {
        Configuration.DEFAULT_CONFIGURATION.addTokenTag("iframe", new HTMLBlockTag("iframe", Configuration.SPECIAL_BLOCK_TAGS));
    }


    /**
     * Set up a test model, which contains predefined templates
     */
    @Before
    public void setUp() throws Exception {
        wikiModel = newWikiTestModel();
    }

    protected final WikiModel newWikiTestModel() {
        return newWikiTestModel(Locale.ENGLISH);
    }

    protected WikiModel newWikiTestModel(Locale locale) {
        WikiTestModel wikiModel = new WikiTestModel(locale,
                "http://www.bliki.info/wiki/${image}",
                "http://www.bliki.info/wiki/${title}");
        wikiModel.setUp();
        return wikiModel;
    }

    @Test public void testStub() {
    }

    /**
     * simple example
     */
    public static void main(String[] args) {
        WikiModel wikiModel = new WikiModel(
                Configuration.DEFAULT_CONFIGURATION, Locale.GERMAN,
                "http://www.bliki.info/wiki/${image}",
                "http://www.bliki.info/wiki/${title}");
        try {
            wikiModel.setUp();

            String htmlStr = wikiModel.render(
                    "This is a simple [[Hello World]] wiki tag", false);
            System.out.print(htmlStr);
        } finally {
            wikiModel.tearDown();
        }
    }
}

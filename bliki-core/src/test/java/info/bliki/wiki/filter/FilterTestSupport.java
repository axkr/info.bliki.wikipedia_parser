package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.tags.HTMLBlockTag;
import org.junit.Before;

import java.util.Locale;

public class FilterTestSupport {
    protected WikiModel wikiModel;

    static {
        Configuration.DEFAULT_CONFIGURATION.addTokenTag("iframe", new HTMLBlockTag("iframe", Configuration.SPECIAL_BLOCK_TAGS));
    }

    @Before public void setUp() throws Exception {
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
}

package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.tags.HTMLBlockTag;
import org.junit.Before;

import java.util.Locale;

public class FilterTestSupport {
    protected WikiModel wikiModel;

    @Before public void setUp() throws Exception {
        wikiModel = newWikiTestModel();
    }

    private WikiModel newWikiTestModel() {
        WikiTestModel model = new WikiTestModel(Locale.ENGLISH,
            "http://www.bliki.info/wiki/${image}",
            "http://www.bliki.info/wiki/${title}",
            "wikitestModel");
        model.addTokenTag("iframe", new HTMLBlockTag("iframe", Configuration.SPECIAL_BLOCK_TAGS));
        model.setUp();
        return model;
    }
}

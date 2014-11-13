package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.filter.WikiTestModel;
import info.bliki.wiki.model.WikiModel;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class ScribuntoLuaEngineIntegrationTest {
    private WikiModel wikiModel;

    @Before public void setUp() throws Exception {
        wikiModel = new WikiTestModel(Locale.ENGLISH,
            "http://www.bliki.info/wiki/${image}",
            "http://www.bliki.info/wiki/${title}",
            "wiktionaryTestModel");
        wikiModel.setUp();
    }

    @Ignore @Test public void test_pt_verb_form_of() throws Exception {
        assertThat(wikiModel.render(new PlainTextConverter(), "{{pt-verb-form-of|rapar}}").trim()).isEqualTo("Foo");
    }
}

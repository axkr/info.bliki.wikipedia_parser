package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.filter.WikiTestModel;
import info.bliki.wiki.model.WikiModel;
import org.junit.Before;
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

    @Test public void test_pt_verb_form_of() throws Exception {
        wikiModel.setPageName("rape");

        assertThat(wikiModel.render(new PlainTextConverter(), "{{pt-verb-form-of|rapar}}").trim())
                .isEqualTo("first-person singular (eu) present subjunctive of rapar\n" +
                        "\n" +
                        "third-person singular (ele and ela, also used with você and others) present subjunctive of rapar\n" +
                        "third-person singular (você) affirmative imperative of rapar\n" +
                        "third-person singular (você) negative imperative of rapar");
    }
}

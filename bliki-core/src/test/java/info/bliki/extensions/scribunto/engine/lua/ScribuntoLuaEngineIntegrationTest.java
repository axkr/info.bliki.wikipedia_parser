package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.annotations.IntegrationTest;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.filter.WikiTestModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@Category(IntegrationTest.class)
public class ScribuntoLuaEngineIntegrationTest {
    private WikiTestModel wikiModel;

    @Before public void setUp() throws Exception {
        wikiModel = new WikiTestModel(Locale.ENGLISH,
            "http://www.bliki.info/wiki/${image}",
            "http://www.bliki.info/wiki/${title}",
            "wiktionaryTestModel");
        wikiModel.setUp();
        wikiModel.setDebug(false);
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

    @Test public void test_contraction_of() throws Exception {
        assertThat(wikiModel.render(new PlainTextConverter(), "{{contraction of|[[de]] [[o]]||[[of]] [[the]]|lang=pt}}").trim())
                .isEqualTo("contraction of de o (“of the”).");
    }

    @Test public void test_pt_verb() throws Exception {
        assertThat(wikiModel.render(new PlainTextConverter(), "{{pt-verb|cant|ar}}").trim())
                .isEqualTo("cantar (first-person singular present indicative canto, past participle cantado)");
    }

    @Test public void test_pt_conj() throws Exception {
        assertThat(wikiModel.render(new HTMLConverter(), "{{pt-conj|cant|ar}}").trim())
                .isXmlEqualToContentOf(new File(getClass().getResource("/expected/pt-conj-cantar.html").getFile()));
    }

}

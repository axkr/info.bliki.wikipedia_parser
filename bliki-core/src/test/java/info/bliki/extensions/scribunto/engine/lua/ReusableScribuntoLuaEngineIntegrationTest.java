package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.annotations.IntegrationTest;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.filter.ReusableWikiTestModel;
import info.bliki.wiki.filter.WikiTestModel;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@Category(IntegrationTest.class)
public class ReusableScribuntoLuaEngineIntegrationTest {
    private ReusableWikiTestModel reusableWikiModel;
    private WikiTestModel wikiModel;

    @Before public void setUp() throws Exception {
        reusableWikiModel = new ReusableWikiTestModel(Locale.ENGLISH,
            "http://www.bliki.info/wiki/${image}",
            "http://www.bliki.info/wiki/${title}",
            "wiktionaryTestModel");
        reusableWikiModel.setUp();
        reusableWikiModel.setDebug(false);
        reusableWikiModel.setTemplateCallsCache(null);
        wikiModel = new WikiTestModel(Locale.ENGLISH,
                "http://www.bliki.info/wiki/${image}",
                "http://www.bliki.info/wiki/${title}",
                "wiktionaryTestModel");
        wikiModel.setUp();
        wikiModel.setDebug(false);
        // Make sure the call cache is inactive (NOTE: as it is a Map declared as static in Configurations objects,
        // voiding the reusableWikiModel cache voids all other wikiModel caches.
        reusableWikiModel.setTemplateCallsCache(null);
    }

    @Test public void test_pt_verb_form_of() throws Exception {
        reusableWikiModel.setPageName("rape");
        wikiModel.setPageName("rape");
        String test = "{{pt-verb-form-of|rapar}}";
        assertThat(reusableWikiModel.render(new PlainTextConverter(), test).trim())
                .isEqualTo(wikiModel.render(new PlainTextConverter(), test).trim());
    }

    @Test public void test_contraction_of() throws Exception {
        String test = "{{contraction of|[[de]] [[o]]||[[of]] [[the]]|lang=pt}}";
        assertThat(reusableWikiModel.render(new PlainTextConverter(), test).trim())
                .isEqualTo(wikiModel.render(new PlainTextConverter(), test).trim());
    }

    @Test public void test_pt_verb() throws Exception {
        assertThat(reusableWikiModel.render(new PlainTextConverter(), "{{pt-verb|cant|ar}}").trim())
                .isEqualTo("cantar (first-person singular present indicative canto, past participle cantado)");
    }

    @Test public void test_pt_conj() throws Exception {
        assertThat(reusableWikiModel.render(new HTMLConverter(), "{{pt-conj|cant|ar}}").trim())
                .isXmlEqualToContentOf(new File(getClass().getResource("/expected/pt-conj-cantar.html").getFile()));
    }

    @Test public void test_ja_r() throws Exception {
        assertThat(reusableWikiModel.render(new HTMLConverter(), "{{ja-r|屏風|びょうぶ|gloss=folding screen}}").trim())
                .isEqualTo("<p><span class=\"Jpan\" lang=\"ja\"><a href=\"http://www.bliki.info/wiki/%E5%B1%8F%E9%A2%A8#Japanese\" title=\"屏風\"><span style=\"font-size: 1.2em\"><ruby>屏風<rp> (</rp><rt>びょうぶ</rt><rp>)</rp></ruby></span></a></span> (<span class=\"tr\"><i>byōbu</i></span>, <span class=\"mention-gloss-double-quote\">“</span><span class=\"mention-gloss\">folding screen</span><span class=\"mention-gloss-double-quote\">”</span>)</p>");
    }

    // FIXME BUG still to be fixed. Code is here to assess reported bug #55
    @Ignore
    @Test public void test_langname() throws Exception {
        assertThat(reusableWikiModel.render(new HTMLConverter(), "Requests for example sentences in {{langname|en}}").trim())
            .isEqualTo("<p>Requests for example sentences in English</p>");
    }

    @Test public void test_getCurrentTitle() throws Exception {
        reusableWikiModel.setPageName("first");

        assertThat(reusableWikiModel.render(new PlainTextConverter(), "{{testtitle}}").trim())
                .isEqualTo("Current Title = first");

        reusableWikiModel.setPageName("second");
        assertThat(reusableWikiModel.render(new PlainTextConverter(), "+ {{testtitle}}").trim())
                .isEqualTo("+ Current Title = second");
    }


}

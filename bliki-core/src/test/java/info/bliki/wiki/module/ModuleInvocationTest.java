package info.bliki.wiki.module;

import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.filter.WikiTestModel;
import info.bliki.wiki.model.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class ModuleInvocationTest {

    protected LuaModuleModel enWikiModel = null;
    protected LuaModuleModel esWikiModel = null;
    protected LuaModuleModel itWikiModel = null;
    protected LuaModuleModel deWikiModel = null;


    @Before
    public void setUp() throws Exception {
        enWikiModel = new LuaModuleModel(new Configuration(), Locale.ENGLISH, "http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
        enWikiModel.setUp();
        esWikiModel = new LuaModuleModel(new Configuration(), Locale.forLanguageTag("es"), "http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
        esWikiModel.setUp();
        itWikiModel = new LuaModuleModel(new Configuration(), Locale.ITALIAN, "http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
        itWikiModel.setUp();
        deWikiModel = new LuaModuleModel(new Configuration(), Locale.GERMAN, "http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
        deWikiModel.setUp();
    }

    @Test public void testModule4EnglishLocale() throws IOException {
        assertThat(enWikiModel.render(new PlainTextConverter(), "{{test|arg1}}").trim())
                .isEqualTo("yes");
    }

    @Test public void testModule4SpanishLocale() throws IOException {
        assertThat(esWikiModel.render(new PlainTextConverter(), "{{test|arg1}}").trim())
                .isEqualTo("yes");
    }

    @Test public void testModule4ItalianLocale() throws IOException {
        assertThat(itWikiModel.render(new PlainTextConverter(), "{{test|arg1}}").trim())
                .isEqualTo("yes");
    }

    @Test public void testModule4GermanLocale() throws IOException {
        assertThat(deWikiModel.render(new PlainTextConverter(), "{{test|arg1}}").trim())
                .isEqualTo("yes");
    }

}

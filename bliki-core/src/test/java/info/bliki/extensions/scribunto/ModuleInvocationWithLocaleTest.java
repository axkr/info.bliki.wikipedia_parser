package info.bliki.extensions.scribunto;

import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ModuleInvocationWithLocaleTest {
    private LuaModuleModel enWikiModel;
    private LuaModuleModel esWikiModel;
    private LuaModuleModel itWikiModel;
    private LuaModuleModel deWikiModel;

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

    @Test public void testModuleEnglishLocale() throws IOException {
        assertThat(enWikiModel.render(new PlainTextConverter(), "{{test|arg1}}").trim())
                .isEqualTo("yes");
    }

    @Test public void testModuleSpanishLocale() throws IOException {
        assertThat(esWikiModel.render(new PlainTextConverter(), "{{test|arg1}}").trim())
                .isEqualTo("yes");
    }

    @Test public void testModuleItalianLocale() throws IOException {
        assertThat(itWikiModel.render(new PlainTextConverter(), "{{test|arg1}}").trim())
                .isEqualTo("yes");
    }

    @Test public void testModuleGermanLocale() throws IOException {
        assertThat(deWikiModel.render(new PlainTextConverter(), "{{test|arg1}}").trim())
                .isEqualTo("yes");
    }

    /**
     * Wiki model implementation which allows some special JUnit tests for template
     * parser functions
     */
    private static class LuaModuleModel extends WikiModel {
        private static final String CONCAT = "{{{1|}}}{{{2|}}}{{{3|}}}{{{4|}}}{{{5|}}}{{{6|}}}{{{7|}}}{{{8|}}}{{{9|}}}{{{10|}}}";

        public LuaModuleModel(Configuration configuration, Locale locale, String imageBaseURL, String linkBaseURL) {
            super(configuration, locale, imageBaseURL, linkBaseURL);
        }

        private static final String func = "local p = {}\n\nfunction p.test()\n" +
                "\treturn '@@RET@@'\n" +
                "end\n\n" +
                "return p\n";
        private static final String yes = func.replaceAll("@@RET@@", "yes");
        private static final String no = func.replaceAll("@@RET@@", "no");

        /**
         * Add templates: &quot;Test&quot;, &quot;Templ1&quot;, &quot;Templ2&quot;,
         * &quot;Include Page&quot;
         *
         */
        @Override
        public String getRawWikiContent(ParsedPageName parsedPagename, Map<String, String> map) throws WikiModelContentException {
            String result = super.getRawWikiContent(parsedPagename, map);
            if (result != null) {
                return result;
            }
            if (parsedPagename.namespace.isType(INamespace.NamespaceCode.TEMPLATE_NAMESPACE_KEY) &&
                    parsedPagename.pagename.equals("test")) {
                return "{{#invoke:test|test|arg}}";
            }
            if (parsedPagename.namespace.isType(INamespace.NamespaceCode.MODULE_NAMESPACE_KEY) &&
                    parsedPagename.pagename.equals("test")) {
                if (getLocale().getISO3Language().equals("ita")) {
                    return parsedPagename.fullPagename().startsWith("Modulo:") ? yes : no;
                } else if (getLocale().getISO3Language().equals("fra") || getLocale().getISO3Language().equals("eng")) {
                    return parsedPagename.fullPagename().startsWith("Module:") ? yes : no;
                } else if (getLocale().getISO3Language().equals("spa") ) {
                    return parsedPagename.fullPagename().startsWith("M\u00F3dulo:") ? yes : no;
                } else if (getLocale().getISO3Language().equals("deu") ) {
                    return parsedPagename.fullPagename().startsWith("Modul:") ? yes : no;
                } else {
                    return no;
                }
            }
            String name = encodeTitleToUrl(parsedPagename.pagename, true);
            if (name.equals("Concat")) {
                return CONCAT;
            }
            return null;
        }
    }
}

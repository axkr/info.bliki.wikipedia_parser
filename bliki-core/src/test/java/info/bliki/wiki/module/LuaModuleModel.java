package info.bliki.wiki.module;

import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.model.AbstractWikiModel;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.template.extension.AttributeRenderer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

/**
 * Wiki model implementation which allows some special JUnit tests for template
 * parser functions
 *
 */
public class LuaModuleModel extends WikiModel {
    final static String CONCAT = "{{{1|}}}{{{2|}}}{{{3|}}}{{{4|}}}{{{5|}}}{{{6|}}}{{{7|}}}{{{8|}}}{{{9|}}}{{{10|}}}";

    /**
     * Add German namespaces to the wiki model
     *
     * @param configuration
     * @param locale
     * @param imageBaseURL
     * @param linkBaseURL
     */
    public LuaModuleModel(Configuration configuration, Locale locale, String imageBaseURL, String linkBaseURL) {
        super(configuration, locale, imageBaseURL, linkBaseURL);
    }

    static final String func = "local p = {}\n\nfunction p.test()\n" +
            "\treturn '@@RET@@'\n" +
            "end\n\n" +
            "return p\n";
    static final String yes = func.replaceAll("@@RET@@", "yes");
    static final String no = func.replaceAll("@@RET@@", "no");

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

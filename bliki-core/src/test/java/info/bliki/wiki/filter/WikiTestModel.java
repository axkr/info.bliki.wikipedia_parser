package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.AbstractParser.ParsedPageName;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace.NamespaceCode;
import info.bliki.wiki.tags.IgnoreTag;
import info.bliki.wiki.tags.extension.ChartTag;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Wiki model implementation which allows some special JUnit tests with
 * namespaces and predefined templates
 */
public class WikiTestModel extends WikiModel {
    private static final String MAIN_FOO     = "BAR";
    private static final String FOODATE      = "FOODATE";
    private boolean fSemanticWebActive;

    static {
        TagNode.addAllowedAttribute("style");
        Configuration.DEFAULT_CONFIGURATION.addUriScheme("tel");
        Configuration.DEFAULT_CONFIGURATION.addInterwikiLink("intra", "/${title}");
        Configuration.DEFAULT_CONFIGURATION.addTokenTag("chart", new ChartTag());
        Configuration.DEFAULT_CONFIGURATION.addTokenTag("inputbox", new IgnoreTag("inputbox"));
        Configuration.DEFAULT_CONFIGURATION.addTokenTag("imagemap", new IgnoreTag("imagemap"));
    }

    /**
     * Add German namespaces to the wiki model
     */
    public WikiTestModel(Locale locale, String imageBaseURL, String linkBaseURL) {
        super(Configuration.DEFAULT_CONFIGURATION, locale, imageBaseURL, linkBaseURL);
        // set up a simple cache mock-up for JUnit tests. HashMap is not usable for
        // production!
        Configuration.DEFAULT_CONFIGURATION.setTemplateCallsCache(new HashMap<String, String>());

        fSemanticWebActive = false;
        // add the German image namespace as an alias
        fNamespace.getImage().addAlias("Bild");
    }

    @Override
    public String getRawWikiContent(ParsedPageName parsedPagename, Map<String, String> map) throws WikiModelContentException {
        String result = super.getRawWikiContent(parsedPagename, map);
        if (result != null) {
            // found magic word template
            return result;
        }
        String name = encodeTitleToUrl(parsedPagename.pagename, true);
        if (parsedPagename.namespace.isType(NamespaceCode.TEMPLATE_NAMESPACE_KEY)) {
            switch (name) {
                case FOODATE: return "FOO" + System.currentTimeMillis();
                default:      return loadTemplateResource(name);
            }
        } else if (parsedPagename.namespace.isType(NamespaceCode.MAIN_NAMESPACE_KEY)) {
            if (name.equals("Include_Page")) {
                return "an include page";
            } else if (name.equals("FOO")) {
                return MAIN_FOO;
            }
        }
        return null;
    }

    @Override
    public boolean isSemanticWebActive() {
        return fSemanticWebActive;
    }

    @Override
    public void setSemanticWebActive(boolean semanticWeb) {
        this.fSemanticWebActive = semanticWeb;
    }

    @Override
    public boolean showSyntax(String tagName) {
        return true;
    }

    @Override
    public void appendExternalLink(String uriSchemeName, String link, String linkName, boolean withoutSquareBrackets) {
        if (uriSchemeName.equalsIgnoreCase("tel")) {
            // example for a telephone link
            link = Utils.escapeXml(link, true, false, false);
            TagNode aTagNode = new TagNode("a");
            aTagNode.addAttribute("href", link, true);
            aTagNode.addAttribute("class", "telephonelink", true);
            aTagNode.addAttribute("title", link, true);
            if (withoutSquareBrackets) {
                append(aTagNode);
                aTagNode.addChild(new ContentToken(linkName));
            } else {
                String trimmedText = linkName.trim();
                if (trimmedText.length() > 0) {
                    pushNode(aTagNode);
                    WikipediaParser.parseRecursive(trimmedText, this, false, true);
                    popNode();
                }
            }
            return;
        }
        super.appendExternalLink(uriSchemeName, link, linkName, withoutSquareBrackets);
    }

    private String loadTemplateResource(String name) {
        if (name.trim().length() == 0) {
            return null;
        }

        try (InputStream is = getClass().getResourceAsStream(resourceNameFromTemplateName(name))) {
            return is == null ? null : IOUtils.toString(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String resourceNameFromTemplateName(String name) {
        return "/templates/wikitestModel/" + name
                .replace(" ", "_")
                .replace("/", "_");
    }
}

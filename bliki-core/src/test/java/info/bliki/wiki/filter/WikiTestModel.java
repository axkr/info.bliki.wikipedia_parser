package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
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
    private boolean semanticWebActive;
    private final String resourceBase;

    static {
        TagNode.addAllowedAttribute("style");
        Configuration.DEFAULT_CONFIGURATION.addUriScheme("tel");
        Configuration.DEFAULT_CONFIGURATION.addInterwikiLink("intra", "/${title}");
        Configuration.DEFAULT_CONFIGURATION.addTokenTag("chart", new ChartTag());
        Configuration.DEFAULT_CONFIGURATION.addTokenTag("inputbox", new IgnoreTag("inputbox"));
        Configuration.DEFAULT_CONFIGURATION.addTokenTag("imagemap", new IgnoreTag("imagemap"));
    }

    public WikiTestModel(Locale locale, String imageBaseURL, String linkBaseURL, String resourceBase) {
        super(Configuration.DEFAULT_CONFIGURATION, locale, imageBaseURL, linkBaseURL);
        Configuration.DEFAULT_CONFIGURATION.setTemplateCallsCache(new HashMap<String, String>());
        this.semanticWebActive = false;
        this.resourceBase = resourceBase;
        fNamespace.getImage().addAlias("Bild");
    }

    @Override public String getRawWikiContent(ParsedPageName parsedPagename, Map<String, String> map) throws WikiModelContentException {
        String result = super.getRawWikiContent(parsedPagename, map);
        if (result != null) {
            return result;
        }
        String name = encodeTitleToUrl(parsedPagename.pagename, true);
        if (parsedPagename.namespace.isType(NamespaceCode.TEMPLATE_NAMESPACE_KEY)) {
            switch (name) {
                case FOODATE: return "FOO" + System.currentTimeMillis();
                default:
//                    System.err.println("loading template "+name);
                    return loadTemplateResource(name);
            }
        } if (parsedPagename.namespace.isType(NamespaceCode.MODULE_NAMESPACE_KEY)) {
            return loadModuleResource(name);
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
        return semanticWebActive;
    }

    @Override
    public void setSemanticWebActive(boolean semanticWeb) {
        this.semanticWebActive = semanticWeb;
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
        return loadResource(resourceNameFromTemplateName(name));
    }

    private String loadModuleResource(String name) {
        return loadResource(resourceNameFromModuleName(name));
    }

    private String loadResource(String name) {
        if (name == null) {
            return null;
        }

        try (InputStream is = getClass().getResourceAsStream(name)) {
            return is == null ? null : IOUtils.toString(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String resourceNameFromTemplateName(String name) {
        return getResource("templates", name, null);
    }
    private String resourceNameFromModuleName(String name) {
        return getResource("modules", name, "lua");
    }

    private String getResource(String type, String name, String ext) {
        if (name.trim().length() == 0) {
            return null;
        } else {
            return "/" + resourceBase + "/" + type + "/" + name
                    .replace(" ", "_")
                    .replace("/", "_") + (ext != null ? ("." + ext) : "");
        }
    }
}

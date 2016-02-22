package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.model.WikiModelContentException;
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
    private static final String MAIN_FOO = "BAR";
    private static final String FOODATE = "FOODATE";
    private boolean semanticWebActive;
    private final String resourceBase;
    private boolean debug;

    static {
        TagNode.addAllowedAttribute("style");
    }

    private static Configuration getTestConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addUriScheme("tel");
        configuration.addInterwikiLink("intra", "/$1");
        configuration.addTokenTag("chart", new ChartTag());
        configuration.addTokenTag("inputbox", new IgnoreTag("inputbox"));
        configuration.addTokenTag("imagemap", new IgnoreTag("imagemap"));
        configuration.setTemplateCallsCache(new HashMap<String, String>());
        return configuration;
    }

    public WikiTestModel(Locale locale, String imageBaseURL, String linkBaseURL, String resourceBase) {
        super(getTestConfiguration(), locale, imageBaseURL, linkBaseURL);
        this.semanticWebActive = false;
        this.resourceBase = resourceBase;
        fNamespace.getImage().addAlias("Bild");
    }

    @Override public String getRawWikiContent(ParsedPageName page, Map<String, String> map) throws WikiModelContentException {
        if (debug) {
            logger.error("getRawWikiContent("+page+")");
        }
        String result = super.getRawWikiContent(page, map);
        if (result != null) {
            return result;
        }
        final String name = encodeTitleToUrl(page.pagename, false);
        switch(page.namespace.getCode()) {
            case TEMPLATE_NAMESPACE_KEY:
                switch (name) {
                    case FOODATE: return "FOO" + System.currentTimeMillis();
                    default     : return loadTemplateResource(name);
                }
            case MODULE_NAMESPACE_KEY:   return loadModuleResource(name);
            case MAIN_NAMESPACE_KEY:
                switch (name) {
                    case "Include_Page": return "an include page";
                    case "FOO":          return MAIN_FOO;
                }
            default: {
                return null;
            }
        }
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
        if (debug) {
            logger.error("loading "+name);
        }
        try (InputStream is = getClass().getResourceAsStream(name)) {
            return is == null ? null : IOUtils.toString(is);
        } catch (IOException e) {
            logger.error("error loading "+name, e);
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
            return String.format("/%s/%s/%s%s",
                resourceBase,
                type,
                name.replaceAll("[ /]", "_"),
                ext != null ? ("." + ext) : "");
        }
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}

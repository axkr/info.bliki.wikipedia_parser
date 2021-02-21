package info.bliki.wiki.model;

import info.bliki.extensions.scribunto.template.Invoke;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.tags.*;
import info.bliki.wiki.tags.code.*;
import info.bliki.wiki.template.*;
import info.bliki.wiki.template.extension.DollarContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Common Configuration settings
 */
public class ReusableConfiguration extends Configuration {

    /* An instance template cache that may be used but is not shared with other configurations */
    private Map<String, String> fTemplateCache = null;

    public ReusableConfiguration() {
        this(DEFAULT_WIKI_ID, Casing.FirstLetter);
    }

    public ReusableConfiguration(String wikiId, Casing casing) {
        super(wikiId, casing);
    }

    @Override
    public Map<String, String> getTemplateCallsCache() {
        return fTemplateCache;
    }

    @Override
    public void setTemplateCallsCache(Map<String, String> map) {
        fTemplateCache = map;
    }

}

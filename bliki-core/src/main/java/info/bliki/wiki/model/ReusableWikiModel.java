package info.bliki.wiki.model;

import info.bliki.extensions.scribunto.engine.ScribuntoEngine;
import info.bliki.extensions.scribunto.engine.lua.CompiledScriptCache;
import info.bliki.extensions.scribunto.engine.lua.ReusableScribuntoLuaEngine;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.namespaces.Namespace;

import java.util.*;

/**
 * Standard model implementation for the Wikipedia syntax.
 */
public abstract class ReusableWikiModel extends WikiModel {

    private CompiledScriptCache compiledScriptCache = new CompiledScriptCache();
    public ReusableWikiModel(String imageBaseURL, String linkBaseURL) {
        this(new ReusableConfiguration(), imageBaseURL, linkBaseURL);
    }

    public ReusableWikiModel(ReusableConfiguration configuration, String imageBaseURL, String linkBaseURL) {
        super(configuration, imageBaseURL, linkBaseURL);
    }

    public ReusableWikiModel(ReusableConfiguration configuration, Locale locale, String imageBaseURL, String linkBaseURL) {
        super(configuration, locale, imageBaseURL, linkBaseURL);
    }

    public ReusableWikiModel(Configuration configuration, Locale locale, INamespace namespace, String imageBaseURL, String linkBaseURL) {
        super(configuration, locale, namespace, imageBaseURL, linkBaseURL);
    }

    @Override
    public void setPageName(String pageTitle) {
        fPageTitle = pageTitle;
        // Invalidate all call caches as they may depend on the current pageTitle and caching breaks reusability
        if (null != this.getTemplateCallsCache())
            this.getTemplateCallsCache().clear();
    }

    @Override
    public void setNamespaceName(String namespaceLowercase) {
        super.setNamespaceName(namespaceLowercase);
        // Invalidate all call caches as they may depend on the current pageTitle and caching breaks reusability
        if (null != this.getTemplateCallsCache())
            this.getTemplateCallsCache().clear();
    }

    @Override
    public ScribuntoEngine createScribuntoEngine() {
        return new ReusableScribuntoLuaEngine(this, compiledScriptCache);
    }
}

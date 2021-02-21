package info.bliki.wiki.filter;

import info.bliki.extensions.scribunto.engine.ScribuntoEngine;
import info.bliki.extensions.scribunto.engine.lua.CompiledScriptCache;
import info.bliki.extensions.scribunto.engine.lua.ReusableScribuntoLuaEngine;
import info.bliki.extensions.scribunto.engine.lua.ScribuntoLuaEngine;
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
import java.util.Locale;
import java.util.Map;

/**
 * A Wiki Test Model that do not use template call cache and reuse its Scribunto Lua engine for several pages
 * transliterations.
 */
public class ReusableWikiTestModel extends WikiTestModel {
    private ScribuntoEngine REUSABLE_ENGINE;

    public ReusableWikiTestModel(Locale locale, String imageBaseURL, String linkBaseURL, String resourceBase) {
        super(locale, imageBaseURL, linkBaseURL, resourceBase);
        // disable the template cache
        this.setTemplateCallsCache(null);
    }

    // NEED TO OVERRIDE LUA ENGINE CREATION METHOD

    @Override
    public ScribuntoEngine createScribuntoEngine() {
        if (null == REUSABLE_ENGINE) {
            REUSABLE_ENGINE =
                    new ReusableScribuntoLuaEngine(this, new CompiledScriptCache());;
        }
        return REUSABLE_ENGINE;
    }
}

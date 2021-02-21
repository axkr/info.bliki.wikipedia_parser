package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.extensions.scribunto.engine.lua.interfaces.*;
import info.bliki.wiki.model.IWikiModel;

/**
 * scribunto/engines/LuaCommon/LuaCommon.php
 */
public class ReusableScribuntoLuaEngine extends ScribuntoLuaEngine {

    public ReusableScribuntoLuaEngine(IWikiModel model, CompiledScriptCache cache) {
        super(model, cache);
    }

    public ReusableScribuntoLuaEngine(IWikiModel model, CompiledScriptCache cache, boolean debug) {
        super(model, cache, debug);
    }

    @Override
    protected MwInterface[] getMwInterfaces(IWikiModel model) {
        return new MwInterface[]{
                new MwSite(model),
                new MwUstring(),
                new ReusableMwTitle(model),
                new MwText(),
                new MwUri(),
                new MwMessage(),
                new MwHtml(),
                new MwLanguage(model),
        };
    }

}

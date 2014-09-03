package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.extensions.scribunto.template.Title;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * LuaCommon.php, Scribunto_LuaEngine
 */
public class ScribuntoLuaEngine implements ScribuntoEngine {

    protected static Logger logger = LoggerFactory.getLogger(ScribuntoLuaEngine.class);
    private final IWikiModel wikiModel;
    private final Globals globals;

    public ScribuntoLuaEngine(IWikiModel wikiModel) {
        this.wikiModel = wikiModel;
        this.globals = initializeGlobals();
    }

    /**
     * Load a module from some parser-defined template loading mechanism and
     * register a parser output dependency.
     *
     * Does not initialize the module, i.e. do not expect it to complain if the module
     * text is garbage or has syntax error.
     *
     * @param title The title of the module
     * @return a module or null if it doesn't exist.
     */
    @Override
    public ScribuntoEngineModule fetchModuleFromParser(Title title) {
        logger.debug("fetchModuleFromParser("+title+")");
        try {
            String content = wikiModel.getRawWikiContent(title.parsedPageName(), null);
            return new ScribuntoEngineModule() {
                @Override
                public String invoke(String functionName, Frame frame) {
                    logger.debug("invoke("+functionName+", "+frame+")");
                    return null;
                }
            };
        } catch (WikiModelContentException e) {
            return null;
        }
    }


    private Globals initializeGlobals() {
        final Globals globals = JsePlatform.standardGlobals();
//        LuaJC.install(globals);
        return globals;
    }
}

package info.bliki.extensions.scribunto.template;

import info.bliki.extensions.scribunto.interfaces.MwCommon;
import info.bliki.wiki.model.IWikiModel;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;

public class ModuleExecutor {
    public String run(IWikiModel model, String module, String method, Frame frame) throws IOException {
        final Globals globals = getGlobals();
        MwCommon common = new MwCommon(model, globals);
        return common.execute(module, method, frame);
    }

    private Globals getGlobals() {
        final Globals globals = JsePlatform.standardGlobals();
//        LuaJC.install(globals);
        return globals;
    }
}

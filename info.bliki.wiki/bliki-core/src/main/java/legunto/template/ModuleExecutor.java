package legunto.template;

import legunto.interfaces.MwCommon;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.IOException;

public class ModuleExecutor {
    private final File moduleDir;
    private final File scribuntoDir;

    public ModuleExecutor(File moduleDir) {
        this.moduleDir = moduleDir;
        this.scribuntoDir = new File("scribunto/engines/LuaCommon/lualib");
    }

    public String run(String module, String method, Frame frame) throws IOException {
        final Globals globals = getGlobals();
        MwCommon common = new MwCommon(globals,
            moduleDir,
            scribuntoDir,
            new File(scribuntoDir, "ustring"),
            new File(scribuntoDir, "luabit"));

        return common.execute(module, method, frame);
    }

    private Globals getGlobals() {
        final Globals globals = JsePlatform.standardGlobals();
//        LuaJC.install(globals);

        globals.set("setfenv", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue f, LuaValue env) {
                return f;
            }
        });
        globals.set("gefenv", new OneArgFunction() {
            public LuaValue call(LuaValue f) {
                return globals;
            }
        });
        globals.set("unpack", new unpack());
        return globals;
    }

    static class unpack extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            LuaTable t = args.checktable(1);
            switch (args.narg()) {
                case 1: return t.unpack();
                case 2: return t.unpack(args.checkint(2));
                default: return t.unpack(args.checkint(2), args.checkint(3));
            }
        }
    }

}

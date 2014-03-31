package legunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

public class MwUstring extends MwInterface {
    @Override
    public String name() {
        return "mw.ustring";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("find", find());
        table.set("match", match());
        table.set("gmatch_init", defaultFunction("gmatch_init"));
        table.set("gmatch_callback", defaultFunction("gmatch_init"));
        table.set("gsub", gsub());
        return table;
    }

    private LuaValue match() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue s, LuaValue pattern, LuaValue init) {
                // logger.debug("match(" + s + "," + pattern + ", " + init);

                return NIL;
            }
        };
    }

    private LuaValue find() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue string, LuaValue pattern,
                    LuaValue init) {
                // logger.debug("find(" + string + "," + pattern + ", " + init);
                return NIL;
            }
        };
    }

    private LuaValue gsub() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue s, LuaValue pattern,
                    LuaValue replacement) {
                // logger.debug("gsub("+s+","+pattern+","+replacement);
                return LuaValue.valueOf(s.tojstring().replaceAll(
                        pattern.tojstring(), replacement.tojstring()));
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable table = new LuaTable();
        table.set("stringLengthLimit", 1024 * 10);
        table.set("patternLengthLimit", 1024 * 10);
        return table;
    }
}

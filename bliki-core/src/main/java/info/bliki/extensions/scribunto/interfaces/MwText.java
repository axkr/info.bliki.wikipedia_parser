package info.bliki.extensions.scribunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

public class MwText extends MwInterface {
    @Override
    public String name() {
        return "mw.text";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();

        table.set("unstrip", unstrip());
        table.set("getEntityTable", getEntityTable());
        return table;
    }

    private LuaValue getEntityTable() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return NIL;
            }
        };
    }

    private LuaValue unstrip() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return NIL;
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        return null;
    }
}

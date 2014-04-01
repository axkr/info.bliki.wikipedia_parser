package info.bliki.extensions.scribunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class MwMessage extends MwInterface {
    @Override
    public String name() {
        return "mw.message";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable iface = new LuaTable();
        iface.set("plain", plain());
        iface.set("check", check());
        return iface;
    }

    private LuaValue check() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue what, LuaValue data) {
                return FALSE;
            }
        };
    }

    private LuaValue plain() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue data) {
                return data;
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable options = new LuaTable();

        return options;
    }
}

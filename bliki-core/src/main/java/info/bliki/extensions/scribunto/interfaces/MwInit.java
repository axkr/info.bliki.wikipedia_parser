package info.bliki.extensions.scribunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class MwInit implements MwInterface {
    @Override public String name() {
        return "mwInit";
    }

    @Override public LuaTable getInterface() {
        return null;
    }

    @Override public LuaValue getSetupOptions() {
        return null;
    }
}

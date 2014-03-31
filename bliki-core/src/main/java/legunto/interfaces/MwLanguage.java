package legunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class MwLanguage extends MwInterface {
    @Override
    public String name() {
        return "mw.language";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("isSupportedLanguage", defaultFunction());
        table.set("isKnownLanguageTag", defaultFunction());
        table.set("isValidCode", defaultFunction());
        table.set("isValidBuiltInCode", defaultFunction());
        table.set("fetchLanguageName", defaultFunction());
        table.set("fetchLanguageNames", defaultFunction());
        table.set("getFallbacksFor", defaultFunction());
        table.set("getContLangCode", getContLangCode());
        return table;
    }

    private LuaValue getContLangCode() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf("en");
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        return null;
    }
}

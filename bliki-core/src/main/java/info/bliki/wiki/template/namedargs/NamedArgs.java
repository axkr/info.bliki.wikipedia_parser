package info.bliki.wiki.template.namedargs;

import org.luaj.vm2.LuaTable;

/**
 * LuaTable wrapper for named arguments passed by callParserFunction().
 */
public class NamedArgs {
    private LuaTable luaTable;

    /**
     * Constructor.
     * @param luaTable named arguments passed by callParserFunction().
     */
    public NamedArgs(LuaTable luaTable) {
        this.luaTable = luaTable;
    }

    /**
     * get a listed argument.
     * @param key index.
     * @return stringified value.
     */
    public String getString(int key) {
        return this.luaTable.get(key).checkjstring();
    }

    /**
     * get a named argument.
     * @param key key.
     * @return stringified value.
     */
    public String getString(String key) {
        return this.luaTable.get(key).checkjstring();
    }
}

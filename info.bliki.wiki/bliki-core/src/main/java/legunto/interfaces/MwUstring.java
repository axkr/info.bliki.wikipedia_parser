package legunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;

import java.util.Locale;

public class MwUstring extends MwInterface {
    @Override
    public String name() {
        return "mw.ustring";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("isutf8", defaultFunction("isutf8"));
        table.set("byteoffset", defaultFunction("byteoffset"));
        table.set("codepoint", defaultFunction("codepoint"));
        table.set("toNFC", defaultFunction("toNFC"));
        table.set("toNFD", defaultFunction("toNDF"));
        table.set("char", charFunction());
        table.set("len", len());
        table.set("sub", sub());
        table.set("upper", upper());
        table.set("lower", lower());
        table.set("find", find());
        table.set("match", match());
        table.set("gmatch_init", defaultFunction("gmatch_init"));
        table.set("gmatch_callback", defaultFunction("gmatch_init"));
        table.set("gsub", gsub());
        return table;
    }

    private LuaValue len() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue s) {
                // logger.debug("len(" + s + ")");
                return LuaValue.valueOf(s.tojstring().length());
            }
        };
    }

    private LuaValue lower() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue s) {
                // logger.debug("lower(" + s + ")");
                return LuaValue.valueOf(s.tojstring().toLowerCase(
                        Locale.ENGLISH));
            }
        };
    }

    private LuaValue upper() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue s) {
                // logger.debug("upper(" + s + ")");
                return LuaValue.valueOf(s.tojstring().toUpperCase(
                        Locale.ENGLISH));
            }
        };
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

    private LuaValue charFunction() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue codepoint) {
                // logger.debug("char(" + codepoint + ")");
                return valueOf(new String(Character.toChars(codepoint.toint())));
            }
        };
    }

    private LuaValue sub() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue s, LuaValue start, LuaValue offset) {
                // logger.debug("sub(" + s + "," + start+", "+offset);
                return LuaValue.valueOf(s.tojstring().substring(start.toint(),
                        offset.toint()));
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

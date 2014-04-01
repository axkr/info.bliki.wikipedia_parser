package info.bliki.extensions.scribunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class MwTitle extends MwInterface {
    @Override
    public String name() {
        return "mw.title";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("newTitle", newTitle());
        table.set("makeTitle", makeTitle());
        table.set("getUrl", defaultFunction());
        table.set("getContent", defaultFunction());
        table.set("fileExists", defaultFunction());
        table.set("protectionLevels", defaultFunction());
        return table;
    }

    private LuaValue newTitle() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue text_or_id, LuaValue defaultNamespace) {
                return title(defaultNamespace, text_or_id,
                        LuaValue.valueOf("fragment"),
                        LuaValue.valueOf("interwiki"));
            }
        };
    }

    private LuaValue makeTitle() {
        return new LibFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                // ns, title, fragment, interwiki
                LuaValue ns    = args.arg(1);
                LuaValue title = args.arg(2);
                LuaValue fragment = args.arg(3);
                LuaValue interwiki = args.arg(4);
                return title(ns, title, fragment, interwiki);
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable table = new LuaTable();
        table.set("thisTitle", title());
        table.set("NS_MEDIA", "NS_MEDIA");
        return table;
    }

    private LuaValue title() {
        return title(
            LuaValue.valueOf("default"),
            LuaValue.valueOf("title"),
            LuaValue.valueOf("fragment"),
            LuaValue.valueOf("interwiki"));
    }

    private LuaValue title(LuaValue ns, LuaValue title, LuaValue fragment, LuaValue interwiki) {
        LuaTable table = new LuaTable();
        table.set("isLocal", "");
        table.set("isRedirect", "");
        table.set("subjectNsText", "");
        table.set("interwiki", interwiki);
        table.set("namespace", ns);
        table.set("nsText", "");
        table.set("text", "");
        table.set("id", title);
        table.set("fragment", fragment);
        table.set("contentModel", "");
        table.set("thePartialUrl", "");
        return table;
    }
}

package info.bliki.extensions.scribunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;

public class MwSite extends MwInterface {

    @Override
    public String name() {
        return "mw.site";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("getNsIndex", getNsIndex());
        table.set("pagesInCategory", defaultFunction());
        table.set("pagesInNamespace", defaultFunction());
        table.set("usersInGroup", defaultFunction());
        return table;
    }

    private LuaValue getNsIndex() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                // logger.debug("getNsIndex(" + arg + ")");
                return NIL;
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable table = new LuaTable();
        table.set("stats", new LuaTable());
        table.set("namespaces", namespaces());
        return table;
    }

    private LuaTable namespaces() {
        LuaTable table = new LuaTable();
        table.set("default", ns("Default", true));
        table.set("Module",  ns("Module", true));
        table.set("Module_talk",  ns("Module", false));
        table.set("Special", ns("Special", true));
        return table;
    }

    private LuaTable ns(String name, boolean content) {
        LuaTable ns = new LuaTable();
        ns.set("isContent", LuaValue.valueOf(content));
        ns.set("isTalk",    LuaValue.valueOf(!content));
        ns.set("interwiki", "interwiki");
        LuaTable subject = new LuaTable();
        subject.set("name", "subjectName");
        ns.set("subject", subject);
        ns.set("name", name);
        ns.set("talk", name+"_talk");
        ns.set("id", name+"_id");
        ns.set("aliases", new LuaTable());
        return ns;
    }
}

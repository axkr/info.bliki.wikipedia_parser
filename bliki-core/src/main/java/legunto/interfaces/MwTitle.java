package legunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class MwTitle extends MwInterface {
    @Override
    public String name() {
        return "mw.title";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("newTitle", defaultFunction());
        table.set("makeTitle", defaultFunction());
        table.set("getUrl", defaultFunction());
        table.set("getContent", defaultFunction());
        table.set("fileExists", defaultFunction());
        table.set("protectionLevels", defaultFunction());
        return table;
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable table = new LuaTable();
        table.set("thisTitle", title());
        table.set("NS_MEDIA", "NS_MEDIA");
        return table;
    }

    private LuaValue title() {
        LuaTable table = new LuaTable();
        table.set("isLocal", "");
        table.set("isRedirect", "");
        table.set("subjectNsText", "");
        table.set("interwiki", "");
        table.set("namespace", "default");
        table.set("nsText", "");
        table.set("text", "");
        table.set("id", "");
        table.set("fragment", "");
        table.set("contentModel", "");
        table.set("thePartialUrl", "");
        return table;
    }
}

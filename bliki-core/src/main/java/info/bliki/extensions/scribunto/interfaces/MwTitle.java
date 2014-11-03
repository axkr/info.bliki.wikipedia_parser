package info.bliki.extensions.scribunto.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import static info.bliki.extensions.scribunto.interfaces.MwInterface.DefaultFunction.defaultFunction;
import static info.bliki.wiki.namespaces.INamespace.NamespaceCode.MAIN_NAMESPACE_KEY;
import static info.bliki.wiki.namespaces.INamespace.NamespaceCode.MEDIA_NAMESPACE_KEY;

public class MwTitle implements MwInterface {
    @Override
    public String name() {
        return "mw.title";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("newTitle", newTitle());
        table.set("makeTitle", makeTitle());
        table.set("getUrl", getUrl());
        table.set("getContent", getContent());
        table.set("fileExists", fileExists());
        table.set("protectionLevels", defaultFunction());
        return table;
    }

    private LuaValue getUrl() {

        return new LibFunction() {
            /**
             *  $text, $which, $query = null, $proto = null
             */
            @Override public Varargs invoke(Varargs args) {
                return NIL;
            }
        };
    }

    private LuaValue fileExists() {
        return new OneArgFunction() {
            /**
             * @param page
             * @return Whether the file exists. For File- and Media-namespace titles, this is
             * expensive. It will also be recorded as an image usage for File- and Media-namespace titles.
             */
            @Override public LuaValue call(LuaValue page) {
                return NIL;
            }
        };
    }

    private LuaValue getContent() {
        return new OneArgFunction() {
            /**
             * @param page
             * @return the (unparsed) content of the page, or nil if there is no page.
             * The page will be recorded as a transclusion.
             */
            @Override public LuaValue call(LuaValue page) {
                return NIL;
            }
        };
    }

    private LuaValue newTitle() {
        return new TwoArgFunction() {
            /**
             * Handler for title.new
             *
             * @param text_or_id       string|int Title or page_id to fetch
             * @param defaultNamespace string|int Namespace name or number to use if $text_or_id doesn't override
             * @return array Lua data
             */
            @Override
            public LuaValue call(LuaValue text_or_id, LuaValue defaultNamespace) {

                return title(defaultNamespace,
                        text_or_id,
                        LuaValue.valueOf("fragment"),
                        LuaValue.valueOf("interwiki"));
            }
        };
    }

    private LuaValue makeTitle() {
        /**
         *
         * Creates a title object with title title in namespace namespace, optionally with the
         * specified fragment and interwiki prefix. namespace may be any key found in mw.site.namespaces.
         * If the resulting title is not valid, returns nil.
         * @param $ns           string|int Namespace
         * @param $text         string Title text
         * @param $fragment     string URI fragment
         * @param $interwiki    string Interwiki code
         */
        return new LibFunction() {
            @Override
            public Varargs invoke(Varargs args) {
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
        table.set("NS_MEDIA", MEDIA_NAMESPACE_KEY.code);
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
        table.set("namespace", LuaValue.valueOf(MAIN_NAMESPACE_KEY.code));
        table.set("nsText", "");
        table.set("text", "");
        table.set("id", title);
        table.set("fragment", fragment);
        table.set("contentModel", "");
        table.set("thePartialUrl", "");
        return table;
    }
}

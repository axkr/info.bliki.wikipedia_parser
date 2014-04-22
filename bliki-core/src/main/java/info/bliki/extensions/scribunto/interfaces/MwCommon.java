package info.bliki.extensions.scribunto.interfaces;

import info.bliki.extensions.scribunto.Importer;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.wiki.filter.AbstractParser;
import info.bliki.wiki.filter.AbstractParser.ParsedPageName;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MwCommon extends MwInterface {
    public static final String MODULE_NS = "Module:";
    private static final int MAX_EXPENSIVE_CALLS = 10;

    private final Globals globals;
    private final IWikiModel model;
    private Frame currentFrame;
    private int expensiveFunctionCount;

    private static final String[] LIBRARY_PATH = new String[] {
        ".",
        "luabit",
        "ustring",
    };

    private static final String[] MODULE_PATH = new String[] {
        ".",
        "modules"
    };

    private final MwInterface[] interfaces;

    public MwCommon(IWikiModel model, Globals globals)
            throws IOException {
        this.globals = globals;
        this.model = model;
        extendGlobals(globals);

        this.interfaces = new MwInterface[] {
            new MwSite(model),
            new MwUstring(),
            new MwTitle(),
            new MwText(),
            new MwUri(),
            new MwMessage(),
            new MwHtml(),
            new MwLanguage(),
        };

        load();
    }

    @Override
    public String name() {
        return "mw";
    }

    public String execute(String module, String method, Frame frame) throws IOException {
        String name = module;
        if (!name.startsWith(Importer.MODULE)) {
            name = Importer.MODULE + name;
        }
        LuaValue chunk = globals.load(findModule(module), name, "t", globals).call();
        LuaValue luaFunction = chunk.get(method);
        LuaValue executeFunction = globals.get("mw").get("executeFunction");

        if (luaFunction == null || luaFunction.isnil()) {
            throw new AssertionError("luaFunction is nil");
        }

        try {
            currentFrame = frame;
            return executeFunction.call(luaFunction).tojstring();
        } finally {
            currentFrame = null;
        }
    }

    private void load() throws IOException {
        load(this);
        for (MwInterface iface : interfaces) {
            load(iface);
        }

        stubExecuteModule();
        fakeWikiBase();
    }

    private void stubExecuteModule() {
        // don't need module isolation
        final LuaValue mw = globals.get("mw");
        mw.set("executeModule", new TwoArgFunction() {
            @Override public LuaValue call(LuaValue chunk, LuaValue isConsole) {
                return chunk.call();
            }
        });
    }

    private void fakeWikiBase() {
        // fake http://www.mediawiki.org/wiki/Extension:Wikibase
        final LuaValue mw = globals.get("mw");
        final LuaTable wikibase = new LuaTable();
        wikibase.set("getEntity", new ZeroArgFunction() {
            @Override public LuaValue call() {
                return NIL;
            }
        });
        mw.set("wikibase", wikibase);
    }

    private void load(MwInterface luaInterface) throws IOException {
        // logger.debug("loading interface " + luaInterface);

        LuaValue pkg = globals.loadfile(luaInterface.name()+
                (luaInterface.name().endsWith(".lua") ? "" : ".lua")).call();

        LuaValue setupInterface = pkg.get("setupInterface");

        if (!setupInterface.isnil()) {
            globals.set("mw_interface", luaInterface.getInterface());
            setupInterface.call(luaInterface.getSetupOptions());
        }
    }

    @Override
    public LuaTable getInterface() {
        final LuaTable table = new LuaTable();
        table.set("loadPackage", loadPackage());
        table.set("frameExists", frameExists());
        table.set("newChildFrame", newChildFrame());
        table.set("getExpandedArgument", getExpandedArgument());
        table.set("getAllExpandedArguments", getAllExpandedArguments());
        table.set("getFrameTitle", getFrameTitle());
        table.set("expandTemplate", expandTemplate());
        table.set("callParserFunction", callParserFunction());
        table.set("preprocess", preprocess());
        table.set("incrementExpensiveFunctionCount", incrementExpensiveFunctionCount());
        table.set("isSubsting", isSubsting());
        return table;
    }

    private LuaValue callParserFunction() {
        return new ThreeArgFunction() {
            @Override public LuaValue call(LuaValue frameId, LuaValue function, LuaValue args) {
                return null;
            }
        };
    }

    private LuaValue isSubsting() {
        return new ZeroArgFunction() {
            @Override public LuaValue call() {
                // TODO
                return FALSE;
            }
        };
    }

    private LuaValue incrementExpensiveFunctionCount() {
        return new ZeroArgFunction() {
            @Override public LuaValue call() {
                if (++expensiveFunctionCount > MAX_EXPENSIVE_CALLS) {
                    error("too many expensive function calls");
                }
                return NIL;
            }
        };
    }

    private LuaValue preprocess() {
        return new TwoArgFunction() {
            @Override public LuaValue call(LuaValue frameId, LuaValue text) {
                Frame frame = getFrameById(frameId);

                return valueOf(model.render(text.checkjstring()));
            }
        };
    }

    private LuaValue expandTemplate() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue frameId, LuaValue title, LuaValue args) {
                throw new UnsupportedOperationException("no implemented");
            }
        };
    }

    private LuaValue getExpandedArgument() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue frameId, LuaValue name) {
                return getFrameById(frameId).getArgument(name.tojstring());
            }
        };
    }

    private Frame getFrameById(LuaValue frameId) {
        Frame frame;
        if (frameId.tojstring().equals("parent")) {
            frame = currentFrame.getParent();
        } else {
            frame = currentFrame;
        }

        if (frame == null) {
            throw new AssertionError("No frame set: "+ frameId);
        }
        return frame;
    }

    private LuaValue getFrameTitle() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return valueOf("getFrameTitleNotImplemented");
            }
        };
    }

    private LuaValue getAllExpandedArguments() {
        return new OneArgFunction() {
            @Override public LuaValue call(LuaValue frameId) {
                return getFrameById(frameId).getAllArguments();
            }
        };
    }

    private LuaValue newChildFrame() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue frameId, LuaValue title, LuaValue args) {
                return NIL;
            }
        };
    }

    private LuaValue frameExists() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                // logger.debug("frameExists(" + arg + ")");
                return TRUE;
            }
        };
    }

    private OneArgFunction loadPackage() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                String name = arg.tojstring();

                InputStream is = loadLocally(name);
                if (is == null) {
                    try {
                        is = findModule(name);
                    } catch (IOException ignored) {
                    }
                }
                if (is != null) {
                    try {
                        return globals.load(is, name, "t", globals);
                    } finally {
                        try { is.close(); } catch (IOException ignored) { }
                    }
                } else {
                    return error("Could not load " + name);
                }
            }
        };
    }

    private InputStream loadLocally(String name) {
        for (String path : LIBRARY_PATH) {
            InputStream is = globals.finder.findResource(path+"/"+name+".lua");
            if (is != null) {
                return is;
            }
        }
        if (name.startsWith(MODULE_NS)) {
            return loadLocally(name.substring(MODULE_NS.length()));
        }
        return null;
    }

    private InputStream findModule(final String moduleName) throws IOException {
        final String name = moduleName.replaceAll("[/:]", "_");
        InputStream is = null;

        for (String path : MODULE_PATH) {
            is = globals.finder.findResource(path+"/"+name);
            if (is != null) {
                break;
            }
        }

        if (is != null) {
            return is;
        } else if (model != null) {
            try {
                ParsedPageName pageName = AbstractParser.parsePageName(model,
                        moduleName,
                        model.getNamespace().getModule(), false, false);

                System.err.println("fetching "+pageName);

                String content = model.getRawWikiContent(pageName, null);

                if (content != null) {
                    return new ByteArrayInputStream(content.getBytes());
                }
            } catch (WikiModelContentException ignored) {
            }
        }
        throw new FileNotFoundException("could not find module " + moduleName);
    }

    @Override
    public LuaValue getSetupOptions() {
        return new LuaTable();
    }

    private void extendGlobals(final Globals globals) {
        globals.set("setfenv", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue f, LuaValue env) {
                return f;
            }
        });
        globals.set("gefenv", new OneArgFunction() {
            public LuaValue call(LuaValue f) {
                return globals;
            }
        });
        globals.set("unpack", new unpack());

        // math.log10 got removed in 5.2
        LuaValue math = globals.get("math");
        math.set("log10", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                return valueOf(Math.log10(luaValue.checkdouble()));
            }
        });

        // table.maxn got removed in 5.2
        LuaValue table = globals.get("table");
        table.set("maxn", new OneArgFunction() {
            @Override public LuaValue call(LuaValue arg) {
                // TODO: is this correct?
                return arg.checktable().len();
            }
        });
    }

    private static class unpack extends VarArgFunction {
        public Varargs invoke(Varargs args) {
            LuaTable t = args.checktable(1);
            switch (args.narg()) {
                case 1: return t.unpack();
                case 2: return t.unpack(args.checkint(2));
                default: return t.unpack(args.checkint(2), args.checkint(3));
            }
        }
    }
}

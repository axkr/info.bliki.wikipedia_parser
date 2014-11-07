package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.extensions.scribunto.ScribuntoException;
import info.bliki.extensions.scribunto.engine.ScribuntoEngineBase;
import info.bliki.extensions.scribunto.engine.ScribuntoModule;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwHtml;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwInit;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwInterface;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwLanguage;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwMessage;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwSite;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwText;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwTitle;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwUri;
import info.bliki.extensions.scribunto.engine.lua.interfaces.MwUstring;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.wiki.filter.AbstractParser;
import info.bliki.wiki.filter.AbstractParser.ParsedPageName;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

/**
 * scribunto/engines/LuaCommon/LuaCommon.php
 */
public class ScribuntoLuaEngine extends ScribuntoEngineBase implements MwInterface {
    private static final int MAX_EXPENSIVE_CALLS = 10;

    private final Globals globals;
    private Frame currentFrame;
    private int expensiveFunctionCount;

    private static final String[] LIBRARY_PATH = new String[] {
        "",
        "luabit",
        "ustring",
    };

    private static final String[] MODULE_PATH = new String[] {
        "",
        "modules"
    };

    private final MwInterface[] interfaces;

    public ScribuntoLuaEngine(IWikiModel model) {
        this(model, JsePlatform.standardGlobals());
    }

    private ScribuntoLuaEngine(IWikiModel model, Globals globals) {
        super(model);
        this.globals = globals;
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

        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public ScribuntoModule newModule(String text, String chunkName) {
        return new ScribuntoLuaModule(this, text, chunkName);
    }

    @Override
    public String name() {
        return "mw";
    }

    protected Globals getGlobals() {
        return globals;
    }

    protected LuaValue load(String code, String chunkName) throws ScribuntoException {
        try {
            return globals.load(new StringReader(code), chunkName);
        } catch (LuaError e) {
            throw new ScribuntoException(e);
        }
    }

    protected String executeFunctionChunk(LuaValue luaFunction, Frame frame) {
        assertFunction(luaFunction);
        try {
            currentFrame = frame;
            LuaValue executeFunction = globals.get("mw").get("executeFunction");
            return executeFunction.call(luaFunction).tojstring();
        } finally {
            currentFrame = null;
        }
    }

    private void assertFunction(LuaValue luaFunction) {
        if (luaFunction == null || luaFunction.isnil()) {
            throw new AssertionError("luaFunction is nil");
        }
    }

    private void load() throws IOException {
        load(new MwInit());
        load(this);
        for (MwInterface iface : interfaces) {
            load(iface);
        }

        stubTitleBlacklist();
        stubExecuteModule();
        fakeWikiBase();
    }

    private void stubTitleBlacklist() {
        // TODO move to separate file
        final LuaValue mw = globals.get("mw");
        LuaValue ext = mw.get("ext");
        if (ext.isnil()) {
            ext = new LuaTable();
            mw.set("ext", ext);
        }
        LuaTable blacklist = new LuaTable();
        blacklist.set("test", new TwoArgFunction() {
            @Override public LuaValue call(LuaValue action, LuaValue title) {
                return NIL;
            }
        });
        ext.set("TitleBlacklist", blacklist);
    }

    private void stubExecuteModule() {
        // don't need module isolation
        final LuaValue mw = globals.get("mw");
        mw.set("executeModule", new VarArgFunction() {
            @Override public Varargs invoke(Varargs args) {
                LuaFunction chunk = args.arg(1).checkfunction();
                LuaValue name     = args.arg(2);

                final LuaValue res = chunk.call();

                if (name.isnil()) {
                    return LuaValue.varargsOf(new LuaValue[]{LuaValue.TRUE, res});
                } else {
                    if (!res.istable()) {
                        return LuaValue.varargsOf(new LuaValue[]{LuaValue.FALSE, LuaValue.valueOf(res.typename())});
                    } else {
                        return LuaValue.varargsOf(new LuaValue[]{LuaValue.TRUE, res.checktable().get(name)});
                    }
                }
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
        table.set("loadPHPLibrary", loadPHPLibrary());
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
                return TRUE;
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
                return TRUE;
            }
        };
    }

    private OneArgFunction loadPackage() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                final String name = arg.tojstring();
                final InputStream is = findPackage(name);
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

    private OneArgFunction loadPHPLibrary() {
        return new OneArgFunction() {
            @Override public LuaValue call(LuaValue arg) {
                return LuaValue.NIL;
            }
        };
    }

    private InputStream findPackage(String name) {
        final InputStream is = loadLocally(name);
        if (is != null) {
            return is;
        } else {
            try {
                return findModule(name);
            } catch (IOException ignored) {
                return null;
            }
        }
    }

    private InputStream loadLocally(String name) {
        for (String path : LIBRARY_PATH) {
            InputStream is = globals.finder.findResource(path+"/"+name+".lua");
            if (is != null) {
                return is;
            }
        }

        final String moduleNS = model.getNamespace().getModule().getPrimaryText();
        if (name.startsWith(moduleNS)) {
            return loadLocally(name.substring(moduleNS.length()+1));
        } else {
            return null;
        }
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

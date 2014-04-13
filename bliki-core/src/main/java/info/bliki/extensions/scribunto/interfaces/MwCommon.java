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

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MwCommon extends MwInterface {
    public static final String MODULE_NS = "Module:";

    private final Globals globals;
    private final IWikiModel model;
    private Frame currentFrame;

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
        LuaTable table = new LuaTable();
        table.set("loadPackage", load_package());
        table.set("frameExists", frameExists());
        table.set("newChildFrame", newChildFrame());
        table.set("getExpandedArgument", getExpandedArgument());
        table.set("getAllExpandedArguments", getAllExpandedArguments());
        table.set("getFrameTitle", getFrameTitle());
        table.set("expandTemplate", expandTemplate());
        table.set("callParserFunction", defaultFunction());
        table.set("preprocess", defaultFunction());
        table.set("incrementExpensiveFunctionCount", defaultFunction());
        table.set("isSubstring", defaultFunction());
        return table;
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
                Frame frame = getFrameById(frameId);
                LuaValue argument = frame.getArgument(name.tojstring());
                System.err.println("getExpandedArgument(" + frameId + "," + name + ") => "+argument);

                return argument;
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
                return LuaValue.valueOf("getFrameTitleNotImplemented");
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
                return LuaValue.NIL;
            }
        };
    }

    private LuaValue frameExists() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                // logger.debug("frameExists(" + arg + ")");
                return LuaValue.TRUE;
            }
        };
    }

    private OneArgFunction load_package() {
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
                    return LuaValue.error("Could not load " + name);
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

    private InputStream findModule(String moduleName)
            throws IOException {
        String name = moduleName;
        InputStream is = null;
        name = name.replaceAll("/", "_");
        name = name.replaceAll(":", "_");

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

        LuaValue math = globals.get("math");
        math.set("log10", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                return LuaValue.valueOf(Math.log10(luaValue.checkdouble()));
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

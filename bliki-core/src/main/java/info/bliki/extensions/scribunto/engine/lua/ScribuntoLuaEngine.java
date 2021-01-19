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
import info.bliki.wiki.filter.MagicWord;
import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.ITemplateFunction;
import info.bliki.wiki.template.namedargs.INamedArgsTemplateFunction;
import info.bliki.wiki.template.namedargs.NamedArgs;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import javax.annotation.Nonnull;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static info.bliki.wiki.filter.MagicWord.processMagicWord;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * scribunto/engines/LuaCommon/LuaCommon.php
 */
public class ScribuntoLuaEngine extends ScribuntoEngineBase implements MwInterface {
    private static final int MAX_EXPENSIVE_CALLS = 10;
    private static final boolean ENABLE_LUA_DEBUG_LIBRARY = false;
    private final Globals globals;
    private Frame currentFrame;
    private Map<String,Frame> childFrames = new HashMap<>();
    private int expensiveFunctionCount;

    private final CompiledScriptCache compiledScriptCache;
    private final MwInterface[] interfaces;

    public ScribuntoLuaEngine(IWikiModel model, CompiledScriptCache cache) {
        this(model, cache, ENABLE_LUA_DEBUG_LIBRARY);
    }

    public ScribuntoLuaEngine(IWikiModel model, CompiledScriptCache cache, boolean debug) {
        this(model, cache, debug ? JsePlatform.debugGlobals() : JsePlatform.standardGlobals());
    }

    private ScribuntoLuaEngine(IWikiModel model, CompiledScriptCache compiledScriptCache, Globals globals) {
        super(model);
        this.compiledScriptCache = compiledScriptCache;
        this.globals = globals;
        this.globals.finder = new LuaResourceFinder(globals.finder);
        extendGlobals(globals);

        this.interfaces = new MwInterface[] {
            new MwSite(model),
            new MwUstring(),
            new MwTitle(model),
            new MwText(),
            new MwUri(),
            new MwMessage(),
            new MwHtml(),
            new MwLanguage(model),
        };

        try {
            load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override public ScribuntoModule fetchModuleFromParser(String moduleName) throws ScribuntoException {
        ParsedPageName pageName = pageNameForModule(moduleName, moduleNamespace);

        Prototype prototype = compiledScriptCache.getPrototypeForChunkname(pageName);
        if (prototype == null) {
            try (InputStream is = getRawWikiContentStream(pageName)) {
                prototype = loadAndCache(is, pageName);
            } catch (IOException e) {
                throw new ScribuntoException(e);
            }
        }
        return new ScribuntoLuaModule(this, prototype, pageName);
    }

    @Override
    public String name() {
        return "mw";
    }

    protected Globals getGlobals() {
        return globals;
    }

    protected LuaValue loadFunction(String functionName, Prototype prototype, Frame frame) throws ScribuntoException {
        try {
            currentFrame = frame;
            LuaValue function =  new LuaClosure(prototype, globals).checkfunction().call().get(functionName);
            if (function.isnil()) {
                throw new ScribuntoException("no such function '"+functionName+"'");
            }
            return function;
        } catch (LuaError e) {
            throw new ScribuntoException(e);
        } finally {
            currentFrame = null;
        }
    }

    protected String executeFunctionChunk(LuaValue luaFunction, Frame frame) {
        assertFunction(luaFunction);
        try {
            currentFrame = frame;
            LuaValue executeFunction = globals.get("mw").get("executeFunction");
            logger.trace("executing " + luaFunction);
            final LuaString result = executeFunction.call(luaFunction).checkstring();
            return new String(result.m_bytes, result.m_offset, result.m_length, UTF_8);
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
        stubWikiBase();
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
            @Override
            public LuaValue call(LuaValue action, LuaValue title) {
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
                        return LuaValue.varargsOf(new LuaValue[]{FALSE, toLuaString(res.typename())});
                    } else {
                        return LuaValue.varargsOf(new LuaValue[]{LuaValue.TRUE, res.checktable().get(name)});
                    }
                }
            }
        });
    }

    private void stubWikiBase() {
        // fake https://www.mediawiki.org/wiki/Extension:Wikibase
        final LuaValue mw = globals.get("mw");
        final LuaTable wikibase = new LuaTable();
        wikibase.set("getEntity", new ZeroArgFunction() {
            @Override public LuaValue call() {
                return NIL;
            }
        });
        wikibase.set("getEntityObject", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return NIL;
            }
        });
        wikibase.set("getBestStatements", new TwoArgFunction() {
            @Override public LuaValue call(LuaValue id, LuaValue property) {
                return new LuaTable();
            }
        });
        wikibase.set("sitelink", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue itemId, LuaValue siteId) {
                return NIL;
            }
        });
        mw.set("wikibase", wikibase);
    }

    private void load(MwInterface luaInterface) throws IOException {
        final String filename = fileNameForInterface(luaInterface);

        try (InputStream is = globals.finder.findResource(filename)) {
            if (is == null) {
                throw new FileNotFoundException("could not find '"+filename+"'. Make sure it is on the classpath.");
            }
            final LuaValue pkg = globals.load(is, "@"+filename, "bt", globals).call();
            final LuaValue setupInterface = pkg.get("setupInterface");

            if (!setupInterface.isnil()) {
                globals.set("mw_interface", luaInterface.getInterface());
                setupInterface.call(luaInterface.getSetupOptions());
            }
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

    private String fileNameForInterface(MwInterface luaInterface) {
        return luaInterface.name() + (luaInterface.name().endsWith(".lua") ? "" : ".lua");
    }

    private LuaValue callParserFunction() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue frameId, LuaValue function, LuaValue args) {
                final String functionName = function.checkjstring();

                if (functionName.startsWith("#")) {
                    final ITemplateFunction templateFunction = model.getTemplateFunction(functionName);
                    if (templateFunction != null) {
                        LuaTable arguments = args.checktable();

                        // If the template function supports named arguments,
                        // the function is invoked with a wrapped LuaTable.
                        if (templateFunction instanceof INamedArgsTemplateFunction) {
                            NamedArgs namedArgs = new NamedArgs(arguments);
                            try {
                                final String ret = ((INamedArgsTemplateFunction) templateFunction).parseFunction(
                                        namedArgs,model, new char[0], 0, 0, false);
                                return ret == null ? NIL : LuaString.valueOf(ret);
                            } catch (IOException e) {
                                return NIL;
                            }
                        }

                        // Otherwise case, the function is invoked with only a listed arguments.
                        List<String> parts = new ArrayList<>();
                        for (int i=1; i<=arguments.length(); i++) {
                            parts.add(arguments.get(i).checkjstring());
                        }
                        try {
                            final String ret = templateFunction.parseFunction(parts, model,
                                    new char[0], 0, 0, false);
                            return ret == null ? NIL : LuaString.valueOf(ret);
                        } catch (IOException e) {
                            return NIL;
                        }
                    } else {
                        return NIL;
                    }

                } else {
                    MagicWord.MagicWordE magic = MagicWord.getMagicWord(functionName);
                    if (magic != null) {
                        final LuaTable arguments = args.checktable();
                        final String argument = arguments.get(1).checkjstring();
                        final String processed = processMagicWord(magic, argument, model);
                        return processed == null ? NIL : toLuaString(processed);
                    } else {
                        return NIL;
                    }
                }
            }
        };
    }

    private LuaValue isSubsting() {
        return new ZeroArgFunction() {
            @Override public LuaValue call() {
                return LuaValue.valueOf(getFrameById(toLuaString("current")).isSubsting());
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
                try {
                    return toLuaString(model.render(text.checkjstring()));
                } catch (IOException e) {
                    logger.error("error rendering", e);
                    return LuaValue.NIL;
                }
            }
        };
    }

    private LuaValue expandTemplate() {
        return new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue frameId, LuaValue title, LuaValue args) {
                StringWriter writer = new StringWriter();
                try {
                    model.substituteTemplateCall(title.tojstring(), luaParams(args), writer);
                    return toLuaString(writer.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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

    private Frame getFrameById(LuaValue luaFrameId) {
        assert(currentFrame != null);
        final String frameId = luaFrameId.checkjstring();
        Frame frame = null;
        if (frameId.equals("parent")) {
            frame = currentFrame.getParent();
        } else if (frameId.equals("current")) {
            frame = currentFrame;
        } else if (childFrames.containsKey(frameId)) {
            frame = childFrames.get(frameId);
        }
        if (frame == null) {
            throw new AssertionError("No frame set: "+ luaFrameId);
        }
        return frame;
    }

    private LuaValue getFrameTitle() {
        return new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return toLuaString(getFrameById(arg).getTitle());
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
            /**
             * Creates a new frame
             * @return the new frame id
             */
            @Override
            public LuaValue call(LuaValue frameId, LuaValue title, LuaValue args) {
                final Frame childFrame =
                        currentFrame.newChild(
                            new ParsedPageName(currentFrame.getPage().namespace, title.checkjstring(), true),
                            luaParams(args),
                            currentFrame.isSubsting());

                childFrames.put(childFrame.getFrameId(), childFrame);
                return toLuaString(childFrame.getFrameId());
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
            public LuaValue call(LuaValue packageName) {
                return loadModule(pageNameForModule(packageName.tojstring(), model.getNamespace().getMain()));
            }
        };
    }

    private LuaValue loadModule(ParsedPageName chunkName) throws LuaError {
        Prototype prototype = compiledScriptCache.getPrototypeForChunkname(chunkName);
        if (prototype != null) {
            return new LuaClosure(prototype, globals);
        } else {
            try (InputStream is = findPackage(chunkName)) {
                return new LuaClosure(
                    loadAndCache(is, chunkName),
                    globals);
            } catch (ScribuntoException | IOException e) {
                logger.error("error loading '"+chunkName+"'", e);
                throw new LuaError(e);
            }
        }
    }

    private Prototype loadAndCache(InputStream code, ParsedPageName chunkName) throws ScribuntoException {
        try {
            logger.debug("compiling " + chunkName);
            Prototype prototype = globals.compilePrototype(code, chunkName.fullPagename());
            compiledScriptCache.cachePrototype(chunkName, prototype);

            return prototype;
        } catch (LuaError | IOException e) {
            throw new ScribuntoException(e);
        }
    }

    private OneArgFunction loadPHPLibrary() {
        return new OneArgFunction() {
            @Override public LuaValue call(LuaValue arg) {
                return LuaValue.NIL;
            }
        };
    }

    private @Nonnull InputStream findPackage(ParsedPageName name) throws IOException {
        logger.debug("findPackage("+name+")");
        if (name.namespace == moduleNamespace) {
            return findModule(name);
        } else {
            InputStream is = globals.finder.findResource(name.pagename+".lua");
            if (is != null) {
                return is;
            } else {
                throw new IOException("package "+name+" not found");
            }
        }
    }

    private InputStream findModule(final ParsedPageName moduleName) throws IOException {
        try {
            return getRawWikiContentStream(moduleName);
        } catch (FileNotFoundException e) {
            // fall back to local files
            final String name = moduleName.pagename.replaceAll("[/:]", "_");
            InputStream is = globals.finder.findResource(name+".lua");
            if (is != null) {
                return is;
            } else {
                throw e;
            }
        }
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

        // math.mod was renamed to fmod
        math.set("mod", math.get("modf"));

        // table.maxn got removed in 5.2
        LuaValue table = globals.get("table");
        table.set("maxn", new OneArgFunction() {
            @Override public LuaValue call(LuaValue arg) {
                // TODO: is this correct?
                return arg.checktable().len();
            }
        });

        // table.getn got removed in 5.2
        table.set("getn", new OneArgFunction() {
            @Override public LuaValue call(LuaValue arg) {
                if (arg.isnil()) {
                    return LuaValue.error("bad argument #1 to 'getn' (table expected, got nil)");
                } else {
                    return arg.checktable().len();
                }
            }
        });
    }

    /**
     * Use Java's UTF-8 decoding instead of luaj's broken implementation,
     * since it does not handle surrogates properly.
     */
    public static LuaString toLuaString(String string) {
        return LuaString.valueOf(string.getBytes(UTF_8));
    }

    private static Map<String,String> luaParams(LuaValue args) {
        Map<String,String> parameters = new HashMap<>();
        final LuaTable table = args.checktable();
        LuaValue key = LuaValue.NIL;
        while (true) {
            Varargs next = table.next(key);
            if ((key = next.arg1()).isnil())
                break;

            LuaValue value = next.arg(2);
            parameters.put(key.checkjstring(), value.checkjstring());
        }
        return parameters;
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

    static class LuaResourceFinder implements ResourceFinder {
        private static final String[] LIBRARY_PATH = new String[] {
            "",
            "luabit",
            "ustring",
        };

        private final ResourceFinder delegate;

        LuaResourceFinder(ResourceFinder delegate) {
            this.delegate = delegate;
        }

        @Override
        public InputStream findResource(String filename) {
            for (String path : LIBRARY_PATH) {
                InputStream is = delegate.findResource(path + "/" + filename);
                if (is != null) {
                    return is;
                }
            }
            return null;
        }
    }
}

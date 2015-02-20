package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.extensions.scribunto.ScribuntoException;
import info.bliki.extensions.scribunto.engine.ScribuntoModule;
import info.bliki.extensions.scribunto.template.Frame;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;

public class ScribuntoLuaModule implements ScribuntoModule {
    private static final int SLOW_MODULE_THRESHOLD = 500;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final ScribuntoLuaEngine engine;
    private final String code;
    private final String chunkName;
    private Prototype initChunk;

    public ScribuntoLuaModule(ScribuntoLuaEngine engine, String code, String chunkName) {
        if (engine == null) throw new IllegalArgumentException("engine is null");
        if (code == null) throw new IllegalArgumentException("code is null");
        this.engine = engine;
        this.code = code;
        this.chunkName = chunkName;
    }

    public ScribuntoLuaModule(ScribuntoLuaEngine engine, Prototype initChunk, String chunkName) {
        if (engine == null) throw new IllegalArgumentException("engine is null");
        if (initChunk == null) throw new IllegalArgumentException("initChunk is null");
        this.engine = engine;
        this.code = "<unused>";
        this.chunkName = chunkName;
        this.initChunk = initChunk;
    }

    @Override public String invoke(String functionName, Frame frame) throws ScribuntoException {
        final LuaValue function = loadExportTable().get(functionName);
        if (function.isnil()) {
            throw new ScribuntoException("no such function '"+functionName+"'");
        }

        final long execStart = System.currentTimeMillis();
        final String result = getEngine().executeFunctionChunk(function, frame);
        final long execDuration = System.currentTimeMillis() - execStart;
        logExecution(functionName, execDuration);

        return result;
    }

    @Override public String toString() {
        return "ScribuntoLuaModule{" +
                getChunkName() +
                '}';
    }

    @Override public Status validate() {
        try {
            getInitChunk();
            return Status.OK;
        } catch (ScribuntoException e) {
            return Status.ERROR;
        }
    }

    protected ScribuntoLuaEngine getEngine() {
        return engine;
    }

    protected String getCode() {
        return code;
    }

    protected String getChunkName() {
        return chunkName;
    }

    private LuaValue loadExportTable() throws ScribuntoException {
        try {
            return new LuaClosure(getInitChunk(), getEngine().getGlobals()).checkfunction().call();
        } catch (LuaError e) {
            throw new ScribuntoException(e);
        }
    }

    private Prototype getInitChunk() throws ScribuntoException {
        if (initChunk == null) {
            initChunk = getEngine().load(new StringReader(getCode()), getChunkName());
        }
        return initChunk;
    }

    private void logExecution(String functionName, long execDuration) {
        final boolean slowExecution = execDuration > SLOW_MODULE_THRESHOLD;
        if (slowExecution || logger.isDebugEnabled()) {
            final String message = String.format("execDuration(%s %s):%d ms", toString(), functionName, execDuration);
            if (slowExecution) {
                logger.warn(message);
            } else {
                logger.debug(message);
            }
        }
    }
}

package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.extensions.scribunto.ScribuntoException;
import info.bliki.extensions.scribunto.engine.ScribuntoEngine;
import info.bliki.extensions.scribunto.engine.ScribuntoModuleBase;
import info.bliki.extensions.scribunto.template.Frame;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;

import java.io.StringReader;

public class ScribuntoLuaModule extends ScribuntoModuleBase {
    private Prototype initChunk;

    public ScribuntoLuaModule(ScribuntoEngine engine, String code, String chunkName) {
        super(engine, code, chunkName);
    }

    public ScribuntoLuaModule(ScribuntoLuaEngine engine, Prototype initChunk, String chunkName) {
        super(engine, null, chunkName);
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

        logger.warn("execDuration("+toString()+" "+functionName+"):"+execDuration+ " ms");
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

    @Override protected ScribuntoLuaEngine getEngine() {
        return (ScribuntoLuaEngine) super.getEngine();
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
}

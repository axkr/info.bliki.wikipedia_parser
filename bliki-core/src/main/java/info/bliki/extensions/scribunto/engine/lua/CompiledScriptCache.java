package info.bliki.extensions.scribunto.engine.lua;

import org.luaj.vm2.Prototype;

import java.util.HashMap;
import java.util.Map;

public class CompiledScriptCache {
    private Map<String,Prototype> compileCache = new HashMap<>();

    public static final CompiledScriptCache DONT_CACHE = new CompiledScriptCache() {
        @Override public Prototype getPrototypeForChunkname(String chunkname) {
            return null;
        }
    };

    public Prototype getPrototypeForChunkname(String chunkname) {
        return compileCache.get(chunkname);
    }

    public void cachePrototype(String chunkName, Prototype prototype) {
        compileCache.put(chunkName, prototype);
    }

}

package info.bliki.extensions.scribunto.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ScribuntoModuleBase implements ScribuntoModule {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final ScribuntoEngine engine;
    private final String code;
    private final String chunkName;

    public ScribuntoModuleBase(ScribuntoEngine engine, String code, String chunkName) {
        if (code == null) throw new IllegalArgumentException("code is null");
        if (engine == null) throw new IllegalArgumentException("engine is null");
        this.engine = engine;
        this.code = code;
        this.chunkName = chunkName;
    }

    protected ScribuntoEngine getEngine() {
        return engine;
    }

    protected String getCode() {
        return code;
    }

    protected String getChunkName() {
        return chunkName;
    }
}

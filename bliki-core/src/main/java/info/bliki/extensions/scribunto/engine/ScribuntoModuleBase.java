package info.bliki.extensions.scribunto.engine;

public abstract class ScribuntoModuleBase implements ScribuntoModule {
    private final ScribuntoEngine engine;
    private final String code;
    private final String chunkName;

    public ScribuntoModuleBase(ScribuntoEngine engine, String code, String chunkName) {
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

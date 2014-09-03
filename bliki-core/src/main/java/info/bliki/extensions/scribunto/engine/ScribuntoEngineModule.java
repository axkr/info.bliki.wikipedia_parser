package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.template.Frame;

public interface ScribuntoEngineModule {
    public String invoke(String functionName, Frame frame);
}

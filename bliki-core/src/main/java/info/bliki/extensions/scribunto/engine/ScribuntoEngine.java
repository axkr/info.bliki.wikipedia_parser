package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.template.Title;

public interface ScribuntoEngine {
    ScribuntoEngineModule fetchModuleFromParser(Title title);
}

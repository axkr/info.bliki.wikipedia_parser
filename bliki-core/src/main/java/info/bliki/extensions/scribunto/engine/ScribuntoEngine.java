package info.bliki.extensions.scribunto.engine;

import info.bliki.wiki.filter.ParsedPageName;

import javax.annotation.Nullable;

public interface ScribuntoEngine {
    /**
     * Load a module from some parser-defined template loading mechanism and
     * register a parser output dependency.
     *
     * Does not initialize the module, i.e. do not expect it to complain if the module
     * text is garbage or has syntax error.
     *
     * @param pageName The name of the module
     * @return a module or null if it doesn't exist.
     */
    @Nullable ScribuntoModule fetchModuleFromParser(ParsedPageName pageName);
}

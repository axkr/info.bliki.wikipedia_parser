package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.template.Title;

import javax.annotation.Nullable;

public interface ScribuntoEngine {
    /**
     * Load a module from some parser-defined template loading mechanism and
     * register a parser output dependency.
     *
     * Does not initialize the module, i.e. do not expect it to complain if the module
     * text is garbage or has syntax error.
     *
     * @param title The title of the module
     * @return a module or null if it doesn't exist.
     */
    @Nullable ScribuntoModule fetchModuleFromParser(Title title);
}

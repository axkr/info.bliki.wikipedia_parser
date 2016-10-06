package info.bliki.extensions.scribunto.engine.lua.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class MwHtml implements MwInterface {
    /**
     * string Prefix and suffix for temporary replacement strings
     * for the multipass parser.
     *
     * \x7f should never appear in input as it's disallowed in XML.
     * Using it at the front also gives us a little extra robustness
     * since it shouldn't match when butted up against identifier-like
     * string constructs.
     *
     * Must not consist of all title characters, or else it will change
     * the behavior of <nowiki> in a link.
     *
     * Must have a character that needs escaping in attributes, otherwise
     * someone could put a strip marker in an attribute, to get around
     * escaping quote marks, and break out of the attribute. Thus we add
     * `'".
     */
    private static final String MARKER_SUFFIX = "-QINU`\"'\u007f";
    private static final String MARKER_PREFIX = "\u007f'\"`UNIQ-";

    @Override
    public String name() {
        return "mw.html";
    }

    @Override
    public LuaTable getInterface() {
        return new LuaTable();
    }

    @Override
    public LuaValue getSetupOptions() {
        LuaTable opts = new LuaTable();
        opts.set("uniqPrefix", MARKER_PREFIX);
        opts.set("uniqSuffix", MARKER_SUFFIX);
        return opts;
    }
}

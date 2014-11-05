package info.bliki.extensions.scribunto.engine.lua.interfaces;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import static info.bliki.extensions.scribunto.engine.lua.interfaces.MwInterface.DefaultFunction.defaultFunction;
import static java.util.Locale.forLanguageTag;

public class MwLanguage implements MwInterface {
    private Languages languages = new Languages();

    @Override
    public String name() {
        return "mw.language";
    }

    @Override
    public LuaTable getInterface() {
        LuaTable table = new LuaTable();
        table.set("isSupportedLanguage", defaultFunction());
        table.set("isKnownLanguageTag", isKnownLanguageTag());
        table.set("isValidCode", defaultFunction());
        table.set("isValidBuiltInCode", defaultFunction());
        table.set("fetchLanguageName", fetchLanguageName());
        table.set("fetchLanguageNames", fetchLanguageNames());
        table.set("getFallbacksFor", defaultFunction());
        table.set("getContLangCode", getContLangCode());
        table.set("lc", lc());
        table.set("uc", uc());
        /*
        // TODO
        lcfirst
        ucfirst
        caseFold
        formatNum
        formatDate
        formatDuration
        getDurationIntervals
        convertPlural
        convertGrammar
        gender
        */
        return table;
    }

    private LuaValue lc() {
        return new TwoArgFunction() {
            @Override public LuaValue call(LuaValue code, LuaValue string) {
                return valueOf(string.checkjstring().toLowerCase(forLanguageTag(code.checkjstring())));
            }
        };
    }

    private LuaValue uc() {
        return new TwoArgFunction() {
            @Override public LuaValue call(LuaValue code, LuaValue string) {
                return valueOf(string.checkjstring().toUpperCase(forLanguageTag(code.checkjstring())));
            }
        };
    }

    public LuaValue isKnownLanguageTag() {
        return new OneArgFunction() {
            @Override public LuaValue call(LuaValue code) {
                return LuaValue.TRUE;
            }
        };
    }

    private LuaValue fetchLanguageNames() {
        return new TwoArgFunction() {
            /**
             * Get an array of language names, indexed by code.
             *
             * @param inLanguage null|string: Code of language in which to return the names
             *                   Use null for autonyms (native names)
             * @param include    string:
             *                   'all' all available languages
             *                   'mw' only if the language is defined in MediaWiki or wgExtraLanguageNames (default)
             *                   'mwfile' only if the language is in 'mw' *and* has a message file
             * @return array: language code => language name
             */
            @Override public LuaValue call(LuaValue inLanguage, LuaValue include) {
                return new LuaTable();
            }
        };
    }

    private LuaValue fetchLanguageName() {
        return new TwoArgFunction() {
            /**
             * @param code string: The code of the language for which to get the name
             * @param inLanguage null|string: Code of language in which to return the name (null for autonyms)
             * @return string: Language name or empty
             */
            @Override public LuaValue call(LuaValue code, LuaValue inLanguage) {
                return valueOf(languages.getName(code.checkjstring(), inLanguage.optjstring(null)));
            }
        };
    }

    private LuaValue getContLangCode() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return valueOf("en");
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        return null;
    }
}

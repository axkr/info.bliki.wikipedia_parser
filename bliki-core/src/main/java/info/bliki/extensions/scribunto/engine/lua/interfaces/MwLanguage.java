package info.bliki.extensions.scribunto.engine.lua.interfaces;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.dates.PHPDate;
import info.bliki.wiki.template.dates.StringToTime;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

import static info.bliki.extensions.scribunto.engine.lua.ScribuntoLuaEngine.toLuaString;
import static info.bliki.extensions.scribunto.engine.lua.interfaces.MwInterface.DefaultFunction.defaultFunction;
import static java.time.ZoneOffset.UTC;

public class MwLanguage implements MwInterface {
    private Languages languages = new Languages();
    private final IWikiModel wikiModel;
    private final PHPDate phpDate;

    public MwLanguage(IWikiModel wikiModel) {
        this.wikiModel = wikiModel;
        this.phpDate = new PHPDate();
    }

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
        table.set("formatDate", formatDate());
        table.set("lc", lc());
        table.set("uc", uc());
        table.set("lcfirst", lcfirst());
        table.set("ucfirst", ucfirst());
        /*
        // TODO
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

    private LuaValue lcfirst() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue lang, LuaValue string) {
                final Locale locale = getLocale(lang);
                final String input = string.checkjstring();
                switch (input.length()) {
                    case 0: return string;
                    case 1: return toLuaString(input.toLowerCase(locale));
                    default: return toLuaString(input.substring(0, 1).toLowerCase(locale) + input.substring(1));
                }
            }
        };
    }

    private LuaValue ucfirst() {
        return new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue lang, LuaValue string) {
                final Locale locale = getLocale(lang);
                final String input = string.checkjstring();
                switch (input.length()) {
                    case 0: return string;
                    case 1: return toLuaString(input.toUpperCase(locale));
                    default: return toLuaString(input.substring(0, 1).toUpperCase(locale) + input.substring(1));
                }
            }
        };
    }

    protected LuaValue formatDate() {
        return new ThreeArgFunction() {
            @Override public LuaValue call(LuaValue lang, LuaValue format, LuaValue date) {
                // https://php.net/manual/en/function.date.php
                final String formatString = format.checkjstring();
                final String dateString = date.checkjstring();
                final Date time;
                if (dateString.isEmpty()) {
                    time = wikiModel.getCurrentTimeStamp();
                } else {
                    Object parseDate = StringToTime.date(dateString);
                    if (parseDate instanceof Date) {
                        time = (Date) parseDate;
                    } else {
                        throw new LuaError("Could not parse date '"+dateString+"'");
                    }
                }
                return toLuaString(phpDate.format(formatString,
                        ZonedDateTime.ofInstant(time.toInstant(), ZoneId.systemDefault())));
            }
        };
    }

    private LuaValue lc() {
        return new TwoArgFunction() {
            @Override public LuaValue call(LuaValue code, LuaValue string) {
                return toLuaString(string.checkjstring().toLowerCase(getLocale(code)));
            }
        };
    }

    private LuaValue uc() {
        return new TwoArgFunction() {
            @Override public LuaValue call(LuaValue code, LuaValue string) {
                return toLuaString(string.checkjstring().toUpperCase(getLocale(code)));
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
            @Override public LuaValue call(LuaValue code, LuaValue inLanguage) {
                final String name = languages.getName(code.checkjstring(), inLanguage.optjstring(null));
                return name == null ? NIL : toLuaString(name);
            }
        };
    }

    private LuaValue getContLangCode() {
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return toLuaString("en");
            }
        };
    }

    @Override
    public LuaValue getSetupOptions() {
        return null;
    }

    static Locale getLocale(LuaValue value) {
        Locale locale = Locale.forLanguageTag(value.checkjstring());
        if (locale.getLanguage().isEmpty()) {
            LuaValue.error(String.format("language code '%s' is invalid", value.checkstring()));
        }
        return locale;
    }
}

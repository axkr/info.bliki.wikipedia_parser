package info.bliki.wiki.model;

import info.bliki.extensions.scribunto.template.Invoke;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.tags.ATag;
import info.bliki.wiki.tags.BrTag;
import info.bliki.wiki.tags.HTMLBlockTag;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.HrTag;
import info.bliki.wiki.tags.MathTag;
import info.bliki.wiki.tags.NowikiTag;
import info.bliki.wiki.tags.PTag;
import info.bliki.wiki.tags.PreTag;
import info.bliki.wiki.tags.RefTag;
import info.bliki.wiki.tags.ReferencesTag;
import info.bliki.wiki.tags.SourceTag;
import info.bliki.wiki.tags.code.ABAPCodeFilter;
import info.bliki.wiki.tags.code.CSharpCodeFilter;
import info.bliki.wiki.tags.code.GroovyCodeFilter;
import info.bliki.wiki.tags.code.JavaCodeFilter;
import info.bliki.wiki.tags.code.JavaScriptCodeFilter;
import info.bliki.wiki.tags.code.PHPCodeFilter;
import info.bliki.wiki.tags.code.PythonCodeFilter;
import info.bliki.wiki.tags.code.SQLCodeFilter;
import info.bliki.wiki.tags.code.SourceCodeFormatter;
import info.bliki.wiki.tags.code.XMLCodeFilter;
import info.bliki.wiki.template.Anchorencode;
import info.bliki.wiki.template.Expr;
import info.bliki.wiki.template.Formatnum;
import info.bliki.wiki.template.Fullurl;
import info.bliki.wiki.template.ITemplateFunction;
import info.bliki.wiki.template.If;
import info.bliki.wiki.template.Ifeq;
import info.bliki.wiki.template.Iferror;
import info.bliki.wiki.template.Ifexist;
import info.bliki.wiki.template.Ifexpr;
import info.bliki.wiki.template.LC;
import info.bliki.wiki.template.LCFirst;
import info.bliki.wiki.template.Localurl;
import info.bliki.wiki.template.NS;
import info.bliki.wiki.template.NSE;
import info.bliki.wiki.template.Padleft;
import info.bliki.wiki.template.Padright;
import info.bliki.wiki.template.Plural;
import info.bliki.wiki.template.Switch;
import info.bliki.wiki.template.Tag;
import info.bliki.wiki.template.Time;
import info.bliki.wiki.template.Titleparts;
import info.bliki.wiki.template.UC;
import info.bliki.wiki.template.UCFirst;
import info.bliki.wiki.template.URLEncode;
import info.bliki.wiki.template.extension.DollarContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Common Configuration settings
 */
public class Configuration implements IConfiguration {
    /**
     * Get the current <code>Bliki.info Engine</code> version
     */
    public final static String BLIKI_VERSION = "3.1.0-SNAPSHOT";

    /**
     * Print additional debug information to System.out
     */
    public final static boolean DEBUG = false;

    /**
     * Print additional stack trace information to System.out
     */
    public final static boolean STACKTRACE = false;

    /**
     * Print parser function calls (ITemplateFunction calls) with isSubst==false
     */
    public final static boolean PARSER_FUNCTIONS = false;

    /**
     * Print template calls
     *
     * @see AbstractWikiModel#substituteTemplateCall(String, Map, Appendable)
     */
    public final static boolean TEMPLATE_NAMES = false;

    /**
     * Print raw content calls
     *
     * @see IWikiModel#getRawWikiContent(ParsedPageName, Map)
     */
    public final static boolean RAW_CONTENT = false;

    private static final String INTERWIKI_RESOURCE_NAME = "/interwiki.properties";

    public static final  String SPECIAL_BLOCK_TAGS = "|applet|snippet|blockquote|body|button|center|dd|del|div|fieldset|form|iframe|ins|li|map|noframes|noscript|object|td|th|";
    final static HTMLTag HTML_A_OPEN = new ATag();

    public static final  HTMLTag HTML_ABBR_OPEN = new HTMLTag("abbr");
    public static final  HTMLTag HTML_EM_OPEN = new HTMLTag("em");
    public static final  HTMLTag HTML_H1_OPEN = new HTMLBlockTag("h1", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_H2_OPEN = new HTMLBlockTag("h2", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_H3_OPEN = new HTMLBlockTag("h3", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_H4_OPEN = new HTMLBlockTag("h4", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_H5_OPEN = new HTMLBlockTag("h5", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_H6_OPEN = new HTMLBlockTag("h6", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_ITALIC_OPEN = new HTMLTag("i");
    public static final  HTMLTag HTML_BOLD_OPEN = new HTMLTag("b");
    public static final  HTMLTag HTML_PARAGRAPH_OPEN = new PTag();
    public static final  HTMLTag HTML_BLOCKQUOTE_OPEN = new HTMLBlockTag("blockquote", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_STRIKE_OPEN = new HTMLTag("strike");
    public static final  HTMLTag HTML_STRONG_OPEN = new HTMLTag("strong");
    public static final  HTMLTag HTML_UNDERLINE_OPEN = new HTMLTag("u");
    public static final  HTMLTag HTML_SUB_OPEN = new HTMLTag("sub");
    public static final  HTMLTag HTML_SUP_OPEN = new HTMLTag("sup");
    public static final  HTMLTag HTML_CENTER_OPEN = new HTMLBlockTag("center", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_TT_OPEN = new HTMLTag("tt");
    public static final  HTMLTag HTML_TABLE_OPEN = new HTMLBlockTag("table", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_CAPTION_OPEN = new HTMLBlockTag("caption", "|table|");
    public static final  HTMLTag HTML_TH_OPEN = new HTMLBlockTag("th", "|tr|");
    public static final  HTMLTag HTML_TR_OPEN = new HTMLBlockTag("tr", "|table|tbody|tfoot|thead|");
    public static final  HTMLTag HTML_TD_OPEN = new HTMLBlockTag("td", "|tr|");
    public static final  HTMLTag HTML_UL_OPEN = new HTMLBlockTag("ul", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_OL_OPEN = new HTMLBlockTag("ol", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_LI_OPEN = new HTMLBlockTag("li", "|dir|menu|ol|ul|", HTML_UL_OPEN);
    public static final  HTMLTag HTML_FONT_OPEN = new HTMLTag("font");
    public static final  HTMLTag HTML_CITE_OPEN = new HTMLTag("cite");
    public static final  HTMLTag HTML_DIV_OPEN = new HTMLBlockTag("div", SPECIAL_BLOCK_TAGS);
    public static final  HTMLTag HTML_SPAN_OPEN = new HTMLTag("span");
    public static final  HTMLTag HTML_VAR_OPEN = new HTMLTag("var");
    public static final  HTMLTag HTML_CODE_OPEN = new HTMLTag("code");

    // strike-through
    public static final  HTMLTag HTML_S_OPEN = new HTMLTag("s");

    // small
    public static final  HTMLTag HTML_SMALL_OPEN = new HTMLTag("small");
    public static final  HTMLTag HTML_BIG_OPEN = new HTMLTag("big");
    public static final  HTMLTag HTML_DEL_OPEN = new HTMLTag("del");
    public static final  HTMLTag HTML_PRE_OPEN = new PreTag();

    public static final String DEFAULT_WIKI_ID = "enwiki";

    private static Map<String, String> TEMPLATE_CALLS_CACHE = null;

    /**
     * Map from the interwiki shortcut to the real Interwiki-URL
     */
    private static final Map<String, String> INTERWIKI_MAPPING = new HashMap<>();
    private final Map<String, String> interWikiMapping = new HashMap<>();

    /**
     * Map the HTML token string to the corresponding TagToken implementation
     */
    protected static final HashMap<String, TagToken> TAG_TOKEN_MAP = new HashMap<>();
    protected final HashMap<String, TagToken> tagTokenMap = new HashMap<>(TAG_TOKEN_MAP);

    /**
     * Map the source code's language string to the code formatter implementation
     */
    protected static final HashMap<String, SourceCodeFormatter> CODE_FORMATTER_MAP = new HashMap<>();

    /**
     * Map the template's function name to the TemplateFunction implementation
     */
    protected static final Map<String, ITemplateFunction> TEMPLATE_FUNCTION_MAP = new TreeMap<>(
            String.CASE_INSENSITIVE_ORDER);

    /**
     * Allowed URI schemes
     */
    protected static final Set<String> URI_SCHEME_MAP = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    public final static Pattern NOWIKI_OPEN_PATTERN = Pattern.compile("\\<nowiki\\>", Pattern.CASE_INSENSITIVE);
    public final static Pattern NOWIKI_CLOSE_PATTERN = Pattern.compile("\\<\\/nowiki\\>", Pattern.CASE_INSENSITIVE);

    static {
        final Properties mapping = Configuration.loadProperties(INTERWIKI_RESOURCE_NAME);

        URI_SCHEME_MAP.add("http");
        URI_SCHEME_MAP.add("https");
        URI_SCHEME_MAP.add("ftp");

        Enumeration<Object> eKeys = mapping.keys();

        while (eKeys.hasMoreElements()) {
            String key = (String) eKeys.nextElement();
            if (!INTERWIKI_MAPPING.containsKey(key)) {
                INTERWIKI_MAPPING.put(key, mapping.getProperty(key));
            }
        }

        TEMPLATE_FUNCTION_MAP.put("#$", DollarContext.CONST);
        TEMPLATE_FUNCTION_MAP.put("anchorencode", Anchorencode.CONST);
        TEMPLATE_FUNCTION_MAP.put("formatnum", Formatnum.CONST);
        TEMPLATE_FUNCTION_MAP.put("fullurl", Fullurl.CONST);
        TEMPLATE_FUNCTION_MAP.put("ns", NS.CONST);
        TEMPLATE_FUNCTION_MAP.put("nse", NSE.CONST);
        TEMPLATE_FUNCTION_MAP.put("urlencode", URLEncode.CONST);
        TEMPLATE_FUNCTION_MAP.put("lc", LC.CONST);
        TEMPLATE_FUNCTION_MAP.put("uc", UC.CONST);
        TEMPLATE_FUNCTION_MAP.put("localurl", Localurl.CONST);
        TEMPLATE_FUNCTION_MAP.put("lcfirst", LCFirst.CONST);
        TEMPLATE_FUNCTION_MAP.put("ucfirst", UCFirst.CONST);
        TEMPLATE_FUNCTION_MAP.put("padleft", Padleft.CONST);
        TEMPLATE_FUNCTION_MAP.put("padright", Padright.CONST);
        TEMPLATE_FUNCTION_MAP.put("plural", Plural.CONST);
        TEMPLATE_FUNCTION_MAP.put("#expr", Expr.CONST);
        TEMPLATE_FUNCTION_MAP.put("#if", If.CONST);
        TEMPLATE_FUNCTION_MAP.put("#iferror", Iferror.CONST);
        TEMPLATE_FUNCTION_MAP.put("#ifeq", Ifeq.CONST);
        TEMPLATE_FUNCTION_MAP.put("#ifexist", Ifexist.CONST);
        TEMPLATE_FUNCTION_MAP.put("#ifexpr", Ifexpr.CONST);
        TEMPLATE_FUNCTION_MAP.put("#invoke", Invoke.CONST);
        TEMPLATE_FUNCTION_MAP.put("#switch", Switch.CONST);
        TEMPLATE_FUNCTION_MAP.put("#tag", Tag.CONST);
        TEMPLATE_FUNCTION_MAP.put("#time", Time.CONST);
        TEMPLATE_FUNCTION_MAP.put("#timel", Time.CONST);
        TEMPLATE_FUNCTION_MAP.put("#titleparts", Titleparts.CONST);

        CODE_FORMATTER_MAP.put("abap", new ABAPCodeFilter());
        CODE_FORMATTER_MAP.put("csharp", new CSharpCodeFilter());
        CODE_FORMATTER_MAP.put("groovy", new GroovyCodeFilter());
        CODE_FORMATTER_MAP.put("java", new JavaCodeFilter());
        CODE_FORMATTER_MAP.put("javascript", new JavaScriptCodeFilter());
        CODE_FORMATTER_MAP.put("php", new PHPCodeFilter());
        CODE_FORMATTER_MAP.put("python", new PythonCodeFilter());
        CODE_FORMATTER_MAP.put("html4strict", new XMLCodeFilter());
        CODE_FORMATTER_MAP.put("sql", new SQLCodeFilter());
        CODE_FORMATTER_MAP.put("xml", new XMLCodeFilter());

        TAG_TOKEN_MAP.put("br", new BrTag());
        TAG_TOKEN_MAP.put("hr", new HrTag());

        TAG_TOKEN_MAP.put("nowiki", new NowikiTag());
        TAG_TOKEN_MAP.put("pre", HTML_PRE_OPEN);// new PreTag());
        TAG_TOKEN_MAP.put("math", new MathTag());
        // TAG_TOKEN_MAP.put("embed", new EmbedTag());
        TAG_TOKEN_MAP.put("ref", new RefTag());
        TAG_TOKEN_MAP.put("references", new ReferencesTag());

        // see http://www.mediawiki.org/wiki/Extension:SyntaxHighlight_GeSHi
        TAG_TOKEN_MAP.put("syntaxhighlight", new SourceTag());
        TAG_TOKEN_MAP.put("source", new SourceTag());

        TAG_TOKEN_MAP.put("a", HTML_A_OPEN);
        TAG_TOKEN_MAP.put("h1", HTML_H1_OPEN);
        TAG_TOKEN_MAP.put("h2", HTML_H2_OPEN);
        TAG_TOKEN_MAP.put("h3", HTML_H3_OPEN);
        TAG_TOKEN_MAP.put("h4", HTML_H4_OPEN);
        TAG_TOKEN_MAP.put("h5", HTML_H5_OPEN);
        TAG_TOKEN_MAP.put("h6", HTML_H6_OPEN);

        TAG_TOKEN_MAP.put("em", HTML_EM_OPEN);
        TAG_TOKEN_MAP.put("i", HTML_ITALIC_OPEN);
        TAG_TOKEN_MAP.put("b", HTML_BOLD_OPEN);

        TAG_TOKEN_MAP.put("strong", HTML_STRONG_OPEN);
        TAG_TOKEN_MAP.put("u", HTML_UNDERLINE_OPEN);
        TAG_TOKEN_MAP.put("p", HTML_PARAGRAPH_OPEN);

        TAG_TOKEN_MAP.put("blockquote", HTML_BLOCKQUOTE_OPEN);

        TAG_TOKEN_MAP.put("var", HTML_VAR_OPEN);
        TAG_TOKEN_MAP.put("code", HTML_CODE_OPEN);
        TAG_TOKEN_MAP.put("s", HTML_S_OPEN);
        TAG_TOKEN_MAP.put("small", HTML_SMALL_OPEN);
        TAG_TOKEN_MAP.put("big", HTML_BIG_OPEN);
        TAG_TOKEN_MAP.put("del", HTML_DEL_OPEN);

        TAG_TOKEN_MAP.put("sub", HTML_SUB_OPEN);
        TAG_TOKEN_MAP.put("sup", HTML_SUP_OPEN);
        TAG_TOKEN_MAP.put("strike", HTML_STRIKE_OPEN);

        TAG_TOKEN_MAP.put("table", HTML_TABLE_OPEN);
        TAG_TOKEN_MAP.put("th", HTML_TH_OPEN);
        TAG_TOKEN_MAP.put("tr", HTML_TR_OPEN);
        TAG_TOKEN_MAP.put("td", HTML_TD_OPEN);
        TAG_TOKEN_MAP.put("caption", HTML_CAPTION_OPEN);

        TAG_TOKEN_MAP.put("ul", HTML_UL_OPEN);
        TAG_TOKEN_MAP.put("ol", HTML_OL_OPEN);
        TAG_TOKEN_MAP.put("li", HTML_LI_OPEN);

        TAG_TOKEN_MAP.put("font", HTML_FONT_OPEN);
        TAG_TOKEN_MAP.put("center", HTML_CENTER_OPEN);
        TAG_TOKEN_MAP.put("tt", HTML_TT_OPEN);
        TAG_TOKEN_MAP.put("div", HTML_DIV_OPEN);
        TAG_TOKEN_MAP.put("span", HTML_SPAN_OPEN);

        TAG_TOKEN_MAP.put("abbr", HTML_ABBR_OPEN);
        TAG_TOKEN_MAP.put("cite", HTML_CITE_OPEN);
    }

    /**
     * Limits the length of the template cache key to this length.
     */
    public final static int MAX_CACHE_KEY_LENGTH = 256;

    /**
     * Limits the recursive call of the Wikipedia and Template parser to a depth
     * of PARSER_RECURSION_LIMIT
     */
    public final static int PARSER_RECURSION_LIMIT = 256;

    /**
     * Limits the recursive call of the HTMLConverter renderer to a depth of
     * RENDERER_RECURSION_LIMIT
     */
    public final static int RENDERER_RECURSION_LIMIT = 256;

    /**
     * Limits the recursive call of the Template parser to a depth of
     * TEMPLATE_RECURSION_LIMIT
     */
    public final static int TEMPLATE_RECURSION_LIMIT = 256;

    /**
     * Limits the Scanner buffer.
     */
    public final static int TEMPLATE_BUFFER_LIMIT = 524288;

    /**
     * Limits the template parameter value length
     */
    public final static int TEMPLATE_VALUE_LIMIT = 262144;

    /**
     * Tries to avoid page breaks inside tables, e.g. for printouts
     * (disable to get more MediaWiki-compliant HTML code).
     *
     * Note: Only supported by Opera, according to
     * http://www.w3schools.com/cssref/pr_print_pagebi.asp
     */
    public static boolean AVOID_PAGE_BREAK_IN_TABLE = true;

    /**
     * Limits the recursive call of the AbstractParser parser to a depth of
     * GLOBAL_RECURSION_LIMIT
     */
    // public final static int GLOBAL_RECURSION_LIMIT = 100000;


    /**
     * The wiki identifier.
     */
    private final String wikiId;
    private final Casing casing;

    public Configuration() {
        this(DEFAULT_WIKI_ID, Casing.FirstLetter);
    }

    public Configuration(String wikiId, Casing casing) {
        interWikiMapping.putAll(INTERWIKI_MAPPING);
        this.wikiId = wikiId;
        this.casing = casing;
    }

    @Override
    public String addInterwikiLink(String key, String value) {
        return interWikiMapping.put("__global:"+key, "0 " + value);
    }

    /**
     * @return the wikiId, e.g. enwiki, frwiktionary etc.
     */
    public String getWikiId() {
        return wikiId;
    }

    public Map<String, String> getInterWikiMapping() {
        return Collections.unmodifiableMap(interWikiMapping);
    }

    /**
     * Get the set of all allowed URI scheme shortcuts like http, https, ftp,...
     *
     * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
     *
     */
    @Override
    public Set<String> getUriSchemeSet() {
        return URI_SCHEME_MAP;
    }

    /**
     * Add an allowed URI scheme shortcut like http, https, ftp,...
     *
     * See <a href="http://en.wikipedia.org/wiki/URI_scheme">URI scheme</a>
     *
     * @return <code>true</code> if the set did not already contain the specified
     *         URI key.
     */
    public boolean addUriScheme(String uriKey) {
        return URI_SCHEME_MAP.add(uriKey);
    }

    @Override
    public Map<String, ITemplateFunction> getTemplateMap() {
        return TEMPLATE_FUNCTION_MAP;
    }

    @Override
    public ITemplateFunction addTemplateFunction(String key, ITemplateFunction value) {
        return TEMPLATE_FUNCTION_MAP.put(key, value);
    }

    @Override
    public Map<String, String> getTemplateCallsCache() {
        return TEMPLATE_CALLS_CACHE;
    }

    @Override
    public void setTemplateCallsCache(Map<String, String> map) {
        TEMPLATE_CALLS_CACHE = map;
    }

    @Override
    public Casing casing() {
        return casing;
    }

    @Override
    public Map<String, SourceCodeFormatter> getCodeFormatterMap() {
        return CODE_FORMATTER_MAP;
    }

    @Override
    public SourceCodeFormatter addCodeFormatter(String key, SourceCodeFormatter value) {
        return CODE_FORMATTER_MAP.put(key, value);
    }

    @Override
    public Map<String, TagToken> getTokenMap() {
        return tagTokenMap;
    }

    @Override
    public TagToken addTokenTag(String key, TagToken value) {
        return tagTokenMap.put(key, value);
    }

    /**
     * Given a property file name, load the property file and return an object
     * representing the property values.
     *
     * @param propertyFile
     *          The name of the property file to load.
     * @return The loaded SortedProperties object.
     */
    private static Properties loadProperties(String propertyFile) {
        Properties properties = new Properties();
        try (InputStream in = Configuration.class.getResourceAsStream(propertyFile)){
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Configuration.java - Properties file:" + propertyFile + " not found.");
        }
        return properties;
    }
}

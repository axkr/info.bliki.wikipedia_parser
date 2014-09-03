package info.bliki.extensions.scribunto.template;

import info.bliki.api.Connector;
import info.bliki.wiki.filter.AbstractParser;
import info.bliki.wiki.namespaces.INamespace;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Title {
    final String fragment;
    final INamespace.INamespaceValue ns;
    final String dbKeyForm;
    final int articleId;
    final String urlform;
    final String textform;

    public Title(INamespace.INamespaceValue ns, String fragment,
                 String dbKeyForm,
                 int articleId,
                 String urlform,
                 String textform) {
        this.fragment = fragment;
        this.ns = ns;
        this.dbKeyForm = dbKeyForm;
        this.articleId = articleId;
        this.urlform = urlform;
        this.textform = textform;
    }

    public AbstractParser.ParsedPageName parsedPageName() {
        return new AbstractParser.ParsedPageName(this.ns, this.dbKeyForm, true);
    }

    public static Title makeTitleSafe(INamespace.INamespaceValue ns, String moduleName) {
        return makeTitleSafe(ns, moduleName, "", "");
    }

    /**
     * Create a new Title from a namespace index and a DB key.
     * It's assumed that $ns and $title are *valid*, for instance when
     * they came directly from the database or a special page name.
     * For convenience, spaces are converted to underscores so that
     * eg user_text fields can be used directly.
     *
     * @param ns The namespace of the article
     * @param title The unprefixed database key form
     * @param fragment The link fragment (after the "#")
     * @param interwiki The interwiki prefix
     * @return Title The new object
     */
    public static Title makeTitle(INamespace.INamespaceValue ns, String title, String fragment, String interwiki) {
        final String dbKeyForm = title.replace(" ", "_");
        return new Title(ns,
            fragment,
            dbKeyForm,
            ns.getCode().code >= 0 ? -1 : 0,
            urlEncode(dbKeyForm),
            title.replace("_", " "));
    }

    /**
     * Create a new Title from a namespace index and a DB key.
     * The parameters will be checked for validity, which is a bit slower
     * than makeTitle() but safer for user-provided data.
     *
     * @param ns The namespace of the article
     * @param title Database key form
     * @param fragment The link fragment (after the "#")
     * @param interwiki Interwiki prefix
     * @return Title The new object, or null on an error
     */
    public static Title makeTitleSafe(INamespace.INamespaceValue ns, String title, String fragment, String interwiki) {
        // TODO
        return makeTitle(ns, title, fragment, interwiki);
    }

    /**
     * Make a prefixed DB key from a DB key and a namespace index
     *
     * @param ns Numerical representation of the namespace
     * @param title The DB key form the title
     * @param fragment The link fragment (after the "#")
     * @param interwiki The interwiki prefix
     * @return string The prefixed form of the title
     */
    public static String makeName( int ns, String title, String fragment, String interwiki) {
//        global $wgContLang;
//        $namespace = $wgContLang->getNsText( $ns );
//        $name = $namespace == '' ? $title : "$namespace:$title";
//        if ( strval( $interwiki ) != '' ) {
//            $name = "$interwiki:$name";
//        }
//        if ( strval( $fragment ) != '' ) {
//            $name .= '#' . $fragment;
//        }
//        return $name;
        return null;
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, Connector.UTF8_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public String toString() {
        return "Title{" +
                "fragment='" + fragment + '\'' +
                ", ns=" + ns +
                ", dbKeyForm='" + dbKeyForm + '\'' +
                ", articleId=" + articleId +
                ", urlform='" + urlform + '\'' +
                ", textform='" + textform + '\'' +
                '}';
    }
}

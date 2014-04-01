package info.bliki.api.creator;

import info.bliki.api.User;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.impl.APIWikiModel;
import info.bliki.wiki.model.Configuration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

/**
 * Test to load a page, images and templates from en.wikipedia.org and render it
 * into an HTML file. The CSS is always included in the generated HTML text
 * which blows up the size of the HTML file. This is happening by using a local
 * Derby database for caching the templates content.
 *
 * @see InMemoryCreatorExample
 */
public class HTMLCreatorExample {

    private static final String TEXT_ISSUE_96 = "{{Shortcut|WP:ADTDO}}{{Wikipedia:Hauptseite/Artikel des Tages/Bearbeitungshinweise}}\n"
            + "<onlyinclude> {{AdT-Vorschlag\n"
            + "| DATUM = 28.07.2011\n"
            + "| LEMMA = Bhaktapur\n"
            + "| BILD = Datei:Nyatapole2.jpg \n"
            + "| BILDBESCHREIBUNG = Nyata-Tempel, 1708 erbaut, dreißig Meter hoch und der hinduistischen Gottheit Lakshmi geweiht \n"
            + "| BILDGROESSE = 116px \n"
            + "| BILDUMRANDUNG = \n"
            + "| TEASERTEXT = '''[[Bhaktapur]]''' (nepali ??????? ‚Stadt der Frommen‘) oder ''Khwopa'' (newari ???? ''Khvapa'') ist neben Kathmandu und Lalitpur mit über 78.000 Einwohnern die dritte und kleinste der Königsstädte des Kathmandutals in Nepal. Bhaktapur liegt am Fluss Hanumante und wie Kathmandu an einer alten Handelsroute nach Tibet, was für den Reichtum der Stadt verantwortlich war. Das Bild der Stadt wird bestimmt von der Landwirtschaft, der Töpferkunst und besonders von einer lebendigen traditionellen Musikerszene. Wegen seiner über 150 Musik- und 100 Kulturgruppen wird Bhaktapur als Hauptstadt der darstellenden Künste Nepals bezeichnet. Die Einwohner von Bhaktapur gehören ethnisch zu den Newar und zeichnen sich durch einen hohen Anteil von 60 Prozent an Bauern der Jyapu-Kaste aus. Die Bewohner sind zu fast 90 Prozent Hindus und zu zehn Prozent Buddhisten. Vom 14. Jahrhundert bis zur zweiten Hälfte des 18. Jahrhunderts war Bhaktapur Hauptstadt des Malla-Reiches. Aus dieser Zeit stammen viele der 172 Tempelanlagen, der 32 künstlichen Teiche und der mit Holzreliefs verzierten Wohnhäuser. Zwar verursachte ein großes Erdbeben 1934 viele Schäden an den Gebäuden, doch konnten diese wieder so instand gesetzt werden, dass Bhaktapurs architektonisches Erbe bereits seit 1979 auf der UNESCO-Liste des Weltkulturerbes steht.\n"
            + "}} </onlyinclude>\n" + "[[Kategorie:Wikipedia:Hauptseite/Artikel des Tages|Donnerstag]]";

    public HTMLCreatorExample() {
        super();
    }

    public static String testWiktionaryENAPI(String title) {
        return testWikipediaENAPI(title, "http://en.wiktionary.org/w/api.php", Locale.ENGLISH);
    }
    public static String testWikipediaENAPI(String title) {
        return testWikipediaENAPI(title, "http://en.wikipedia.org/w/api.php", Locale.ENGLISH);
    }

    public static void testWikipediaText(String rawWikiText, String title, String apiLink, Locale locale) {
        String[] listOfTitleStrings = { title };
        String titleURL = Encoder.encodeTitleLocalUrl(title);
        User user = new User("", "", apiLink);
        String mainDirectory = System.getProperty("java.io.tmpdir");
        // the following subdirectory should not exist if you would like to create a
        // new database
        String databaseSubdirectory = "WikiDB";
        // the following directory must exist for image downloads
        String imageDirectory = new File(mainDirectory, "WikiImages").getAbsolutePath();
        // the generated HTML will be stored in this file name:
        String generatedHTMLFilename = mainDirectory + titleURL + ".html";

        WikiDB db = null;

        try {
            db = new WikiDB(mainDirectory, databaseSubdirectory);
            APIWikiModel wikiModel = new APIWikiModel(user, db, locale, "${image}", "${title}", imageDirectory);
            DocumentCreator creator = new DocumentCreator(wikiModel, user, listOfTitleStrings);
            // create header and CSS information
            StringBuilder buf = new StringBuilder();
            buf.append(HTMLConstants.HTML_HEADER1);
            buf.append(HTMLConstants.CSS_MAIN_STYLE);
            buf.append(HTMLConstants.CSS_SCREEN_STYLE);
            buf.append(HTMLConstants.HTML_HEADER2);
            creator.setHeader(buf.toString());
            creator.setFooter(HTMLConstants.HTML_FOOTER);
            wikiModel.setUp();
            creator.renderToFile(rawWikiText, title, new HTMLConverter(), generatedHTMLFilename);
            System.out.println("Created file: " + generatedHTMLFilename);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.tearDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get the wiki text throuh the Wikipedia API (i.e. <a
     * href="http://en.wikipedia.org/w/api.php"
     * >http://en.wikipedia.org/w/api.php</a> for the english Wikipedia) and write
     * the generated HTML file to the<code>c:/temp/</code> Windows directory.
     *
     * @param title
     *          the wiki article's title
     * @param apiLink
     *          the link to the Wikipedia API
     * @param locale
     *          the locale (i.e. for english use
     *          <code>java.util.Locale.ENGLISH</code>)
     * @return the redirected link title if a <code>#REDIRECT [[...]]</code> link
     *         is set in the wiki text; <code>null</code> otherwise
     */
    public static String testWikipediaENAPI(String title, String apiLink, Locale locale) {
        String[] listOfTitleStrings = { title };
        String titleURL = Encoder.encodeTitleLocalUrl(title);
        User user = new User("", "", apiLink);
        user.login();
        String mainDirectory =  System.getProperty("java.io.tmpdir");
        // the following subdirectory should not exist if you would like to create a
        // new database
        String databaseSubdirectory = "WikiDB";
        // the following directory must exist for image downloads
        String imageDirectory = new File(mainDirectory, "WikiImages").getAbsolutePath();
        // the generated HTML will be stored in this file name:
        String generatedHTMLFilename = mainDirectory + titleURL + ".html";

        WikiDB db = null;

        try {
            db = new WikiDB(mainDirectory, databaseSubdirectory);
            APIWikiModel wikiModel = new APIWikiModel(user, db, locale, "${image}", "${title}", imageDirectory);
            DocumentCreator creator = new DocumentCreator(wikiModel, user, listOfTitleStrings);
            // create header and CSS information
            StringBuilder buf = new StringBuilder();
            buf.append(HTMLConstants.HTML_HEADER1);
            buf.append(HTMLConstants.CSS_MAIN_STYLE);
            buf.append(HTMLConstants.CSS_SCREEN_STYLE);
            buf.append(HTMLConstants.HTML_HEADER2);
            creator.setHeader(buf.toString());
            creator.setFooter(HTMLConstants.HTML_FOOTER);
            wikiModel.setUp();
            // set up a simple cache for this example. HashMap is not usable for
            // production! Use something like http://ehcache.org for production!
            Configuration.DEFAULT_CONFIGURATION.setTemplateCallsCache(new HashMap<String,String>());
            creator.renderToFile(generatedHTMLFilename);
            System.out.println("Created file: " + generatedHTMLFilename);
            return wikiModel.getRedirectLink();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                try {
                    db.tearDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void testCreator001() {
        testWikipediaENAPI("Tom Hanks");
    }

    public static void testCreator002() {
        testWikipediaENAPI("Political party strength in California");
    }

    public static void testCreator003() {
        testWikipediaENAPI("Chris Capuano");
    }

    public static void testCreator004() {
        testWikipediaENAPI("Protein");
    }

    public static void testCreator005() {
        testWikipediaENAPI("Depeche Mode");
    }

    public static void testCreator006() {
        testWikipediaENAPI("Anarchism");
    }

    public static void testCreator007() {
        testWikipediaENAPI("JavaScript", "http://de.wikipedia.org/w/api.php", Locale.GERMAN);
    }

    public static void testCreator008() {
        testWikipediaENAPI("libero", "http://en.wiktionary.org/w/api.php", Locale.ENGLISH);
    }

    public static void testCreator009() {
        testWikipediaENAPI("Metallica");
    }

    public static void testCreator010() {
        testWikipediaENAPI("HTTP-Statuscode", "http://de.wikipedia.org/w/api.php", Locale.GERMAN);
    }

    public static void testCreator011() {
        testWikipediaENAPI("Main Page", "http://simple.wikipedia.org/w/api.php", Locale.ENGLISH);
    }

    public static void testCreator012() {
        testWikipediaENAPI("Grafenwöhr", "http://bar.wikipedia.org/w/api.php", Locale.GERMAN);
    }

    public static void testCreator013() {
        testWikipediaENAPI("Wikipedia:Hauptseite/Artikel_des_Tages/Montag", "http://de.wikipedia.org/w/api.php", Locale.GERMAN);
    }

    public static void testCreator014() {
        testWikipediaENAPI("Pakistan");
    }

    public static void testCreator015() {
        testWikipediaENAPI("Alps");
    }

    public static void testCreator016() {
        testWikipediaENAPI("Acute disseminated encephalomyelitis");
    }

    public static void testCreator017() {
        testWikipediaENAPI("Apatosaurus");
    }

    public static void testCreator018() {
        testWikipediaENAPI("Batman Returns");
    }

    public static void testCreateText001() {
        testWikipediaText(TEXT_ISSUE_96, "Wikipedia:Hauptseite/Artikel des Tages/Donnerstag", "http://de.wikipedia.org/w/api.php",
                Locale.GERMAN);
    }

    public static void testCreateText002() {
        String redirectedLink = testWikipediaENAPI("Manchester United Football Club");
        if (redirectedLink != null) {
            // see http://code.google.com/p/gwtwiki/issues/detail?id=38
            testWikipediaENAPI(redirectedLink);
        }
    }

    public static void main(String[] args) {
        testWiktionaryENAPI("backplane");
//        testCreator009();
    }
}

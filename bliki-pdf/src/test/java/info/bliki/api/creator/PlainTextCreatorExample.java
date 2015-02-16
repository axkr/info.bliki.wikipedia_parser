package info.bliki.api.creator;

import info.bliki.api.User;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.impl.APIWikiModel;

import java.io.File;

/**
 * Test to load a page and images and templates from en.wikipedia.org and render
 * it into a plain text file.
 */
public class PlainTextCreatorExample {
    private static void testWikipediaENAPI(String title) {
        String[] listOfTitleStrings = { title };
        String titleURL = Encoder.encodeTitleLocalUrl(title);
        User user = new User("", "", "http://en.wikipedia.org/w/api.php");
        user.login();
        File mainDirectory = new File(System.getProperty("java.io.tmpdir"));
        File textFile = new File(mainDirectory, titleURL + ".txt");
        WikiDB db = null;

        try {
            db = new WikiDB(new File(mainDirectory, "WikiDB"));
            APIWikiModel wikiModel = new APIWikiModel(user, db, "${image}", "${title}", null);
            DocumentCreator creator = new DocumentCreator(wikiModel, user, listOfTitleStrings);
            creator.setHeader("");
            creator.setFooter("");
            wikiModel.setUp();
            creator.renderToFile(new PlainTextConverter(true), textFile.getAbsolutePath());

            System.err.println("=> "+textFile);
        } catch (Exception e) {
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
        testWikipediaENAPI("Batman Returns");
    }

    public static void main(String[] args) {
        testCreator007();
    }
}

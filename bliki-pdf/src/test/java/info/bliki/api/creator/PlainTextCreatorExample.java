package info.bliki.api.creator;

import info.bliki.api.User;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.PlainTextConverter;
import info.bliki.wiki.impl.APIWikiModel;

import java.io.File;
import java.io.IOException;

/**
 * Test to load a page and images and templates from en.wikipedia.org and render
 * it into a plain text file.
 */
public class PlainTextCreatorExample {
    public PlainTextCreatorExample() {
        super();
    }

    public static void testWikipediaENAPI(String title) {
        String[] listOfTitleStrings = { title };
        String titleURL = Encoder.encodeTitleLocalUrl(title);
        User user = new User("", "", "http://en.wikipedia.org/w/api.php");
        user.login();
        String mainDirectory = System.getProperty("java.io.tmpdir");
        // the following subdirectory should not exist if you would like to create a
        // new database
        String databaseSubdirectory = "WikiDB";
        // the generated TXT will be stored in this file name:
         String generatedTXTFilename = mainDirectory + titleURL + ".txt";

        WikiDB db = null;

        try {
            db = new WikiDB(new File(mainDirectory, databaseSubdirectory));
            APIWikiModel wikiModel = new APIWikiModel(user, db, "${image}", "${title}", null);
            DocumentCreator creator = new DocumentCreator(wikiModel, user, listOfTitleStrings);
            creator.setHeader("");
            creator.setFooter("");
            wikiModel.setUp();
            creator.renderToFile(new PlainTextConverter(true), generatedTXTFilename);
            // StringBuilder buffer = new StringBuilder();
            // creator.render(new PlainTextConverter(true), buffer);
            // System.out.println(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e1) {
            e1.printStackTrace();
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

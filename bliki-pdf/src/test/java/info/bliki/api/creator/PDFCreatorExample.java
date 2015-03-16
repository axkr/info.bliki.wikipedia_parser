package info.bliki.api.creator;

import info.bliki.api.User;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.impl.APIWikiModel;

import java.io.File;

/**
 * Test to load a page from en.wikipedia.org and render it to a PDF file
 *
 */
public class PDFCreatorExample {
    private static void testWikipediaENAPI(String title) {
        String[] listOfTitleStrings = { title };
        String titleURL = Encoder.encodeTitleLocalUrl(title);
        User user = new User("", "", "http://en.wikipedia.org/w/api.php");
        user.login();
        WikiDB db = null;
        File mainDirectory = new File(System.getProperty("java.io.tmpdir"));
        File imageDirectory = new File(mainDirectory, "WikiImages");
        imageDirectory.mkdirs();

        try {
            db = new WikiDB(new File(mainDirectory,  "WikiDB"));
            APIWikiModel myWikiModel = new APIWikiModel(user, db, "${image}", mainDirectory.toURI().toURL().toExternalForm() +"/${title}", imageDirectory);
            DocumentCreator creator = new DocumentCreator(myWikiModel, user, listOfTitleStrings);

            creator.renderPDFToFile(mainDirectory.getAbsolutePath(), titleURL + ".pdf", HTMLConstants.CSS_MAIN_STYLE);
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

    public static void testPDF001() {
        testWikipediaENAPI("Metallica");
    }

    public static void main(String[] args) {
        testPDF001();
    }
}

package info.bliki.api.creator;

import info.bliki.annotations.IntegrationTest;
import info.bliki.api.User;
import info.bliki.wiki.impl.APIWikiModel;
import info.bliki.wiki.model.Configuration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;

import static info.bliki.wiki.filter.Encoder.encodeTitleLocalUrl;
import static org.fest.assertions.api.Assertions.assertThat;

@Category(IntegrationTest.class)
public class HTMLCreatorTest {
    private static final File TEST_RESOURCES = new File("src/test/resources");
    private static final File TEST_DB = new File(TEST_RESOURCES, "WikiDB");
    private static final File TEST_DB_BZ2 = new File(TEST_RESOURCES, "WikiDB.tar.bz2");

    private static WikiDB wikipediaEn, wikipediaDe, wiktionaryEn;

    @BeforeClass
    public static void setupDb() throws Exception {
        DbUtils.uncompressDb(TEST_DB_BZ2, TEST_DB);

        wikipediaEn  = new WikiDB(new File(TEST_DB, "Wikipedia-EN"));
        wikipediaDe  = new WikiDB(new File(TEST_DB, "Wikipedia-DE"));
        wiktionaryEn = new WikiDB(new File(TEST_DB, "Wiktionary-EN"));
    }

    @AfterClass
    public static void closeDb() throws Exception {
        for (WikiDB db : new WikiDB[] { wikipediaEn, wikipediaEn, wiktionaryEn }) {
            if (db != null) db.tearDown();
        }
    }

    @Test
    public void testCreator001() throws Exception {
        testWikipediaENAPI("Tom Hanks");
    }

    @Test
    public void testCreator002() throws Exception {
        testWikipediaENAPI("Political party strength in California");
    }

    @Test
    public void testCreator003() throws Exception {
        testWikipediaENAPI("Chris Capuano");
    }

    @Test
    public void testCreator004() throws Exception {
        testWikipediaENAPI("Protein");
    }

    @Test
    public void testCreator005() throws Exception {
        testWikipediaENAPI("Depeche Mode");
    }

    @Test
    public void testCreator006() throws Exception {
        testWikipediaENAPI("Anarchism");
    }

    @Test
    public void testCreator007() throws Exception {
        testWikipediaDEAPI("JavaScript");
    }

    @Test
    public void testCreator008() throws Exception {
        testWikipediaENAPI("libero");
    }

    @Test
    public void testCreator009() throws Exception {
        testWikipediaENAPI("Metallica");
    }

    @Test
    public void testCreator010() throws Exception {
        testWikipediaDEAPI("HTTP-Statuscode");
    }

    @Test @Ignore
    public void testCreator011() throws Exception {
        testAPI("Main Page", "http://simple.wikipedia.org/w/api.php", null, Locale.ENGLISH);
    }

    @Test @Ignore
    public void testCreator012() throws Exception {
        testAPI("Grafenwöhr", "http://bar.wikipedia.org/w/api.php", null, Locale.GERMAN);
    }

    @Test
    public void testCreator013() throws Exception {
        testWikipediaDEAPI("Wikipedia:Hauptseite/Artikel_des_Tages/Montag");
    }

    @Test
    public void testCreator014() throws Exception {
        testWikipediaENAPI("Pakistan");
    }

    @Test
    public void testCreator015() throws Exception {
        testWikipediaENAPI("Alps");
    }

    @Test
    public void testCreator016() throws Exception {
        testWikipediaENAPI("Acute disseminated encephalomyelitis");
    }

    @Test
    public void testCreator017() throws Exception {
        testWikipediaENAPI("Apatosaurus");
    }

    @Test
    public void testCreator018() throws Exception {
        testWikipediaENAPI("Batman Returns");
    }

    @Test
    public void testCreateText002() throws Exception {
        String redirectedLink = testWikipediaENAPI("Manchester United Football Club").redirectLink;
        if (redirectedLink != null) {
            // see http://code.google.com/p/gwtwiki/issues/detail?id=38
            testWikipediaENAPI(redirectedLink);
        }
    }

    @Test
    public void testWiktionary() throws Exception {
        testWiktionaryENAPI("backplane");
    }

    private Result testWiktionaryENAPI(String title) throws Exception {
        return testAPI(title, "http://en.wiktionary.org/w/api.php", wiktionaryEn, Locale.ENGLISH);
    }

    private Result testWikipediaENAPI(String title) throws Exception {
        return testAPI(title, "http://en.wikipedia.org/w/api.php", wikipediaEn, Locale.ENGLISH);
    }

    private Result testWikipediaDEAPI(String title) throws Exception {
        return testAPI(title, "http://de.wikipedia.org/w/api.php", wikipediaDe, Locale.GERMAN);
    }

    private Result testAPI(String title, String apiLink, WikiDB db, Locale locale) throws IOException {
        User user = new User("", "", apiLink);
        user.login();

        Path mainDirectory = Files.createTempDirectory("bliki-" + encodeTitleLocalUrl(title).replace("/", "_"));
        Path imageDirectory = mainDirectory.resolve("WikiImages");

        APIWikiModel wikiModel = new APIWikiModel(user, db,
                locale,
                "${image}",
                "${title}",
                imageDirectory.toString());

        DocumentCreator creator = new DocumentCreator(wikiModel, user, new String[]{title});
        @SuppressWarnings("StringBufferReplaceableByString")
        StringBuilder builder = new StringBuilder();
        builder.append(HTMLConstants.HTML_HEADER1)
           .append(HTMLConstants.CSS_MAIN_STYLE)
           .append(HTMLConstants.CSS_SCREEN_STYLE)
           .append(HTMLConstants.HTML_HEADER2);
        creator.setHeader(builder.toString());
        creator.setFooter(HTMLConstants.HTML_FOOTER);
        wikiModel.setUp();
        Configuration.DEFAULT_CONFIGURATION.setTemplateCallsCache(new HashMap<String, String>());
        Path generatedHTMLFilename = mainDirectory.resolve(encodeTitleLocalUrl(title) + ".html");
        creator.renderToFile(generatedHTMLFilename.toString());
        System.out.println("Created file: " + generatedHTMLFilename);

        assertThat(generatedHTMLFilename.toFile()).isFile();
        assertThat(generatedHTMLFilename.toFile().length()).isGreaterThan(0);

        return new Result(generatedHTMLFilename.toFile(), wikiModel.getRedirectLink());
    }

    static class Result {
        final String redirectLink;
        final File content;

        Result(File content, String redirectLink) {
            this.redirectLink = redirectLink;
            this.content = content;
        }
    }
}

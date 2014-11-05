package info.bliki.api.creator;

import co.freeside.betamax.ProxyConfiguration;
import co.freeside.betamax.junit.Betamax;
import co.freeside.betamax.junit.RecorderRule;
import info.bliki.annotations.IntegrationTest;
import info.bliki.api.User;
import info.bliki.wiki.impl.APIWikiModel;
import info.bliki.wiki.model.Configuration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;

import static co.freeside.betamax.TapeMode.READ_ONLY;
import static co.freeside.betamax.TapeMode.WRITE_ONLY;
import static info.bliki.wiki.filter.Encoder.encodeTitleLocalUrl;
import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

@Category(IntegrationTest.class)
public class HTMLCreatorTest {
    private TestWikiDB wikipediaEn, wikipediaDe, wiktionaryEn;

    @Before
    public void setupDb() throws Exception {
        wikipediaEn  = createTestDB("wikipedia-EN");
        wikipediaDe  = createTestDB("wikipedia-DE");
        wiktionaryEn = createTestDB("wiktionary-EN");
    }

    private TestWikiDB createTestDB(String name) throws SQLException, IOException {
        File directory = File.createTempFile("bliki-tests", name);
        assert directory.delete();
        return new TestWikiDB(directory);
    }

    @Rule public RecorderRule recorder = new RecorderRule(ProxyConfiguration.builder().build());

    @Betamax(tape="Pakistan", mode = READ_ONLY)
    @Test public void testWikipediaPakistan() throws Exception {
        testWikipediaENAPI("Pakistan");
    }

    @Betamax(tape="Tom_Hanks", mode = READ_ONLY)
    @Test public void testWikipediaTomHanks() throws Exception {
        testWikipediaENAPI("Tom Hanks");
    }

    @Betamax(tape = "backplane", mode = READ_ONLY)
    @Test public void testWiktionaryBackplane() throws Exception {
        testWiktionaryENAPI("backplane");
    }

    @Betamax(tape="Political_party_strength_in_California", mode = WRITE_ONLY)
    @Ignore @Test public void testPoliticalPartyStrengthInCalifornia() throws Exception {
        testWikipediaENAPI("Political party strength in California");
    }

    @Betamax(tape="Chris_Capuano", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaChrisCapuano() throws Exception {
        testWikipediaENAPI("Chris Capuano");
    }

    @Betamax(tape="Protein", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaProtein() throws Exception {
        testWikipediaENAPI("Protein");
    }

    @Betamax(tape="Depeche_Mode", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaDepeche_Mode() throws Exception {
        testWikipediaENAPI("Depeche Mode");
    }

    @Betamax(tape="Anarchism", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaAnarchism() throws Exception {
        testWikipediaENAPI("Anarchism");
    }

    @Betamax(tape="Javascript", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaJavascript() throws Exception {
        testWikipediaDEAPI("JavaScript");
    }

    @Betamax(tape="libero", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaLibero() throws Exception {
        testWikipediaENAPI("libero");
    }

    @Ignore @Betamax(tape="Metallica", mode = WRITE_ONLY)
    @Test public void testWikipediaMetallica() throws Exception {
        testWikipediaENAPI("Metallica");
    }

    @Betamax(tape="HTTP-Statuscode", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaHTTPStatusCode() throws Exception {
        testWikipediaDEAPI("HTTP-Statuscode");
    }

    @Ignore @Test public void testWikipediaDEMontag() throws Exception {
        testWikipediaDEAPI("Wikipedia:Hauptseite/Artikel_des_Tages/Montag");
    }

    @Betamax(tape="Alps", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaAlps() throws Exception {
        testWikipediaENAPI("Alps");
    }

    @Betamax(tape="Acute_disseminated_encephalomyelitis", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaAcute_disseminated_encephalomyelitis() throws Exception {
        testWikipediaENAPI("Acute disseminated encephalomyelitis");
    }

    @Betamax(tape="Apatosaurus", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaApatosaurus() throws Exception {
        testWikipediaENAPI("Apatosaurus");
    }

    @Betamax(tape="Batman returns", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaBatmanReturns() throws Exception {
        testWikipediaENAPI("Batman Returns");
    }

    @Betamax(tape="Manchester_United_Football_Club", mode = WRITE_ONLY)
    @Ignore @Test public void testWikipediaManchester_United_Football_Club() throws Exception {
        String redirectedLink = testWikipediaENAPI("Manchester United Football Club").redirectLink;
        if (redirectedLink != null) {
            // see http://code.google.com/p/gwtwiki/issues/detail?id=38
            testWikipediaENAPI(redirectedLink);
        }
    }

    private Result testWiktionaryENAPI(String title) throws Exception {
        return testAPI(title, "http://en.wiktionary.org/w/api.php", wiktionaryEn, ENGLISH).assertNoTemplateError();
    }

    private Result testWikipediaENAPI(String title) throws Exception {
        return testAPI(title, "http://en.wikipedia.org/w/api.php", wikipediaEn, ENGLISH).assertNoTemplateError();
    }

    private Result testWikipediaDEAPI(String title) throws Exception {
        return testAPI(title, "http://de.wikipedia.org/w/api.php", wikipediaDe, GERMAN).assertNoTemplateError();
    }

    private Result testAPI(String title, String apiLink, WikiDB db, Locale locale) throws IOException {
        User user = new User(null, null, apiLink);
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

        public Result assertNoTemplateError() {
            assertThat(contentOf(content)).doesNotContain("TemplateParserError:");
            return this;
        }
    }
}

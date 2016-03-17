package info.bliki.api.creator;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import software.betamax.junit.Betamax;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.GERMAN;
import static software.betamax.TapeMode.READ_ONLY;
import static software.betamax.TapeMode.WRITE_ONLY;

public class WikipediaIntegrationTest extends HTMLCreatorIntegrationTest {
    private TestWikiDB wikipediaEn, wikipediaDe;

    @Before
    public void setUp() throws Exception {
        wikipediaEn  = createTestDB("wikipedia-EN");
        wikipediaDe  = createTestDB("wikipedia-DE");
    }

    @Ignore
    @Test @Betamax(tape="Pakistan", mode = READ_ONLY)
    public void testPakistan() throws Exception {
        testWikipediaENAPI("Pakistan");
    }

    @Betamax(tape="Tom_Hanks", mode = READ_ONLY)
    @Test public void testTomHanks() throws Exception {
        testWikipediaENAPI("Tom Hanks");
    }

    @Betamax(tape="Foobar", mode = READ_ONLY)
    @Test public void testFoobar() throws Exception {
        testWikipediaENAPI("Foobar");
    }

    @Betamax(tape="Political_party_strength_in_California", mode = WRITE_ONLY)
    @Ignore @Test public void testPoliticalPartyStrengthInCalifornia() throws Exception {
        testWikipediaENAPI("Political party strength in California");
    }

    @Betamax(tape="Chris_Capuano", mode = WRITE_ONLY)
    @Ignore @Test public void testChrisCapuano() throws Exception {
        testWikipediaENAPI("Chris Capuano");
    }

    @Betamax(tape="Protein", mode = WRITE_ONLY)
    @Ignore @Test public void testProtein() throws Exception {
        testWikipediaENAPI("Protein");
    }

    @Betamax(tape="Depeche_Mode", mode = WRITE_ONLY)
    @Ignore @Test public void testDepeche_Mode() throws Exception {
        testWikipediaENAPI("Depeche Mode");
    }

    @Betamax(tape="Anarchism", mode = WRITE_ONLY)
    @Ignore @Test public void testAnarchism() throws Exception {
        testWikipediaENAPI("Anarchism");
    }

    @Betamax(tape="Javascript", mode = WRITE_ONLY)
    @Ignore @Test public void testJavascript() throws Exception {
        testWikipediaDEAPI("JavaScript");
    }

    @Betamax(tape="libero", mode = WRITE_ONLY)
    @Ignore @Test public void testLibero() throws Exception {
        testWikipediaENAPI("libero");
    }

    @Ignore @Betamax(tape="Metallica", mode = WRITE_ONLY)
    @Test public void testMetallica() throws Exception {
        testWikipediaENAPI("Metallica");
    }

    @Betamax(tape="HTTP-Statuscode", mode = WRITE_ONLY)
    @Ignore @Test public void testHTTPStatusCode() throws Exception {
        testWikipediaDEAPI("HTTP-Statuscode");
    }

    @Ignore @Test public void testWikipediaDEMontag() throws Exception {
        testWikipediaDEAPI("Wikipedia:Hauptseite/Artikel_des_Tages/Montag");
    }

    @Betamax(tape="Alps", mode = WRITE_ONLY)
    @Ignore @Test public void testAlps() throws Exception {
        testWikipediaENAPI("Alps");
    }

    @Betamax(tape="Acute_disseminated_encephalomyelitis", mode = WRITE_ONLY)
    @Ignore @Test public void testAcute_disseminated_encephalomyelitis() throws Exception {
        testWikipediaENAPI("Acute disseminated encephalomyelitis");
    }

    @Betamax(tape="Apatosaurus", mode = WRITE_ONLY)
    @Ignore @Test public void testApatosaurus() throws Exception {
        testWikipediaENAPI("Apatosaurus");
    }

    @Betamax(tape="Batman returns", mode = WRITE_ONLY)
    @Ignore @Test public void testBatmanReturns() throws Exception {
        testWikipediaENAPI("Batman Returns");
    }

    @Betamax(tape="Manchester_United_Football_Club", mode = WRITE_ONLY)
    @Ignore @Test public void testManchester_United_Football_Club() throws Exception {
        String redirectedLink = testWikipediaENAPI("Manchester United Football Club").redirectLink;
        if (redirectedLink != null) {
            // see http://code.google.com/p/gwtwiki/issues/detail?id=38
            testWikipediaENAPI(redirectedLink);
        }
    }

    private Result testWikipediaENAPI(String title) throws Exception {
        return testAPI(title, "https://en.wikipedia.org/w/api.php", wikipediaEn, ENGLISH).assertNoErrors();
    }

    private Result testWikipediaDEAPI(String title) throws Exception {
        return testAPI(title, "https://de.wikipedia.org/w/api.php", wikipediaDe, GERMAN).assertNoErrors();
    }
}

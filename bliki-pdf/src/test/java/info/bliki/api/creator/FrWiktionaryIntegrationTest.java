package info.bliki.api.creator;

import info.bliki.wiki.model.Configuration;
import org.junit.Before;
import org.junit.Test;
import software.betamax.junit.Betamax;

import static info.bliki.wiki.model.IConfiguration.Casing.CaseSensitive;
import static java.util.Locale.FRENCH;
import static software.betamax.TapeMode.READ_ONLY;

public class FrWiktionaryIntegrationTest extends HTMLCreatorIntegrationTest {
    private TestWikiDB wiktionaryFr;

    @Before public void setupDb() throws Exception {
        wiktionaryFr = createTestDB("wiktionary-FR");
    }

    @Test @Betamax(tape = "eau", mode = READ_ONLY)
    public void testEau() throws Exception {
        testWiktionaryFRAPI("eau");
    }

    @Test @Betamax(tape = "tenir", mode = READ_ONLY)
    public void testTenir() throws Exception {
        testWiktionaryFRAPI("tenir");
    }

    @Test @Betamax(tape = "accommodation", mode = READ_ONLY)
    public void testAccommodation() throws Exception {
        testWiktionaryFRAPI("accommodation");
    }

    @Override
    protected Configuration getConfiguration() {
        return new Configuration("frwiktionary", CaseSensitive);
    }

    private HTMLCreatorIntegrationTest.Result testWiktionaryFRAPI(String title) throws Exception {
        return testAPI(title, "https://fr.wiktionary.org/w/api.php", wiktionaryFr, FRENCH).assertNoErrors();
    }
}

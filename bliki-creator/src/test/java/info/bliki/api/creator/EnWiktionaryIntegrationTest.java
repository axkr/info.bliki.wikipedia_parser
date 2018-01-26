package info.bliki.api.creator;

import info.bliki.wiki.model.Configuration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import software.betamax.junit.Betamax;

import static info.bliki.wiki.model.IConfiguration.Casing.CaseSensitive;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static software.betamax.TapeMode.READ_ONLY;

public class EnWiktionaryIntegrationTest extends HTMLCreatorIntegrationTest {
    private TestWikiDB wiktionaryEn;

    @Before
    public void setupDb() throws Exception {
        wiktionaryEn = createTestDB("wiktionary-EN");
    }

    @Betamax(tape = "backplane", mode = READ_ONLY)
    @Test public void testBackplane() throws Exception {
        testWiktionaryENAPI("backplane");
    }

    @Betamax(tape = "backplane", mode = READ_ONLY)
    @Test public void testCategoriesAreExtracted() throws Exception {
        Result result = testWiktionaryENAPI("backplane");
        assertThat(result.categories).containsKeys(
            "English compound words",
            "English lemmas",
            "English nouns",
            "English terms with audio links",
            "en:Electronics"
        );
    }
    @Betamax(tape = "-yer", mode = READ_ONLY)
    @Test public void testCategoriesWithExtraSpaces() throws Exception {
        Result result = testWiktionaryENAPI("-yer");
        assertThat(result.categories.keySet()).contains(
            "English terms derived from Middle English",
            "English terms with rare senses",
            "English lemmas",
            "English suffixes",
            "etyl cleanup/en"
        );
    }

    @Betamax(tape = "Vadimas", mode = READ_ONLY)
    @Test public void testVadimas() throws Exception {
        testWiktionaryENAPI("Vadimas");
    }

    @Betamax(tape = "AB", mode = READ_ONLY)
    @Test public void testAB() throws Exception {
        testWiktionaryENAPI("AB");
    }

    @Betamax(tape = "Veja", mode = READ_ONLY)
    @Test public void testVeja() throws Exception {
        testWiktionaryENAPI("veja");
    }

    @Betamax(tape = "quine", mode = READ_ONLY)
    @Test public void testQuine() throws Exception {
        testWiktionaryENAPI("quine");
    }

    @Betamax(tape = "Kazakh", mode = READ_ONLY)
    @Test public void testKazakh() throws Exception {
        final Result result = testWiktionaryENAPI("Kazakh");
        result.assertContains("<a href=\"//kk.wiktionary.org/wiki/\">Kazakh edition</a>");
    }

    @Betamax(tape = "gagauseueo", mode = READ_ONLY)
    @Test public void testHangul() throws Exception {
        testWiktionaryENAPI("가가우스어");
    }

    @Betamax(tape = "French_toast", mode = READ_ONLY)
    @Test public void testFrenchToast() throws Exception {
        testWiktionaryENAPI("French toast");
    }

    @Ignore
    @Betamax(tape = "teo", mode = READ_ONLY)
    @Test public void testHangul2() throws Exception {
        // {{ko-usex|[[ ]] {{=}} | ... => TemplateParserError
        testWiktionaryENAPI("터");
    }

    @Betamax(tape = "colon", mode = READ_ONLY)
    @Test public void testColon() throws Exception {
        final Result result = testWiktionaryENAPI("colon");
        result.assertNoTemplatesLeft();
    }

    @Betamax(tape = "ar-translit", mode = READ_ONLY)
    @Test public void testArTranslit() throws Exception {
        final Result result = testWiktionaryENAPI("User:Jberkel/bliki-testcases/ar-translit");
        result.assertContains("ʾaʿdād");
    }

    @Betamax(tape = "quote", mode = READ_ONLY)
    @Test public void testQuote() throws Exception {
        testWiktionaryENAPI("User:Jberkel/bliki-testcases/quote");
    }

    @Betamax(tape = "inh", mode = READ_ONLY)
    @Test public void testInh() throws Exception {
        final Result result = testWiktionaryENAPI("User:Jberkel/bliki-testcases/inh");
        result.assertNoErrors();
    }

    @Betamax(tape = "eye_dialect", mode = READ_ONLY)
    @Test public void testEyeDialect() throws Exception {
        testWiktionaryENAPI("User:Jberkel/bliki-testcases/eye_dialect")
            .assertContains("representing NotFound English");
    }

    @Betamax(tape = "t2i", mode = READ_ONLY)
    @Test public void testT2i() throws Exception {
        final Result result = testWiktionaryENAPI("User:Jberkel/bliki-testcases/t2i");
        result.assertNoTemplatesLeft();
    }

    @Betamax(tape = "areia", mode = READ_ONLY)
    @Test public void testSurrogateHandling() throws Exception {
        final Result result = testWiktionaryENAPI("User:Jberkel/bliki-testcases/areia");
        result.assertContains("*\uD800\uDF07\uD800\uDF00\uD800\uDF14\uD800\uDF04\uD800\uDF0D\uD800\uDF00");
        result.assertContains("*hasena");
    }

    @Betamax(tape = "limpet", mode = READ_ONLY)
    @Test public void test_limpet() throws Exception {
        testWiktionaryENAPI("limpet");
    }

    @Betamax(tape = "Stein_des_Anstosses", mode = READ_ONLY)
    @Test public void testSteinDesAnstosses() throws Exception {
        testWiktionaryENAPI("Stein_des_Anstoßes");
    }

    @Betamax(tape = "titeln", mode = READ_ONLY)
    @Ignore @Test public void testTiteln() throws Exception {
        final Result result = testWiktionaryENAPI("titeln");
        result.assertContains("Template:rfdef");
    }

    @Betamax(tape = "biombo", mode = READ_ONLY)
    @Test public void testBiombo() throws Exception {
        testWiktionaryENAPI("biombo");
    }

    @Betamax(tape = "senseid", mode = READ_ONLY)
    @Test public void testSenseId() throws Exception {
        testWiktionaryENAPI("User:Jberkel/bliki-testcases/senseid");
    }

    @Override
    protected Configuration getConfiguration() {
        return new Configuration("enwiktionary", CaseSensitive);
    }

    private Result testWiktionaryENAPI(String title) throws Exception {
        return testAPI(title, "https://en.wiktionary.org/w/api.php", wiktionaryEn, ENGLISH).assertNoErrors();
    }
}

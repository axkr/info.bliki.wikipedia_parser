package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MagicWordTest extends FilterTestSupport {
    @Test public void testProcessMagicWordNAMESPACENUMBERWithCurrentNamespace() throws Exception {
        wikiModel.setNamespaceName("Talk");
        final String number = MagicWord.processMagicWord(MagicWord.MagicWordE.MAGIC_NAMESPACENUMBER, null, wikiModel);
        assertThat(number).isEqualTo("1");
    }

    @Test public void testProcessMagicWordNAMESPACENUMBERWithParameter() throws Exception {
        final String number = MagicWord.processMagicWord(MagicWord.MagicWordE.MAGIC_NAMESPACENUMBER, "User:Foo", wikiModel);
        assertThat(number).isEqualTo("2");
    }

    @Test public void testProcessMagicWordBANG() throws Exception {
        final String result = MagicWord.processMagicWord(MagicWord.MagicWordE.MAGIC_BANG, null, wikiModel);
        assertThat(result).isEqualTo("|");
    }

    @Test public void testFullUrl() throws Exception {
        final String result = MagicWord.processMagicWord(MagicWord.MagicWordE.MAGIC_FULL_URL, "Category:Name With Space", wikiModel);
        assertThat(result).isEqualTo("//www.bliki.info/wiki/Category:Name With Space");
    }

    @Test public void testFullUrlE() throws Exception {
        final String result = MagicWord.processMagicWord(MagicWord.MagicWordE.MAGIC_FULL_URL_E, "Category:Name With Space", wikiModel);
        assertThat(result).isEqualTo("//www.bliki.info/wiki/Category:Name_With_Space");
    }
}

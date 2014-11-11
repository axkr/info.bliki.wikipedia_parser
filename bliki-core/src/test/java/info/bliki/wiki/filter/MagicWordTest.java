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
}

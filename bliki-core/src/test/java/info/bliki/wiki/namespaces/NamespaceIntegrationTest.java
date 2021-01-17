package info.bliki.wiki.namespaces;

import info.bliki.annotations.IntegrationTest;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.wiki.filter.WikiTestModel;
import info.bliki.wiki.model.WikiModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

@Category(IntegrationTest.class)
public class NamespaceIntegrationTest {

    private WikiModel wikiModel;

    @Before
    public void setUp() throws Exception {
        wikiModel = new WikiTestModel(Locale.ENGLISH,
                "http://www.bliki.info/wiki/${image}",
                "http://www.bliki.info/wiki/${title}",
                "wikitestModel");
        wikiModel.setUp();
    }

    @Test
    public void mwTitleInNamespaceTest() throws IOException {
        // This test aims to guarantee that enwiktionary's headword#non_categorizable() function doesn't throw error.

        String[] testNamespaces = {"", "Appendix"};
        for (String ns : testNamespaces) {
            wikiModel.setFrame(new Frame(null, null, null, false));
            String source = "{{#invoke:mw-title-in-namespace|inNamespaceTest|" + ns + "}}";
            String result = wikiModel.parseTemplates(source);
            assertThat(result).isNotEmpty();
        }
    }
}

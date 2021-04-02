package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.annotations.IntegrationTest;
import info.bliki.wiki.filter.WikiTestModel;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.template.AbstractTemplateFunction;
import info.bliki.wiki.template.namedargs.AbstractNamedArgsTemplateFunction;
import info.bliki.wiki.template.namedargs.NamedArgs;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.*;

/**
 * The integration test of ScribuntoLuaEngine#callParserFunction().
 */
@Category(IntegrationTest.class)
public class CallParserFunctionIntegrationTest {

    public static class NamedArgsTestFunction extends AbstractNamedArgsTemplateFunction {
        @Override
        public String parseFunction(NamedArgs parts, IWikiModel model, char[] src,
                                    int beginIndex, int endIndex, boolean isSubst) throws IOException {
            String result = "named:" + String.join(",", parts.getString(123), parts.getString("abc"));
            return result;
        }

        @Override
        public String getFunctionDoc() {
            return null;
        }
    }

    public static class ListedTestFunction extends AbstractTemplateFunction {
        @Override
        public String parseFunction(List<String> parts, IWikiModel model, char[] src,
                                    int beginIndex, int endIndex, boolean isSubst) throws IOException {
            String result = "listed:" + String.join(",", parts);
            return result;
        }

        @Override
        public String getFunctionDoc() {
            return null;
        }
    }

    private WikiModel wikiModel;

    @Before
    public void setUp() throws Exception {
        wikiModel = new WikiTestModel(Locale.ENGLISH,
                "http://www.bliki.info/wiki/${image}",
                "http://www.bliki.info/wiki/${title}",
                "wikitestModel");

        wikiModel.addTemplateFunction("#tag:named-args", new NamedArgsTestFunction());
        wikiModel.addTemplateFunction("#tag:listed-args", new ListedTestFunction());

        wikiModel.setUp();
    }

    /**
     * This test expects the below process flow;
     * <code>
     *      "{{named-args}}"
     *     --> wikitestModel/templates/named-args
     *     --> wikitestModel/modules/named-args-caller.lua
     *     --> wikitestModel/modules/parser-function-caller.lua
     *     --> ScribuntoLuaEngine#callParserFunction()
     *     --> NamedArgsTestFunction#parseFunction(namedArgsObject)
     * </code>
     */
    @Test
    public void namedArgsTest() throws IOException {
        String source = "{{named-args}}";
        String expected = "named:zyx,987";
        String actual = wikiModel.render(source);
        assertThat(actual).contains(expected);
    }

    /**
     * This test expects the below process flow;
     * <code>
     *     "{{named-args}}"
     *     --> wikitestModel/templates/listed-args
     *     --> wikitestModel/modules/listed-args-caller.lua
     *     --> wikitestModel/modules/parser-function-caller.lua
     *     --> ScribuntoLuaEngine#callParserFunction()
     *     --> ListedArgsTestFunction#parseFunction(stringList)
     * </code>
     */
    @Test
    public void listedArgsTest() throws IOException {
        String source = "{{listed-args}}";
        String expected = "listed:,234,cde,345"; // note: args[0] is always ""
        String actual = wikiModel.render(source);
        assertThat(actual).contains(expected);
    }
}

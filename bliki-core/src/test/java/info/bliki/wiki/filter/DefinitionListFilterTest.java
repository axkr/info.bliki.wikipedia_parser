package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DefinitionListFilterTest extends FilterTestSupport {

    @Test public void testDefinitionList0() throws Exception {
        assertThat(wikiModel.render(":''There is also an [[asteroid]] [[9969 Braille]]''", false)).isEqualTo("\n" +
                "<dl>\n" +
                "<dd><i>There is also an <a href=\"http://www.bliki.info/wiki/Asteroid\" title=\"Asteroid\">asteroid</a> <a href=\"http://www.bliki.info/wiki/9969_Braille\" title=\"9969 Braille\">9969 Braille</a></i></dd>\n</dl>");
    }

    @Test public void testDefinitionList1() throws Exception {
        assertThat(wikiModel.render(";name:Definition", false)).isEqualTo("\n" +
                "<dl>\n" +
                "<dt>name</dt>\n" +
                "<dd>Definition</dd>\n</dl>");
    }

    @Test public void testDefinitionList2() throws Exception {
        assertThat(wikiModel.render("; name : Definition", false)).isEqualTo("\n" +
                "<dl>\n" +
                "<dt>name </dt>\n" +
                "<dd>Definition</dd>\n</dl>");
    }

    @Test public void testDefinitionList3() throws Exception {
        assertThat(wikiModel.render(";foo:12:30", false)).isEqualTo("\n" +
                "<dl>\n" +
                "<dt>foo</dt>\n" +
                "<dd>12:30</dd>\n</dl>");
    }

    @Test public void testDefinitionList10() throws Exception {
        assertThat(wikiModel
                .render(":a simple test<nowiki>\n" + "  x+y\n" + "  </nowiki>\n" + "test test", false)).isEqualTo("\n" +
                "<dl>\n" +
                "<dd>a simple test\n" +
                "  x+y\n" +
                "  </dd>\n</dl>\n" +
                "<p>test test</p>");
    }

    @Test public void testDefinitionList11() throws Exception {
        assertThat(wikiModel.render(":a simple test<math>ein text\n" + "  x+y\n" + "  \n" + "test test", false)).isEqualTo("\n" +
                "<dl>\n" +
                "<dd>a simple test<span class=\"math\">ein text</span></dd>\n</dl>\n" +
                "<pre>" +
                " x+y\n" +
                " \n" +
                "</pre>\n" +
                "<p>test test</p>");
    }

    @Test public void testDefinitionList12() throws Exception {
        assertThat(wikiModel.render(":blabla\n::blablabla\n" + " test it\n", false)).isEqualTo("\n" +
                "<dl>\n" +
                "<dd>blabla\n" +
                "<dl>\n" +
                "<dd>blablabla</dd>\n</dl></dd>\n</dl>\n" +
                "<pre>" +
                "test it\n" +
                "\n" +
                "</pre>");
    }
}

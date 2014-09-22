package info.bliki.wiki.filter;

import info.bliki.wiki.model.SemanticAttribute;
import info.bliki.wiki.model.SemanticRelation;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WPSemanticLinkTest extends FilterTestSupport {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        wikiModel.setSemanticWebActive(true);
    }

    /*
     * Test a semantic relation
     *
     * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
     * MediaWiki</a> for more information.
     */
    @Test public void testLink01() {

        assertThat(wikiModel.render("Berlin is the capital of [[Is capital of::Germany]].", false)).isEqualTo("\n"
                + "<p>Berlin is the capital of <a class=\"interwiki\" href=\"http://www.bliki.info/wiki/Germany\" title=\"Germany\">Germany</a>.</p>");
        List<SemanticRelation> list = wikiModel.getSemanticRelations();
        SemanticRelation rel = list.get(0);
        assertThat(rel.getRelation()).isEqualTo("Is capital of");
        assertThat(rel.getValue()).isEqualTo("Germany");
    }

    /*
     * Test a semantic attribute
     *
     * See <a href="http://en.wikipedia.org/wiki/Semantic_MediaWiki">Semantic
     * MediaWiki</a> for more information.
     */
    @Test public void testLink02() {

        assertThat(wikiModel.render("The population is [[Has population:=3,993,933]].", false)).isEqualTo("\n<p>The population is 3,993,933.</p>");
        List<SemanticAttribute> list = wikiModel.getSemanticAttributes();
        SemanticAttribute rel = list.get(0);
        assertThat(rel.getAttribute()).isEqualTo("Has population");
        assertThat(rel.getValue()).isEqualTo("3,993,933");
    }

    @Test public void testLink03() {

        assertThat(wikiModel.render("Make [[example relation::link|alternate text]] appear in place of the link.", false)).isEqualTo("\n"
                + "<p>Make <a class=\"interwiki\" href=\"http://www.bliki.info/wiki/Link\" title=\"Link\">alternate text</a> appear in place of the link.</p>");
        List<SemanticRelation> list = wikiModel.getSemanticRelations();
        SemanticRelation rel = list.get(0);
        assertThat(rel.getRelation()).isEqualTo("example relation");
        assertThat(rel.getValue()).isEqualTo("link");
    }

    @Test public void testLink04() {

        assertThat(wikiModel
                .render("To hide the property [[    example relation::link   | ]] from appearing at all", false)).isEqualTo("\n<p>To hide the property  from appearing at all</p>");
        List<SemanticRelation> list = wikiModel.getSemanticRelations();
        SemanticRelation rel = list.get(0);
        assertThat(rel.getRelation()).isEqualTo("example relation");
        assertThat(rel.getValue()).isEqualTo("link");
    }

    @Test public void testLink05() {

        assertThat(wikiModel
                .render("The [[:C++ :: operator]].", false)).isEqualTo("\n" +
                "<p>The <a href=\"http://www.bliki.info/wiki/C%2B%2B_::_operator\" title=\"C++ :: operator\">C++ :: operator</a>.</p>");
        List<SemanticRelation> list = wikiModel.getSemanticRelations();
        assertThat(list).isNull();
    }
}

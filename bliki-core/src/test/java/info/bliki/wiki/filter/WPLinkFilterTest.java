package info.bliki.wiki.filter;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class WPLinkFilterTest extends FilterTestSupport {

  @Test public void testIssue136() {
      assertThat(wikiModel
              .render(
                      "[[Test:http://somesite.org]]",
                      false)).isEqualTo("\n" +
              "<p><a href=\"http://www.bliki.info/wiki/Test:http://somesite.org\" title=\"Test:http://somesite.org\">Test:http://somesite.org</a></p>");
  }

    @Test public void testLinkHash() {
        assertThat(wikiModel.render("##[[Using Eclipse Wikipedia Editor:Getting Started#Features|Features]]", false)).isEqualTo("\n"
                + "<ol>\n"
                + "<li>\n"
                + "<ol>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Using_Eclipse_Wikipedia_Editor:Getting_Started#Features\" title=\"Using Eclipse Wikipedia Editor:Getting Started\">Features</a></li>\n</ol></li>\n</ol>");
    }

    @Test public void testLink() {
        assertThat(wikiModel
                .render(
                        "You could open the [[Wikipedia:sandbox|sandbox]] in a separate window or tab to be able to see both this text and your tests in the sandbox.",
                        false)).isEqualTo("\n"
                + "<p>You could open the <a href=\"http://www.bliki.info/wiki/Wikipedia:sandbox\" title=\"Wikipedia:sandbox\">sandbox</a> in a separate window or tab to be able to see both this text and your tests in the sandbox.</p>");
    }

    @Test public void testLink0() {
        assertThat(wikiModel.render("[X]", false)).isEqualTo("\n" + "<p>[X]</p>");
    }

    @Test public void testLink1() {
        assertThat(wikiModel
                .render("[[en:Test|Test]]", false)).isEqualTo("\n" + "<p><a href=\"http://en.wikipedia.org/wiki/Test\">Test</a></p>");
    }

    @Test public void testLink2() {
        assertThat(wikiModel.render(
                "[[Test|Test]]", false)).isEqualTo("\n" + "<p><a href=\"http://www.bliki.info/wiki/Test\" title=\"Test\">Test</a></p>");
    }

    @Test public void testLink3() {
        assertThat(wikiModel.render("[[:Category:Test page]]", false)).isEqualTo("\n"
                + "<p><a href=\"http://www.bliki.info/wiki/Category:Test_page\" title=\"Category:Test page\">Category:Test page</a></p>");
    }

    /**
     * Categories are not rendered
     *
     */
    @Test public void testCategory01() {
        assertThat(wikiModel.render("[[Category:Tips and Tricks]]", false)).isEqualTo("");
        Map<String, String> map = wikiModel.getCategories();
        assertThat(map).containsKey("Tips and Tricks");
    }

    @Test public void testCategory02() {
        assertThat(wikiModel.render("[[Category:Rock and Roll Hall of Fame inductees|Beatles, The]]", false)).isEqualTo("");
        Map<String, String> map = wikiModel.getCategories();
        // assertTrue(map.containsKey("Rock and Roll Hall of Fame inductees"));
        assertThat(map).containsValue("Beatles, The");
    }

    @Test public void testLink5() {
        assertThat(wikiModel.render(
                "[[wikitravel:test]]", false)).isEqualTo("\n" + "<p><a href=\"http://wikitravel.org/en/test\">wikitravel:test</a></p>");
    }

    @Test public void testLink6() {
        assertThat(wikiModel.render("[[Test:hello world|]]", false)).isEqualTo("\n" + "<p><a href=\"http://www.bliki.info/wiki/Test:hello_world\" title=\"Test:hello world\">hello world</a></p>");
    }

    @Test public void testLink7() {
        assertThat(wikiModel.render("[[Test(hello world)|]]", false)).isEqualTo("\n" + "<p><a href=\"http://www.bliki.info/wiki/Test(hello_world)\" title=\"Test(hello world)\">Test</a></p>");
    }

    @Test public void testLink8() {
        assertThat(wikiModel.render("[[Boston, Massachusetts|]]", false)).isEqualTo("\n"
                + "<p><a href=\"http://www.bliki.info/wiki/Boston%2C_Massachusetts\" title=\"Boston, Massachusetts\">Boston</a></p>");
    }

    @Test public void testLink9() {
        assertThat(wikiModel.render(
                "test [[lets start\na 2 rows link]] test", false)).isEqualTo("\n" + "<p>test [[lets start\n" + "a 2 rows link]] test</p>");
    }

    @Test public void testLink9a() {
        assertThat(wikiModel.render("test [[lets start a 2 rows link|lets start\na 2 rows link]] test", false)).isEqualTo("\n"
                + "<p>test <a href=\"http://www.bliki.info/wiki/Lets_start_a_2_rows_link\" title=\"Lets start a 2 rows link\">lets start\n"
                + "a 2 rows link</a> test</p>");
    }

    @Test public void testLink10() {
        assertThat(wikiModel.render("test [[lets start|\na 2 rows piped link]] test", false)).isEqualTo("\n"
                + "<p>test <a href=\"http://www.bliki.info/wiki/Lets_start\" title=\"Lets start\">a 2 rows piped link</a> test</p>");
    }

    @Test public void testLink11() {
        assertThat(wikiModel.render("test\n*blabla[[List of cities by country#Morocco|\nCities in Morocco]]",
                false)).isEqualTo("\n" +
                "<p>test\n" +
                "</p>\n" +
                "<ul>\n" +
                "<li>blabla<a href=\"http://www.bliki.info/wiki/List_of_cities_by_country#Morocco\" title=\"List of cities by country\">Cities in Morocco</a></li>\n" +
                "</ul>");
    }

    //
    @Test public void testLink12() {
        assertThat(wikiModel.render("kellereien wie [[Henkell & Söhnlein|Henkell]], [[Söhnlein]]", false)).isEqualTo("\n"
                + "<p>kellereien wie <a href=\"http://www.bliki.info/wiki/Henkell_%26_S%C3%B6hnlein\" title=\"Henkell &amp; Söhnlein\">Henkell</a>, <a href=\"http://www.bliki.info/wiki/S%C3%B6hnlein\" title=\"Söhnlein\">Söhnlein</a></p>");
        Set<String> set = wikiModel.getLinks();
        assertThat(set).contains("Söhnlein");
        assertThat(set).contains("Henkell & Söhnlein");
    }

    @Test public void testLink13() {
        assertThat(wikiModel.render("test [[lets start a [[nested]] link]] test", false)).isEqualTo("\n"
                + "<p>test [[lets start a <a href=\"http://www.bliki.info/wiki/Nested\" title=\"Nested\">nested</a> link]] test</p>");
        Set<String> set = wikiModel.getLinks();
        assertThat(set).contains("nested");
    }

    @Test public void testLink14() {
        assertThat(wikiModel.render("Dolphins are [[aquatic mammal]]s that are closely related to [[whale]]s and [[porpoise]]s.", false)).isEqualTo("\n"
                + "<p>Dolphins are <a href=\"http://www.bliki.info/wiki/Aquatic_mammal\" title=\"Aquatic mammal\">aquatic mammals</a> that are closely related to <a href=\"http://www.bliki.info/wiki/Whale\" title=\"Whale\">whales</a> and <a href=\"http://www.bliki.info/wiki/Porpoise\" title=\"Porpoise\">porpoises</a>.</p>");
    }

    @Test public void testLink15() {
        assertThat(wikiModel.render("[[main Page]]", false)).isEqualTo("\n<p><a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">main Page</a></p>");
    }

    @Test public void testLink16() {
        assertThat(wikiModel.render("[[main  Page]]", false)).isEqualTo("\n<p><a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">main  Page</a></p>");
    }

    @Test public void testLink17() {
        assertThat(wikiModel.render("[[main__Page]]", false)).isEqualTo("\n<p><a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">main__Page</a></p>");
    }

    @Test public void testLink18() {
        assertThat(wikiModel.render("[[main_ Page]]", false)).isEqualTo("\n<p><a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">main_ Page</a></p>");
    }

    @Test public void testInterwiki1() {
        assertThat(wikiModel
                .render("[[de:Johann Wolfgang von Goethe|Goethe]]s Faust", false)).isEqualTo("\n" + "<p><a href=\"http://de.wikipedia.org/wiki/Johann_Wolfgang_von_Goethe\">Goethes</a> Faust</p>");
    }

    @Test public void testInterwiki2() {
        assertThat(wikiModel.render(
                "[[intra:page/directory|Page directory]]", false)).isEqualTo("\n" + "<p><a href=\"/page/directory\">Page directory</a></p>");
    }

    @Test public void testSectionLink01() {
        assertThat(wikiModel.render("[[#Section Link|A Section Link]]",
                false)).isEqualTo("\n" + "<p><a href=\"#Section_Link\">A Section Link</a></p>");
    }

    @Test public void testSectionLink02() {
        assertThat(wikiModel.render("[[#Sectionäöü]]", false)).isEqualTo("\n" + "<p><a href=\"#Section.C3.A4.C3.B6.C3.BC\">#Sectionäöü</a></p>");
    }

    /**
     * See issue 101
     */
    @Test public void testSectionLink03() {
        assertThat(wikiModel.render("[[#Section Link]]", false)).isEqualTo("\n" + "<p><a href=\"#Section_Link\">#Section Link</a></p>");
    }

    @Test public void testSpecialLink01() {
        assertThat(wikiModel.render("* [[Special:Specialpages|Special Pages]]", false)).isEqualTo("\n"
                + "<ul>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Special:Specialpages\" title=\"Special:Specialpages\">Special Pages</a></li>\n</ul>");
    }

    @Test public void testSubLink01() {
        assertThat(wikiModel.render("[[test/testing]]", false)).isEqualTo("\n" + "<p><a href=\"http://www.bliki.info/wiki/Test/testing\" title=\"Test/testing\">test/testing</a></p>");
    }

    //
    @Test public void testSubLink04() {
        assertThat(wikiModel.render("[[Hello World?id=42]]", false)).isEqualTo("\n"
                + "<p><a href=\"http://www.bliki.info/wiki/Hello_World%3Fid%3D42\" title=\"Hello World?id=42\">Hello World?id=42</a></p>");
    }

    @Test public void testRedirect01() {
        assertThat(wikiModel.render("#REDIRECT [[Official position]]", false)).isEqualTo("");
        assertThat(wikiModel.getRedirectLink()).isEqualTo("Official position");
    }

    @Test public void testRedirect02() {
        assertThat(wikiModel.render(" \n  #REDIRECT[[Official position]] bla \n other blabls", false)).isEqualTo("");
        assertThat(wikiModel.getRedirectLink()).isEqualTo("Official position");
    }

    @Test public void testRedirect03() {
        assertThat(wikiModel.render(" \n{{TestRedirect1}}", false)).isEqualTo(" \n" + "<p>Hello World!\n</p>");
    }

    @Test public void testRedirect04() {
        assertThat(wikiModel.render(" \n {{TestRedirect1}} ", false)).isEqualTo(" \n" + "<pre>" + "Hello World!\n\n" + "</pre>");
    }

    @Test public void testRedirect05() {
        assertThat(wikiModel.render("#Redirect [[El Niño-Southern Oscillation]]", false)).isEqualTo("");
        assertThat(wikiModel.getRedirectLink()).isEqualTo("El Niño-Southern Oscillation");
    }

    @Test public void testPlainTextConverter001() {
        assertThat(wikiModel.render(new PlainTextConverter(), "An [http://www.example.com external link]. ",
                false)).isEqualTo("\n" +
                "An external link. ");
    }

    @Test public void testPlainTextConverter002() {
        String wikitext = "The '''Eiffel Tower''',{{IPA-fr|tuʀ ɛfɛl|}}"
                + "<!--Note: French does not have tonic accents, so do not add stress marks to this pronunciation-->)"
                + " is a 19th century ";

        assertThat(wikiModel.render(
                new PlainTextConverter(), wikitext, false)).isEqualTo("\n" +
                "The Eiffel Tower,[tuʀ ɛfɛl]) is a 19th century ");
    }

    @Test public void testPlainTextConverter003() {
        String wikitext = "The '''Eiffel Tower''',{{IPA-fr|tuʀ ɛfɛl}}"
                + "<!--Note: French does not have tonic accents, so do not add stress marks to this pronunciation-->)"
                + " is a 19th century ";

        assertThat(wikiModel.render(
                new PlainTextConverter(), wikitext, false)).isEqualTo("\n" +
                "The Eiffel Tower,French pronunciation: [tuʀ ɛfɛl]) is a 19th century ");
    }

    @Test public void testPlainTextConverter004() {
        String wikitext = "The '''Eiffel Tower''',{{IPA-fr|tuʀ ɛfɛl|lang}}"
                + "<!--Note: French does not have tonic accents, so do not add stress marks to this pronunciation-->)"
                + " is a 19th century ";

        assertThat(wikiModel.render(
                new PlainTextConverter(), wikitext, false)).isEqualTo("\n" +
                "The Eiffel Tower,French: [tuʀ ɛfɛl]) is a 19th century ");
    }

    @Test public void testPlainTextConverter005() {
        String wikitext = "The '''Eiffel Tower''',{{IPA-fr|tuʀ ɛfɛl| }}"
                + "<!--Note: French does not have tonic accents, so do not add stress marks to this pronunciation-->)"
                + " is a 19th century ";

        assertThat(wikiModel.render(
                new PlainTextConverter(), wikitext, false)).isEqualTo("\n" +
                "The Eiffel Tower,[tuʀ ɛfɛl]) is a 19th century ");
    }
    // public static void main(String[] args) {
    // String test = Encoder.encode("erreäöü öüä++", ".");
    // System.out.println(test);
    // }
}

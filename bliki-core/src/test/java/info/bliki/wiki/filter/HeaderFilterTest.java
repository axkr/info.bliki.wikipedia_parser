package info.bliki.wiki.filter;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class HeaderFilterTest extends FilterTestSupport {

    @Test public void testBad01() {
        assertThat(wikiModel.render("=", false)).isEqualTo("\n" + "<p>=</p>");
    }

    @Test public void testBad02() {
        assertThat(wikiModel.render("=\n", false)).isEqualTo("\n<p>=\n</p>");
    }

    @Test public void testBad03() {
        assertThat(wikiModel.render("==  \r  \n", false)).isEqualTo("<h1><span class=\"mw-headline\" id=\"\" /></h1>\n" + "");
    }

    @Test public void testH2OneCharacter() {
        assertThat(wikiModel.render("==a==", false)).isEqualTo("<h2><span class=\"mw-headline\" id=\"a\">a</span></h2>");
        assertThat(wikiModel.render("line 1\n==a==", false)).isEqualTo("\n" +
                "<p>line 1\n" +
                "</p><h2><span class=\"mw-headline\" id=\"a\">a</span></h2>");
        assertThat(wikiModel.render("===a===", false)).isEqualTo("<h3><span class=\"mw-headline\" id=\"a\">a</span></h3>");
        assertThat(wikiModel.render("line 1\n===a===", false)).isEqualTo("\n" +
                "<p>line 1\n" +
                "</p><h3><span class=\"mw-headline\" id=\"a\">a</span></h3>");
    }
    @Test public void testH2() {
        assertThat(wikiModel.render("==Text==", false)).isEqualTo("<h2><span class=\"mw-headline\" id=\"Text\">Text</span></h2>");
    }

    @Test public void testH6Whitespace() {
        assertThat(wikiModel.render("=======Text Übersicht=======   \r \nA new line.", false)).isEqualTo("<h6><span class=\"mw-headline\" id=\"Text_.C3.9Cbersicht\">Text Übersicht</span></h6>\n" + "<p>A new line.</p>");
    }

    @Test public void testH2WithPunctuation() {
        assertThat(wikiModel.render("==Text Übersicht==:", false)).isEqualTo("\n" + "<p>==Text Übersicht==:</p>");
    }

    @Test public void testH2Apostrophe() {
        assertThat(wikiModel.render("==Wikipedia's sister projects==", false)).isEqualTo("<h2><span class=\"mw-headline\" id=\"Wikipedia.27s_sister_projects\">Wikipedia&#39;s sister projects</span></h2>");
    }

    @Test public void testH2Link01() {
        assertThat(wikiModel.render("__FORCETOC__ \n==Text [[Overview|Übersicht]]==", false)).isEqualTo(" \n"
                + "<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n"
                + "<tr>\n"
                + "<td>\n"
                + "<div id=\"toctitle\">\n"
                + "<h2>Contents</h2>\n"
                + "</div>\n"
                + "<ul>\n"
                + "<ul>\n"
                + "<li class=\"toclevel-1\"><a href=\"#Text_.C3.9Cbersicht\">Text Übersicht</a>\n"
                + "</li>\n"
                + "</ul>\n"
                + "</ul></td></tr></table><hr/>\n"
                + "<h2><span class=\"mw-headline\" id=\"Text_.C3.9Cbersicht\">Text <a href=\"http://www.bliki.info/wiki/Overview\" title=\"Overview\">Übersicht</a></span></h2>");
    }

    @Test public void testH3Link01() {
        assertThat(wikiModel.render("__FORCETOC__ \n===[[Help:Table Caption|Captions]]===", false)).isEqualTo(" \n"
                + "<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n"
                + "<tr>\n"
                + "<td>\n"
                + "<div id=\"toctitle\">\n"
                + "<h2>Contents</h2>\n"
                + "</div>\n"
                + "<ul>\n"
                + "<ul>\n"
                + "<ul>\n"
                + "<li class=\"toclevel-1\"><a href=\"#Captions\">Captions</a>\n"
                + "</li>\n"
                + "</ul>\n"
                + "</ul>\n"
                + "</ul></td></tr></table><hr/>\n"
                + "<h3><span class=\"mw-headline\" id=\"Captions\"><a href=\"http://www.bliki.info/wiki/Help:Table_Caption\" title=\"Help:Table Caption\">Captions</a></span></h3>");
    }

    @Test public void testH2Multiple() {
        assertThat(wikiModel.render("__FORCETOC__ \n==Head 1==\nA first line.\n==Head 1==\nA second line.\n==Head 1==\nA third line.", false)).isEqualTo(" \n" +
                "<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" +
                "<tr>\n" +
                "<td>\n" +
                "<div id=\"toctitle\">\n" +
                "<h2>Contents</h2>\n" +
                "</div>\n" +
                "<ul>\n" +
                "<ul>\n" +
                "<li class=\"toclevel-1\"><a href=\"#Head_1\">Head 1</a>\n" +
                "</li>\n" +
                "<li class=\"toclevel-1\"><a href=\"#Head_1_2\">Head 1</a>\n" +
                "</li>\n" +
                "<li class=\"toclevel-1\"><a href=\"#Head_1_3\">Head 1</a>\n" +
                "</li>\n" +
                "</ul>\n" +
                "</ul></td></tr></table><hr/>\n" +
                "<h2><span class=\"mw-headline\" id=\"Head_1\">Head 1</span></h2>\n" +
                "<p>A first line.\n" +
                "</p><h2><span class=\"mw-headline\" id=\"Head_1_2\">Head 1</span></h2>\n" +
                "<p>A second line.\n" +
                "</p><h2><span class=\"mw-headline\" id=\"Head_1_3\">Head 1</span></h2>\n" +
                "<p>A third line.</p>");
    }
}

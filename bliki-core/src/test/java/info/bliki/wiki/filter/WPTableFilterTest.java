package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WPTableFilterTest extends FilterTestSupport {
    // unclosed outer table:
    public static final String TEST = "{| width=\"100%\" cellspacing=\"3\" cellpadding=\"6\"   \n"
            + "|- valign=\"top\"   \n"
            + "| width=\"40%\" bgcolor=\"#FFF4F4\" style=\"border: solid 1px #ffc9c9; padding:1em;\" cellpadding=\"0\" cellspacing=\"0\" |   \n"
            + "{| cellspacing=\"0\" cellpadding=\"0\"   \n" + "| bgcolor=\"#FFF4F4\" |       \n"
            + "\'\'\'Plog4u.org\'\'\' is dedicated to developing a Wikipedia Eclipse Plugin\n" + "\n" + "|}   \n"
            + "| width=\"60%\" bgcolor=\"#f0f0ff\" style=\"border: 1px solid #C6C9FF; padding: 1em;\" |   \n"
            + "{| cellspacing=\"0\" cellpadding=\"6\"   \n" + "| bgcolor=\"#f0f0ff\" |\n" + "\n" + "|}\n" + "\n" + "";

    public static final String TEST2 = "{| style=&quot;width:100%; border-top: 1px solid black; border-bottom: 1px solid black;&quot;\n"
            + "|style=&quot;width:60%; text-align:right; font-size: 100%;background:#EEEEEE; color: black; padding-left: 3px; padding-right: 3px;&quot;|\n"
            + "\n"
            + "[[Wikinews:Willkommen|Willkommen]]\n"
            + "&lt;br /&gt;\n"
            + "&lt;div style=&quot;white-space: nowrap;&quot;&gt; \n"
            + "[[Wikinews:Schreibe eine Nachricht|Mach mit!]] |\n"
            + "[[Hilfe:Erste Schritte|Erste Schritte]] |\n"
            + "[[Hilfe:Zweite Schritte|Zweite Schritte]] |\n"
            + "[[Wikinews:Ententeich|Ententeich]] | \n"
            + "[[Hilfe:FAQ|FAQ]] |\n"
            + "[[:Kategorie:!Hauptkategorie|Index]] | \n"
            + "&lt;span class=&quot;plainlinks&quot;&gt;[http://tools.wikimedia.de/~dapete/wikinews-rss/rss-de.php&lt;span style=&quot;color: #FFFFFF; background-color: #FF5500; background-image: none !important; border-color: #FF5500; border-style: outset; text-decoration: none !important; padding-left: 0.2em; padding-right: 0.2em; border-width: 0.15em; font-size: 95%; line-height: 95%; font-family: verdana, sans-serif; font-weight: bold;&quot;&gt;RSS&lt;/span&gt;]&lt;/span&gt;\n"
            + "|style=&quot;width:40%; padding-left: 10px; padding-top: 15px;&quot;|\n"
            + "&lt;div style=&quot;font-size: 285%;&quot;&gt;&lt;span style=&quot;color: #444444;&quot;&gt;\'\'\'WIKI\'\'\'&lt;/span&gt;&lt;span style=&quot;color: #999999;&quot;&gt;\'\'\'NEWS\'\'\'&lt;/span&gt;&lt;/div&gt;\n"
            + "&lt;span style=&quot;font-size: 90%;&quot;&gt;{{CURRENTDAY}}. {{CURRENTMONTHNAMEGEN}} {{CURRENTYEAR}} {{CURRENTTIME}} UTC &lt;/span&gt;\n"
            + "|}";

    public static final String TEST3 = "=== Simple example ===\n"
            + "Both of these generate the same output.  Choose a style based on the number of cells in each row and the total text inside each cell.\n"
            + "\n" + "\'\'\'Wiki markup\'\'\'\n"
            + "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\"><pre><nowiki>\n" + "{| \n"
            + "| A \n" + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "</nowiki></pre></blockquote>\n" + "\n"
            + "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\"><pre><nowiki>\n" + "{| \n"
            + "| A || B\n" + "|- \n" + "| C || D \n" + "|}\n" + "</nowiki></pre></blockquote>\n" + "\n"
            + "\'\'\'What it looks like in your browser\'\'\'\n"
            + "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" + "{| \n" + "| A\n"
            + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "</blockquote>\n";

    public static final String TEST5 = "<table><tr></tr><tr><td>&#160;</td><td width=\"48%\">\n"
            + "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n"
            + "{| class=\"wikitable\"\n" + "|+Multiplication table\n" + "|-\n" + "! &times; !! 1 !! 2 !! 3\n" + "|-\n" + "! 1\n"
            + "| 1 || 2 || 3\n" + "|-\n" + "! 2\n" + "| 2 || 4 || 6\n" + "|-\n" + "! 3\n" + "| 3 || 6 || 9\n" + "|-\n" + "! 4\n"
            + "| 4 || 8 || 12\n" + "|-\n" + "! 5\n" + "| 5 || 10 || 15\n" + "|}\n" + "</blockquote></td></tr></table>\n";

    public static final String TEST6 = "=== Nested tables ===\n"
            + "This shows one table (in blue) nested inside another table\'s cell2.  \'\'Nested tables have to start on a new line.\'\'\n"
            + "\n"
            + "\'\'\'Wiki markup\'\'\'\n"
            + "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\"><p style=\"border: 1px dashed #2f6fab; background-color: #f9f9f9;\"><tt>\n"
            + "<nowiki>{|</nowiki> border=\"1\"<br />\n" + "| &amp;alpha;<br />\n" + "| align=\"center\" | cell2<br />\n"
            + "<span style=\"color: navy;\">\n"
            + "\'\'\'<nowiki>{|</nowiki> border=\"2\" style=\"background-color:#ABCDEF;\"\'\'\'<br />\n" + "\'\'\'| NESTED\'\'\'<br />\n"
            + "\'\'\'|-\'\'\'<br />\n" + "\'\'\'| TABLE\'\'\'<br />\n" + "\'\'\'<nowiki>|}</nowiki>\'\'\'<br />\n" + "</span>\n"
            + "| valign=\"bottom\" | the original table again<br />\n" + "<nowiki>|}</nowiki>\n" + "</tt></p></blockquote>\n" + "\n"
            + "\'\'\'What it looks like in your browser\'\'\'\n"
            + "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" + "{| border=\"1\"\n"
            + "| &alpha;\n" + "| align=\"center\" | cell2\n" + "{| border=\"2\" style=\"background-color:#ABCDEF;\"\n" + "| NESTED\n"
            + "|-\n" + "| TABLE\n" + "|}\n" + "| valign=\"bottom\" | the original table again\n" + "|}\n" + "</blockquote>\n" + "\n" + "";


    @Test public void testNestedTable1() throws Exception {
        assertThat(wikiModel
                .render(TEST, false)).isEqualTo("\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table cellpadding=\"6\" cellspacing=\"3\" width=\"100%\">\n" +
                "<tr valign=\"top\">\n" +
                "<td bgcolor=\"#FFF4F4\" cellpadding=\"0\" cellspacing=\"0\" style=\"border: solid 1px #ffc9c9; padding:1em;\" width=\"40%\">\n" +
                "\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\">\n" +
                "<tr>\n" +
                "<td bgcolor=\"#FFF4F4\">\n" +
                "<b>Plog4u.org</b> is dedicated to developing a Wikipedia Eclipse Plugin\n" +
                "</td></tr></table></div>   </td>\n" +
                "<td bgcolor=\"#f0f0ff\" style=\"border: 1px solid #C6C9FF; padding: 1em;\" width=\"60%\">\n" +
                "\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table cellpadding=\"6\" cellspacing=\"0\">\n" +
                "<tr>\n" +
                "<td bgcolor=\"#f0f0ff\">\n" +
                "</td></tr></table></div>\n" +
                "\n" +
                "</td></tr></table></div>");
    }

    @Test public void testNestedTable2() throws Exception {
        assertThat(wikiModel.render("{| border=1\n" + "|[[Test Link|Test]]\n"
                + "| &alpha;\n" + "|\n" + "{| bgcolor=#ABCDEF border=2\n" + "|nested\n" + "|-\n" + "|table\n" + "|}\n"
                + "|the original table again\n" + "|}", false)).isEqualTo("\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table border=\"1\">\n" +
                "<tr>\n" +
                "<td><a href=\"http://www.bliki.info/wiki/Test_Link\" title=\"Test Link\">Test</a></td>\n" +
                "<td>α</td>\n" +
                "<td>\n" +
                "\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table bgcolor=\"#ABCDEF\" border=\"2\">\n" +
                "<tr>\n" +
                "<td>nested</td></tr>\n" +
                "<tr>\n" +
                "<td>table</td></tr></table></div></td>\n" +
                "<td>the original table again</td></tr></table></div>");
    }

    @Test public void testBlockquoteTable01() throws Exception {
        assertThat(wikiModel.render(TEST3, false)).isEqualTo("<h3><span class=\"mw-headline\" id=\"Simple_example\">Simple example</span></h3>\n" +
                "<p>Both of these generate the same output.  Choose a style based on the number of cells in each row and the total text inside each cell.</p>\n" +
                "<p><b>Wiki markup</b>\n" +
                "</p><blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" +
                "<pre>\n" +
                "{| \n" +
                "| A \n" +
                "| B\n" +
                "|- \n" +
                "| C\n" +
                "| D\n" +
                "|}\n" +
                "</pre>\n</blockquote>\n" +
                "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" +
                "<pre>\n" +
                "{| \n" +
                "| A || B\n" +
                "|- \n" +
                "| C || D \n" +
                "|}\n" +
                "</pre>\n</blockquote>\n" +
                "\n" +
                "<p><b>What it looks like in your browser</b>\n" +
                "</p><blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" +
                "\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table>\n" +
                "<tr>\n" +
                "<td>A</td>\n" +
                "<td>B</td></tr>\n" +
                "<tr>\n" +
                "<td>C</td>\n" +
                "<td>D</td></tr></table></div>\n</blockquote>\n" +
                "");
    }

    @Test public void testBlockquoteTable02() throws Exception {
        assertThat(wikiModel.render("<table><tr> \n" + "<td></td>\n\n</tr>\n</table>\n", false)).isEqualTo("\n" + "<table>\n" + "\n" + "<tr>\n" + " \n" + "\n" + "\n" + "\n" + "</tr>\n" + "\n" + "</table>\n"
                + "");
    }

    @Test public void testBlockquoteTable03() throws Exception {
        assertThat(wikiModel.render("<table><tr> \n" + "<td></td><tr><td></tr></table>\n", false)).isEqualTo("\n" +
                "<table>\n" +
                "\n" +
                "<tr>\n" +
                " \n" +
                "\n" +
                "</tr>\n" +
                "<tr>\n" +
                "\n" +
                "</tr>\n" +
                "</table>\n" +
                "" + "");
    }

    @Test public void testBlockquoteTable04() throws Exception {
        assertThat(wikiModel.render("<table><tr> \n"
                + "<td>number 1<tr><td>number2</table>\n", false)).isEqualTo("\n" + "<table>\n" + "\n" + "<tr>\n" + " \n" + "\n" + "<td>number 1</td>\n" + "</tr>\n" + "<tr>\n" + "\n"
                + "<td>number2\n" + "</td>\n" + "</tr>\n" + "</table>");
    }

    @Test public void testBlockquoteTable05() throws Exception {
        assertThat(wikiModel.render(TEST5, false)).isEqualTo("\n" +
                "<table>\n" +
                "\n" +
                "<tr>\n" +
                "\n" +
                "<td> </td>\n" +
                "<td width=\"48%\"><blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n" +
                "\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table class=\"wikitable\">\n" +
                "<caption>Multiplication table</caption>\n" +
                "<tr>\n" +
                "<th>× </th>\n" +
                "<th>1 </th>\n" +
                "<th>2 </th>\n" +
                "<th>3</th></tr>\n" +
                "<tr>\n" +
                "<th>1</th>\n" +
                "<td>1 </td>\n" +
                "<td>2 </td>\n" +
                "<td>3</td></tr>\n" +
                "<tr>\n" +
                "<th>2</th>\n" +
                "<td>2 </td>\n" +
                "<td>4 </td>\n" +
                "<td>6</td></tr>\n" +
                "<tr>\n" +
                "<th>3</th>\n" +
                "<td>3 </td>\n" +
                "<td>6 </td>\n" +
                "<td>9</td></tr>\n" +
                "<tr>\n" +
                "<th>4</th>\n" +
                "<td>4 </td>\n" +
                "<td>8 </td>\n" +
                "<td>12</td></tr>\n" +
                "<tr>\n" +
                "<th>5</th>\n" +
                "<td>5 </td>\n" +
                "<td>10 </td>\n" +
                "<td>15</td></tr></table></div>\n</blockquote></td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "");
    }

    @Test public void testBlockquoteTable06() throws Exception {
        assertThat(wikiModel
                .render("<table align=\"center\" border=\"1\" cellspacing=\"0\" cellpadding=\"3\">\n" + "   <tr>\n" + "      <td>1</td>\n"
                        + "      <td>2</td>\n" + "   </tr> \n" + "   <tr>\n" + "      <td>3</td> \n" + "      <td>4</td> \n" + "   </tr>\n"
                        + "</table>\n", false)).isEqualTo("\n" +
                "<table align=\"center\" border=\"1\" cellpadding=\"3\" cellspacing=\"0\">\n" +
                "\n" +
                "   \n" +
                "<tr>\n" +
                "\n" +
                "      \n" +
                "<td>1</td>\n" +
                "      \n" +
                "<td>2</td>\n" +
                "   \n" +
                "</tr> \n" +
                "   \n" +
                "<tr>\n" +
                "\n" +
                "      \n" +
                "<td>3</td> \n" +
                "      \n" +
                "<td>4</td> \n" +
                "   \n" +
                "</tr>\n" +
                "\n" +
                "</table>\n" +
                "");
    }

    @Test public void testMathTable1() throws Exception {
        assertThat(wikiModel.render("{| border=1\n" + "|-\n" + "|<math>\\frac{1}{|a|} G \\left( \\frac{\\omega}{a} \\right)\\,</math>\n"
                + "|}", false)).isEqualTo("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table border=\"1\">\n" + "<tr>\n"
                + "<td><span class=\"math\">\\frac{1}{|a|} G \\left( \\frac{\\omega}{a} \\right)\\,</span></td></tr></table></div>");
    }

    @Test public void testAll() throws Exception {
        assertThat(wikiModel
                .render(TEST, false)).isEqualTo("\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table cellpadding=\"6\" cellspacing=\"3\" width=\"100%\">\n" +
                "<tr valign=\"top\">\n" +
                "<td bgcolor=\"#FFF4F4\" cellpadding=\"0\" cellspacing=\"0\" style=\"border: solid 1px #ffc9c9; padding:1em;\" width=\"40%\">\n" +
                "\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\">\n" +
                "<tr>\n" +
                "<td bgcolor=\"#FFF4F4\">\n" +
                "<b>Plog4u.org</b> is dedicated to developing a Wikipedia Eclipse Plugin\n" +
                "</td></tr></table></div>   </td>\n" +
                "<td bgcolor=\"#f0f0ff\" style=\"border: 1px solid #C6C9FF; padding: 1em;\" width=\"60%\">\n" +
                "\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table cellpadding=\"6\" cellspacing=\"0\">\n" +
                "<tr>\n" +
                "<td bgcolor=\"#f0f0ff\">\n" +
                "</td></tr></table></div>\n" +
                "\n" +
                "</td></tr></table></div>");

        assertThat(wikiModel.render("{| border=1\n" + "|[[Test Link|Test]]\n"
                + "| &alpha;\n" + "|\n" + "{| bgcolor=#ABCDEF border=2\n" + "|nested\n" + "|-\n" + "|table\n" + "|}\n"
                + "|the original table again\n" + "|}", false)).isEqualTo("\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table border=\"1\">\n" +
                "<tr>\n" +
                "<td><a href=\"http://www.bliki.info/wiki/Test_Link\" title=\"Test Link\">Test</a></td>\n" +
                "<td>α</td>\n" +
                "<td>\n" +
                "\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table bgcolor=\"#ABCDEF\" border=\"2\">\n" +
                "<tr>\n" +
                "<td>nested</td></tr>\n" +
                "<tr>\n" +
                "<td>table</td></tr></table></div></td>\n" +
                "<td>the original table again</td></tr></table></div>");

        assertThat(wikiModel.render("{| border=1\n" + "|-\n" + "|<math>\\frac{1}{|a|} G \\left( \\frac{\\omega}{a} \\right)\\,</math>\n"
                + "|}", false)).isEqualTo("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table border=\"1\">\n" + "<tr>\n"
                + "<td><span class=\"math\">\\frac{1}{|a|} G \\left( \\frac{\\omega}{a} \\right)\\,</span></td></tr></table></div>");

        assertThat(wikiModel
                .render("{| class=\"wikitable\" style=\"text-align:left\"\n"
                        + "|+align=\"bottom\"|My special table caption for {{{TopicName|<Name of Topic>}}}\n" + "|-\n" + "|\'\'\'Start\'\'\'\n"
                        + "| colspan=\"3\" | {{{Period|<From Date - To Date>}}}\n" + "|-\n" + "|}", false)).isEqualTo("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table class=\"wikitable\" style=\"text-align:left\">\n"
                + "<caption align=\"bottom\">My special table caption for &#60;Name of Topic&#62;</caption>\n" + "<tr>\n"
                + "<td><b>Start</b></td>\n" + "<td colspan=\"3\">&#60;From Date - To Date&#62;</td></tr></table></div>");
    }

    @Test public void testTHTableMix001() throws Exception {
        assertThat(wikiModel
                .render("{| class=\"infobox bordered vcard\" style=\"width: 25em; text-align: left; font-size: 95%;\"\n"
                        + "! colspan=\"2\" style=\"text-align:center; font-size:larger;\" class=\"fn\"| Chris Capuano\n"
                        + "|-\n"
                        + "<th colspan=\"2\" style=\"text-align:center;\">[[Image:Cap.jpg|300px]]<br></th>\n"
                        + "|-\n"
                        + "| colspan=\"2\" style=\"text-align:center; background: #042462;\"| <span style=\"color:white;\" class=\"note\">'''<span style=\"color:white;\">Milwaukee Brewers</span> — No. 39'''</span>\n"
                        + "|- style=\"text-align: center;\"\n" + "| ! colspan=\"2\" | '''[[Starting pitcher]]'''\n"
                        + "|- style=\"text-align: center;\"\n" + "\n" + "|-\n"
                        + "<th colspan=\"2\" style=\"text-align:center;\">Born: [[18]] [[1978]]<span\n"
                        + "style=\"display:none\"> (<span class=\"bday\">1978-{{padleft:8}}-{{padleft:\n"
                        + "18}}</span>)</span><span class=\"noprint\"> (age&nbsp;29)</span></th> \n" + "|-\n" + "|}", false)).isEqualTo("\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table class=\"infobox bordered vcard\" style=\"width: 25em; text-align: left; font-size: 95%;\">\n" +
                "<tr>\n" +
                "<th class=\"fn\" colspan=\"2\" style=\"text-align:center; font-size:larger;\">Chris Capuano</th></tr>\n" +
                "<tr>\n" +
                "<th colspan=\"2\" style=\"text-align:center;\">\n" +
                "<a class=\"image\" href=\"http://www.bliki.info/wiki/Image:300px-Cap.jpg\" ><img src=\"http://www.bliki.info/wiki/300px-Cap.jpg\" width=\"300\" />\n" +
                "</a><br />\n" +
                "</th></tr>\n" +
                "<tr>\n" +
                "<td colspan=\"2\" style=\"text-align:center; background: #042462;\"><span class=\"note\" style=\"color:white;\"><b><span style=\"color:white;\">Milwaukee Brewers</span> — No. 39</b></span></td></tr>\n" +
                "<tr style=\"text-align: center;\">\n" +
                "<td colspan=\"2\"><b><a href=\"http://www.bliki.info/wiki/Starting_pitcher\" title=\"Starting pitcher\">Starting pitcher</a></b></td></tr>\n" +
                "<tr>\n" +
                "<th colspan=\"2\" style=\"text-align:center;\">\n" +
                "Born: <a href=\"http://www.bliki.info/wiki/18\" title=\"18\">18</a> <a href=\"http://www.bliki.info/wiki/1978\" title=\"1978\">1978</a><span style=\"display:none\"> (<span class=\"bday\">1978-8-18</span>)</span><span class=\"noprint\"> (age 29)</span>\n" +
                "</th> </tr></table></div>");
    }

    @Test public void testEmptyCellTable() throws Exception {
        assertThat(wikiModel
                .render("{|border=\"1\"\n" + "|\n" + "|a.\n" + "|-\n" + "|\n" + "|b.\n" + "|}\n" + "", false)).isEqualTo("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table border=\"1\">\n" + "<tr>\n" + "<td></td>\n"
                + "<td>a.</td></tr>\n" + "<tr>\n" + "<td></td>\n" + "<td>b.</td></tr></table></div>\n" + "");
    }

    @Test public void testBlockquoteTableText01() throws Exception {
        assertThat(wikiModel.render(new PlainTextConverter(), TEST3, false)).isEqualTo("\n" +
                "Simple example\n" +
                "Both of these generate the same output.  Choose a style based on the number of cells in each row and the total text inside each cell.\n" +
                "Wiki markup\n" +
                "\n" +
                "\n" +
                "<nowiki>\n" +
                "{| \n" +
                "| A \n" +
                "| B\n" +
                "|- \n" +
                "| C\n" +
                "| D\n" +
                "|}\n" +
                "</nowiki>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<nowiki>\n" +
                "{| \n" +
                "| A || B\n" +
                "|- \n" +
                "| C || D \n" +
                "|}\n" +
                "</nowiki>\n" +
                "\n" +
                "What it looks like in your browser\n" +
                "\n" +
                "\n" +
                "\n" +
                "A B \n" +
                "C D \n" +
                "\n");
    }

    @Test public void testWPTableText01() throws Exception {
        assertThat(wikiModel.render(new PlainTextConverter(), TEST, false)).isEqualTo("\n" + "\n" + "\n" + "\n" + "Plog4u.org is dedicated to developing a Wikipedia Eclipse Plugin\n" + "     \n"
                + "\n" + "\n" + " \n" + "\n" + " ");
    }

    @Test public void testUmlauts01() throws Exception {
        assertThat(wikiModel.render(new PlainTextConverter(), "Eine große''' <del>Überraschung</del>", false)).isEqualTo("\n" +
                "Eine große Überraschung");
    }

    @Test public void testTableCross() throws Exception {
        // 2009-11-03, from enwiki page "Cross"
        String raw = "{| class=\"wikitable\"\n" + "|-\n" + "! col1 !! col2 !! col3\n"
                + "|-| | '''[[lorem ipsum]]''' (test malformed table)\n" + "| | '''[[testing]]'''\n" + "|\n"
                + "This is a test of a malformed table.\n" + "|align=\"center\"|[[Image:test.png|140px]]\n" + "|}\n";
        String expected = "\n" +
                "<div style=\"page-break-inside: avoid;\">\n" +
                "<table class=\"wikitable\">\n" +
                "<tr>\n" +
                "<th>col1 </th>\n" +
                "<th>col2 </th>\n" +
                "<th>col3</th></tr>\n" +
                "<tr malformed=\"\">\n" +
                "<td><b><a href=\"http://www.bliki.info/wiki/Testing\" title=\"Testing\">testing</a></b></td>\n" +
                "<td>\n" +
                "This is a test of a malformed table.</td>\n" +
                "<td align=\"center\"><a class=\"image\" href=\"http://www.bliki.info/wiki/Image:140px-test.png\" ><img src=\"http://www.bliki.info/wiki/140px-test.png\" width=\"140\" />\n" +
                "</a></td></tr></table></div>\n" +
                "";
        assertThat(wikiModel.render(raw, false)).isEqualTo(expected);
    }

    @Test public void testSkipHtmlTagInCell() throws Exception {
        // the simplified output of Wiktionary's ru-verb module.
        String targetHtmlTag = "<span class=\"... pres|act|part-form-of ...\">";
        String raw ="{|\n" +
                "|-\n" +
                "| " + targetHtmlTag + " ... SOMETHING_TEXT\n" +
                "|}\n";

        String actual = wikiModel.render(raw, false);
        assertThat(actual).contains(targetHtmlTag); // must be preserve the HTML tag.
    }
}

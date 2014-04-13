package info.bliki.wiki.filter;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class BasicFilterTest extends FilterTestSupport {

    /**
     * Issue 149
     */
    @Test public void testIssue149() {
        assertThat(wikiModel.render("mit '''db2 connect to <db>''' mit der Datenbank v", false)).isEqualTo("\n" +
                "<p>mit <b>db2 connect to &#60;db&#62;</b> mit der Datenbank v</p>");
    }

    /**
     * Issue 135
     */
    @Test public void testIssue135() {
        assertThat(wikiModel.render("Test_", false)).isEqualTo("\n" +
                "<p>Test_</p>");
    }

    /**
     * Issue 135
     */
    @Test public void testIssue135b() {
        assertThat(wikiModel.render("Test__", false)).isEqualTo("\n" +
                "<p>Test__</p>");
    }
    /**
     * Issue 118
     */
    @Test public void testLiNoUl01() {
        assertThat(wikiModel.render(
                "<li>test1\n<li>test2", false)).isEqualTo("\n" + "<ul>\n" + "\n" + "<li>test1</li>\n" + "<li>test2</li>\n" + "</ul>");
    }

    /**
     * Issue 98
     */
    @Test public void testEmptyTags001() {
        assertThat(wikiModel.render("<s></s>", false)).isEqualTo("\n" + "<p></p>");
    }

    /**
     * Issue 98
     */
    @Test public void testEmptyTags002() {
        assertThat(wikiModel.render("<div class=\"ltrtxt\"></div>", false)).isEqualTo("");
    }

    /**
     * Issue 98
     */
    @Test public void testEmptyTags003() {
        assertThat(wikiModel.render("<br />", false)).isEqualTo("\n" + "<p><br /></p>");
    }

    /**
     * Issue 98
     */
    @Test public void testEmptyTags004() {
        assertThat(wikiModel.render("<hr />", false)).isEqualTo("<hr />");
    }

    @Test public void testTT() {
        assertThat(wikiModel.render("'''hosted by:'''<br>", false)).isEqualTo("\n" + "<p><b>hosted by:</b><br /></p>");
    }

    @Test public void testBlankInput() {
        assertThat(wikiModel.render("", false)).isEqualTo("");
    }

    @Test public void testNullInput() {
        assertThat(wikiModel.render(null, false)).isEqualTo("");
    }

    @Test public void testCharInput() {
        assertThat(wikiModel.render("[", false)).isEqualTo("\n" + "<p>[</p>");
    }

    @Test public void testParagraph1() {
        assertThat(wikiModel.render("This is a simple paragraph.", false)).isEqualTo("\n" + "<p>This is a simple paragraph.</p>");
    }

    @Test public void testParagraph2() {
        assertThat(wikiModel.render(
                "This is a simple paragraph.\n\nA second paragraph.", false)).isEqualTo("\n" + "<p>This is a simple paragraph.</p>\n" + "<p>A second paragraph.</p>");
    }

    @Test public void testParagraph3() {
        assertThat(wikiModel.render(
                "This is a simple paragraph.\n\nA second paragraph.", false)).isEqualTo("\n" + "<p>This is a simple paragraph.</p>\n" + "<p>A second paragraph.</p>");
    }

    @Test public void testNowiki01() {
        assertThat(wikiModel.render(
                "<nowiki>\n* This is not an unordered list item.</nowiki>", false)).isEqualTo("\n" + "<p>\n" + "* This is not an unordered list item.</p>");
    }

    @Test public void testNowiki02() {
        assertThat(wikiModel.render(
                "<noWiki>\n* This is not an unordered list item.</nowiKi>", false)).isEqualTo("\n" + "<p>\n" + "* This is not an unordered list item.</p>");
    }

    @Test public void testSimpleList() {
        assertThat(wikiModel.render("* Item 1\n" + "* Item 2",
                false)).isEqualTo("\n" + "<ul>\n" + "<li>Item 1</li>\n" + "<li>Item 2</li>\n</ul>");
    }

    @Test public void testSimpleTable() {
        assertThat(wikiModel.render("{|\n" + "|a\n|b\n" + "|}", false)).isEqualTo("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table>\n" + "<tr>\n" + "<td>a</td>\n"
                + "<td>b</td></tr></table></div>");
    }

    @Test public void testNOTOC() {
        assertThat(wikiModel.render("jhfksd __NOTOC__ sflkjsd", false)).isEqualTo("\n" + "<p>jhfksd  sflkjsd</p>");
    }

    @Test public void testWrongNOTOC() {
        assertThat(wikiModel.render("jhfksd __WRONGTOC__ sflkjsd", false)).isEqualTo("\n" + "<p>jhfksd  sflkjsd</p>");
    }

    @Test public void testbq1() {
        assertThat(wikiModel
                .render(
                        "<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n'''Hello World'''</blockquote>",
                        false)).isEqualTo("<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n"
                + "<p><b>Hello World</b></p>\n</blockquote>");
    }

    @Test public void testbq2() {
        assertThat(wikiModel.render("<blockquote>\n" + "The \'\'\'blockquote\'\'\' command formats block \n"
                        + "quotations, typically by surrounding them \n" + "with whitespace and a slightly different font.\n" + "</blockquote>\n",
                false
        )).isEqualTo("<blockquote>\n" + "<p>The <b>blockquote</b> command formats block \n"
                + "quotations, typically by surrounding them \n" + "with whitespace and a slightly different font.\n"
                + "</p>\n</blockquote>\n" + "");
    }

    @Test public void testbq3() {
        assertThat(wikiModel.render("<blockquote>start blockquote here\n" + "\n"
                + "line above me\n" + "no line above me and i am <b>bold</b>\n" + "\n" + "\n" + "and line above me\n"
                + "end of blockquote here</blockquote> ", false)).isEqualTo("<blockquote>start blockquote here\n" + "\n" + "<p>line above me\n"
                + "no line above me and i am <b>bold</b></p>\n" + "\n" + "<p>and line above me\n"
                + "end of blockquote here</p>\n</blockquote> ");
    }

    @Test public void testPreBlock() {
        assertThat(wikiModel.render(" * Lists are easy to do:\n" + " ** start every line\n"
                + " * with a star\n" + " ** more stars mean\n" + " *** deeper levels", false)).isEqualTo("\n<pre>* Lists are easy to do:\n" + "** start every line\n" + "* with a star\n" + "** more stars mean\n"
                + "*** deeper levels\n</pre>");
    }

    @Test public void testNestedPreBlock() {
        assertThat(wikiModel.render(
                "{|border=1 width=\"79%\"\n" + "!wikitext\n" + "|-\n" + "|\n" + " * Lists are easy to do:\n" + " ** start every line\n"
                        + " * with a star\n" + " ** more stars mean\n" + " *** deeper levels\n" + "|}", false
        )).isEqualTo("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table border=\"1\" width=\"79%\">\n" + "<tr>\n"
                + "<th>wikitext</th></tr>\n" + "<tr>\n" + "<td>\n" + "<pre>* Lists are easy to do:\n" + "** start every line\n"
                + "* with a star\n" + "** more stars mean\n" + "*** deeper levels\n</pre></td></tr></table></div>");
    }

    @Test public void testPBlock() {
        assertThat(wikiModel
                .render(
                        "<p style=\"padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;\"> <tt>\n"
                                + "&#60;p style=\"padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;\"> &#60;tt> <br />\n"
                                + "&amp;#123;&amp;#124; border=\"5\" cellspacing=\"5\" cellpadding=\"2\" &#60;br&nbsp;&#47;> <br />\n"
                                + "&amp;#124; style=\"text-align: center;\" &amp;#124; &amp;#91;&amp;#91;Image:gnome-system.png]] &#60;br&nbsp;&#47;> <br />\n"
                                + "&amp;#124;- &#60;br&nbsp;&#47;> <br />\n"
                                + "&amp;#33; Computer &#60;br&nbsp;&#47;> <br />\n"
                                + "&amp;#124;- &#60;br&nbsp;&#47;> <br />\n"
                                + "\'\'\'&amp;#124; style=\"color: yellow; background-color: green;\" &amp;#124; Processor Speed: &amp;#60;span style=\"color: red;\"> 1.8 GHz &amp;#60;/span> &#60;br&nbsp;&#47;>\'\'\' <br />\n"
                                + "&amp;#124;&amp;#125; &#60;br&nbsp;&#47;> <br />\n" + "&#60;/tt> &#60;/p>\n" + "</tt> </p>", false
                )).isEqualTo("\n"
                + "<p style=\"padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;\"> <tt>\n"
                + "&#60;p style=&#34;padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;&#34;&#62; &#60;tt&#62; <br />\n"
                + "&#38;#123;&#38;#124; border=&#34;5&#34; cellspacing=&#34;5&#34; cellpadding=&#34;2&#34; &#60;br /&#62; <br />\n"
                + "&#38;#124; style=&#34;text-align: center;&#34; &#38;#124; &#38;#91;&#38;#91;Image:gnome-system.png]] &#60;br /&#62; <br />\n"
                + "&#38;#124;- &#60;br /&#62; <br />\n"
                + "&#38;#33; Computer &#60;br /&#62; <br />\n"
                + "&#38;#124;- &#60;br /&#62; <br />\n"
                + "<b>&#38;#124; style=&#34;color: yellow; background-color: green;&#34; &#38;#124; Processor Speed: &#38;#60;span style=&#34;color: red;&#34;&#62; 1.8 GHz &#38;#60;/span&#62; &#60;br /&#62;</b> <br />\n"
                + "&#38;#124;&#38;#125; &#60;br /&#62; <br />\n" + "&#60;/tt&#62; &#60;/p&#62;\n" + "</tt> </p>");
    }

    @Test public void testALink001() {
        assertThat(wikiModel.render(
                "<a href=\"http://www.test.com\">Test2</a>", false)).isEqualTo("\n" + "<p><a href=\"http://www.test.com\" rel=\"nofollow\">Test2</a></p>");
    }

    @Test public void testXSS001() {
        assertThat(wikiModel.render("<h1 onmouseover=\"javascript:alert(\'yo\')\">Test</h1>", false)).isEqualTo("<h1>Test</h1>");
    }

    @Test public void testSignature01() {
        assertThat(wikiModel.render("a simple~~~~test", false)).isEqualTo("\n" + "<p>a simple~~~~test</p>");
    }

    @Test public void testSignature02() {
        assertThat(wikiModel.render("a simple~~~~", false)).isEqualTo("\n" + "<p>a simple~~~~</p>");
    }

    @Test public void testSignature03() {
        assertThat(wikiModel.render("a simple~~~~~test", false)).isEqualTo("\n" + "<p>a simple~~~~~test</p>");
    }

    @Test public void testSignature04() {
        assertThat(wikiModel.render("a simple~~~~~", false)).isEqualTo("\n" + "<p>a simple~~~~~</p>");
    }

    @Test public void testSignature05() {
        assertThat(wikiModel.render("a simple~~~test", false)).isEqualTo("\n" + "<p>a simple~~~test</p>");
    }

    @Test public void testSignature06() {
        assertThat(wikiModel.render("a simple~~~", false)).isEqualTo("\n" + "<p>a simple~~~</p>");
    }

    @Test public void testSignature07() {
        assertThat(wikiModel.render("~~~test", false)).isEqualTo("\n" + "<p>~~~test</p>");
    }

    @Test public void testSignature08() {
        assertThat(wikiModel.render("~~~", false)).isEqualTo("\n" + "<p>~~~</p>");
    }

    @Test public void testSpan001() {

        assertThat(wikiModel.render("<span class=\"xxx\"\n" + ">test</span>", false)).isEqualTo("\n" + "<p><span class=\"xxx\">test</span></p>");

    }

    @Test public void testReuseModel001() {
        wikiModel.setUp();
        try {
            assertThat(wikiModel
                    .render("= My Title 1=\n" + "__TOC__\n" + "== secA ==", false)).isEqualTo("<h1><span class=\"mw-headline\" id=\"My_Title_1\">My Title 1</span></h1>\n" +
                    "<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" +
                    "<tr>\n" +
                    "<td>\n" +
                    "<div id=\"toctitle\">\n" +
                    "<h2>Contents</h2>\n" +
                    "</div>\n" +
                    "<ul>\n" +
                    "<li class=\"toclevel-1\"><a href=\"#My_Title_1\">My Title 1</a>\n" +
                    "<ul>\n" +
                    "<li class=\"toclevel-2\"><a href=\"#secA\">secA</a>\n" +
                    "</li>\n" +
                    "</ul>\n" +
                    "</li>\n" +
                    "</ul></td></tr></table><hr/>\n" +
                    "\n" +
                    "<h2><span class=\"mw-headline\" id=\"secA\">secA</span></h2>");
        } finally {
            wikiModel.tearDown();
        }
        wikiModel.setUp();
        try {
            assertThat(wikiModel
                    .render("= My Title 2=\n" + "__TOC__\n" + "== secB ==", false)).isEqualTo("<h1><span class=\"mw-headline\" id=\"My_Title_2\">My Title 2</span></h1>\n" +
                    "<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" +
                    "<tr>\n" +
                    "<td>\n" +
                    "<div id=\"toctitle\">\n" +
                    "<h2>Contents</h2>\n" +
                    "</div>\n" +
                    "<ul>\n" +
                    "<li class=\"toclevel-1\"><a href=\"#My_Title_2\">My Title 2</a>\n" +
                    "<ul>\n" +
                    "<li class=\"toclevel-2\"><a href=\"#secB\">secB</a>\n" +
                    "</li>\n" +
                    "</ul>\n" +
                    "</li>\n" +
                    "</ul></td></tr></table><hr/>\n" +
                    "\n" +
                    "<h2><span class=\"mw-headline\" id=\"secB\">secB</span></h2>");
        } finally {
            wikiModel.tearDown();
        }

    }

    @Test public void testAbbr01() {
        assertThat(wikiModel.render("<abbr title=\"test\">[?]</abbr>", false)).isEqualTo("\n" + "<p>" + "<abbr title=\"test\">[?]</abbr></p>");
    }

    @Test public void testAbbr02() {
        assertThat(wikiModel.render(
                "<abbr title=\"<nowiki>test</nowiki>\">[?]</abbr>", false)).isEqualTo("\n" + "<p>" + "<abbr title=\"test\">[?]</abbr></p>");
    }

    @Test public void testIFrame01() {
        assertThat(wikiModel.render("<iframe name=\"inlineframe\" src=\"float.html\" frameborder=\"0\" scrolling=\"auto\" width=\"500\" height=\"180\" marginwidth=\"5\" marginheight=\"5\" >&nbsp;</iframe>", false)).isEqualTo("<iframe height=\"180\" name=\"inlineframe\" src=\"float.html\" width=\"500\"> </iframe>");
    }

    // TODO: should this work, too?!
    // @Test public void testAbbr03() {
    // assertEquals("\n" + "<p>" + "<abbr title=\"test\">[?]</abbr></p>",
    // wikiModel.render("<abbr title=\"<noWiki>test</nowiKi>\">[?]</abbr>",
    // false));
    // }
}

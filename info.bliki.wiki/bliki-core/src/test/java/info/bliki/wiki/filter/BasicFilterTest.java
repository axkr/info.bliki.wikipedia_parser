package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class BasicFilterTest extends FilterTestSupport {
	public BasicFilterTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public static Test suite() {
		return new TestSuite(BasicFilterTest.class);
	}

	
	/**
	 * Issue 149
	 */
	public void testIssue149() {
		assertEquals("\n" + 
				"<p>mit <b>db2 connect to &#60;db&#62;</b> mit der Datenbank v</p>", wikiModel.render("mit '''db2 connect to <db>''' mit der Datenbank v", false));
	}
	
	/**
	 * Issue 135
	 */
	public void testIssue135() {
		assertEquals("\n" + 
				"<p>Test_</p>", wikiModel.render("Test_", false));
	}
	
	/**
	 * Issue 135
	 */
	public void testIssue135b() {
		assertEquals("\n" + 
				"<p>Test__</p>", wikiModel.render("Test__", false));
	}
	/**
	 * Issue 118
	 */
	public void testLiNoUl01() {
		assertEquals("\n" + "<ul>\n" + "\n" + "<li>test1</li>\n" + "<li>test2</li>\n" + "</ul>", wikiModel.render(
				"<li>test1\n<li>test2", false));
	}

	/**
	 * Issue 98
	 */
	public void testEmptyTags001() {
		assertEquals("\n" + "<p></p>", wikiModel.render("<s></s>", false));
	}

	/**
	 * Issue 98
	 */
	public void testEmptyTags002() {
		assertEquals("", wikiModel.render("<div class=\"ltrtxt\"></div>", false));
	}

	/**
	 * Issue 98
	 */
	public void testEmptyTags003() {
		assertEquals("\n" + "<p><br /></p>", wikiModel.render("<br />", false));
	}

	/**
	 * Issue 98
	 */
	public void testEmptyTags004() {
		assertEquals("<hr />", wikiModel.render("<hr />", false));
	}

	public void testTT() {
		assertEquals("\n" + "<p><b>hosted by:</b><br /></p>", wikiModel.render("'''hosted by:'''<br>", false));
	}

	public void testBlankInput() {
		assertEquals("", wikiModel.render("", false));
	}

	public void testNullInput() {
		assertEquals("", wikiModel.render(null, false));
	}

	public void testCharInput() {
		assertEquals("\n" + "<p>[</p>", wikiModel.render("[", false));
	}

	public void testParagraph1() {
		assertEquals("\n" + "<p>This is a simple paragraph.</p>", wikiModel.render("This is a simple paragraph.", false));
	}

	public void testParagraph2() {
		assertEquals("\n" + "<p>This is a simple paragraph.</p>\n" + "<p>A second paragraph.</p>", wikiModel.render(
				"This is a simple paragraph.\n\nA second paragraph.", false));
	}

	public void testParagraph3() {
		assertEquals("\n" + "<p>This is a simple paragraph.</p>\n" + "<p>A second paragraph.</p>", wikiModel.render(
				"This is a simple paragraph.\n\nA second paragraph.", false));
	}

	public void testNowiki01() {
		assertEquals("\n" + "<p>\n" + "* This is not an unordered list item.</p>", wikiModel.render(
				"<nowiki>\n* This is not an unordered list item.</nowiki>", false));
	}

	public void testNowiki02() {
		assertEquals("\n" + "<p>\n" + "* This is not an unordered list item.</p>", wikiModel.render(
				"<noWiki>\n* This is not an unordered list item.</nowiKi>", false));
	}

	public void testSimpleList() {
		assertEquals("\n" + "<ul>\n" + "<li>Item 1</li>\n" + "<li>Item 2</li>\n</ul>", wikiModel.render("* Item 1\n" + "* Item 2",
				false));
	}

	public void testSimpleTable() {
		assertEquals("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table>\n" + "<tr>\n" + "<td>a</td>\n"
				+ "<td>b</td></tr></table></div>", wikiModel.render("{|\n" + "|a\n|b\n" + "|}", false));
	}

	public void testNOTOC() {
		assertEquals("\n" + "<p>jhfksd  sflkjsd</p>", wikiModel.render("jhfksd __NOTOC__ sflkjsd", false));
	}

	public void testWrongNOTOC() {
		assertEquals("\n" + "<p>jhfksd  sflkjsd</p>", wikiModel.render("jhfksd __WRONGTOC__ sflkjsd", false));
	}

	public void testbq1() {
		assertEquals(
				"<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n"
						+ "<p><b>Hello World</b></p>\n</blockquote>",
				wikiModel
						.render(
								"<blockquote style=\"background: white; border: 1px solid rgb(153, 153, 153); padding: 1em;\">\n'''Hello World'''</blockquote>",
								false));
	}

	public void testbq2() {
		assertEquals("<blockquote>\n" + "<p>The <b>blockquote</b> command formats block \n"
				+ "quotations, typically by surrounding them \n" + "with whitespace and a slightly different font.\n"
				+ "</p>\n</blockquote>\n" + "", wikiModel.render("<blockquote>\n" + "The \'\'\'blockquote\'\'\' command formats block \n"
				+ "quotations, typically by surrounding them \n" + "with whitespace and a slightly different font.\n" + "</blockquote>\n",
				false));
	}

	public void testbq3() {
		assertEquals("<blockquote>start blockquote here\n" + "\n" + "<p>line above me\n"
				+ "no line above me and i am <b>bold</b></p>\n" + "\n" + "<p>and line above me\n"
				+ "end of blockquote here</p>\n</blockquote> ", wikiModel.render("<blockquote>start blockquote here\n" + "\n"
				+ "line above me\n" + "no line above me and i am <b>bold</b>\n" + "\n" + "\n" + "and line above me\n"
				+ "end of blockquote here</blockquote> ", false));
	}

	public void testPreBlock() {
		assertEquals("\n<pre>* Lists are easy to do:\n" + "** start every line\n" + "* with a star\n" + "** more stars mean\n"
				+ "*** deeper levels\n</pre>", wikiModel.render(" * Lists are easy to do:\n" + " ** start every line\n"
				+ " * with a star\n" + " ** more stars mean\n" + " *** deeper levels", false));
	}

	public void testNestedPreBlock() {
		assertEquals("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table border=\"1\" width=\"79%\">\n" + "<tr>\n"
				+ "<th>wikitext</th></tr>\n" + "<tr>\n" + "<td>\n" + "<pre>* Lists are easy to do:\n" + "** start every line\n"
				+ "* with a star\n" + "** more stars mean\n" + "*** deeper levels\n</pre></td></tr></table></div>", wikiModel.render(
				"{|border=1 width=\"79%\"\n" + "!wikitext\n" + "|-\n" + "|\n" + " * Lists are easy to do:\n" + " ** start every line\n"
						+ " * with a star\n" + " ** more stars mean\n" + " *** deeper levels\n" + "|}", false));
	}

	public void testPBlock() {
		assertEquals(
				"\n"
						+ "<p style=\"padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;\"> <tt>\n"
						+ "&#60;p style=&#34;padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;&#34;&#62; &#60;tt&#62; <br />\n"
						+ "&#38;#123;&#38;#124; border=&#34;5&#34; cellspacing=&#34;5&#34; cellpadding=&#34;2&#34; &#60;br /&#62; <br />\n"
						+ "&#38;#124; style=&#34;text-align: center;&#34; &#38;#124; &#38;#91;&#38;#91;Image:gnome-system.png]] &#60;br /&#62; <br />\n"
						+ "&#38;#124;- &#60;br /&#62; <br />\n"
						+ "&#38;#33; Computer &#60;br /&#62; <br />\n"
						+ "&#38;#124;- &#60;br /&#62; <br />\n"
						+ "<b>&#38;#124; style=&#34;color: yellow; background-color: green;&#34; &#38;#124; Processor Speed: &#38;#60;span style=&#34;color: red;&#34;&#62; 1.8 GHz &#38;#60;/span&#62; &#60;br /&#62;</b> <br />\n"
						+ "&#38;#124;&#38;#125; &#60;br /&#62; <br />\n" + "&#60;/tt&#62; &#60;/p&#62;\n" + "</tt> </p>",
				wikiModel
						.render(
								"<p style=\"padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;\"> <tt>\n"
										+ "&#60;p style=\"padding: 1em; border: 1px dashed #2f6fab; color: Black; background-color: #f9f9f9; line-height: 1.1em;\"> &#60;tt> <br />\n"
										+ "&amp;#123;&amp;#124; border=\"5\" cellspacing=\"5\" cellpadding=\"2\" &#60;br&nbsp;&#47;> <br />\n"
										+ "&amp;#124; style=\"text-align: center;\" &amp;#124; &amp;#91;&amp;#91;Image:gnome-system.png]] &#60;br&nbsp;&#47;> <br />\n"
										+ "&amp;#124;- &#60;br&nbsp;&#47;> <br />\n"
										+ "&amp;#33; Computer &#60;br&nbsp;&#47;> <br />\n"
										+ "&amp;#124;- &#60;br&nbsp;&#47;> <br />\n"
										+ "\'\'\'&amp;#124; style=\"color: yellow; background-color: green;\" &amp;#124; Processor Speed: &amp;#60;span style=\"color: red;\"> 1.8 GHz &amp;#60;/span> &#60;br&nbsp;&#47;>\'\'\' <br />\n"
										+ "&amp;#124;&amp;#125; &#60;br&nbsp;&#47;> <br />\n" + "&#60;/tt> &#60;/p>\n" + "</tt> </p>", false));
	}

	public void testALink001() {
		assertEquals("\n" + "<p><a href=\"http://www.test.com\" rel=\"nofollow\">Test2</a></p>", wikiModel.render(
				"<a href=\"http://www.test.com\">Test2</a>", false));
	}

	public void testXSS001() {
		assertEquals("<h1>Test</h1>", wikiModel.render("<h1 onmouseover=\"javascript:alert(\'yo\')\">Test</h1>", false));
	}

	public void testSignature01() {
		assertEquals("\n" + "<p>a simple~~~~test</p>", wikiModel.render("a simple~~~~test", false));
	}

	public void testSignature02() {
		assertEquals("\n" + "<p>a simple~~~~</p>", wikiModel.render("a simple~~~~", false));
	}

	public void testSignature03() {
		assertEquals("\n" + "<p>a simple~~~~~test</p>", wikiModel.render("a simple~~~~~test", false));
	}

	public void testSignature04() {
		assertEquals("\n" + "<p>a simple~~~~~</p>", wikiModel.render("a simple~~~~~", false));
	}

	public void testSignature05() {
		assertEquals("\n" + "<p>a simple~~~test</p>", wikiModel.render("a simple~~~test", false));
	}

	public void testSignature06() {
		assertEquals("\n" + "<p>a simple~~~</p>", wikiModel.render("a simple~~~", false));
	}

	public void testSignature07() {
		assertEquals("\n" + "<p>~~~test</p>", wikiModel.render("~~~test", false));
	}

	public void testSignature08() {
		assertEquals("\n" + "<p>~~~</p>", wikiModel.render("~~~", false));
	}

	public void testSpan001() {

		assertEquals("\n" + "<p><span class=\"xxx\">test</span></p>", wikiModel.render("<span class=\"xxx\"\n" + ">test</span>", false));

	}

	public void testReuseModel001() {
		wikiModel.setUp();
		try {
			assertEquals("<h1><span class=\"mw-headline\" id=\"My_Title_1\">My Title 1</span></h1>\n" + 
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
					"<h2><span class=\"mw-headline\" id=\"secA\">secA</span></h2>", wikiModel
					.render("= My Title 1=\n" + "__TOC__\n" + "== secA ==", false));
		} finally {
			wikiModel.tearDown();
		}
		wikiModel.setUp();
		try {
			assertEquals("<h1><span class=\"mw-headline\" id=\"My_Title_2\">My Title 2</span></h1>\n" + 
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
					"<h2><span class=\"mw-headline\" id=\"secB\">secB</span></h2>", wikiModel
					.render("= My Title 2=\n" + "__TOC__\n" + "== secB ==", false));
		} finally {
			wikiModel.tearDown();
		}

	}

	public void testAbbr01() {
		assertEquals("\n" + "<p>" + "<abbr title=\"test\">[?]</abbr></p>", wikiModel.render("<abbr title=\"test\">[?]</abbr>", false));
	}

	public void testAbbr02() {
		assertEquals("\n" + "<p>" + "<abbr title=\"test\">[?]</abbr></p>", wikiModel.render(
				"<abbr title=\"<nowiki>test</nowiki>\">[?]</abbr>", false));
	}

	public void testIFrame01() {
		assertEquals("<iframe height=\"180\" name=\"inlineframe\" src=\"float.html\" width=\"500\"> </iframe>", wikiModel.render("<iframe name=\"inlineframe\" src=\"float.html\" frameborder=\"0\" scrolling=\"auto\" width=\"500\" height=\"180\" marginwidth=\"5\" marginheight=\"5\" >&nbsp;</iframe>", false));
	}
	
	// TODO: should this work, too?!
	// public void testAbbr03() {
	// assertEquals("\n" + "<p>" + "<abbr title=\"test\">[?]</abbr></p>",
	// wikiModel.render("<abbr title=\"<noWiki>test</nowiKi>\">[?]</abbr>",
	// false));
	// }
}
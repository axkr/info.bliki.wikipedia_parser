package info.bliki.wiki.filter;

import info.bliki.wiki.events.EventListener;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TOCFilterTest extends FilterTestSupport {
	public TOCFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TOCFilterTest.class);
	}

	public void testTOC01() {
		assertEquals("\n" + 
				"<div style=\"page-break-inside: avoid;\">\n" + 
				"<table align=\"right\">\n" + 
				"<tr>\n" + 
				"<td><table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" + 
				"<tr>\n" + 
				"<td>\n" + 
				"<div id=\"toctitle\">\n" + 
				"<h2>Contents</h2>\n" + 
				"</div>\n" + 
				"<ul>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-1\"><a href=\"#hello_world_2\">hello world 2</a>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-2\"><a href=\"#hello_world3\">hello world3</a>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</ul></td></tr></table><hr/>\n" + 
				" </td></tr></table></div>\n" + 
				"\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world_2\">hello world 2</span></h2>\n" + 
				"<p>hello world 2</p>\n" + 
				"<h3><span class=\"mw-headline\" id=\"hello_world3\">hello world3</span></h3>\n" + 
				"<p>hello world 3</p>", wikiModel
				.render("{| align=\"right\" \n" + "| __TOC__ \n" + "|}\n" + "\n" + "==hello world 2==\n" + "hello world 2\n" + "\n"
						+ "===hello world3===\n" + "hello world 3", false));
	}

	public void testTOC02() {
		assertEquals("\n" + 
				"<div style=\"page-break-inside: avoid;\">\n" + 
				"<table align=\"right\">\n" + 
				"<tr>\n" + 
				"<td><table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" + 
				"<tr>\n" + 
				"<td>\n" + 
				"<div id=\"toctitle\">\n" + 
				"<h2>Contents</h2>\n" + 
				"</div>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-1\"><a href=\"#hello_world_1\">hello world 1</a>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-2\"><a href=\"#hello_world_2\">hello world 2</a>\n" + 
				"</li>\n" + 
				"<li class=\"toclevel-2\"><a href=\"#hello_world1a\">hello world1a</a>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</li>\n" + 
				"</ul></td></tr></table><hr/>\n" + 
				"</td></tr></table></div>\n" + 
				"\n" + 
				"<h1><span class=\"mw-headline\" id=\"hello_world_1\">hello world 1</span></h1>\n" + 
				"<p>hello world 1</p>\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world_2\">hello world 2</span></h2>\n" + 
				"<p>hello world 2</p>\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world1a\">hello world1a</span></h2>\n" + 
				"<p>hello world 1a\n" + 
				"</p>", wikiModel
				.render("{| align=\"right\"\n" + "| __TOC__\n" + "|}\n" + "\n" + "=hello world 1=\n" + "hello world 1\n" + "\n"
						+ "==hello world 2==\n" + "hello world 2\n" + "\n" + "==hello world1a==\n" + "hello world 1a\n" + "", false));
	}

	public void testTOC03() {
		assertEquals("<h1><span class=\"mw-headline\" id=\"hello_world_1\">hello world 1</span></h1>\n" + 
				"<p>hello world 1</p>\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world_2\">hello world 2</span></h2>\n" + 
				"<p>hello world 2\n" + 
				"</p>", wikiModel
				.render("=hello world 1=\n" + "hello world 1\n" + "\n" + "==hello world 2==\n" + "hello world 2\n", false));
	}

	public void testTOC04() {
		assertEquals("<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" + 
				"<tr>\n" + 
				"<td>\n" + 
				"<div id=\"toctitle\">\n" + 
				"<h2>Contents</h2>\n" + 
				"</div>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-1\"><a href=\"#hello_world_1\">hello world 1</a>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-2\"><a href=\"#hello_world_2\">hello world 2</a>\n" + 
				"</li>\n" + 
				"<li class=\"toclevel-2\"><a href=\"#hello_world_3\">hello world 3</a>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-3\"><a href=\"#hello_world_4\">hello world 4</a>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</li>\n" + 
				"</ul></td></tr></table><hr/>\n" + 
				"<h1><span class=\"mw-headline\" id=\"hello_world_1\">hello world 1</span></h1>\n" + 
				"<p>hello world 1</p>\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world_2\">hello world 2</span></h2>\n" + 
				"<p>hello world 2</p>\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world_3\">hello world 3</span></h2>\n" + 
				"<p>hello world 3\n" + 
				"</p><h3><span class=\"mw-headline\" id=\"hello_world_4\">hello world 4</span></h3>\n" + 
				"<p>hello world 4\n" + 
				"</p>", wikiModel.render("=hello world 1=\n" + "hello world 1\n" + "\n" + "==hello world 2==\n"
				+ "hello world 2\n" + "\n" + "==hello world 3==\n" + "hello world 3\n" + "===hello world 4===\n" + "hello world 4\n", false));
	}

	public void testTOC05() {
		assertEquals(" \n" + 
				"<h1><span class=\"mw-headline\" id=\"hello_world_1\">hello world 1</span></h1>\n" + 
				"<p>hello world 1</p>\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world_2\">hello world 2</span></h2>\n" + 
				"<p>hello world 2</p>\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world_3\">hello world 3</span></h2>\n" + 
				"<p>hello world 3\n" + 
				"</p><h3><span class=\"mw-headline\" id=\"hello_world_4\">hello world 4</span></h3>\n" + 
				"<p>hello world 4\n" + 
				"</p>",
				wikiModel.render("__NOTOC__ \n" + "=hello world 1=\n" + "hello world 1\n" + "\n" + "==hello world 2==\n"
						+ "hello world 2\n" + "\n" + "==hello world 3==\n" + "hello world 3\n" + "===hello world 4===\n" + "hello world 4\n", false));
	}

	public void testTOC06() {
		assertEquals("<h1><span class=\"mw-headline\" id=\"hello_world_1\">hello world 1</span></h1>\n" + 
				"<p>hello world 1</p>\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world_2\">hello world 2</span></h2>\n" + 
				"<p>hello world 2</p>\n" + 
				"<h2><span class=\"mw-headline\" id=\"hello_world_.C3.9Cbersicht\">hello world Übersicht</span></h2>\n" + 
				"<p>hello world Übersicht\n" + 
				"</p>", wikiModel.render("=hello world 1=\n" + "hello world 1\n" + "\n"
				+ "==hello world 2==\n" + "hello world 2\n" + "\n" + "==hello world Übersicht==\n" + "hello world Übersicht\n", false));
	}

	public void testTOC07() { 
		assertEquals("" + 
				"\n" + 
				"\n" + 
				"<table id=\"toc\" class=\"toc\" summary=\"Contents\">\n" + 
				"<tr>\n" + 
				"<td>\n" + 
				"<div id=\"toctitle\">\n" + 
				"<h2>Contents</h2>\n" + 
				"</div>\n" + 
				"<ul>\n" + 
				"<ul>\n" + 
				"<li class=\"toclevel-1\"><a href=\"#-_.2F.:.21.7E.27.28.29\">-_/.:!~&#39;()</a>\n" + 
				"</li>\n" + 
				"</ul>\n" + 
				"</ul></td></tr></table><hr/>\n" + 
				"<h2><span class=\"mw-headline\" id=\"-_.2F.:.21.7E.27.28.29\">-_/.:!~&#39;()</span></h2>", wikiModel.render("\n" + 
				"__FORCETOC__\n" + 
				"== -_/.:!~'() ==", false));
	}
	public void testTOC_Listener() {
		String rawWikiText = wikiModel.parseTemplates("=hello world 1=\n" + "hello world 1\n" + "\n" + "==hello world 2==\n"
				+ "hello world 2\n");
		EventListener listener = new EventListener();
		wikiModel.parseEvents(listener, rawWikiText);
		assertEquals("=hello world 1=\n" + "==hello world 2==\n" + "", listener.getCollectorBuffer().toString());
	}
}
package info.bliki.wiki.filter;

import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WPLinkFilterTest extends FilterTestSupport {
	public WPLinkFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(WPLinkFilterTest.class);
	}
	
  public void testIssue136() {
		assertEquals(
				"\n" + 
				"<p><a href=\"http://www.bliki.info/wiki/Test:http://somesite.org\" title=\"Test:http://somesite.org\">Test:http://somesite.org</a></p>",
				wikiModel
						.render(
								"[[Test:http://somesite.org]]",
								false));
	}
  
	public void testLinkHash() {
		assertEquals(
				"\n"
						+ "<ol>\n"
						+ "<li>\n"
						+ "<ol>\n"
						+ "<li><a href=\"http://www.bliki.info/wiki/Using_Eclipse_Wikipedia_Editor:Getting_Started#Features\" title=\"Using Eclipse Wikipedia Editor:Getting Started\">Features</a></li>\n</ol></li>\n</ol>",
				wikiModel.render("##[[Using Eclipse Wikipedia Editor:Getting Started#Features|Features]]", false));
	}

	public void testLink() {
		assertEquals(
				"\n"
						+ "<p>You could open the <a href=\"http://www.bliki.info/wiki/Wikipedia:sandbox\" title=\"Wikipedia:sandbox\">sandbox</a> in a separate window or tab to be able to see both this text and your tests in the sandbox.</p>",
				wikiModel
						.render(
								"You could open the [[Wikipedia:sandbox|sandbox]] in a separate window or tab to be able to see both this text and your tests in the sandbox.",
								false));
	}

	public void testLink0() {
		assertEquals("\n" + "<p>[X]</p>", wikiModel.render("[X]", false));
	}

	public void testLink1() {
		assertEquals("\n" + "<p><a href=\"http://en.wikipedia.org/wiki/Test\">Test</a></p>", wikiModel
				.render("[[en:Test|Test]]", false));
	}

	public void testLink2() {
		assertEquals("\n" + "<p><a href=\"http://www.bliki.info/wiki/Test\" title=\"Test\">Test</a></p>", wikiModel.render(
				"[[Test|Test]]", false));
	}

	public void testLink3() {
		assertEquals("\n"
				+ "<p><a href=\"http://www.bliki.info/wiki/Category:Test_page\" title=\"Category:Test page\">Category:Test page</a></p>",
				wikiModel.render("[[:Category:Test page]]", false));
	}

	/**
	 * Categories are not rendered
	 * 
	 */
	public void testCategory01() {
		assertEquals("", wikiModel.render("[[Category:Tips and Tricks]]", false));
		Map<String, String> map = wikiModel.getCategories();
		assertTrue(map.containsKey("Tips and Tricks"));
	}

	public void testCategory02() {
		assertEquals("", wikiModel.render("[[Category:Rock and Roll Hall of Fame inductees|Beatles, The]]", false));
		Map<String, String> map = wikiModel.getCategories();
		// assertTrue(map.containsKey("Rock and Roll Hall of Fame inductees"));
		assertTrue(map.containsValue("Beatles, The"));
	}

	public void testLink5() {
		assertEquals("\n" + "<p><a href=\"http://wikitravel.org/en/test\">wikitravel:test</a></p>", wikiModel.render(
				"[[wikitravel:test]]", false));
	}

	public void testLink6() {
		assertEquals(
				"\n" + "<p><a href=\"http://www.bliki.info/wiki/Test:hello_world\" title=\"Test:hello world\">hello world</a></p>",
				wikiModel.render("[[Test:hello world|]]", false));
	}

	public void testLink7() {
		assertEquals("\n" + "<p><a href=\"http://www.bliki.info/wiki/Test(hello_world)\" title=\"Test(hello world)\">Test</a></p>",
				wikiModel.render("[[Test(hello world)|]]", false));
	}

	public void testLink8() {
		assertEquals("\n"
				+ "<p><a href=\"http://www.bliki.info/wiki/Boston%2C_Massachusetts\" title=\"Boston, Massachusetts\">Boston</a></p>",
				wikiModel.render("[[Boston, Massachusetts|]]", false));
	}

	public void testLink9() {
		assertEquals("\n" + "<p>test [[lets start\n" + "a 2 rows link]] test</p>", wikiModel.render(
				"test [[lets start\na 2 rows link]] test", false));
	}

	public void testLink9a() {
		assertEquals("\n"
				+ "<p>test <a href=\"http://www.bliki.info/wiki/Lets_start_a_2_rows_link\" title=\"Lets start a 2 rows link\">lets start\n"
				+ "a 2 rows link</a> test</p>", wikiModel.render("test [[lets start a 2 rows link|lets start\na 2 rows link]] test", false));
	}

	public void testLink10() {
		assertEquals("\n"
				+ "<p>test <a href=\"http://www.bliki.info/wiki/Lets_start\" title=\"Lets start\">a 2 rows piped link</a> test</p>",
				wikiModel.render("test [[lets start|\na 2 rows piped link]] test", false));
	}

	public void testLink11() {
			assertEquals("\n" + 
					"<p>test\n" + 
					"</p>\n" + 
					"<ul>\n" + 
					"<li>blabla<a href=\"http://www.bliki.info/wiki/List_of_cities_by_country#Morocco\" title=\"List of cities by country\">Cities in Morocco</a></li>\n" + 
					"</ul>", wikiModel.render("test\n*blabla[[List of cities by country#Morocco|\nCities in Morocco]]",
				false));
	}

	//
	public void testLink12() {
		assertEquals(
				"\n"
						+ "<p>kellereien wie <a href=\"http://www.bliki.info/wiki/Henkell_%26_S%C3%B6hnlein\" title=\"Henkell &amp; Söhnlein\">Henkell</a>, <a href=\"http://www.bliki.info/wiki/S%C3%B6hnlein\" title=\"Söhnlein\">Söhnlein</a></p>",
				wikiModel.render("kellereien wie [[Henkell & Söhnlein|Henkell]], [[Söhnlein]]", false));
		Set<String> set = wikiModel.getLinks();
		assertTrue(set.contains("Söhnlein"));
		assertTrue(set.contains("Henkell & Söhnlein"));
	}

	public void testLink13() {
		assertEquals("\n"
				+ "<p>test [[lets start a <a href=\"http://www.bliki.info/wiki/Nested\" title=\"Nested\">nested</a> link]] test</p>",
				wikiModel.render("test [[lets start a [[nested]] link]] test", false));
		Set<String> set = wikiModel.getLinks();
		assertTrue(set.contains("nested"));
	}

	public void testLink14() {
		assertEquals(
				"\n"
						+ "<p>Dolphins are <a href=\"http://www.bliki.info/wiki/Aquatic_mammal\" title=\"Aquatic mammal\">aquatic mammals</a> that are closely related to <a href=\"http://www.bliki.info/wiki/Whale\" title=\"Whale\">whales</a> and <a href=\"http://www.bliki.info/wiki/Porpoise\" title=\"Porpoise\">porpoises</a>.</p>",
				wikiModel.render("Dolphins are [[aquatic mammal]]s that are closely related to [[whale]]s and [[porpoise]]s.", false));
	}
	
	public void testLink15() {
		assertEquals(
				"\n<p><a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">main Page</a></p>",
				wikiModel.render("[[main Page]]", false));
	}
	
	public void testLink16() {
		assertEquals(
				"\n<p><a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">main  Page</a></p>",
				wikiModel.render("[[main  Page]]", false));
	}
	
	public void testLink17() {
		assertEquals(
				"\n<p><a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">main__Page</a></p>",
				wikiModel.render("[[main__Page]]", false));
	}
	
	public void testLink18() {
		assertEquals(
				"\n<p><a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">main_ Page</a></p>",
				wikiModel.render("[[main_ Page]]", false));
	}

	public void testInterwiki1() {
		assertEquals("\n" + "<p><a href=\"http://de.wikipedia.org/wiki/Johann_Wolfgang_von_Goethe\">Goethes</a> Faust</p>", wikiModel
				.render("[[de:Johann Wolfgang von Goethe|Goethe]]s Faust", false));
	}

	public void testInterwiki2() {
		assertEquals("\n" + "<p><a href=\"/page/directory\">Page directory</a></p>", wikiModel.render(
				"[[intra:page/directory|Page directory]]", false));
	}

	public void testSectionLink01() {
		assertEquals("\n" + "<p><a href=\"#Section_Link\">A Section Link</a></p>", wikiModel.render("[[#Section Link|A Section Link]]",
				false));
	}

	public void testSectionLink02() {
		assertEquals("\n" + "<p><a href=\"#Section.C3.A4.C3.B6.C3.BC\">#Sectionäöü</a></p>", wikiModel.render("[[#Sectionäöü]]", false));
	}

	/**
	 * See issue 101
	 */
	public void testSectionLink03() {
		assertEquals("\n" + "<p><a href=\"#Section_Link\">#Section Link</a></p>", wikiModel.render("[[#Section Link]]", false));
	}

	public void testSpecialLink01() {
		assertEquals(
				"\n"
						+ "<ul>\n"
						+ "<li><a href=\"http://www.bliki.info/wiki/Special:Specialpages\" title=\"Special:Specialpages\">Special Pages</a></li>\n</ul>",
				wikiModel.render("* [[Special:Specialpages|Special Pages]]", false));
	}

	public void testSubLink01() {
		assertEquals("\n" + "<p><a href=\"http://www.bliki.info/wiki/Test/testing\" title=\"Test/testing\">test/testing</a></p>",
				wikiModel.render("[[test/testing]]", false));
	}

	//
	public void testSubLink04() {
		assertEquals("\n"
				+ "<p><a href=\"http://www.bliki.info/wiki/Hello_World%3Fid%3D42\" title=\"Hello World?id=42\">Hello World?id=42</a></p>",
				wikiModel.render("[[Hello World?id=42]]", false));
	}

	public void testRedirect01() {
		assertEquals("", wikiModel.render("#REDIRECT [[Official position]]", false));
		assertEquals("Official position", wikiModel.getRedirectLink());
	}

	public void testRedirect02() {
		assertEquals("", wikiModel.render(" \n  #REDIRECT[[Official position]] bla \n other blabls", false));
		assertEquals("Official position", wikiModel.getRedirectLink());
	}

	public void testRedirect03() {
		assertEquals(" \n" + "<p>Hello World!</p>", wikiModel.render(" \n{{TestRedirect1}}", false));
	}

	public void testRedirect04() {
		assertEquals(" \n" + "<pre>" + "Hello World! \n" + "</pre>", wikiModel.render(" \n {{TestRedirect1}} ", false));
	}

	public void testRedirect05() {
		assertEquals("", wikiModel.render("#Redirect [[El Niño-Southern Oscillation]]", false));
		assertEquals("El Niño-Southern Oscillation", wikiModel.getRedirectLink());
	}

	public void testPlainTextConverter001() {
		assertEquals("\n" + 
				"An external link. ", wikiModel.render(new PlainTextConverter(), "An [http://www.example.com external link]. ",
				false));
	}

	public void testPlainTextConverter002() {
		String wikitext = "The '''Eiffel Tower''',{{IPA-fr|tuʀ ɛfɛl|}}"
				+ "<!--Note: French does not have tonic accents, so do not add stress marks to this pronunciation-->)"
				+ " is a 19th century ";

		assertEquals("\n" + 
				"The Eiffel Tower,[tuʀ ɛfɛl]) is a 19th century ", wikiModel.render(
				new PlainTextConverter(), wikitext, false));
	}

	public void testPlainTextConverter003() {
		String wikitext = "The '''Eiffel Tower''',{{IPA-fr|tuʀ ɛfɛl}}"
				+ "<!--Note: French does not have tonic accents, so do not add stress marks to this pronunciation-->)"
				+ " is a 19th century ";

		assertEquals("\n" + 
				"The Eiffel Tower,French pronunciation: [tuʀ ɛfɛl]) is a 19th century ", wikiModel.render(
				new PlainTextConverter(), wikitext, false));
	}

	public void testPlainTextConverter004() {
		String wikitext = "The '''Eiffel Tower''',{{IPA-fr|tuʀ ɛfɛl|lang}}"
				+ "<!--Note: French does not have tonic accents, so do not add stress marks to this pronunciation-->)"
				+ " is a 19th century ";

		assertEquals("\n" + 
				"The Eiffel Tower,French: [tuʀ ɛfɛl]) is a 19th century ", wikiModel.render(
				new PlainTextConverter(), wikitext, false));
	}

	public void testPlainTextConverter005() {
		String wikitext = "The '''Eiffel Tower''',{{IPA-fr|tuʀ ɛfɛl| }}"
				+ "<!--Note: French does not have tonic accents, so do not add stress marks to this pronunciation-->)"
				+ " is a 19th century ";

		assertEquals("\n" + 
				"The Eiffel Tower,[tuʀ ɛfɛl]) is a 19th century ", wikiModel.render(
				new PlainTextConverter(), wikitext, false));
	}
	// public static void main(String[] args) {
	// String test = Encoder.encode("erreäöü öüä++", ".");
	// System.out.println(test);
	// }
}

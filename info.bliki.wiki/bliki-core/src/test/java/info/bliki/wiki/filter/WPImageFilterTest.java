package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WPImageFilterTest extends FilterTestSupport {
	public WPImageFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(WPImageFilterTest.class);
	}

	public void testEncoder001() {
		assertEquals("/hello/'2E'2E/world.html", Encoder.encodeTitleLocalUrl("/hello/../world.html"));
	}

	public void testEncoder002() {
		assertEquals("/hello/you'2Ethis'2Eis'2E'2Ean'2E'2E/exa..mple.html", Encoder
				.encodeTitleLocalUrl("/hello/you.this.is..an../exa..mple.html"));
	}
	
	public void testImage00() {
		assertEquals(
				"\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:baby_elephant.jpg\" title=\"Link to the Sandbox\"><img src=\"http://www.bliki.info/wiki/baby_elephant.jpg\" alt=\"Link to the Sandbox\" title=\"Link to the Sandbox\" class=\"type-thumb\" width=\"220\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">Link to the <a href=\"http://www.bliki.info/wiki/Sandbox\" title=\"Sandbox\">Sandbox</a></div></div>\n" + 
				"</div>\n", 
				wikiModel.render("[[Image:baby_elephant.jpg|thumb|Link to the [[Sandbox]]]]", false));
	}

	public void testImage01b() {
		assertEquals(
				"\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:baby_elephant.jpg\" title=\"Link to the Sandbox caption\"><img src=\"http://www.bliki.info/wiki/baby_elephant.jpg\" alt=\"Link to the Sandbox caption\" title=\"Link to the Sandbox caption\" class=\"type-thumb\" width=\"220\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">Link to the <a href=\"http://www.bliki.info/wiki/Sandbox\" title=\"Sandbox\">Sandbox</a> caption</div></div>\n" + 
				"</div>\n" + 
				""  , wikiModel.render("[[Image:baby_elephant.jpg|thumb|Link to the [[Sandbox]] caption]]", false));
	}

	public void testImage02() {
		assertEquals(
				"\n" + 
				"<p>these are the </p>\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:220px-FIFA_WM_2006_Teams.png\" title=\"Qualifying countries\"><img src=\"http://www.bliki.info/wiki/220px-FIFA_WM_2006_Teams.png\" alt=\"Qualifying countries\" title=\"Qualifying countries\" class=\"type-thumb\" width=\"220\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">Qualifying countries</div></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<p>...</p>", wikiModel
						.render("these are the [[Image:FIFA WM 2006 Teams.png|thumb|220px|Qualifying countries]]...", false));
	}

	public void testImage03() {
		assertEquals(
				"\n"
						+ "<p><a class=\"external free\" href=\"http://Westernpad.jpg\" rel=\"nofollow\">http://Westernpad.jpg</a></p>",
				wikiModel.render("http://Westernpad.jpg", false));
	}

	public void testImage04() {
		assertEquals(
				"\n" + 
				"<p>test </p>\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:Kurhaus_Wiesbaden_A.jpg\" title=\"Neues Kurhaus aus dem Jahr 1907 am Bowling Green\"><img src=\"http://www.bliki.info/wiki/Kurhaus_Wiesbaden_A.jpg\" alt=\"Neues Kurhaus aus dem Jahr 1907 am Bowling Green\" title=\"Neues Kurhaus aus dem Jahr 1907 am Bowling Green\" class=\"type-thumb\" width=\"220\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">Neues <a href=\"http://www.bliki.info/wiki/Kurhaus_Wiesbaden\" title=\"Kurhaus Wiesbaden\">Kurhaus</a> aus dem Jahr 1907 am <a href=\"http://www.bliki.info/wiki/Bowling_Green_(Wiesbaden)\" title=\"Bowling Green (Wiesbaden)\">Bowling Green</a></div></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<p> abc...</p>",
				wikiModel
						.render(
								"test [[Image:Kurhaus Wiesbaden A.jpg|thumb|Neues [[Kurhaus Wiesbaden|Kurhaus]] aus dem Jahr 1907 am [[Bowling Green (Wiesbaden)|Bowling Green]]]] abc...",
								false));
	}

	public void testImage05() {
		assertEquals(
				"\n" + 
				"<p><a class=\"image\" href=\"http://www.bliki.info/wiki/Image:wikipedia_new_project1.png\" ><img src=\"http://www.bliki.info/wiki/wikipedia_new_project1.png\" width=\"220\" />\n" + 
				"</a></p>", wikiModel.render("[[Image:wikipedia_new_project1.png]]", false));
	}

	public void testImage06() {
		assertEquals(
				"\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:Henkell-Schl%C3%B6sschen.JPG\" title=\"Henkell-Schlösschen der Sektkellerei Henkell &#38; Söhnlein KG\"><img src=\"http://www.bliki.info/wiki/Henkell-Schl%C3%B6sschen.JPG\" alt=\"Henkell-Schlösschen der Sektkellerei Henkell &#38; Söhnlein KG\" title=\"Henkell-Schlösschen der Sektkellerei Henkell &#38; Söhnlein KG\" class=\"type-thumb\" width=\"220\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\"><i>Henkell-Schlösschen</i> der Sektkellerei <a href=\"http://www.bliki.info/wiki/Henkell_%26_S%C3%B6hnlein_KG\" title=\"Henkell &amp; Söhnlein KG\">Henkell &#38; Söhnlein KG</a></div></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<p> Wiesbaden</p>",
				wikiModel
						.render(
								"[[Image:Henkell-Schlösschen.JPG|thumb|\'\'Henkell-Schlösschen\'\' der Sektkellerei [[Henkell & Söhnlein KG]]]] Wiesbaden",
								false));
	}

	public void testImage07() {
		assertEquals(
				"\n" + 
				"<p><a class=\"image\" href=\"http://www.bliki.info/wiki/Image:400px-ImageFileName.jpg\" ><img src=\"http://www.bliki.info/wiki/400px-ImageFileName.jpg\" class=\"location-center\" width=\"400\" />\n" + 
				"</a></p>", wikiModel.render("[[Image:ImageFileName.jpg|400px|Center]]", false));
	}

	public void testImage08() {
		assertEquals(
				"\n"
						+ "<p>[<a class=\"external free\" href=\"http://www.homeportals.net/downloads/ClassDiagram_3.0.198.jpg\" rel=\"nofollow\">http://www.homeportals.net/downloads/ClassDiagram_3.0.198.jpg</a> Class\n"
						+ "Diagram]</p>", wikiModel.render("[http://www.homeportals.net/downloads/ClassDiagram_3.0.198.jpg Class\n"
						+ "Diagram]", false));
	}

	public void testImage09() {
		assertEquals(
				"\n" + 
				"<p><a class=\"image\" href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Alt text\"><img src=\"http://www.bliki.info/wiki/150px-Example.png\" alt=\"Alt text\" width=\"150\" />\n" + 
				"</a></p>", wikiModel.render(
						"[[Image:Example.png|150px|link=Main Page\n" + "|alt=Alt text|Title text]]", false));
	}

	public void testImage10() {
		assertEquals(
				"\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"width:152px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Alt text\"><img src=\"http://www.bliki.info/wiki/150px-Example.png\" alt=\"Alt text\" title=\"Alt text\" class=\"type-thumb\" width=\"150\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">Caption</div></div>\n" + 
				"</div>\n" + 
				"", wikiModel.render(
						"[[Bild:Example.png|150px|link=Main Page|thumb|alt=Alt text|Caption]]", false));
	}

	public void testImage11() {
		assertEquals(
				"\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"width:152px;\"><img src=\"http://www.bliki.info/wiki/150px-Example.png\" alt=\"Alt text\" title=\"Alt text\" class=\"type-thumb\" width=\"150\" />\n" + 
				"\n" + 
				"<div class=\"thumbcaption\">Caption</div></div>\n" + 
				"</div>\n" + 
				"", wikiModel.render(
						"[[Bild:Example.png|150px|link=|thumb|alt=Alt text|Caption]]", false));
	}

	public void testImage12() {
		assertEquals(
				"\n" + 
				"<p>these are the </p>\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"height:100px;width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:220px-FIFA_WM_2006_Teams.png\" title=\"Qualifying countries\"><img src=\"http://www.bliki.info/wiki/220px-FIFA_WM_2006_Teams.png\" alt=\"Qualifying countries\" title=\"Qualifying countries\" class=\"type-thumb\" height=\"100\" width=\"220\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">Qualifying countries</div></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<p>...</p>", wikiModel
						.render("these are the [[Image:FIFA WM 2006 Teams.png|thumb|220x100px|Qualifying countries]]...", false));
	}

	public void testImage13() {
		assertEquals(
				"\n" + 
				"<p>these are the </p>\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"height:100px;width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:FIFA_WM_2006_Teams.png\" title=\"Qualifying countries\"><img src=\"http://www.bliki.info/wiki/FIFA_WM_2006_Teams.png\" alt=\"Qualifying countries\" title=\"Qualifying countries\" class=\"type-thumb\" height=\"100\" width=\"220\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">Qualifying countries</div></div>\n" + 
				"</div>\n" + 
				"\n" + 
				"<p>...</p>", wikiModel
						.render("these are the [[Image:FIFA WM 2006 Teams.png|thumb|x100px|Qualifying countries]]...", false));
	}

	public void testImage14() {
		assertEquals(
				"\n" + 
				"<div class=\"thumb tleft\">\n" + 
				"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:William_Fettes_Douglas_-_The_Alchemist.jpg\" title=\"&#34;Renel the Alchemist&#34;, by Sir William Douglas, 1853\"><img src=\"http://www.bliki.info/wiki/William_Fettes_Douglas_-_The_Alchemist.jpg\" alt=\"&#34;Renel the Alchemist&#34;, by Sir William Douglas, 1853\" title=\"&#34;Renel the Alchemist&#34;, by Sir William Douglas, 1853\" class=\"location-left type-thumb\" width=\"220\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">&#34;Renel the Alchemist&#34;, by Sir William Douglas, 1853</div></div>\n" + 
				"</div>\n" + 
				"",
				wikiModel
						.render(
								"[[Image:William Fettes Douglas - The Alchemist.jpg|thumb|left|\"Renel the Alchemist\", by Sir William Douglas, 1853]]",
								false));
	}

	public void testImage15() {
		String raw = "[[Image:Bamfield (171).jpg|right|thumb" + "|Tsunami hazard sign at [[Bamfield]], [[British Columbia]]"
				+ "|alt=Photo of sign reading " + "\"Tsunami Hazard Zone...In case of earthquake," + " go to higher ground or inland\"]]";
		String expected = "\n" + 
				"<div class=\"thumb tright\">\n" + 
				"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:Bamfield_(171).jpg\" title=\"Photo of sign reading &#34;Tsunami Hazard Zone...In case of earthquake, go to higher ground or inland&#34;\"><img src=\"http://www.bliki.info/wiki/Bamfield_(171).jpg\" alt=\"Photo of sign reading &#34;Tsunami Hazard Zone...In case of earthquake, go to higher ground or inland&#34;\" title=\"Photo of sign reading &#34;Tsunami Hazard Zone...In case of earthquake, go to higher ground or inland&#34;\" class=\"location-right type-thumb\" width=\"220\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">Tsunami hazard sign at <a href=\"http://www.bliki.info/wiki/Bamfield\" title=\"Bamfield\">Bamfield</a>, <a href=\"http://www.bliki.info/wiki/British_Columbia\" title=\"British Columbia\">British Columbia</a></div></div>\n" + 
				"</div>\n" + 
				"";
		assertEquals(expected, wikiModel.render(raw, false));
	}

	public void testImage16() {
		assertEquals(
				"\n" + 
				"<p><a class=\"image\" href=\"http://www.bliki.info/wiki/Sandbox:createNewPage#Section_Three\" ><img src=\"http://www.bliki.info/wiki/skull.jpg\" width=\"220\" />\n" + 
				"</a></p>", wikiModel.render("[[Image:skull.jpg|link=sandbox:createNewPage#Section Three]]", false));
	}
	
	public void testImage17() {
		assertEquals("Test_'26_ToDo.jpg", Encoder.encodeTitleLocalUrl("Test & ToDo.jpg"));
		assertEquals(
				"\n" + 
				"<div class=\"thumb tleft\">\n" + 
				"<div class=\"thumbinner\" style=\"width:152px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/File:150px-Test_4711.jpg\" title=\"Test 4711\"><img src=\"http://www.bliki.info/wiki/150px-Test_4711.jpg\" alt=\"Test 4711\" title=\"Test 4711\" class=\"location-left type-thumb\" width=\"150\" />\n" + 
				"</a>\n" + 
				"<div class=\"thumbcaption\">Test 4711</div></div>\n" + 
				"</div>\n" + 
				"", 
						wikiModel.render(
						"[[File:Test 4711.jpg|thumb|left|150px|Test 4711]]", false));
	}
	
	public void testImage19() {
		assertEquals("\n" + 
				"<p>test1<a class=\"image\" href=\"http://www.bliki.info/wiki/Image:PeanutButter.jpg\" ><img src=\"http://www.bliki.info/wiki/PeanutButter.jpg\" width=\"220\" />\n" + 
				"</a>test2</p>", wikiModel.render("test1[[Image:PeanutButter.jpg]]test2", false));
	}
	
	public void testImage18() {
		assertEquals("\n" + 
				"<p><a class=\"image\" href=\"http://www.bliki.info/wiki/Speakerlink.png\" title=\"Listen\"><img src=\"http://www.bliki.info/wiki/11px-Speakerlink.png\" alt=\"Listen\" width=\"11\" />\n" + 
				"</a></p>", wikiModel.render("[[Image:Speakerlink.png|11px|link=Speakerlink.png|Listen]]", false));
	}
}
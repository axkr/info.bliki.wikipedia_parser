package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class PreFilterTest extends FilterTestSupport {
	public PreFilterTest(String name) {
		super(name);
	}

	public static Test suite() { 
		return new TestSuite(PreFilterTest.class);
	}

	/** 
	 * See Issue 106
	 * 
	 */
	public void testPre4() {
	  assertEquals("<h2><span class=\"mw-headline\" id=\"Peanut_Butter_.28variant_1.29\">Peanut Butter (variant 1)</span></h2>\n" + 
	 		"<div class=\"thumb tright\">\n" + 
	 		"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:PeanutButter.jpg\" title=\"&#34;Smooth&#34; peanut butter in a jar\"><img src=\"http://www.bliki.info/wiki/PeanutButter.jpg\" alt=\"&#34;Smooth&#34; peanut butter in a jar\" title=\"&#34;Smooth&#34; peanut butter in a jar\" class=\"type-thumb\" width=\"220\" />\n" + 
	 		"</a>\n" + 
	 		"<div class=\"thumbcaption\">&#34;Smooth&#34; peanut butter in a jar</div></div>\n" + 
	 		"</div>\n" + 
	 		"", 
			 wikiModel.render("== Peanut Butter (variant 1) ==\n" +
	       " [[Image:PeanutButter.jpg|thumb|\"Smooth\" peanut butter in a jar]]",
	  false));
	}
	
	/** 
	 * See Issue 106
	 * 
	 */
	public void testPre5() {
	  assertEquals("<h2><span class=\"mw-headline\" id=\"Peanut_Butter_.28variant_2.29\">Peanut Butter (variant 2)</span></h2>\n" + 
	  		"<pre>" + 
	  		"test0\n" + 
	  		"\n" + 
	  		"</pre>\n" + 
	  		"<div class=\"thumb tright\">\n" + 
	  		"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:PeanutButter.jpg\" title=\"&#34;Smooth&#34; peanut butter in a jar\"><img src=\"http://www.bliki.info/wiki/PeanutButter.jpg\" alt=\"&#34;Smooth&#34; peanut butter in a jar\" title=\"&#34;Smooth&#34; peanut butter in a jar\" class=\"type-thumb\" width=\"220\" />\n" + 
	  		"</a>\n" + 
	  		"<div class=\"thumbcaption\">&#34;Smooth&#34; peanut butter in a jar</div></div>\n" + 
	  		"</div>\n" + 
	  		"\n" + 
	  		"<pre>" + 
	  		"test2\n" + 
	  		"</pre>\n" + 
	  		"<p><b>Peanut butter</b> is a food paste made primarily from ...</p>", 
			 wikiModel.render("== Peanut Butter (variant 2) ==\n" + 
			 		" test0\n" + 
			 		" [[Image:PeanutButter.jpg|thumb|\"Smooth\" peanut butter in a jar]]\n" + 
			 		" test2\n" + 
			 		"\n" + 
			 		"'''Peanut butter''' is a food paste made primarily from ...",
	  false));
	}
	/** 
	 * See Issue 106
	 * 
	 */
	public void testPre6() {
	  assertEquals("<h2><span class=\"mw-headline\" id=\"Peanut_Butter_.28variant_3.29\">Peanut Butter (variant 3)</span></h2>\n" + 
	  		"<pre>" + 
	  		"test0\n" + 
	  		"\n" + 
	  		"</pre>\n" + 
	  		"<p>test</p>\n" + 
	  		"<div class=\"thumb tright\">\n" + 
	  		"<div class=\"thumbinner\" style=\"width:222px;\"><a class=\"internal\" href=\"http://www.bliki.info/wiki/Image:PeanutButter.jpg\" title=\"&#34;Smooth&#34; peanut butter in a jar\"><img src=\"http://www.bliki.info/wiki/PeanutButter.jpg\" alt=\"&#34;Smooth&#34; peanut butter in a jar\" title=\"&#34;Smooth&#34; peanut butter in a jar\" class=\"type-thumb\" width=\"220\" />\n" + 
	  		"</a>\n" + 
	  		"<div class=\"thumbcaption\">&#34;Smooth&#34; peanut butter in a jar</div></div>\n" + 
	  		"</div>\n" + 
	  		"\n" + 
	  		"<pre>" + 
	  		"test2\n" + 
	  		"</pre>\n" + 
	  		"<p><b>Peanut butter</b> is a food paste made primarily from ...</p>", 
			 wikiModel.render("== Peanut Butter (variant 3) ==\n" + 
			 		" test0\n" + 
			 		" test[[Image:PeanutButter.jpg|thumb|\"Smooth\" peanut butter in a jar]]\n" + 
			 		" test2\n" + 
			 		"\n" + 
			 		"'''Peanut butter''' is a food paste made primarily from ...",
	  false));
	}
	/** 
	 * See Issue 104
	 * 
	 */
	public void testPre7() {
			assertEquals("<h2><span class=\"mw-headline\" id=\"TestLBPre\">TestLBPre</span></h2>\n" + 
					"<pre>" + 
					"test1\n" + 
					"</pre>\n" + 
					"<pre>" + 
					"test2\n" + 
					"</pre>", wikiModel.render("== TestLBPre ==\n" + 
					" test1\n" + 
					"\n" + 
					" test2", false));
	}
	
	/** 
	 * See Issue 104
	 * 
	 */
	public void testPre8() {
			assertEquals("<h2><span class=\"mw-headline\" id=\"TestLBPre\">TestLBPre</span></h2>\n" + 
					"<pre>" + 
					"test1\n" + 
					"\n" + 
					"test2\n" + 
					"</pre>", wikiModel.render("== TestLBPre ==\n" + 
					" test1\n" + 
					" \n" + 
					" test2", false));
	}
	
	public void testPre0() {
		assertEquals("\n" + "<p>\n" + "The nowiki tag ignores [[Wiki]] &#39;&#39;markup&#39;&#39;.\n"
				+ "It reformats text by removing\n" + "newlines    and multiple spaces.\n" + "It still interprets special\n"
				+ "characters: &#38; \n" + "</p>", wikiModel.render("<nowiki>\n" + "The nowiki tag ignores [[Wiki]] ''markup''.\n"
				+ "It reformats text by removing\n" + "newlines    and multiple spaces.\n" + "It still interprets special\n"
				+ "characters: & \n" + "</nowiki>", false));
	}

	public void testPre1() {
		assertEquals("\n" + "<p>First line:</p>\n" + "<pre>" + "pre text 1\n" + "pre text 2\n" + "</pre>\n" + "<p>last line</p>",
				wikiModel.render("First line:\n" + " pre text 1\n" + " pre text 2\n" + "last line", false));
	}

	public void testPre3() {
		assertEquals("\n" + "\n" + "<ul>\n" + "<li>line 1 ...</li>\n" + "<li>line 2 &#60; test wrong tag</li>\n"
				+ "<li>line 3 &#60; test wrong tag</li>\n</ul>\n" + "\n" + "<pre>" + "preformatted text\n" + "</pre>", wikiModel.render(
				"\n" + "* line 1 ...\n" + "* line 2 < test wrong tag\n" + "* line 3 < test wrong tag\n" + "\n" + "<pre>"
						+ "preformatted text\n" + "</pre>", false));
	}

	// public void testPre1() {
	// assertEquals("", wikiModel.render("<pre><nowiki>\n" +
	// "The nowiki tag ignores [[Wiki]] ''markup''.\n" +
	// "It reformats text by removing\n" +
	// "newlines    and multiple spaces.\n" +
	// "It still interprets special\n" +
	// "characters: & \n" +
	// "</nowiki>\n</pre>"));
	// }
	public void testPre2() {
		assertEquals("\n" + 
				"<pre>" + 
				"pre text\n" + 
				"</pre>\n" + 
				"\n" + 
				"\n" + 
				"<p>last line</p>", 
				wikiModel.render("\n" + " pre text\n" + "\n"
				+ "\n" + "\n" + "last line", false));
	}

	public void testPre10() {
		assertEquals(
				"\n"
						+ "<p>Aufzählungstypen dienen zur automatischen Nummerierung der in der Aufzählung enthaltenen Elemente. Die Syntax für die Definition von Aufzählungstypen verwendet das Schlüsselwort <tt>enum</tt> (Kurzform für Enumeration).</p>\n"
						+ "<p>Beim in C# verwendeten Aufzählungstyp kann ein zugrundeliegender Datentyp für die Nummerierung der Elemente angegeben werden kann. Per Voreinstellung wird der Datentyp <tt>int</tt> verwendet. </p> \n"
						+ "<pre> public enum Woche : int\n" + " {\n" + "   Montag = 1,\n" + "   Dienstag,\n" + "   Mittwoch,\n"
						+ "   Donnerstag,\n" + "   Freitag,\n" + "   Samstag,\n" + "   Sonntag\n" + " }\n" + "\n</pre>",
				wikiModel
						.render(
								"Aufzählungstypen dienen zur automatischen Nummerierung der in der Aufzählung enthaltenen Elemente. Die Syntax für die Definition von Aufzählungstypen verwendet das Schlüsselwort <tt>enum</tt> (Kurzform für Enumeration).\n"
										+ "\n"
										+ "Beim in C# verwendeten Aufzählungstyp kann ein zugrundeliegender Datentyp für die Nummerierung der Elemente angegeben werden kann. Per Voreinstellung wird der Datentyp <tt>int</tt> verwendet. \n"
										+ " \n"
										+ "  public enum Woche : int\n"
										+ "  {\n"
										+ "    Montag = 1,\n"
										+ "    Dienstag,\n"
										+ "    Mittwoch,\n"
										+ "    Donnerstag,\n" + "    Freitag,\n" + "    Samstag,\n" + "    Sonntag\n" + "  }\n" + "", false));
	}

}
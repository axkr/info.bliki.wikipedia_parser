package info.bliki.wiki.filter;

import info.bliki.wiki.model.WikiModel;

import java.util.HashMap;
import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TemplateParserTest extends FilterTestSupport {

	public TemplateParserTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TemplateParserTest.class);
	}

	private final String TEST_STRING_03 = "{{{1|{{PAGENAME}}}}}";
 
	public void testWeather07() {
		assertEquals("20\n", wikiModel.parseTemplates("{{WeatherBox03}}\n"));
	}

	public void testWeather06() {
		assertEquals("10\n", wikiModel.parseTemplates("{{WeatherBox03|show1=1}}\n"));
	}

	/**
	 * Issue 86
	 */
	public void testSortnameDemo001() {
		assertEquals(
				"<span style=\"display:none;\">Man with One Red Shoe, The</span><span class=\"vcard\"><span class=\"fn\">[[The Man with One Red Shoe|The Man with One Red Shoe]]</span></span>[[Category:Articles with hCards]]",
				wikiModel.parseTemplates("{{sortname|The|Man with One Red Shoe}}"));
	}

	/**
	 * Issue 86
	 */
	public void testNoincludeDemo001() {
		assertEquals("test1 noincludetext\n" + "\n" + "", wikiModel.parseTemplates("test1<noinclude> noincludetext</noinclude>\n"
				+ "\n" + "<includeonly>includeonlytext<noinclude> noincludetext</noinclude></includeonly>"));
	}

	/**
	 * Issue 86
	 */
	public void testNoincludeDemo002() {
		assertEquals("\n" + "<p>test1 noincludetext</p>\n" + "", wikiModel.render("test1<noinclude> noincludetext</noinclude>\n" + "\n"
				+ "<includeonly>includeonlytext<noinclude> noincludetext</noinclude></includeonly>", true));
	}

	/**
	 * Issue 86
	 */
	public void testOnlyicludeDemo001() {
		assertEquals("abcdefghi<hr>\n" + ";Only active template content is above.\n" + "\n"
				+ ";The verbatim active code within reads:\n"
				+ " abc'''&lt;onlyinclude>'''def'''&lt;/onlyinclude>'''ghi'''&lt;includeonly>'''jkl'''&lt;/includeonly>'''\n" + "\n"
				+ "If transposed, the only part included will be the string literal <code>def</code>. \n" + "\n" + "==Example==\n"
				+ "Including [[:Help:Template/onlyinclude demo]] yields only:\n" + " [[:Help:Template/onlyinclude demo]]\n" + "\n" + "\n"
				+ "\n" + "[[Category:Handbook templates]]\n" + "[[Category:Template documentation|PAGENAME]]\n" + "\n" + "", wikiModel
				.parseTemplates(WikiTestModel.ONLYINCLUDE_DEMO));
	}

	/**
	 * Issue 86
	 */
	public void testOnlyicludeDemo002() {
		assertEquals("def", wikiModel.parseTemplates("{{OnlyicludeDemo}}"));
	}

	/**
	 * Issue 86
	 */
	public void testOnlyicludeDemo003() {
		assertEquals(
				"\n"
						+ "<p>abcdefghi</p><hr />\n"
						+ "\n"
						+ "<dl>\n"
						+ "<dt>Only active template content is above.</dt>\n</dl>\n"
						+ "\n"
						+ "\n"
						+ "<dl>\n"
						+ "<dt>The verbatim active code within reads</dt>\n"
						+ "<dd></dd>\n</dl>:\n"
						+ "<pre>"
						+ "abc<b>&#60;onlyinclude&#62;</b>def<b>&#60;/onlyinclude&#62;</b>ghi<b>&#60;includeonly&#62;</b>jkl<b>&#60;/includeonly&#62;</b>\n"
						+ "</pre>\n"
						+ "<p>If transposed, the only part included will be the string literal <code>def</code>. </p>\n"
						+ "<h2><span class=\"mw-headline\" id=\"Example\">Example</span></h2>\n"
						+ "<p>Including <a href=\"http://www.bliki.info/wiki/Help:Template/onlyinclude_demo\" title=\"Help:Template/onlyinclude demo\">Help:Template/onlyinclude demo</a> yields only:</p>\n"
						+ "<pre>"
						+ "<a href=\"http://www.bliki.info/wiki/Help:Template/onlyinclude_demo\" title=\"Help:Template/onlyinclude demo\">Help:Template/onlyinclude demo</a>\n"
						+ "</pre>\n" + "\n" + "\n" + "<p>\n" + "</p>\n" + "", wikiModel.render(WikiTestModel.ONLYINCLUDE_DEMO, true));
	}

	/**
	 * Issue 86
	 */
	public void testOnlyicludeDemo004() {
		assertEquals("\n" + "<p>def</p>", wikiModel.render("{{OnlyicludeDemo}}", false));
	}

	/**
	 * Issue 86
	 */
	public void testInclude() {
		assertEquals(
				"{| class=\"wikitable float-right\" style=\"width:30%; min-width:250px; max-width:400px; font-size:90%; margin-top:0px;\"\n"
						+ "|--\n" + "! colspan=\"2\" style=\"background-color:Khaki; font-size:110%;\" | [[Asteroid]]<br/>{{{Name}}}\n"
						+ "|--\n" + "|}", wikiModel.parseTemplates("{{TestInclude}}"));
	}

	/**
	 * Issue 86
	 */
	public void testInclude2() {
		assertEquals(
				"{| class=\"wikitable float-right\" style=\"width:30%; min-width:250px; max-width:400px; font-size:90%; margin-top:0px;\"\n"
						+ "|--\n" + "! colspan=\"2\" style=\"background-color:Khaki; font-size:110%;\" | [[Asteroid]]<br/>{{{Name}}}\n"
						+ "|--\n" + "|}", wikiModel.parseTemplates("{{TestInclude2}}"));
	}

	/**
	 * Issue 86
	 */
	public void testInclude3() {
		assertEquals("text", wikiModel.parseTemplates("{{TestInclude3}}"));
	}

	/**
	 * Issue 86
	 */
	public void testInclude4() {
		assertEquals("Text ", wikiModel.parseTemplates("{{TestInclude4}}"));
	}

	public void test00() {
		assertEquals(
				"{| class=\"float-right\" style=\"width:290px; font-size:90%; background:#FAFAFA;  border:1px solid #bbb; margin:0px 0px 1em 1em; border-collapse:collapse;\" summary=\"Infobox\"\n"
						+ "|-\n"
						+ "| colspan=\"2\" style=\"background:#ffffff; text-align:center; font-size:135%;\" | '''PAGENAME'''</font></br><small></small>\n"
						+ "|- class=\"hintergrundfarbe2\"\n"
						+ "| colspan=\"2\" style=\"font-weight:bold; padding-left:8px; border-top:solid 1px #bbb;\" |\n"
						+ "|- class=\"hintergrundfarbe2\" style=\"text-align: center;\"\n"
						+ "| style=\"width: 50%;\" | [[Bild:Sin escudo.svg|120px|Wappn fêîht]]\n"
						+ "| align=\"center\" style=\"width: 50%;\" | {| border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\"\n"
						+ "|<div style=\"position: relative;\"><div style=\"font-size: 5px; position: absolute; display: block; left:87px; top:117px; padding:0;\">[[Bild:reddot.svg|5px|PAGENAME]]</div>[[Bild:Karte Deutschland.svg|140x175px|Deitschlandkartn, Position vo PAGENAME heavoghom]]</div>\n"
						+ "|}\n"
						+ "|-\n"
						+ "! colspan=\"2\" style=\"background-color:#ABCDEF; border:1px solid #bbb;\" | Basisdatn\n"
						+ "|- class=\"hintergrundfarbe2\"\n"
						+ "| '''[[Bundesland (Deutschland)|Bundesland]]''': || [[Bayern]]\n"
						+ "|- class=\"hintergrundfarbe2\"\n" + "|}", wikiModel.parseTemplates("{{Infobox Ort in Deutschland\n"
						+ "|Art               = Stadt\n" + "|Wappen            = Wappen_Grafenwöhr.png\n"
						+ "|lat_deg           = 49 |lat_min = 43\n" + "|lon_deg           = 11 |lon_min = 54\n" + "|Lageplan          = \n"
						+ "|Bundesland        = Bayern\n" + "}}"));
	}

	public void test000() {
		assertEquals(
				"{| border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\"\n"
						+ "|<div style=\"position: relative;\"><div style=\"font-size: 5px; position: absolute; display: block; left:108px; top:55px; padding:0;\">[[Bild:reddot.svg|5px|PAGENAME]]</div>[[Bild:Karte Deutschland.svg|140x175px|Deitschlandkartn, Position vo PAGENAME heavoghom]]</div>\n"
						+ "|}", wikiModel
						.parseTemplates("{{Lageplan\n" + "|marker     = reddot.svg\n" + "|markersize = 5\n"
								+ "|markertext = {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }}\n"
								+ "|pos_y      = {{#expr: (55.1 - {{{lat_deg|52.5}}} - {{{lat_min|0}}} / 60) * 100 / 7.9}}\n"
								+ "|pos_x      = {{#expr: ({{{lon_deg|13.4}}} + {{{lon_min|0}}} / 60) * 10 - 55}}\n"
								+ "|map        = Karte Deutschland.svg\n" + "|mapsize_x  = 140\n" + "|mapsize_y  = 175\n"
								+ "|maptext    = Deitschlandkartn, Position vo {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }} heavoghom\n"
								+ "|warning    = [[Bild:Missing Map of Germany.png|140px|Koordinaten san außerhoib vom darstellbarn Bereich]]\n"
								+ "}}"));
	}

	public void test001() {
		assertEquals(
				"\n"
						+ "<div style=\"page-break-inside: avoid;\">\n"
						+ "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\">\n"
						+ "<tr>\n"
						+ "<td>\n"
						+ "<div style=\"position: relative;\">\n"
						+ "<div style=\"font-size: 5px; position: absolute; display: block; left:108px; top:55px; padding:0;\"><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:5px-reddot.svg.png\" title=\"PAGENAME\"><img src=\"http://www.bliki.info/wiki/5px-reddot.svg.png\" alt=\"PAGENAME\" width=\"5\" />\n"
						+ "</a></div><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:140px-Karte_Deutschland.svg.png\" title=\"Deitschlandkartn, Position vo PAGENAME heavoghom\"><img src=\"http://www.bliki.info/wiki/140px-Karte_Deutschland.svg.png\" alt=\"Deitschlandkartn, Position vo PAGENAME heavoghom\" height=\"175\" width=\"140\" />\n"
						+ "</a></div></td></tr></table></div>", wikiModel.render(
						"{{Lageplan\n" + "|marker     = reddot.svg\n" + "|markersize = 5\n"
								+ "|markertext = {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }}\n"
								+ "|pos_y      = {{#expr: (55.1 - {{{lat_deg|52.5}}} - {{{lat_min|0}}} / 60) * 100 / 7.9}}\n"
								+ "|pos_x      = {{#expr: ({{{lon_deg|13.4}}} + {{{lon_min|0}}} / 60) * 10 - 55}}\n"
								+ "|map        = Karte Deutschland.svg\n" + "|mapsize_x  = 140\n" + "|mapsize_y  = 175\n"
								+ "|maptext    = Deitschlandkartn, Position vo {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }} heavoghom\n"
								+ "|warning    = [[Bild:Missing Map of Germany.png|140px|Koordinaten san außerhoib vom darstellbarn Bereich]]\n"
								+ "}}", false));
	}

	public void test002() {
		assertEquals(
				"{| class=\"float-right\" style=\"width:290px; font-size:90%; background:#FAFAFA;  border:1px solid #bbb; margin:0px 0px 1em 1em; border-collapse:collapse;\" summary=\"Infobox\"\n"
						+ "|-\n"
						+ "| colspan=\"2\" style=\"background:#ffffff; text-align:center; font-size:135%;\" | '''PAGENAME'''</font></br><small></small>\n"
						+ "|- class=\"hintergrundfarbe2\"\n"
						+ "| colspan=\"2\" style=\"font-weight:bold; padding-left:8px; border-top:solid 1px #bbb;\" |\n"
						+ "|- class=\"hintergrundfarbe2\" style=\"text-align: center;\"\n"
						+ "| style=\"width: 50%;\" | [[Bild:Sin escudo.svg|120px|Wappn fêîht]]\n"
						+ "| align=\"center\" style=\"width: 50%;\" | [[Bild:Karte Deutschland.png|140px|Koordinatn san net da]]\n"
						+ "|-\n"
						+ "! colspan=\"2\" style=\"background-color:#ABCDEF; border:1px solid #bbb;\" | Basisdatn\n"
						+ "|- class=\"hintergrundfarbe2\"\n"
						+ "| '''[[Bundesland (Deutschland)|Bundesland]]''': || [[]]\n"
						+ "|- class=\"hintergrundfarbe2\"\n" + "|}", wikiModel.parseTemplates("{{Infobox Ort in Deutschland}}"));
	}

	public void test003() {
		assertEquals(
				"\n"
						+ "<div style=\"page-break-inside: avoid;\">\n"
						+ "<table class=\"float-right\" style=\"width:290px; font-size:90%; background:#FAFAFA;  border:1px solid #bbb; margin:0px 0px 1em 1em; border-collapse:collapse;\" summary=\"Infobox\">\n"
						+ "<tr>\n"
						+ "<td colspan=\"2\" style=\"background:#ffffff; text-align:center; font-size:135%;\"><b>PAGENAME</b></td></tr>\n"
						+ "<tr class=\"hintergrundfarbe2\">\n"
						+ "<td colspan=\"2\" style=\"font-weight:bold; padding-left:8px; border-top:solid 1px #bbb;\" /></tr>\n"
						+ "<tr class=\"hintergrundfarbe2\" style=\"text-align: center;\">\n"
						+ "<td style=\"width: 50%;\"><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:120px-Sin_escudo.svg.png\" title=\"Wappn fêîht\"><img src=\"http://www.bliki.info/wiki/120px-Sin_escudo.svg.png\" alt=\"Wappn fêîht\" width=\"120\" />\n"
						+ "</a></td>\n"
						+ "<td align=\"center\" style=\"width: 50%;\"><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:140px-Karte_Deutschland.png\" title=\"Koordinatn san net da\"><img src=\"http://www.bliki.info/wiki/140px-Karte_Deutschland.png\" alt=\"Koordinatn san net da\" width=\"140\" />\n"
						+ "</a></td></tr>\n"
						+ "<tr>\n"
						+ "<th colspan=\"2\" style=\"background-color:#ABCDEF; border:1px solid #bbb;\">Basisdatn</th></tr>\n"
						+ "<tr class=\"hintergrundfarbe2\">\n"
						+ "<td><b><a href=\"http://www.bliki.info/wiki/Bundesland_(Deutschland)\" title=\"Bundesland (Deutschland)\">Bundesland</a></b>: </td>\n"
						+ "<td><a href=\"http://www.bliki.info/wiki/\" /></td></tr></table></div>", wikiModel.render(
						"{{Infobox Ort in Deutschland}}", false));
	}

	public void test004() {
		assertEquals(
				"{| class=\"float-right\" style=\"width:290px; font-size:90%; background:#FAFAFA;  border:1px solid #bbb; margin:0px 0px 1em 1em; border-collapse:collapse;\" summary=\"Infobox\"\n"
						+ "|-\n"
						+ "| colspan=\"2\" style=\"background:#ffffff; text-align:center; font-size:135%;\" | '''PAGENAME'''</font></br><small></small>\n"
						+ "|- class=\"hintergrundfarbe2\"\n"
						+ "| colspan=\"2\" style=\"font-weight:bold; padding-left:8px; border-top:solid 1px #bbb;\" |\n"
						+ "|- class=\"hintergrundfarbe2\" style=\"text-align: center;\"\n"
						+ "| style=\"width: 50%;\" | [[Bild:Sin escudo.svg|120px|Wappn fêîht]]\n"
						+ "| align=\"center\" style=\"width: 50%;\" | {| border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\"\n"
						+ "|<div style=\"position: relative;\"><div style=\"font-size: 5px; position: absolute; display: block; left:87px; top:117px; padding:0;\">[[Bild:reddot.svg|5px|PAGENAME]]</div>[[Bild:Karte Deutschland.svg|140x175px|Deitschlandkartn, Position vo PAGENAME heavoghom]]</div>\n"
						+ "|}\n"
						+ "|-\n"
						+ "! colspan=\"2\" style=\"background-color:#ABCDEF; border:1px solid #bbb;\" | Basisdatn\n"
						+ "|- class=\"hintergrundfarbe2\"\n"
						+ "| '''[[Bundesland (Deutschland)|Bundesland]]''': || [[Bayern]]\n"
						+ "|- class=\"hintergrundfarbe2\"\n" + "|}", wikiModel.parseTemplates("{{Infobox Ort in Deutschland\n"
						+ "|Art               = Stadt\n" + "|Wappen            = Wappen_Grafenwöhr.png\n"
						+ "|lat_deg           = 49 |lat_min = 43\n" + "|lon_deg           = 11 |lon_min = 54\n" + "|Lageplan          = \n"
						+ "|Bundesland        = Bayern\n" + "}}"));
	}

	public void test005() {
		assertEquals(
				"\n"
						+ "<div style=\"page-break-inside: avoid;\">\n"
						+ "<table class=\"float-right\" style=\"width:290px; font-size:90%; background:#FAFAFA;  border:1px solid #bbb; margin:0px 0px 1em 1em; border-collapse:collapse;\" summary=\"Infobox\">\n"
						+ "<tr>\n"
						+ "<td colspan=\"2\" style=\"background:#ffffff; text-align:center; font-size:135%;\"><b>PAGENAME</b></td></tr>\n"
						+ "<tr class=\"hintergrundfarbe2\">\n"
						+ "<td colspan=\"2\" style=\"font-weight:bold; padding-left:8px; border-top:solid 1px #bbb;\" /></tr>\n"
						+ "<tr class=\"hintergrundfarbe2\" style=\"text-align: center;\">\n"
						+ "<td style=\"width: 50%;\"><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:120px-Sin_escudo.svg.png\" title=\"Wappn fêîht\"><img src=\"http://www.bliki.info/wiki/120px-Sin_escudo.svg.png\" alt=\"Wappn fêîht\" width=\"120\" />\n"
						+ "</a></td>\n"
						+ "<td align=\"center\" style=\"width: 50%;\">\n"
						+ "<div style=\"page-break-inside: avoid;\">\n"
						+ "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\">\n"
						+ "<tr>\n"
						+ "<td>\n"
						+ "<div style=\"position: relative;\">\n"
						+ "<div style=\"font-size: 5px; position: absolute; display: block; left:87px; top:117px; padding:0;\"><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:5px-reddot.svg.png\" title=\"PAGENAME\"><img src=\"http://www.bliki.info/wiki/5px-reddot.svg.png\" alt=\"PAGENAME\" width=\"5\" />\n"
						+ "</a></div><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:140px-Karte_Deutschland.svg.png\" title=\"Deitschlandkartn, Position vo PAGENAME heavoghom\"><img src=\"http://www.bliki.info/wiki/140px-Karte_Deutschland.svg.png\" alt=\"Deitschlandkartn, Position vo PAGENAME heavoghom\" height=\"175\" width=\"140\" />\n"
						+ "</a></div></td></tr></table></div></td></tr>\n"
						+ "<tr>\n"
						+ "<th colspan=\"2\" style=\"background-color:#ABCDEF; border:1px solid #bbb;\">Basisdatn</th></tr>\n"
						+ "<tr class=\"hintergrundfarbe2\">\n"
						+ "<td><b><a href=\"http://www.bliki.info/wiki/Bundesland_(Deutschland)\" title=\"Bundesland (Deutschland)\">Bundesland</a></b>: </td>\n"
						+ "<td><a href=\"http://www.bliki.info/wiki/Bayern\" title=\"Bayern\">Bayern</a></td></tr></table></div>", wikiModel
						.render("{{Infobox Ort in Deutschland\n" + "|Art               = Stadt\n"
								+ "|Wappen            = Wappen_Grafenwöhr.png\n" + "|lat_deg           = 49 |lat_min = 43\n"
								+ "|lon_deg           = 11 |lon_min = 54\n" + "|Lageplan          = \n" + "|Bundesland        = Bayern\n" + "}}",
								false));
	}

	public void test006() {
		assertEquals(
				"\n"
						+ "<div style=\"page-break-inside: avoid;\">\n"
						+ "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\">\n"
						+ "<tr>\n"
						+ "<td>\n"
						+ "<div style=\"position: relative;\">\n"
						+ "<div style=\"font-size: 5px; position: absolute; display: block; left:108px; top:55px; padding:0;\"><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:5px-reddot.svg.png\" title=\"PAGENAME\"><img src=\"http://www.bliki.info/wiki/5px-reddot.svg.png\" alt=\"PAGENAME\" width=\"5\" />\n"
						+ "</a></div><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:140px-Karte_Deutschland.svg.png\" title=\"Deitschlandkartn, Position vo PAGENAME heavoghom\"><img src=\"http://www.bliki.info/wiki/140px-Karte_Deutschland.svg.png\" alt=\"Deitschlandkartn, Position vo PAGENAME heavoghom\" height=\"175\" width=\"140\" />\n"
						+ "</a></div></td></tr></table></div>",
				wikiModel
						.render(
								"{{#if: {{{Karte|}}} | [[Bild:{{{Karte}}}|140x175px|Deitschlandkartn, Position vo {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }} heavoghobn]] | {{#if: {{{lat_deg|t2}}} |\n"
										+ "{{Lageplan\n"
										+ "|marker     = reddot.svg\n"
										+ "|markersize = 5\n"
										+ "|markertext = {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }}\n"
										+ "|pos_y      = {{#expr: (55.1 - {{{lat_deg|52.5}}} - {{{lat_min|0}}} / 60) * 100 / 7.9}}\n"
										+ "|pos_x      = {{#expr: ({{{lon_deg|13.4}}} + {{{lon_min|0}}} / 60) * 10 - 55}}\n"
										+ "|map        = Karte Deutschland.svg\n"
										+ "|mapsize_x  = 140\n"
										+ "|mapsize_y  = 175\n"
										+ "|maptext    = Deitschlandkartn, Position vo {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }} heavoghom\n"
										+ "|warning    = [[Bild:Missing Map of Germany.png|140px|Koordinaten san außerhoib vom darstellbarn Bereich]]\n"
										+ "}}\n" + "| [[Bild:Karte Deutschland.png|140px|Koordinatn san net da]] }}\n" + "}}", false));
	}

	public void test010() {
		assertEquals(
				"start\n"
						+ "{|border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\"\n"
						+ "|<div style=\"position: relative;\"><div style=\"font-size:16px\">middle</div></div> \n" + "|}\n" + "end",
				wikiModel
						.parseTemplates("start\n{{#if: {{{1|test}}} | {{{!}}border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\"\n"
								+ "{{!}}<div style=\"position: relative;\"><div style=\"font-size:16px\">middle</div></div> \n{{!}}} }}\nend"));
	}

	public void testIf00() {
		assertEquals(
				"start[[:Template:es-verb form of/indicative]]end",
				wikiModel
						.parseTemplates("start{{{{ #if:  | l | u }}cfirst:  {{ es-verb form of/{{ #switch: indicative  | ind | indicative = indicative  | subj | subjunctive = subjunctive  | imp | imperative = imperative  | cond | conditional = conditional  | par | part | participle | past participle  | past-participle = participle  | adv | adverbial | ger | gerund | gerundive  | gerundio | present participle  | present-participle = adverbial  | error  }}  | tense =  {{ #switch: present  | pres | present = present  | imp | imperfect = imperfect  | pret | preterit | preterite = preterite  | fut | future = future  | cond | conditional = conditional  }}  | number =  {{ #switch: singular  | s | sg | sing | singular = singular  | p | pl | plural = plural  }}  | person =  {{ #switch: 1  | 1 | first | first person | first-person = first  | 2 | second|second person | second-person = second  | 3 | third | third person | third-person = third  | 0 | - | imp | impersonal = impersonal  }}  | formal =  {{ #switch: {{{formal}}}  | y | yes = yes  | n | no = no  }}  | gender =  {{ #switch:   | m | masc | masculine = masculine  | f | fem | feminine = feminine  }}  | sense =  {{ #switch: {{{sense}}}  | + | aff | affirmative = affirmative  | - | neg | negative = negative  }}  | sera = {{ #switch: {{{sera}}} | se = se | ra = ra }}  | ending =  {{ #switch: ar  | ar | -ar = -ar  | er | -er = -er  | ir | -ir = -ir  }}  | participle =   | voseo = {{ #if:  | yes | no }}  }}}}end"));
	}

	public void testIf01() {
		assertEquals("startnoend", wikiModel.parseTemplates("start{{#if:\n" + "\n" + "\n" + "| yes | no}}end"));
	}

	public void testIf02() {
		assertEquals("startend", wikiModel.parseTemplates("start{{      #if:   \n    |{{#ifeq:{{{seperator}}}|;|;|. }}     }}end"));
	}

	public void testIf03() {
		assertEquals("startyesend", wikiModel.parseTemplates("start{{#if:string\n" + "\n" + "\n" + "| yes | no}}end"));
	}

	public void testIf04() {
		assertEquals("startend", wikiModel.parseTemplates("start{{#if: | yes }}end"));
	}

	public void testIfeq01() {
		assertEquals("startyesend", wikiModel.parseTemplates("start{{#ifeq: 01 | 1 | yes | no}}end"));
	}

	public void testIfeq02() {
		assertEquals("startyesend", wikiModel.parseTemplates("start{{#ifeq: 0 | -0 | yes | no}}end"));
	}

	public void testIfeq03() {
		assertEquals("startnoend", wikiModel.parseTemplates("start{{#ifeq: \"01\" | \"1\" | yes | no}}end"));
	}

	public void testIf05() {
		assertEquals("startend", wikiModel.parseTemplates("start{{#if: foo | | no}}end"));
	}

	/**
	 * See issue 102
	 */
	public void testIf06() {
		assertEquals("== SpaceInIfTest1 ==\n" + "Article space", wikiModel.parseTemplates("== SpaceInIfTest1 ==\n"
				+ "{{#if:{{NAMESPACE}}\n" + "| <!--Not article space, do nothing-->Not article space\n"
				+ "| <!--Article space-->Article space\n" + "}}"));

		assertEquals("== SpaceInIfTest2 ==\n" + "Article space\n" + "", wikiModel.parseTemplates("== SpaceInIfTest2 ==\n"
				+ "{{#if:{{NAMESPACE}}\n" + "| <!--Not article space, do nothing-->\n" + "  Not article space\n"
				+ "| <!--Article space-->\n" + "  Article space\n" + "}}\n" + ""));
	}

	public void testIfexpr01() {
		assertEquals("startnoend", wikiModel.parseTemplates("start{{#ifexpr: 1 < 0 | | no}}end"));
	}

	public void testIfexpr02() {
		assertEquals("startend", wikiModel.parseTemplates("start{{#ifexpr: 1 > 0 | | no}}end"));
	}

	public void testIfexpr03() {
		assertEquals("start<div class=\"error\">Expression error: Error in factor at character: ' ' (0)</div>end", wikiModel
				.parseTemplates("start{{#ifexpr: 1 + |no| }}end"));
	}

	public void testBORN_DATA() {
		assertEquals(
				"test Thomas Jeffrey Hanks<br />[[Concord, California]],  [[United States|U.S.]] test123",
				wikiModel
						.parseTemplates("test {{Born_data | birthname = Thomas Jeffrey Hanks | birthplace = [[Concord, California]],  [[United States|U.S.]] }} test123"));
	}

	public void testMONTHNUMBER() {
		assertEquals("test 10 test123", wikiModel.parseTemplates("test {{MONTHNUMBER | 10 }} test123"));
	}

	public void testMONTHNAME() {
		assertEquals("test October test123", wikiModel.parseTemplates("test {{MONTHNAME | 10 }} test123"));
	}

	public void testAnarchismSidebar() {
		assertEquals("[[:Template:Sidebar]]", wikiModel.parseTemplates("{{Anarchism sidebar}}", false));
	}

	public void testNonExistentTemplate() {
		assertEquals("==Other areas of Wikipedia==\n" + "[[:Template:WikipediaOther]]", wikiModel.parseTemplates(
				"==Other areas of Wikipedia==\n" + "{{WikipediaOther}}<!--Template:WikipediaOther-->", false));
	}

	public void testTemplateCall1() {
		// see method WikiTestModel#getRawWikiContent()
		assertEquals("start-an include page-end", wikiModel.parseTemplates("start-{{:Include Page}}-end", false));
	}

	public void testTemplateCall2() {
		// see method WikiTestModel#getRawWikiContent()
		assertEquals("start-b) First: 3 Second: b-end start-c) First: sdfsf Second: klj-end", wikiModel.parseTemplates(
				"start-{{templ1|a=3|b}}-end start-{{templ2|sdfsf|klj}}-end", false));
	}

	public void testTemplateCall3() {
		// see method WikiTestModel#getRawWikiContent()
		assertEquals("b) First: Test1 Second: c) First: sdfsf Second: klj \n" + "\n" + "", wikiModel.parseTemplates("{{templ1\n"
				+ " | a = Test1\n" + " |{{templ2|sdfsf|klj}} \n" + "}}\n" + "", false));
	}

	public void testTemplateCall4() {
		// see method WikiTestModel#getRawWikiContent()
		assertEquals("[[:Template:[[Template:example|example]]]]", wikiModel.parseTemplates("{{tl|example}}", false));
	}

	public void testTemplateCall5() {
		// see method WikiTestModel#getRawWikiContent()
		assertEquals(
				"(pronounced <span title=\"Pronunciation in the International Phonetic Alphabet (IPA)\" class=\"IPA\">[[WP:IPA for English|/dəˌpeʃˈmoʊd/]]</span>)",
				wikiModel.parseTemplates("({{pron-en|dəˌpeʃˈmoʊd}})", false));
	}

	public void testTemplateParameter01() {
		// see method WikiTestModel#getTemplateContent()
		assertEquals("start-a) First: arg1 Second: arg2-end", wikiModel.parseTemplates("start-{{Test|arg1|arg2}}-end", false));
	}

	public void testTemplateParameter02() {
		// see method WikiTestModel#getTemplateContent()
		assertEquals(
				"start- ''[http://www.etymonline.com/index.php?search=hello&searchmode=none Online Etymology Dictionary]''. -end",
				wikiModel
						.parseTemplates(
								"start- {{cite web|url=http://www.etymonline.com/index.php?search=hello&searchmode=none|title=Online Etymology Dictionary}} -end",
								false));
	}

	public void testTemplateParameter03() {
		// see method WikiTestModel#getTemplateContent()
		assertEquals("start- <div class=\"references-small\" style=\"-moz-column-count:2; -webkit-column-count:2; column-count:2;\">\n"
				+ "<references /></div> -end", wikiModel.parseTemplates("start- {{reflist|2}} -end", false));
	}

	public void testTemplateParameter04() {
		// see method WikiTestModel#getTemplateContent()
		assertEquals("start-<nowiki>{{Test|arg1|arg2}}-</noWiKi>end", wikiModel.parseTemplates(
				"start-<nowiki>{{Test|arg1|arg2}}-</noWiKi>end", false));
	}

	public void testTemplateParameter05() {
		// see method WikiTestModel#getTemplateContent()
		assertEquals("start- end", wikiModel.parseTemplates("start- <!-- {{Test|arg1|arg2}} \n --->end", false));
	}

	// 
	public void testTemplate06() {
		assertEquals("A is not equal B", wikiModel.parseTemplates("{{#ifeq: A | B | A equals B | A is not equal B}}", false));
	}

	public void testTemplate07() {
		assertEquals("start- A is not equal B \n" + " end", wikiModel.parseTemplates("start- {{ifeq|A|B}} \n end", false));
	}

	public void testNestedTemplate() {
		assertEquals("test a a nested template text template", wikiModel.parseTemplates("{{nested tempplate test}}", false));
	}

	public void testEndlessRecursion() {
		assertEquals("<span class=\"error\">Template loop detected: <strong class=\"selflink\">Template:recursion</strong></span>", wikiModel.parseTemplates("{{recursion}}", false));
	}

	private final String TEST_STRING_01 = "[[Category:Interwiki templates|wikipedia]]\n" + "[[zh:Template:Wikipedia]]\n"
			+ "</noinclude><div class=\"sister-\n" + "wikipedia\"><div class=\"sister-project\"><div\n"
			+ "class=\"noprint\" style=\"clear: right; border: solid #aaa\n"
			+ "1px; margin: 0 0 1em 1em; font-size: 90%; background: #f9f9f9; width:\n"
			+ "250px; padding: 4px; text-align: left; float: right;\">\n" + "<div style=\"float: left;\">[[Image:Wikipedia-logo-\n"
			+ "en.png|44px|none| ]]</div>\n" + "<div style=\"margin-left: 60px;\">{{#if:{{{lang|}}}|\n"
			+ "{{{{{lang}}}}}&amp;nbsp;}}[[Wikipedia]] has {{#if:{{{cat|\n" + "{{{category|}}}}}}|a category|{{#if:{{{mul|{{{dab|\n"
			+ "{{{disambiguation|}}}}}}}}}|articles|{{#if:{{{mulcat|}}}|categories|an\n" + "article}}}}}} on:\n"
			+ "<div style=\"margin-left: 10px;\">'''''{{#if:{{{cat|\n"
			+ "{{{category|}}}}}}|[[w:{{#if:{{{lang|}}}|{{{lang}}}:}}Category:\n"
			+ "{{ucfirst:{{{cat|{{{category}}}}}}}}|{{ucfirst:{{{1|{{{cat|\n"
			+ "{{{category}}}}}}}}}}}]]|[[w:{{#if:{{{lang|}}}|{{{lang}}}:}}{{ucfirst:\n"
			+ "{{#if:{{{dab|{{{disambiguation|}}}}}}|{{{dab|{{{disambiguation}}}}}}|\n"
			+ "{{{1|{{PAGENAME}}}}}}}}}|{{ucfirst:{{{2|{{{1|{{{dab|{{{disambiguation|\n"
			+ "{{PAGENAME}}}}}}}}}}}}}}}}]]}}''''' {{#if:{{{mul|{{{mulcat|}}}}}}|and\n"
			+ "'''''{{#if:{{{mulcat|}}}|[[w:{{#if:{{{lang|}}}|{{{lang}}}:}}Category:\n"
			+ "{{ucfirst:{{{mulcat}}}}}|{{ucfirst:{{{mulcatlabel|{{{mulcat}}}}}}}}]]|\n"
			+ "[[w:{{#if:{{{lang|}}}|{{{lang}}}:}}{{ucfirst:{{{mul}}}}}|{{ucfirst:\n" + "{{{mullabel|{{{mul}}}}}}}}]]'''''}}|}}</div>\n"
			+ "</div>\n" + "</div>\n" + "</div></div><span class=\"interProject\">[[w:\n"
			+ "{{#if:{{{lang|}}}|{{{lang}}}:}}{{#if:{{{cat|{{{category|}}}}}}|\n"
			+ "Category:{{ucfirst:{{{cat|{{{category}}}}}}}}|{{ucfirst:{{{dab|\n"
			+ "{{{disambiguation|{{{1|{{PAGENAME}}}}}}}}}}}}}}}|Wikipedia {{#if:\n"
			+ "{{{lang|}}}|<sup>{{{lang}}}</sup>}}]]</span>{{#if:\n" + "{{{mul|{{{mulcat|}}}}}}|<span class=\"interProject\">[[w:\n"
			+ "{{#if:{{{lang|}}}|{{{lang}}}:}}{{#if:{{{mulcat|}}}|Category:{{ucfirst:\n"
			+ "{{{mulcat}}}}}|{{ucfirst:{{{mul}}}}}}}|Wikipedia {{#if:{{{lang|}}}|\n" + "<sup>{{{lang}}}</sup>}}]]</span>}}";

	public void testNestedIf01() {
		// String temp = StringEscapeUtils.unescapeHtml(TEST_STRING_01);
		String temp = TEST_STRING_01;
		assertEquals("[[Category:Interwiki templates|wikipedia]]\n" + "[[zh:Template:Wikipedia]]\n"
				+ "</noinclude><div class=\"sister-\n" + "wikipedia\"><div class=\"sister-project\"><div\n"
				+ "class=\"noprint\" style=\"clear: right; border: solid #aaa\n"
				+ "1px; margin: 0 0 1em 1em; font-size: 90%; background: #f9f9f9; width:\n"
				+ "250px; padding: 4px; text-align: left; float: right;\">\n" + "<div style=\"float: left;\">[[Image:Wikipedia-logo-\n"
				+ "en.png|44px|none| ]]</div>\n" + "<div style=\"margin-left: 60px;\">[[Wikipedia]] has an\n" + "article on:\n"
				+ "<div style=\"margin-left: 10px;\">\'\'\'\'\'[[w:PAGENAME|PAGENAME]]\'\'\'\'\' </div>\n" + "</div>\n" + "</div>\n"
				+ "</div></div><span class=\"interProject\">[[w:\n" + "PAGENAME|Wikipedia ]]</span>", wikiModel.parseTemplates(temp, false));
	}

	private final String TEST_STRING_02 = " {{#if:{{{cat|\n" + "{{{category|}}}}}}|a category|{{#if:{{{mul|{{{dab|\n"
			+ "{{{disambiguation|}}}}}}}}}|articles|{{#if:{{{mulcat|}}}|categories|an\n" + "article}}}}}} on:\n";

	public void testNestedIf02() {
		assertEquals(" an\n" + "article on:\n" + "", wikiModel.parseTemplates(TEST_STRING_02, false));
	}

	public void testNestedIf03() {
		assertEquals("PAGENAME", wikiModel.parseTemplates(TEST_STRING_03, false));
	}

	private final String TEST_STRING_04 = "{{ucfirst:{{{cat|{{{category}}}}}}}}";

	public void testNestedIf04() {
		assertEquals("{{{category}}}", wikiModel.parseTemplates(TEST_STRING_04, false));
	}//

	public void testSwitch001() {
		assertEquals("UPPER", wikiModel.parseTemplates("{{ #switch: A | a=lower | A=UPPER  }}", false));
	}

	public void testSwitch002() {
		assertEquals("lower", wikiModel.parseTemplates("{{ #switch: {{lc:A}} | a=lower | UPPER  }}", false));
	}

	public void testSwitch003() {
		assertEquals("'''''abc''' or '''ABC'''''", wikiModel.parseTemplates("{{#switch: {{lc: {{{1| B }}} }}\n" + "| a\n" + "| b\n"
				+ "| c = '''''abc''' or '''ABC'''''\n" + "| A\n" + "| B\n" + "| C = ''Memory corruption due to cosmic rays''\n"
				+ "| #default = N/A\n" + "}}", false));
	}

	public void testSwitch004() {
		assertEquals("Yes", wikiModel.parseTemplates("{{ #switch: +07 | 7 = Yes | 007 = Bond | No  }}", false));
	}

	public void testSwitch005() {
		assertEquals("Nothing", wikiModel.parseTemplates("{{#switch: | = Nothing | foo = Foo | Something }}", false));
	}

	public void testSwitch006() {
		assertEquals("Something", wikiModel.parseTemplates("{{#switch: test | = Nothing | foo = Foo | Something }}", false));
	}

	public void testSwitch007() {
		assertEquals("Bar", wikiModel.parseTemplates("{{#switch: test | foo = Foo | #default = Bar | baz = Baz }}", false));
	}

	public void testSwitch008() {
		assertEquals("[[:Template:Templ1/ind&]]", wikiModel.parseTemplates("{{Templ1/{{ #switch: imperative  | ind | ind&}}}}", false));
	}

	/**
	 * <a href="https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Parameter_selection_templates"
	 * >Help:Newlines_and_spaces#Parameter_selection_templates</a>
	 */
	public void testSwitch009() {
		assertEquals("*\"q\"", wikiModel.parseTemplates("*\"{{#switch: p |p= q |r=s}}\"", false));
		assertEquals("*\"q\"", wikiModel.parseTemplates("*\"{{#switch:p| p = q |r=s}}\"", false));
	}

	/**
	 * See <a href="http://www.mediawiki.org/wiki/Help:Extension:ParserFunctions#Comparison_behaviour"
	 * >Help:Extension:ParserFunctions - Comparison behaviour</a>
	 */
	public void testSwitch010() {
		assertEquals("three", wikiModel.parseTemplates("{{#switch: 0 + 1 | 1 = one | 2 = two | three}}", false));
		assertEquals("one", wikiModel.parseTemplates("{{#switch: {{#expr: 0 + 1}} | 1 = one | 2 = two | three}}", false));
		assertEquals("A", wikiModel.parseTemplates("{{#switch: a | a = A | b = B | C}}", false));
		assertEquals("C", wikiModel.parseTemplates("{{#switch: A | a = A | b = B | C}}", false));
		assertEquals("Nothing", wikiModel.parseTemplates("{{#switch: | = Nothing | foo = Foo | Something }}", false));
		assertEquals("Bar", wikiModel.parseTemplates("{{#switch: b | f = Foo | b = Bar | b = Baz | }}", false));
		assertEquals("B", wikiModel.parseTemplates("{{#switch: 12345678901234567 | 12345678901234568 = A | B}}", false));

		assertEquals("A", wikiModel.parseTemplates("{{#ifexpr: 12345678901234567 = 12345678901234568 | A | B}}", false));

	}

	public void testExpr001() {
		assertEquals("1.0E-6", wikiModel.parseTemplates("{{ #expr: 0.000001 }}", false));
	}

	public void testExpr002() {
		assertEquals("210", wikiModel.parseTemplates("{{ #expr: +30 * +7 }}", false));
	}

	public void testExpr003() {
		assertEquals("210", wikiModel.parseTemplates("{{ #expr: -30 * -7 }}", false));
	}

	public void testExpr004() {
		assertEquals("210", wikiModel.parseTemplates("{{ #expr: 30 * 7 }}", false));
	}

	public void testExpr005() {
		assertEquals("4.285714285714286", wikiModel.parseTemplates("{{ #expr: 30 / 7 }}", false));
		// assertEquals("4.285714285714286", wikiModel.parseTemplates("{{ #expr: 30
		// div 7 }}", false));
	}

	public void testExpr006() {
		assertEquals("37", wikiModel.parseTemplates("{{ #expr: 30 + 7 }}", false));
	}

	public void testExpr007() {
		assertEquals("23", wikiModel.parseTemplates("{{ #expr: 30 - 7 }}", false));
	}

	public void testExpr008() {
		assertEquals("19", wikiModel.parseTemplates("{{ #expr: 30 - 7 - 4}}", false));
	}

	public void testExpr009() {
		assertEquals("4.2857", wikiModel.parseTemplates("{{ #expr: 30 / 7 round 4}}", false));
	}

	public void testExpr010() {
		assertEquals("1", wikiModel.parseTemplates("{{ #expr: 30 <> 7}}", false));
		assertEquals("1", wikiModel.parseTemplates("{{ #expr: 30 != 7}}", false));
	}

	public void testExpr011() {
		assertEquals("0", wikiModel.parseTemplates("{{ #expr: 30 < 7}}", false));
		assertEquals("1", wikiModel.parseTemplates("{{ #expr: 30 <= 42}}", false));
	}

	public void testExpr012() {
		assertEquals("259", wikiModel.parseTemplates("{{ #expr: (30 + 7)*7 }}", false));
		assertEquals("79", wikiModel.parseTemplates("{{ #expr: 30 + 7 * 7 }}", false));
	}

	public void testExpr013() {
		assertEquals("0", wikiModel.parseTemplates("{{ #expr: 4 < 5 and 4 mod 2 }}", false));
		assertEquals("1", wikiModel.parseTemplates("{{ #expr: 4 < 5 or 4 mod 2 }}", false));
	}

	public void testExpr014() {
		assertEquals("7", wikiModel.parseTemplates("{{ #expr: not 0 * 7 }}", false));
		assertEquals("7", wikiModel.parseTemplates("{{ #expr: not 30 + 7 }}", false));
	}

	public void testFormatnum001() {
		// default locale is ENGLISH
		assertEquals("1,401", wikiModel.parseTemplates("{{formatnum:1401}}", false));
		assertEquals("987,654,321.654", wikiModel.parseTemplates("{{formatnum:987654321.654321}}", false));
		assertEquals("987,654,321.654", wikiModel.parseTemplates("{{FORMATNUM:987654321.654321}}", false));
		
		WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
		assertEquals("1.401", germanWikiModel.parseTemplates("{{formatnum:1401}}", false));
		assertEquals("987.654.321,654", germanWikiModel.parseTemplates("{{formatnum:987654321.654321}}", false));
		
		WikiModel italianWikiModel = newWikiTestModel(Locale.ITALIAN);
		assertEquals("1.401", italianWikiModel.parseTemplates("{{formatnum:1401}}", false));
		assertEquals("987.654.321", italianWikiModel.parseTemplates("{{formatnum:987654321}}", false));
	}

	public void testFormatnum002() {
		// default locale is ENGLISH
		assertEquals("9.87654321654321E8", wikiModel.parseTemplates("{{formatnum:987,654,321.654321|R}}", false));

		WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
		assertEquals("9.87654321654321E8", germanWikiModel.parseTemplates("{{formatnum:987.654.321,654321|R}}", false));
	}

	public void testFormatnum003() {
		// default locale is ENGLISH
		assertEquals("90", wikiModel.parseTemplates("{{formatnum:90}}"));
		WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
		assertEquals("90", germanWikiModel.parseTemplates("{{formatnum:90}}"));
	}

	public void testFormatnum004() {
		// default locale is ENGLISH
		assertEquals("90.", wikiModel.parseTemplates("{{formatnum:90.}}"));
		WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
		assertEquals("90,", germanWikiModel.parseTemplates("{{formatnum:90.}}"));
	}

	public void testFormatnum005() {
		// default locale is ENGLISH
		assertEquals("90.0", wikiModel.parseTemplates("{{formatnum:90.0}}"));
		WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
		assertEquals("90,0", germanWikiModel.parseTemplates("{{formatnum:90.0}}"));
	}
	
	public void testFormatnum006() {
		// default locale is ENGLISH
		assertEquals("90.000", wikiModel.parseTemplates("{{formatnum:90.000}}"));
		assertEquals("90.000000000000000000000000000000000000", wikiModel.parseTemplates("{{formatnum:90.000000000000000000000000000000000000}}"));
		WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
		assertEquals("90,000", germanWikiModel.parseTemplates("{{formatnum:90.000}}"));
		assertEquals("90,000000000000000000000000000000000000", germanWikiModel.parseTemplates("{{formatnum:90.000000000000000000000000000000000000}}"));
	}

	public void testPlural001() {
		assertEquals("is", wikiModel.parseTemplates("{{plural:n|is|are}}", false));
		assertEquals("is", wikiModel.parseTemplates("{{plural:0|is|are}}", false));
		assertEquals("is", wikiModel.parseTemplates("{{plural:1|is|are}}", false));
		assertEquals("are", wikiModel.parseTemplates("{{plural:2|is|are}}", false));
		assertEquals("are", wikiModel.parseTemplates("{{plural:3|is|are}}", false));
		assertEquals("are", wikiModel.parseTemplates("{{plural:{{#expr:30+7}}|is|are}}", false));
	}

	public void testExpr015() {
		assertEquals("1", wikiModel.parseTemplates("{{ #expr: trunc1.2}}", false));
		assertEquals("-1", wikiModel.parseTemplates("{{ #expr: trunc-1.2 }}", false));
		assertEquals("1", wikiModel.parseTemplates("{{ #expr: floor 1.2}}", false));
		assertEquals("-2", wikiModel.parseTemplates("{{ #expr: floor -1.2 }}", false));
		assertEquals("-2", wikiModel.parseTemplates("{{ #expr: fLoOr -1.2 }}", false));
		assertEquals("2", wikiModel.parseTemplates("{{ #expr: ceil 1.2}}", false));
		assertEquals("-1", wikiModel.parseTemplates("{{ #expr: ceil-1.2 }}", false));
	}

	public void testExpr016() {
		assertEquals("1.0E-7", wikiModel.parseTemplates("{{#expr:1.0E-7}}", false));
	}

	public void testExpr017() {
		assertEquals("<div class=\"error\">Expression error: Division by zero</div>", wikiModel.parseTemplates("{{#expr:4/0}}", false));
		assertEquals("0.75", wikiModel.parseTemplates("{{#expr:3/4}}", false));
		assertEquals("<div class=\"error\">Expression error: Division by zero</div>", wikiModel.parseTemplates("{{#expr:13 mod 0}}",
				false));
		assertEquals("1", wikiModel.parseTemplates("{{#expr:13 mod 3}}", false));
		assertEquals("27", wikiModel.parseTemplates("{{#expr:3 ^3}}", false));
		assertEquals("0.037037037037037035", wikiModel.parseTemplates("{{#expr:3 ^ (-3)}}", false));
		assertEquals("NAN", wikiModel.parseTemplates("{{#expr:(-4) ^ (-1/2)}}", false));
		assertEquals("1", wikiModel.parseTemplates("{{#expr:ln EXp 1 }}", false));
		assertEquals("2.7182818284590455", wikiModel.parseTemplates("{{#expr:exp ln e }}", false));
		assertEquals("1", wikiModel.parseTemplates("{{#expr:sin (pi/2) }}", false));
		assertEquals("6.123233995736766E-17", wikiModel.parseTemplates("{{#expr:(sin pi)/2 }}", false));
		assertEquals("6.123233995736766E-17", wikiModel.parseTemplates("{{#expr:sin pi/2 }}", false));
	}

	public void testExpr018() {
		assertEquals("0", wikiModel.parseTemplates("{{#expr:1e-92round400}}", false));
		assertEquals("1", wikiModel.parseTemplates("{{#expr:(15782.316272965878)round((3))<1E9}}", false));
		assertEquals("1578200000", wikiModel.parseTemplates("{{#expr:((15782.316272965878)round((3))/1E5round0)E5}}", false));
	}

	/**
	 * See <a href=
	 * "http://www.mediawiki.org/wiki/Help:Extension:ParserFunctions#Rounding"
	 * >Help:Extension:ParserFunctions Expr Rounding</a>
	 */
	public void testExpr019() {
		assertEquals("9.88", wikiModel.parseTemplates("{{#expr: 9.876 round2 }}", false));
		assertEquals("1200", wikiModel.parseTemplates("{{#expr: (trunc1234) round trunc-2 }}", false));
		assertEquals("5", wikiModel.parseTemplates("{{#expr: 4.5 round0 }}", false));
		assertEquals("-5", wikiModel.parseTemplates("{{#expr: -4.5 round0 }}", false));
		// assertEquals("46.9",
		// wikiModel.parseTemplates("{{#expr: 46.857 round1.8 }}", false));
		// assertEquals("50",
		// wikiModel.parseTemplates("{{#expr: 46.857 round-1.8 }}", false));
	}

	public void testExpr020() {
		assertEquals("2.718281828459045", wikiModel.parseTemplates("{{#expr: e }}", false));
		assertEquals("3.141592653589793", wikiModel.parseTemplates("{{#expr: pi }}", false));
	}

	public void testNS001() {
		// TODO: these namespaces should actually have spaces
		assertEquals("User_talk", wikiModel.parseTemplates("{{ns:3}}", false));
		assertEquals("Help_talk", wikiModel.parseTemplates("{{ns:{{ns:12}}_talk}}", false));
		assertEquals("MediaWiki_talk", wikiModel.parseTemplates("{{ns:{{ns:8}}_talk}}", false));
		assertEquals("MediaWiki_talk", wikiModel.parseTemplates("{{ns:{{ns:8}} talk}}", false));
		assertEquals("MediaWiki_talk", wikiModel.parseTemplates("{{ns:{{ns:8}} talk  }}", false));
		assertEquals("[[:Template:Ns:MediaWikitalk]]", wikiModel.parseTemplates("{{ns:{{ns:8}}talk}}", false));
		assertEquals("Portal", wikiModel.parseTemplates("{{ns:100}}", false));
		assertEquals("Portal_talk", wikiModel.parseTemplates("{{ns:{{ns:100}}_talk}}", false));
	}

	public void testNSE001() {
		assertEquals("User_talk", wikiModel.parseTemplates("{{nse:3}}", false));
		assertEquals("Help_talk", wikiModel.parseTemplates("{{nse:{{nse:12}}_talk}}", false));
		assertEquals("MediaWiki_talk", wikiModel.parseTemplates("{{nse:{{nse:8}}_talk}}", false));
		assertEquals("MediaWiki_talk", wikiModel.parseTemplates("{{nse:{{nse:8}} talk}}", false));
		assertEquals("MediaWiki_talk", wikiModel.parseTemplates("{{nse:{{nse:8}} talk  }}", false));
		assertEquals("[[:Template:Ns:MediaWikitalk]]", wikiModel.parseTemplates("{{nse:{{nse:8}}talk}}", false));
		assertEquals("Portal", wikiModel.parseTemplates("{{ns:100}}", false));
		assertEquals("Portal_talk", wikiModel.parseTemplates("{{ns:{{ns:100}}_talk}}", false));
	}

	public void testNAMESPACE001() {
		assertEquals("", wikiModel.parseTemplates("{{NAMESPACE}}", false));
		assertEquals("Template", wikiModel.parseTemplates("{{NAMESPACE:Template:Main Page}}", false));
		assertEquals("", wikiModel.parseTemplates("{{NAMESPACE:Bad:Main Page}}", false));
	}

	public void testNAMESPACE002() {
		assertEquals("", wikiModel.parseTemplates("{{NAMESPACE}}", false));
		assertEquals("File", wikiModel.parseTemplates("{{NAMESPACE:Image:Main Page}}", false));
	}

	public void testNAMESPACE003() {
		wikiModel.setNamespaceName("category");
		assertEquals("Category", wikiModel.parseTemplates("{{NAMESPACE}}", false));
		assertEquals("File", wikiModel.parseTemplates("{{NAMESPACE:Image:Main Page}}", false));
	}

	public void testNAMESPACE004() {
		wikiModel.setPageName("Sandbox");
		wikiModel.setNamespaceName("meta");
		assertEquals("Meta", wikiModel.parseTemplates("{{NAMESPACE}}", false));
		assertEquals("", wikiModel.parseTemplates("{{NAMESPACE:}}", false));
	}

	public void testNAMESPACE005() {
		wikiModel.setPageName("Sandbox");
		wikiModel.setNamespaceName("meta_talk");
		assertEquals("Meta_talk", wikiModel.parseTemplates("{{NAMESPACEE}}", false));
		// TODO: namespace should actually have a space:
		assertEquals("Meta_talk", wikiModel.parseTemplates("{{NAMESPACE}}", false));
	}

	public void testNAMESPACE006() {
		assertEquals("File", wikiModel.parseTemplates("{{NAMESPACE:Image:test.jpg}}", false));
		assertEquals("File", wikiModel.parseTemplates("{{NAMESPACEE:Image:test.jpg}}", false));
	}

	public void testTALKSPACE001() {
		assertEquals("Talk", wikiModel.parseTemplates("{{TALKSPACE}}", false));
		assertEquals("Template_talk", wikiModel.parseTemplates("{{TALKSPACE:Template:Main Page}}", false));
		assertEquals("Talk", wikiModel.parseTemplates("{{TALKSPACE:Bad:Main Page}}", false));
	}

	public void testTALKSPACE002() {
		assertEquals("Talk", wikiModel.parseTemplates("{{TALKSPACE}}", false));
		assertEquals("File_talk", wikiModel.parseTemplates("{{TALKSPACE:Image:Main Page}}", false));
	}

	public void testTALKSPACE003() {
		wikiModel.setNamespaceName("category");
		assertEquals("Category_talk", wikiModel.parseTemplates("{{TALKSPACE}}", false));
		assertEquals("File_talk", wikiModel.parseTemplates("{{TALKSPACE:Image:Main Page}}", false));
	}

	public void testTALKSPACE004() {
		wikiModel.setPageName("Sandbox");
		wikiModel.setNamespaceName("meta");
		assertEquals("Meta_talk", wikiModel.parseTemplates("{{TALKSPACE}}", false));
		assertEquals("", wikiModel.parseTemplates("{{TALKSPACE:}}", false));
	}

	public void testTALKSPACE005() {
		wikiModel.setPageName("Sandbox");
		wikiModel.setNamespaceName("meta_talk");
		assertEquals("Meta_talk", wikiModel.parseTemplates("{{TALKSPACEE}}", false));
		// TODO: talkspace should actually have a space:
		assertEquals("Meta_talk", wikiModel.parseTemplates("{{TALKSPACE}}", false));
	}

	public void testTALKSPACE006() {
		wikiModel.setNamespaceName("portal");
		assertEquals("Portal_talk", wikiModel.parseTemplates("{{TALKSPACE}}", false));
	}

	public void testTALKSPACE007() {
		wikiModel.setNamespaceName("");
		assertEquals("Talk", wikiModel.parseTemplates("{{TALKSPACE}}", false));
		assertEquals("Portal_talk", wikiModel.parseTemplates("{{TALKSPACE:Portal:Main Page}}", false));
	}

	public void testSUBJECTSPACE001() {
		assertEquals("", wikiModel.parseTemplates("{{SUBJECTSPACE}}", false));
		assertEquals("Template", wikiModel.parseTemplates("{{SUBJECTSPACE:Template:Main Page}}", false));
		assertEquals("", wikiModel.parseTemplates("{{SUBJECTSPACE:Bad:Main Page}}", false));
	}

	public void testSUBJECTSPACE002() {
		assertEquals("", wikiModel.parseTemplates("{{SUBJECTSPACE}}", false));
		assertEquals("File", wikiModel.parseTemplates("{{SUBJECTSPACE:Image:Main Page}}", false));
	}

	public void testSUBJECTSPACE003() {
		wikiModel.setNamespaceName("category");
		assertEquals("Category", wikiModel.parseTemplates("{{SUBJECTSPACE}}", false));
		assertEquals("File", wikiModel.parseTemplates("{{SUBJECTSPACE:Image:Main Page}}", false));
	}

	public void testSUBJECTSPACE004() {
		wikiModel.setPageName("Sandbox");
		wikiModel.setNamespaceName("meta");
		assertEquals("Meta", wikiModel.parseTemplates("{{SUBJECTSPACE}}", false));
		assertEquals("", wikiModel.parseTemplates("{{SUBJECTSPACE:}}", false));
	}

	public void testSUBJECTSPACE005() {
		wikiModel.setPageName("Sandbox");
		wikiModel.setNamespaceName("meta_talk");
		assertEquals("Meta", wikiModel.parseTemplates("{{SUBJECTSPACEE}}", false));
		assertEquals("Meta", wikiModel.parseTemplates("{{SUBJECTSPACE}}", false));
	}

	public void testPAGENAME001() {
		wikiModel.setPageName("MyPage");
		assertEquals("MyPage", wikiModel.parseTemplates("{{PAGENAME}}", false));
		assertEquals("", wikiModel.parseTemplates("{{PAGENAME:}}", false));
		assertEquals("Main Page", wikiModel.parseTemplates("{{PAGENAME:Main Page}}", false));
		assertEquals("Main Page", wikiModel.parseTemplates("{{PAGENAME:Main_Page}}", false));
		assertEquals("Main page", wikiModel.parseTemplates("{{PAGENAME:Main page}}", false));
		assertEquals("Main Page", wikiModel.parseTemplates("{{PAGENAME:main Page}}", false));
		assertEquals("Main page", wikiModel.parseTemplates("{{PAGENAME:main page}}", false));
		assertEquals("Main Page", wikiModel.parseTemplates("{{PAGENAME:Template:Main Page}}", false));
		assertEquals("Bad:Main Page", wikiModel.parseTemplates("{{PAGENAME:Bad:Main Page}}", false));
		assertEquals("Test.jpg", wikiModel.parseTemplates("{{PAGENAME:Image:test.jpg}}", false));
	}

	public void testPAGENAMEE001() {
		wikiModel.setPageName("MyPage");
		assertEquals("MyPage", wikiModel.parseTemplates("{{PAGENAMEE}}", false));
		assertEquals("", wikiModel.parseTemplates("{{PAGENAMEE:}}", false));
		assertEquals("Main_Page", wikiModel.parseTemplates("{{PAGENAMEE:Main Page}}", false));
		assertEquals("Main_Page", wikiModel.parseTemplates("{{PAGENAMEE:Main_Page}}", false));
		assertEquals("Main_page", wikiModel.parseTemplates("{{PAGENAMEE:Main page}}", false));
		assertEquals("Main_Page", wikiModel.parseTemplates("{{PAGENAMEE:main Page}}", false));
		assertEquals("Main_page", wikiModel.parseTemplates("{{PAGENAMEE:main page}}", false));
		assertEquals("Main_Page", wikiModel.parseTemplates("{{PAGENAMEE:Template:Main Page}}", false));
		assertEquals("Bad:Main_Page", wikiModel.parseTemplates("{{PAGENAMEE:Bad:Main Page}}", false));
		assertEquals("Test.jpg", wikiModel.parseTemplates("{{PAGENAMEE:Image:test.jpg}}", false));
	}

	public void testFULLPAGENAME001() {
		wikiModel.setPageName("MyPage");
		assertEquals("MyPage", wikiModel.parseTemplates("{{FULLPAGENAME}}", false));
		assertEquals("", wikiModel.parseTemplates("{{FULLPAGENAME:}}", false));
		assertEquals("Main Page", wikiModel.parseTemplates("{{FULLPAGENAME:Main Page}}", false));
		assertEquals("Main Page", wikiModel.parseTemplates("{{FULLPAGENAME:Main_Page}}", false));
		assertEquals("Template:Main Page", wikiModel.parseTemplates("{{FULLPAGENAME:Template:Main Page}}", false));
		assertEquals("Bad:Main Page", wikiModel.parseTemplates("{{FULLPAGENAME:Bad:Main Page}}", false));
		assertEquals("File:Test.jpg", wikiModel.parseTemplates("{{FULLPAGENAME:Image:test.jpg}}", false));
	}

	public void testFULLPAGENAMEE001() {
		wikiModel.setPageName("MyPage");
		assertEquals("MyPage", wikiModel.parseTemplates("{{FULLPAGENAMEE}}", false));
		assertEquals("", wikiModel.parseTemplates("{{FULLPAGENAMEE:}}", false));
		assertEquals("Main_Page", wikiModel.parseTemplates("{{FULLPAGENAMEE:Main Page}}", false));
		assertEquals("Main_Page", wikiModel.parseTemplates("{{FULLPAGENAMEE:Main_Page}}", false));
		assertEquals("Template:Main_Page", wikiModel.parseTemplates("{{FULLPAGENAMEE:Template:Main Page}}", false));
		assertEquals("Bad:Main_Page", wikiModel.parseTemplates("{{FULLPAGENAMEE:Bad:Main Page}}", false));
		assertEquals("File:Test.jpg", wikiModel.parseTemplates("{{FULLPAGENAMEE:Image:test.jpg}}", false));
	}

	public void testTALKPAGENAME001() {
		wikiModel.setPageName("MyPage");
		wikiModel.setNamespaceName("");
		assertEquals("Talk:MyPage", wikiModel.parseTemplates("{{TALKPAGENAME}}", false));
		assertEquals("", wikiModel.parseTemplates("{{TALKPAGENAME:}}", false));
		assertEquals("Talk:Main Page", wikiModel.parseTemplates("{{TALKPAGENAME:Main Page}}", false));
		assertEquals("Template_talk:Main Page", wikiModel.parseTemplates("{{TALKPAGENAME:Template:Main Page}}", false)); // TODO: talk namespace should contain a space
		assertEquals("Talk:Bad:Main Page", wikiModel.parseTemplates("{{TALKPAGENAME:Bad:Main Page}}", false));
		assertEquals("Template_talk:Sandbox", wikiModel.parseTemplates("{{TALKPAGENAME:Template:Sandbox}}", false)); // TODO: talk namespace should contain a space
		assertEquals("Template_talk:Sandbox", wikiModel.parseTemplates("{{TALKPAGENAME:Template_talk:Sandbox}}", false)); // TODO: talk namespace should contain a space
		assertEquals("Template_talk:Sandbox", wikiModel.parseTemplates("{{TALKPAGENAME:Template talk:Sandbox}}", false)); // TODO: talk namespace should contain a space
	}

	public void testTALKPAGENAMEE001() {
		wikiModel.setPageName("MyPage");
		assertEquals("Talk:MyPage", wikiModel.parseTemplates("{{TALKPAGENAMEE}}", false));
		assertEquals("", wikiModel.parseTemplates("{{TALKPAGENAMEE:}}", false));
		assertEquals("Talk:Main_Page", wikiModel.parseTemplates("{{TALKPAGENAMEE:Main Page}}", false));
		assertEquals("Template_talk:Main_Page", wikiModel.parseTemplates("{{TALKPAGENAMEE:Template:Main Page}}", false));
		assertEquals("Talk:Bad:Main_Page", wikiModel.parseTemplates("{{TALKPAGENAMEE:Bad:Main Page}}", false));
		assertEquals("Template_talk:Sandbox", wikiModel.parseTemplates("{{TALKPAGENAMEE:Template:Sandbox}}", false));
		assertEquals("Template_talk:Sandbox", wikiModel.parseTemplates("{{TALKPAGENAMEE:Template_talk:Sandbox}}", false));
		assertEquals("Template_talk:Sandbox", wikiModel.parseTemplates("{{TALKPAGENAMEE:Template talk:Sandbox}}", false));
	}

	public void testSUBJECTPAGENAME001() {
		wikiModel.setPageName("MyPage");
		wikiModel.setNamespaceName("");
		assertEquals("MyPage", wikiModel.parseTemplates("{{SUBJECTPAGENAME}}", false));
		assertEquals("", wikiModel.parseTemplates("{{SUBJECTPAGENAME:}}", false));
		assertEquals("Main Page", wikiModel.parseTemplates("{{SUBJECTPAGENAME:Main Page}}", false));
		assertEquals("Template:Main Page", wikiModel.parseTemplates("{{SUBJECTPAGENAME:Template:Main Page}}", false));
		assertEquals("Bad:Main Page", wikiModel.parseTemplates("{{SUBJECTPAGENAME:Bad:Main Page}}", false));
		assertEquals("Template:Sandbox", wikiModel.parseTemplates("{{SUBJECTPAGENAME:Template:Sandbox}}", false));
		assertEquals("Template:Sandbox", wikiModel.parseTemplates("{{SUBJECTPAGENAME:Template_talk:Sandbox}}", false));
		assertEquals("Template:Sandbox", wikiModel.parseTemplates("{{SUBJECTPAGENAME:Template talk:Sandbox}}", false));
	}

	public void testSUBJECTPAGENAMEE001() {
		wikiModel.setPageName("MyPage");
		assertEquals("MyPage", wikiModel.parseTemplates("{{SUBJECTPAGENAMEE}}", false));
		assertEquals("", wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:}}", false));
		assertEquals("Main_Page", wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Main Page}}", false));
		assertEquals("Template:Main_Page", wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Template:Main Page}}", false));
		assertEquals("Bad:Main_Page", wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Bad:Main Page}}", false));
		assertEquals("Template:Sandbox", wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Template:Sandbox}}", false));
		assertEquals("Template:Sandbox", wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Template_talk:Sandbox}}", false));
		assertEquals("Template:Sandbox", wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Template talk:Sandbox}}", false));
	}

	public void testBASEPAGENAME001() {
		wikiModel.setPageName("MyPage");
		wikiModel.setNamespaceName("");
		assertEquals("MyPage", wikiModel.parseTemplates("{{BASEPAGENAME}}", false));
	}

	public void testBASEPAGENAME002() {
		wikiModel.setPageName("MyPage/foo");
		wikiModel.setNamespaceName("");
		assertEquals("MyPage", wikiModel.parseTemplates("{{BASEPAGENAME}}", false));
	}

	public void testBASEPAGENAME003() {
		wikiModel.setPageName("MyPage/foo/bar");
		wikiModel.setNamespaceName("");
		assertEquals("MyPage/foo", wikiModel.parseTemplates("{{BASEPAGENAME}}", false));
	}

	public void testBASEPAGENAME004() {
		assertEquals("", wikiModel.parseTemplates("{{BASEPAGENAME:}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{BASEPAGENAME:Talk:title}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title/foo}}", false));
		assertEquals("Title/foo", wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title/foo/bar}}", false));
		assertEquals("Title/foo/bar", wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title/foo/bar/baz}}", false));
		assertEquals("Title 2/foo/bar", wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title 2/foo/bar/baz}}", false));
		assertEquals("Bad:Title 2/foo/bar", wikiModel.parseTemplates("{{BASEPAGENAME:Bad:Title 2/foo/bar/baz}}", false));
	}

	public void testBASEPAGENAMEE001() {
		wikiModel.setPageName("MyPage");
		wikiModel.setNamespaceName("");
		assertEquals("MyPage", wikiModel.parseTemplates("{{BASEPAGENAMEE}}", false));
	}

	public void testBASEPAGENAMEE002() {
		wikiModel.setPageName("MyPage/foo");
		wikiModel.setNamespaceName("");
		assertEquals("MyPage", wikiModel.parseTemplates("{{BASEPAGENAMEE}}", false));
	}

	public void testBASEPAGENAMEE003() {
		wikiModel.setPageName("MyPage/foo/bar");
		wikiModel.setNamespaceName("");
		assertEquals("MyPage/foo", wikiModel.parseTemplates("{{BASEPAGENAMEE}}", false));
	}

	public void testBASEPAGENAMEE004() {
		assertEquals("", wikiModel.parseTemplates("{{BASEPAGENAMEE:}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:title}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title/foo}}", false));
		assertEquals("Title/foo", wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title/foo/bar}}", false));
		assertEquals("Title/foo/bar", wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title/foo/bar/baz}}", false));
		assertEquals("Title_2/foo/bar", wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title 2/foo/bar/baz}}", false));
		assertEquals("Bad:Title_2/foo/bar", wikiModel.parseTemplates("{{BASEPAGENAMEE:Bad:Title 2/foo/bar/baz}}", false));
	}

	public void testSUBPAGENAME001() {
		wikiModel.setPageName("MyPage");
		wikiModel.setNamespaceName("");
		assertEquals("MyPage", wikiModel.parseTemplates("{{SUBPAGENAME}}", false));
	}

	public void testSUBPAGENAME002() {
		wikiModel.setPageName("MyPage/foo");
		wikiModel.setNamespaceName("");
		assertEquals("foo", wikiModel.parseTemplates("{{SUBPAGENAME}}", false));
	}

	public void testSUBPAGENAME003() {
		wikiModel.setPageName("MyPage/foo/bar");
		wikiModel.setNamespaceName("");
		assertEquals("bar", wikiModel.parseTemplates("{{SUBPAGENAME}}", false));
	}

	public void testSUBPAGENAME004() {
		assertEquals("", wikiModel.parseTemplates("{{SUBPAGENAME:}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{SUBPAGENAME:Talk:title}}", false));
		assertEquals("foo", wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title/foo}}", false));
		assertEquals("bar", wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title/foo/bar}}", false));
		assertEquals("baz", wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title/foo/bar/baz}}", false));
		assertEquals("baz 2", wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title 2/foo/bar/baz 2}}", false));
		assertEquals("baz 2", wikiModel.parseTemplates("{{SUBPAGENAME:Bad:Title 2/foo/bar/baz 2}}", false));
	}

	public void testSUBPAGENAMEE001() {
		wikiModel.setPageName("MyPage");
		wikiModel.setNamespaceName("");
		assertEquals("MyPage", wikiModel.parseTemplates("{{SUBPAGENAMEE}}", false));
	}

	public void testSUBPAGENAMEE002() {
		wikiModel.setPageName("MyPage/foo");
		wikiModel.setNamespaceName("");
		assertEquals("foo", wikiModel.parseTemplates("{{SUBPAGENAMEE}}", false));
	}

	public void testSUBPAGENAMEE003() {
		wikiModel.setPageName("MyPage/foo/bar");
		wikiModel.setNamespaceName("");
		assertEquals("bar", wikiModel.parseTemplates("{{SUBPAGENAMEE}}", false));
	}

	public void testSUBPAGENAMEE004() {
		assertEquals("", wikiModel.parseTemplates("{{SUBPAGENAMEE:}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title}}", false));
		assertEquals("Title", wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:title}}", false));
		assertEquals("foo", wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title/foo}}", false));
		assertEquals("bar", wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title/foo/bar}}", false));
		assertEquals("baz", wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title/foo/bar/baz}}", false));
		assertEquals("baz_2", wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title 2/foo/bar/baz 2}}", false));
		assertEquals("baz_2", wikiModel.parseTemplates("{{SUBPAGENAMEE:Bad:Title 2/foo/bar/baz 2}}", false));
	}

	public void testURLEncode001() {
		assertEquals("%22%23%24%25%26%27%28%29*%2C%3B%3F%5B%5D%5E%60%7B%7D", wikiModel.parseTemplates(
				"{{urlencode: \"#$%&'()*,;?[]^`{}}}", false));
		assertEquals("%3C", wikiModel.parseTemplates("{{urlencode:<}}", false));
		assertEquals("%3E", wikiModel.parseTemplates("{{urlencode:>}}", false));
		assertEquals("%7C", wikiModel.parseTemplates("{{urlencode:{{!}}}}", false));
	}

	public void testPadleft001() {
		assertEquals("8", wikiModel.parseTemplates("{{padleft:8}}", false));
		assertEquals("008", wikiModel.parseTemplates("{{padleft:8|3}}", false));
		assertEquals("8", wikiModel.parseTemplates("{{padleft:8|a}}", false));
		assertEquals("007", wikiModel.parseTemplates("{{padleft:7|3|0}}", false));
		assertEquals("000", wikiModel.parseTemplates("{{padleft:0|3|0}}", false));
		assertEquals("aaabcd", wikiModel.parseTemplates("{{padleft:bcd|6|a}}", false));
		assertEquals("----cafe", wikiModel.parseTemplates("{{padleft:cafe|8|-}}", false));
		assertEquals("|||bcd", wikiModel.parseTemplates("{{padleft:bcd|6|{{!}}}}", false));
	}

	public void testPadright001() {
		assertEquals("8", wikiModel.parseTemplates("{{padright:8}}", false));
		assertEquals("800", wikiModel.parseTemplates("{{padright:8|3}}", false));
		assertEquals("8", wikiModel.parseTemplates("{{padright:8|a}}", false));
		assertEquals("bcdaaa", wikiModel.parseTemplates("{{padright:bcd|6|a}}", false));
		assertEquals("0aaaaa", wikiModel.parseTemplates("{{padright:0|6|a}}", false));

	}

	public void testTime001() {
		// seconds since January 1970
		String currentSecondsStr = wikiModel.parseTemplates("{{ #time: U }}", false);
		Long currentSeconds = Long.valueOf(currentSecondsStr);
		assertTrue(currentSeconds > 1212598361);
	}

	public void testTag001() {
		assertEquals("<references =\"\"></references>", wikiModel.parseTemplates("{{#tag:references|{{{refs|}}}|group={{{group|}}}}}"));
	}

	public void testPipe001() {
		assertEquals("hello worldhello world ", wikiModel.parseTemplates("{{2x|hello world" + "}} ", false));
	}

	public void testPipe001a() {
		assertEquals("Hello World\n" + "Hello World\n" + "", wikiModel.parseTemplates("{{2x|Hello World\n" + "}}", false));
	}

	public void testPipe002() {
		assertEquals("{| \n" + "| A \n" + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "{| \n" + "| A \n" + "| B\n" + "|- \n"
				+ "| C\n" + "| D\n" + "|}\n", wikiModel.parseTemplates("{{2x|{{{!}} \n" + "{{!}} A \n" + "{{!}} B\n" + "{{!}}- \n"
				+ "{{!}} C\n" + "{{!}} D\n" + "{{!}}}\n" + "}}", false));
	}

//	public void testPipe003() {
//		assertEquals("{| \n" + "| A \n" + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "{| \n" + "| A \n" + "| B\n" + "|- \n"
//				+ "| C\n" + "| D\n" + "|}\n" + "{| \n" + "| A \n" + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "{| \n" + "| A \n"
//				+ "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "", wikiModel.parseTemplates("{{2x|{{2x|{{{!}} \n" + "{{!}} A \n"
//				+ "{{!}} B\n" + "{{!}}- \n" + "{{!}} C\n" + "{{!}} D\n" + "{{!}}}\n" + "}}}}", false));
//	}

	public void testInvalidNoinclude() {
		assertEquals("test123 start\n" + "test123 end", wikiModel.parseTemplates("test123 start<noinclude>\n" + "test123 end"));
	}

	public void testTemplateImage1() {
		// see method WikiTestModel#getRawWikiContent()
		assertEquals(
				"{|\n"
						+ "! | <h2 style=\"background:#cedff2;\">In the news</h2>\n"
						+ "|-\n"
						+ "| style=\"color:#000; padding:2px 5px;\" | <div id=\"mp-itn\">[[File:Yoshihiko Noda-1.jpg|Yoshihiko Noda|alt=Yoshihiko Noda]]\n"
						+ "The ruling Democratic Party of Japan selects '''Yoshihiko Noda''' ''(pictured)'' as the country's new prime minister, following the resignation of Naoto Kan\n"
						+ "</div>\n" + "|}",
				wikiModel
						.parseTemplates("{|\n"
								+ "! | <h2 style=\"background:#cedff2;\">In the news</h2>\n"
								+ "|-\n"
								+ "| style=\"color:#000; padding:2px 5px;\" | <div id=\"mp-itn\">{{Image\n"
								+ " |image  = Yoshihiko Noda-1.jpg\n"
								+ " |title  = Yoshihiko Noda\n"
								+ "}}\n"
								+ "The ruling Democratic Party of Japan selects '''Yoshihiko Noda''' ''(pictured)'' as the country's new prime minister, following the resignation of Naoto Kan\n"
								+ "</div>\n" + "|}"));
	}

	public void testInvalidIncludeonly() {
		assertEquals("test123 start", wikiModel.parseTemplates("test123 start<includeonly>\n" + "test123 end"));
	}

	public void testInvalidOnlyinclude() {
		assertEquals("test123 start\n" + "test123 end", wikiModel.parseTemplates("test123 start<onlyinclude>\n" + "test123 end"));
	}

	public void testMagicCURRENTYEAR() {
		// assertEquals("test 2010 test123",
		// wikiModel.parseTemplates("test {{CURRENTYEAR}} test123"));
	}

	public void testMagicPAGENAME01() {
		assertEquals("test [[PAGENAME]] test123", wikiModel.parseTemplates("test [[{{PAGENAME}}]] test123"));
	}

	public void testMagicPAGENAME02() {
		assertEquals("test [[Sandbox]] test123", wikiModel.parseTemplates("test [[{{PAGENAME:Sandbox}}]] test123"));
	}

	public void testMagicTALKPAGENAME01() {
		assertEquals("test [[Talk:Sandbox]] test123", wikiModel.parseTemplates("test [[{{TALKPAGENAME:Sandbox}}]] test123"));
	}

	public void testMagicTALKPAGENAME02() {
		assertEquals("test [[Help_talk:Sandbox]] test123", wikiModel.parseTemplates("test [[{{TALKPAGENAME:Help:Sandbox}}]] test123"));
	}

	public void testMagicTALKPAGENAME03() {
		assertEquals("test [[Help_talk:Sandbox]] test123", wikiModel.parseTemplates("test [[{{TALKPAGENAME:\nHelp:Sandbox}}]] test123"));
	}

	public void testMagicTALKPAGENAME04() {
		wikiModel.setPageName("MyPage");
		wikiModel.setNamespaceName("");
		assertEquals("test [[Talk:MyPage]] test123", wikiModel.parseTemplates("test [[{{TALKPAGENAME}}]] test123"));
	}

	public void testMagicTALKPAGENAME05() {
		wikiModel.setPageName("MyPage");
		wikiModel.setNamespaceName("category");
		assertEquals("test [[Category_talk:MyPage]] test123", wikiModel.parseTemplates("test [[{{TALKPAGENAME}}]] test123"));
	}

	// public void testRef001() {
	// assertEquals(
	// "",
	// wikiModel.parseTemplates("<ref>{{cite web |url=http://www.pottsmerc.com/articles/2009/04/12/opinion/srv0000005095974.txt |title=Actor Tom Hanks talks about religion |author=Terry Mattingly |work=The Mercury |date=April 12, 2009 |accessdate=October 19, 2010}}</ref>\n\n<references/>"));
	// }
	//
	//	
	// public void testCommonsCategory() {
	// assertEquals(
	// "",
	// wikiModel.parseTemplates("{{Commons category}}"));
	// }

	public void testTemplateSwitch() {
		// issue #32
		assertEquals(
				"1001",
				wikiModel
						.parseTemplates("{{#switch:y|y=1001|d={{#switch:w1001|w0=1|w-0=-8|{{#expr:\n"
								+ "1001*10{{#ifexpr:1001<0|-8|+1}}}}}}|c={{#expr:1001*100{{#ifexpr: 1001>0|-98|+1}}}}|m={{#expr:1001*1000{{#ifexpr:1001>0|-998|+1}}}}}}"));
	}

	public void testTitleparts000() {
		assertEquals("Talk:Foo/bar/baz/quok", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok }}", false));
	}

	public void testTitleparts001() {
		assertEquals("Talk:Foo", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | 1 }}", false));
	}

	public void testTitleparts002() {
		assertEquals("Talk:Foo/bar", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | 2 }}", false));
	}

	public void testTitleparts003() {
		assertEquals("Talk:Foo/bar/baz", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | 3 }}", false));
	}

	public void testTitleparts004() {
		assertEquals("Talk:Foo/bar/baz", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -1 }}", false));
	}

	public void testTitleparts005() {
		assertEquals("Talk:Foo/bar", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -2 }}", false));
	}

	public void testTitleparts006() {
		assertEquals("Talk:Foo", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -3 }}", false));
	}

	public void testTitleparts007() {
		assertEquals("", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -4 }}", false));
	}

	public void testTitleparts008() {
		assertEquals("", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -5 }}", false));
	}

	public void testTitleparts009() {
		assertEquals("bar/baz", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | 2 | 2 }}", false));
	}

	public void testTitleparts010() {
		assertEquals("bar/baz/quok", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | | 2 }}", false));
	}

	public void testTitleparts011() {
		assertEquals("quok", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | | -1 }}", false));
	}

	public void testTitleparts012() {
		assertEquals("bar/baz", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -1 | 2 }}", false));
	}

	public void testTitleparts013() {
		assertEquals("", wikiModel.parseTemplates("{{#titleparts: Talk:Foo | -1 }}", false));
	}

	public void testTitleparts014() {
		assertEquals("Talk:Foo", wikiModel.parseTemplates("{{#titleparts: Talk:Foo | | -1 }}", false));
	}

	public void testTitleparts015() {
		assertEquals("bar", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar | | -1 }}", false));
	}

	public void testTitleparts016() {
		assertEquals("Talk:Foo/bar", wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar | | -2 }}", false));
	}


	public void testIssue77_001() {
		assertEquals(
				"{|\n"
						+ "! | <h2 style=\"background:#cedff2;\">In the news</h2>\n"
						+ "|-\n"
						+ "| | <div><div style=\"float:right;margin-left:0.5em;\">\n"
						+ "[[File:Yoshihiko Noda-1.jpg|100x100px||Yoshihiko Noda|alt=Yoshihiko Noda|link=File:Yoshihiko Noda-1.jpg]]\n"
						+ "</div>\n"
						+ "The ruling Democratic Party of Japan selects '''Yoshihiko Noda''' ''(pictured)'' as the country's new Prime Minister of Japan|prime minister, following the resignation of Naoto Kan.</div>\n"
						+ "|}",
				wikiModel
						.parseTemplates("{|\n"
								+ "! | <h2 style=\"background:#cedff2;\">In the news</h2>\n"
								+ "|-\n"
								+ "| | <div>{{In the news/image\n"
								+ " |image  = Yoshihiko Noda-1.jpg\n"
								+ " |size   = 100x100px\n"
								+ " |title  = Yoshihiko Noda\n"
								+ " |link   = \n"
								+ " |border = no\n"
								+ "}}\n"
								+ "The ruling Democratic Party of Japan selects '''Yoshihiko Noda''' ''(pictured)'' as the country's new Prime Minister of Japan|prime minister, following the resignation of Naoto Kan.</div>\n"
								+ "|}"));
	}

//	public void testIssue77_002() {
//		assertEquals(
//				"{| style=\"width: 100%; height: auto; border: 1px solid #88A; background-color: #ACF; vertical-align: top; margin: 0em 0em 0.5em 0em; border-spacing: 0.6em;\" cellspacing=\"6\"\n"
//						+ "|-\n"
//						+ "\n"
//						+ "| style=\"width: 100%; vertical-align:top; color:#000; border: 3px double #AAA; background-color: #ffffff; padding: 0.5em; margin: 0em;\" colspan=\"2\" |\n"
//						+ "{| style=\"vertical-align: top; margin: 0em; width: 100% !important; width: auto; display: table !important; display: inline; background-color: transparent;\"\n"
//						+ "! colspan=\"2\" style=\"background:#F0F0F0; margin: 0em; height: 1em; font-weight:bold; border:1px solid #AAA; text-align:left; color:#000;\" | <div style=\"float:right;\"></div><h1 style=\"text-align: left; font-size: 1.2em; border: none; margin: 0; padding: 1.5px 0 2px 4px;\">'''Knowledge groups'''</h1></div>\n"
//						+ "|-\n"
//						+ "|\n"
//						+ "TEST1\n"
//						+ "|}\n"
//						+ "|-\n"
//						+ "\n"
//						+ "| style=\"width: 100%; vertical-align:top; color:#000; border: 3px double #AAA; background-color: #ffffff; padding: 0.5em; margin: 0em;\" colspan=\"2\" |\n"
//						+ "{| style=\"vertical-align: top; margin: 0em; width: 100% !important; width: auto; display: table !important; display: inline; background-color: transparent;\"\n"
//						+ "! colspan=\"2\" style=\"background:#F0F0F0; margin: 0em; height: 1em; font-weight:bold; border:1px solid #AAA; text-align:left; color:#000;\" | <div style=\"float:right;\"></div><h1 style=\"text-align: left; font-size: 1.2em; border: none; margin: 0; padding: 1.5px 0 2px 4px;\">'''Sister projects'''</h1></div>\n"
//						+ "|-\n" + "|\n" + "TEST2\n" + "|}\n" + "|}", 
//						wikiModel.parseTemplates(
//								"{{Main Page panel|\n"
//						+ "{{Main Page subpanel|column=both|title=Knowledge groups|1=\n" 
//						+ "TEST1\n" + "}}\n" 
//						+ "|\n"
//						+ "{{Main Page subpanel|column=both|title=Sister projects|1=\n" 
//						+ "TEST2\n" 
//						+ "}}\n" 
//						+ "}}"));
//	}

	public void testIssue77_003a() {
		assertEquals("1test2foo|bar\n", wikiModel.parseTemplates("{{1x1y_opt|test|foo{{!}}bar}}\n"));
	}

	public void testIssue77_003b() {
		assertEquals("1test2foo|bar\n", wikiModel.parseTemplates("{{1x1y_opt|1=test|2=foo{{!}}bar}}\n"));
	}

	public void testIssue81_001() {
		assertEquals(" April 14 ", wikiModel.parseTemplates(" {{{1|April 14}}} "));
	}

// time dependent tests
//	public void testIssue81_002() {
//		assertEquals("104", wikiModel.parseTemplates("{{#time:z|{{{1|April 14}}}}}"));
//	}
// time dependent tests
//	public void testIssue82_001() {
//		assertEquals("105", wikiModel.parseTemplates("{{#expr:{{#time:z|{{{1|April 14}}}}}+1}}"));
//	}

	public void testIssue82_002() {
		assertEquals("105th", wikiModel.parseTemplates("{{ordinal|105}}"));
	}
	
// time dependent tests
//	public void testIssue82_003() {
//		assertEquals("105th", wikiModel.parseTemplates("{{ordinal|{{#expr:{{#time:z|{{{1|April 14}}}}}+1}}}}"));
//	}
// time dependent tests
//	public void testIssue82_004() {
//		assertEquals("105", wikiModel.parseTemplates("{{subst:#expr:{{#time:z|{{{1|April 14}}}}}+1}}"));
//	}

	public void testTemplateMain() {
		assertEquals(
				"<div class=\"rellink<nowiki> </nowiki>relarticle mainarticle\">Main articles: [[Demographics of Pakistan|Demographics of Pakistan]]&#32;and&#32;[[Pakistani people|Pakistani people]]</div>",
				wikiModel.parseTemplates("{{Main|Demographics of Pakistan|Pakistani people}}"));
	}

	public void testTemplateSeeAlso() {
		assertEquals(
				"<div class=\"rellink<nowiki> </nowiki>boilerplate seealso\">See also: [[:Ethnic groups in Pakistan]]&nbsp;and [[:Religion in Pakistan]]</div>",
				wikiModel.parseTemplates("{{See also|Ethnic groups in Pakistan|Religion in Pakistan}}"));
	}

	public void testTemplateNavbox() {
		assertEquals(
				"<table cellspacing=\"0\" class=\"navbox\" style=\"border-spacing:0;;\"><tr><td style=\"padding:2px;\"><table cellspacing=\"0\" class=\"nowraplinks  collapsible autocollapse navbox-inner\" style=\"border-spacing:0;background:transparent;color:inherit;;\"><tr><th scope=\"col\" style=\";\" class=\"navbox-title\" colspan=2><div class=\"noprint plainlinks hlist navbar mini\" style=\"\"><ul><li class=\"nv-view\">[[Template:National Board of Review Award for Best Actor|<span title=\"View this template\" style=\";;background:none transparent;border:none;\">v</span>]]</li><li class=\"nv-talk\">[[Template_talk:National Board of Review Award for Best Actor|<span title=\"Discuss this template\" style=\";;background:none transparent;border:none;\">t</span>]]</li><li class=\"nv-edit\">[http://en.wikipedia.org/w/index.php?title=Template%3ANational+Board+of+Review+Award+for+Best+Actor&amp;action=edit <span title=\"Edit this template\" style=\";;background:none transparent;border:none;\">e</span>]</li></ul></div><div class=\"\" style=\"font-size:110%;\">\n" + 
				"[[National Board of Review Award for Best Actor]]</div></th></tr><tr style=\"height:2px;\"><td></td></tr><tr><td colspan=2 style=\"width:100%;padding:0px;;;\" class=\"navbox-list navbox-odd hlist\n" + 
				"\"><div style=\"padding:0em 0.25em\">\n" + 
				"* [[Ray Milland]] (1945)\n" + 
				"* [[Laurence Olivier]] (1946)\n" + 
				"* [[Michael Redgrave]] (1947)\n" + 
				"* [[Walter Huston]] (1948)\n" + 
				"* [[Ralph Richardson]] (1949)\n" + 
				"</div></td></tr><tr style=\"height:2px\"><td></td></tr><tr><td colspan=2 style=\"width:100%;padding:0px;;;\" class=\"navbox-list navbox-even hlist\n" + 
				"\"><div style=\"padding:0em 0.25em\">\n" + 
				"* [[Alec Guinness]] (1950)\n" + 
				"* [[Richard Basehart]] (1951)\n" + 
				"* [[Ralph Richardson]] (1952)\n" + 
				"* [[James Mason]] (1953)\n" + 
				"* [[Bing Crosby]] (1954)\n" + 
				"* [[Ernest Borgnine]] (1955)\n" + 
				"* [[Yul Brynner]] (1956)\n" + 
				"* [[Alec Guinness]] (1957)\n" + 
				"* [[Spencer Tracy]] (1958)\n" + 
				"* [[Victor Sjöström]] (1959)\n" + 
				"</div></td></tr><tr style=\"height:2px\"><td></td></tr><tr><td colspan=2 style=\"width:100%;padding:0px;;;\" class=\"navbox-list navbox-odd hlist\n" + 
				"\"><div style=\"padding:0em 0.25em\">\n" + 
				"* [[Robert Mitchum]] (1960)\n" + 
				"* [[Albert Finney]] (1961)\n" + 
				"* [[Jason Robards]] (1962)\n" + 
				"* [[Rex Harrison]] (1963)\n" + 
				"* [[Anthony Quinn]] (1964)\n" + 
				"* [[Lee Marvin]] (1965)\n" + 
				"* [[Paul Scofield]] (1966)\n" + 
				"* [[Peter Finch]] (1967)\n" + 
				"* [[Cliff Robertson]] (1968)\n" + 
				"* [[Peter O'Toole]] (1969)\n" + 
				"</div></td></tr><tr style=\"height:2px\"><td></td></tr><tr><td colspan=2 style=\"width:100%;padding:0px;;;\" class=\"navbox-list navbox-even hlist\n" + 
				"\"><div style=\"padding:0em 0.25em\">\n" + 
				"* [[George C. Scott]] (1970)\n" + 
				"* [[Gene Hackman]] (1971)\n" + 
				"* [[Peter O'Toole]] (1972)\n" + 
				"* [[Al Pacino]] / [[Robert Ryan]] (1973)\n" + 
				"* [[Gene Hackman]] (1974)\n" + 
				"* [[Jack Nicholson]] (1975)\n" + 
				"* [[David Carradine]] (1976)\n" + 
				"* [[John Travolta]] (1977)\n" + 
				"* [[Jon Voight]] / [[Laurence Olivier]] (1978)\n" + 
				"* [[Peter Sellers]] (1979)\n" + 
				"</div></td></tr><tr style=\"height:2px\"><td></td></tr><tr><td colspan=2 style=\"width:100%;padding:0px;;;\" class=\"navbox-list navbox-odd hlist\n" + 
				"\"><div style=\"padding:0em 0.25em\">\n" + 
				"* [[Robert De Niro]] (1980)\n" + 
				"* [[Peter Fonda]] (1981)\n" + 
				"* [[Ben Kingsley]] (1982)\n" + 
				"* [[Tom Conti]] (1983)\n" + 
				"* [[Victor Banerjee]] (1984)\n" + 
				"* [[William Hurt]] / [[Raúl Juliá]] (1985)\n" + 
				"* [[Paul Newman]] (1986)\n" + 
				"* [[Michael Douglas]] (1987)\n" + 
				"* [[Gene Hackman]] (1988)\n" + 
				"* [[Morgan Freeman]] (1989)\n" + 
				"</div></td></tr><tr style=\"height:2px\"><td></td></tr><tr><td colspan=2 style=\"width:100%;padding:0px;;;\" class=\"navbox-list navbox-even hlist\n" + 
				"\"><div style=\"padding:0em 0.25em\">\n" + 
				"* [[Robert De Niro]] / [[Robin Williams]] (1990)\n" + 
				"* [[Warren Beatty]] (1991)\n" + 
				"* [[Jack Lemmon]] (1992)\n" + 
				"* [[Anthony Hopkins]] (1993)\n" + 
				"* [[Tom Hanks]] (1994)\n" + 
				"* [[Nicolas Cage]] (1995)\n" + 
				"* [[Tom Cruise]] (1996)\n" + 
				"* [[Jack Nicholson]] (1997)\n" + 
				"* [[Ian McKellen]] (1998)\n" + 
				"* [[Russell Crowe]] (1999)\n" + 
				"</div></td></tr><tr style=\"height:2px\"><td></td></tr><tr><td colspan=2 style=\"width:100%;padding:0px;;;\" class=\"navbox-list navbox-odd hlist\n" + 
				"\"><div style=\"padding:0em 0.25em\">\n" + 
				"* [[Javier Bardem]] (2000)\n" + 
				"* [[Billy Bob Thornton]] (2001)\n" + 
				"* [[Campbell Scott]] (2002)\n" + 
				"* [[Sean Penn]] (2003)\n" + 
				"* [[Jamie Foxx]] (2004)\n" + 
				"* [[Philip Seymour Hoffman]] (2005)\n" + 
				"* [[Forest Whitaker]] (2006)\n" + 
				"* [[George Clooney]] (2007)\n" + 
				"* [[Clint Eastwood]] (2008)\n" + 
				"* [[George Clooney]] / [[Morgan Freeman]] (2009)\n" + 
				"</div></td></tr><tr style=\"height:2px\"><td></td></tr><tr><td colspan=2 style=\"width:100%;padding:0px;;;\" class=\"navbox-list navbox-even hlist\n" + 
				"\"><div style=\"padding:0em 0.25em\">\n" + 
				"* [[Jesse Eisenberg]] (2010)\n" + 
				"* [[George Clooney]] (2011)\n" + 
				"\n" + 
				"</div></td></tr></table></td></tr></table>\n" + 
				"\n" + 
				"[[Category:National Board of Review Awards|*|National Board of Review Award for Best Actor]]\n" + 
				"[[Category:National Board of Review Awards|*]]\n" + 
				"[[Category:Film award templates|PAGENAME]]\n" + 
				"[[fr:Modèle:Palette Critics Choice Awards]]\n" + 
				"[[ja:Template:ナショナル・ボード・オブ・レビュー賞]]\n" + 
				"\n" + 
				"", wikiModel
						.parseTemplates("{{Navbox \n" + "| name       = National Board of Review Award for Best Actor\n"
								+ "| title      = [[National Board of Review Award for Best Actor]]\n" + "| listclass = hlist\n" + "\n"
								+ "|group 1 = 1945-1949\n" + "|list1=\n" + "* [[Ray Milland]] (1945)\n" + "* [[Laurence Olivier]] (1946)\n"
								+ "* [[Michael Redgrave]] (1947)\n" + "* [[Walter Huston]] (1948)\n" + "* [[Ralph Richardson]] (1949)\n" + "\n"
								+ "|group 2 = 1950-1959\n" + "|list2=\n" + "* [[Alec Guinness]] (1950)\n" + "* [[Richard Basehart]] (1951)\n"
								+ "* [[Ralph Richardson]] (1952)\n" + "* [[James Mason]] (1953)\n" + "* [[Bing Crosby]] (1954)\n"
								+ "* [[Ernest Borgnine]] (1955)\n" + "* [[Yul Brynner]] (1956)\n" + "* [[Alec Guinness]] (1957)\n"
								+ "* [[Spencer Tracy]] (1958)\n" + "* [[Victor Sjöström]] (1959)\n" + "\n" + "|group 3 = 1960-1969\n" + "|list3=\n"
								+ "* [[Robert Mitchum]] (1960)\n" + "* [[Albert Finney]] (1961)\n" + "* [[Jason Robards]] (1962)\n"
								+ "* [[Rex Harrison]] (1963)\n" + "* [[Anthony Quinn]] (1964)\n" + "* [[Lee Marvin]] (1965)\n"
								+ "* [[Paul Scofield]] (1966)\n" + "* [[Peter Finch]] (1967)\n" + "* [[Cliff Robertson]] (1968)\n"
								+ "* [[Peter O'Toole]] (1969)\n" + "\n" + "|group 4 = 1970-1979\n" + "|list4=\n" + "* [[George C. Scott]] (1970)\n"
								+ "* [[Gene Hackman]] (1971)\n" + "* [[Peter O'Toole]] (1972)\n" + "* [[Al Pacino]] / [[Robert Ryan]] (1973)\n"
								+ "* [[Gene Hackman]] (1974)\n" + "* [[Jack Nicholson]] (1975)\n" + "* [[David Carradine]] (1976)\n"
								+ "* [[John Travolta]] (1977)\n" + "* [[Jon Voight]] / [[Laurence Olivier]] (1978)\n"
								+ "* [[Peter Sellers]] (1979)\n" + "\n" + "|group 5 = 1980-1989\n" + "|list5=\n" + "* [[Robert De Niro]] (1980)\n"
								+ "* [[Peter Fonda]] (1981)\n" + "* [[Ben Kingsley]] (1982)\n" + "* [[Tom Conti]] (1983)\n"
								+ "* [[Victor Banerjee]] (1984)\n" + "* [[William Hurt]] / [[Raúl Juliá]] (1985)\n" + "* [[Paul Newman]] (1986)\n"
								+ "* [[Michael Douglas]] (1987)\n" + "* [[Gene Hackman]] (1988)\n" + "* [[Morgan Freeman]] (1989)\n" + "\n"
								+ "|group 6 = 1990-1999\n" + "|list6=\n" + "* [[Robert De Niro]] / [[Robin Williams]] (1990)\n"
								+ "* [[Warren Beatty]] (1991)\n" + "* [[Jack Lemmon]] (1992)\n" + "* [[Anthony Hopkins]] (1993)\n"
								+ "* [[Tom Hanks]] (1994)\n" + "* [[Nicolas Cage]] (1995)\n" + "* [[Tom Cruise]] (1996)\n"
								+ "* [[Jack Nicholson]] (1997)\n" + "* [[Ian McKellen]] (1998)\n" + "* [[Russell Crowe]] (1999)\n" + "\n"
								+ "|group 7 = 2000-2009\n" + "|list7= \n" + "* [[Javier Bardem]] (2000)\n" + "* [[Billy Bob Thornton]] (2001)\n"
								+ "* [[Campbell Scott]] (2002)\n" + "* [[Sean Penn]] (2003)\n" + "* [[Jamie Foxx]] (2004)\n"
								+ "* [[Philip Seymour Hoffman]] (2005)\n" + "* [[Forest Whitaker]] (2006)\n" + "* [[George Clooney]] (2007)\n"
								+ "* [[Clint Eastwood]] (2008)\n" + "* [[George Clooney]] / [[Morgan Freeman]] (2009)\n" + "\n"
								+ "|group 8 = 2010-present\n" + "|list8=\n" + "* [[Jesse Eisenberg]] (2010)\n" + "* [[George Clooney]] (2011)\n"
								+ "\n" + "\n" + "}}<noinclude>\n" + "\n"
								+ "[[Category:National Board of Review Awards|*|National Board of Review Award for Best Actor]]\n"
								+ "[[Category:National Board of Review Awards|*]]\n" + "[[Category:Film award templates|{{PAGENAME}}]]\n"
								+ "[[fr:Modèle:Palette Critics Choice Awards]]\n" + "[[ja:Template:ナショナル・ボード・オブ・レビュー賞]]\n" + "</noinclude>\n" + ""));

	}

	public void testTemplateNavbar() {
		assertEquals(
				"<div class=\"noprint plainlinks hlist navbar \" style=\"\"><span style=\"word-spacing:0;\">This box: </span><ul><li class=\"nv-view\">[[Template:Screen Actors Guild Award for Outstanding Performance by a Cast in a Motion Picture (1995–2000)|<span title=\"View this template\" style=\"\">view</span>]]</li><li class=\"nv-talk\">[[Template_talk:Screen Actors Guild Award for Outstanding Performance by a Cast in a Motion Picture (1995–2000)|<span title=\"Discuss this template\" style=\"\">talk</span>]]</li><li class=\"nv-edit\">[http://en.wikipedia.org/w/index.php?title=Template%3AScreen+Actors+Guild+Award+for+Outstanding+Performance+by+a+Cast+in+a+Motion+Picture+%281995%E2%80%932000%29&amp;action=edit <span title=\"Edit this template\" style=\"\">edit</span>]</li></ul></div>\n"
						+ "",
				wikiModel
						.parseTemplates("{{Navbar|Screen Actors Guild Award for Outstanding Performance by a Cast in a Motion Picture (1995–2000)}}\n"));
	}

	public void testWeather01() {
		assertEquals("1\n", wikiModel.parseTemplates("{{WeatherVal01\n" + "|show=1\n" + "|jan1=10\n" + "}}\n"));
	}

	public void testWeather02() {
		assertEquals("10\n", wikiModel.parseTemplates("{{WeatherVal02\n" + "|show=1\n" + "|jan1=10\n" + "}}\n"));
	}

	public void testWeather03() {
		assertEquals("10\n", wikiModel.parseTemplates("{{WeatherVal03\n" + "|show=1\n" + "|jan1=10\n" + "}}\n"));
	}

	public void testWeather04() {
		assertEquals("10\n", wikiModel.parseTemplates("{{WeatherVal03\r\n" + "|show=1\r\n" + "|jan1=10\r\n" + "}}\n"));
	}

	public void testWeather05() {
		assertEquals("10\n" + "", wikiModel.parseTemplates("{{WeatherVal03\n\r" + "|show=1\n\r" + "|jan1=10\n\r" + "}}\n"));
	}

	public void testTemplateLastTilde01() {
		assertEquals("[[:Template:TestTemplateName01~]]" + "", wikiModel.parseTemplates("{{TestTemplateName01~}}"));
	}

	public void testShouldNotBeParsed01() {
		wikiModel.parseTemplates("{{#switch:TestPage\n|OtherPage={{ShouldNotBeParsed}}\n|#default={{ShouldBeParsed}}\n}}");
		assertTrue(wikiModel.getTemplates().contains("ShouldBeParsed"));
		assertTrue(!wikiModel.getTemplates().contains("ShouldNotBeParsed"));
	}

	public void testShouldNotBeParsed02() {
		wikiModel
				.parseTemplates("{{#switch:TestPage\n|TestPage={{ShouldBeParsed}}\n|OtherPage={{ShouldNotBeParsed1}}\n|#default={{ShouldNotBeParsed2}}\n}}");
		assertTrue(wikiModel.getTemplates().contains("ShouldBeParsed"));
		assertTrue(!wikiModel.getTemplates().contains("ShouldNotBeParsed1"));
		assertTrue(!wikiModel.getTemplates().contains("ShouldNotBeParsed2"));
	}

	public void testShouldNotBeParsed03() {
		wikiModel
				.parseTemplates("{{#switch:TestPage\n|OtherPage={{ShouldNotBeParsed1}}\n|TestPage={{ShouldBeParsed}}\n|#default={{ShouldNotBeParsed2}}\n}}");
		assertTrue(wikiModel.getTemplates().contains("ShouldBeParsed"));
		assertTrue(!wikiModel.getTemplates().contains("ShouldNotBeParsed1"));
		assertTrue(!wikiModel.getTemplates().contains("ShouldNotBeParsed2"));
	}

	public void testShouldNotBeParsed04() {
		wikiModel
				.parseTemplates("{{#switch:TestPage\n|#default={{ShouldNotBeParsed2}}\n|OtherPage={{ShouldNotBeParsed1}}\n|TestPage={{ShouldBeParsed}}\n}}");
		assertTrue(wikiModel.getTemplates().contains("ShouldBeParsed"));
		assertTrue(!wikiModel.getTemplates().contains("ShouldNotBeParsed1"));
		assertTrue(!wikiModel.getTemplates().contains("ShouldNotBeParsed2"));
	}

	public void testShouldNotBeParsed05() {
		wikiModel.parseTemplates("{{#switch:TestPage\n|TestPage={{ShouldBeParsed}}\n|#default={{ShouldNotBeParsed}}\n}}");
		assertTrue(wikiModel.getTemplates().contains("ShouldBeParsed"));
		assertTrue(!wikiModel.getTemplates().contains("ShouldNotBeParsed"));
	}

	public void testMissingImplicitParameter01() {
		assertEquals("", wikiModel.parseTemplates("{{1x||2}}"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces01() {
		assertEquals(" a ", wikiModel.parseTemplates("{{1x| a }}"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces02() {
		assertEquals("a", wikiModel.parseTemplates("{{1x|1= a }}"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces03() {
		assertEquals("p\n\nq", wikiModel.parseTemplates("p{{2x|{{nln}}}}q"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces04() {
		assertEquals("pq", wikiModel.parseTemplates("p{{2x|1={{nln}}}}q"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces05() {
		assertEquals("pq", wikiModel.parseTemplates("p{{#if:x|{{2x|{{nln}}}}}}q"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces06() {
		assertEquals("pq", wikiModel.parseTemplates("p{{2x|{{#if:x|{{nln}}}}}}q"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces07() {
		assertEquals("p  q", wikiModel.parseTemplates("p{{2x|{{spc}}}}q"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces08() {
		assertEquals("pq", wikiModel.parseTemplates("p{{2x|1={{spc}}}}q"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces09() {
		assertEquals("pq", wikiModel.parseTemplates("p{{#if:x|{{2x|{{spc}}}}}}q"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces10() {
		assertEquals("pq", wikiModel.parseTemplates("p{{2x|{{#if:x|{{spc}}}}}}q"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces11() {
		assertEquals("pqpq", wikiModel.parseTemplates("{{2x\n|\n1 =\npq\n}}"));
	}

	/**
	 * Some tests from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
	 * > Help:Newlines and spaces - Trimming on expansion</a>
	 */
	public void testNewlineSpaces12() {
		assertEquals("rs", wikiModel.parseTemplates("{{#switch:\n2\n|\n1 =\npq\n|\n2 =\nrs\n|\n3 =\ntu\n}}"));
	}

	public void testCategory001() {
		assertEquals("", wikiModel.render("[[Category:Main Page]]", false));
		HashMap<String, String> expectedCategories = new HashMap<String, String>();
		expectedCategories.put("Main Page", "Category:Main Page");
		assertEquals(expectedCategories, wikiModel.getCategories());
	}
}
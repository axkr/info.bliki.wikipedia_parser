package info.bliki.wiki.filter;

import info.bliki.wiki.model.WikiModel;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateParserTest extends FilterTestSupport {

    private static final String TEST_STRING_03 = "{{{1|{{PAGENAME}}}}}";

    @Test public void testWeather07() {
        assertThat(wikiModel.parseTemplates("{{WeatherBox03}}\n")).isEqualTo("20\n");
    }

    @Test public void testWeather06() {
        assertThat(wikiModel.parseTemplates("{{WeatherBox03|show1=1}}\n")).isEqualTo("10\n");
    }

    /**
     * Issue 86
     */
    @Test public void testSortnameDemo001() {
        assertThat(wikiModel.parseTemplates("{{sortname|The|Man with One Red Shoe}}")).isEqualTo("<span style=\"display:none;\">Man with One Red Shoe, The</span><span class=\"vcard\"><span class=\"fn\">[[The Man with One Red Shoe|The Man with One Red Shoe]]</span></span>[[Category:Articles with hCards]]");
    }

    /**
     * Issue 86
     */
    @Test public void testNoincludeDemo001() {
        assertThat(wikiModel.parseTemplates("test1<noinclude> noincludetext</noinclude>\n"
                + "\n" + "<includeonly>includeonlytext<noinclude> noincludetext</noinclude></includeonly>")).isEqualTo("test1 noincludetext\n" + "\n" + "");
    }

    /**
     * Issue 86
     */
    @Test public void testNoincludeDemo002() {
        assertThat(wikiModel.render("test1<noinclude> noincludetext</noinclude>\n" + "\n"
                + "<includeonly>includeonlytext<noinclude> noincludetext</noinclude></includeonly>", true)).isEqualTo("\n" + "<p>test1 noincludetext</p>\n" + "");
    }

    /**
     * Issue 86
     */
    @Test public void testOnlyicludeDemo001() {
        assertThat(wikiModel
                .parseTemplates(WikiTestModel.ONLYINCLUDE_DEMO)).isEqualTo("abcdefghi<hr>\n" + ";Only active template content is above.\n" + "\n"
                + ";The verbatim active code within reads:\n"
                + " abc'''&lt;onlyinclude>'''def'''&lt;/onlyinclude>'''ghi'''&lt;includeonly>'''jkl'''&lt;/includeonly>'''\n" + "\n"
                + "If transposed, the only part included will be the string literal <code>def</code>. \n" + "\n" + "==Example==\n"
                + "Including [[:Help:Template/onlyinclude demo]] yields only:\n" + " [[:Help:Template/onlyinclude demo]]\n" + "\n" + "\n"
                + "\n" + "[[Category:Handbook templates]]\n" + "[[Category:Template documentation|PAGENAME]]\n" + "\n" + "");
    }

    /**
     * Issue 86
     */
    @Test public void testOnlyicludeDemo002() {
        assertThat(wikiModel.parseTemplates("{{OnlyicludeDemo}}")).isEqualTo("def");
    }

    /**
     * Issue 86
     */
    @Test public void testOnlyicludeDemo003() {
        assertThat(wikiModel.render(WikiTestModel.ONLYINCLUDE_DEMO, true)).isEqualTo("\n"
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
                + "</pre>\n" + "\n" + "\n" + "<p>\n" + "</p>\n" + "");
    }

    /**
     * Issue 86
     */
    @Test public void testOnlyicludeDemo004() {
        assertThat(wikiModel.render("{{OnlyicludeDemo}}", false)).isEqualTo("\n" + "<p>def</p>");
    }

    /**
     * Issue 86
     */
    @Test public void testInclude() {
        assertThat(wikiModel.parseTemplates("{{TestInclude}}")).isEqualTo("{| class=\"wikitable float-right\" style=\"width:30%; min-width:250px; max-width:400px; font-size:90%; margin-top:0px;\"\n"
                + "|--\n" + "! colspan=\"2\" style=\"background-color:Khaki; font-size:110%;\" | [[Asteroid]]<br/>{{{Name}}}\n"
                + "|--\n" + "|}");
    }

    /**
     * Issue 86
     */
    @Test public void testInclude2() {
        assertThat(wikiModel.parseTemplates("{{TestInclude2}}")).isEqualTo("{| class=\"wikitable float-right\" style=\"width:30%; min-width:250px; max-width:400px; font-size:90%; margin-top:0px;\"\n"
                + "|--\n" + "! colspan=\"2\" style=\"background-color:Khaki; font-size:110%;\" | [[Asteroid]]<br/>{{{Name}}}\n"
                + "|--\n" + "|}");
    }

    /**
     * Issue 86
     */
    @Test public void testInclude3() {
        assertThat(wikiModel.parseTemplates("{{TestInclude3}}")).isEqualTo("text");
    }

    /**
     * Issue 86
     */
    @Test public void testInclude4() {
        assertThat(wikiModel.parseTemplates("{{TestInclude4}}")).isEqualTo("Text ");
    }

    @Test public void test00() {
        assertThat(wikiModel.parseTemplates("{{Infobox Ort in Deutschland\n"
                + "|Art               = Stadt\n" + "|Wappen            = Wappen_Grafenwöhr.png\n"
                + "|lat_deg           = 49 |lat_min = 43\n" + "|lon_deg           = 11 |lon_min = 54\n" + "|Lageplan          = \n"
                + "|Bundesland        = Bayern\n" + "}}")).isEqualTo("{| class=\"float-right\" style=\"width:290px; font-size:90%; background:#FAFAFA;  border:1px solid #bbb; margin:0px 0px 1em 1em; border-collapse:collapse;\" summary=\"Infobox\"\n"
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
                + "|- class=\"hintergrundfarbe2\"\n" + "|}");
    }

    @Test public void test000() {
        assertThat(wikiModel
                .parseTemplates("{{Lageplan\n" + "|marker     = reddot.svg\n" + "|markersize = 5\n"
                        + "|markertext = {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }}\n"
                        + "|pos_y      = {{#expr: (55.1 - {{{lat_deg|52.5}}} - {{{lat_min|0}}} / 60) * 100 / 7.9}}\n"
                        + "|pos_x      = {{#expr: ({{{lon_deg|13.4}}} + {{{lon_min|0}}} / 60) * 10 - 55}}\n"
                        + "|map        = Karte Deutschland.svg\n" + "|mapsize_x  = 140\n" + "|mapsize_y  = 175\n"
                        + "|maptext    = Deitschlandkartn, Position vo {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }} heavoghom\n"
                        + "|warning    = [[Bild:Missing Map of Germany.png|140px|Koordinaten san außerhoib vom darstellbarn Bereich]]\n"
                        + "}}")).isEqualTo("{| border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\"\n"
                + "|<div style=\"position: relative;\"><div style=\"font-size: 5px; position: absolute; display: block; left:108px; top:55px; padding:0;\">[[Bild:reddot.svg|5px|PAGENAME]]</div>[[Bild:Karte Deutschland.svg|140x175px|Deitschlandkartn, Position vo PAGENAME heavoghom]]</div>\n"
                + "|}");
    }

    @Test public void test001() {
        assertThat(wikiModel.render(
                "{{Lageplan\n" + "|marker     = reddot.svg\n" + "|markersize = 5\n"
                        + "|markertext = {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }}\n"
                        + "|pos_y      = {{#expr: (55.1 - {{{lat_deg|52.5}}} - {{{lat_min|0}}} / 60) * 100 / 7.9}}\n"
                        + "|pos_x      = {{#expr: ({{{lon_deg|13.4}}} + {{{lon_min|0}}} / 60) * 10 - 55}}\n"
                        + "|map        = Karte Deutschland.svg\n" + "|mapsize_x  = 140\n" + "|mapsize_y  = 175\n"
                        + "|maptext    = Deitschlandkartn, Position vo {{#if: {{{Name|}}} | {{{Name}}} | {{PAGENAME}} }} heavoghom\n"
                        + "|warning    = [[Bild:Missing Map of Germany.png|140px|Koordinaten san außerhoib vom darstellbarn Bereich]]\n"
                        + "}}", false
        )).isEqualTo("\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\">\n"
                + "<tr>\n"
                + "<td>\n"
                + "<div style=\"position: relative;\">\n"
                + "<div style=\"font-size: 5px; position: absolute; display: block; left:108px; top:55px; padding:0;\"><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:5px-reddot.svg.png\" title=\"PAGENAME\"><img src=\"http://www.bliki.info/wiki/5px-reddot.svg.png\" alt=\"PAGENAME\" width=\"5\" />\n"
                + "</a></div><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:140px-Karte_Deutschland.svg.png\" title=\"Deitschlandkartn, Position vo PAGENAME heavoghom\"><img src=\"http://www.bliki.info/wiki/140px-Karte_Deutschland.svg.png\" alt=\"Deitschlandkartn, Position vo PAGENAME heavoghom\" height=\"175\" width=\"140\" />\n"
                + "</a></div></td></tr></table></div>");
    }

    @Test public void test002() {
        assertThat(wikiModel.parseTemplates("{{Infobox Ort in Deutschland}}")).isEqualTo("{| class=\"float-right\" style=\"width:290px; font-size:90%; background:#FAFAFA;  border:1px solid #bbb; margin:0px 0px 1em 1em; border-collapse:collapse;\" summary=\"Infobox\"\n"
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
                + "|- class=\"hintergrundfarbe2\"\n" + "|}");
    }

    @Test public void test003() {
        assertThat(wikiModel.render(
                "{{Infobox Ort in Deutschland}}", false)).isEqualTo("\n"
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
                + "<td><a href=\"http://www.bliki.info/wiki/\" /></td></tr></table></div>");
    }

    @Test public void test004() {
        assertThat(wikiModel.parseTemplates("{{Infobox Ort in Deutschland\n"
                + "|Art               = Stadt\n" + "|Wappen            = Wappen_Grafenwöhr.png\n"
                + "|lat_deg           = 49 |lat_min = 43\n" + "|lon_deg           = 11 |lon_min = 54\n" + "|Lageplan          = \n"
                + "|Bundesland        = Bayern\n" + "}}")).isEqualTo("{| class=\"float-right\" style=\"width:290px; font-size:90%; background:#FAFAFA;  border:1px solid #bbb; margin:0px 0px 1em 1em; border-collapse:collapse;\" summary=\"Infobox\"\n"
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
                + "|- class=\"hintergrundfarbe2\"\n" + "|}");
    }

    @Test public void test005() {
        assertThat(wikiModel
                .render("{{Infobox Ort in Deutschland\n" + "|Art               = Stadt\n"
                                + "|Wappen            = Wappen_Grafenwöhr.png\n" + "|lat_deg           = 49 |lat_min = 43\n"
                                + "|lon_deg           = 11 |lon_min = 54\n" + "|Lageplan          = \n" + "|Bundesland        = Bayern\n" + "}}",
                        false
                )).isEqualTo("\n"
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
                + "<td><a href=\"http://www.bliki.info/wiki/Bayern\" title=\"Bayern\">Bayern</a></td></tr></table></div>");
    }

    @Test public void test006() {
        assertThat(wikiModel
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
                                + "}}\n" + "| [[Bild:Karte Deutschland.png|140px|Koordinatn san net da]] }}\n" + "}}", false
                )).isEqualTo("\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\">\n"
                + "<tr>\n"
                + "<td>\n"
                + "<div style=\"position: relative;\">\n"
                + "<div style=\"font-size: 5px; position: absolute; display: block; left:108px; top:55px; padding:0;\"><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:5px-reddot.svg.png\" title=\"PAGENAME\"><img src=\"http://www.bliki.info/wiki/5px-reddot.svg.png\" alt=\"PAGENAME\" width=\"5\" />\n"
                + "</a></div><a class=\"image\" href=\"http://www.bliki.info/wiki/Bild:140px-Karte_Deutschland.svg.png\" title=\"Deitschlandkartn, Position vo PAGENAME heavoghom\"><img src=\"http://www.bliki.info/wiki/140px-Karte_Deutschland.svg.png\" alt=\"Deitschlandkartn, Position vo PAGENAME heavoghom\" height=\"175\" width=\"140\" />\n"
                + "</a></div></td></tr></table></div>");
    }

    @Test public void test010() {
        assertThat(wikiModel
                .parseTemplates("start\n{{#if: {{{1|test}}} | {{{!}}border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\"\n"
                        + "{{!}}<div style=\"position: relative;\"><div style=\"font-size:16px\">middle</div></div> \n{{!}}} }}\nend")).isEqualTo("start\n"
                + "{|border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"margin:0 0 0 0; border-style:none; border-width:0px; border-collapse:collapse; empty-cells:show\"\n"
                + "|<div style=\"position: relative;\"><div style=\"font-size:16px\">middle</div></div> \n" + "|}\n" + "end");
    }

    @Test public void testIf00() {
        assertThat(wikiModel
                .parseTemplates("start{{{{ #if:  | l | u }}cfirst:  {{ es-verb form of/{{ #switch: indicative  | ind | indicative = indicative  | subj | subjunctive = subjunctive  | imp | imperative = imperative  | cond | conditional = conditional  | par | part | participle | past participle  | past-participle = participle  | adv | adverbial | ger | gerund | gerundive  | gerundio | present participle  | present-participle = adverbial  | error  }}  | tense =  {{ #switch: present  | pres | present = present  | imp | imperfect = imperfect  | pret | preterit | preterite = preterite  | fut | future = future  | cond | conditional = conditional  }}  | number =  {{ #switch: singular  | s | sg | sing | singular = singular  | p | pl | plural = plural  }}  | person =  {{ #switch: 1  | 1 | first | first person | first-person = first  | 2 | second|second person | second-person = second  | 3 | third | third person | third-person = third  | 0 | - | imp | impersonal = impersonal  }}  | formal =  {{ #switch: {{{formal}}}  | y | yes = yes  | n | no = no  }}  | gender =  {{ #switch:   | m | masc | masculine = masculine  | f | fem | feminine = feminine  }}  | sense =  {{ #switch: {{{sense}}}  | + | aff | affirmative = affirmative  | - | neg | negative = negative  }}  | sera = {{ #switch: {{{sera}}} | se = se | ra = ra }}  | ending =  {{ #switch: ar  | ar | -ar = -ar  | er | -er = -er  | ir | -ir = -ir  }}  | participle =   | voseo = {{ #if:  | yes | no }}  }}}}end")).isEqualTo("start[[:Template:es-verb form of/indicative]]end");
    }

    @Test public void testIf01() {
        assertThat(wikiModel.parseTemplates("start{{#if:\n" + "\n" + "\n" + "| yes | no}}end")).isEqualTo("startnoend");
    }

    @Test public void testIf02() {
        assertThat(wikiModel.parseTemplates("start{{      #if:   \n    |{{#ifeq:{{{seperator}}}|;|;|. }}     }}end")).isEqualTo("startend");
    }

    @Test public void testIf03() {
        assertThat(wikiModel.parseTemplates("start{{#if:string\n" + "\n" + "\n" + "| yes | no}}end")).isEqualTo("startyesend");
    }

    @Test public void testIf04() {
        assertThat(wikiModel.parseTemplates("start{{#if: | yes }}end")).isEqualTo("startend");
    }

    @Test public void testIfeq01() {
        assertThat(wikiModel.parseTemplates("start{{#ifeq: 01 | 1 | yes | no}}end")).isEqualTo("startyesend");
    }

    @Test public void testIfeq02() {
        assertThat(wikiModel.parseTemplates("start{{#ifeq: 0 | -0 | yes | no}}end")).isEqualTo("startyesend");
    }

    @Test public void testIfeq03() {
        assertThat(wikiModel.parseTemplates("start{{#ifeq: \"01\" | \"1\" | yes | no}}end")).isEqualTo("startnoend");
    }

    @Test public void testIf05() {
        assertThat(wikiModel.parseTemplates("start{{#if: foo | | no}}end")).isEqualTo("startend");
    }

    /**
     * See issue 102
     */
    @Test public void testIf06() {
        assertThat(wikiModel.parseTemplates("== SpaceInIfTest1 ==\n"
                + "{{#if:{{NAMESPACE}}\n" + "| <!--Not article space, do nothing-->Not article space\n"
                + "| <!--Article space-->Article space\n" + "}}")).isEqualTo("== SpaceInIfTest1 ==\n" + "Article space");

        assertThat(wikiModel.parseTemplates("== SpaceInIfTest2 ==\n"
                + "{{#if:{{NAMESPACE}}\n" + "| <!--Not article space, do nothing-->\n" + "  Not article space\n"
                + "| <!--Article space-->\n" + "  Article space\n" + "}}\n" + "")).isEqualTo("== SpaceInIfTest2 ==\n" + "Article space\n" + "");
    }

    @Test public void testIfexpr01() {
        assertThat(wikiModel.parseTemplates("start{{#ifexpr: 1 < 0 | | no}}end")).isEqualTo("startnoend");
    }

    @Test public void testIfexpr02() {
        assertThat(wikiModel.parseTemplates("start{{#ifexpr: 1 > 0 | | no}}end")).isEqualTo("startend");
    }

    @Test public void testIfexpr03() {
        assertThat(wikiModel
                .parseTemplates("start{{#ifexpr: 1 + |no| }}end")).isEqualTo("start<div class=\"error\">Expression error: Error in factor at character: ' ' (0)</div>end");
    }

    @Test public void testBORN_DATA() {
        assertThat(wikiModel
                .parseTemplates("test {{Born_data | birthname = Thomas Jeffrey Hanks | birthplace = [[Concord, California]],  [[United States|U.S.]] }} test123")).isEqualTo("test Thomas Jeffrey Hanks<br />[[Concord, California]],  [[United States|U.S.]] test123");
    }

    @Test public void testMONTHNUMBER() {
        assertThat(wikiModel.parseTemplates("test {{MONTHNUMBER | 10 }} test123")).isEqualTo("test 10 test123");
    }

    @Test public void testMONTHNAME() {
        assertThat(wikiModel.parseTemplates("test {{MONTHNAME | 10 }} test123")).isEqualTo("test October test123");
    }

    @Test public void testAnarchismSidebar() {
        assertThat(wikiModel.parseTemplates("{{Anarchism sidebar}}", false)).isEqualTo("[[:Template:Sidebar]]");
    }

    @Test public void testNonExistentTemplate() {
        assertThat(wikiModel.parseTemplates(
                "==Other areas of Wikipedia==\n" + "{{WikipediaOther}}<!--Template:WikipediaOther-->", false)).isEqualTo("==Other areas of Wikipedia==\n" + "[[:Template:WikipediaOther]]");
    }

    @Test public void testTemplateCall1() {
        // see method WikiTestModel#getRawWikiContent()
        assertThat(wikiModel.parseTemplates("start-{{:Include Page}}-end", false)).isEqualTo("start-an include page-end");
    }

    @Test public void testTemplateCall2() {
        // see method WikiTestModel#getRawWikiContent()
        assertThat(wikiModel.parseTemplates(
                "start-{{templ1|a=3|b}}-end start-{{templ2|sdfsf|klj}}-end", false)).isEqualTo("start-b) First: 3 Second: b-end start-c) First: sdfsf Second: klj-end");
    }

    @Test public void testTemplateCall3() {
        // see method WikiTestModel#getRawWikiContent()
        assertThat(wikiModel.parseTemplates("{{templ1\n"
                + " | a = Test1\n" + " |{{templ2|sdfsf|klj}} \n" + "}}\n" + "", false)).isEqualTo("b) First: Test1 Second: c) First: sdfsf Second: klj \n" + "\n" + "");
    }

    @Test public void testTemplateCall4() {
        // see method WikiTestModel#getRawWikiContent()
        assertThat(wikiModel.parseTemplates("{{tl|example}}", false)).isEqualTo("[[:Template:[[Template:example|example]]]]");
    }

    @Test public void testTemplateCall5() {
        // see method WikiTestModel#getRawWikiContent()
        assertThat(wikiModel.parseTemplates("({{pron-en|dəˌpeʃˈmoʊd}})", false)).isEqualTo("(pronounced <span title=\"Pronunciation in the International Phonetic Alphabet (IPA)\" class=\"IPA\">[[WP:IPA for English|/dəˌpeʃˈmoʊd/]]</span>)");
    }

    @Test public void testTemplateParameter01() {
        // see method WikiTestModel#getTemplateContent()
        assertThat(wikiModel.parseTemplates("start-{{Test|arg1|arg2}}-end", false)).isEqualTo("start-a) First: arg1 Second: arg2-end");
    }

    @Test public void testTemplateParameter02() {
        // see method WikiTestModel#getTemplateContent()
        assertThat(wikiModel
                .parseTemplates(
                        "start- {{cite web|url=http://www.etymonline.com/index.php?search=hello&searchmode=none|title=Online Etymology Dictionary}} -end",
                        false)).isEqualTo("start- ''[http://www.etymonline.com/index.php?search=hello&searchmode=none Online Etymology Dictionary]''. -end");
    }

    @Test public void testTemplateParameter03() {
        // see method WikiTestModel#getTemplateContent()
        assertThat(wikiModel.parseTemplates("start- {{reflist|2}} -end", false)).isEqualTo("start- <div class=\"references-small\" style=\"-moz-column-count:2; -webkit-column-count:2; column-count:2;\">\n"
                + "<references /></div> -end");
    }

    @Test public void testTemplateParameter04() {
        // see method WikiTestModel#getTemplateContent()
        assertThat(wikiModel.parseTemplates(
                "start-<nowiki>{{Test|arg1|arg2}}-</noWiKi>end", false)).isEqualTo("start-<nowiki>{{Test|arg1|arg2}}-</noWiKi>end");
    }

    @Test public void testTemplateParameter05() {
        // see method WikiTestModel#getTemplateContent()
        assertThat(wikiModel.parseTemplates("start- <!-- {{Test|arg1|arg2}} \n --->end", false)).isEqualTo("start- end");
    }

    //
    @Test public void testTemplate06() {
        assertThat(wikiModel.parseTemplates("{{#ifeq: A | B | A equals B | A is not equal B}}", false)).isEqualTo("A is not equal B");
    }

    @Test public void testTemplate07() {
        assertThat(wikiModel.parseTemplates("start- {{ifeq|A|B}} \n end", false)).isEqualTo("start- A is not equal B \n" + " end");
    }

    @Test public void testNestedTemplate() {
        assertThat(wikiModel.parseTemplates("{{nested tempplate test}}", false)).isEqualTo("test a a nested template text template");
    }

    @Test public void testEndlessRecursion() {
        assertThat(wikiModel.parseTemplates("{{recursion}}", false)).isEqualTo("<span class=\"error\">Template loop detected: <strong class=\"selflink\">Template:recursion</strong></span>");
    }

    private static final String TEST_STRING_01 = "[[Category:Interwiki templates|wikipedia]]\n" + "[[zh:Template:Wikipedia]]\n"
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

    @Test public void testNestedIf01() {
        // String temp = StringEscapeUtils.unescapeHtml(TEST_STRING_01);
        String temp = TEST_STRING_01;
        assertThat(wikiModel.parseTemplates(temp, false)).isEqualTo("[[Category:Interwiki templates|wikipedia]]\n" + "[[zh:Template:Wikipedia]]\n"
                + "</noinclude><div class=\"sister-\n" + "wikipedia\"><div class=\"sister-project\"><div\n"
                + "class=\"noprint\" style=\"clear: right; border: solid #aaa\n"
                + "1px; margin: 0 0 1em 1em; font-size: 90%; background: #f9f9f9; width:\n"
                + "250px; padding: 4px; text-align: left; float: right;\">\n" + "<div style=\"float: left;\">[[Image:Wikipedia-logo-\n"
                + "en.png|44px|none| ]]</div>\n" + "<div style=\"margin-left: 60px;\">[[Wikipedia]] has an\n" + "article on:\n"
                + "<div style=\"margin-left: 10px;\">\'\'\'\'\'[[w:PAGENAME|PAGENAME]]\'\'\'\'\' </div>\n" + "</div>\n" + "</div>\n"
                + "</div></div><span class=\"interProject\">[[w:\n" + "PAGENAME|Wikipedia ]]</span>");
    }

    private static final String TEST_STRING_02 = " {{#if:{{{cat|\n" + "{{{category|}}}}}}|a category|{{#if:{{{mul|{{{dab|\n"
            + "{{{disambiguation|}}}}}}}}}|articles|{{#if:{{{mulcat|}}}|categories|an\n" + "article}}}}}} on:\n";

    @Test public void testNestedIf02() {
        assertThat(wikiModel.parseTemplates(TEST_STRING_02, false)).isEqualTo(" an\n" + "article on:\n" + "");
    }

    @Test public void testNestedIf03() {
        assertThat(wikiModel.parseTemplates(TEST_STRING_03, false)).isEqualTo("PAGENAME");
    }

    private static final String TEST_STRING_04 = "{{ucfirst:{{{cat|{{{category}}}}}}}}";

    @Test public void testNestedIf04() {
        assertThat(wikiModel.parseTemplates(TEST_STRING_04, false)).isEqualTo("{{{category}}}");
    }//

    @Test public void testSwitch001() {
        assertThat(wikiModel.parseTemplates("{{ #switch: A | a=lower | A=UPPER  }}", false)).isEqualTo("UPPER");
    }

    @Test public void testSwitch002() {
        assertThat(wikiModel.parseTemplates("{{ #switch: {{lc:A}} | a=lower | UPPER  }}", false)).isEqualTo("lower");
    }

    @Test public void testSwitch003() {
        assertThat(wikiModel.parseTemplates("{{#switch: {{lc: {{{1| B }}} }}\n" + "| a\n" + "| b\n"
                + "| c = '''''abc''' or '''ABC'''''\n" + "| A\n" + "| B\n" + "| C = ''Memory corruption due to cosmic rays''\n"
                + "| #default = N/A\n" + "}}", false)).isEqualTo("'''''abc''' or '''ABC'''''");
    }

    @Test public void testSwitch004() {
        assertThat(wikiModel.parseTemplates("{{ #switch: +07 | 7 = Yes | 007 = Bond | No  }}", false)).isEqualTo("Yes");
    }

    @Test public void testSwitch005() {
        assertThat(wikiModel.parseTemplates("{{#switch: | = Nothing | foo = Foo | Something }}", false)).isEqualTo("Nothing");
    }

    @Test public void testSwitch006() {
        assertThat(wikiModel.parseTemplates("{{#switch: test | = Nothing | foo = Foo | Something }}", false)).isEqualTo("Something");
    }

    @Test public void testSwitch007() {
        assertThat(wikiModel.parseTemplates("{{#switch: test | foo = Foo | #default = Bar | baz = Baz }}", false)).isEqualTo("Bar");
    }

    @Test public void testSwitch008() {
        assertThat(wikiModel.parseTemplates("{{Templ1/{{ #switch: imperative  | ind | ind&}}}}", false)).isEqualTo("[[:Template:Templ1/ind&]]");
    }

    /**
     * <a href="https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Parameter_selection_templates"
     * >Help:Newlines_and_spaces#Parameter_selection_templates</a>
     */
    @Test public void testSwitch009() {
        assertThat(wikiModel.parseTemplates("*\"{{#switch: p |p= q |r=s}}\"", false)).isEqualTo("*\"q\"");
        assertThat(wikiModel.parseTemplates("*\"{{#switch:p| p = q |r=s}}\"", false)).isEqualTo("*\"q\"");
    }

    /**
     * See <a href="http://www.mediawiki.org/wiki/Help:Extension:ParserFunctions#Comparison_behaviour"
     * >Help:Extension:ParserFunctions - Comparison behaviour</a>
     */
    @Test public void testSwitch010() {
        assertThat(wikiModel.parseTemplates("{{#switch: 0 + 1 | 1 = one | 2 = two | three}}", false)).isEqualTo("three");
        assertThat(wikiModel.parseTemplates("{{#switch: {{#expr: 0 + 1}} | 1 = one | 2 = two | three}}", false)).isEqualTo("one");
        assertThat(wikiModel.parseTemplates("{{#switch: a | a = A | b = B | C}}", false)).isEqualTo("A");
        assertThat(wikiModel.parseTemplates("{{#switch: A | a = A | b = B | C}}", false)).isEqualTo("C");
        assertThat(wikiModel.parseTemplates("{{#switch: | = Nothing | foo = Foo | Something }}", false)).isEqualTo("Nothing");
        assertThat(wikiModel.parseTemplates("{{#switch: b | f = Foo | b = Bar | b = Baz | }}", false)).isEqualTo("Bar");
        assertThat(wikiModel.parseTemplates("{{#switch: 12345678901234567 | 12345678901234568 = A | B}}", false)).isEqualTo("B");

        assertThat(wikiModel.parseTemplates("{{#ifexpr: 12345678901234567 = 12345678901234568 | A | B}}", false)).isEqualTo("A");

    }

    @Test public void testExpr001() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 0.000001 }}", false)).isEqualTo("1.0E-6");
    }

    @Test public void testExpr002() {
        assertThat(wikiModel.parseTemplates("{{ #expr: +30 * +7 }}", false)).isEqualTo("210");
    }

    @Test public void testExpr003() {
        assertThat(wikiModel.parseTemplates("{{ #expr: -30 * -7 }}", false)).isEqualTo("210");
    }

    @Test public void testExpr004() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 * 7 }}", false)).isEqualTo("210");
    }

    @Test public void testExpr005() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 / 7 }}", false)).isEqualTo("4.285714285714286");
        // assertEquals("4.285714285714286", wikiModel.parseTemplates("{{ #expr: 30
        // div 7 }}", false));
    }

    @Test public void testExpr006() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 + 7 }}", false)).isEqualTo("37");
    }

    @Test public void testExpr007() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 - 7 }}", false)).isEqualTo("23");
    }

    @Test public void testExpr008() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 - 7 - 4}}", false)).isEqualTo("19");
    }

    @Test public void testExpr009() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 / 7 round 4}}", false)).isEqualTo("4.2857");
    }

    @Test public void testExpr010() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 <> 7}}", false)).isEqualTo("1");
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 != 7}}", false)).isEqualTo("1");
    }

    @Test public void testExpr011() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 < 7}}", false)).isEqualTo("0");
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 <= 42}}", false)).isEqualTo("1");
    }

    @Test public void testExpr012() {
        assertThat(wikiModel.parseTemplates("{{ #expr: (30 + 7)*7 }}", false)).isEqualTo("259");
        assertThat(wikiModel.parseTemplates("{{ #expr: 30 + 7 * 7 }}", false)).isEqualTo("79");
    }

    @Test public void testExpr013() {
        assertThat(wikiModel.parseTemplates("{{ #expr: 4 < 5 and 4 mod 2 }}", false)).isEqualTo("0");
        assertThat(wikiModel.parseTemplates("{{ #expr: 4 < 5 or 4 mod 2 }}", false)).isEqualTo("1");
    }

    @Test public void testExpr014() {
        assertThat(wikiModel.parseTemplates("{{ #expr: not 0 * 7 }}", false)).isEqualTo("7");
        assertThat(wikiModel.parseTemplates("{{ #expr: not 30 + 7 }}", false)).isEqualTo("7");
    }

    @Test public void testFormatnum001() {
        // default locale is ENGLISH
        assertThat(wikiModel.parseTemplates("{{formatnum:1401}}", false)).isEqualTo("1,401");
        assertThat(wikiModel.parseTemplates("{{formatnum:987654321.654321}}", false)).isEqualTo("987,654,321.654");
        assertThat(wikiModel.parseTemplates("{{FORMATNUM:987654321.654321}}", false)).isEqualTo("987,654,321.654");

        WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
        assertThat(germanWikiModel.parseTemplates("{{formatnum:1401}}", false)).isEqualTo("1.401");
        assertThat(germanWikiModel.parseTemplates("{{formatnum:987654321.654321}}", false)).isEqualTo("987.654.321,654");

        WikiModel italianWikiModel = newWikiTestModel(Locale.ITALIAN);
        assertThat(italianWikiModel.parseTemplates("{{formatnum:1401}}", false)).isEqualTo("1.401");
        assertThat(italianWikiModel.parseTemplates("{{formatnum:987654321}}", false)).isEqualTo("987.654.321");
    }

    @Test public void testFormatnum002() {
        // default locale is ENGLISH
        assertThat(wikiModel.parseTemplates("{{formatnum:987,654,321.654321|R}}", false)).isEqualTo("9.87654321654321E8");

        WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
        assertThat(germanWikiModel.parseTemplates("{{formatnum:987.654.321,654321|R}}", false)).isEqualTo("9.87654321654321E8");
    }

    @Test public void testFormatnum003() {
        // default locale is ENGLISH
        assertThat(wikiModel.parseTemplates("{{formatnum:90}}")).isEqualTo("90");
        WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
        assertThat(germanWikiModel.parseTemplates("{{formatnum:90}}")).isEqualTo("90");
    }

    @Test public void testFormatnum004() {
        // default locale is ENGLISH
        assertThat(wikiModel.parseTemplates("{{formatnum:90.}}")).isEqualTo("90.");
        WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
        assertThat(germanWikiModel.parseTemplates("{{formatnum:90.}}")).isEqualTo("90,");
    }

    @Test public void testFormatnum005() {
        // default locale is ENGLISH
        assertThat(wikiModel.parseTemplates("{{formatnum:90.0}}")).isEqualTo("90.0");
        WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
        assertThat(germanWikiModel.parseTemplates("{{formatnum:90.0}}")).isEqualTo("90,0");
    }

    @Test public void testFormatnum006() {
        // default locale is ENGLISH
        assertThat(wikiModel.parseTemplates("{{formatnum:90.000}}")).isEqualTo("90.000");
        assertThat(wikiModel.parseTemplates("{{formatnum:90.000000000000000000000000000000000000}}")).isEqualTo("90.000000000000000000000000000000000000");
        WikiModel germanWikiModel = newWikiTestModel(Locale.GERMAN);
        assertThat(germanWikiModel.parseTemplates("{{formatnum:90.000}}")).isEqualTo("90,000");
        assertThat(germanWikiModel.parseTemplates("{{formatnum:90.000000000000000000000000000000000000}}")).isEqualTo("90,000000000000000000000000000000000000");
    }

    @Test public void testPlural001() {
        assertThat(wikiModel.parseTemplates("{{plural:n|is|are}}", false)).isEqualTo("is");
        assertThat(wikiModel.parseTemplates("{{plural:0|is|are}}", false)).isEqualTo("is");
        assertThat(wikiModel.parseTemplates("{{plural:1|is|are}}", false)).isEqualTo("is");
        assertThat(wikiModel.parseTemplates("{{plural:2|is|are}}", false)).isEqualTo("are");
        assertThat(wikiModel.parseTemplates("{{plural:3|is|are}}", false)).isEqualTo("are");
        assertThat(wikiModel.parseTemplates("{{plural:{{#expr:30+7}}|is|are}}", false)).isEqualTo("are");
    }

    @Test public void testExpr015() {
        assertThat(wikiModel.parseTemplates("{{ #expr: trunc1.2}}", false)).isEqualTo("1");
        assertThat(wikiModel.parseTemplates("{{ #expr: trunc-1.2 }}", false)).isEqualTo("-1");
        assertThat(wikiModel.parseTemplates("{{ #expr: floor 1.2}}", false)).isEqualTo("1");
        assertThat(wikiModel.parseTemplates("{{ #expr: floor -1.2 }}", false)).isEqualTo("-2");
        assertThat(wikiModel.parseTemplates("{{ #expr: fLoOr -1.2 }}", false)).isEqualTo("-2");
        assertThat(wikiModel.parseTemplates("{{ #expr: ceil 1.2}}", false)).isEqualTo("2");
        assertThat(wikiModel.parseTemplates("{{ #expr: ceil-1.2 }}", false)).isEqualTo("-1");
    }

    @Test public void testExpr016() {
        assertThat(wikiModel.parseTemplates("{{#expr:1.0E-7}}", false)).isEqualTo("1.0E-7");
    }

    @Test public void testExpr017() {
        assertThat(wikiModel.parseTemplates("{{#expr:4/0}}", false)).isEqualTo("<div class=\"error\">Expression error: Division by zero</div>");
        assertThat(wikiModel.parseTemplates("{{#expr:3/4}}", false)).isEqualTo("0.75");
        assertThat(wikiModel.parseTemplates("{{#expr:13 mod 0}}",
                false)).isEqualTo("<div class=\"error\">Expression error: Division by zero</div>");
        assertThat(wikiModel.parseTemplates("{{#expr:13 mod 3}}", false)).isEqualTo("1");
        assertThat(wikiModel.parseTemplates("{{#expr:3 ^3}}", false)).isEqualTo("27");
        assertThat(wikiModel.parseTemplates("{{#expr:3 ^ (-3)}}", false)).isEqualTo("0.037037037037037035");
        assertThat(wikiModel.parseTemplates("{{#expr:(-4) ^ (-1/2)}}", false)).isEqualTo("NAN");
        assertThat(wikiModel.parseTemplates("{{#expr:ln EXp 1 }}", false)).isEqualTo("1");
        assertThat(wikiModel.parseTemplates("{{#expr:exp ln e }}", false)).startsWith("2.718281828459045");
        assertThat(wikiModel.parseTemplates("{{#expr:sin (pi/2) }}", false)).isEqualTo("1");
        assertThat(wikiModel.parseTemplates("{{#expr:(sin pi)/2 }}", false)).isEqualTo("6.123233995736766E-17");
        assertThat(wikiModel.parseTemplates("{{#expr:sin pi/2 }}", false)).isEqualTo("6.123233995736766E-17");
    }

    @Test public void testExpr018() {
        assertThat(wikiModel.parseTemplates("{{#expr:1e-92round400}}", false)).isEqualTo("0");
        assertThat(wikiModel.parseTemplates("{{#expr:(15782.316272965878)round((3))<1E9}}", false)).isEqualTo("1");
        assertThat(wikiModel.parseTemplates("{{#expr:((15782.316272965878)round((3))/1E5round0)E5}}", false)).isEqualTo("1578200000");
    }

    /**
     * See <a href=
     * "http://www.mediawiki.org/wiki/Help:Extension:ParserFunctions#Rounding"
     * >Help:Extension:ParserFunctions Expr Rounding</a>
     */
    @Test public void testExpr019() {
        assertThat(wikiModel.parseTemplates("{{#expr: 9.876 round2 }}", false)).isEqualTo("9.88");
        assertThat(wikiModel.parseTemplates("{{#expr: (trunc1234) round trunc-2 }}", false)).isEqualTo("1200");
        assertThat(wikiModel.parseTemplates("{{#expr: 4.5 round0 }}", false)).isEqualTo("5");
        assertThat(wikiModel.parseTemplates("{{#expr: -4.5 round0 }}", false)).isEqualTo("-5");
        // assertEquals("46.9",
        // wikiModel.parseTemplates("{{#expr: 46.857 round1.8 }}", false));
        // assertEquals("50",
        // wikiModel.parseTemplates("{{#expr: 46.857 round-1.8 }}", false));
    }

    @Test public void testExpr020() {
        assertThat(wikiModel.parseTemplates("{{#expr: e }}", false)).isEqualTo("2.718281828459045");
        assertThat(wikiModel.parseTemplates("{{#expr: pi }}", false)).isEqualTo("3.141592653589793");
    }

    @Test public void testNS001() {
        // TODO: these namespaces should actually have spaces
        assertThat(wikiModel.parseTemplates("{{ns:3}}", false)).isEqualTo("User_talk");
        assertThat(wikiModel.parseTemplates("{{ns:{{ns:12}}_talk}}", false)).isEqualTo("Help_talk");
        assertThat(wikiModel.parseTemplates("{{ns:{{ns:8}}_talk}}", false)).isEqualTo("MediaWiki_talk");
        assertThat(wikiModel.parseTemplates("{{ns:{{ns:8}} talk}}", false)).isEqualTo("MediaWiki_talk");
        assertThat(wikiModel.parseTemplates("{{ns:{{ns:8}} talk  }}", false)).isEqualTo("MediaWiki_talk");
        assertThat(wikiModel.parseTemplates("{{ns:{{ns:8}}talk}}", false)).isEqualTo("[[:Template:Ns:MediaWikitalk]]");
        assertThat(wikiModel.parseTemplates("{{ns:100}}", false)).isEqualTo("Portal");
        assertThat(wikiModel.parseTemplates("{{ns:{{ns:100}}_talk}}", false)).isEqualTo("Portal_talk");
    }

    @Test public void testNSE001() {
        assertThat(wikiModel.parseTemplates("{{nse:3}}", false)).isEqualTo("User_talk");
        assertThat(wikiModel.parseTemplates("{{nse:{{nse:12}}_talk}}", false)).isEqualTo("Help_talk");
        assertThat(wikiModel.parseTemplates("{{nse:{{nse:8}}_talk}}", false)).isEqualTo("MediaWiki_talk");
        assertThat(wikiModel.parseTemplates("{{nse:{{nse:8}} talk}}", false)).isEqualTo("MediaWiki_talk");
        assertThat(wikiModel.parseTemplates("{{nse:{{nse:8}} talk  }}", false)).isEqualTo("MediaWiki_talk");
        assertThat(wikiModel.parseTemplates("{{nse:{{nse:8}}talk}}", false)).isEqualTo("[[:Template:Ns:MediaWikitalk]]");
        assertThat(wikiModel.parseTemplates("{{ns:100}}", false)).isEqualTo("Portal");
        assertThat(wikiModel.parseTemplates("{{ns:{{ns:100}}_talk}}", false)).isEqualTo("Portal_talk");
    }

    @Test public void testNAMESPACE001() {
        assertThat(wikiModel.parseTemplates("{{NAMESPACE}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{NAMESPACE:Template:Main Page}}", false)).isEqualTo("Template");
        assertThat(wikiModel.parseTemplates("{{NAMESPACE:Bad:Main Page}}", false)).isEqualTo("");
    }

    @Test public void testNAMESPACE002() {
        assertThat(wikiModel.parseTemplates("{{NAMESPACE}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{NAMESPACE:Image:Main Page}}", false)).isEqualTo("File");
    }

    @Test public void testNAMESPACE003() {
        wikiModel.setNamespaceName("category");
        assertThat(wikiModel.parseTemplates("{{NAMESPACE}}", false)).isEqualTo("Category");
        assertThat(wikiModel.parseTemplates("{{NAMESPACE:Image:Main Page}}", false)).isEqualTo("File");
    }

    @Test public void testNAMESPACE004() {
        wikiModel.setPageName("Sandbox");
        wikiModel.setNamespaceName("project");
        assertThat(wikiModel.parseTemplates("{{NAMESPACE}}", false)).isEqualTo("Project");
        assertThat(wikiModel.parseTemplates("{{NAMESPACE:}}", false)).isEqualTo("");
    }

    @Test public void testNAMESPACE005() {
        wikiModel.setPageName("Sandbox");
        wikiModel.setNamespaceName("project_talk");
        assertThat(wikiModel.parseTemplates("{{NAMESPACEE}}", false)).isEqualTo("Project_talk");
        // TODO: namespace should actually have a space:
        assertThat(wikiModel.parseTemplates("{{NAMESPACE}}", false)).isEqualTo("Project_talk");
    }

    @Test public void testNAMESPACE006() {
        assertThat(wikiModel.parseTemplates("{{NAMESPACE:Image:test.jpg}}", false)).isEqualTo("File");
        assertThat(wikiModel.parseTemplates("{{NAMESPACEE:Image:test.jpg}}", false)).isEqualTo("File");
    }

    @Test public void testTALKSPACE001() {
        assertThat(wikiModel.parseTemplates("{{TALKSPACE}}", false)).isEqualTo("Talk");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE:Template:Main Page}}", false)).isEqualTo("Template_talk");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE:Bad:Main Page}}", false)).isEqualTo("Talk");
    }

    @Test public void testTALKSPACE002() {
        assertThat(wikiModel.parseTemplates("{{TALKSPACE}}", false)).isEqualTo("Talk");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE:Image:Main Page}}", false)).isEqualTo("File_talk");
    }

    @Test public void testTALKSPACE003() {
        wikiModel.setNamespaceName("category");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE}}", false)).isEqualTo("Category_talk");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE:Image:Main Page}}", false)).isEqualTo("File_talk");
    }

    @Test public void testTALKSPACE004() {
        wikiModel.setPageName("Sandbox");
        wikiModel.setNamespaceName("project");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE}}", false)).isEqualTo("Project_talk");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE:}}", false)).isEqualTo("");
    }

    @Test public void testTALKSPACE005() {
        wikiModel.setPageName("Sandbox");
        wikiModel.setNamespaceName("project_talk");
        assertThat(wikiModel.parseTemplates("{{TALKSPACEE}}", false)).isEqualTo("Project_talk");
        // TODO: talkspace should actually have a space:
        assertThat(wikiModel.parseTemplates("{{TALKSPACE}}", false)).isEqualTo("Project_talk");
    }

    @Test public void testTALKSPACE006() {
        wikiModel.setNamespaceName("portal");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE}}", false)).isEqualTo("Portal_talk");
    }

    @Test public void testTALKSPACE007() {
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE}}", false)).isEqualTo("Talk");
        assertThat(wikiModel.parseTemplates("{{TALKSPACE:Portal:Main Page}}", false)).isEqualTo("Portal_talk");
    }

    @Test public void testSUBJECTSPACE001() {
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE:Template:Main Page}}", false)).isEqualTo("Template");
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE:Bad:Main Page}}", false)).isEqualTo("");
    }

    @Test public void testSUBJECTSPACE002() {
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE:Image:Main Page}}", false)).isEqualTo("File");
    }

    @Test public void testSUBJECTSPACE003() {
        wikiModel.setNamespaceName("category");
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE}}", false)).isEqualTo("Category");
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE:Image:Main Page}}", false)).isEqualTo("File");
    }

    @Test public void testSUBJECTSPACE004() {
        wikiModel.setPageName("Sandbox");
        wikiModel.setNamespaceName("project");
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE}}", false)).isEqualTo("Project");
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE:}}", false)).isEqualTo("");
    }

    @Test public void testSUBJECTSPACE005() {
        wikiModel.setPageName("Sandbox");
        wikiModel.setNamespaceName("project_talk");
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACEE}}", false)).isEqualTo("Project");
        assertThat(wikiModel.parseTemplates("{{SUBJECTSPACE}}", false)).isEqualTo("Project");
    }

    @Test public void testPAGENAME001() {
        wikiModel.setPageName("MyPage");
        assertThat(wikiModel.parseTemplates("{{PAGENAME}}", false)).isEqualTo("MyPage");
        assertThat(wikiModel.parseTemplates("{{PAGENAME:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{PAGENAME:Main Page}}", false)).isEqualTo("Main Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAME:Main_Page}}", false)).isEqualTo("Main Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAME:Main page}}", false)).isEqualTo("Main page");
        assertThat(wikiModel.parseTemplates("{{PAGENAME:main Page}}", false)).isEqualTo("Main Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAME:main page}}", false)).isEqualTo("Main page");
        assertThat(wikiModel.parseTemplates("{{PAGENAME:Template:Main Page}}", false)).isEqualTo("Main Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAME:Bad:Main Page}}", false)).isEqualTo("Bad:Main Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAME:Image:test.jpg}}", false)).isEqualTo("Test.jpg");
    }

    @Test public void testPAGENAMEE001() {
        wikiModel.setPageName("MyPage");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE}}", false)).isEqualTo("MyPage");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE:Main Page}}", false)).isEqualTo("Main_Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE:Main_Page}}", false)).isEqualTo("Main_Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE:Main page}}", false)).isEqualTo("Main_page");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE:main Page}}", false)).isEqualTo("Main_Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE:main page}}", false)).isEqualTo("Main_page");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE:Template:Main Page}}", false)).isEqualTo("Main_Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE:Bad:Main Page}}", false)).isEqualTo("Bad:Main_Page");
        assertThat(wikiModel.parseTemplates("{{PAGENAMEE:Image:test.jpg}}", false)).isEqualTo("Test.jpg");
    }

    @Test public void testFULLPAGENAME001() {
        wikiModel.setPageName("MyPage");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAME}}", false)).isEqualTo("MyPage");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAME:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAME:Main Page}}", false)).isEqualTo("Main Page");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAME:Main_Page}}", false)).isEqualTo("Main Page");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAME:Template:Main Page}}", false)).isEqualTo("Template:Main Page");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAME:Bad:Main Page}}", false)).isEqualTo("Bad:Main Page");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAME:Image:test.jpg}}", false)).isEqualTo("File:Test.jpg");
    }

    @Test public void testFULLPAGENAMEE001() {
        wikiModel.setPageName("MyPage");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAMEE}}", false)).isEqualTo("MyPage");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAMEE:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAMEE:Main Page}}", false)).isEqualTo("Main_Page");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAMEE:Main_Page}}", false)).isEqualTo("Main_Page");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAMEE:Template:Main Page}}", false)).isEqualTo("Template:Main_Page");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAMEE:Bad:Main Page}}", false)).isEqualTo("Bad:Main_Page");
        assertThat(wikiModel.parseTemplates("{{FULLPAGENAMEE:Image:test.jpg}}", false)).isEqualTo("File:Test.jpg");
    }

    @Test public void testTALKPAGENAME001() {
        wikiModel.setPageName("MyPage");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAME}}", false)).isEqualTo("Talk:MyPage");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAME:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAME:Main Page}}", false)).isEqualTo("Talk:Main Page");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAME:Template:Main Page}}", false)).isEqualTo("Template_talk:Main Page"); // TODO: talk namespace should contain a space
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAME:Bad:Main Page}}", false)).isEqualTo("Talk:Bad:Main Page");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAME:Template:Sandbox}}", false)).isEqualTo("Template_talk:Sandbox"); // TODO: talk namespace should contain a space
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAME:Template_talk:Sandbox}}", false)).isEqualTo("Template_talk:Sandbox"); // TODO: talk namespace should contain a space
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAME:Template talk:Sandbox}}", false)).isEqualTo("Template_talk:Sandbox"); // TODO: talk namespace should contain a space
    }

    @Test public void testTALKPAGENAMEE001() {
        wikiModel.setPageName("MyPage");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAMEE}}", false)).isEqualTo("Talk:MyPage");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAMEE:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAMEE:Main Page}}", false)).isEqualTo("Talk:Main_Page");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAMEE:Template:Main Page}}", false)).isEqualTo("Template_talk:Main_Page");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAMEE:Bad:Main Page}}", false)).isEqualTo("Talk:Bad:Main_Page");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAMEE:Template:Sandbox}}", false)).isEqualTo("Template_talk:Sandbox");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAMEE:Template_talk:Sandbox}}", false)).isEqualTo("Template_talk:Sandbox");
        assertThat(wikiModel.parseTemplates("{{TALKPAGENAMEE:Template talk:Sandbox}}", false)).isEqualTo("Template_talk:Sandbox");
    }

    @Test public void testSUBJECTPAGENAME001() {
        wikiModel.setPageName("MyPage");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAME}}", false)).isEqualTo("MyPage");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAME:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAME:Main Page}}", false)).isEqualTo("Main Page");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAME:Template:Main Page}}", false)).isEqualTo("Template:Main Page");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAME:Bad:Main Page}}", false)).isEqualTo("Bad:Main Page");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAME:Template:Sandbox}}", false)).isEqualTo("Template:Sandbox");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAME:Template_talk:Sandbox}}", false)).isEqualTo("Template:Sandbox");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAME:Template talk:Sandbox}}", false)).isEqualTo("Template:Sandbox");
    }

    @Test public void testSUBJECTPAGENAMEE001() {
        wikiModel.setPageName("MyPage");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAMEE}}", false)).isEqualTo("MyPage");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Main Page}}", false)).isEqualTo("Main_Page");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Template:Main Page}}", false)).isEqualTo("Template:Main_Page");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Bad:Main Page}}", false)).isEqualTo("Bad:Main_Page");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Template:Sandbox}}", false)).isEqualTo("Template:Sandbox");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Template_talk:Sandbox}}", false)).isEqualTo("Template:Sandbox");
        assertThat(wikiModel.parseTemplates("{{SUBJECTPAGENAMEE:Template talk:Sandbox}}", false)).isEqualTo("Template:Sandbox");
    }

    @Test public void testBASEPAGENAME001() {
        wikiModel.setPageName("MyPage");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME}}", false)).isEqualTo("MyPage");
    }

    @Test public void testBASEPAGENAME002() {
        wikiModel.setPageName("MyPage/foo");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME}}", false)).isEqualTo("MyPage");
    }

    @Test public void testBASEPAGENAME003() {
        wikiModel.setPageName("MyPage/foo/bar");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME}}", false)).isEqualTo("MyPage/foo");
    }

    @Test public void testBASEPAGENAME004() {
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME:Talk:title}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title/foo}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title/foo/bar}}", false)).isEqualTo("Title/foo");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title/foo/bar/baz}}", false)).isEqualTo("Title/foo/bar");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME:Talk:Title 2/foo/bar/baz}}", false)).isEqualTo("Title 2/foo/bar");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAME:Bad:Title 2/foo/bar/baz}}", false)).isEqualTo("Bad:Title 2/foo/bar");
    }

    @Test public void testBASEPAGENAMEE001() {
        wikiModel.setPageName("MyPage");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE}}", false)).isEqualTo("MyPage");
    }

    @Test public void testBASEPAGENAMEE002() {
        wikiModel.setPageName("MyPage/foo");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE}}", false)).isEqualTo("MyPage");
    }

    @Test public void testBASEPAGENAMEE003() {
        wikiModel.setPageName("MyPage/foo/bar");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE}}", false)).isEqualTo("MyPage/foo");
    }

    @Test public void testBASEPAGENAMEE004() {
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:title}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title/foo}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title/foo/bar}}", false)).isEqualTo("Title/foo");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title/foo/bar/baz}}", false)).isEqualTo("Title/foo/bar");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE:Talk:Title 2/foo/bar/baz}}", false)).isEqualTo("Title_2/foo/bar");
        assertThat(wikiModel.parseTemplates("{{BASEPAGENAMEE:Bad:Title 2/foo/bar/baz}}", false)).isEqualTo("Bad:Title_2/foo/bar");
    }

    @Test public void testSUBPAGENAME001() {
        wikiModel.setPageName("MyPage");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME}}", false)).isEqualTo("MyPage");
    }

    @Test public void testSUBPAGENAME002() {
        wikiModel.setPageName("MyPage/foo");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME}}", false)).isEqualTo("foo");
    }

    @Test public void testSUBPAGENAME003() {
        wikiModel.setPageName("MyPage/foo/bar");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME}}", false)).isEqualTo("bar");
    }

    @Test public void testSUBPAGENAME004() {
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME:Talk:title}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title/foo}}", false)).isEqualTo("foo");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title/foo/bar}}", false)).isEqualTo("bar");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title/foo/bar/baz}}", false)).isEqualTo("baz");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME:Talk:Title 2/foo/bar/baz 2}}", false)).isEqualTo("baz 2");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAME:Bad:Title 2/foo/bar/baz 2}}", false)).isEqualTo("baz 2");
    }

    @Test public void testSUBPAGENAMEE001() {
        wikiModel.setPageName("MyPage");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE}}", false)).isEqualTo("MyPage");
    }

    @Test public void testSUBPAGENAMEE002() {
        wikiModel.setPageName("MyPage/foo");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE}}", false)).isEqualTo("foo");
    }

    @Test public void testSUBPAGENAMEE003() {
        wikiModel.setPageName("MyPage/foo/bar");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE}}", false)).isEqualTo("bar");
    }

    @Test public void testSUBPAGENAMEE004() {
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE:}}", false)).isEqualTo("");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:title}}", false)).isEqualTo("Title");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title/foo}}", false)).isEqualTo("foo");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title/foo/bar}}", false)).isEqualTo("bar");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title/foo/bar/baz}}", false)).isEqualTo("baz");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE:Talk:Title 2/foo/bar/baz 2}}", false)).isEqualTo("baz_2");
        assertThat(wikiModel.parseTemplates("{{SUBPAGENAMEE:Bad:Title 2/foo/bar/baz 2}}", false)).isEqualTo("baz_2");
    }

    @Test public void testURLEncode001() {
        assertThat(wikiModel.parseTemplates(
                "{{urlencode: \"#$%&'()*,;?[]^`{}}}", false)).isEqualTo("%22%23%24%25%26%27%28%29*%2C%3B%3F%5B%5D%5E%60%7B%7D");
        assertThat(wikiModel.parseTemplates("{{urlencode:<}}", false)).isEqualTo("%3C");
        assertThat(wikiModel.parseTemplates("{{urlencode:>}}", false)).isEqualTo("%3E");
        assertThat(wikiModel.parseTemplates("{{urlencode:{{!}}}}", false)).isEqualTo("%7C");
    }

    @Test public void testPadleft001() {
        assertThat(wikiModel.parseTemplates("{{padleft:8}}", false)).isEqualTo("8");
        assertThat(wikiModel.parseTemplates("{{padleft:8|3}}", false)).isEqualTo("008");
        assertThat(wikiModel.parseTemplates("{{padleft:8|a}}", false)).isEqualTo("8");
        assertThat(wikiModel.parseTemplates("{{padleft:7|3|0}}", false)).isEqualTo("007");
        assertThat(wikiModel.parseTemplates("{{padleft:0|3|0}}", false)).isEqualTo("000");
        assertThat(wikiModel.parseTemplates("{{padleft:bcd|6|a}}", false)).isEqualTo("aaabcd");
        assertThat(wikiModel.parseTemplates("{{padleft:cafe|8|-}}", false)).isEqualTo("----cafe");
        assertThat(wikiModel.parseTemplates("{{padleft:bcd|6|{{!}}}}", false)).isEqualTo("|||bcd");
    }

    @Test public void testPadright001() {
        assertThat(wikiModel.parseTemplates("{{padright:8}}", false)).isEqualTo("8");
        assertThat(wikiModel.parseTemplates("{{padright:8|3}}", false)).isEqualTo("800");
        assertThat(wikiModel.parseTemplates("{{padright:8|a}}", false)).isEqualTo("8");
        assertThat(wikiModel.parseTemplates("{{padright:bcd|6|a}}", false)).isEqualTo("bcdaaa");
        assertThat(wikiModel.parseTemplates("{{padright:0|6|a}}", false)).isEqualTo("0aaaaa");

    }

    @Test public void testTime001() {
        // seconds since January 1970
        String currentSecondsStr = wikiModel.parseTemplates("{{ #time: U }}", false);
        Long currentSeconds = Long.valueOf(currentSecondsStr);

        assertThat(currentSeconds).isGreaterThan(1212598361);
    }

    @Test public void testTag001() {
        assertThat(wikiModel.parseTemplates("{{#tag:references|{{{refs|}}}|group={{{group|}}}}}")).isEqualTo("<references =\"\"></references>");
    }

    @Test public void testPipe001() {
        assertThat(wikiModel.parseTemplates("{{2x|hello world" + "}} ", false)).isEqualTo("hello worldhello world ");
    }

    @Test public void testPipe001a() {
        assertThat(wikiModel.parseTemplates("{{2x|Hello World\n" + "}}", false)).isEqualTo("Hello World\n" + "Hello World\n" + "");
    }

    @Test public void testPipe002() {
        assertThat(wikiModel.parseTemplates("{{2x|{{{!}} \n" + "{{!}} A \n" + "{{!}} B\n" + "{{!}}- \n"
                + "{{!}} C\n" + "{{!}} D\n" + "{{!}}}\n" + "}}", false)).isEqualTo("{| \n" + "| A \n" + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "{| \n" + "| A \n" + "| B\n" + "|- \n"
                + "| C\n" + "| D\n" + "|}\n");
    }

//    @Test public void testPipe003() {
//        assertEquals("{| \n" + "| A \n" + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "{| \n" + "| A \n" + "| B\n" + "|- \n"
//                + "| C\n" + "| D\n" + "|}\n" + "{| \n" + "| A \n" + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "{| \n" + "| A \n"
//                + "| B\n" + "|- \n" + "| C\n" + "| D\n" + "|}\n" + "", wikiModel.parseTemplates("{{2x|{{2x|{{{!}} \n" + "{{!}} A \n"
//                + "{{!}} B\n" + "{{!}}- \n" + "{{!}} C\n" + "{{!}} D\n" + "{{!}}}\n" + "}}}}", false));
//    }

    @Test public void testInvalidNoinclude() {
        assertThat(wikiModel.parseTemplates("test123 start<noinclude>\n" + "test123 end")).isEqualTo("test123 start\n" + "test123 end");
    }

    @Test public void testTemplateImage1() {
        // see method WikiTestModel#getRawWikiContent()
        assertThat(wikiModel
                .parseTemplates("{|\n"
                        + "! | <h2 style=\"background:#cedff2;\">In the news</h2>\n"
                        + "|-\n"
                        + "| style=\"color:#000; padding:2px 5px;\" | <div id=\"mp-itn\">{{Image\n"
                        + " |image  = Yoshihiko Noda-1.jpg\n"
                        + " |title  = Yoshihiko Noda\n"
                        + "}}\n"
                        + "The ruling Democratic Party of Japan selects '''Yoshihiko Noda''' ''(pictured)'' as the country's new prime minister, following the resignation of Naoto Kan\n"
                        + "</div>\n" + "|}")).isEqualTo("{|\n"
                + "! | <h2 style=\"background:#cedff2;\">In the news</h2>\n"
                + "|-\n"
                + "| style=\"color:#000; padding:2px 5px;\" | <div id=\"mp-itn\">[[File:Yoshihiko Noda-1.jpg|Yoshihiko Noda|alt=Yoshihiko Noda]]\n"
                + "The ruling Democratic Party of Japan selects '''Yoshihiko Noda''' ''(pictured)'' as the country's new prime minister, following the resignation of Naoto Kan\n"
                + "</div>\n" + "|}");
    }

    @Test public void testInvalidIncludeonly() {
        assertThat(wikiModel.parseTemplates("test123 start<includeonly>\n" + "test123 end")).isEqualTo("test123 start");
    }

    @Test public void testInvalidOnlyinclude() {
        assertThat(wikiModel.parseTemplates("test123 start<onlyinclude>\n" + "test123 end")).isEqualTo("test123 start\n" + "test123 end");
    }

    @Test public void testMagicCURRENTYEAR() {
        // assertEquals("test 2010 test123",
        // wikiModel.parseTemplates("test {{CURRENTYEAR}} test123"));
    }

    @Test public void testMagicPAGENAME01() {
        assertThat(wikiModel.parseTemplates("test [[{{PAGENAME}}]] test123")).isEqualTo("test [[PAGENAME]] test123");
    }

    @Test public void testMagicPAGENAME02() {
        assertThat(wikiModel.parseTemplates("test [[{{PAGENAME:Sandbox}}]] test123")).isEqualTo("test [[Sandbox]] test123");
    }

    @Test public void testMagicTALKPAGENAME01() {
        assertThat(wikiModel.parseTemplates("test [[{{TALKPAGENAME:Sandbox}}]] test123")).isEqualTo("test [[Talk:Sandbox]] test123");
    }

    @Test public void testMagicTALKPAGENAME02() {
        assertThat(wikiModel.parseTemplates("test [[{{TALKPAGENAME:Help:Sandbox}}]] test123")).isEqualTo("test [[Help_talk:Sandbox]] test123");
    }

    @Test public void testMagicTALKPAGENAME03() {
        assertThat(wikiModel.parseTemplates("test [[{{TALKPAGENAME:\nHelp:Sandbox}}]] test123")).isEqualTo("test [[Help_talk:Sandbox]] test123");
    }

    @Test public void testMagicTALKPAGENAME04() {
        wikiModel.setPageName("MyPage");
        wikiModel.setNamespaceName("");
        assertThat(wikiModel.parseTemplates("test [[{{TALKPAGENAME}}]] test123")).isEqualTo("test [[Talk:MyPage]] test123");
    }

    @Test public void testMagicTALKPAGENAME05() {
        wikiModel.setPageName("MyPage");
        wikiModel.setNamespaceName("category");
        assertThat(wikiModel.parseTemplates("test [[{{TALKPAGENAME}}]] test123")).isEqualTo("test [[Category_talk:MyPage]] test123");
    }

    // @Test public void testRef001() {
    // assertEquals(
    // "",
    // wikiModel.parseTemplates("<ref>{{cite web |url=http://www.pottsmerc.com/articles/2009/04/12/opinion/srv0000005095974.txt |title=Actor Tom Hanks talks about religion |author=Terry Mattingly |work=The Mercury |date=April 12, 2009 |accessdate=October 19, 2010}}</ref>\n\n<references/>"));
    // }
    //
    //
    // @Test public void testCommonsCategory() {
    // assertEquals(
    // "",
    // wikiModel.parseTemplates("{{Commons category}}"));
    // }

    @Test public void testTemplateSwitch() {
        // issue #32
        assertThat(wikiModel
                .parseTemplates("{{#switch:y|y=1001|d={{#switch:w1001|w0=1|w-0=-8|{{#expr:\n"
                        + "1001*10{{#ifexpr:1001<0|-8|+1}}}}}}|c={{#expr:1001*100{{#ifexpr: 1001>0|-98|+1}}}}|m={{#expr:1001*1000{{#ifexpr:1001>0|-998|+1}}}}}}")).isEqualTo("1001");
    }

    @Test public void testTitleparts000() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok }}", false)).isEqualTo("Talk:Foo/bar/baz/quok");
    }

    @Test public void testTitleparts001() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | 1 }}", false)).isEqualTo("Talk:Foo");
    }

    @Test public void testTitleparts002() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | 2 }}", false)).isEqualTo("Talk:Foo/bar");
    }

    @Test public void testTitleparts003() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | 3 }}", false)).isEqualTo("Talk:Foo/bar/baz");
    }

    @Test public void testTitleparts004() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -1 }}", false)).isEqualTo("Talk:Foo/bar/baz");
    }

    @Test public void testTitleparts005() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -2 }}", false)).isEqualTo("Talk:Foo/bar");
    }

    @Test public void testTitleparts006() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -3 }}", false)).isEqualTo("Talk:Foo");
    }

    @Test public void testTitleparts007() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -4 }}", false)).isEqualTo("");
    }

    @Test public void testTitleparts008() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -5 }}", false)).isEqualTo("");
    }

    @Test public void testTitleparts009() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | 2 | 2 }}", false)).isEqualTo("bar/baz");
    }

    @Test public void testTitleparts010() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | | 2 }}", false)).isEqualTo("bar/baz/quok");
    }

    @Test public void testTitleparts011() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | | -1 }}", false)).isEqualTo("quok");
    }

    @Test public void testTitleparts012() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar/baz/quok | -1 | 2 }}", false)).isEqualTo("bar/baz");
    }

    @Test public void testTitleparts013() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo | -1 }}", false)).isEqualTo("");
    }

    @Test public void testTitleparts014() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo | | -1 }}", false)).isEqualTo("Talk:Foo");
    }

    @Test public void testTitleparts015() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar | | -1 }}", false)).isEqualTo("bar");
    }

    @Test public void testTitleparts016() {
        assertThat(wikiModel.parseTemplates("{{#titleparts: Talk:Foo/bar | | -2 }}", false)).isEqualTo("Talk:Foo/bar");
    }


    @Test public void testIssue77_001() {
        assertThat(wikiModel
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
                        + "|}")).isEqualTo("{|\n"
                + "! | <h2 style=\"background:#cedff2;\">In the news</h2>\n"
                + "|-\n"
                + "| | <div><div style=\"float:right;margin-left:0.5em;\">\n"
                + "[[File:Yoshihiko Noda-1.jpg|100x100px||Yoshihiko Noda|alt=Yoshihiko Noda|link=File:Yoshihiko Noda-1.jpg]]\n"
                + "</div>\n"
                + "The ruling Democratic Party of Japan selects '''Yoshihiko Noda''' ''(pictured)'' as the country's new Prime Minister of Japan|prime minister, following the resignation of Naoto Kan.</div>\n"
                + "|}");
    }

//    @Test public void testIssue77_002() {
//        assertEquals(
//                "{| style=\"width: 100%; height: auto; border: 1px solid #88A; background-color: #ACF; vertical-align: top; margin: 0em 0em 0.5em 0em; border-spacing: 0.6em;\" cellspacing=\"6\"\n"
//                        + "|-\n"
//                        + "\n"
//                        + "| style=\"width: 100%; vertical-align:top; color:#000; border: 3px double #AAA; background-color: #ffffff; padding: 0.5em; margin: 0em;\" colspan=\"2\" |\n"
//                        + "{| style=\"vertical-align: top; margin: 0em; width: 100% !important; width: auto; display: table !important; display: inline; background-color: transparent;\"\n"
//                        + "! colspan=\"2\" style=\"background:#F0F0F0; margin: 0em; height: 1em; font-weight:bold; border:1px solid #AAA; text-align:left; color:#000;\" | <div style=\"float:right;\"></div><h1 style=\"text-align: left; font-size: 1.2em; border: none; margin: 0; padding: 1.5px 0 2px 4px;\">'''Knowledge groups'''</h1></div>\n"
//                        + "|-\n"
//                        + "|\n"
//                        + "TEST1\n"
//                        + "|}\n"
//                        + "|-\n"
//                        + "\n"
//                        + "| style=\"width: 100%; vertical-align:top; color:#000; border: 3px double #AAA; background-color: #ffffff; padding: 0.5em; margin: 0em;\" colspan=\"2\" |\n"
//                        + "{| style=\"vertical-align: top; margin: 0em; width: 100% !important; width: auto; display: table !important; display: inline; background-color: transparent;\"\n"
//                        + "! colspan=\"2\" style=\"background:#F0F0F0; margin: 0em; height: 1em; font-weight:bold; border:1px solid #AAA; text-align:left; color:#000;\" | <div style=\"float:right;\"></div><h1 style=\"text-align: left; font-size: 1.2em; border: none; margin: 0; padding: 1.5px 0 2px 4px;\">'''Sister projects'''</h1></div>\n"
//                        + "|-\n" + "|\n" + "TEST2\n" + "|}\n" + "|}",
//                        wikiModel.parseTemplates(
//                                "{{Main Page panel|\n"
//                        + "{{Main Page subpanel|column=both|title=Knowledge groups|1=\n"
//                        + "TEST1\n" + "}}\n"
//                        + "|\n"
//                        + "{{Main Page subpanel|column=both|title=Sister projects|1=\n"
//                        + "TEST2\n"
//                        + "}}\n"
//                        + "}}"));
//    }

    @Test public void testIssue77_003a() {
        assertThat(wikiModel.parseTemplates("{{1x1y_opt|test|foo{{!}}bar}}\n")).isEqualTo("1test2foo|bar\n");
    }

    @Test public void testIssue77_003b() {
        assertThat(wikiModel.parseTemplates("{{1x1y_opt|1=test|2=foo{{!}}bar}}\n")).isEqualTo("1test2foo|bar\n");
    }

    @Test public void testIssue81_001() {
        assertThat(wikiModel.parseTemplates(" {{{1|April 14}}} ")).isEqualTo(" April 14 ");
    }

// time dependent tests
//    @Test public void testIssue81_002() {
//        assertEquals("104", wikiModel.parseTemplates("{{#time:z|{{{1|April 14}}}}}"));
//    }
// time dependent tests
//    @Test public void testIssue82_001() {
//        assertEquals("105", wikiModel.parseTemplates("{{#expr:{{#time:z|{{{1|April 14}}}}}+1}}"));
//    }

    @Test public void testIssue82_002() {
        assertThat(wikiModel.parseTemplates("{{ordinal|105}}")).isEqualTo("105th");
    }

// time dependent tests
//    @Test public void testIssue82_003() {
//        assertEquals("105th", wikiModel.parseTemplates("{{ordinal|{{#expr:{{#time:z|{{{1|April 14}}}}}+1}}}}"));
//    }
// time dependent tests
//    @Test public void testIssue82_004() {
//        assertEquals("105", wikiModel.parseTemplates("{{subst:#expr:{{#time:z|{{{1|April 14}}}}}+1}}"));
//    }

    @Test public void testTemplateMain() {
        assertThat(wikiModel.parseTemplates("{{Main|Demographics of Pakistan|Pakistani people}}")).isEqualTo("<div class=\"rellink<nowiki> </nowiki>relarticle mainarticle\">Main articles: [[Demographics of Pakistan|Demographics of Pakistan]]&#32;and&#32;[[Pakistani people|Pakistani people]]</div>");
    }

    @Test public void testTemplateSeeAlso() {
        assertThat(wikiModel.parseTemplates("{{See also|Ethnic groups in Pakistan|Religion in Pakistan}}")).isEqualTo("<div class=\"rellink<nowiki> </nowiki>boilerplate seealso\">See also: [[:Ethnic groups in Pakistan]]&nbsp;and [[:Religion in Pakistan]]</div>");
    }

    @Test public void testTemplateNavbox() {
        assertThat(wikiModel
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
                        + "[[fr:Modèle:Palette Critics Choice Awards]]\n" + "[[ja:Template:ナショナル・ボード・オブ・レビュー賞]]\n" + "</noinclude>\n" + "")).isEqualTo("<table cellspacing=\"0\" class=\"navbox\" style=\"border-spacing:0;;\"><tr><td style=\"padding:2px;\"><table cellspacing=\"0\" class=\"nowraplinks  collapsible autocollapse navbox-inner\" style=\"border-spacing:0;background:transparent;color:inherit;;\"><tr><th scope=\"col\" style=\";\" class=\"navbox-title\" colspan=2><div class=\"noprint plainlinks hlist navbar mini\" style=\"\"><ul><li class=\"nv-view\">[[Template:National Board of Review Award for Best Actor|<span title=\"View this template\" style=\";;background:none transparent;border:none;\">v</span>]]</li><li class=\"nv-talk\">[[Template_talk:National Board of Review Award for Best Actor|<span title=\"Discuss this template\" style=\";;background:none transparent;border:none;\">t</span>]]</li><li class=\"nv-edit\">[http://en.wikipedia.org/w/index.php?title=Template%3ANational+Board+of+Review+Award+for+Best+Actor&amp;action=edit <span title=\"Edit this template\" style=\";;background:none transparent;border:none;\">e</span>]</li></ul></div><div class=\"\" style=\"font-size:110%;\">\n" +
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
                "");

    }

    @Test public void testTemplateNavbar() {
        assertThat(wikiModel
                .parseTemplates("{{Navbar|Screen Actors Guild Award for Outstanding Performance by a Cast in a Motion Picture (1995–2000)}}\n")).isEqualTo("<div class=\"noprint plainlinks hlist navbar \" style=\"\"><span style=\"word-spacing:0;\">This box: </span><ul><li class=\"nv-view\">[[Template:Screen Actors Guild Award for Outstanding Performance by a Cast in a Motion Picture (1995–2000)|<span title=\"View this template\" style=\"\">view</span>]]</li><li class=\"nv-talk\">[[Template_talk:Screen Actors Guild Award for Outstanding Performance by a Cast in a Motion Picture (1995–2000)|<span title=\"Discuss this template\" style=\"\">talk</span>]]</li><li class=\"nv-edit\">[http://en.wikipedia.org/w/index.php?title=Template%3AScreen+Actors+Guild+Award+for+Outstanding+Performance+by+a+Cast+in+a+Motion+Picture+%281995%E2%80%932000%29&amp;action=edit <span title=\"Edit this template\" style=\"\">edit</span>]</li></ul></div>\n"
                + "");
    }

    @Test public void testWeather01() {
        assertThat(wikiModel.parseTemplates("{{WeatherVal01\n" + "|show=1\n" + "|jan1=10\n" + "}}\n")).isEqualTo("1\n");
    }

    @Test public void testWeather02() {
        assertThat(wikiModel.parseTemplates("{{WeatherVal02\n" + "|show=1\n" + "|jan1=10\n" + "}}\n")).isEqualTo("10\n");
    }

    @Test public void testWeather03() {
        assertThat(wikiModel.parseTemplates("{{WeatherVal03\n" + "|show=1\n" + "|jan1=10\n" + "}}\n")).isEqualTo("10\n");
    }

    @Test public void testWeather04() {
        assertThat(wikiModel.parseTemplates("{{WeatherVal03\r\n" + "|show=1\r\n" + "|jan1=10\r\n" + "}}\n")).isEqualTo("10\n");
    }

    @Test public void testWeather05() {
        assertThat(wikiModel.parseTemplates("{{WeatherVal03\n\r" + "|show=1\n\r" + "|jan1=10\n\r" + "}}\n")).isEqualTo("10\n" + "");
    }

    @Test public void testTemplateLastTilde01() {
        assertThat(wikiModel.parseTemplates("{{TestTemplateName01~}}")).isEqualTo("[[:Template:TestTemplateName01~]]" + "");
    }

    @Test public void testShouldNotBeParsed01() {
        wikiModel.parseTemplates("{{#switch:TestPage\n|OtherPage={{ShouldNotBeParsed}}\n|#default={{ShouldBeParsed}}\n}}");
        assertThat(wikiModel.getTemplates()).contains("ShouldBeParsed");
        assertThat(wikiModel.getTemplates()).doesNotContain("ShouldNotBeParsed");
    }

    @Test public void testShouldNotBeParsed02() {
        wikiModel
                .parseTemplates("{{#switch:TestPage\n|TestPage={{ShouldBeParsed}}\n|OtherPage={{ShouldNotBeParsed1}}\n|#default={{ShouldNotBeParsed2}}\n}}");
        assertThat(wikiModel.getTemplates()).contains("ShouldBeParsed");
        assertThat(wikiModel.getTemplates()).doesNotContain("ShouldNotBeParsed1");
        assertThat(wikiModel.getTemplates()).doesNotContain("ShouldNotBeParsed2");
    }

    @Test public void testShouldNotBeParsed03() {
        wikiModel
                .parseTemplates("{{#switch:TestPage\n|OtherPage={{ShouldNotBeParsed1}}\n|TestPage={{ShouldBeParsed}}\n|#default={{ShouldNotBeParsed2}}\n}}");
        assertThat(wikiModel.getTemplates()).contains("ShouldBeParsed");
        assertThat(wikiModel.getTemplates()).doesNotContain("ShouldNotBeParsed1");
        assertThat(wikiModel.getTemplates()).doesNotContain("ShouldNotBeParsed2");
    }

    @Test public void testShouldNotBeParsed04() {
        wikiModel
                .parseTemplates("{{#switch:TestPage\n|#default={{ShouldNotBeParsed2}}\n|OtherPage={{ShouldNotBeParsed1}}\n|TestPage={{ShouldBeParsed}}\n}}");
        assertThat(wikiModel.getTemplates()).contains("ShouldBeParsed");
        assertThat(wikiModel.getTemplates()).doesNotContain("ShouldNotBeParsed1");
        assertThat(wikiModel.getTemplates()).doesNotContain("ShouldNotBeParsed2");
    }

    @Test public void testShouldNotBeParsed05() {
        wikiModel.parseTemplates("{{#switch:TestPage\n|TestPage={{ShouldBeParsed}}\n|#default={{ShouldNotBeParsed}}\n}}");
        assertThat(wikiModel.getTemplates()).contains("ShouldBeParsed");
        assertThat(wikiModel.getTemplates()).doesNotContain("ShouldNotBeParsed");
    }

    @Test public void testMissingImplicitParameter01() {
        assertThat(wikiModel.parseTemplates("{{1x||2}}")).isEqualTo("");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces01() {
        assertThat(wikiModel.parseTemplates("{{1x| a }}")).isEqualTo(" a ");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces02() {
        assertThat(wikiModel.parseTemplates("{{1x|1= a }}")).isEqualTo("a");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces03() {
        assertThat(wikiModel.parseTemplates("p{{2x|{{nln}}}}q")).isEqualTo("p\n\nq");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces04() {
        assertThat(wikiModel.parseTemplates("p{{2x|1={{nln}}}}q")).isEqualTo("pq");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces05() {
        assertThat(wikiModel.parseTemplates("p{{#if:x|{{2x|{{nln}}}}}}q")).isEqualTo("pq");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces06() {
        assertThat(wikiModel.parseTemplates("p{{2x|{{#if:x|{{nln}}}}}}q")).isEqualTo("pq");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces07() {
        assertThat(wikiModel.parseTemplates("p{{2x|{{spc}}}}q")).isEqualTo("p  q");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces08() {
        assertThat(wikiModel.parseTemplates("p{{2x|1={{spc}}}}q")).isEqualTo("pq");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces09() {
        assertThat(wikiModel.parseTemplates("p{{#if:x|{{2x|{{spc}}}}}}q")).isEqualTo("pq");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces10() {
        assertThat(wikiModel.parseTemplates("p{{2x|{{#if:x|{{spc}}}}}}q")).isEqualTo("pq");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces11() {
        assertThat(wikiModel.parseTemplates("{{2x\n|\n1 =\npq\n}}")).isEqualTo("pqpq");
    }

    /**
     * Some tests from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Newlines_and_spaces#Trimming_on_expansion"
     * > Help:Newlines and spaces - Trimming on expansion</a>
     */
    @Test public void testNewlineSpaces12() {
        assertThat(wikiModel.parseTemplates("{{#switch:\n2\n|\n1 =\npq\n|\n2 =\nrs\n|\n3 =\ntu\n}}")).isEqualTo("rs");
    }

    @Test public void testCategory001() {
        assertThat(wikiModel.render("[[Category:Main Page]]", false)).isEqualTo("");
        HashMap<String, String> expectedCategories = new HashMap<>();
        expectedCategories.put("Main Page", "Category:Main Page");
        assertThat(wikiModel.getCategories()).isEqualTo(expectedCategories);
    }
}

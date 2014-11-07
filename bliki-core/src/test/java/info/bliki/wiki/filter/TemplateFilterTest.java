package info.bliki.wiki.filter;

import info.bliki.wiki.model.WikiModel;
import org.junit.Test;

import java.util.HashSet;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateFilterTest extends FilterTestSupport {
    private static final String SELF_RECURSION = "Line1\n\n{{SELF_RECURSION}}";
    private static final String INDIRECT_SELF_RECURSION1 = "INDIRECT_SELF_RECURSION1\n\n{{INDIRECT_SELF_RECURSION2}}";
    private static final String INDIRECT_SELF_RECURSION2 = "INDIRECT_SELF_RECURSION2\n\n{{INDIRECT_SELF_RECURSION1}}";
    private static final String INDIRECT_SELF_RECURSION1a = "INDIRECT_SELF_RECURSION1a\n\n{{INDIRECT_SELF_RECURSION2a|{{#if:{{{1|}}}|{{#expr:{{{1}}}+1}}|1}}}}";
    private static final String INDIRECT_SELF_RECURSION2a = "INDIRECT_SELF_RECURSION2a\n\n{{INDIRECT_SELF_RECURSION1a|{{#if:{{{1|}}}|{{#expr:{{{1}}}+1}}|1}}}}";
    private static final String NAVBOX_STRING = "{{Navbox\n" + "|name  = AcademyAwardBestActor 1981-2000\n"
            + "|title = [[Academy Award for Best Actor|Academy Award for]] [[Academy Award for Best Actor#1980s|Best Actor]]\n"
            + "|titlestyle = background: #EEDD82\n" + "|list1 = <div>\n" + "{{nowrap|[[Henry Fonda]] (1981)}}{{·}}\n"
            + "{{nowrap|[[Ben Kingsley]] (1982)}}{{·}}\n" + "{{nowrap|[[Robert Duvall]] (1983)}}{{·}}\n"
            + "{{nowrap|[[F. Murray Abraham]] (1984)}}{{·}}\n" + "{{nowrap|[[William Hurt]] (1985)}}{{·}}\n"
            + "{{nowrap|[[Paul Newman]] (1986)}}{{·}}\n" + "{{nowrap|[[Michael Douglas]] (1987)}}{{·}}\n"
            + "{{nowrap|[[Dustin Hoffman]] (1988)}}{{·}}\n" + "{{nowrap|[[Daniel Day-Lewis]] (1989)}}{{·}}\n"
            + "{{nowrap|[[Jeremy Irons]] (1990)}}{{·}}\n" + "{{nowrap|[[Anthony Hopkins]] (1991)}}{{·}}\n"
            + "{{nowrap|[[Al Pacino]] (1992)}}{{·}}\n" + "{{nowrap|[[Tom Hanks]] (1993)}}{{·}}\n"
            + "{{nowrap|[[Tom Hanks]] (1994)}}{{·}}\n" + "{{nowrap|[[Nicolas Cage]] (1995)}}{{·}}\n"
            + "{{nowrap|[[Geoffrey Rush]] (1996)}}{{·}}\n" + "{{nowrap|[[Jack Nicholson]] (1997)}}{{·}}\n"
            + "{{nowrap|[[Roberto Benigni]] (1998)}}{{·}}\n" + "{{nowrap|[[Kevin Spacey]] (1999)}}{{·}}\n"
            + "{{nowrap|[[Russell Crowe]] (2000) }}\n" + "----\n"
            + "{{nowrap|[[:Template:Academy Award Best Actor|Complete List]]}}{{·}}\n"
            + "{{nowrap|[[:Template:AcademyAwardBestActor 1927-1940|(1928–1940)]]}}{{·}}\n"
            + "{{nowrap|[[:Template:AcademyAwardBestActor 1941-1960|(1941–1960)]]}}{{·}}\n"
            + "{{nowrap|[[:Template:AcademyAwardBestActor 1961-1980|(1961–1980)]]}}{{·}}\n"
            + "{{nowrap|[[:Template:AcademyAwardBestActor 1981-2000|\'\'\'(1981–2000)\'\'\']]}}{{·}}\n"
            + "{{nowrap|[[:Template:AcademyAwardBestActor 2001-2020|(2001-present)]]}}\n" + "</div>\n" + "}}<noinclude>\n" + "\n"
            + "[[Category:Academy Award for Best Actor templates| 1981-2000]]\n" + "</noinclude>";


    private static final String TEST_STRING_02 = " {{#if:{{{cat|\n" + "{{{category|}}}}}}|a category|{{#if:{{{mul|{{{dab|\n"
            + "{{{disambiguation|}}}}}}}}}|articles|{{#if:{{{mulcat|}}}|categories|an\n" + "article}}}}}} on:\n";


    /**
     * Issue124 - Image URL parsing broken in some cases
     */
    @Test public void testTemplateIssue124_001() {
        final String rawWikiText = "{{AdT-Vorschlag\n"
                + "|DATUM            = 01.09.2012\n"
                + "|LEMMA            = Nashörner\n"
                + "|BILD             = File:Waterberg Nashorn1.jpg\n"
                + "|BILDGROESSE      = 150px\n"
                + "|BILDUMRANDUNG    = \n"
                + "|BILDBESCHREIBUNG = Breitmaulnashörner in Namibia\n"
                + "|TEASERTEXT       = Die '''[[Nashörner]]''' (Rhinocerotidae) oder auch '''Rhinozerosse''' zählen zu den markantesten Säugetieren mit ihrem großen Kopf und den namengebenden ein bis zwei Hörnern.}}";
        final String expected = "\n"
                + "<div style=\"float:left; padding-top:0.5em; padding-right:0.5em;\"><a class=\"image\" href=\"http://www.bliki.info/wiki/File:150px-Waterberg_Nashorn1.jpg\" title=\"Breitmaulnashörner in Namibia\"><img src=\"http://www.bliki.info/wiki/150px-Waterberg_Nashorn1.jpg\" alt=\"Breitmaulnashörner in Namibia\" width=\"150\" />\n"
                + "</a></div>\n"
                + "<p>Die <b><a href=\"http://www.bliki.info/wiki/Nash%C3%B6rner\" title=\"Nashörner\">Nashörner</a></b> (Rhinocerotidae) oder auch <b>Rhinozerosse</b> zählen zu den markantesten Säugetieren mit ihrem großen Kopf und den namengebenden ein bis zwei Hörnern. <small><a href=\"http://www.bliki.info/wiki/Nash%C3%B6rner\" title=\"Nashörner\">mehr</a></small></p>";
        assertThat(wikiModel.render(rawWikiText)).isEqualTo(expected);
        WikiModel germanWikiTextModel = newWikiTestModel(Locale.GERMAN);
        assertThat(germanWikiTextModel.render(rawWikiText)).isEqualTo(expected);
    }

    /**
     * Issue124 - Image URL parsing broken in some cases
     */
    @Test public void testTemplateIssue124_002() {
        final String rawWikiText = "[[File:Waterberg Nashorn1.jpg|150px]]";
        final String expected = "\n"
                + "<p><a class=\"image\" href=\"http://www.bliki.info/wiki/File:150px-Waterberg_Nashorn1.jpg\" ><img src=\"http://www.bliki.info/wiki/150px-Waterberg_Nashorn1.jpg\" width=\"150\" />\n</a></p>";
        assertThat(wikiModel.render(rawWikiText)).isEqualTo(expected);
        WikiModel germanWikiTextModel = newWikiTestModel(Locale.GERMAN);
        assertThat(germanWikiTextModel.render(rawWikiText)).isEqualTo(expected);
    }

    @Test public void testTemplate06() {
        assertThat(wikiModel.render("start- {{ifeq|5.0|+5}} -end", false)).isEqualTo("\n" + "<p>start- 5.0 equals +5 -end</p>");
    }

    @Test public void testTemplate09() {
        assertThat(wikiModel.render("start- {{ifeq|test|Test}} -end", false)).isEqualTo("\n" + "<p>start- test is not equal Test -end</p>");
    }

    @Test public void testTemplateCall3() {
        assertThat(wikiModel.render("{{templ1\n"
                + " | a = Test1\n" + " | {{templ2|sdfsf|klj}} \n" + "}}\n" + "", false)).isEqualTo("\n" + "<p>b) First: Test1 Second:  c) First: sdfsf Second: klj </p>\n" + "");
    }

    @Test public void testSwitch001() {
        assertThat(wikiModel.render("{{Templ1/{{ #switch: imperative  | ind | ind&}}}}", false)).isEqualTo("\n"
                + "<p><a href=\"http://www.bliki.info/wiki/Template:Templ1/ind%26\" title=\"Template:Templ1/ind&amp;\">Template:Templ1/ind&#38;</a></p>");
    }

    @Test public void testNonExistingTemplate01() {
        assertThat(wikiModel.render("==Other areas of Wikipedia==\n" + "{{WikipediaOther}}<!--Template:WikipediaOther-->", false)).isEqualTo("<h2><span class=\"mw-headline\" id=\"Other_areas_of_Wikipedia\">Other areas of Wikipedia</span></h2>\n"
                + "<p><a href=\"http://www.bliki.info/wiki/Template:WikipediaOther\" title=\"Template:WikipediaOther\">Template:WikipediaOther</a></p>");
    }

    @Test public void testNonExistingTemplate02() {
        assertThat(wikiModel.render("start {{NonExistingTemplate}} end", false)).isEqualTo("\n"
                + "<p>start <a href=\"http://www.bliki.info/wiki/Template:NonExistingTemplate\" title=\"Template:NonExistingTemplate\">Template:NonExistingTemplate</a> end</p>");
    }

    private void nonExistingTemplateTestLink(String wikiText, String linkTitle, String templateName) {
        String urlTitle = Character.toUpperCase(linkTitle.charAt(0)) + linkTitle.substring(1);
        assertThat(wikiModel.render(wikiText, false)).isEqualTo("\n<p><a href=\"http://www.bliki.info/wiki/" + urlTitle + "\" title=\"" + urlTitle + "\">" + linkTitle
                + "</a></p>");
        if (templateName != null) {
            assertThat(wikiModel.getTemplates()).contains(templateName);
            assertThat(wikiModel.getIncludes()).doesNotContain(linkTitle);
            assertThat(wikiModel.getIncludes()).doesNotContain(linkTitle);
        } else {
            assertThat(wikiModel.getTemplates()).doesNotContainNull();
            assertThat(wikiModel.getIncludes()).contains(linkTitle);
        }
    }

    @Test public void testNonExistingTemplate03() {
        nonExistingTemplateTestLink("{{Help:NonExistingPage}}", "Help:NonExistingPage", null);
        nonExistingTemplateTestLink("{{NonExistingTemplate}}", "Template:NonExistingTemplate", "NonExistingTemplate");
        nonExistingTemplateTestLink("{{:NonExistingTemplate}}", "NonExistingTemplate", null);
        nonExistingTemplateTestLink("{{:Template:Help:test123456789}}", "Template:Help:test123456789", "Help:test123456789");
        nonExistingTemplateTestLink("{{:foo:Help:test123456789}}", "foo:Help:test123456789", null);
        nonExistingTemplateTestLink("{{:Help:Test123456789}}", "Help:Test123456789", null);
        nonExistingTemplateTestLink("{{Help:Test123456789}}", "Help:Test123456789", null);
        nonExistingTemplateTestLink("{{:foo:test123456789}}", "foo:test123456789", null);
        nonExistingTemplateTestLink("{{foo:test123456789}}", "Template:foo:test123456789", "foo:test123456789");
        nonExistingTemplateTestLink("{{:PAGESIZE:Help:test}}", "PAGESIZE:Help:test", null);
        assertThat(wikiModel.render("{{::Help:test123456789}}", false)).isEqualTo("\n<p>{{::Help:test123456789}}</p>");
        assertThat(wikiModel.render("{{PAGESIZE:Help:test}}", false)).isEqualTo("\n<p>PAGESIZE</p>");
    }

    @Test public void testTemplateCall1() {
        assertThat(wikiModel.render("start-{{:Include Page}}-end", false)).isEqualTo("\n" + "<p>start-an include page-end</p>");
    }

    @Test public void testTemplateCall2() {
        assertThat(wikiModel.render(
                "start-{{templ1|a=3|b}}-end start-{{templ2|sdfsf|klj}}-end", false)).isEqualTo("\n" + "<p>start-b) First: 3 Second: b-end start-c) First: sdfsf Second: klj-end</p>");
    }

    @Test public void testTemplateCall4() {
        assertThat(wikiModel.render("{{tl|example}}", false)).isEqualTo("\n"
                + "<p>[[:Template:<a href=\"http://www.bliki.info/wiki/Template:example\" title=\"Template:example\">example</a>]]</p>");
    }

    @Test public void testTemplateCall4a() {
        assertThat(wikiModel.render("{{tl}}", false)).isEqualTo("\n" + "<p>[[:Template:[[Template:{{{1}}}|{{{1}}}]]]]</p>");
    }

    @Test public void testTemplateCall5() {
        assertThat(wikiModel.render("({{pron-en|dəˌpeʃˈmoʊd}})", false)).isEqualTo("\n"
                + "<p>(pronounced <span class=\"IPA\" title=\"Pronunciation in the International Phonetic Alphabet (IPA)\"><a href=\"http://www.bliki.info/wiki/WP:IPA_for_English\" title=\"WP:IPA for English\">/dəˌpeʃˈmoʊd/</a></span>)</p>");
    }

    @Test public void testTemplateImage1() {
        assertThat(wikiModel
                .render(
                        "{|\n"
                                + "! | <h2 style=\"background:#cedff2;\">In the news</h2>\n"
                                + "|-\n"
                                + "| style=\"color:#000; padding:2px 5px;\" | <div id=\"mp-itn\">{{Image\n"
                                + " |image  = Yoshihiko Noda-1.jpg\n"
                                + " |title  = Yoshihiko Noda\n"
                                + "}}\n"
                                + "The ruling Democratic Party of Japan selects '''Yoshihiko Noda''' ''(pictured)'' as the country's new prime minister, following the resignation of Naoto Kan\n"
                                + "</div>\n" + "|}", false
                )).isEqualTo("\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table>\n"
                + "<tr>\n"
                + "<th><h2 style=\"background:#cedff2;\">In the news</h2></th></tr>\n"
                + "<tr>\n"
                + "<td style=\"color:#000; padding:2px 5px;\">\n"
                + "<div id=\"mp-itn\"><a class=\"image\" href=\"http://www.bliki.info/wiki/File:Yoshihiko_Noda-1.jpg\" title=\"Yoshihiko Noda\"><img src=\"http://www.bliki.info/wiki/Yoshihiko_Noda-1.jpg\" alt=\"Yoshihiko Noda\" width=\"220\" />\n"
                + "</a>\n"
                + "The ruling Democratic Party of Japan selects <b>Yoshihiko Noda</b> <i>(pictured)</i> as the country&#39;s new prime minister, following the resignation of Naoto Kan\n"
                + "</div></td></tr></table></div>");
    }

    @Test public void testTemplateNowiki() {
        assertThat(wikiModel.render("start-<nowiki>{{templ1|a=3|b}}-</noWiKi>end", false)).isEqualTo("\n" + "<p>start-{{templ1|a=3|b}}-end</p>");
    }

    @Test public void testTemplateParameter01() {
        assertThat(wikiModel.render("start-{{Test|arg1|arg2}}-end", false)).isEqualTo("\n" + "<p>start-a) First: arg1 Second: arg2-end</p>");
    }

    @Test public void testTemplateParameter02() {
        assertThat(wikiModel
                .render(
                        "start- {{cite web|url=http://www.etymonline.com/index.php?search=hello&searchmode=none|title=Online Etymology Dictionary}} -end",
                        false)).isEqualTo("\n"
                + "<p>start- <i><a class=\"external text\" href=\"http://www.etymonline.com/index.php?search=hello&#38;searchmode=none\" rel=\"nofollow\">Online Etymology Dictionary</a></i>. -end</p>");
    }

    @Test public void testTemplateParameter03() {
        assertThat(wikiModel.render("start- {{reflist|2}} -end", false)).isEqualTo("\n" + "<p>start- </p>\n"
                + "<div class=\"references-small\" style=\"-moz-column-count:2; -webkit-column-count:2; column-count:2;\"></div> -end");
    }

    @Test public void testTemplate04() {
        assertThat(wikiModel.render("{{#ifeq: A | B |A equals B |A is not equal B}}", false)).isEqualTo("\n" + "<p>A is not equal B</p>");
    }

    @Test public void testTemplate05() {
        assertThat(wikiModel.render("start- {{ifeq|A|B}} -end", false)).isEqualTo("\n" + "<p>start- A is not equal B -end</p>");
    }

    @Test public void testTemplate07() {
        assertThat(wikiModel.render("start- {{ifeq|5.001|+5}} -end", false)).isEqualTo("\n" + "<p>start- 5.001 is not equal +5 -end</p>");
    }

    @Test public void testTemplate08() {
        assertThat(wikiModel.render("start- {{ifeq|test|test}} -end", false)).isEqualTo("\n" + "<p>start- test equals test -end</p>");
    }

    @Test public void testTemplate10() {
        assertThat(wikiModel.render("{{{x| }}}", false)).isEqualTo("");
    }

    @Test public void testTemplate11() {
        assertThat(wikiModel.render("{{{title}}}", false)).isEqualTo("\n" + "<p>{{{title}}}</p>");
    }

    @Test public void testTemplate12() {
        assertThat(wikiModel.render("{{Infobox_Software\n" + "|name = JAMWiki\n" + "|logo = \n" + "|caption =\n" + "\n"
                + "|developer = \n" + "|latest_release_version = 0.6.5\n" + "|latest_release_date = [[March 16]], [[2008]]\n"
                + "|latest preview version = 0.6.5 \n" + "|latest preview date = \n" + "|operating_system = [[Cross-platform]]\n"
                + "|genre = [[wiki software|Wiki software]]\n" + "|license = [[GNU Lesser General Public License|LGPL]]\n"
                + "|website = [http://www.jamwiki.org/ JAMWiki wiki]\n" + "}}\n", false)).isEqualTo("\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table cellspacing=\"5\" class=\"infobox\" style=\"width: 21em; font-size: 90%; text-align: left;\">\n"
                + "<tr>\n"
                + "<th colspan=\"2\" style=\"text-align: center; font-size: 130%;\">JAMWiki</th></tr>\n"
                + "<tr>\n"
                + "<th><a href=\"http://www.bliki.info/wiki/Software_release_life_cycle\" title=\"Software release life cycle\">Latest release</a></th>\n"
                + "<td>0.6.5 / <a href=\"http://www.bliki.info/wiki/March_16\" title=\"March 16\">March 16</a>, <a href=\"http://www.bliki.info/wiki/2008\" title=\"2008\">2008</a></td></tr>\n"
                + "<tr>\n"
                + "<th><a href=\"http://www.bliki.info/wiki/Software_release_life_cycle\" title=\"Software release life cycle\">Preview release</a></th>\n"
                + "<td>0.6.5  </td></tr>\n"
                + "<tr>\n"
                + "<th><a href=\"http://www.bliki.info/wiki/Operating_system\" title=\"Operating system\">OS</a></th>\n"
                + "<td><a href=\"http://www.bliki.info/wiki/Cross-platform\" title=\"Cross-platform\">Cross-platform</a></td></tr>\n"
                + "<tr>\n"
                + "<th><a href=\"http://www.bliki.info/wiki/List_of_software_categories\" title=\"List of software categories\">Genre</a></th>\n"
                + "<td><a href=\"http://www.bliki.info/wiki/Wiki_software\" title=\"Wiki software\">Wiki software</a></td></tr>\n"
                + "<tr>\n"
                + "<th><a href=\"http://www.bliki.info/wiki/Software_license\" title=\"Software license\">License</a></th>\n"
                + "<td><a href=\"http://www.bliki.info/wiki/GNU_Lesser_General_Public_License\" title=\"GNU Lesser General Public License\">LGPL</a></td></tr>\n"
                + "<tr>\n"
                + "<th><a href=\"http://www.bliki.info/wiki/Website\" title=\"Website\">Website</a></th>\n"
                + "<td><a class=\"external text\" href=\"http://www.jamwiki.org/\" rel=\"nofollow\">JAMWiki wiki</a></td></tr></table></div>\n"
                + "");
    }

    @Test public void testTemplateParameter13() {
        assertThat(wikiModel
                .render(
                        "{| class=\"wikitable\"\n"
                                + "|+Versionsgeschichte von JavaScript<ref>{{cite web|author=John Resig |url=http://ejohn.org/blog/versions-of-javascript |title=Versions of JavaScript |publisher=Ejohn.org |date= |accessdate=2009-05-19}}</ref>\n"
                                + "|-\n"
                                + "! Version !! Release !! Entsprechung !! Netscape Navigator !! Mozilla Firefox !! Internet Explorer !! Opera !! Safari !! Google Chrome\n"
                                + "|}\n" + " <references/>\n", false
                )).isEqualTo("\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table class=\"wikitable\">\n"
                + "<caption>Versionsgeschichte von JavaScript<sup id=\"_ref-1\" class=\"reference\"><a href=\"#_note-1\" title=\"\">[1]</a></sup></caption>\n"
                + "<tr>\n"
                + "<th>Version </th>\n"
                + "<th>Release </th>\n"
                + "<th>Entsprechung </th>\n"
                + "<th>Netscape Navigator </th>\n"
                + "<th>Mozilla Firefox </th>\n"
                + "<th>Internet Explorer </th>\n"
                + "<th>Opera </th>\n"
                + "<th>Safari </th>\n"
                + "<th>Google Chrome</th></tr></table></div>\n"
                + "<pre>"
                + "<ol class=\"references\">\n"
                + "<li id=\"_note-1\"><b><a href=\"#_ref-1\" title=\"\">&#8593;</a></b> John Resig. <i><a class=\"external text\" href=\"http://ejohn.org/blog/versions-of-javascript\" rel=\"nofollow\">Versions of JavaScript</a></i>. Ejohn.org. Abgerufen am <a href=\"http://www.bliki.info/wiki/Template:safesubst:\" title=\"Template:safesubst:\">Template:safesubst:</a>.</li>\n</ol>\n"
                + "</pre>");
    }

    @Test public void testNestedIf02() {
        assertThat(wikiModel.render(TEST_STRING_02, false)).isEqualTo("\n" + "<pre>an\n</pre>\n" + "<p>article on:\n" + "</p>" + "");
    }

    @Test public void testParserFunctionLC001() {
        assertThat(wikiModel.render("A {{lc: Lower Case}} text", false)).isEqualTo("\n" + "<p>A lower case text</p>");
    }

    @Test public void testParserFunctionTag001() {
        assertThat(wikiModel
                .render("{{#tag:ref|'''a simple test'''}}{{#tag:references}}", false)).isEqualTo("\n"
                + "<p><sup id=\"_ref-1\" class=\"reference\"><a href=\"#_note-1\" title=\"\">[1]</a></sup></p><ol class=\"references\">\n"
                + "<li id=\"_note-1\"><b><a href=\"#_ref-1\" title=\"\">&#8593;</a></b> <b>a simple test</b></li>\n</ol>");
    }

    @Test public void testNavbox() {
        assertThat(wikiModel.render(NAVBOX_STRING,
                false)).isEqualTo("\n"
                + "<table cellspacing=\"0\" class=\"navbox\" style=\"border-spacing:0;;\">\n"
                + "\n"
                + "<tr>\n"
                + "\n"
                + "<td style=\"padding:2px;\">\n"
                + "<table cellspacing=\"0\" class=\"nowraplinks  collapsible autocollapse navbox-inner\" style=\"border-spacing:0;background:transparent;color:inherit;;\">\n"
                + "\n"
                + "<tr>\n"
                + "\n"
                + "<th class=\"navbox-title\" colspan=\"2\" scope=\"col\" style=\";background: #EEDD82\">\n"
                + "\n"
                + "<div class=\"noprint plainlinks hlist navbar mini\">\n"
                + "<ul>\n"
                + "\n"
                + "<li class=\"nv-view\"><a href=\"http://www.bliki.info/wiki/Template:AcademyAwardBestActor_1981-2000\" title=\"Template:AcademyAwardBestActor 1981-2000\"><span style=\";background: #EEDD82;background:none transparent;border:none;\" title=\"View this template\">v</span></a></li>\n"
                + "<li class=\"nv-talk\"><a href=\"http://www.bliki.info/wiki/Template_talk:AcademyAwardBestActor_1981-2000\" title=\"Template talk:AcademyAwardBestActor 1981-2000\"><span style=\";background: #EEDD82;background:none transparent;border:none;\" title=\"Discuss this template\">t</span></a></li>\n"
                + "<li class=\"nv-edit\"><a class=\"external text\" href=\"http://en.wikipedia.org/w/index.php?title=Template%3AAcademyAwardBestActor+1981-2000&#38;action=edit\" rel=\"nofollow\"><span style=\";background: #EEDD82;background:none transparent;border:none;\" title=\"Edit this template\">e</span></a></li>\n"
                + "</ul></div>\n"
                + "<div style=\"font-size:110%;\">\n"
                + "<p><a href=\"http://www.bliki.info/wiki/Academy_Award_for_Best_Actor\" title=\"Academy Award for Best Actor\">Academy Award for</a> <a href=\"http://www.bliki.info/wiki/Academy_Award_for_Best_Actor#1980s\" title=\"Academy Award for Best Actor\">Best Actor</a></p></div>\n"
                + "</th>\n"
                + "</tr>\n"
                + "<tr style=\"height:2px;\">\n"
                + "\n"
                + "</tr>\n"
                + "<tr>\n"
                + "\n"
                + "<td class=\"navbox-list navbox-odd \" colspan=\"2\" style=\"width:100%;padding:0px;;;\">\n"
                + "<div style=\"padding:0em 0.25em\">\n"
                + "<div>\n"
                + "<p><span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Henry_Fonda\" title=\"Henry Fonda\">Henry Fonda</a> (1981)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Ben_Kingsley\" title=\"Ben Kingsley\">Ben Kingsley</a> (1982)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Robert_Duvall\" title=\"Robert Duvall\">Robert Duvall</a> (1983)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/F._Murray_Abraham\" title=\"F. Murray Abraham\">F. Murray Abraham</a> (1984)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/William_Hurt\" title=\"William Hurt\">William Hurt</a> (1985)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Paul_Newman\" title=\"Paul Newman\">Paul Newman</a> (1986)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Michael_Douglas\" title=\"Michael Douglas\">Michael Douglas</a> (1987)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Dustin_Hoffman\" title=\"Dustin Hoffman\">Dustin Hoffman</a> (1988)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Daniel_Day-Lewis\" title=\"Daniel Day-Lewis\">Daniel Day-Lewis</a> (1989)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Jeremy_Irons\" title=\"Jeremy Irons\">Jeremy Irons</a> (1990)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Anthony_Hopkins\" title=\"Anthony Hopkins\">Anthony Hopkins</a> (1991)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Al_Pacino\" title=\"Al Pacino\">Al Pacino</a> (1992)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Tom_Hanks\" title=\"Tom Hanks\">Tom Hanks</a> (1993)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Tom_Hanks\" title=\"Tom Hanks\">Tom Hanks</a> (1994)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Nicolas_Cage\" title=\"Nicolas Cage\">Nicolas Cage</a> (1995)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Geoffrey_Rush\" title=\"Geoffrey Rush\">Geoffrey Rush</a> (1996)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Jack_Nicholson\" title=\"Jack Nicholson\">Jack Nicholson</a> (1997)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Roberto_Benigni\" title=\"Roberto Benigni\">Roberto Benigni</a> (1998)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Kevin_Spacey\" title=\"Kevin Spacey\">Kevin Spacey</a> (1999)</span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Russell_Crowe\" title=\"Russell Crowe\">Russell Crowe</a> (2000) </span></p><hr />\n"
                + "<p><span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Template:Academy_Award_Best_Actor\" title=\"Template:Academy Award Best Actor\">Complete List</a></span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Template:AcademyAwardBestActor_1927-1940\" title=\"Template:AcademyAwardBestActor 1927-1940\">(1928–1940)</a></span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Template:AcademyAwardBestActor_1941-1960\" title=\"Template:AcademyAwardBestActor 1941-1960\">(1941–1960)</a></span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Template:AcademyAwardBestActor_1961-1980\" title=\"Template:AcademyAwardBestActor 1961-1980\">(1961–1980)</a></span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Template:AcademyAwardBestActor_1981-2000\" title=\"Template:AcademyAwardBestActor 1981-2000\"><b>(1981–2000)</b></a></span><span style=\"font-weight:bold;\"> ·</span> \n"
                + "<span style=\"white-space:nowrap;\"><a href=\"http://www.bliki.info/wiki/Template:AcademyAwardBestActor_2001-2020\" title=\"Template:AcademyAwardBestActor 2001-2020\">(2001-present)</a></span>\n"
                + "</p></div></div></td>\n" + "</tr>\n" + "</table></td>\n" + "</tr>\n" + "</table>");
    }

    @Test public void test11() {
        assertThat(wikiModel
                .render(
                        "[[Template:AcademyAwardBestActor_1981-2000|<span title=\"View this template\" style=\";background: #EEDD82;border:none;;\">v</span>]]",
                        false)).isEqualTo("\n"
                + "<p><a href=\"http://www.bliki.info/wiki/Template:AcademyAwardBestActor_1981-2000\" title=\"Template:AcademyAwardBestActor 1981-2000\"><span style=\";background: #EEDD82;border:none;;\" title=\"View this template\">v</span></a></p>");
    }

    @Test public void testPipe001a() {
        assertThat(wikiModel.render("{{2x|Hello World\n" + "}}", false)).isEqualTo("\n" + "<p>Hello World\n" + "Hello World\n" + "</p>");
    }

    @Test public void testPipe001() {
        assertThat(wikiModel.render("{{2x|hello world" + "}} ", false)).isEqualTo("\n" + "<p>hello worldhello world </p>");
    }

    @Test public void testPipe002() {
        assertThat(wikiModel.render("{{2x|{{{!}} \n" + "{{!}} A \n" + "{{!}} B\n"
                + "{{!}}- \n" + "{{!}} C\n" + "{{!}} D\n" + "{{!}}}\n" + "}}", false)).isEqualTo("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table>\n" + "<tr>\n" + "<td>A </td>\n"
                + "<td>B</td></tr>\n" + "<tr>\n" + "<td>C</td>\n" + "<td>D</td></tr></table></div>\n" + "\n"
                + "<div style=\"page-break-inside: avoid;\">\n" + "<table>\n" + "<tr>\n" + "<td>A </td>\n" + "<td>B</td></tr>\n" + "<tr>\n"
                + "<td>C</td>\n" + "<td>D</td></tr></table></div>\n" + "");
    }

    @Test public void testPipe003() {
        assertThat(wikiModel.render("{{2x|{{2x|{{{!}} \n" + "{{!}} A \n"
                + "{{!}} B\n" + "{{!}}- \n" + "{{!}} C\n" + "{{!}} D\n" + "{{!}}}\n" + "}}}}", false)).isEqualTo("\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table>\n" + "<tr>\n" + "<td>A </td>\n"
                + "<td>B</td></tr>\n" + "<tr>\n" + "<td>C</td>\n" + "<td>D</td></tr></table></div>\n" + "\n"
                + "<div style=\"page-break-inside: avoid;\">\n" + "<table>\n" + "<tr>\n" + "<td>A </td>\n" + "<td>B</td></tr>\n" + "<tr>\n"
                + "<td>C</td>\n" + "<td>D</td></tr></table></div>\n" + "\n" + "<div style=\"page-break-inside: avoid;\">\n" + "<table>\n"
                + "<tr>\n" + "<td>A </td>\n" + "<td>B</td></tr>\n" + "<tr>\n" + "<td>C</td>\n" + "<td>D</td></tr></table></div>\n" + "\n"
                + "<div style=\"page-break-inside: avoid;\">\n" + "<table>\n" + "<tr>\n" + "<td>A </td>\n" + "<td>B</td></tr>\n" + "<tr>\n"
                + "<td>C</td>\n" + "<td>D</td></tr></table></div>\n" + "");
    }

    @Test public void testFurther() {
        assertThat(wikiModel.render("{{further|[[History of molecular biology]]}}", false)).isEqualTo("\n"
                + "<dl>\n"
                + "<dd><span class=\"boilerplate further\"><i>Further information: <a href=\"http://www.bliki.info/wiki/History_of_molecular_biology\" title=\"History of molecular biology\">History of molecular biology</a></i></span></dd>\n</dl>");
    }

    @Test public void testInvalidNoinclude() {
        assertThat(wikiModel.render("test123 start<noinclude>\n" + "test123 end", false)).isEqualTo("\n" + "<p>test123 start</p>");
    }

    @Test public void testInvalidIncludeonly() {
        assertThat(wikiModel.render("test123 start<includeonly>\n" + "test123 end",
                false)).isEqualTo("\n" + "<p>test123 start\n" + "test123 end</p>");
    }

    @Test public void testInvalidOnlyinclude() {
        assertThat(wikiModel.render("test123 start<onlyinclude>\n" + "test123 end", false)).isEqualTo("\n" + "<p>test123 end</p>");
    }

    @Test public void testIf_image_test() {
        assertThat(wikiModel.render("test {{If_image_test|  image =  test.jpg}} test123", false)).isEqualTo("\n"
                + "<p>test <a class=\"image\" href=\"http://www.bliki.info/wiki/File:220px-test.jpg\" ><img src=\"http://www.bliki.info/wiki/220px-test.jpg\" width=\"220\" />\n"
                + "</a> test123</p>");
    }

    @Test public void testMONTHNAME() {
        assertThat(wikiModel.render("test {{MONTHNAME | 10 }} test123", false)).isEqualTo("\n" + "<p>test October test123</p>");
    }

    @Test public void testMONTHNUMBER() {
        assertThat(wikiModel.render("test {{MONTHNUMBER | OctoBer }} test123", false)).isEqualTo("\n" + "<p>test 10 test123</p>");
    }

    @Test public void testInfoboxProgrammiersprachen() {
        assertThat(wikiModel
                .render(
                        "{{Infobox Programmiersprache"
                                + "|Name = JavaScript"
                                + "|Beschreibung = Skriptsprache"
                                + "|Paradigma = multiparadigmatisch"
                                + "|Erscheinungsjahr = 1995"
                                + "|Entwickler = [[Brendan Eich]]"
                                + "|AktuelleVersion = 1.8"
                                + "|AktuelleVersionFreigabeDatum = 2008"
                                + "|Typisierung = [[Schwache Typisierung|schwach]], [[Dynamische Typisierung|dynamisch]], [[Duck Typing|duck]]"
                                + "|Implementierung = [[SpiderMonkey]], [[Rhino]], [[SquirrelFish]], [[V8 (JavaScript-Engine)|V8]]"
                                + "|Beeinflusst_von = [[Self (Programmiersprache)|Self]], [[C (Programmiersprache)|C]], [[Scheme]], [[Perl (Programmiersprache)|Perl]], [[Python (Programmiersprache)|Python]], [[Java (Programmiersprache)|Java]]"
                                + "}}", false
                )).isEqualTo("\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table class=\"prettytable float-right\" style=\"font-size:90%; width:21em;\">\n"
                + "<tr class=\"hintergrundfarbe6\">\n"
                + "<th colspan=\"2\" style=\"font-size:105%; text-align: center;\"><big>JavaScript</big></th></tr>\n"
                + "<tr valign=\"top\">\n"
                + "<td><b><a href=\"http://www.bliki.info/wiki/Programmierparadigma\" title=\"Programmierparadigma\">Paradigmen</a>:</b>\n"
                + " </td>\n"
                + "<td>multiparadigmatisch</td></tr>\n"
                + "<tr valign=\"top\">\n"
                + "<td><b>Erscheinungsjahr:</b>\n"
                + " </td>\n"
                + "<td>1995</td></tr>\n"
                + "<tr valign=\"top\">\n"
                + "<td><b>Entwickler:</b>\n"
                + " </td>\n"
                + "<td><a href=\"http://www.bliki.info/wiki/Brendan_Eich\" title=\"Brendan Eich\">Brendan Eich</a></td></tr>\n"
                + "<tr valign=\"top\">\n"
                + "<td><b>Aktuelle <a href=\"http://www.bliki.info/wiki/Versionierung\" title=\"Versionierung\">Version</a>:</b>\n"
                + " </td>\n"
                + "<td style=\"white-space:nowrap;\">1.8  <small>(2008)</small></td></tr>\n"
                + "<tr valign=\"top\">\n"
                + "<td><b><a href=\"http://www.bliki.info/wiki/Typisierung_(Informatik)\" title=\"Typisierung (Informatik)\">Typisierung</a>:</b>\n"
                + " </td>\n"
                + "<td><a href=\"http://www.bliki.info/wiki/Schwache_Typisierung\" title=\"Schwache Typisierung\">schwach</a>, <a href=\"http://www.bliki.info/wiki/Dynamische_Typisierung\" title=\"Dynamische Typisierung\">dynamisch</a>, <a href=\"http://www.bliki.info/wiki/Duck_Typing\" title=\"Duck Typing\">duck</a></td></tr>\n"
                + "<tr valign=\"top\">\n"
                + "<td><b>wichtige <a href=\"http://www.bliki.info/wiki/Implementierung\" title=\"Implementierung\">Implementierungen</a>:</b>\n"
                + " </td>\n"
                + "<td><a href=\"http://www.bliki.info/wiki/SpiderMonkey\" title=\"SpiderMonkey\">SpiderMonkey</a>, <a href=\"http://www.bliki.info/wiki/Rhino\" title=\"Rhino\">Rhino</a>, <a href=\"http://www.bliki.info/wiki/SquirrelFish\" title=\"SquirrelFish\">SquirrelFish</a>, <a href=\"http://www.bliki.info/wiki/V8_(JavaScript-Engine)\" title=\"V8 (JavaScript-Engine)\">V8</a></td></tr>\n"
                + "<tr valign=\"top\">\n"
                + "<td><b>Einflüsse:</b>\n"
                + " </td>\n"
                + "<td><a href=\"http://www.bliki.info/wiki/Self_(Programmiersprache)\" title=\"Self (Programmiersprache)\">Self</a>, <a href=\"http://www.bliki.info/wiki/C_(Programmiersprache)\" title=\"C (Programmiersprache)\">C</a>, <a href=\"http://www.bliki.info/wiki/Scheme\" title=\"Scheme\">Scheme</a>, <a href=\"http://www.bliki.info/wiki/Perl_(Programmiersprache)\" title=\"Perl (Programmiersprache)\">Perl</a>, <a href=\"http://www.bliki.info/wiki/Python_(Programmiersprache)\" title=\"Python (Programmiersprache)\">Python</a>, <a href=\"http://www.bliki.info/wiki/Java_(Programmiersprache)\" title=\"Java (Programmiersprache)\">Java</a></td></tr></table></div>");
    }

    @Test public void testProgrammiersprachen() {
        assertThat(wikiModel
                .render(
                        "=== Versionsgeschichte ===\n"
                                + "{| class=\"wikitable\"\n"
                                + "|+Versionsgeschichte von JavaScript<ref>{{cite web|author=John Resig |url=http://ejohn.org/blog/versions-of-javascript |title=Versions of JavaScript |publisher=Ejohn.org |date= |accessdate=2009-05-19}}</ref>\n"
                                + "|-\n"
                                + "! Version !! Release !! Entsprechung !! Netscape Navigator !! Mozilla Firefox !! Internet Explorer !! Opera !! Safari !! Google Chrome \n"
                                + "|-\n"
                                + "| 1.0 || März 1996 ||  || 2.0 ||  || 3.0 ||  || || \n"
                                + "|-\n"
                                + "| 1.1 || August 1996 ||  || 3.0 ||  ||  ||  || || \n"
                                + "|-\n"
                                + "| 1.2 || Juni 1997 ||  || 4.0-4.05 ||  ||  ||  || || \n"
                                + "|-\n"
                                + "| 1.3 || Oktober 1998 || ECMA-262 1<sup>st</sup> edition / ECMA-262 2<sup>nd</sup> edition || 4.06-4.7x ||  || 4.0 ||  || || \n"
                                + "|-\n"
                                + "| 1.4 ||  ||  || Netscape Server ||  ||  ||  || || \n"
                                + "|-\n"
                                + "| 1.5 || November 2000 || ECMA-262 3<sup>rd</sup> edition || 6.0 || 1.0\n"
                                + "|\n"
                                + "*5.5 (JScript 5.5)\n"
                                + "*6 (JScript 5.6)\n"
                                + "*7 (JScript 5.7)\n"
                                + "*8 (JScript 6)\n"
                                + "|\n"
                                + "*6.0\n"
                                + "*7.0\n"
                                + "*8.0\n"
                                + "*9.0\n"
                                + "| || \n"
                                + "|-\n"
                                + "| 1.6 || November 2005 || 1.5 + Array extras + Array & String generics + E4X ||  || 1.5 ||  ||\n"
                                + "|\n"
                                + "*3.0\n"
                                + "*3.1\n"
                                + "| \n"
                                + "|-\n"
                                + "| 1.7 || Oktober 2006 || 1.6 + Pythonic generators + Iterators + let + destructuring assignments ||  || 2.0 ||  ||\n"
                                + "|\n" + "* 3.2\n" + "* 4.0\n" + "| 1.0\n" + "|-\n"
                                + "| 1.8 || Juni 2008 || 1.7 + Generator expressions + Expression closures ||  || 3.0 ||  ||  || || \n"
                                + "|-\n" + "| 1.8.1 ||  || 1.8 + geringfügige Updates ||  || 3.5 ||  ||  || || \n" + "|-\n"
                                + "| 1.9 ||  || 1.8.1 + ECMAScript 5 Compliance ||  || 4 ||  ||  || || \n" + "|}\n" + "\n" + "<references/>\n",
                        false
                )).isEqualTo("<h3><span class=\"mw-headline\" id=\"Versionsgeschichte\">Versionsgeschichte</span></h3>\n"
                + "\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table class=\"wikitable\">\n"
                + "<caption>Versionsgeschichte von JavaScript<sup id=\"_ref-1\" class=\"reference\"><a href=\"#_note-1\" title=\"\">[1]</a></sup></caption>\n"
                + "<tr>\n"
                + "<th>Version </th>\n"
                + "<th>Release </th>\n"
                + "<th>Entsprechung </th>\n"
                + "<th>Netscape Navigator </th>\n"
                + "<th>Mozilla Firefox </th>\n"
                + "<th>Internet Explorer </th>\n"
                + "<th>Opera </th>\n"
                + "<th>Safari </th>\n"
                + "<th>Google Chrome </th></tr>\n"
                + "<tr>\n"
                + "<td>1.0 </td>\n"
                + "<td>März 1996 </td>\n"
                + "<td />\n"
                + "<td>2.0 </td>\n"
                + "<td />\n"
                + "<td>3.0 </td>\n"
                + "<td />\n"
                + "<td />\n"
                + "<td /></tr>\n"
                + "<tr>\n"
                + "<td>1.1 </td>\n"
                + "<td>August 1996 </td>\n"
                + "<td />\n"
                + "<td>3.0 </td>\n"
                + "<td />\n"
                + "<td />\n"
                + "<td />\n"
                + "<td />\n"
                + "<td /></tr>\n"
                + "<tr>\n"
                + "<td>1.2 </td>\n"
                + "<td>Juni 1997 </td>\n"
                + "<td />\n"
                + "<td>4.0-4.05 </td>\n"
                + "<td />\n"
                + "<td />\n"
                + "<td />\n"
                + "<td />\n"
                + "<td /></tr>\n"
                + "<tr>\n"
                + "<td>1.3 </td>\n"
                + "<td>Oktober 1998 </td>\n"
                + "<td>ECMA-262 1<sup>st</sup> edition / ECMA-262 2<sup>nd</sup> edition </td>\n"
                + "<td>4.06-4.7x </td>\n"
                + "<td />\n"
                + "<td>4.0 </td>\n"
                + "<td />\n"
                + "<td />\n"
                + "<td /></tr>\n"
                + "<tr>\n"
                + "<td>1.4 </td>\n"
                + "<td />\n"
                + "<td />\n"
                + "<td>Netscape Server </td>\n"
                + "<td />\n"
                + "<td />\n"
                + "<td />\n"
                + "<td />\n"
                + "<td /></tr>\n"
                + "<tr>\n"
                + "<td>1.5 </td>\n"
                + "<td>November 2000 </td>\n"
                + "<td>ECMA-262 3<sup>rd</sup> edition </td>\n"
                + "<td>6.0 </td>\n"
                + "<td>1.0</td>\n"
                + "<td>\n"
                + "\n"
                + "<ul>\n"
                + "<li>5.5 (JScript 5.5)</li>\n"
                + "<li>6 (JScript 5.6)</li>\n"
                + "<li>7 (JScript 5.7)</li>\n"
                + "<li>8 (JScript 6)</li>\n</ul></td>\n"
                + "<td>\n"
                + "\n"
                + "<ul>\n"
                + "<li>6.0</li>\n"
                + "<li>7.0</li>\n"
                + "<li>8.0</li>\n"
                + "<li>9.0</li>\n</ul></td>\n"
                + "<td />\n"
                + "<td /></tr>\n"
                + "<tr>\n"
                + "<td>1.6 </td>\n"
                + "<td>November 2005 </td>\n"
                + "<td>1.5 + Array extras + Array &#38; String generics + E4X </td>\n"
                + "<td />\n"
                + "<td>1.5 </td>\n"
                + "<td />\n"
                + "<td></td>\n"
                + "<td>\n"
                + "\n"
                + "<ul>\n"
                + "<li>3.0</li>\n"
                + "<li>3.1</li>\n</ul></td>\n"
                + "<td /></tr>\n"
                + "<tr>\n"
                + "<td>1.7 </td>\n"
                + "<td>Oktober 2006 </td>\n"
                + "<td>1.6 + Pythonic generators + Iterators + let + destructuring assignments </td>\n"
                + "<td />\n"
                + "<td>2.0 </td>\n"
                + "<td />\n"
                + "<td></td>\n"
                + "<td>\n"
                + "\n"
                + "<ul>\n"
                + "<li>3.2</li>\n"
                + "<li>4.0</li>\n</ul></td>\n"
                + "<td>1.0</td></tr>\n"
                + "<tr>\n"
                + "<td>1.8 </td>\n"
                + "<td>Juni 2008 </td>\n"
                + "<td>1.7 + Generator expressions + Expression closures </td>\n"
                + "<td />\n"
                + "<td>3.0 </td>\n"
                + "<td />\n"
                + "<td />\n"
                + "<td />\n"
                + "<td /></tr>\n"
                + "<tr>\n"
                + "<td>1.8.1 </td>\n"
                + "<td />\n"
                + "<td>1.8 + geringfügige Updates </td>\n"
                + "<td />\n"
                + "<td>3.5 </td>\n"
                + "<td />\n"
                + "<td />\n"
                + "<td />\n"
                + "<td /></tr>\n"
                + "<tr>\n"
                + "<td>1.9 </td>\n"
                + "<td />\n"
                + "<td>1.8.1 + ECMAScript 5 Compliance </td>\n"
                + "<td />\n"
                + "<td>4 </td>\n"
                + "<td />\n"
                + "<td />\n"
                + "<td />\n"
                + "<td /></tr></table></div>\n"
                + "<ol class=\"references\">\n"
                + "<li id=\"_note-1\"><b><a href=\"#_ref-1\" title=\"\">&#8593;</a></b> John Resig. <i><a class=\"external text\" href=\"http://ejohn.org/blog/versions-of-javascript\" rel=\"nofollow\">Versions of JavaScript</a></i>. Ejohn.org. Abgerufen am <a href=\"http://www.bliki.info/wiki/Template:safesubst:\" title=\"Template:safesubst:\">Template:safesubst:</a>.</li>\n</ol>");
    }

    @Test public void testIssue77_001() {
        assertThat(wikiModel
                .render(
                        "{|\n"
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
                                + "|}", false
                )).isEqualTo("\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table>\n"
                + "<tr>\n"
                + "<th><h2 style=\"background:#cedff2;\">In the news</h2></th></tr>\n"
                + "<tr>\n"
                + "<td>\n"
                + "<div>\n"
                + "<div style=\"float:right;margin-left:0.5em;\">\n"
                + "<a class=\"image\" href=\"http://www.bliki.info/wiki/File:Yoshihiko_Noda-1.jpg\" title=\"Yoshihiko Noda\"><img src=\"http://www.bliki.info/wiki/100px-Yoshihiko_Noda-1.jpg\" alt=\"Yoshihiko Noda\" height=\"100\" width=\"100\" />\n"
                + "</a>\n"
                + "</div>\n"
                + "The ruling Democratic Party of Japan selects <b>Yoshihiko Noda</b> <i>(pictured)</i> as the country&#39;s new Prime Minister of Japan|prime minister, following the resignation of Naoto Kan.</div></td></tr></table></div>");
    }

    @Test public void testIssue77_002() {
        assertThat(wikiModel
                .render(
                        "{| style=\"width: 100%; height: auto; border: 1px solid #88A; background-color: #ACF; vertical-align: top; margin: 0em 0em 0.5em 0em; border-spacing: 0.6em;\" cellspacing=\"6\"\n"
                                + "|-\n"
                                + "{{Main Page subpanel|column=both|title=Knowledge groups|1=\n"
                                + "TEST1\n"
                                + "}}\n"
                                + "{{!}}-\n"
                                + "{{Main Page subpanel|column=both|title=Sister projects|1=\n" + "TEST2\n" + "}}\n" + "|}", false
                )).isEqualTo("\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table cellspacing=\"6\" style=\"width: 100%; height: auto; border: 1px solid #88A; background-color: #ACF; vertical-align: top; margin: 0em 0em 0.5em 0em; border-spacing: 0.6em;\">\n"
                + "<tr>\n"
                + "<td colspan=\"2\" style=\"width: 100%; vertical-align:top; color:#000; border: 3px double #AAA; background-color: #ffffff; padding: 0.5em; margin: 0em;\">\n"
                + "\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table style=\"vertical-align: top; margin: 0em; width: 100% !important; width: auto; display: table !important; display: inline; background-color: transparent;\">\n"
                + "<tr>\n"
                + "<th colspan=\"2\" style=\"background:#F0F0F0; margin: 0em; height: 1em; font-weight:bold; border:1px solid #AAA; text-align:left; color:#000;\"><h1 style=\"text-align: left; font-size: 1.2em; border: none; margin: 0; padding: 1.5px 0 2px 4px;\"><b>Knowledge groups</b></h1></th></tr>\n"
                + "<tr>\n"
                + "<td>\n"
                + "TEST1</td></tr></table></div></td></tr>\n"
                + "<tr>\n"
                + "<td colspan=\"2\" style=\"width: 100%; vertical-align:top; color:#000; border: 3px double #AAA; background-color: #ffffff; padding: 0.5em; margin: 0em;\">\n"
                + "\n"
                + "<div style=\"page-break-inside: avoid;\">\n"
                + "<table style=\"vertical-align: top; margin: 0em; width: 100% !important; width: auto; display: table !important; display: inline; background-color: transparent;\">\n"
                + "<tr>\n"
                + "<th colspan=\"2\" style=\"background:#F0F0F0; margin: 0em; height: 1em; font-weight:bold; border:1px solid #AAA; text-align:left; color:#000;\"><h1 style=\"text-align: left; font-size: 1.2em; border: none; margin: 0; padding: 1.5px 0 2px 4px;\"><b>Sister projects</b></h1></th></tr>\n"
                + "<tr>\n" + "<td>\n" + "TEST2</td></tr></table></div></td></tr></table></div>");
    }

    @Test public void testIssue82_002() {
        assertThat(wikiModel.parseTemplates("{{ordinal|105}}")).isEqualTo("105th");
    }

    @Test public void testRndfracTemplate001() {
        assertThat(wikiModel.render(
                "{{rndfrac|0.1234|8}}", false)).isEqualTo("\n" + "<p><span class=\"frac nowrap\"><sup>1</sup>⁄<sub>8</sub></span></p>");
    }

    @Test public void testRndfracTemplate002() {
        assertThat(wikiModel.render(
                "{{rndfrac|12.65|6}}", false)).isEqualTo("\n" + "<p><span class=\"frac nowrap\">12<sup> </sup><sup>4</sup>⁄<sub>6</sub></span></p>");
    }

    @Test public void testTemplateMain() {
        assertThat(wikiModel.render("{{Main|Demographics of Pakistan|Pakistani people}}", false)).isEqualTo("\n"
                + "<div class=\"rellink relarticle mainarticle\">Main articles: <a href=\"http://www.bliki.info/wiki/Demographics_of_Pakistan\" title=\"Demographics of Pakistan\">Demographics of Pakistan</a> and <a href=\"http://www.bliki.info/wiki/Pakistani_people\" title=\"Pakistani people\">Pakistani people</a></div>");
    }

    @Test public void testTemplateSeeAlso() {
        assertThat(wikiModel.render("{{See also|Ethnic groups in Pakistan|Religion in Pakistan}}", false)).isEqualTo("\n"
                + "<div class=\"rellink boilerplate seealso\">See also: <a href=\"http://www.bliki.info/wiki/Ethnic_groups_in_Pakistan\" title=\"Ethnic groups in Pakistan\">Ethnic groups in Pakistan</a> and <a href=\"http://www.bliki.info/wiki/Religion_in_Pakistan\" title=\"Religion in Pakistan\">Religion in Pakistan</a></div>");
    }

    @Test public void testTemplateMain002() {
        assertThat(wikiModel.render("==Demographics==\n" + "{{Main|Demographics of Pakistan|Pakistani people}}\n"
                + "{{See also|Ethnic groups in Pakistan|Religion in Pakistan}}", false)).isEqualTo("<h2><span class=\"mw-headline\" id=\"Demographics\">Demographics</span></h2>\n"
                + "<div class=\"rellink relarticle mainarticle\">Main articles: <a href=\"http://www.bliki.info/wiki/Demographics_of_Pakistan\" title=\"Demographics of Pakistan\">Demographics of Pakistan</a> and <a href=\"http://www.bliki.info/wiki/Pakistani_people\" title=\"Pakistani people\">Pakistani people</a></div>\n"
                + "<div class=\"rellink boilerplate seealso\">See also: <a href=\"http://www.bliki.info/wiki/Ethnic_groups_in_Pakistan\" title=\"Ethnic groups in Pakistan\">Ethnic groups in Pakistan</a> and <a href=\"http://www.bliki.info/wiki/Religion_in_Pakistan\" title=\"Religion in Pakistan\">Religion in Pakistan</a></div>");
    }

    @Test public void testUnknownTag001() {
        assertThat(wikiModel.render("start<unknowntag>\nsome text\n new line\ntest</unknowntag>end", false)).isEqualTo("\n" +
                "<p>start&#60;unknowntag&#62;\n" +
                "some text</p>\n" +
                "<pre>new line\n" +
                "</pre>\n" +
                "<p>test&#60;/unknowntag&#62;end</p>");
    }

    @Test public void testTemplateNavbox() {
        assertThat(wikiModel.render("{{Navbox \n"
                        + "| name       = National Board of Review Award for Best Actor\n"
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
                        + "* [[John Travolta]] (1977)\n" + "* [[Jon Voight]] / [[Laurence Olivier]] (1978)\n" + "* [[Peter Sellers]] (1979)\n"
                        + "\n" + "|group 5 = 1980-1989\n" + "|list5=\n" + "* [[Robert De Niro]] (1980)\n" + "* [[Peter Fonda]] (1981)\n"
                        + "* [[Ben Kingsley]] (1982)\n" + "* [[Tom Conti]] (1983)\n" + "* [[Victor Banerjee]] (1984)\n"
                        + "* [[William Hurt]] / [[Raúl Juliá]] (1985)\n" + "* [[Paul Newman]] (1986)\n" + "* [[Michael Douglas]] (1987)\n"
                        + "* [[Gene Hackman]] (1988)\n" + "* [[Morgan Freeman]] (1989)\n" + "\n" + "|group 6 = 1990-1999\n" + "|list6=\n"
                        + "* [[Robert De Niro]] / [[Robin Williams]] (1990)\n" + "* [[Warren Beatty]] (1991)\n" + "* [[Jack Lemmon]] (1992)\n"
                        + "* [[Anthony Hopkins]] (1993)\n" + "* [[Tom Hanks]] (1994)\n" + "* [[Nicolas Cage]] (1995)\n"
                        + "* [[Tom Cruise]] (1996)\n" + "* [[Jack Nicholson]] (1997)\n" + "* [[Ian McKellen]] (1998)\n"
                        + "* [[Russell Crowe]] (1999)\n" + "\n" + "|group 7 = 2000-2009\n" + "|list7= \n" + "* [[Javier Bardem]] (2000)\n"
                        + "* [[Billy Bob Thornton]] (2001)\n" + "* [[Campbell Scott]] (2002)\n" + "* [[Sean Penn]] (2003)\n"
                        + "* [[Jamie Foxx]] (2004)\n" + "* [[Philip Seymour Hoffman]] (2005)\n" + "* [[Forest Whitaker]] (2006)\n"
                        + "* [[George Clooney]] (2007)\n" + "* [[Clint Eastwood]] (2008)\n"
                        + "* [[George Clooney]] / [[Morgan Freeman]] (2009)\n" + "\n" + "|group 8 = 2010-present\n" + "|list8=\n"
                        + "* [[Jesse Eisenberg]] (2010)\n" + "* [[George Clooney]] (2011)\n" + "\n" + "\n" + "}}<noinclude>\n" + "\n"
                        + "[[Category:National Board of Review Awards|*|National Board of Review Award for Best Actor]]\n"
                        + "[[Category:National Board of Review Awards|*]]\n" + "[[Category:Film award templates|{{PAGENAME}}]]\n"
                        + "[[fr:Modèle:Palette Critics Choice Awards]]\n" + "[[ja:Template:ナショナル・ボード・オブ・レビュー賞]]\n" + "</noinclude>\n" + "",
                false
        )).isEqualTo("\n"
                + "<table cellspacing=\"0\" class=\"navbox\" style=\"border-spacing:0;;\">\n"
                + "\n"
                + "<tr>\n"
                + "\n"
                + "<td style=\"padding:2px;\">\n"
                + "<table cellspacing=\"0\" class=\"nowraplinks  collapsible autocollapse navbox-inner\" style=\"border-spacing:0;background:transparent;color:inherit;;\">\n"
                + "\n"
                + "<tr>\n"
                + "\n"
                + "<th class=\"navbox-title\" colspan=\"2\" scope=\"col\" style=\";\">\n"
                + "\n"
                + "<div class=\"noprint plainlinks hlist navbar mini\">\n"
                + "<ul>\n"
                + "\n"
                + "<li class=\"nv-view\"><a href=\"http://www.bliki.info/wiki/Template:National_Board_of_Review_Award_for_Best_Actor\" title=\"Template:National Board of Review Award for Best Actor\"><span style=\";;background:none transparent;border:none;\" title=\"View this template\">v</span></a></li>\n"
                + "<li class=\"nv-talk\"><a href=\"http://www.bliki.info/wiki/Template_talk:National_Board_of_Review_Award_for_Best_Actor\" title=\"Template talk:National Board of Review Award for Best Actor\"><span style=\";;background:none transparent;border:none;\" title=\"Discuss this template\">t</span></a></li>\n"
                + "<li class=\"nv-edit\"><a class=\"external text\" href=\"http://en.wikipedia.org/w/index.php?title=Template%3ANational+Board+of+Review+Award+for+Best+Actor&#38;action=edit\" rel=\"nofollow\"><span style=\";;background:none transparent;border:none;\" title=\"Edit this template\">e</span></a></li>\n"
                + "</ul></div>\n"
                + "<div style=\"font-size:110%;\">\n"
                + "<p><a href=\"http://www.bliki.info/wiki/National_Board_of_Review_Award_for_Best_Actor\" title=\"National Board of Review Award for Best Actor\">National Board of Review Award for Best Actor</a></p></div>\n"
                + "</th>\n"
                + "</tr>\n"
                + "<tr style=\"height:2px;\">\n"
                + "\n"
                + "</tr>\n"
                + "<tr>\n"
                + "\n"
                + "<td class=\"navbox-list navbox-odd hlist\n"
                + "\" colspan=\"2\" style=\"width:100%;padding:0px;;;\">\n"
                + "<div style=\"padding:0em 0.25em\">\n"
                + "\n"
                + "<ul>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Ray_Milland\" title=\"Ray Milland\">Ray Milland</a> (1945)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Laurence_Olivier\" title=\"Laurence Olivier\">Laurence Olivier</a> (1946)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Michael_Redgrave\" title=\"Michael Redgrave\">Michael Redgrave</a> (1947)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Walter_Huston\" title=\"Walter Huston\">Walter Huston</a> (1948)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Ralph_Richardson\" title=\"Ralph Richardson\">Ralph Richardson</a> (1949)</li>\n</ul></div></td>\n"
                + "</tr>\n"
                + "<tr style=\"height:2px\">\n"
                + "\n"
                + "</tr>\n"
                + "<tr>\n"
                + "\n"
                + "<td class=\"navbox-list navbox-even hlist\n"
                + "\" colspan=\"2\" style=\"width:100%;padding:0px;;;\">\n"
                + "<div style=\"padding:0em 0.25em\">\n"
                + "\n"
                + "<ul>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Alec_Guinness\" title=\"Alec Guinness\">Alec Guinness</a> (1950)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Richard_Basehart\" title=\"Richard Basehart\">Richard Basehart</a> (1951)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Ralph_Richardson\" title=\"Ralph Richardson\">Ralph Richardson</a> (1952)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/James_Mason\" title=\"James Mason\">James Mason</a> (1953)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Bing_Crosby\" title=\"Bing Crosby\">Bing Crosby</a> (1954)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Ernest_Borgnine\" title=\"Ernest Borgnine\">Ernest Borgnine</a> (1955)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Yul_Brynner\" title=\"Yul Brynner\">Yul Brynner</a> (1956)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Alec_Guinness\" title=\"Alec Guinness\">Alec Guinness</a> (1957)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Spencer_Tracy\" title=\"Spencer Tracy\">Spencer Tracy</a> (1958)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Victor_Sj%C3%B6str%C3%B6m\" title=\"Victor Sjöström\">Victor Sjöström</a> (1959)</li>\n</ul></div></td>\n"
                + "</tr>\n"
                + "<tr style=\"height:2px\">\n"
                + "\n"
                + "</tr>\n"
                + "<tr>\n"
                + "\n"
                + "<td class=\"navbox-list navbox-odd hlist\n"
                + "\" colspan=\"2\" style=\"width:100%;padding:0px;;;\">\n"
                + "<div style=\"padding:0em 0.25em\">\n"
                + "\n"
                + "<ul>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Robert_Mitchum\" title=\"Robert Mitchum\">Robert Mitchum</a> (1960)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Albert_Finney\" title=\"Albert Finney\">Albert Finney</a> (1961)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Jason_Robards\" title=\"Jason Robards\">Jason Robards</a> (1962)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Rex_Harrison\" title=\"Rex Harrison\">Rex Harrison</a> (1963)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Anthony_Quinn\" title=\"Anthony Quinn\">Anthony Quinn</a> (1964)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Lee_Marvin\" title=\"Lee Marvin\">Lee Marvin</a> (1965)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Paul_Scofield\" title=\"Paul Scofield\">Paul Scofield</a> (1966)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Peter_Finch\" title=\"Peter Finch\">Peter Finch</a> (1967)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Cliff_Robertson\" title=\"Cliff Robertson\">Cliff Robertson</a> (1968)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Peter_O&#39;Toole\" title=\"Peter O&#39;Toole\">Peter O&#39;Toole</a> (1969)</li>\n</ul></div></td>\n"
                + "</tr>\n"
                + "<tr style=\"height:2px\">\n"
                + "\n"
                + "</tr>\n"
                + "<tr>\n"
                + "\n"
                + "<td class=\"navbox-list navbox-even hlist\n"
                + "\" colspan=\"2\" style=\"width:100%;padding:0px;;;\">\n"
                + "<div style=\"padding:0em 0.25em\">\n"
                + "\n"
                + "<ul>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/George_C._Scott\" title=\"George C. Scott\">George C. Scott</a> (1970)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Gene_Hackman\" title=\"Gene Hackman\">Gene Hackman</a> (1971)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Peter_O&#39;Toole\" title=\"Peter O&#39;Toole\">Peter O&#39;Toole</a> (1972)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Al_Pacino\" title=\"Al Pacino\">Al Pacino</a> / <a href=\"http://www.bliki.info/wiki/Robert_Ryan\" title=\"Robert Ryan\">Robert Ryan</a> (1973)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Gene_Hackman\" title=\"Gene Hackman\">Gene Hackman</a> (1974)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Jack_Nicholson\" title=\"Jack Nicholson\">Jack Nicholson</a> (1975)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/David_Carradine\" title=\"David Carradine\">David Carradine</a> (1976)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/John_Travolta\" title=\"John Travolta\">John Travolta</a> (1977)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Jon_Voight\" title=\"Jon Voight\">Jon Voight</a> / <a href=\"http://www.bliki.info/wiki/Laurence_Olivier\" title=\"Laurence Olivier\">Laurence Olivier</a> (1978)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Peter_Sellers\" title=\"Peter Sellers\">Peter Sellers</a> (1979)</li>\n</ul></div></td>\n"
                + "</tr>\n"
                + "<tr style=\"height:2px\">\n"
                + "\n"
                + "</tr>\n"
                + "<tr>\n"
                + "\n"
                + "<td class=\"navbox-list navbox-odd hlist\n"
                + "\" colspan=\"2\" style=\"width:100%;padding:0px;;;\">\n"
                + "<div style=\"padding:0em 0.25em\">\n"
                + "\n"
                + "<ul>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Robert_De_Niro\" title=\"Robert De Niro\">Robert De Niro</a> (1980)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Peter_Fonda\" title=\"Peter Fonda\">Peter Fonda</a> (1981)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Ben_Kingsley\" title=\"Ben Kingsley\">Ben Kingsley</a> (1982)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Tom_Conti\" title=\"Tom Conti\">Tom Conti</a> (1983)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Victor_Banerjee\" title=\"Victor Banerjee\">Victor Banerjee</a> (1984)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/William_Hurt\" title=\"William Hurt\">William Hurt</a> / <a href=\"http://www.bliki.info/wiki/Ra%C3%BAl_Juli%C3%A1\" title=\"Raúl Juliá\">Raúl Juliá</a> (1985)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Paul_Newman\" title=\"Paul Newman\">Paul Newman</a> (1986)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Michael_Douglas\" title=\"Michael Douglas\">Michael Douglas</a> (1987)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Gene_Hackman\" title=\"Gene Hackman\">Gene Hackman</a> (1988)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Morgan_Freeman\" title=\"Morgan Freeman\">Morgan Freeman</a> (1989)</li>\n</ul></div></td>\n"
                + "</tr>\n"
                + "<tr style=\"height:2px\">\n"
                + "\n"
                + "</tr>\n"
                + "<tr>\n"
                + "\n"
                + "<td class=\"navbox-list navbox-even hlist\n"
                + "\" colspan=\"2\" style=\"width:100%;padding:0px;;;\">\n"
                + "<div style=\"padding:0em 0.25em\">\n"
                + "\n"
                + "<ul>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Robert_De_Niro\" title=\"Robert De Niro\">Robert De Niro</a> / <a href=\"http://www.bliki.info/wiki/Robin_Williams\" title=\"Robin Williams\">Robin Williams</a> (1990)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Warren_Beatty\" title=\"Warren Beatty\">Warren Beatty</a> (1991)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Jack_Lemmon\" title=\"Jack Lemmon\">Jack Lemmon</a> (1992)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Anthony_Hopkins\" title=\"Anthony Hopkins\">Anthony Hopkins</a> (1993)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Tom_Hanks\" title=\"Tom Hanks\">Tom Hanks</a> (1994)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Nicolas_Cage\" title=\"Nicolas Cage\">Nicolas Cage</a> (1995)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Tom_Cruise\" title=\"Tom Cruise\">Tom Cruise</a> (1996)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Jack_Nicholson\" title=\"Jack Nicholson\">Jack Nicholson</a> (1997)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Ian_McKellen\" title=\"Ian McKellen\">Ian McKellen</a> (1998)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Russell_Crowe\" title=\"Russell Crowe\">Russell Crowe</a> (1999)</li>\n</ul></div></td>\n"
                + "</tr>\n"
                + "<tr style=\"height:2px\">\n"
                + "\n"
                + "</tr>\n"
                + "<tr>\n"
                + "\n"
                + "<td class=\"navbox-list navbox-odd hlist\n"
                + "\" colspan=\"2\" style=\"width:100%;padding:0px;;;\">\n"
                + "<div style=\"padding:0em 0.25em\">\n"
                + "\n"
                + "<ul>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Javier_Bardem\" title=\"Javier Bardem\">Javier Bardem</a> (2000)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Billy_Bob_Thornton\" title=\"Billy Bob Thornton\">Billy Bob Thornton</a> (2001)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Campbell_Scott\" title=\"Campbell Scott\">Campbell Scott</a> (2002)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Sean_Penn\" title=\"Sean Penn\">Sean Penn</a> (2003)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Jamie_Foxx\" title=\"Jamie Foxx\">Jamie Foxx</a> (2004)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Philip_Seymour_Hoffman\" title=\"Philip Seymour Hoffman\">Philip Seymour Hoffman</a> (2005)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Forest_Whitaker\" title=\"Forest Whitaker\">Forest Whitaker</a> (2006)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/George_Clooney\" title=\"George Clooney\">George Clooney</a> (2007)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Clint_Eastwood\" title=\"Clint Eastwood\">Clint Eastwood</a> (2008)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/George_Clooney\" title=\"George Clooney\">George Clooney</a> / <a href=\"http://www.bliki.info/wiki/Morgan_Freeman\" title=\"Morgan Freeman\">Morgan Freeman</a> (2009)</li>\n</ul></div></td>\n"
                + "</tr>\n"
                + "<tr style=\"height:2px\">\n"
                + "\n"
                + "</tr>\n"
                + "<tr>\n"
                + "\n"
                + "<td class=\"navbox-list navbox-even hlist\n"
                + "\" colspan=\"2\" style=\"width:100%;padding:0px;;;\">\n"
                + "<div style=\"padding:0em 0.25em\">\n"
                + "\n"
                + "<ul>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/Jesse_Eisenberg\" title=\"Jesse Eisenberg\">Jesse Eisenberg</a> (2010)</li>\n"
                + "<li><a href=\"http://www.bliki.info/wiki/George_Clooney\" title=\"George Clooney\">George Clooney</a> (2011)</li>\n</ul>\n"
                + "</div></td>\n" + "</tr>\n" + "</table></td>\n" + "</tr>\n" + "</table>\n" + "");
    }

    @Test public void testTemplateNavbar() {
        assertThat(wikiModel.render(
                "{{Navbar|Screen Actors Guild Award for Outstanding Performance by a Cast in a Motion Picture (1995–2000)}}\n", false)).isEqualTo("\n"
                + "<div class=\"noprint plainlinks hlist navbar \"><span style=\"word-spacing:0;\">This box: </span>\n"
                + "<ul>\n"
                + "\n"
                + "<li class=\"nv-view\"><a href=\"http://www.bliki.info/wiki/Template:Screen_Actors_Guild_Award_for_Outstanding_Performance_by_a_Cast_in_a_Motion_Picture_(1995%E2%80%932000)\" title=\"Template:Screen Actors Guild Award for Outstanding Performance by a Cast in a Motion Picture (1995–2000)\"><span title=\"View this template\">view</span></a></li>\n"
                + "<li class=\"nv-talk\"><a href=\"http://www.bliki.info/wiki/Template_talk:Screen_Actors_Guild_Award_for_Outstanding_Performance_by_a_Cast_in_a_Motion_Picture_(1995%E2%80%932000)\" title=\"Template talk:Screen Actors Guild Award for Outstanding Performance by a Cast in a Motion Picture (1995–2000)\"><span title=\"Discuss this template\">talk</span></a></li>\n"
                + "<li class=\"nv-edit\"><a class=\"external text\" href=\"http://en.wikipedia.org/w/index.php?title=Template%3AScreen+Actors+Guild+Award+for+Outstanding+Performance+by+a+Cast+in+a+Motion+Picture+%281995%E2%80%932000%29&#38;action=edit\" rel=\"nofollow\"><span title=\"Edit this template\">edit</span></a></li>\n"
                + "</ul></div>\n" + "");
    }

    @Test public void testTemplateWithNewlines001() {
        assertThat(wikiModel.render("{{Serie de TV\n  Nombre        = FabulÃ³polis\n}}", false)).isEqualTo("\n" + "<p>{{Serie de TV</p>\n" + "<pre>" + " Nombre        = FabulÃ³polis\n" + "</pre>\n" + "<p>}}</p>");

        assertThat(wikiModel.getTemplates()).isEqualTo(new HashSet<String>());
    }

    @Test public void testTemplateWithNewlines002() {
        assertThat(wikiModel.parseTemplates("{{Serie de TV\n\n}}")).isEqualTo("[[:Template:Serie de TV]]");
        assertThat(wikiModel.getTemplates()).contains("Serie de TV");
    }

    @Test public void testTemplateWithNewlines003() {
        assertThat(wikiModel.parseTemplates("{{Serie de TV\n}}")).isEqualTo("[[:Template:Serie de TV]]");

        assertThat(wikiModel.getTemplates()).contains("Serie de TV");
    }

    @Test public void testTemplateWithHashtag001() {
        assertThat(wikiModel
                .render("{{::Viva_World_Cup#Tournament_results}}\n" + "{{:Viva_World_Cup#Tournament_results}}\n"
                        + "{{:Viva_World_Cup#Tournament_results#History}}\n" + "[[Category:Test#test]]", false)).isEqualTo("\n" + "<p>{{::Viva_World_Cup#Tournament_results}}\n"
                + "<a href=\"http://www.bliki.info/wiki/Viva_World_Cup\" title=\"Viva World Cup\">Viva_World_Cup</a>\n"
                + "<a href=\"http://www.bliki.info/wiki/Viva_World_Cup\" title=\"Viva World Cup\">Viva_World_Cup</a>\n" + "</p>");

        assertThat(wikiModel.getIncludes()).contains("Viva_World_Cup");
        assertThat(wikiModel.getCategories().keySet()).contains("Test");
    }

    @Test public void testTransclusionInPre001() {
        assertThat(wikiModel.parseTemplates("<pre>{{:Viva_World_Cup}}</pre>")).isEqualTo("<pre>{{:Viva_World_Cup}}</pre>");

        assertThat(wikiModel.getIncludes()).isEmpty();
        assertThat(wikiModel.getTemplates()).isEmpty();
    }

    @Test public void testPreAlmostEqualToNowiki001() {
        // https://en.wikipedia.org/wiki/Help:Wiki_markup#Pre
        assertThat(wikiModel.render("<pre>Text\n[[wiki]] markup</pre>", false)).isEqualTo("\n<pre>Text\n[[wiki]] markup</pre>");

        assertThat(wikiModel.getLinks()).isEmpty();
    }

    @Test public void testPreAlmostEqualToNowiki002() {
        // https://en.wikipedia.org/wiki/Help:Wiki_markup#Nowiki
        assertThat(wikiModel.render("<nowiki>Text\n[[wiki]] markup</nowiki>", false)).isEqualTo("\n<p>Text\n[[wiki]] markup</p>");
        assertThat(wikiModel.getLinks()).isEmpty();
    }

    /**
     * Issue 131 - incorrect parsing of links if an additional HTML tag is
     * appended
     */
    @Test public void testLinkWithExtraHTMLTag001() {
        assertThat(wikiModel.render("http://www.example.com/<hello>", false)).isEqualTo("\n<p><a class=\"external free\" href=\"http://www.example.com/\" rel=\"nofollow\">http://www.example.com/</a>&#60;hello&#62;</p>");
    }

    /**
     * Issue 133 - self-inclusion is only allowed once in MediaWiki
     */
    @Test public void testSelfRecursion001() {
        // https://en.wikipedia.org/wiki/Help:Template#Nesting_templates
        assertThat(wikiModel.render(SELF_RECURSION, false)).isEqualTo("\n<p>Line1</p>"
                + "\n<p>Line1</p>"
                + "\n<p><span class=\"error\">Template loop detected: <strong class=\"selflink\">Template:SELF_RECURSION</strong></span></p>");
    }

    /**
     * Issue 133 - self-inclusion is only allowed once in MediaWiki
     */
    @Test public void testSelfRecursion002() {
        // check that a template can be included more than once
        assertThat(wikiModel.render("{{1x|a}}, {{1x|a}}", false)).isEqualTo("\n<p>a, a</p>");
        assertThat(wikiModel.render("{{1x|a}}, {{1x|b}}", false)).isEqualTo("\n<p>a, b</p>");
    }

    @Test public void testSelfRecursion003() {
        // check that a template can be included as a parameter to itself
        assertThat(wikiModel.render("{{1x|{{1x|a}}}}", false)).isEqualTo("\n<p>a</p>");
        assertThat(wikiModel.render("{{1x|a{{1x|b}}}}", false)).isEqualTo("\n<p>ab</p>");
    }

    @Test public void testSelfRecursion004() {
        // check that a template which includes itself via a parameter is recognised as a loop
        assertThat(wikiModel.render("{{SELF_RECURSION1|SELF_RECURSION1}}", false)).isEqualTo("\n<p>Line1</p>"
                + "\n<p><span class=\"error\">Template loop detected: <strong class=\"selflink\">Template:SELF_RECURSION1</strong></span></p>");
    }

    @Test public void testSelfRecursion005() {
        assertThat(wikiModel.parseTemplates("{{1x|{{1x|foo{{!}}bar}}}}")).isEqualTo("foo|bar");
    }

    @Test public void testSelfRecursion005a() {
        assertThat(wikiModel.render("{{1x|{{1x|foo{{!}}bar}}}}")).isEqualTo("\n" +
                "<p>foo|bar</p>");
    }
    /**
     * Issue 133 - self-inclusion is only allowed once in MediaWiki
     */
    @Test public void testIndirectSelfRecursion001() {
        // https://en.wikipedia.org/wiki/Help:Template#Nesting_templates
        assertThat(wikiModel.render(INDIRECT_SELF_RECURSION1, false)).isEqualTo("\n"
                + "<p>INDIRECT_SELF_RECURSION1</p>\n"
                + "<p>INDIRECT_SELF_RECURSION2</p>\n"
                + "<p>INDIRECT_SELF_RECURSION1</p>\n"
                + "<p><span class=\"error\">Template loop detected: <strong class=\"selflink\">Template:INDIRECT_SELF_RECURSION2</strong></span></p>");
    }

    /**
     * Issue 133 - self-inclusion is only allowed once in MediaWiki
     */
    @Test public void testIndirectSelfRecursion002() {
        // https://en.wikipedia.org/wiki/Help:Template#Nesting_templates
        assertThat(wikiModel.render(INDIRECT_SELF_RECURSION2, false)).isEqualTo("\n"
                + "<p>INDIRECT_SELF_RECURSION2</p>\n"
                + "<p>INDIRECT_SELF_RECURSION1</p>\n"
                + "<p>INDIRECT_SELF_RECURSION2</p>\n"
                + "<p><span class=\"error\">Template loop detected: <strong class=\"selflink\">Template:INDIRECT_SELF_RECURSION1</strong></span></p>");
    }

    /**
     * Issue 133 - self-inclusion is only allowed once in MediaWiki
     */
    @Test public void testIndirectSelfRecursion001a() {
        // https://en.wikipedia.org/wiki/Help:Template#Nesting_templates
        assertThat(wikiModel.render(INDIRECT_SELF_RECURSION1a, false)).isEqualTo("\n"
                + "<p>INDIRECT_SELF_RECURSION1a</p>\n"
                + "<p>INDIRECT_SELF_RECURSION2a</p>\n"
                + "<p>INDIRECT_SELF_RECURSION1a</p>\n"
                + "<p><span class=\"error\">Template loop detected: <strong class=\"selflink\">Template:INDIRECT_SELF_RECURSION2a</strong></span></p>");
    }

    /**
     * Issue 133 - self-inclusion is only allowed once in MediaWiki
     */
    @Test public void testIndirectSelfRecursion002a() {
        // https://en.wikipedia.org/wiki/Help:Template#Nesting_templates
        assertThat(wikiModel.render(INDIRECT_SELF_RECURSION2a, false)).isEqualTo("\n"
                + "<p>INDIRECT_SELF_RECURSION2a</p>\n"
                + "<p>INDIRECT_SELF_RECURSION1a</p>\n"
                + "<p>INDIRECT_SELF_RECURSION2a</p>\n"
                + "<p><span class=\"error\">Template loop detected: <strong class=\"selflink\">Template:INDIRECT_SELF_RECURSION1a</strong></span></p>");
    }

    /**
     * Test whether the template cache distinguishes pages from different
     * namespaces with the same page title.
     */
    @Test public void testTemplateCache001() {
        assertThat(wikiModel.render("{{FOO}}{{:FOO}}", false)).isEqualTo("\n<p>FOOBAR</p>");
    }

    /**
     * Test whether the template cache distinguishes pages from different
     * namespaces with the same page title.
     */
    @Test public void testTemplateCache002() {
        assertThat(wikiModel.render("{{FOO}}{{Template:FOO}}", false)).isEqualTo("\n<p>FOOFOO</p>");
    }

    /**
     * Test whether the template cache distinguishes pages from different
     * namespaces with the same page title.
     */
    @Test public void testTemplateCache003() {
        assertThat(wikiModel.render("{{Template:FOO}}{{FOO}}", false)).isEqualTo("\n<p>FOOFOO</p>");
    }

    /**
     * Test whether the template cache distinguishes pages from different
     * namespaces with the same page title.
     */
    @Test public void testTemplateCache004() {
        String foodate = wikiModel.render("{{FOODATE}}", false);
        System.out.println("testTemplateCache004: " + foodate);
        try {
            Thread.sleep(1);
        } catch (InterruptedException ignored) {
        }
        assertThat(wikiModel.render("{{Template:FOODATE}}", false)).isEqualTo(foodate);
    }

    /**
     * Test whether the template cache distinguishes pages from different
     * namespaces with the same page title.
     */
    @Test public void testTemplateCache005() {
        String foodate = wikiModel.render("{{Template:FOODATE}}", false);
        System.out.println("testTemplateCache005: " + foodate);
        try {
            Thread.sleep(1);
        } catch (InterruptedException ignored) {
        }
        assertThat(wikiModel.render("{{FOODATE}}", false)).isEqualTo(foodate);
    }
}

package info.bliki.wiki.template;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateFunctionsTest  {

    protected ParserFunctionModel wikiModel = null;


    @Before
    public void setUp() throws Exception {
        wikiModel = new ParserFunctionModel("http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
        wikiModel.setUp();
    }

    @Test public void testIf01() {
        // {{ #if: {{{x| }}} | not blank | blank }} = blank
        assertThat(wikiModel.parseTemplates("{{ #if: {{{x| }}} | not blank | blank }}", false)).isEqualTo("blank");
    }

    @Test public void testIferror01() {
        assertThat(wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + 2 }} | error | correct }}", false)).isEqualTo("correct");
    }

    @Test public void testIferror02() {
        assertThat(wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + X }} | error | correct }}", false)).isEqualTo("error");
    }

    @Test public void testIferror03() {
        assertThat(wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + 2 }} | error }}", false)).isEqualTo("3");
    }

    @Test public void testIferror04() {
        assertThat(wikiModel.parseTemplates("{{#iferror: {{#expr: 1 * }} | error }}", false)).isEqualTo("error");
    }

    @Test public void testIferror05() {
        assertThat(wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + 2 }} }}", false)).isEqualTo("3");
    }

    @Test public void testIferror06() {
        assertThat(wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + X }} }}", false)).isEqualTo("");
    }

    @Test public void testIfEq01() {
        // {{ #ifeq: +07 | 007 | 1 | 0 }} gives 1
        assertThat(wikiModel.parseTemplates("{{ #ifeq: +07 | 007 | 1 | 0 }}", false)).isEqualTo("1");
    }

    @Test public void testIfEq02() {
        // {{ #ifeq: "+07" | "007" | 1 | 0 }} gives 0
        assertThat(wikiModel.parseTemplates("{{ #ifeq: \"+07\" | \"007\" | 1 | 0 }}", false)).isEqualTo("0");
    }

    @Test public void testIfEq03() {
        // {{ #ifeq: A | a | 1 | 0 }} gives 0
        assertThat(wikiModel.parseTemplates("{{ #ifeq: A | a | 1 | 0 }}", false)).isEqualTo("0");
    }

    @Test public void testIfEq05() {
        // {{ #ifeq: {{{x| }}} | | blank | not blank }} = blank,
        assertThat(wikiModel.parseTemplates("{{ #ifeq: {{{x| }}} | | blank | not blank }}", false)).isEqualTo("blank");
    }

    @Test public void testIfEq06() {
        // {{ #ifeq: {{{x| }}} | {{{x|u}}} | defined | undefined }} = undefined.
        assertThat(wikiModel.parseTemplates("{{ #ifeq: {{{x| }}} | {{{x|u}}} | defined | undefined }}", false)).isEqualTo("undefined");
    }

    @Test public void testIfEq07() {
        // {{ #ifeq: {{{x}}} | {{concat| {|{|{x}|}|} }} | 1 | 0 }} = 1
        assertThat(wikiModel.parseTemplates("{{concat| {|{|{x}|}|} }}", false)).isEqualTo(" {{{x}}} ");

        assertThat(wikiModel.parseTemplates("{{ #ifeq: {{{x}}} | {{concat| {|{|{x}|}|} }} | 1 | 0 }}", false)).isEqualTo("1");
    }

    @Test public void testRendererForST() throws Exception {
        wikiModel.setAttribute("created", new GregorianCalendar(2005, 07 - 1, 05));
        wikiModel.registerRenderer(GregorianCalendar.class, wikiModel.new DateRenderer());
        String expecting = "date: 2005.07.05";
        assertThat(wikiModel.parseTemplates("date: {{#$:created}}")).isEqualTo(expecting);
    }

    @Test public void testRendererWithFormatAndList() throws Exception {
        wikiModel.setAttribute("names", "ter");
        wikiModel.setAttribute("names", "tom");
        wikiModel.setAttribute("names", "sriram");
        wikiModel.registerRenderer(String.class, wikiModel.new UppercaseRenderer());
        String expecting = "The names: TERTOMSRIRAM";
        assertThat(wikiModel.parseTemplates("The names: {{#$:names|upper}}")).isEqualTo(expecting);
    }

    @Test public void testRendererWithFormatAndSeparator() throws Exception {
        wikiModel.setAttribute("names", "ter");
        wikiModel.setAttribute("names", "tom");
        wikiModel.setAttribute("names", "sriram");
        wikiModel.registerRenderer(String.class, wikiModel.new UppercaseRenderer());
        String expecting = "The names: TER and TOM and SRIRAM";
        assertThat(wikiModel.parseTemplates("The names: {{#$:names|upper|' and '}}")).isEqualTo(expecting);
    }

    @Test public void testRendererWithFormatAndSeparatorAndNull() throws Exception {
        List<String> names = new ArrayList<>();
        names.add("ter");
        names.add(null);
        names.add("sriram");
        wikiModel.setAttribute("names", names);
        wikiModel.registerRenderer(String.class, wikiModel.new UppercaseRenderer());
        String expecting = "The names: TER and N/A and SRIRAM";
        assertThat(wikiModel.parseTemplates("The names: {{#$:names|upper|' and '|n/a}}")).isEqualTo(expecting);
    }

    /**
     * Test from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
     * >Help:Calculation - Operators, numbers, and constants</a>
     */
    @Test public void testMod_001() {
        assertThat(wikiModel.parseTemplates("{{#expr:30mod7}}")).isEqualTo("2");
    }

    /**
     * Test from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
     * >Help:Calculation - Operators, numbers, and constants</a>
     */
    @Test public void testMod_002() {
        assertThat(wikiModel.parseTemplates("{{#expr:-30mod7}}")).isEqualTo("-2");
    }

    /**
     * Test from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
     * >Help:Calculation - Operators, numbers, and constants</a>
     */
    @Test public void testMod_003() {
        assertThat(wikiModel.parseTemplates("{{#expr:30mod-7}}")).isEqualTo("2");
    }

    /**
     * Test from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
     * >Help:Calculation - Operators, numbers, and constants</a>
     */
    @Test public void testMod_004() {
        assertThat(wikiModel.parseTemplates("{{#expr:-30mod-7}}")).isEqualTo("-2");
    }

    /**
     * Test from <a href=
     * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
     * >Help:Calculation - Operators, numbers, and constants</a>
     */
    @Test public void testMod_005() {
        assertThat(wikiModel.parseTemplates("{{#expr:30.5mod7.9}}")).isEqualTo("2");
    }

    @Test public void testMod10_001() {
        assertThat(wikiModel.parseTemplates("{{#expr:0.515654*1E1mod10}}")).isEqualTo("5");
    }

    @Test public void testMod10_002() {
        assertThat(wikiModel.parseTemplates("{{#expr:0.515654mod10}}")).isEqualTo("0");
    }
}

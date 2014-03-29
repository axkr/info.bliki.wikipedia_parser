package info.bliki.wiki.template;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TemplateFunctionsTest extends TestCase {

	protected ParserFunctionModel wikiModel = null;

	public TemplateFunctionsTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TemplateFunctionsTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wikiModel = new ParserFunctionModel("http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
		wikiModel.setUp();
	}

	public void testIf01() {
		// {{ #if: {{{x| }}} | not blank | blank }} = blank
		assertEquals("blank", wikiModel.parseTemplates("{{ #if: {{{x| }}} | not blank | blank }}", false));
	}

	public void testIferror01() {
		assertEquals("correct", wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + 2 }} | error | correct }}", false));
	}

	public void testIferror02() {
		assertEquals("error", wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + X }} | error | correct }}", false));
	}

	public void testIferror03() {
		assertEquals("3", wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + 2 }} | error }}", false));
	}

	public void testIferror04() {
		assertEquals("error", wikiModel.parseTemplates("{{#iferror: {{#expr: 1 * }} | error }}", false));
	}

	public void testIferror05() {
		assertEquals("3", wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + 2 }} }}", false));
	}

	public void testIferror06() {
		assertEquals("", wikiModel.parseTemplates("{{#iferror: {{#expr: 1 + X }} }}", false));
	}

	public void testIfEq01() {
		// {{ #ifeq: +07 | 007 | 1 | 0 }} gives 1
		assertEquals("1", wikiModel.parseTemplates("{{ #ifeq: +07 | 007 | 1 | 0 }}", false));
	}

	public void testIfEq02() {
		// {{ #ifeq: "+07" | "007" | 1 | 0 }} gives 0
		assertEquals("0", wikiModel.parseTemplates("{{ #ifeq: \"+07\" | \"007\" | 1 | 0 }}", false));
	}

	public void testIfEq03() {
		// {{ #ifeq: A | a | 1 | 0 }} gives 0
		assertEquals("0", wikiModel.parseTemplates("{{ #ifeq: A | a | 1 | 0 }}", false));
	}

	public void testIfEq05() {
		// {{ #ifeq: {{{x| }}} | | blank | not blank }} = blank,
		assertEquals("blank", wikiModel.parseTemplates("{{ #ifeq: {{{x| }}} | | blank | not blank }}", false));
	}

	public void testIfEq06() {
		// {{ #ifeq: {{{x| }}} | {{{x|u}}} | defined | undefined }} = undefined.
		assertEquals("undefined", wikiModel.parseTemplates("{{ #ifeq: {{{x| }}} | {{{x|u}}} | defined | undefined }}", false));
	}

	public void testIfEq07() {
		// {{ #ifeq: {{{x}}} | {{concat| {|{|{x}|}|} }} | 1 | 0 }} = 1
		assertEquals(" {{{x}}} ", wikiModel.parseTemplates("{{concat| {|{|{x}|}|} }}", false));

		assertEquals("1", wikiModel.parseTemplates("{{ #ifeq: {{{x}}} | {{concat| {|{|{x}|}|} }} | 1 | 0 }}", false));
	}

	public void testRendererForST() throws Exception {
		wikiModel.setAttribute("created", new GregorianCalendar(2005, 07 - 1, 05));
		wikiModel.registerRenderer(GregorianCalendar.class, wikiModel.new DateRenderer());
		String expecting = "date: 2005.07.05";
		assertEquals(expecting, wikiModel.parseTemplates("date: {{#$:created}}"));
	}

	public void testRendererWithFormatAndList() throws Exception {
		wikiModel.setAttribute("names", "ter");
		wikiModel.setAttribute("names", "tom");
		wikiModel.setAttribute("names", "sriram");
		wikiModel.registerRenderer(String.class, wikiModel.new UppercaseRenderer());
		String expecting = "The names: TERTOMSRIRAM";
		assertEquals(expecting, wikiModel.parseTemplates("The names: {{#$:names|upper}}"));
	}

	public void testRendererWithFormatAndSeparator() throws Exception {
		wikiModel.setAttribute("names", "ter");
		wikiModel.setAttribute("names", "tom");
		wikiModel.setAttribute("names", "sriram");
		wikiModel.registerRenderer(String.class, wikiModel.new UppercaseRenderer());
		String expecting = "The names: TER and TOM and SRIRAM";
		assertEquals(expecting, wikiModel.parseTemplates("The names: {{#$:names|upper|' and '}}"));
	}

	public void testRendererWithFormatAndSeparatorAndNull() throws Exception {
		List<String> names = new ArrayList<String>();
		names.add("ter");
		names.add(null);
		names.add("sriram");
		wikiModel.setAttribute("names", names);
		wikiModel.registerRenderer(String.class, wikiModel.new UppercaseRenderer());
		String expecting = "The names: TER and N/A and SRIRAM";
		assertEquals(expecting, wikiModel.parseTemplates("The names: {{#$:names|upper|' and '|n/a}}"));
	}
	
	/**
	 * Test from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
	 * >Help:Calculation - Operators, numbers, and constants</a>
	 */
	public void testMod_001() {
		assertEquals("2", wikiModel.parseTemplates("{{#expr:30mod7}}"));
	}

	/**
	 * Test from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
	 * >Help:Calculation - Operators, numbers, and constants</a>
	 */
	public void testMod_002() {
		assertEquals("-2", wikiModel.parseTemplates("{{#expr:-30mod7}}"));
	}

	/**
	 * Test from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
	 * >Help:Calculation - Operators, numbers, and constants</a>
	 */
	public void testMod_003() {
		assertEquals("2", wikiModel.parseTemplates("{{#expr:30mod-7}}"));
	}

	/**
	 * Test from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
	 * >Help:Calculation - Operators, numbers, and constants</a>
	 */
	public void testMod_004() {
		assertEquals("-2", wikiModel.parseTemplates("{{#expr:-30mod-7}}"));
	}

	/**
	 * Test from <a href=
	 * "https://meta.wikimedia.org/wiki/Help:Calculation#Operators.2C_numbers.2C_and_constants"
	 * >Help:Calculation - Operators, numbers, and constants</a>
	 */
	public void testMod_005() {
		assertEquals("2", wikiModel.parseTemplates("{{#expr:30.5mod7.9}}"));
	}

	public void testMod10_001() {
		assertEquals("5", wikiModel.parseTemplates("{{#expr:0.515654*1E1mod10}}"));
	}

	public void testMod10_002() {
		assertEquals("0", wikiModel.parseTemplates("{{#expr:0.515654mod10}}"));
	}
}
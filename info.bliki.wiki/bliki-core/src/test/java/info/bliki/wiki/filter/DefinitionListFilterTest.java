package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class DefinitionListFilterTest extends FilterTestSupport {
	public DefinitionListFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(DefinitionListFilterTest.class);
	}

	public void testDefinitionList0() {
		assertEquals(
				"\n" + 
				"<dl>\n" + 
				"<dd><i>There is also an <a href=\"http://www.bliki.info/wiki/Asteroid\" title=\"Asteroid\">asteroid</a> <a href=\"http://www.bliki.info/wiki/9969_Braille\" title=\"9969 Braille\">9969 Braille</a></i></dd>\n</dl>",
				wikiModel.render(":''There is also an [[asteroid]] [[9969 Braille]]''", false));
	}

	public void testDefinitionList1() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dt>name</dt>\n" + 
				"<dd>Definition</dd>\n</dl>", wikiModel.render(";name:Definition", false));
	}

	public void testDefinitionList2() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dt>name </dt>\n" + 
				"<dd>Definition</dd>\n</dl>", wikiModel.render("; name : Definition", false));
	}
	 
	public void testDefinitionList3() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dt>foo</dt>\n" + 
				"<dd>12:30</dd>\n</dl>", wikiModel.render(";foo:12:30", false));
	}

	public void testDefinitionList10() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dd>a simple test\n" + 
				"  x+y\n" + 
				"  </dd>\n</dl>\n" + 
				"<p>test test</p>", wikiModel
				.render(":a simple test<nowiki>\n" + "  x+y\n" + "  </nowiki>\n" + "test test", false));
	}

	public void testDefinitionList11() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dd>a simple test<span class=\"math\">ein text</span></dd>\n</dl>\n" + 
				"<pre>" + 
				" x+y\n" + 
				" \n" + 
				"</pre>\n" + 
				"<p>test test</p>", wikiModel.render(":a simple test<math>ein text\n" + "  x+y\n" + "  \n" + "test test", false));
	}
	
	public void testDefinitionList12() {
		assertEquals("\n" + 
				"<dl>\n" + 
				"<dd>blabla\n" + 
				"<dl>\n" + 
				"<dd>blablabla</dd>\n</dl></dd>\n</dl>\n" + 
				"<pre>" + 
				"test it\n" + 
				"\n" + 
				"</pre>", wikiModel.render(":blabla\n::blablabla\n" + " test it\n", false));
	}
}
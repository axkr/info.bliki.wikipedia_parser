package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GroovyTest extends FilterTestSupport {
	public GroovyTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(GroovyTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testGroovy001() {
		String result = wikiModel.render("'''Groovy Example'''\n" + "<source lang=\"groovy\">\n" + "public class Test {\n"
				+ "< > \" \' &" + "}\n" + "</source>", false);

		assertEquals(
				"\n"
						+ "<p><b>Groovy Example</b>\n"
						+ "</p><pre class=\"groovy\">\n"
						+ "<span style=\"color:#7F0055; font-weight: bold; \">public</span> <span style=\"color:#7F0055; font-weight: bold; \">class</span> Test {\n"
						+ "&#60; &#62; <span style=\"color:#2A00FF; \">&#34; &#39; &#38;}\n" + "</span></pre>", result);
	}

	public void testGroovy002() {
		String result = wikiModel.render("<source lang=\"groovy\">\n" + "import groovy.xml.StreamingMarkupBuilder\n"
				+ "import groovy.xml.XmlUtil\n" + "\n" + "def input = '''\n" + "<shopping>\n" + "  <category type=\"groceries\">\n"
				+ "      <item>Chocolate</item>\n" + "      <item>Coffee</item>\n" + "  </category>\n" + "  <category type=\"supplies\">\n"
				+ "      <item>Paper</item>\n" + "      <item quantity=\"4\">Pens</item>\n" + "  </category>\n"
				+ "  <category type=\"present\">\n" + "      <item when=\"Aug 10\">Kathryn's Birthday</item>\n" + "  </category>\n"
				+ "</shopping>\n" + "'''\n" + "</source>", false);
		assertEquals(
				"<pre class=\"groovy\">\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">import</span> groovy.xml.StreamingMarkupBuilder\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">import</span> groovy.xml.XmlUtil\n" + 
				"\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">def</span> input = <span style=\"color:#2A00FF; \">&#39;&#39;&#39;\n" + 
				"&#60;shopping&#62;\n" + 
				"  &#60;category type=&#34;groceries&#34;&#62;\n" + 
				"      &#60;item&#62;Chocolate&#60;/item&#62;\n" + 
				"      &#60;item&#62;Coffee&#60;/item&#62;\n" + 
				"  &#60;/category&#62;\n" + 
				"  &#60;category type=&#34;supplies&#34;&#62;\n" + 
				"      &#60;item&#62;Paper&#60;/item&#62;\n" + 
				"      &#60;item quantity=&#34;4&#34;&#62;Pens&#60;/item&#62;\n" + 
				"  &#60;/category&#62;\n" + 
				"  &#60;category type=&#34;present&#34;&#62;\n" + 
				"      &#60;item when=&#34;Aug 10&#34;&#62;Kathryn&#39;s Birthday&#60;/item&#62;\n" + 
				"  &#60;/category&#62;\n" + 
				"&#60;/shopping&#62;\n" + 
				"&#39;</span><span style=\"color:#2A00FF; \">&#39;&#39;</span>\n" + 
				"</pre>", result);
	}
}

package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import junit.framework.Test;
import junit.framework.TestSuite;

public class XMLTest extends FilterTestSupport {
	public XMLTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(XMLTest.class);
	}

	public void testXml() {
		String result = wikiModel.render("\'\'\'XML example:\'\'\'\n" + "<source lang=xml>\n" + "  <extension\n"
				+ "           point=\"org.eclipse.help.toc\">\n" + "        <toc\n" + "              file=\"phphelp.xml\"\n"
				+ "              primary=\"true\">\n" + "     <!-- simple comment -->                      \n" + "        </toc>\n"
				+ "  </extension>\n" + "</source>", false);

		assertEquals(
				"\n" + 
				"<p><b>XML example:</b>\n" + 
				"</p><pre class=\"xml\">\n" + 
				"  <span style=\"color:#7F0055; font-weight: bold; \">&#60;extension</span>\n" + 
				"           point=<span style=\"color:#2A00FF; \">&#34;org.eclipse.help.toc&#34;</span><span style=\"color:#7F0055; font-weight: bold; \">&#62;</span>\n" + 
				"        <span style=\"color:#7F0055; font-weight: bold; \">&#60;toc</span>\n" + 
				"              file=<span style=\"color:#2A00FF; \">&#34;phphelp.xml&#34;</span>\n" + 
				"              primary=<span style=\"color:#2A00FF; \">&#34;true&#34;</span><span style=\"color:#7F0055; font-weight: bold; \">&#62;</span>\n" + 
				"     <span style=\"color:#3F7F5F; \">&#60;!-- simple comment --&#62;</span>                      \n" + 
				"        <span style=\"color:#7F0055; font-weight: bold; \">&#60;/toc&#62;</span>\n" + 
				"  <span style=\"color:#7F0055; font-weight: bold; \">&#60;/extension&#62;</span>\n" + 
				"</pre>", result);
	}

	public void testXmlWithoutLangAttr() {
		String result = wikiModel.render("\'\'\'XML example:\'\'\'\n" + "<source>\n" + "  <extension\n"
				+ "           point=\"org.eclipse.help.toc\">\n" + "        <toc\n" + "              file=\"phphelp.xml\"\n"
				+ "              primary=\"true\">\n" + "     <!-- simple comment -->                      \n" + "        </toc>\n"
				+ "  </extension>\n" + "</source>", false);

		assertEquals(
				"\n" + 
				"<p><b>XML example:</b>\n" + 
				"</p><pre class=\"xml\">\n" + 
				"  <span style=\"color:#7F0055; font-weight: bold; \">&#60;extension</span>\n" + 
				"           point=<span style=\"color:#2A00FF; \">&#34;org.eclipse.help.toc&#34;</span><span style=\"color:#7F0055; font-weight: bold; \">&#62;</span>\n" + 
				"        <span style=\"color:#7F0055; font-weight: bold; \">&#60;toc</span>\n" + 
				"              file=<span style=\"color:#2A00FF; \">&#34;phphelp.xml&#34;</span>\n" + 
				"              primary=<span style=\"color:#2A00FF; \">&#34;true&#34;</span><span style=\"color:#7F0055; font-weight: bold; \">&#62;</span>\n" + 
				"     <span style=\"color:#3F7F5F; \">&#60;!-- simple comment --&#62;</span>                      \n" + 
				"        <span style=\"color:#7F0055; font-weight: bold; \">&#60;/toc&#62;</span>\n" + 
				"  <span style=\"color:#7F0055; font-weight: bold; \">&#60;/extension&#62;</span>\n" + 
				"</pre>", result);
	}

	public void testXmlColon() {
		String result = wikiModel.render("a '''simple XML''' \n<source lang=\"xml\">\n" 
				+ "<ui:remove>\n" 
				+ "   <!-- das ist der kommentar -->\n"
				+ "</ui:remove>\n" + "</source>\n test", false);

		assertEquals("\n" + 
				"<p>a <b>simple XML</b> \n" + 
				"</p><pre class=\"xml\">\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">&#60;ui:remove&#62;</span>\n" + 
				"   <span style=\"color:#3F7F5F; \">&#60;!-- das ist der kommentar --&#62;</span>\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">&#60;/ui:remove&#62;</span>\n" + 
				"</pre>\n" + 
				"<pre>" + 
				"test\n" + 
				"</pre>", result);
	}

	public void testXmlColonNotClosed() {
		String result = wikiModel.render("a '''simple XML''' \n<source lang=\"xml\">\n" 
				+ "<ui:remove>\n" 
				+ "   <!-- das ist der kommentar -->\n"
				+ "</ui:remove>\n" + "</src>\n test", false);

		assertEquals("\n" + 
				"<p>a <b>simple XML</b> \n" + 
				"</p><pre class=\"xml\">\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">&#60;ui:remove&#62;</span>\n" + 
				"   <span style=\"color:#3F7F5F; \">&#60;!-- das ist der kommentar --&#62;</span>\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">&#60;/ui:remove&#62;</span>\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">&#60;/src&#62;</span>\n" + 
				" test</pre>", result);
	}
} 

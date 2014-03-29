package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import junit.framework.Test;
import junit.framework.TestSuite;

public class PythonTest extends FilterTestSupport {
	public PythonTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(PythonTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testPython() {
		String result = wikiModel.render("<source lang=python>\n" + "# Python: \"Hello, world!\"\n" + "print \"Hello, world!\"\n"
				+ "# last comment line" + "</source>", false);

		assertEquals("<pre class=\"python\">\n" + 
				"<span style=\"color:#3F7F5F; \"># Python: &#34;Hello, world!&#34;\n" + 
				"</span><span style=\"color:#7F0055; \">print</span> <span style=\"color:#2A00FF; \">&#34;Hello, world!&#34;</span>\n" + 
				"<span style=\"color:#3F7F5F; \"># last comment line</span></pre>", result);
	}

}

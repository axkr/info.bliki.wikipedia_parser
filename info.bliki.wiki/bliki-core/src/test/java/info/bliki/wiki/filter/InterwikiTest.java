package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class InterwikiTest extends FilterTestSupport {
	public InterwikiTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(InterwikiTest.class);
	}

	public void test001() {
		assertEquals("\n" + 
				"<p>Interwiki <a href=\"http://www.disinfopedia.org/wiki.phtml?title=link\">disinfopedia:link</a> link test</p>", wikiModel.render("Interwiki [[disinfopedia:link]] link test", false));
	}
	public void test002() {
		assertEquals("\n" + 
				"<p>Interwiki <a href=\"http://www.FreeBSD.org/cgi/man.cgi?apropos=1&query=link\">freebsdman:link</a> link test</p>", wikiModel.render("Interwiki [[freebsdman:link]] link test", false));
	}
}
package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ItalicFilterTest extends FilterTestSupport {
	public ItalicFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(ItalicFilterTest.class);
	}

	public void testEM() {
		assertEquals("\n" + "<p>a <em> project </em>.</p>", wikiModel.render("a <em> project </em>.", false));
	}

	public void testItalic01() { 
		assertEquals("\n" + "<p><i>Text</i></p>", wikiModel.render("''Text''", false));
	}

//	public void testItalic02() {
//		assertEquals(
//				"",
//				wikiModel
//						.render("Terry Mattingly&#32;({{FormatDate}}).&#32;''[http://www.pottsmerc.com/articles/2009/04/12/opinion/srv0000005095974.txt Actor Tom Hanks talks about religion]''.&#32;"));
//	}

	public void testItalicWithPunctuation() {
		assertEquals("\n" + "<p><i>Text</i>:</p>", wikiModel.render("''Text'':", false));
	}
}

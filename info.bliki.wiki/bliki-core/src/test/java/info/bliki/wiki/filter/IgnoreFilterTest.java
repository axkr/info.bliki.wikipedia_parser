package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class IgnoreFilterTest extends FilterTestSupport {
	public IgnoreFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(IgnoreFilterTest.class);
	}

	/**
	 * see Issue94
	 */
	public void testInputbox001() {
		assertEquals("\n" + 
				"<p>start  end</p>", wikiModel.render("start <inputbox>\n" + "type=search\n" + "width=31\n" + "buttonlabel=Go\n"
				+ "searchbuttonlabel=Search\n" + "break=no\n" + "</inputbox> end", false));
	}

	public void testImagemap001() {
		assertEquals("\n" + 
				"<p>start  end</p>", wikiModel.render("start <imagemap>\n" + 
				"Image:Example2.png|150px|alt=Alt text\n" + 
				"default [[Main Page|Go to main page]]\n" + 
				"</imagemap> end", false));
	}

}
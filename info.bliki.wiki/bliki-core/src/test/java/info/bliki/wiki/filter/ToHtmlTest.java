package info.bliki.wiki.filter;

import java.io.IOException;

import info.bliki.wiki.model.WikiModel;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ToHtmlTest extends FilterTestSupport {
	public ToHtmlTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public static Test suite() {
		return new TestSuite(ToHtmlTest.class);
	}

	public void test001() {
		assertEquals("\n" + "<p>This is a simple <a href=\"/Hello_World\" title=\"Hello World\">Hello World</a> wiki tag</p>",
				WikiModel.toHtml("This is a simple [[Hello World]] wiki tag"));
	}

	public void test002() {
		java.io.StringWriter writer = new java.io.StringWriter();
		try {
			WikiModel.toHtml("This is a simple [[Hello World]] wiki tag", writer);
			writer.flush();
			assertEquals("\n" + "<p>This is a simple <a href=\"/Hello_World\" title=\"Hello World\">Hello World</a> wiki tag</p>", writer
					.toString());
			return;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		fail("test002()");
	}

	public void test003() {
		// the toHtml() method does not do the template parsing step
		assertEquals("\n" + "<p>start &#60;includeonly&#62; test &#60;/includeonly&#62; end</p>", WikiModel
				.toHtml("start <includeonly> test </includeonly> end"));
	}

	public void test004() {
		java.io.StringWriter writer = new java.io.StringWriter();
		try {
			WikiModel model = new WikiModel("/${image}", "/${title}");
			// the toText() method with last parameter true, does the template parsing
			// step
			WikiModel.toText(model, new HTMLConverter(), "start <includeonly> test </includeonly> end", writer, false, true);
			writer.flush();
			assertEquals("\n" + "<p>start  test  end</p>", writer.toString());
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
		fail("test002()");
	}

}
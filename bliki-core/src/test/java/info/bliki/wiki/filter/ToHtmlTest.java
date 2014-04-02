package info.bliki.wiki.filter;

import info.bliki.wiki.model.WikiModel;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ToHtmlTest extends FilterTestSupport {

    @Test public void test001() {
        assertEquals("\n" + "<p>This is a simple <a href=\"/Hello_World\" title=\"Hello World\">Hello World</a> wiki tag</p>",
                WikiModel.toHtml("This is a simple [[Hello World]] wiki tag"));
    }

    @Test public void test002() {
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

    @Test public void test003() {
        // the toHtml() method does not do the template parsing step
        assertEquals("\n" + "<p>start &#60;includeonly&#62; test &#60;/includeonly&#62; end</p>", WikiModel
                .toHtml("start <includeonly> test </includeonly> end"));
    }

    @Test public void test004() {
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

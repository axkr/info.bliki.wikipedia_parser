package info.bliki.wiki.filter;

import info.bliki.wiki.model.WikiModel;
import org.junit.Test;

import java.io.StringWriter;

import static org.fest.assertions.api.Assertions.assertThat;

public class ToHtmlTest extends FilterTestSupport {

    @Test public void test001() {
        assertThat(WikiModel.toHtml("This is a simple [[Hello World]] wiki tag")).isEqualTo("\n" + "<p>This is a simple <a href=\"/Hello_World\" title=\"Hello World\">Hello World</a> wiki tag</p>");
    }

    @Test public void test002() throws Exception {
        StringWriter writer = new StringWriter();
        WikiModel.toHtml("This is a simple [[Hello World]] wiki tag", writer);
        writer.flush();
        assertThat(writer
                .toString()).isEqualTo("\n" + "<p>This is a simple <a href=\"/Hello_World\" title=\"Hello World\">Hello World</a> wiki tag</p>");
    }

    @Test public void test003() {
        // the toHtml() method does not do the template parsing step
        assertThat(WikiModel
                .toHtml("start <includeonly> test </includeonly> end")).isEqualTo("\n" + "<p>start &#60;includeonly&#62; test &#60;/includeonly&#62; end</p>");
    }

    @Test public void test004() throws Exception {
        StringWriter writer = new StringWriter();
        WikiModel model = new WikiModel("/${image}", "/${title}");
        // the toText() method with last parameter true, does the template parsing
        // step
        WikiModel.toText(model, new HTMLConverter(), "start <includeonly> test </includeonly> end", writer, false, true);
        writer.flush();
        assertThat(writer.toString()).isEqualTo("\n" + "<p>start  test  end</p>");
    }
}

package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class JavaTest extends FilterTestSupport {

    @Test public void testJava001() {
        String result = wikiModel.render("'''Java Example'''\n" + "<source lang=java>\n" + "public class Test {\n" + "< > \" \' &"
                + "}\n" + "</source>", false);
        assertThat(result).isEqualTo("\n" +
                "<p><b>Java Example</b>\n" +
                "</p><pre class=\"java\">\n" +
                "<span style=\"color:#7F0055; font-weight: bold; \">public</span> <span style=\"color:#7F0055; font-weight: bold; \">class</span> Test {\n" +
                "&#60; &#62; <span style=\"color:#2A00FF; \">&#34; &#39; &#38;}\n" +
                "</span></pre>");
    }

    @Test public void testJava002() {
        String result = wikiModel.render("'''Java Example'''\n" + "<source lang=java>"
                + "Util util = new Util(\"c:\\\\temp\\\\\");\n" + "util.doIt();" + "</source>", false);

        assertThat(result).isEqualTo("\n" +
                "<p><b>Java Example</b>\n" +
                "</p><pre class=\"java\">Util util = <span style=\"color:#7F0055; font-weight: bold; \">new</span> Util(<span style=\"color:#2A00FF; \">&#34;c:\\\\temp\\\\&#34;</span>);\n" +
                "util.doIt();</pre>");
    }
}

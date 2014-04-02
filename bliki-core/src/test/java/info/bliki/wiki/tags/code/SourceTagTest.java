package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class SourceTagTest extends FilterTestSupport {
    @Test public void testUnknownSourceTag001() {
        String result = wikiModel.render("start <source lang=unknown>Hello World</source> end", false);

        assertEquals("\n" + "<p>start </p><pre class=\"unknown\">Hello World</pre> end", result);
    }

    @Test public void testUnknownSourceTag002() {
        String result = wikiModel.render("start <source lang=unknown>first line\n second line\n <html> third line\n</source> end", false);

        assertEquals("\n" + "<p>start </p><pre class=\"unknown\">first line\n" + " second line\n" + " &lt;html&gt; third line\n"
                + "</pre> end", result);
    }

    /**
     * Test escaping of JavaSyript
     */
    @Test public void testUnknownSourceTag003() {
        final String result = wikiModel.render("'''Example'''\n" + "<source lang=unknown>\n"
                + "<form><input type=\"button\" onclick=\"alert('Are you sure you want to do this?')\" value=\"Alert\"></form>\n"
                + "</source>", false);
        String expect = "\n"
                + "<p><b>Example</b>\n"
                + "</p><pre class=\"unknown\">\n"
                + "&lt;form&gt;&lt;input type=&#34;button&#34; onclick=&#34;alert(&#39;Are you sure you want to do this?&#39;)&#34; value=&#34;Alert&#34;&gt;&lt;/form&gt;\n"
                + "</pre>";
        assertEquals("SQL test002", expect, result);
    }
}

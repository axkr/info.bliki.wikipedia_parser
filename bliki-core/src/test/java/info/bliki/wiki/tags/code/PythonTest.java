package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PythonTest extends FilterTestSupport {
    @Test public void testPython() throws Exception {
        String result = wikiModel.render("<source lang=python>\n" + "# Python: \"Hello, world!\"\n" + "print \"Hello, world!\"\n"
                + "# last comment line" + "</source>", false);

        assertThat(result).isEqualTo("<pre class=\"python\">\n" +
                "<span style=\"color:#3F7F5F; \"># Python: &#34;Hello, world!&#34;\n" +
                "</span><span style=\"color:#7F0055; \">print</span> <span style=\"color:#2A00FF; \">&#34;Hello, world!&#34;</span>\n" +
                "<span style=\"color:#3F7F5F; \"># last comment line</span></pre>");
    }

}

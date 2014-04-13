package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ABAPTest extends FilterTestSupport {

    @Test public void testABAP() {
        String result = wikiModel.render("'''ABAP Example'''\n" + "<source lang=\"abap\">\n" + "*--- line comment\n"
                + "*--- line comment\n" + "WRITE: / '''Hello World''' \"test comment\n" + "< > \" \' &" + "}\n" + "</source>", false);

        assertThat(result).isEqualTo("\n" +
                "<p><b>ABAP Example</b>\n" +
                "</p><pre class=\"abap\"><span style=\"color:#3F7F5F; \">\n" +
                "*--- line comment\n" +
                "</span><span style=\"color:#3F7F5F; \">\n" +
                "*--- line comment\n" +
                "</span>\n" +
                "<span style=\"color:#7F0055; font-weight: bold; \">WRITE</span>: / <span style=\"color:#2A00FF; \">&#39;&#39;&#39;Hello World&#39;&#39;&#39;</span> <span style=\"color:#3F7F5F; \">&#34;test comment\n" +
                "</span>&#60; &#62; <span style=\"color:#3F7F5F; \">&#34; &#39; &#38;}\n" +
                "</span></pre>");
    }

    @Test public void testABAPWithoutLangAttr() {
        String result = wikiModel.render("'''ABAP Example'''\n" + "<source> REPORT ZZTEST00\n" + "*--- line comment\n"
                + "*--- line comment\n" + "WRITE: / '''Hello World''' \"test comment\n" + "< > \" \' &" + "}\n" + "</source>", false);

        assertThat(result).isEqualTo("\n" +
                "<p><b>ABAP Example</b>\n" +
                "</p><pre class=\"abap\"> <span style=\"color:#7F0055; font-weight: bold; \">REPORT</span> ZZTEST00<span style=\"color:#3F7F5F; \">\n" +
                "*--- line comment\n" +
                "</span><span style=\"color:#3F7F5F; \">\n" +
                "*--- line comment\n" +
                "</span>\n" +
                "<span style=\"color:#7F0055; font-weight: bold; \">WRITE</span>: / <span style=\"color:#2A00FF; \">&#39;&#39;&#39;Hello World&#39;&#39;&#39;</span> <span style=\"color:#3F7F5F; \">&#34;test comment\n" +
                "</span>&#60; &#62; <span style=\"color:#3F7F5F; \">&#34; &#39; &#38;}\n" +
                "</span></pre>");
    }

}

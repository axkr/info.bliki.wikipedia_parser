package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PHPTest extends FilterTestSupport
{
    @Test public void testPHP() throws Exception {
        String result = wikiModel.render("<source lang=php>\n" + "<?php\n" + "/* A simple php script */\n" + " \n" + "$choice = $_GET[\'foo\'];\n"
                + "if ( $choice == 1 )\n" + "{\n" + "  echo \"Hello world\";\n" + "}\n" + "# end of script\n" + "?>\n" + "</source>", false);

        assertThat(result).isEqualTo("<pre class=\"php\">\n" +
                "<span style=\"color:#7F0055; font-weight: bold; \">&#60;?php</span>\n" +
                "<span style=\"color:#3F7F5F; \">/* A simple php script */</span>\n" +
                " \n" +
                "$choice = $_GET[<span style=\"color:#2A00FF; \">&#39;foo&#39;</span>];\n" +
                "<span style=\"color:#7F0055; font-weight: bold; \">if</span> ( $choice == 1 )\n" +
                "{\n" +
                "  <span style=\"color:#7F0055; font-weight: bold; \">echo</span> <span style=\"color:#2A00FF; \">&#34;Hello world&#34;</span>;\n" +
                "}\n" +
                "# end of script\n" +
                "<span style=\"color:#7F0055; font-weight: bold; \">?&#62;</span>\n" +
                "</pre>");
    }
}

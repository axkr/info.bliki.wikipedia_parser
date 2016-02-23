package info.bliki.wiki.filter;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeFilterTest extends FilterTestSupport {

    @Test public void testTime001() throws Exception {
        assertThat(wikiModel.render("{{#time: d F Y | 29 Feb 2004 }}", false)).isEqualTo("\n" + "<p>29 February 2004</p>");
    }

    @Test public void testTime002() throws Exception {
        assertThat(wikiModel.render("{{#time: Y-m-d | 19 Oct 2011}}")).isEqualTo("\n" + "<p>2011-10-19</p>");
    }

    @Test public void testTime003() throws Exception {
        assertThat(wikiModel.render("{{#time: [[Y]] m d | 19 Oct 2011}}"))
              .isEqualTo("\n<p><a href=\"http://www.bliki.info/wiki/2011\" title=\"2011\">2011</a> 10 19</p>");
    }

    @Test public void testTime004() throws Exception {
        assertThat(wikiModel.render("{{#time: [[Y (year)]] | 19 Oct 2011 }}"))
                .isEqualTo("\n<p><a href=\"http://www.bliki.info/wiki/2011_(11eamWed%2C_19_Oct_2011_00:00:00_%2B0000)\" title=\"2011 (11eamWed, 19 Oct 2011 00:00:00 +0000)\">2011 (11eamWed, 19 Oct 2011 00:00:00 +0000)</a></p>"
                );
    }

    @Test public void testTime005() throws Exception {
        assertThat(wikiModel.render("{{#time: [[Y \"(year)\"]] | 2011 }}")).isEqualTo(
        "\n<p><a href=\"http://www.bliki.info/wiki/2011_%22(year)%22\" title=\"2011 &#34;(year)&#34;\">2011 &#34;(year)&#34;</a></p>"
        );
    }

    @Ignore @Test public void testTime006() throws Exception {
        assertThat(wikiModel.render("{{#time: i's\" }}")).isEqualTo("\n<p>38&#39;34&#34;</p>");
    }

    @Ignore @Test public void testTime007() throws Exception {
        assertThat(wikiModel.render("{{#time: r|now}}")).isEqualTo("\n<p>Fri, 13 Apr 2012 19:31:04 +0000</p>");
    }
}

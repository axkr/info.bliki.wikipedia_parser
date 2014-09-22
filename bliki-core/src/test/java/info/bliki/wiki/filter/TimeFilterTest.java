package info.bliki.wiki.filter;

import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JUnit tests for time parser function. Uncomment the test method below if you
 * would test a new time formatter.
 *
 */
public class TimeFilterTest extends FilterTestSupport {

    @Test public void testTime001() {
        // TODO implement more options for time
        assertThat(wikiModel.render("{{#time: d F Y | 29 Feb 2004 }}", false)).isEqualTo("\n" + "<p>29 February 2004</p>");
    }

    @Ignore @Test public void testTime002() {
        assertThat(wikiModel.render("{{#time: Y-m-d }}")).isEqualTo("\n" + "<p>2011-10-19</p>");
    }

    @Ignore @Test public void testTime003() {
        assertThat(wikiModel.render("{{#time: [[Y]] m d }}"))
              .isEqualTo("\n<p><a href=\"http://www.bliki.info/wiki/2011\" title=\"2011\">2011</a> 10 19</p>");
    }

    @Ignore @Test public void testTime004() {
        assertThat(wikiModel.render("{{#time: [[Y (year)]] }}"))
                .isEqualTo("\n<p><a href=\"http://www.bliki.info/wiki/2011_(11epmWed%2C_19_Oct_2011_18:28:04_%2B0000)\" title=\"2011 (11epmWed, 19 Oct 2011 18:28:04 +0000)\">2011 (11epmWed, 19 Oct 2011 18:28:04 +0000)</a></p>"
                );
    }

    @Ignore @Test public void testTime005() {
        assertThat(wikiModel.render("{{#time: [[Y \"(year)\"]] }}")).isEqualTo(
        "\n<p><a href=\"http://www.bliki.info/wiki/2011_%22(year)%22\" title=\"2011 &#34;(year)&#34;\">2011 &#34;(year)&#34;</a></p>"
        );
    }

    @Ignore @Test public void testTime006() {
        assertThat(wikiModel.render("{{#time: i's\" }}")).isEqualTo("\n<p>38&#39;34&#34;</p>");
    }

    @Ignore @Test public void testTime007() {
        assertThat(wikiModel.render("{{#time: r|now}}")).isEqualTo("\n<p>Fri, 13 Apr 2012 19:31:04 +0000</p>");
    }

}

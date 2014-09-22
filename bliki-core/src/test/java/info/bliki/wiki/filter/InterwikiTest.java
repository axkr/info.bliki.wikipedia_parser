package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InterwikiTest extends FilterTestSupport {

    @Test public void test001() {
        assertThat(wikiModel.render("Interwiki [[disinfopedia:link]] link test", false)).isEqualTo("\n" +
                "<p>Interwiki <a href=\"http://www.disinfopedia.org/wiki.phtml?title=link\">disinfopedia:link</a> link test</p>");
    }
    @Test public void test002() {
        assertThat(wikiModel.render("Interwiki [[freebsdman:link]] link test", false)).isEqualTo("\n" +
                "<p>Interwiki <a href=\"http://www.FreeBSD.org/cgi/man.cgi?apropos=1&query=link\">freebsdman:link</a> link test</p>");
    }
}

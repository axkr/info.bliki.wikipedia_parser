package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class InterwikiTest extends FilterTestSupport {

    @Test public void test001() {
        assertEquals("\n" +
                "<p>Interwiki <a href=\"http://www.disinfopedia.org/wiki.phtml?title=link\">disinfopedia:link</a> link test</p>", wikiModel.render("Interwiki [[disinfopedia:link]] link test", false));
    }
    @Test public void test002() {
        assertEquals("\n" +
                "<p>Interwiki <a href=\"http://www.FreeBSD.org/cgi/man.cgi?apropos=1&query=link\">freebsdman:link</a> link test</p>", wikiModel.render("Interwiki [[freebsdman:link]] link test", false));
    }
}

package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HTTPUrlFilterTest extends FilterTestSupport {
    /**
     * Test for issue 89
     */
    @Test public void testIssue89() throws Exception {
        assertThat(wikiModel.render("start [//en.wikipedia.org/w/index.php?title=Main_Page] end", false)).isEqualTo("\n"
                + "<p>start <a class=\"external autonumber\" href=\"//en.wikipedia.org/w/index.php?title=Main_Page\" rel=\"nofollow\">[1]</a> end</p>");
    }

    /**
     * Test for issue 90, Issue 91
     */
    @Test public void testIssue90() throws Exception {
        assertThat(wikiModel.render("start [http://www.google.com] middle [http://www.google.de] end", false)).isEqualTo("\n"
                + "<p>start <a class=\"external autonumber\" href=\"http://www.google.com\" rel=\"nofollow\">[1]</a> middle <a class=\"external autonumber\" href=\"http://www.google.de\" rel=\"nofollow\">[2]</a> end</p>");
    }

    @Test public void testIssue90a() throws Exception {
        assertThat(wikiModel.render("start [  http://www.google.com Google] middle [\nhttp://www.google.de Google DE] end", false)).isEqualTo("\n"
                + "<p>start [  <a class=\"external free\" href=\"http://www.google.com\" rel=\"nofollow\">http://www.google.com</a> Google] middle [\n"
                + "<a class=\"external free\" href=\"http://www.google.de\" rel=\"nofollow\">http://www.google.de</a> Google DE] end</p>");
    }

    @Test public void testIssue90b() throws Exception {
        assertThat(wikiModel.render("start [www.google.de Google DE ] end", false)).isEqualTo("\n" +
                "<p>start [www.google.de Google DE ] end</p>");
    }

    @Test public void testUrlHTTP() throws Exception {
        assertThat(wikiModel.render("see http://www.plog4u.de", false)).isEqualTo("\n"
                + "<p>see <a class=\"external free\" href=\"http://www.plog4u.de\" rel=\"nofollow\">http://www.plog4u.de</a></p>");
    }

    @Test public void testUrlHTTP001() throws Exception {
        assertThat(wikiModel.render("'''http://bla.blub.com'''", false)).isEqualTo("\n"
                + "<p><b><a class=\"external free\" href=\"http://bla.blub.com\" rel=\"nofollow\">http://bla.blub.com</a></b></p>");
    }

    @Test public void testUrlFTP() throws Exception {
        assertThat(wikiModel.render("see ftp://www.plog4u.de", false)).isEqualTo("\n"
                + "<p>see <a class=\"external free\" href=\"ftp://www.plog4u.de\" rel=\"nofollow\">ftp://www.plog4u.de</a></p>");
    }

    @Test public void testUrl2() throws Exception {
        assertThat(wikiModel.render("see http://www.plog4u.de/index.php&test_me", false)).isEqualTo("\n"
                + "<p>see <a class=\"external free\" href=\"http://www.plog4u.de/index.php&#38;test_me\" rel=\"nofollow\">http://www.plog4u.de/index.php&#38;test_me</a></p>");
    }

    @Test public void testUrl3() throws Exception {
        assertThat(wikiModel.render("# Bare URL: http://www.nupedia.com/", false)).isEqualTo("\n"
                + "<ol>\n"
                + "<li>Bare URL: <a class=\"external free\" href=\"http://www.nupedia.com/\" rel=\"nofollow\">http://www.nupedia.com/</a></li>\n</ol>");
    }

    @Test public void testUrl4() throws Exception {
        assertThat(wikiModel
                .render(
                        "Bericht über die Weltkulturerbe-Bewerbung von [ftp://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&amp;key=standard&key=standard_document_7782534 www.hr-online.de?rubrik=5676&key=standard] vom 13.&nbsp;Juli 2005",
                        false)).isEqualTo("\n"
                + "<p>Bericht über die Weltkulturerbe-Bewerbung von <a class=\"external text\" href=\"ftp://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&#38;key=standard&#38;key=standard_document_7782534\" rel=\"nofollow\">www.hr-online.de?rubrik=5676&#38;key=standard</a> vom 13. Juli 2005</p>");
    }

    @Test public void testUrl5() throws Exception {
        assertThat(wikiModel
                .render(
                        "Bericht über die Weltkulturerbe-Bewerbung von [http://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&amp;key=standard&key=standard_document_7782534 www.hr-online.de?rubrik=5676&key=standard] vom 13.&nbsp;Juli 2005",
                        false)).isEqualTo("\n"
                + "<p>Bericht über die Weltkulturerbe-Bewerbung von <a class=\"external text\" href=\"http://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&#38;key=standard&#38;key=standard_document_7782534\" rel=\"nofollow\">www.hr-online.de?rubrik=5676&#38;key=standard</a> vom 13. Juli 2005</p>");
    }

    @Test public void testImageUrl() throws Exception {
        assertThat(wikiModel.render("see http://www.plog4u.de/image.gif", false)).isEqualTo("\n"
                + "<p>see <a class=\"external free\" href=\"http://www.plog4u.de/image.gif\" rel=\"nofollow\">http://www.plog4u.de/image.gif</a></p>");
    }

    @Test public void testISBN() throws Exception {
        assertThat(wikiModel.render("Note that the numbers are not automatically made clickable until they are in this format:\n" + "\n"
                        + ":ISBN 1413304540\n" + "\n" + ":ISBN 978-1413304541 \'\'\'\'\'or\'\'\'\'\' ISBN 9781413304541 (without the dash)",
                false
        )).isEqualTo("\n"
                + "<p>Note that the numbers are not automatically made clickable until they are in this format:</p>\n"
                + "\n"
                + "<dl>\n"
                + "<dd><a class=\"external text\" href=\"http://www.amazon.com/exec/obidos/ASIN/1413304540\" rel=\"nofollow\" title=\"http://www.amazon.com/exec/obidos/ASIN/1413304540\">ISBN 1413304540</a></dd>\n</dl>\n"
                + "\n"
                + "\n"
                + "<dl>\n"
                + "<dd><a class=\"external text\" href=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\" rel=\"nofollow\" title=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\">ISBN 978-1413304541</a> <i><b>or</b></i> <a class=\"external text\" href=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\" rel=\"nofollow\" title=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\">ISBN 9781413304541</a> (without the dash)</dd>\n</dl>");
    }

    @Test public void testMailto001() throws Exception {
        assertThat(wikiModel.render("Linking to an e-mail address works the same way: \n" + "mailto:someone@domain.com or \n"
                + "[mailto:someone@domain.com someone]", false)).isEqualTo("\n"
                + "<p>Linking to an e-mail address works the same way: \n"
                + "<a class=\"external free\" href=\"mailto:someone@domain.com\" rel=\"nofollow\" title=\"mailto:someone@domain.com\">mailto:someone@domain.com</a> or \n"
                + "<a class=\"external free\" href=\"mailto:someone@domain.com\" rel=\"nofollow\" title=\"mailto:someone@domain.com\">someone</a></p>");
    }

    @Test public void testMailto002() throws Exception {
        assertThat(wikiModel.render("Linking to an e-mail address works the same way: \n" + "maILto:someone@domain.com or \n"
                + "[mAilTo:someone@domain.com someone]", false)).isEqualTo("\n"
                + "<p>Linking to an e-mail address works the same way: \n"
                + "<a class=\"external free\" href=\"maILto:someone@domain.com\" rel=\"nofollow\" title=\"maILto:someone@domain.com\">maILto:someone@domain.com</a> or \n"
                + "<a class=\"external free\" href=\"mAilTo:someone@domain.com\" rel=\"nofollow\" title=\"mAilTo:someone@domain.com\">someone</a></p>");
    }

    @Test public void testMailto003() throws Exception {
        assertThat(wikiModel.render("Linking to an e-mail address works the same way: \n" + "mailto:info@example.org?Subject=URL%20Encoded%20Subject&body=Body%20Text or \n"
                + "[mailto:info@example.org?Subject=URL%20Encoded%20Subject&body=Body%20Text info]", false)).isEqualTo("\n"
                + "<p>Linking to an e-mail address works the same way: \n"
                + "<a class=\"external free\" href=\"mailto:info@example.org?Subject=URL%20Encoded%20Subject&body=Body%20Text\" rel=\"nofollow\" title=\"mailto:info@example.org?Subject=URL%20Encoded%20Subject&body=Body%20Text\">mailto:info@example.org?Subject=URL%20Encoded%20Subject&#38;body=Body%20Text</a> or \n"
                + "<a class=\"external free\" href=\"mailto:info@example.org?Subject=URL%20Encoded%20Subject&body=Body%20Text\" rel=\"nofollow\" title=\"mailto:info@example.org?Subject=URL%20Encoded%20Subject&body=Body%20Text\">info</a></p>");
    }

    @Test public void testWrongMailto() throws Exception {
        assertThat(wikiModel.render("Linking to an e-mail address works the same way: \n"
                + "mailto:some one@domain.com or \n" + "[mailto:some one@domain.com someone]", false)).isEqualTo("\n" + "<p>Linking to an e-mail address works the same way: \n" + "mailto:some one@domain.com or \n"
                + "[mailto:some one@domain.com someone]</p>");
    }

    @Test public void testWrongMailto2() throws Exception {
        assertThat(wikiModel.render("mailto someone@domain.com or \n" + "[mailto someone@domain.com someone]", false)).isEqualTo("\n" +
                "<p>mailto someone@domain.com or \n" +
                "[mailto someone@domain.com someone]</p>");
    }

    @Test public void testUrlWithSpan() throws Exception {
        assertThat(wikiModel
                .render(
                        "[http://en.wikipedia.org/w/index.php?title=Template%3AMilwaukee+Brewers+roster+navbox&action=edit <span style=\"color:#002bb8;;background:#0a2351; color:#c9b074;;border:none;;\" title=\"You can edit this template. Please use the preview button before saving.\">e</span>]",
                        false)).isEqualTo("\n"
                + "<p><a class=\"external text\" href=\"http://en.wikipedia.org/w/index.php?title=Template%3AMilwaukee+Brewers+roster+navbox&#38;action=edit\" rel=\"nofollow\"><span style=\"color:#002bb8;;background:#0a2351; color:#c9b074;;border:none;;\" title=\"You can edit this template. Please use the preview button before saving.\">e</span></a></p>");
    }

    @Test public void testUrlTEL01() throws Exception {
        assertThat(wikiModel
                .render("call tel:+0815-4711", false)).isEqualTo("\n"
                + "<p>call <a class=\"telephonelink\" href=\"tel:+0815-4711\" title=\"tel:+0815-4711\">tel:+0815-4711</a></p>");
    }

    @Test public void testUrlTEL02() throws Exception {
        assertThat(wikiModel
                .render("call [tel:+0815-4711 a phone number]", false)).isEqualTo("\n"
                + "<p>call <a class=\"telephonelink\" href=\"tel:+0815-4711\" title=\"tel:+0815-4711\">a phone number</a></p>");
    }

    @Test public void testUrlTEL03() throws Exception {
        assertThat(wikiModel
                .render("call [tEl:+0815-4711 a phone number]", false)).isEqualTo("\n"
                + "<p>call <a class=\"telephonelink\" href=\"tEl:+0815-4711\" title=\"tEl:+0815-4711\">a phone number</a></p>");
    }
}

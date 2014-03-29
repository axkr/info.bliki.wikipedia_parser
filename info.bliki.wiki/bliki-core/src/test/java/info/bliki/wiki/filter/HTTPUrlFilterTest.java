package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class HTTPUrlFilterTest extends FilterTestSupport {
	public HTTPUrlFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(HTTPUrlFilterTest.class);
	}

	/**
	 * Test for issue 89
	 */
	public void testIssue89() {
		assertEquals(
				"\n"
						+ "<p>start <a class=\"external autonumber\" href=\"//en.wikipedia.org/w/index.php?title=Main_Page\" rel=\"nofollow\">[1]</a> end</p>",
				wikiModel.render("start [//en.wikipedia.org/w/index.php?title=Main_Page] end", false));
	}

	/**
	 * Test for issue 90, Issue 91
	 */
	public void testIssue90() {
		assertEquals(
				"\n"
						+ "<p>start <a class=\"external autonumber\" href=\"http://www.google.com\" rel=\"nofollow\">[1]</a> middle <a class=\"external autonumber\" href=\"http://www.google.de\" rel=\"nofollow\">[2]</a> end</p>",
				wikiModel.render("start [http://www.google.com] middle [http://www.google.de] end", false));
	}

	public void testIssue90a() {
		assertEquals(
				"\n"
						+ "<p>start [  <a class=\"external free\" href=\"http://www.google.com\" rel=\"nofollow\">http://www.google.com</a> Google] middle [\n"
						+ "<a class=\"external free\" href=\"http://www.google.de\" rel=\"nofollow\">http://www.google.de</a> Google DE] end</p>",
				wikiModel.render("start [  http://www.google.com Google] middle [\nhttp://www.google.de Google DE] end", false));
	}

	public void testIssue90b() {
		assertEquals("\n" + 
				"<p>start [www.google.de Google DE ] end</p>", wikiModel.render("start [www.google.de Google DE ] end", false));
	}

	public void testUrlHTTP() {
		assertEquals(
				"\n"
						+ "<p>see <a class=\"external free\" href=\"http://www.plog4u.de\" rel=\"nofollow\">http://www.plog4u.de</a></p>",
				wikiModel.render("see http://www.plog4u.de", false));
	}

	public void testUrlHTTP001() {
		assertEquals(
				"\n"
						+ "<p><b><a class=\"external free\" href=\"http://bla.blub.com\" rel=\"nofollow\">http://bla.blub.com</a></b></p>",
				wikiModel.render("'''http://bla.blub.com'''", false));
	}

	public void testUrlFTP() {
		assertEquals(
				"\n"
						+ "<p>see <a class=\"external free\" href=\"ftp://www.plog4u.de\" rel=\"nofollow\">ftp://www.plog4u.de</a></p>",
				wikiModel.render("see ftp://www.plog4u.de", false));
	}

	public void testUrl2() {
		assertEquals(
				"\n"
						+ "<p>see <a class=\"external free\" href=\"http://www.plog4u.de/index.php&#38;test_me\" rel=\"nofollow\">http://www.plog4u.de/index.php&#38;test_me</a></p>",
				wikiModel.render("see http://www.plog4u.de/index.php&test_me", false));
	}

	public void testUrl3() {
		assertEquals(
				"\n"
						+ "<ol>\n"
						+ "<li>Bare URL: <a class=\"external free\" href=\"http://www.nupedia.com/\" rel=\"nofollow\">http://www.nupedia.com/</a></li>\n</ol>",
				wikiModel.render("# Bare URL: http://www.nupedia.com/", false));
	}

	public void testUrl4() {
		assertEquals(
				"\n"
						+ "<p>Bericht über die Weltkulturerbe-Bewerbung von <a class=\"external text\" href=\"ftp://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&#38;key=standard&#38;key=standard_document_7782534\" rel=\"nofollow\">www.hr-online.de?rubrik=5676&#38;key=standard</a> vom 13. Juli 2005</p>",
				wikiModel
						.render(
								"Bericht über die Weltkulturerbe-Bewerbung von [ftp://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&amp;key=standard&key=standard_document_7782534 www.hr-online.de?rubrik=5676&key=standard] vom 13.&nbsp;Juli 2005",
								false));
	}

	public void testUrl5() {
		assertEquals(
				"\n"
						+ "<p>Bericht über die Weltkulturerbe-Bewerbung von <a class=\"external text\" href=\"http://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&#38;key=standard&#38;key=standard_document_7782534\" rel=\"nofollow\">www.hr-online.de?rubrik=5676&#38;key=standard</a> vom 13. Juli 2005</p>",
				wikiModel
						.render(
								"Bericht über die Weltkulturerbe-Bewerbung von [http://www.hr-online.de/website/rubriken/kultur/index.jsp?rubrik=5676&amp;key=standard&key=standard_document_7782534 www.hr-online.de?rubrik=5676&key=standard] vom 13.&nbsp;Juli 2005",
								false));
	}

	public void testImageUrl() {
		assertEquals(
				"\n"
						+ "<p>see <a class=\"external free\" href=\"http://www.plog4u.de/image.gif\" rel=\"nofollow\">http://www.plog4u.de/image.gif</a></p>",
				wikiModel.render("see http://www.plog4u.de/image.gif", false));
	}

	public void testISBN() {
		assertEquals(
				"\n"
						+ "<p>Note that the numbers are not automatically made clickable until they are in this format:</p>\n"
						+ "\n"
						+ "<dl>\n"
						+ "<dd><a class=\"external text\" href=\"http://www.amazon.com/exec/obidos/ASIN/1413304540\" rel=\"nofollow\" title=\"http://www.amazon.com/exec/obidos/ASIN/1413304540\">ISBN 1413304540</a></dd>\n</dl>\n"
						+ "\n"
						+ "\n"
						+ "<dl>\n"
						+ "<dd><a class=\"external text\" href=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\" rel=\"nofollow\" title=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\">ISBN 978-1413304541</a> <b><i>or</i></b> <a class=\"external text\" href=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\" rel=\"nofollow\" title=\"http://www.amazon.com/exec/obidos/ASIN/9781413304541\">ISBN 9781413304541</a> (without the dash)</dd>\n</dl>",
				wikiModel.render("Note that the numbers are not automatically made clickable until they are in this format:\n" + "\n"
						+ ":ISBN 1413304540\n" + "\n" + ":ISBN 978-1413304541 \'\'\'\'\'or\'\'\'\'\' ISBN 9781413304541 (without the dash)",
						false));
	}

	public void testMailto001() {
		assertEquals(
				"\n"
						+ "<p>Linking to an e-mail address works the same way: \n"
						+ "<a class=\"external free\" href=\"mailto:someone@domain.com\" rel=\"nofollow\" title=\"mailto:someone@domain.com\">mailto:someone@domain.com</a> or \n"
						+ "<a class=\"external free\" href=\"mailto:someone@domain.com\" rel=\"nofollow\" title=\"mailto:someone@domain.com\">someone</a></p>",
				wikiModel.render("Linking to an e-mail address works the same way: \n" + "mailto:someone@domain.com or \n"
						+ "[mailto:someone@domain.com someone]", false));
	}

	public void testMailto002() {
		assertEquals(
				"\n"
						+ "<p>Linking to an e-mail address works the same way: \n"
						+ "<a class=\"external free\" href=\"maILto:someone@domain.com\" rel=\"nofollow\" title=\"maILto:someone@domain.com\">maILto:someone@domain.com</a> or \n"
						+ "<a class=\"external free\" href=\"mAilTo:someone@domain.com\" rel=\"nofollow\" title=\"mAilTo:someone@domain.com\">someone</a></p>",
				wikiModel.render("Linking to an e-mail address works the same way: \n" + "maILto:someone@domain.com or \n"
						+ "[mAilTo:someone@domain.com someone]", false));
	}

	public void testWrongMailto() {
		assertEquals("\n" + "<p>Linking to an e-mail address works the same way: \n" + "mailto:some one@domain.com or \n"
				+ "[mailto:some one@domain.com someone]</p>", wikiModel.render("Linking to an e-mail address works the same way: \n"
				+ "mailto:some one@domain.com or \n" + "[mailto:some one@domain.com someone]", false));
	}

	public void testWrongMailto2() {
	  	assertEquals("\n" +
	      "<p>mailto someone@domain.com or \n" +
		    "[mailto someone@domain.com someone]</p>", wikiModel.render("mailto someone@domain.com or \n" + "[mailto someone@domain.com someone]", false));
	}
	
	public void testUrlWithSpan() {
		assertEquals(
				"\n"
						+ "<p><a class=\"external text\" href=\"http://en.wikipedia.org/w/index.php?title=Template%3AMilwaukee+Brewers+roster+navbox&#38;action=edit\" rel=\"nofollow\"><span style=\"color:#002bb8;;background:#0a2351; color:#c9b074;;border:none;;\" title=\"You can edit this template. Please use the preview button before saving.\">e</span></a></p>",
				wikiModel
						.render(
								"[http://en.wikipedia.org/w/index.php?title=Template%3AMilwaukee+Brewers+roster+navbox&action=edit <span style=\"color:#002bb8;;background:#0a2351; color:#c9b074;;border:none;;\" title=\"You can edit this template. Please use the preview button before saving.\">e</span>]",
								false));
	}

	public void testUrlTEL01() {
		assertEquals("\n"
				+ "<p>call <a class=\"telephonelink\" href=\"tel:+0815-4711\" title=\"tel:+0815-4711\">tel:+0815-4711</a></p>", wikiModel
				.render("call tel:+0815-4711", false));
	}

	public void testUrlTEL02() {
		assertEquals("\n"
				+ "<p>call <a class=\"telephonelink\" href=\"tel:+0815-4711\" title=\"tel:+0815-4711\">a phone number</a></p>", wikiModel
				.render("call [tel:+0815-4711 a phone number]", false));
	}

	public void testUrlTEL03() {
		assertEquals("\n"
				+ "<p>call <a class=\"telephonelink\" href=\"tEl:+0815-4711\" title=\"tEl:+0815-4711\">a phone number</a></p>", wikiModel
				.render("call [tEl:+0815-4711 a phone number]", false));
	}
}
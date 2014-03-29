package info.bliki.html;

import info.bliki.html.HTML2WikiConverter;
import info.bliki.html.googlecode.ToMoinMoin;
import junit.framework.TestCase;

public class HTML2MoinMoinTest extends TestCase {

	public HTML2MoinMoinTest(String name) {
		super(name);
	}

	public void test0() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML("<b>hello<em>world</em></b>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "'''hello''world'''''");
	}

	public void test1() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv
				.setInputHTML("<ul><li>hello world<ol><li>hello subworld1<ul><li>sub sub test1</li>\n<li>sub sub test2</li></ul></li><li>hello subworld2</li></ol></li><li>second line</li></ul>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "\n" + "* hello world\n" + " 1. hello subworld1\n" + "  * sub sub test1\n"
				+ "  * sub sub test2\n" + " 1. hello subworld2\n" + "* second line\n" + "");
	}

	public void test2() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		// invalid nested html test
		conv.setInputHTML("<ul><li>hello world<ol><li>hello subworld</ol><li>second line</ul>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "\n" + "* hello world\n" + " 1. hello subworld\n" + "* second line\n" + "");
	}

	public void test3() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		// invalid nested html test
		conv.setInputHTML("<h2>the good</h2><h3>the bad</h3><h2>and the ugly</h2>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "\n" + "== the good ==\n" + "\n" + "=== the bad ===\n" + "\n" + "== and the ugly ==\n");
	}

	public void test4() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		// invalid nested html test
		conv.setInputHTML("<table>\n" + "<tr>\n"
				+ "<td><i><a href=\"/wiki/Klasse_%28Biologie%29\" title=\"Klasse (Biologie)\">Klasse</a>:</i></td>\n"
				+ "<td><a href=\"/wiki/V%C3%B6gel\" title=\"Vögel\">Vögel</a> (Aves)</td>\n" + "</tr>\n" + "<tr>\n"
				+ "<td><i><a href=\"/wiki/Klasse_%28Biologie%29\" title=\"Klasse (Biologie)\">Unterklasse</a>:</i></td>\n"
				+ "<td><a href=\"/wiki/Urkieferv%C3%B6gel\" title=\"Urkiefervögel\">Urkiefervögel</a> (Palaeognathae)</td>\n" + "\n"
				+ "</tr>\n" + "<tr>\n"
				+ "<td><i><a href=\"/wiki/Ordnung_%28Biologie%29\" title=\"Ordnung (Biologie)\">Ordnung</a>:</i></td>\n"
				+ "<td><a href=\"/wiki/Laufv%C3%B6gel\" title=\"Laufvögel\">Laufvögel</a> (Struthioniformes)</td>\n" + "</tr>\n" + "<tr>\n"
				+ "<td><i><a href=\"/wiki/Familie_%28Biologie%29\" title=\"Familie (Biologie)\">Familie</a>:</i></td>\n"
				+ "<td>Strauße (Struthionidae)</td>\n" + "</tr>\n" + "\n" + "<tr>\n"
				+ "<td><i><a href=\"/wiki/Gattung_%28Biologie%29\" title=\"Gattung (Biologie)\">Gattung</a>:</i></td>\n"
				+ "<td>Strauße (<i>Struthio</i>)</td>\n" + "</tr>\n" + "<tr>\n"
				+ "<td><i><a href=\"/wiki/Art_%28Biologie%29\" title=\"Art (Biologie)\">Art</a>:</i></td>\n"
				+ "<td>Afrikanischer Strauß</td>\n" + "</tr>\n" + "</table>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "                         \n" + 
				"||\'\'[[Klasse]]:\'\'||[[Vögel]] (Aves)||\n" + 
				"||\'\'[[Unterklasse]]:\'\'||[[Urkiefervögel]] (Palaeognathae)||\n" + 
				"||\'\'[[Ordnung]]:\'\'||[[Laufvögel]] (Struthioniformes)||\n" + 
				"||\'\'[[Familie]]:\'\'||Strauße (Struthionidae)||\n" + 
				"||\'\'[[Gattung]]:\'\'||Strauße (\'\'Struthio\'\')||\n" + 
				"||\'\'[[Art]]:\'\'||Afrikanischer Strauß||\n" + 
				"");
	}

	public void test6() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML("<b>hello</b> <em>world</em>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "'''hello''' ''world''");
	}

	public void test7() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML("<html><body><table>\n<tr>\n<td>hello world</td>\n</tr></table></body></html>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "   \n" + "||hello world||\n");
	}

	public void test8() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML("<font size=\"1\">hello</font> <em>world</em>");
		String result = conv.toWiki(new ToMoinMoin(true, true));
		assertEquals(result, "hello ''world''");
	}

	public void test9() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML("<h2>hello \n world\n</h2>");
		String result = conv.toWiki(new ToMoinMoin(true, true));
		assertEquals(result, "\n" + "== hello   world ==\n");
	}

	public void test10() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML("<b>hello&nbsp;<em>world</em></b>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "'''hello ''world'''''");
	}

	public void test11() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML("<h2> \n \n</h2>");
		String result = conv.toWiki(new ToMoinMoin(true, true));
		assertEquals(result, "");
	}

	public void test12() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML("<b> </b> <em> </em>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "   ");
	}

	public void test13() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		conv.setInputHTML("<div> </div>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, " ");
	}

	public void test14() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		// invalid nested html test
		conv
				.setInputHTML("<h2>the <a href=\"http://good\">good</a></h2><h3>the <div>bad</div></h3><h2>and <b><i>the</i> ugly</b></h2>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "\n" + "== the good ==\n" + "\n" + "=== the bad ===\n" + "\n" + "== and the ugly ==\n");
	}

	public void test15() {
		HTML2WikiConverter conv = new HTML2WikiConverter();
		// invalid nested html test
		conv.setInputHTML("The <a href=\"http://good\">good</a> the <div>bad</div> and <b><i>the</i> ugly</b>");
		String result = conv.toWiki(new ToMoinMoin());
		assertEquals(result, "The [[good]] the bad and '''''the'' ugly'''");
	}

	public static void main(String[] args) {
		try {
			HTML2WikiConverter conv = new HTML2WikiConverter();
			conv.setInputHTML("<b>hello</b> <em>world</em>");
			System.out.print(conv.toWiki(new ToMoinMoin()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

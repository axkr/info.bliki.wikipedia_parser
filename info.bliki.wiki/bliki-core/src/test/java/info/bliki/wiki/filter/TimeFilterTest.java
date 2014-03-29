package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * JUnit tests for time parser function. Uncomment the test method below if you
 * would test a new time formatter.
 * 
 */
public class TimeFilterTest extends FilterTestSupport {
	public TimeFilterTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public static Test suite() {
		return new TestSuite(TimeFilterTest.class);
	}

	public void testTime001() {
		// TODO implement more options for time
		assertEquals("\n" + "<p>29 February 2004</p>", wikiModel.render("{{#time: d F Y | 29 Feb 2004 }}", false));
	}

	public void testTime002() {
		// assertEquals("\n" + "<p>2011-10-19</p>",
		// wikiModel.render("{{#time: Y-m-d }}"));
	}

	public void testTime003() {
		// assertEquals("\n" +
		// "<p><a href=\"http://www.bliki.info/wiki/2011\" title=\"2011\">2011</a> 10 19</p>",
		// wikiModel
		// .render("{{#time: [[Y]] m d }}"));
	}

	public void testTime004() {
		// assertEquals("\n" +
		// "<p><a href=\"http://www.bliki.info/wiki/2011_(11epmWed%2C_19_Oct_2011_18:28:04_%2B0000)\" title=\"2011 (11epmWed, 19 Oct 2011 18:28:04 +0000)\">2011 (11epmWed, 19 Oct 2011 18:28:04 +0000)</a></p>",
		// wikiModel.render("{{#time: [[Y (year)]] }}"));
	}

	public void testTime005() {
		// assertEquals(
		// "\n"
		// +
		// "<p><a href=\"http://www.bliki.info/wiki/2011_%22(year)%22\" title=\"2011 &#34;(year)&#34;\">2011 &#34;(year)&#34;</a></p>",
		// wikiModel.render("{{#time: [[Y \"(year)\"]] }}"));
	}

	public void testTime006() {
		// assertEquals("\n" + "<p>38&#39;34&#34;</p>",
		// wikiModel.render("{{#time: i's\" }}"));
	}

	// current time dependent test
	// public void testTime007() {
	// assertEquals("\n" + "<p>Fri, 13 Apr 2012 19:31:04 +0000</p>",
	// wikiModel.render("{{#time: r|now}}"));
	// }

}
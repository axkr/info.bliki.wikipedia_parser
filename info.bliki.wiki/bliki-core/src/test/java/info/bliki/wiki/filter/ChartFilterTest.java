package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ChartFilterTest extends FilterTestSupport {
	public ChartFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(ChartFilterTest.class);
	}

	public void testChart001() {
		assertEquals(
				"\n"
						+ "<p><img border=\"0\" src=\"http://chart.apis.google.com/chart?chbh=20%2C1&amp;chco=76A4FB&amp;chd=t%3A1%2C2%2C3%2C6%2C9%2C13%2C20%2C28%2C37%2C49%2C60%2C72%2C83%2C92%2C98%2C100%2C98%2C92%2C83%2C72%2C60%2C49%2C37%2C28%2C20%2C13%2C9%2C6%2C3%2C2%2C1&amp;chls=2.0&amp;chs=800x300&amp;cht=bvs&amp;chxt=x%2Cy\" alt=\"\" /></p>",
				wikiModel
						.render("<chart \n" + 
								"  cht=bvs \n" + 
								"  chs=800x300 \n" + 
								"  chbh=20,1 \n" + 
								"  chxt=x,y \n" + 
								"  chco=76A4FB \n" + 
								"  chls=2.0   " +
								"  chd=t:1,2,3,6,9,13,20,28,37,49,60,72,83,92,98,100,98,92,83,72,60,49,37,28,20,13,9,6,3,2,1 \n" + 
								"/>", false));
	}

	public void testChart002() {
		// TeX formula
		assertEquals("\n" + 
				"<p><img border=\"0\" src=\"http://chart.apis.google.com/chart?chl=x+%3D+%5Cfrac%7B-b+%5Cpm+%5Csqrt+%7Bb%5E2-4ac%7D%7D%7B2a%7D&amp;cht=tx\" alt=\"\" /></p>", wikiModel.render("<chart cht=tx chl=\"x = \\frac{-b \\pm \\sqrt {b^2-4ac}}{2a}\" />", false));
	}

}
package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class BehaviorSwitchTest extends FilterTestSupport {
	public BehaviorSwitchTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(BehaviorSwitchTest.class);
	}

	public void test001() {
		assertEquals("\n" + 
				"<p>before  after</p>", wikiModel.render("before __NOEDITSECTION__ after", false));
	}
}
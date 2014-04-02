package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BehaviorSwitchTest extends FilterTestSupport {


    @Test public void test001() {
        assertEquals("\n" +
                "<p>before  after</p>", wikiModel.render("before __NOEDITSECTION__ after", false));
    }
}

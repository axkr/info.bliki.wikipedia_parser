package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BehaviorSwitchTest extends FilterTestSupport {


    @Test public void test001() {
        assertThat(wikiModel.render("before __NOEDITSECTION__ after", false)).isEqualTo("\n" +
                "<p>before  after</p>");
    }
}

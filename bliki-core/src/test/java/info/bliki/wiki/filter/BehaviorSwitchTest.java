package info.bliki.wiki.filter;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class BehaviorSwitchTest extends FilterTestSupport {


    @Test public void test001() {
        assertThat(wikiModel.render("before __NOEDITSECTION__ after", false)).isEqualTo("\n" +
                "<p>before  after</p>");
    }
}

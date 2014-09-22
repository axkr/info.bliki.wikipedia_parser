package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HTMLTableFilterTest extends FilterTestSupport {

  @Test public void testHTMLTable2() {
      assertThat(wikiModel.render("<table 250px>test</table>", false)).isEqualTo("\n" +
              "<table>\ntest\n</table>");
  }
}

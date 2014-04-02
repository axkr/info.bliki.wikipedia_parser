package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class HTMLTableFilterTest extends FilterTestSupport {

  @Test public void testHTMLTable2() {
  assertEquals("\n" +
          "<table>\ntest\n</table>", wikiModel.render("<table 250px>test</table>", false));
  }
}

package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class TagFilterTest extends FilterTestSupport {

  @Test public void testWrongTag1() {
    assertEquals("\n" +
            "<pre>madfkfj </pre>hg", wikiModel.render("<pre>madfkfj </pre>hg", false));
  }

  @Test public void testWrongTag2() {
    assertEquals("\n" +
            "<pre>madfkfj </pre>hg", wikiModel.render("<pre>madfkfj </pRE>hg", false));
  }

  @Test public void testWrongTag3() {
    assertEquals("\n" +
            "<pre>madfk&lt;/prefj </pre>hg", wikiModel.render("<pre>madfk</prefj </pRE>hg", false));
  }
}

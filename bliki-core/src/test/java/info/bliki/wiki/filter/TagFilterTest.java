package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TagFilterTest extends FilterTestSupport {

  @Test public void testWrongTag1() throws Exception {
      assertThat(wikiModel.render("<pre>madfkfj </pre>hg", false)).isEqualTo("\n" +
              "<pre>madfkfj </pre>hg");
  }

  @Test public void testWrongTag2() throws Exception {
      assertThat(wikiModel.render("<pre>madfkfj </pRE>hg", false)).isEqualTo("\n" +
              "<pre>madfkfj </pre>hg");
  }

  @Test public void testWrongTag3() throws Exception {
      assertThat(wikiModel.render("<pre>madfk</prefj </pRE>hg", false)).isEqualTo("\n" +
              "<pre>madfk&lt;/prefj </pre>hg");
  }
}

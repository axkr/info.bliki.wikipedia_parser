package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WrongTagFilterTest extends FilterTestSupport {

  @Test public void testWrongTag1() throws Exception {
      assertThat(wikiModel.render("<blubber>...", false)).isEqualTo("\n" +
              "<p>&#60;blubber&#62;...</p>");
  }
}

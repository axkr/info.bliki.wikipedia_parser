package info.bliki.wiki.filter;

import org.fest.assertions.api.Assertions;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class WrongTagFilterTest extends FilterTestSupport {

//   @Test public void testWrongTag0() {
//    assertEquals("<p>[[Dülmener Wildpferde im Merfelder Bruch]]\n" +
//            "</p>", wikiModel.render("[[Image:Merfelder_Wildpferde.jpg|thumb|right|250px|[[Dülmener Wildpferd]]e im Merfelder Bruch]]", wikiModel));
//  }
  @Test public void testWrongTag1() {
      assertThat(wikiModel.render("<blubber>...", false)).isEqualTo("\n" +
              "<p>&#60;blubber&#62;...</p>");
  }
}

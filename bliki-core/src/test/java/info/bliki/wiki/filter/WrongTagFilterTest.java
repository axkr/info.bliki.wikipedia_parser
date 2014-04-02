package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class WrongTagFilterTest extends FilterTestSupport {

//   @Test public void testWrongTag0() {
//    assertEquals("<p>[[Dülmener Wildpferde im Merfelder Bruch]]\n" +
//            "</p>", wikiModel.render("[[Image:Merfelder_Wildpferde.jpg|thumb|right|250px|[[Dülmener Wildpferd]]e im Merfelder Bruch]]", wikiModel));
//  }
  @Test public void testWrongTag1() {
    assertEquals("\n" +
            "<p>&#60;blubber&#62;...</p>", wikiModel.render("<blubber>...", false));
  }
}

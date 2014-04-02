package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class MathFilterTest extends FilterTestSupport {

  @Test public void testMath() {
        assertEquals("\n" +
                "<p><span class=\"math\">\\sin x</span></p>", wikiModel.render("<math>\\sin x</math>", false));
    }

  @Test public void testMath0() {
    assertEquals("\n" +
            "<p><span class=\"math\">H(j\\omega)=A_h(\\omega)\\cdot e^{j\\phi_h(\\omega)}={1\\over{1+j\\omega t}}</span></p>", wikiModel.render("<math>H(j\\omega)=A_h(\\omega)\\cdot e^{j\\phi_h(\\omega)}={1\\over{1+j\\omega t}}</math>", false));
  }
}

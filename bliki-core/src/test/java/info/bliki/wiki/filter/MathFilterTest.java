package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MathFilterTest extends FilterTestSupport {

  @Test public void testMath() {
      assertThat(wikiModel.render("<math>\\sin x</math>", false)).isEqualTo("\n" +
              "<p><span class=\"math\">\\sin x</span></p>");
  }

  @Test public void testMath0() {
      assertThat(wikiModel.render("<math>H(j\\omega)=A_h(\\omega)\\cdot e^{j\\phi_h(\\omega)}={1\\over{1+j\\omega t}}</math>", false)).isEqualTo("\n" +
              "<p><span class=\"math\">H(j\\omega)=A_h(\\omega)\\cdot e^{j\\phi_h(\\omega)}={1\\over{1+j\\omega t}}</span></p>");
  }
}

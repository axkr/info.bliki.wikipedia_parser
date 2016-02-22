package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EncoderTest {
    @Test
    public void testIsUrlIdentifierPart() throws Exception {
        assertThat(Encoder.isUrlIdentifierPart('\\')).isTrue();
    }

    @Test
    public void testEncodeNamespacePage() throws Exception {
        assertThat(Encoder.encodeTitleToUrl("Foo:Bar", false)).isEqualTo("Foo:Bar");
    }
}

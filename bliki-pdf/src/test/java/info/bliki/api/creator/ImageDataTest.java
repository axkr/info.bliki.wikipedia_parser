package info.bliki.api.creator;

import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class ImageDataTest {

    @Test public void shouldImplementEqualsByName() throws Exception {
        assertThat(new ImageData("foo")).isEqualTo(new ImageData("foo"));
        assertThat(new ImageData("foo")).isNotEqualTo(new ImageData("bar"));
    }

    @Test public void shouldImplementEqualsByUrl() throws Exception {
        assertThat(new ImageData("foo", "url")).isEqualTo(new ImageData("foo", "url"));
        assertThat(new ImageData("foo", "url")).isNotEqualTo(new ImageData("foo", "otherUrl"));
    }

    @Test public void shouldImplementEqualsByFilename() throws Exception {
        assertThat(new ImageData("foo", "url", new File("file"))).isEqualTo(new ImageData("foo", "url", new File("file")));
        assertThat(new ImageData("foo", "url", new File("file"))).isNotEqualTo(new ImageData("foo", "url", new File("otherfile")));
    }
}

package info.bliki.api.creator;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class TopicDataTest {

    @Test public void shouldImplementEqualsByName() throws Exception {
        assertThat(new TopicData("foo")).isEqualTo(new TopicData("foo"));
        assertThat(new TopicData("foo")).isNotEqualTo(new TopicData("bar"));
    }

    @Test public void shouldImplementEqualsByContent() throws Exception {
        assertThat(new TopicData("foo", "content")).isEqualTo(new TopicData("foo", "content"));
        assertThat(new TopicData("foo", "content")).isNotEqualTo(new TopicData("foo", "othercontent"));
    }
}

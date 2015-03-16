package info.bliki.api.creator;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

public class WikiDBTest {
    private File tempDir;

    @Before public void before() throws IOException {
        tempDir = Files.createTempDirectory(getClass().getName()).resolve("db").toFile();
    }

    @Test public void shouldCreateDatabase() throws Exception {
        WikiDB db = new WikiDB(tempDir);
        TopicData data = db.selectTopic("test");
        assertThat(data).isNull();
    }

    @Test public void shouldInsertTopic() throws Exception {
        WikiDB db = new WikiDB(tempDir);
        TopicData topicData = new TopicData("test", "someContent");

        db.insertTopic(topicData);

        TopicData selected = db.selectTopic("test");
        assertThat(selected).isEqualTo(topicData);
    }

    @Test public void shouldInsertNewImage() throws Exception {
        WikiDB db = new WikiDB(tempDir);
        ImageData imageData = new ImageData("name", "http://foo.com", new File("/foo"));
        db.insertImage(imageData);
        ImageData selected = db.selectImage("name");

        assertThat(selected).isEqualTo(imageData);
    }
}

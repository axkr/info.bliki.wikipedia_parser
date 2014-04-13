package info.bliki.extensions.scribunto.template;

import info.bliki.wiki.model.WikiModel;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

public class ModuleExecutorTest {
    private ModuleExecutor subject;

    @Before
    public void before() {
        subject = new ModuleExecutor();
    }

    @Test public void testRun() throws IOException {
        Map<String, String> templateParams = new LinkedHashMap<>();
        templateParams.put("1", "foo");

        Frame frame = new Frame(templateParams, null);
        assertThat("3").isEqualTo(subject.run(new WikiModel("${image}", "${title}"), "Module:string", "len", frame));
    }
}

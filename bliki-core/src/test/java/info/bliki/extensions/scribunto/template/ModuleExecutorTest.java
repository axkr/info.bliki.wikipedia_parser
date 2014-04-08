package info.bliki.extensions.scribunto.template;

import info.bliki.wiki.model.WikiModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
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
        assertEquals(subject.run(new WikiModel("${image}", "${title}"), "Module:string", "len", frame), "3");
    }
}

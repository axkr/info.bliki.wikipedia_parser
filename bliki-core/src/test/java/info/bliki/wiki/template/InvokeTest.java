package info.bliki.wiki.template;

import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.extensions.scribunto.template.ModuleExecutor;
import info.bliki.wiki.model.IWikiModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvokeTest {
    private Invoke subject;
    private @Mock IWikiModel model;
    private @Mock ModuleExecutor moduleExecutor;

    @Before public void before() {
        subject = new Invoke();
        initMocks(this);
        when(model.getModuleExecutor()).thenReturn(moduleExecutor);
    }

    @Test public void testParseFunction() throws Exception {
        List<String> parts = Arrays.asList("moduleName", "method", "anotherParameter");
        final String source = "source";
        when(moduleExecutor.run(same(model), eq("moduleName"), eq("method"), any(Frame.class))).thenReturn("expanded");
        String result = subject.parseFunction(parts, model, source.toCharArray(), 0, source.length(), false);
        assertThat(result).isEqualTo("expanded");
    }

    @Test public void testParseFunctionWithExtraWhitespace() throws Exception {
        List<String> parts = Arrays.asList("  moduleName  ", "method\n\n");
        final String source = "source";
        when(moduleExecutor.run(same(model), eq("moduleName"), eq("method"), any(Frame.class))).thenReturn("expanded");
        String result = subject.parseFunction(parts, model, source.toCharArray(), 0, source.length(), false);
        assertThat(result).isEqualTo("expanded");
    }
}

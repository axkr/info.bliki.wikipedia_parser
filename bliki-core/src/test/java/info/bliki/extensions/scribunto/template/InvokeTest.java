package info.bliki.extensions.scribunto.template;

import info.bliki.extensions.scribunto.engine.ScribuntoEngine;
import info.bliki.extensions.scribunto.engine.ScribuntoEngineModule;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.namespaces.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvokeTest {
    private Invoke subject;
    private @Mock IWikiModel model;
    private @Mock ScribuntoEngine scribuntoEngine;
    private @Mock ScribuntoEngineModule scribuntoModule;

    @Before public void before() {
        subject = new Invoke();
        initMocks(this);
        when(model.getScribuntoEngine()).thenReturn(scribuntoEngine);
        when(model.getNamespace()).thenReturn(new Namespace());
        when(model.getFrame()).thenReturn(new Frame(null, null));
        when(scribuntoEngine.fetchModuleFromParser(any(Title.class))).thenReturn(scribuntoModule);
    }

    @Test public void testParseFunction() throws Exception {
        List<String> parts = Arrays.asList("moduleName", "method", "anotherParameter");
        final String source = "source";
        when(scribuntoModule.invoke(eq("method"), any(Frame.class))).thenReturn("expanded");
        String result = subject.parseFunction(parts, model, source.toCharArray(), 0, source.length(), false);
        assertThat(result).isEqualTo("expanded");
    }

    @Test public void testParseFunctionWithExtraWhitespace() throws Exception {
        List<String> parts = Arrays.asList("  moduleName  ", "method\n\n");
        final String source = "source";
        when(scribuntoModule.invoke(eq("method"), any(Frame.class))).thenReturn("expanded");
        String result = subject.parseFunction(parts, model, source.toCharArray(), 0, source.length(), false);
        assertThat(result).isEqualTo("expanded");
    }

}

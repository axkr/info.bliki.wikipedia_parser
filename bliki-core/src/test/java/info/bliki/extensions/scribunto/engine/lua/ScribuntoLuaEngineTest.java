package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.extensions.scribunto.ScribuntoException;
import info.bliki.extensions.scribunto.engine.ScribuntoModule;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.luaj.vm2.LuaError;
import org.mockito.Mock;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("unchecked")
public class ScribuntoLuaEngineTest {
    @Mock private IWikiModel model;
    private ScribuntoLuaEngine subject;

    @Before public void setUp() throws Exception {
        initMocks(this);
        when(model.getNamespace()).thenReturn(new Namespace());
        subject = new ScribuntoLuaEngine(model, CompiledScriptCache.DONT_CACHE);
    }

    @Test(expected = ScribuntoException.class)
    public void testFetchModuleFromParserWhenModuleDoesNOTExistReturnsNull() throws Exception {
        when(model.getRawWikiContent(any(ParsedPageName.class), isNull(Map.class)))
            .thenThrow(new WikiModelContentException(null, null));
        subject.fetchModuleFromParser("doesNotExit");
    }

    @Test public void testFetchModuleFromParserWhenModuleExistsReturnsModuleWhichCanBeInvoked() throws Exception {
        when(model.getRawWikiContent(any(ParsedPageName.class), anyMap())).thenReturn(
            "return { " +
            "   test_function = function() return 'result' end" +
            "}");

        ScribuntoModule module = subject.fetchModuleFromParser("testModule");

        String result = module.invoke("test_function", new Frame(null, null, null, false));
        assertThat(result).isEqualTo("result");
    }

    @Test public void testWhenUsingCompiledScriptCacheItReturnsModuleFromCache() throws Exception {
        final CompiledScriptCache cache = new CompiledScriptCache();
        subject = new ScribuntoLuaEngine(model, cache);

        final String pageCode =
            "return { " +
            "   test_function = function() return 'result' end " +
            "}";


        when(model.getRawWikiContent(any(ParsedPageName.class), anyMap()))
            .thenReturn(pageCode)
            .thenThrow(new RuntimeException("should only be called once"));

        ScribuntoModule module = subject.fetchModuleFromParser("testModule");
        assertThat(module.invoke("test_function", new Frame(null, null, null, false))).isEqualTo("result");
        ScribuntoModule cachedModule = subject.fetchModuleFromParser("testModule");
        assertThat(cachedModule.invoke("test_function", new Frame(null, null, null, false))).isEqualTo("result");

        assertThat(cachedModule).isNotSameAs(module);
    }

    @Test public void testExecutionErrorsHaveUsefulDebugInformation() throws Exception {
        when(model.getRawWikiContent(any(ParsedPageName.class), anyMap())).thenReturn(
            "return {" +
            "   test_function = function() error('testingErrorHandling') end " +
            "}");

        ScribuntoModule module = subject.fetchModuleFromParser("testModuleWithError");

        try {
            module.invoke("test_function", new Frame(null, null, null, false));
            fail("expected LuaError");
        } catch (LuaError e) {
            // expected
        }
    }
}

package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.extensions.scribunto.engine.ScribuntoModule;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.extensions.scribunto.template.Title;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import info.bliki.wiki.filter.ParsedPageName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("unchecked")
public class ScribuntoLuaEngineTest {
    @Mock private IWikiModel model;
    private ScribuntoLuaEngine subject;
    private Namespace namespace = new Namespace();

    @Before public void setUp() throws Exception {
        initMocks(this);
        when(model.getNamespace()).thenReturn(new Namespace());
        subject = new ScribuntoLuaEngine(model);
    }

    @Test public void testFetchModuleFromParserWhenModuleDoesNOTExistReturnsNull() throws Exception {
        when(model.getRawWikiContent(any(ParsedPageName.class), isNull(Map.class)))
                .thenThrow(new WikiModelContentException(null, null));

        ScribuntoModule module =
                subject.fetchModuleFromParser(Title.makeTitle(namespace.getMain(), "test", "", ""));

        assertThat(module).isNull();
    }

    @Test public void testFetchModuleFromParserWhenModuleExistsReturnsModuleWhichCanBeInvoked() throws Exception {
        when(model.getRawWikiContent(any(ParsedPageName.class), anyMap())).thenReturn("return { test_function = function() return \"result\" end }");

        ScribuntoModule module =
                subject.fetchModuleFromParser(Title.makeTitle(namespace.getModule(), "string", "", ""));

        String result = module.invoke("test_function", new Frame(null, null));
        assertThat(result).isEqualTo("result");
    }
}

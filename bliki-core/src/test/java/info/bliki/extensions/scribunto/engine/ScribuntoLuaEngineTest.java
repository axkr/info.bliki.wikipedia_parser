package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.interfaces.ScribuntoLuaEngine;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.extensions.scribunto.template.Title;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import static info.bliki.wiki.filter.AbstractParser.ParsedPageName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScribuntoLuaEngineTest {
    @Mock private IWikiModel model;
    private ScribuntoLuaEngine subject;

    @Before public void setUp() throws Exception {
        initMocks(this);
        when(model.getNamespace()).thenReturn(new Namespace());
        subject = new ScribuntoLuaEngine(model);
    }

    @Test public void testFetchModuleFromParserWhenModuleDoesNOTExistReturnsNull() throws Exception {
        when(model.getRawWikiContent(any(ParsedPageName.class), isNull(Map.class)))
                .thenThrow(new WikiModelContentException(null, null));

        ScribuntoModule module =
                subject.fetchModuleFromParser(Title.makeTitle(new Namespace().getMain(), "test", "", ""));

        assertThat(module).isNull();
    }

    @Test public void testFetchModuleFromParserWhenModuleExistsReturnsModuleWhichCanBeInvoked() throws Exception {
        when(model.getRawWikiContent(any(ParsedPageName.class), anyMap())).thenReturn("return { test_function = function() return \"result\" end }");

        ScribuntoModule module =
                subject.fetchModuleFromParser(Title.makeTitle(new Namespace().getModule(), "string", "", ""));

        String result = module.invoke("test_function", new Frame(null, null));
        assertThat(result).isEqualTo("result");
    }
}

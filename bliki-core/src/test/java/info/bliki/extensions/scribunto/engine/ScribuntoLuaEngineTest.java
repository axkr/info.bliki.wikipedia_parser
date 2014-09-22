package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.template.Title;
import info.bliki.wiki.filter.AbstractParser;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@SuppressWarnings("unchecked")
public class ScribuntoLuaEngineTest {
    @Mock private IWikiModel model;
    private ScribuntoLuaEngine subject;

    @Before public void setUp() throws Exception {
        initMocks(this);
        subject = new ScribuntoLuaEngine(model);
    }


    @Test public void testFetchModuleFromParserWhenModuleDoesNOTExistReturnsNull() throws Exception {

        when(model.getRawWikiContent(any(AbstractParser.ParsedPageName.class), isNull(Map.class)))
                .thenThrow(new WikiModelContentException(null, null));

        ScribuntoEngineModule module =
                subject.fetchModuleFromParser(Title.makeTitle(new Namespace().getMain(), "test", "", ""));

        assertThat(module).isNull();
    }

    @Test public void testFetchModuleFromParserWhenModuleExists() throws Exception {
        ScribuntoEngineModule module =
                subject.fetchModuleFromParser(Title.makeTitle(new Namespace().getMain(), "test", "", ""));

        assertThat(module).isNotNull();
    }
}

package info.bliki.wiki.impl;

import info.bliki.api.Page;
import info.bliki.api.Revision;
import info.bliki.api.User;
import info.bliki.api.creator.TopicData;
import info.bliki.api.creator.WikiDB;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.namespaces.Namespace;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Locale;

import info.bliki.wiki.filter.ParsedPageName;
import static info.bliki.wiki.filter.MagicWord.MagicWordE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class APIWikiModelTest {
    private APIWikiModel subject;
    private @Mock User user;
    private @Mock WikiDB wikiDB;
    private ParsedPageName modulePageName, templatePageName;
    private final INamespace ns = new Namespace();

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        modulePageName = new ParsedPageName(ns.getModule(), "moduleTestPage", true);
        templatePageName = new ParsedPageName(ns.getTemplate(), "templateTestPage", true);

        subject = new APIWikiModel(user, wikiDB, Locale.getDefault(), null, null, null);
    }

    @Test public void getRawWikiContentWithMagicWordReturnsContent() throws Exception {
        ParsedPageName magicParsedPage = new ParsedPageName(new Namespace().getMain(), "foo", MagicWordE.MAGIC_PAGE_NAME, null, true);
        String content = subject.getRawWikiContent(magicParsedPage, null);
        assertThat(content).isEqualTo("PAGENAME");
    }

    @Test(expected = WikiModelContentException.class) public void getRawWikiContentForTemplateRaisesWikiModelExceptionWhenSQLExceptionIsEncountered() throws Exception {
        when(wikiDB.selectTopic(templatePageName.fullPagename())).thenThrow(new SQLException());
        subject.getRawWikiContent(templatePageName, null);
    }

    @Test public void getRawWikiContentForTemplateRaisesWikiModelExceptionWithCorrectMessageWhenSQLExceptionIsEncountered() throws Exception {
        when(wikiDB.selectTopic(templatePageName.fullPagename())).thenThrow(new SQLException());
        try {
            subject.getRawWikiContent(templatePageName, null);
            fail("expected exception");
        } catch (WikiModelContentException e) {
            assertThat(e.getMessage()).isEqualTo("<span class=\"error\">Exception: SQLException</span>");
        }
    }


    @Test(expected = WikiModelContentException.class) public void getRawWikiContentForModuleRaisesWikiModelExceptionWhenSQLExceptionIsEncountered() throws Exception {
        when(wikiDB.selectTopic(modulePageName.fullPagename())).thenThrow(new SQLException());
        subject.getRawWikiContent(modulePageName, null);
    }

    @Test public void testWikiDBReturnsCachedContent() throws Exception {
        TopicData topicData = new TopicData("name", "content");
        when(wikiDB.selectTopic(modulePageName.fullPagename())).thenReturn(topicData);
        assertThat(subject.getRawWikiContent(modulePageName, null)).isEqualTo(topicData.getContent());
    }

    @Test public void testWikiDBReturnsNullContent() throws Exception {
        TopicData topicData = new TopicData("name", null);
        when(wikiDB.selectTopic(modulePageName.fullPagename())).thenReturn(topicData);
        assertThat(subject.getRawWikiContent(modulePageName, null)).isNull();
    }

    @Test public void testWikiDBReturnsNullContentAndUserFetchesData() throws Exception {
        when(wikiDB.selectTopic(modulePageName.fullPagename())).thenReturn(null);
        Page page = new Page();
        Revision revision = new Revision();
        revision.setContent("testContent");
        page.setCurrentRevision(revision);
        when(user.queryContent(modulePageName.fullPagename())).thenReturn(Arrays.asList(page));
        assertThat(subject.getRawWikiContent(modulePageName, null)).isEqualTo("testContent");
    }

    @Test public void testWikiDBReturnsNullTopicData() throws Exception {
        when(wikiDB.selectTopic(modulePageName.fullPagename())).thenReturn(null);
        assertThat(subject.getRawWikiContent(modulePageName, null)).isNull();
    }

    @Test public void getRawWikiContentReturnsNullIfNotTemplateNorModuleNorMagicWord() throws Exception {
        String content = subject.getRawWikiContent(new ParsedPageName(ns.getMain(), "whatever", true), null);
        assertThat(content).isNull();
    }
}

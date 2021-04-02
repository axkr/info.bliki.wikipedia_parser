package info.bliki.wiki.namespaces;

import info.bliki.wiki.namespaces.INamespace.NamespaceCode;
import info.bliki.wiki.namespaces.Namespace.NamespaceValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static info.bliki.wiki.namespaces.INamespace.NamespaceCode.*;
import static info.bliki.wiki.namespaces.INamespace.NamespaceCode.CITATIONS_NAMESPACE_KEY;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Namespace}.
 *
 * @author Nico Kruber, kruber@zib.de
 */
public class NamespaceTest {
    protected Namespace namespace = null;

    @Before
    public void setUp() throws Exception {
        namespace = new Namespace(Locale.ENGLISH);
    }

    /**
     * Checks whether all content spaces are set.
     */
    @Test public void testEnsureContentSpacesNotNull() {
        Namespace namespaceObj = new Namespace();
        for (NamespaceCode nsCode : NamespaceCode.values()) {
            NamespaceValue ns = namespaceObj.getNamespaceByNumber(nsCode.code);
            if (!ns.getCanonicalName().equals(namespace.TOPIC.getCanonicalName())) {
                assertThat(ns.getContentspace()).isNotNull();
            }
        }
    }

    @Test public void testNamespace001() {
        assertThat(namespace.getNamespace("Meta")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getNamespace("Meta_talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getNamespace("Meta talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getNamespace("Project")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getNamespace("Project_talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getNamespace("Project talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getNamespace("Wiktionary")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getNamespace("Wiktionary_talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getNamespace("Wiktionary talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getNamespace("WT")).isEqualTo(namespace.PROJECT_TALK);
    }

    @Test public void testTalkspace001() {
        assertThat(namespace.getTalkspace("Meta")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Meta_talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Meta talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Project")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Project_talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Project talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Wiktionary")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Wiktionary_talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Wiktionary talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("WT")).isEqualTo(namespace.PROJECT_TALK);
    }

    @Test public void testContentspace001() {
        assertThat(namespace.getContentspace("Meta")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Meta_talk")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Meta talk")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Project")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Project_talk")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Project talk")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Wiktionary")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Wiktionary_talk")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Wiktionary talk")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("WT")).isEqualTo(namespace.PROJECT);
    }

    @Test public void testOldAliases001() {
        namespace.getCategory_talk().setTexts("Kategorie Diskussion");
        assertThat(namespace.getNamespace("Category talk")).isEqualTo(null);
        assertThat(namespace.getNamespace("Category_talk")).isEqualTo(null);
    }

    @Test public void testCategoryNamespace() {
        assertThat(namespace.getNamespace("Category")).isEqualTo(namespace.CATEGORY);
        assertThat(namespace.getContentspace("Category_talk")).isEqualTo(namespace.CATEGORY);
        assertThat(namespace.getNamespace("Category talk")).isEqualTo(namespace.CATEGORY_TALK);
        assertThat(namespace.getNamespace("CAT")).isEqualTo(namespace.CATEGORY);
    }

    @Test public void testModuleNamespace() {
        assertThat(namespace.getNamespace("Module")).isEqualTo(namespace.MODULE);
        assertThat(namespace.getContentspace("Module_talk")).isEqualTo(namespace.MODULE);
        assertThat(namespace.getNamespace("Module_talk")).isEqualTo(namespace.MODULE_TALK);
        assertThat(namespace.getNamespace("MOD")).isEqualTo(namespace.MODULE);
    }

    @Test public void testPortalNamespace() {
        assertThat(namespace.getNamespace("Portal")).isEqualTo(namespace.PORTAL);
        assertThat(namespace.getContentspace("Portal_talk")).isEqualTo(namespace.PORTAL);
        assertThat(namespace.getNamespace("Portal_talk")).isEqualTo(namespace.PORTAL_TALK);
        assertThat(namespace.getNamespace("Appendix")).isEqualTo(namespace.PORTAL);
        assertThat(namespace.getContentspace("Appendix_talk")).isEqualTo(namespace.PORTAL);
        assertThat(namespace.getNamespace("Appendix_talk")).isEqualTo(namespace.PORTAL_TALK);
        assertThat(namespace.getNamespace("AP")).isEqualTo(namespace.PORTAL);
    }

    @Test public void testBookNamespace() {
        assertThat(namespace.getNamespace("Book")).isEqualTo(namespace.BOOK);
        assertThat(namespace.getContentspace("Book_talk")).isEqualTo(namespace.BOOK);
        assertThat(namespace.getNamespace("Book_talk")).isEqualTo(namespace.BOOK_TALK);
        assertThat(namespace.getNamespace("Transwiki")).isEqualTo(namespace.BOOK);
        assertThat(namespace.getContentspace("Transwiki_talk")).isEqualTo(namespace.BOOK);
        assertThat(namespace.getNamespace("Transwiki_talk")).isEqualTo(namespace.BOOK_TALK);
    }

    @Test public void testDraftNamespace() {
        assertThat(namespace.getNamespace("Draft")).isEqualTo(namespace.DRAFT);
        assertThat(namespace.getContentspace("Draft_talk")).isEqualTo(namespace.DRAFT);
        assertThat(namespace.getNamespace("Draft_talk")).isEqualTo(namespace.DRAFT_TALK);
        assertThat(namespace.getNamespace("Reconstruction")).isEqualTo(namespace.DRAFT);
        assertThat(namespace.getContentspace("Reconstruction_talk")).isEqualTo(namespace.DRAFT);
        assertThat(namespace.getNamespace("Reconstruction_talk")).isEqualTo(namespace.DRAFT_TALK);
        assertThat(namespace.getNamespace("RC")).isEqualTo(namespace.DRAFT);
    }

    @Test public void testEducationProgramNamespace() {
        assertThat(namespace.getNamespace("Education_Program")).isEqualTo(namespace.EP);
        assertThat(namespace.getContentspace("Education_Program_talk")).isEqualTo(namespace.EP);
        assertThat(namespace.getNamespace("Education_Program_talk")).isEqualTo(namespace.EP_TALK);
    }

    @Test public void testTimedTextNamespace() {
        assertThat(namespace.getNamespace("TimedText")).isEqualTo(namespace.TIMEDTEXT);
        assertThat(namespace.getContentspace("TimedText_talk")).isEqualTo(namespace.TIMEDTEXT);
        assertThat(namespace.getNamespace("TimedText_talk")).isEqualTo(namespace.TIMEDTEXT_TALK);
    }

    @Test public void testTopicNamespace() {
        assertThat(namespace.getNamespace("Topic")).isEqualTo(namespace.TOPIC);
    }

    @Test public void testCanonicalNamespace() throws Exception {
        assertThat(namespace.PROJECT_TALK.getCanonicalName()).isEqualTo("Project_talk");
        assertThat(new Namespace(Locale.GERMAN).PROJECT_TALK.getCanonicalName()).isEqualTo("Project_talk");
        assertThat(new Namespace(Locale.FRENCH).PROJECT_TALK.getCanonicalName()).isEqualTo("Project_talk");
    }

    @Test public void testTemplateNamespace() {
        assertThat(namespace.getNamespace("Template")).isEqualTo(namespace.TEMPLATE);
        assertThat(namespace.getContentspace("Template_talk")).isEqualTo(namespace.TEMPLATE);
        assertThat(namespace.getNamespace("Template_talk")).isEqualTo(namespace.TEMPLATE_TALK);
        assertThat(namespace.getContentspace("T")).isEqualTo(namespace.TEMPLATE);
    }

    @Test public void testThreadNamespace() {
        assertThat(namespace.getNamespace("Thread")).isEqualTo(namespace.THREAD);
        assertThat(namespace.getContentspace("Thread_talk")).isEqualTo(namespace.THREAD);
        assertThat(namespace.getNamespace("Thread_talk")).isEqualTo(namespace.THREAD_TALK);
    }

    @Test public void testSummaryNamespace() {
        assertThat(namespace.getNamespace("Summary")).isEqualTo(namespace.SUMMARY);
        assertThat(namespace.getContentspace("Summary_talk")).isEqualTo(namespace.SUMMARY);
        assertThat(namespace.getNamespace("Summary_talk")).isEqualTo(namespace.SUMMARY_TALK);
    }

    @Test public void testConcordanceNamespace() {
        assertThat(namespace.getNamespace("Concordance")).isEqualTo(namespace.CONCORDANCE);
        assertThat(namespace.getContentspace("Concordance_talk")).isEqualTo(namespace.CONCORDANCE);
        assertThat(namespace.getNamespace("Concordance_talk")).isEqualTo(namespace.CONCORDANCE_TALK);
    }

    @Test public void testIndexNamespace() {
        assertThat(namespace.getNamespace("Index")).isEqualTo(namespace.INDEX);
        assertThat(namespace.getContentspace("Index_talk")).isEqualTo(namespace.INDEX);
        assertThat(namespace.getNamespace("Index_talk")).isEqualTo(namespace.INDEX_TALK);
    }

    @Test public void testRhymesNamespace() {
        assertThat(namespace.getNamespace("Rhymes")).isEqualTo(namespace.RHYMES);
        assertThat(namespace.getContentspace("Rhymes_talk")).isEqualTo(namespace.RHYMES);
        assertThat(namespace.getNamespace("Rhymes_talk")).isEqualTo(namespace.RHYMES_TALK);
    }

    @Test public void testThesaurusNamespace() {
        assertThat(namespace.getNamespace("Thesaurus")).isEqualTo(namespace.THESAURUS);
        assertThat(namespace.getContentspace("Thesaurus_talk")).isEqualTo(namespace.THESAURUS);
        assertThat(namespace.getNamespace("Thesaurus_talk")).isEqualTo(namespace.THESAURUS_TALK);
        assertThat(namespace.getNamespace("Wikisaurus")).isEqualTo(namespace.THESAURUS);
        assertThat(namespace.getContentspace("Wikisaurus_talk")).isEqualTo(namespace.THESAURUS);
        assertThat(namespace.getNamespace("Wikisaurus_talk")).isEqualTo(namespace.THESAURUS_TALK);
        assertThat(namespace.getNamespace("WS")).isEqualTo(namespace.THESAURUS);
    }

    @Test public void testCitationsNamespace() {
        assertThat(namespace.getNamespace("Citations")).isEqualTo(namespace.CITATIONS);
        assertThat(namespace.getContentspace("Citations_talk")).isEqualTo(namespace.CITATIONS);
        assertThat(namespace.getNamespace("Citations_talk")).isEqualTo(namespace.CITATIONS_TALK);
    }

    @Test public void testSignGlossNamespace() {
        assertThat(namespace.getNamespace("Sign_gloss")).isEqualTo(namespace.SIGN_GLOSS);
        assertThat(namespace.getContentspace("Sign_gloss_talk")).isEqualTo(namespace.SIGN_GLOSS);
        assertThat(namespace.getNamespace("Sign_gloss_talk")).isEqualTo(namespace.SIGN_GLOSS_TALK);
    }
}

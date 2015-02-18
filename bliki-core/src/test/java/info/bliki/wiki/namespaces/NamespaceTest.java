package info.bliki.wiki.namespaces;

import info.bliki.wiki.namespaces.INamespace.NamespaceCode;
import info.bliki.wiki.namespaces.Namespace.NamespaceValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

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
    }

    @Test public void testTalkspace001() {
        assertThat(namespace.getTalkspace("Meta")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Meta_talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Meta talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Project")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Project_talk")).isEqualTo(namespace.PROJECT_TALK);
        assertThat(namespace.getTalkspace("Project talk")).isEqualTo(namespace.PROJECT_TALK);
    }

    @Test public void testContentspace001() {
        assertThat(namespace.getContentspace("Meta")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Meta_talk")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Meta talk")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Project")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Project_talk")).isEqualTo(namespace.PROJECT);
        assertThat(namespace.getContentspace("Project talk")).isEqualTo(namespace.PROJECT);
    }

    @Test public void testOldAliases001() {
        namespace.getCategory_talk().setTexts("Kategorie Diskussion");
        assertThat(namespace.getNamespace("Category talk")).isEqualTo(null);
        assertThat(namespace.getNamespace("Category_talk")).isEqualTo(null);
    }

    @Test public void testModuleNamespace() {
        assertThat(namespace.getNamespace("Module")).isEqualTo(namespace.MODULE);
        assertThat(namespace.getContentspace("Module_talk")).isEqualTo(namespace.MODULE);
        assertThat(namespace.getNamespace("Module_talk")).isEqualTo(namespace.MODULE_TALK);
    }

    @Test public void testBookNamespace() {
        assertThat(namespace.getNamespace("Book")).isEqualTo(namespace.BOOK);
        assertThat(namespace.getContentspace("Book_talk")).isEqualTo(namespace.BOOK);
        assertThat(namespace.getNamespace("Book_talk")).isEqualTo(namespace.BOOK_TALK);
    }

    @Test public void testDraftNamespace() {
        assertThat(namespace.getNamespace("Draft")).isEqualTo(namespace.DRAFT);
        assertThat(namespace.getContentspace("Draft_talk")).isEqualTo(namespace.DRAFT);
        assertThat(namespace.getNamespace("Draft_talk")).isEqualTo(namespace.DRAFT_TALK);
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
}

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
    @Test public void testNumberCodeConversion01() {
        for (NamespaceCode nsCode : NamespaceCode.values()) {
            assertThat(Namespace.intToNumberCode(Namespace.numberCodeToInt(nsCode.code))).isEqualTo((int) nsCode.code);
        }
    }

    /**
     * Checks whether all content spaces are set.
     */
    @Test public void testNumberCodeConversion02() {
        for (int i = 0; i <= NamespaceCode.values().length-1; ++i) {
            assertThat(Namespace.numberCodeToInt(Namespace.intToNumberCode(i))).isEqualTo(i);
        }
    }

    /**
     * Checks whether all content spaces are set.
     */
    @Test public void testEnsureContentSpacesNotNull() {
        Namespace namespaceObj = new Namespace();
        for (NamespaceCode nsCode : NamespaceCode.values()) {
            NamespaceValue namespace = namespaceObj.getNamespaceByNumber(nsCode.code);

            assertThat(namespace.getContentspace()).isNotNull();
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

    @Test public void testCanonicalNamespace() throws Exception {
        assertThat(namespace.PROJECT_TALK.getCanonicalName()).isEqualTo("Project_talk");
        assertThat(new Namespace(Locale.GERMAN).PROJECT_TALK.getCanonicalName()).isEqualTo("Project_talk");
        assertThat(new Namespace(Locale.FRENCH).PROJECT_TALK.getCanonicalName()).isEqualTo("Project_talk");
    }
}

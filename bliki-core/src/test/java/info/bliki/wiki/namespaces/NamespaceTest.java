package info.bliki.wiki.namespaces;

import info.bliki.wiki.namespaces.INamespace.NamespaceCode;
import info.bliki.wiki.namespaces.Namespace.NamespaceValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
            assertEquals((int) nsCode.code, Namespace.intToNumberCode(Namespace.numberCodeToInt(nsCode.code)));
        }
    }

    /**
     * Checks whether all content spaces are set.
     */
    @Test public void testNumberCodeConversion02() {
        for (int i = 0; i <= NamespaceCode.values().length-1; ++i) {
            assertEquals(i, Namespace.numberCodeToInt(Namespace.intToNumberCode(i)));
        }
    }

    /**
     * Checks whether all content spaces are set.
     */
    @Test public void testEnsureContentSpacesNotNull() {
        Namespace namespaceObj = new Namespace();
        for (NamespaceCode nsCode : NamespaceCode.values()) {
            NamespaceValue namespace = namespaceObj.getNamespaceByNumber(nsCode.code);
            assertNotNull("contentspace of " + nsCode + ", " + namespace, namespace.getContentspace());
        }
    }

    @Test public void testNamespace001() {
        assertEquals(namespace.PROJECT, namespace.getNamespace("Meta"));
        assertEquals(namespace.PROJECT_TALK, namespace.getNamespace("Meta_talk"));
        assertEquals(namespace.PROJECT_TALK, namespace.getNamespace("Meta talk"));
        assertEquals(namespace.PROJECT, namespace.getNamespace("Project"));
        assertEquals(namespace.PROJECT_TALK, namespace.getNamespace("Project_talk"));
        assertEquals(namespace.PROJECT_TALK, namespace.getNamespace("Project talk"));
    }

    @Test public void testTalkspace001() {
        assertEquals(namespace.PROJECT_TALK, namespace.getTalkspace("Meta"));
        assertEquals(namespace.PROJECT_TALK, namespace.getTalkspace("Meta_talk"));
        assertEquals(namespace.PROJECT_TALK, namespace.getTalkspace("Meta talk"));
        assertEquals(namespace.PROJECT_TALK, namespace.getTalkspace("Project"));
        assertEquals(namespace.PROJECT_TALK, namespace.getTalkspace("Project_talk"));
        assertEquals(namespace.PROJECT_TALK, namespace.getTalkspace("Project talk"));
    }

    @Test public void testContentspace001() {
        assertEquals(namespace.PROJECT, namespace.getContentspace("Meta"));
        assertEquals(namespace.PROJECT, namespace.getContentspace("Meta_talk"));
        assertEquals(namespace.PROJECT, namespace.getContentspace("Meta talk"));
        assertEquals(namespace.PROJECT, namespace.getContentspace("Project"));
        assertEquals(namespace.PROJECT, namespace.getContentspace("Project_talk"));
        assertEquals(namespace.PROJECT, namespace.getContentspace("Project talk"));
    }

    @Test public void testOldAliases001() {
        namespace.getCategory_talk().setTexts("Kategorie Diskussion");
        assertEquals(null, namespace.getNamespace("Category talk"));
        assertEquals(null, namespace.getNamespace("Category_talk"));
    }

    @Test public void testModuleNamespace() {
        assertEquals(namespace.MODULE, namespace.getNamespace("Module"));
        assertEquals(namespace.MODULE, namespace.getContentspace("Module_talk"));
        assertEquals(namespace.MODULE_TALK, namespace.getNamespace("Module_talk"));
    }

    @Test public void testCanonicalNamespace() throws Exception {
        assertEquals("Project_talk", namespace.PROJECT_TALK.getCanonicalName());
        assertEquals("Project_talk", new Namespace(Locale.GERMAN).PROJECT_TALK.getCanonicalName());
        assertEquals("Project_talk", new Namespace(Locale.FRENCH).PROJECT_TALK.getCanonicalName());
    }
}

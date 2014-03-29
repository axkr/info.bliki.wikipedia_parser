package info.bliki.wiki.namespaces;

import info.bliki.wiki.namespaces.INamespace.NamespaceCode;
import info.bliki.wiki.namespaces.Namespace.NamespaceValue;

import java.util.Locale;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests for {@link Namespace}.
 * 
 * @author Nico Kruber, kruber@zib.de
 */
public class NamespaceTest extends TestCase {
	protected Namespace namespace = null;
	
	public NamespaceTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(NamespaceTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		namespace = new Namespace(Locale.ENGLISH);
	}

	/**
	 * Checks whether all content spaces are set.
	 */
	public void testNumberCodeConversion01() {
		for (NamespaceCode nsCode : NamespaceCode.values()) {
			assertEquals((int) nsCode.code, Namespace.intToNumberCode(Namespace.numberCodeToInt(nsCode.code)));
		}
	}

	/**
	 * Checks whether all content spaces are set.
	 */
	public void testNumberCodeConversion02() {
		for (int i = 0; i <= 19; ++i) {
			assertEquals(i, Namespace.numberCodeToInt(Namespace.intToNumberCode(i)));
		}
	}

	/**
	 * Checks whether all content spaces are set.
	 */
	public void testEnsureContentSpacesNotNull() {
		Namespace namespaceObj = new Namespace();
		for (NamespaceCode nsCode : NamespaceCode.values()) {
			NamespaceValue namespace = namespaceObj.getNamespaceByNumber(nsCode.code);
			assertNotNull("contentspace of " + nsCode + ", " + namespace, namespace.getContentspace());
		}
	}

	public void testNamespace001() {
		assertEquals(namespace.META, namespace.getNamespace("Meta"));
		assertEquals(namespace.META_TALK, namespace.getNamespace("Meta_talk"));
		assertEquals(namespace.META_TALK, namespace.getNamespace("Meta talk"));
		assertEquals(namespace.META, namespace.getNamespace("Project"));
		assertEquals(namespace.META_TALK, namespace.getNamespace("Project_talk"));
		assertEquals(namespace.META_TALK, namespace.getNamespace("Project talk"));
	}

	public void testTalkspace001() {
		assertEquals(namespace.META_TALK, namespace.getTalkspace("Meta"));
		assertEquals(namespace.META_TALK, namespace.getTalkspace("Meta_talk"));
		assertEquals(namespace.META_TALK, namespace.getTalkspace("Meta talk"));
		assertEquals(namespace.META_TALK, namespace.getTalkspace("Project"));
		assertEquals(namespace.META_TALK, namespace.getTalkspace("Project_talk"));
		assertEquals(namespace.META_TALK, namespace.getTalkspace("Project talk"));
	}

	public void testContentspace001() {
		assertEquals(namespace.META, namespace.getContentspace("Meta"));
		assertEquals(namespace.META, namespace.getContentspace("Meta_talk"));
		assertEquals(namespace.META, namespace.getContentspace("Meta talk"));
		assertEquals(namespace.META, namespace.getContentspace("Project"));
		assertEquals(namespace.META, namespace.getContentspace("Project_talk"));
		assertEquals(namespace.META, namespace.getContentspace("Project talk"));
	}

	public void testOldAliases001() {
		namespace.getCategory_talk().setTexts("Kategorie Diskussion");
		assertEquals(null, namespace.getNamespace("Category talk"));
		assertEquals(null, namespace.getNamespace("Category_talk"));
	}
}

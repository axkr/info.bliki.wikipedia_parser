package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TemplateSubstParserTest extends FilterTestSupport {

	public TemplateSubstParserTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(TemplateSubstParserTest.class);
	}

	/**
	 * See <a
	 * href="http://en.wikipedia.org/wiki/Help:Substitution">Wikipedia-Help:
	 * Substitution</a>
	 */
	public void testSubst001() {
		assertEquals("", wikiModel.parseTemplates("{{subst:}}", false));
		assertEquals("a nested template text", wikiModel.parseTemplates("{{subst:Nested}}", false));
	}

	/**
	 * See <a
	 * href="http://en.wikipedia.org/wiki/Help:Substitution">Wikipedia-Help:
	 * Substitution</a>
	 */
	public void testSubst002() {
		assertEquals("File", wikiModel.parseTemplates("{{subst:ns:{{subst:#expr:2*3}}}}", false));
	}

	/**
	 * See <a
	 * href="http://en.wikipedia.org/wiki/Help:Substitution">Wikipedia-Help:
	 * Substitution</a>
	 */
	public void testSubst003() {
		assertEquals("File", wikiModel.parseTemplates("{{ns:{{subst:#expr:2*3}}}}", false));
	}

	/**
	 * See <a
	 * href="http://en.wikipedia.org/wiki/Help:Substitution">Wikipedia-Help:
	 * Substitution</a>
	 */
	public void testSubst004() {
		assertEquals("1.0e-5", wikiModel.parseTemplates("{{subst:LC:{{subst:#expr:1/100000}}}}", false));
	}

	/**
	 * See <a
	 * href="http://en.wikipedia.org/wiki/Help:Substitution">Wikipedia-Help:
	 * Substitution</a>
	 */
	public void testSubst005() {
		assertEquals("IN", wikiModel.parseTemplates("{{subst:UC:{{subst:tc}}}}", false));
	}

	public void testSubst006() { 
		// http://www.mediawiki.org/wiki/Manual:Substitution#Predefined_templates
		wikiModel.setNamespaceName("help");
		assertEquals("startHelpend", wikiModel.parseTemplates("{{subst:t1|{{subst:NAMESPACE}}}}", true));
		assertEquals("startHelpend", wikiModel.parseTemplates("{{subst:t1|{{subst:NAMESPACE}}}}", false));
		wikiModel.setNamespaceName("");
	}

	public void testSubst007() {
		assertEquals("yes", wikiModel.parseTemplates("{{subst:#if:{{x0}}|yes|no}}"));
	}

	public void testSubst008() {
		assertEquals("no", wikiModel.parseTemplates("{{subst:#if:{{subst:x0}}|yes|no}}"));
	}

	public void testSubst009() {
		assertEquals("6", wikiModel
				.parseTemplates("{{subst:#expr:2*{{{p|3}}}}}"));
	}
}
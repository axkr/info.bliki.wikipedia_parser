package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.tags.HTMLBlockTag;

import java.util.Locale;

import junit.framework.TestCase;

/**
 * Support class for defining JUnit FilterTests.
 * 
 */
public class FilterTestSupport extends TestCase {
	public static final String WINDOWS_NEWLINE = "\r\n";

	public static final String UNIX_NEWLINE = "\n";

	public static final String NEWLINE = WINDOWS_NEWLINE;

	protected WikiModel wikiModel = null;

	static {
		Configuration.DEFAULT_CONFIGURATION.addTokenTag("iframe", new HTMLBlockTag("iframe", Configuration.SPECIAL_BLOCK_TAGS));
	}

	public FilterTestSupport(String s) {
		super(s);
	}

	/**
	 * Set up a test model, which contains predefined templates
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wikiModel = newWikiTestModel();
	}

	protected final WikiModel newWikiTestModel() {
		return newWikiTestModel(Locale.ENGLISH);
	}

	protected WikiModel newWikiTestModel(Locale locale) {
		WikiTestModel wikiModel = new WikiTestModel(locale,
				"http://www.bliki.info/wiki/${image}",
				"http://www.bliki.info/wiki/${title}");
		wikiModel.setUp();
		return wikiModel;
	}

	public void testStub() {
	}

	/**
	 * simple example
	 */
	public static void main(String[] args) {
		WikiModel wikiModel = new WikiModel(
				Configuration.DEFAULT_CONFIGURATION, Locale.GERMAN,
				"http://www.bliki.info/wiki/${image}",
				"http://www.bliki.info/wiki/${title}");
		try {
			wikiModel.setUp();

			String htmlStr = wikiModel.render(
					"This is a simple [[Hello World]] wiki tag", false);
			System.out.print(htmlStr);
		} finally {
			wikiModel.tearDown();
		}
	}
}

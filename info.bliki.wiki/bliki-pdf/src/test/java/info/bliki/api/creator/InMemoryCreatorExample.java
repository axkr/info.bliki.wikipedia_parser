package info.bliki.api.creator;

import info.bliki.api.User;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.filter.HTMLConverter;
import info.bliki.wiki.impl.APIWikiModelInMemory;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Test to load a page with image links and templates from en.wikipedia.org and
 * render it to the string / console output. This is happening completely in
 * memory without downloading files to the hard-disk and without creating a
 * local database.
 * 
 * @see HTMLCreatorExample
 */
public class InMemoryCreatorExample {
	public static void main(String[] args) throws Exception {
		testCreator001();
	}

	private static void testCreator001() throws Exception {
		testWikipediaENAPI("Tom Hanks");
	}

	private static void testWikipediaENAPI(String title) throws Exception {
		String articleAddress = "http://en.wikipedia.org/w/api.php?action=query&titles=" + Encoder.encodeTitleToUrl(title, true)
				+ "&prop=revisions&rvprop=content&format=xml";
		String articleWikiContent = loadRAWContent(articleAddress);

		User wikiUser = new User("", "", "http://en.wikipedia.org/w/api.php");
		wikiUser.login();

		// set up a simple cache for this example. HashMap is not usable for
		// production! Use something like http://ehcache.org for production!
		Map<String, String> contentCache = new HashMap<String, String>(2000);
		APIWikiModelInMemory wikiModel = new APIWikiModelInMemory(wikiUser, Locale.ENGLISH, "${image}", "${title}", contentCache);
		DocumentCreator documentCreator = new DocumentCreator(wikiModel, wikiUser, new String[0]);
		wikiModel.setUp();

		StringWriter writer = new StringWriter();
		documentCreator.render(articleWikiContent, articleAddress, new HTMLConverter(), writer);

		String wikiHTMLText = writer.toString();
		System.out.println(wikiHTMLText);
	}

	private static String loadRAWContent(String articleAddress) throws Exception {
		URL articleURL = new URL(articleAddress);

		BufferedReader reader = new BufferedReader(new InputStreamReader(articleURL.openStream()));
		StringBuilder contentStringBuilder = new StringBuilder(10000);
		String line;
		while ((line = reader.readLine()) != null) {
			contentStringBuilder.append(line);
		}
		return contentStringBuilder.toString();
	}
}
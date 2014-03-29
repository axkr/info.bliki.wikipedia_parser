package info.bliki.wiki.dump;

import info.bliki.wiki.model.WikiModel;

import org.xml.sax.SAXException;

/**
 * Example filter which prints the rendered HTML articles to System.out
 * 
 * The number of processed articles is limited by a maximum counter
 * 
 */
public class PrintArticle implements IArticleFilter {

	private int counter;

	private int max_counter;

	/**
	 * Example filter which prints the rendered HTML articles to System.out
	 * 
	 * The number of processed articles is limited by a maximum counter
	 * <code>max_count</code>
	 * 
	 */
	public PrintArticle(int max_count) {
		counter = 0;
		max_counter = max_count;
	}

	@Override
	public void process(WikiArticle article, Siteinfo siteinfo) throws SAXException {
		counter++;
		if (counter >= max_counter) {
			throw new SAXException("\nLimit reached after " + max_counter + " entries.");
		}
		String htmlText = "";
		WikiModel wikiModel = new WikiModel("${image}", "${title}");
		try {
			wikiModel.setUp();
			htmlText = wikiModel.render(article.getText(), false);
			if (htmlText == null) {
				System.out.println("An IOException occured!");
			} else {
				System.out.println(htmlText);
			}
		} finally {
			wikiModel.tearDown();
		}
	}

	public static void main(String args[]) throws Exception {
		// OutputStream fout = new FileOutputStream("data.txt");
		// OutputStream bout = new BufferedOutputStream(fout);
		// Writer out = new OutputStreamWriter(bout, Connector.UTF8_CHARSET);

		// test for Wikipedia input
		IArticleFilter filter = new PrintArticle(999999);
		WikiXMLParser parser = new WikiXMLParser("C:/temp/v1.xml", filter);
		parser.parse();
	}
}

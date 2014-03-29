package info.bliki.api.creator;

import info.bliki.wiki.dump.IArticleFilter;
import info.bliki.wiki.dump.Siteinfo;
import info.bliki.wiki.dump.WikiArticle;
import info.bliki.wiki.dump.WikiXMLParser;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.impl.DumpWikiModel;

import java.io.IOException;

import org.xml.sax.SAXException;

/**
 * Create static HTML files from a given Mediawiki dump
 */
public class Dump2HTMLCreatorExample {
	public Dump2HTMLCreatorExample() {
		super();
	}

	static class DemoArticleFilter implements IArticleFilter {
		WikiDB wikiDB;
		int counter;
		private final String htmlDirectory;
		private final String imageDirectory;

		public DemoArticleFilter(WikiDB db, String htmlDirectory, String imageDirectory) {
			this.counter = 0;
			this.wikiDB = db;
			if (htmlDirectory.charAt(htmlDirectory.length() - 1) != '/') {
				htmlDirectory = htmlDirectory + "/";
			}
			this.htmlDirectory = htmlDirectory;
			this.imageDirectory = imageDirectory;
		}

		public void process(WikiArticle page, Siteinfo siteinfo) throws SAXException {
			if (page.isMain() || page.isCategory() || page.isProject()) {
				String title = page.getTitle();
				String titleURL = Encoder.encodeTitleLocalUrl(title);
				String generatedHTMLFilename = htmlDirectory + titleURL + ".html";
				DumpWikiModel wikiModel = new DumpWikiModel(wikiDB, siteinfo, "${image}", "${title}", imageDirectory);
				DumpDocumentCreator creator = new DumpDocumentCreator(wikiModel, page);
				creator.setHeader(HTMLConstants.HTML_HEADER1 + HTMLConstants.CSS_SCREEN_STYLE + HTMLConstants.HTML_HEADER2);
				creator.setFooter(HTMLConstants.HTML_FOOTER);
				wikiModel.setUp();
				try {
					creator.renderToFile(generatedHTMLFilename);
					System.out.print('.');
					if (++counter >= 80) {
						System.out.println(' ');
						counter = 0;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	static class DemoTemplateArticleFilter implements IArticleFilter {
		WikiDB wikiDB;
		int counter;

		public DemoTemplateArticleFilter(WikiDB wikiDB) {
			this.wikiDB = wikiDB;
			this.counter = 0;
		}

		public void process(WikiArticle page, Siteinfo siteinfo) throws SAXException {
			if (page.isTemplate()) {
				// System.out.println(page.getTitle());
				TopicData topicData = new TopicData(page.getTitle(), page.getText());
				try {
					wikiDB.insertTopic(topicData);
					System.out.print('.');
					if (++counter >= 80) {
						System.out.println(' ');
						counter = 0;
					}
				} catch (Exception e) {
					String mess = e.getMessage();
					if (mess == null) {
						throw new SAXException(e.getClass().getName());
					}
					throw new SAXException(mess);
				}
			}
		}
	}

	public static WikiDB prepareDB(String mainDirectory) {
		// the following subdirectory should not exist if you would like to create a
		// new database
		if (mainDirectory.charAt(mainDirectory.length() - 1) != '/') {
			mainDirectory = mainDirectory + "/";
		}
		String databaseSubdirectory = "WikiDumpDB";

		WikiDB db = null;

		try {
			db = new WikiDB(mainDirectory, databaseSubdirectory);
			return db;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			// if (db != null) {
			// try {
			// db.tearDown();
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			// }
		}
		return null;
	}

	public static void main(String[] args) {
		boolean skipFirstPass = false;
		if (args.length < 1) {
			System.err.println("Usage: Dump2HTMLCreatorExample <XML-FILE> [<SKIP-FIRST_PASS>=true|yes]");
			System.exit(-1);
		}
		if (args.length > 1) {
			String arg1 = args[1].toLowerCase();
			if (arg1.equals("true") || arg1.equals("yes")) {
				skipFirstPass = true;
				System.out.println("Option <skip first pass> is set to true");
			}
		}
		// String bz2Filename =
		// "c:\\temp\\dewikiversity-20100401-pages-articles.xml.bz2";
		String bz2Filename = args[0];
		WikiDB db = null;

		try {
			String mainDirectory = "c:/temp";
			String htmlDirectory = "c:/temp/dump/";

			// the following directory must exist for image references
			String imageDirectory = "c:/temp/dump/WikiDumpImages";
			System.out.println("Prepare wiki database");
			db = prepareDB(mainDirectory);
			IArticleFilter handler;
			WikiXMLParser wxp;
			if (!skipFirstPass) {
				System.out.println("First pass - write templates to database:");
				handler = new DemoTemplateArticleFilter(db);
				wxp = new WikiXMLParser(bz2Filename, handler);
				wxp.parse();
				System.out.println(' ');
			}
			System.out.println("Second pass - write HTML files to directory:");
			handler = new DemoArticleFilter(db, htmlDirectory, imageDirectory);
			wxp = new WikiXMLParser(bz2Filename, handler);
			wxp.parse();
			System.out.println(' ');
			System.out.println("Done!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (db != null) {
				try {
					db.tearDown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

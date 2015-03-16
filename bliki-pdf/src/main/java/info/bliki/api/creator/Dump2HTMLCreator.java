package info.bliki.api.creator;

import info.bliki.wiki.dump.IArticleFilter;
import info.bliki.wiki.dump.Siteinfo;
import info.bliki.wiki.dump.WikiArticle;
import info.bliki.wiki.dump.WikiXMLParser;
import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.impl.DumpWikiModel;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static info.bliki.api.creator.Dump2HTMLCreator.DumpMode.BOTH;
import static info.bliki.api.creator.Dump2HTMLCreator.DumpMode.WRITE_TEMPLATES_AND_MODULES;
import static info.bliki.api.creator.Dump2HTMLCreator.DumpMode.WRITE_HTML;

/**
 * Create static HTML files from a given Mediawiki dump
 */
public class Dump2HTMLCreator {
    private static final String WIKI_DB = "WikiDB";
    private static final String WIKI_DUMP_IMAGES = "WikiDumpImages";
    private static final String HTML_DIR = "html";

    private final File dumpFile;

    public enum DumpMode {
        WRITE_TEMPLATES_AND_MODULES,
        WRITE_HTML,
        BOTH
    }

    public Dump2HTMLCreator(File dumpFile) {
        this.dumpFile = dumpFile;
    }

    public static void main(String[] args) throws Exception{
        DumpMode mode = DumpMode.BOTH;

        if (args.length < 2) {
            System.err.println("Usage: "+Dump2HTMLCreator.class.getSimpleName()+" <dump.xml> <dump-dir> [WRITE_TEMPLATES_AND_MODULES|WRITE_HTML|BOTH]");
            System.exit(-1);
        } else {
            final File dumpFile = new File(args[0]);
            final File baseDir = new File(args[1]);

            if (args.length > 2) {
                mode = DumpMode.valueOf(args[2].toUpperCase());
            }

            System.err.println("importing into "+baseDir);
            new Dump2HTMLCreator(dumpFile).dump(mode, baseDir);
            System.out.println("done!");
        }
    }

    protected void dump(DumpMode mode, File baseDir) throws SAXException, IOException, SQLException {
        final File htmlDirectory = new File(baseDir, HTML_DIR);
        final File dbDirectory = new File(baseDir, WIKI_DB);
        // the following directory must exist for image references
        final File imageDirectory = new File(htmlDirectory, WIKI_DUMP_IMAGES);

        dump(mode, dbDirectory, imageDirectory, htmlDirectory);
    }


    protected void dump(DumpMode mode,
                        File dbDirectory,
                        File imageDirectory,
                        File htmlDirectory)
            throws IOException, SAXException, SQLException {

        try (WikiDB db = new WikiDB(dbDirectory)) {
            if (mode == BOTH || mode == WRITE_TEMPLATES_AND_MODULES) {
                firstPass(db);
            }

            if (mode == BOTH || mode == WRITE_HTML) {
                secondPass(db, htmlDirectory, imageDirectory);
            }
        }
    }
    private void firstPass(WikiDB db) throws IOException, SAXException {
        System.out.println("First pass - write templates to database "+db);
        new WikiXMLParser(dumpFile, new InsertTemplateAndModuleFilter(db)).parse();
        System.out.println(' ');
    }

    private void secondPass(WikiDB db, File htmlDirectory, File imageDirectory) throws IOException, SAXException {
        System.out.println("Second pass - write HTML files to directory "+htmlDirectory);
        new WikiXMLParser(dumpFile, new RenderArticleFilter(db, htmlDirectory, imageDirectory)).parse();
        System.out.println(' ');
    }

    private static class InsertTemplateAndModuleFilter implements IArticleFilter {
        private WikiDB wikiDB;
        private int counter;

        public InsertTemplateAndModuleFilter(WikiDB wikiDB) {
            this.wikiDB = wikiDB;
        }

        public void process(WikiArticle page, Siteinfo siteinfo) throws SAXException {
            if (page.isTemplate() || page.isModule()) {
                TopicData topicData = new TopicData(page.getTitle(), page.getText());
                try {
                    wikiDB.insertTopic(topicData);
                    System.out.print('.');
                    if (++counter % 80 == 0) {
                        System.out.println(' ');
                    }
                } catch (Exception e) {
                    throw new SAXException(e);
                }
            }
        }
    }

    private static class RenderArticleFilter implements IArticleFilter {
        private WikiDB wikiDB;
        private int counter;
        private final File htmlDirectory;
        private final File imageDirectory;

        public RenderArticleFilter(WikiDB db, File htmlDirectory, File imageDirectory) {
            this.wikiDB = db;
            this.htmlDirectory = htmlDirectory;
            this.imageDirectory = imageDirectory;
        }

        public void process(WikiArticle page, Siteinfo siteinfo) throws SAXException {
            if (page.isMain() || page.isCategory() || page.isProject()) {
                String title = page.getTitle();
                String titleURL = Encoder.encodeTitleLocalUrl(title);
                File generatedHTMLFilename = new File(htmlDirectory, titleURL + ".html");
                DumpWikiModel wikiModel = new DumpWikiModel(wikiDB, siteinfo, "${image}", "${title}", imageDirectory);
                DumpDocumentCreator creator = new DumpDocumentCreator(wikiModel, page);
                creator.setHeader(HTMLConstants.HTML_HEADER1 + HTMLConstants.CSS_SCREEN_STYLE + HTMLConstants.HTML_HEADER2);
                creator.setFooter(HTMLConstants.HTML_FOOTER);
                wikiModel.setUp();
                try {
                    creator.renderToFile(generatedHTMLFilename);
                    System.out.print('.');
                    if (++counter % 80 == 0) {
                        System.out.println(' ');
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

package info.bliki.wiki.dump;

import info.bliki.wiki.namespaces.Namespace;
import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WikiXMLParserTest {
    @Test
    public void testParseWikipediaDump() throws Exception {
        parse(getClass().getResource("/dump/enwiki-20150112-pages-articles1.xml"));
    }

    @Test
    public void testParseWikipediaDumpBZ2Compressed() throws Exception {
        parse(getClass().getResource("/dump/enwiki-20150112-pages-articles1.xml.bz2"));
    }

    private void parse(URL dump) throws IOException, SAXException {
        assertThat(dump).isNotNull();

        final List<WikiArticle> articles = new ArrayList<>();
        final Siteinfo[] parsedSiteInfo = new Siteinfo[1];
        WikiXMLParser parser = new WikiXMLParser(new File(dump.getFile()), new IArticleFilter() {
            @Override
            public void process(WikiArticle article, Siteinfo siteinfo) throws SAXException {
                articles.add(article);
                parsedSiteInfo[0] = siteinfo;
            }
        });
        parser.parse();

        assertThat(articles).hasSize(3);
        assertThat(parsedSiteInfo[0]).isNotNull();
        Siteinfo siteinfo = parsedSiteInfo[0];

        assertThat(siteinfo.getSitename()).isEqualTo("Wikipedia");
        assertThat(siteinfo.getGenerator()).isEqualTo("MediaWiki 1.25wmf13");
        assertThat(siteinfo.getBase()).isEqualTo("http://en.wikipedia.org/wiki/Main_Page");
        final Namespace.NamespaceValue main = siteinfo.getNamespace().getNamespaceByNumber(0);
        assertThat(main.getCanonicalName()).isEqualTo("");
        final Namespace.NamespaceValue topic = siteinfo.getNamespace().getNamespaceByNumber(2600);
        assertThat(topic.getCanonicalName()).isEqualTo("Topic");
    }
}

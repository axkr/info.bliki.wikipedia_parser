package info.bliki.api;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Reads <code>SearchResult</code> data from an XML file generated by the <a
 * href="https://meta.wikimedia.org/w/api.php">Wikimedia API</a> through the
 * <code>search</code> query.
 */
public class XMLSearchParser extends AbstractXMLParser {
    private static final String SEARCH_TAG = "search";
    private static final String SIZE_ID = "size";
    private static final String SNIPPET_ID = "snippet";
    private static final String SROFFSET_ID = "sroffset";
    private static final String TIMESTAMP_ID = "timestamp";
    private static final String WORDCOUNT_ID = "wordcount";

    private SearchResult fSearchResult;

    private List<SearchResult> searchResultsList;

    private String srOffset;

    public XMLSearchParser(String xmlText) throws SAXException {
        super(xmlText);
        this.searchResultsList = new ArrayList<>();
        this.srOffset = "";
    }

    @Override
    public void endElement(String uri, String name, String qName) {
        try {
            if (PAGE_TAG2.equals(qName)) {
                if (fSearchResult != null) {
                    searchResultsList.add(fSearchResult);
                }
            }

            fData = null;
            fAttributes = null;

        } catch (RuntimeException re) {
            re.printStackTrace();
        }
    }

    public List<SearchResult> getSearchResultList() {
        return searchResultsList;
    }

    /**
     * @return the srOffset
     */
    public String getSrOffset() {
        if (srOffset == null) {
            return "";
        }
        return srOffset;
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        fAttributes = atts;

        if (PAGE_TAG2.equals(qName)) {
            fSearchResult = new SearchResult();
            fSearchResult.setNs(fAttributes.getValue(NS_ID));
            fSearchResult.setTitle(fAttributes.getValue(TITLE_ID));
            fSearchResult.setSize(fAttributes.getValue(SIZE_ID));
            fSearchResult.setSnippet(fAttributes.getValue(SNIPPET_ID));
            fSearchResult.setTimestamp(fAttributes.getValue(TIMESTAMP_ID));
            fSearchResult.setWordCount(fAttributes.getValue(WORDCOUNT_ID));
        } else if (SEARCH_TAG.equals(qName)) {
            String value = fAttributes.getValue(SROFFSET_ID);
            if (value != null) {
                srOffset = value;
            }
        }
        fData = null;
    }

}

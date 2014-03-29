package info.bliki.wiki.events;

import info.bliki.wiki.filter.AbstractParser.ParsedPageName;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace.NamespaceCode;

import java.util.Map;

/**
 * Wiki model implementation which allows some special JUnit tests with
 * predefined templates
 * 
 * Uses a modified template from: <a
 * href="http://en.wikipedia.org/wiki/Template:Reflist"
 * >http://en.wikipedia.org/wiki/Template:Reflist</a>
 */
public class HelloWikiModel extends WikiModel {
	public final static String REFLIST_TEXT = "<div class=\"references-small\" {{#if: {{{colwidth|}}}| style=\"-moz-column-width:{{{colwidth}}}; -webkit-column-width:{{{colwidth}}}; column-width:{{{colwidth}}};\" | {{#if: {{{1|}}}| style=\"-moz-column-count:{{{1}}}; -webkit-column-count:{{{1}}}; column-count:{{{1}}} }};\" |}}>\n"
			+ "<references /></div><noinclude>{{pp-template|small=yes}}{{template doc}}</noinclude>\n";
	public final static String CITE_WEB_TEXT = "[{{{url}}} {{{title}}}]";

	public final static String CITE_BOOK_TEXT = "{{{last}}}, {{{first}}} [{{{url}}} {{{title}}}]";

	public HelloWikiModel(String imageBaseURL, String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
	}

	/**
	 * Add template: &quot;Reflist&quot;
	 * 
	 */
	@Override
	public String getRawWikiContent(ParsedPageName parsedPagename, Map<String, String> templateParameters) throws WikiModelContentException {
		String result = super.getRawWikiContent(parsedPagename, templateParameters);
		if (result != null) {
			return result;
		}
		if (parsedPagename.namespace.isType(NamespaceCode.TEMPLATE_NAMESPACE_KEY)) {
			String name = encodeTitleToUrl(parsedPagename.pagename, true);
			// important: the Template name starts with an uppercase character!
			if (name.equals("Reflist")) {
				return REFLIST_TEXT;
			} else if (name.equals("Cite_web")) {
				return CITE_WEB_TEXT;
			} else if (name.equals("Cite_book")) {
				return CITE_BOOK_TEXT;
			}
		}
		return null;
	}
}

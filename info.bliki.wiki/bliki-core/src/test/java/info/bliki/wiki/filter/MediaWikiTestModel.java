package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.Utils;
import info.bliki.wiki.filter.AbstractParser.ParsedPageName;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace.INamespaceValue;
import info.bliki.wiki.tags.IgnoreTag;
import info.bliki.wiki.tags.extension.ChartTag;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Wiki model implementation which allows some special JUnit tests with
 * namespaces and predefined templates
 * 
 */
public class MediaWikiTestModel extends WikiModel {
	final protected Map<String, String> db;

	boolean fSemanticWebActive;

	static {
		TagNode.addAllowedAttribute("style");
		Configuration.DEFAULT_CONFIGURATION.addUriScheme("tel");
		Configuration.DEFAULT_CONFIGURATION.addInterwikiLink("intra", "/${title}");
		Configuration.DEFAULT_CONFIGURATION.addTokenTag("chart", new ChartTag());
		Configuration.DEFAULT_CONFIGURATION.addTokenTag("inputbox", new IgnoreTag("inputbox"));
		Configuration.DEFAULT_CONFIGURATION.addTokenTag("imagemap", new IgnoreTag("imagemap"));
	}

	public MediaWikiTestModel(String imageBaseURL, String linkBaseURL, Map<String, String> db) {
		this(Locale.ENGLISH, imageBaseURL, linkBaseURL, db);
	}

	/**
	 * Add German namespaces to the wiki model
	 * 
	 * @param imageBaseURL
	 * @param linkBaseURL
	 */
	public MediaWikiTestModel(Locale locale, String imageBaseURL, String linkBaseURL, Map<String, String> db) {
		super(Configuration.DEFAULT_CONFIGURATION, locale, imageBaseURL, linkBaseURL);
		this.db = db;
		// add some basic pages assumed to always exist (at least in
		// parserTests.txt):
		db.put("Main_Page", "");
		db.put("Special:Version", "");

		// set up a simple cache mock-up for JUnit tests. HashMap is not usable for
		// production!
		Configuration.DEFAULT_CONFIGURATION.setTemplateCallsCache(new HashMap());

		fSemanticWebActive = false;
	}

	/**
	 * Add templates: &quot;Test&quot;, &quot;Templ1&quot;, &quot;Templ2&quot;,
	 * &quot;Include Page&quot;
	 * 
	 */
	@Override
	public String getRawWikiContent(ParsedPageName parsedPagename, Map<String, String> templateParameters) throws WikiModelContentException {
		String result = super.getRawWikiContent(parsedPagename, templateParameters);
		if (result != null) {
			// found magic word template
			return result;
		}
		String articleName = parsedPagename.pagename;
		INamespaceValue namespace = parsedPagename.namespace;
		String name = encodeTitleToUrl(articleName, true);
		if (namespace == null) {
			return db.get(name);
		} else {
			return db.get(namespace + ":" + name);
		}
	}

	@Override
	public boolean isSemanticWebActive() {
		return fSemanticWebActive;
	}

	@Override
	public void setSemanticWebActive(boolean semanticWeb) {
		this.fSemanticWebActive = semanticWeb;
	}

	public boolean showSyntax(String tagName) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.bliki.wiki.model.WikiModel#appendInternalLink(java.lang.String,
	 * java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public void appendInternalLink(String topic, String hashSection, String topicDescription, String cssClass, boolean parseRecursive) {
		String encodedtopic = encodeTitleToUrl(topic, true);
		appendInternalLink(topic, hashSection, topicDescription, cssClass, parseRecursive, db.get(encodedtopic) != null);
	}

	@Override
	public void appendExternalLink(String uriSchemeName, String link, String linkName, boolean withoutSquareBrackets) {
		if (uriSchemeName.equalsIgnoreCase("tel")) {
			// example for a telephone link
			link = Utils.escapeXml(link, true, false, false);
			TagNode aTagNode = new TagNode("a");
			aTagNode.addAttribute("href", link, true);
			aTagNode.addAttribute("class", "telephonelink", true);
			aTagNode.addAttribute("title", link, true);
			if (withoutSquareBrackets) {
				append(aTagNode);
				aTagNode.addChild(new ContentToken(linkName));
			} else {
				String trimmedText = linkName.trim();
				if (trimmedText.length() > 0) {
					pushNode(aTagNode);
					WikipediaParser.parseRecursive(trimmedText, this, false, true);
					popNode();
				}
			}
			return;
		}
		super.appendExternalLink(uriSchemeName, link, linkName, withoutSquareBrackets);
	}

	/**
	 * Test for <a
	 * href="http://groups.google.de/group/bliki/t/a0540e27f27f02a5">Discussion:
	 * Hide Table of Contents (toc)?</a>
	 */
	// public ITableOfContent createTableOfContent(boolean isTOCIdentifier) {
	// if (fToCSet == null) {
	// fToCSet = new HashSet<String>();
	// fTableOfContent = new ArrayList<Object>();
	// }
	// fTableOfContentTag = new TableOfContentTag("div") {
	// public void setShowToC(boolean showToC) {
	// // do nothing
	// }
	// };
	// return fTableOfContentTag;
	// }
}
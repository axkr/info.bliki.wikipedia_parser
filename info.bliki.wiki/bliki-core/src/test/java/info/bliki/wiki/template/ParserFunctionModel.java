package info.bliki.wiki.template;

import info.bliki.wiki.filter.AbstractParser.ParsedPageName;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.WikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.template.extension.AttributeRenderer;
import info.bliki.wiki.template.extension.DollarContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Wiki model implementation which allows some special JUnit tests for template
 * parser functions
 * 
 */
public class ParserFunctionModel extends WikiModel {
	final static String CONCAT = "{{{1|}}}{{{2|}}}{{{3|}}}{{{4|}}}{{{5|}}}{{{6|}}}{{{7|}}}{{{8|}}}{{{9|}}}{{{10|}}}";

	// final static String CONVERT=
	// "<includeonly>{{#ifeq:{{{sortable|}}}|on|<span style=\"display:none\">{{padleft:{{{1}}}|16|0}}</span>}}{{convert/{{{2}}}|{{{1}}}|{{#ifeq:{{#expr:{{{3|0}}}*0}}|0|0}}|{{{3|}}}|{{{4|}}}|{{{5|}}}|{{{6|}}}|r={{#ifeq:{{{sp}}}|us|er|re}}|d=L{{{lk|off}}}A{{{abbr|off}}}D{{{disp|b}}}S{{{adj|{{{sing|off}}}}}}|s={{{sigfig|}}}}}</includeonly><noinclude>{{Documentation}}</noinclude>"
	// ;
	public class DateRenderer implements AttributeRenderer {
		@Override
		public String toString(Object o) {
			SimpleDateFormat f = new SimpleDateFormat("yyyy.MM.dd");
			return f.format(((Calendar) o).getTime());
		}

		@Override
		public String toString(Object o, String formatString) {
			return toString(o);
		}
	}

	public class UppercaseRenderer implements AttributeRenderer {
		@Override
		public String toString(Object o) {
			return (String) o;
		}

		@Override
		public String toString(Object o, String formatString) {
			if (formatString.equals("upper")) {
				return ((String) o).toUpperCase();
			}
			return toString(o);
		}
	}

	/**
	 * Add German namespaces to the wiki model
	 * 
	 * @param imageBaseURL
	 * @param linkBaseURL
	 */
	public ParserFunctionModel(String imageBaseURL, String linkBaseURL) {
		super(imageBaseURL, linkBaseURL);
		Configuration.DEFAULT_CONFIGURATION.addTemplateFunction("#$", DollarContext.CONST);
		// set up a simple cache mock-up for JUnit tests. HashMap is not usable for
		// production!
		Configuration.DEFAULT_CONFIGURATION.setTemplateCallsCache(new HashMap<String, String>());
	}

	/**
	 * Add templates: &quot;Test&quot;, &quot;Templ1&quot;, &quot;Templ2&quot;,
	 * &quot;Include Page&quot;
	 * 
	 */
	@Override
	public String getRawWikiContent(ParsedPageName parsedPagename, Map<String, String> map) throws WikiModelContentException {
		String result = super.getRawWikiContent(parsedPagename, map);
		if (result != null) {
			return result;
		}
		String name = encodeTitleToUrl(parsedPagename.pagename, true);
		if (name.equals("Concat")) {
			return CONCAT;
		}
		return null;
	}

}

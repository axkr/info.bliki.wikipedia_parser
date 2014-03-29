package info.bliki.wiki.template;

import info.bliki.api.Connector;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * A template parser function for <code>{{urlencode: ... }}</code> syntax
 * 
 */
public class URLEncode extends AbstractTemplateFunction {
	public final static ITemplateFunction CONST = new URLEncode();

	public URLEncode() {

	}

	@Override
	public String parseFunction(List<String> list, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) throws IOException {
		if (list.size() > 0) {
			String result = isSubst ? list.get(0) : parseTrim(list.get(0), model);
			return URLEncoder.encode(result, Connector.UTF8_CHARSET);
		}
		return null;
	}
}

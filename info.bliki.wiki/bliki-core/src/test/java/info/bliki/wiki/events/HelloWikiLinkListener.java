package info.bliki.wiki.events;

import info.bliki.wiki.model.DefaultEventListener;

/**
 * A test wiki event listener implementation which will trigger the
 * <code>on....</code> event methods during the parsing process.
 * 
 * 
 */
public class HelloWikiLinkListener extends DefaultEventListener {
	StringBuffer collectorBuffer = new StringBuffer();

	public HelloWikiLinkListener() {

	}

	@Override
	public void onWikiLink(char[] src, int rawStart, int rawEnd, String suffix) {
		collectorBuffer.append(src, rawStart, rawEnd - rawStart);
		collectorBuffer.append("\n");
	}

	public StringBuffer getCollectorBuffer() {
		return collectorBuffer;
	}

}

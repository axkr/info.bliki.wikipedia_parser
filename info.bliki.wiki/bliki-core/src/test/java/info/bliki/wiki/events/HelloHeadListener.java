package info.bliki.wiki.events;

import info.bliki.wiki.model.DefaultEventListener;

/**
 * A test wiki event listener implementation which will trigger the
 * <code>on....</code> event methods during the parsing process.
 * 
 * 
 */
public class HelloHeadListener extends DefaultEventListener {
	StringBuffer collectorBuffer = new StringBuffer();

	public HelloHeadListener() {

	}

	/** {@inheritDoc} */
	@Override
	public void onHeader(char[] src, int startPosition, int endPosition, int rawStart, int rawEnd, int level) {
		collectorBuffer.append(src, rawStart, rawEnd - rawStart);
		collectorBuffer.append("\n");
	}

	/** {@inheritDoc} */
	@Override
	public void onWikiLink(char[] src, int rawStart, int rawEnd, String suffix) {
	}

	public StringBuffer getCollectorBuffer() {
		return collectorBuffer;
	}

}

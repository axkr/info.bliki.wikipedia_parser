package info.bliki.wiki.events;

import info.bliki.wiki.model.DefaultEventListener;

public class EventListener extends DefaultEventListener {
	StringBuffer collectorBuffer = new StringBuffer();

	public EventListener() {

	}

	/** {@inheritDoc} */
	@Override
	public void onHeader(char[] src, int startPosition, int endPosition, int rawStart, int rawEnd, int level) {
		collectorBuffer.append(src, startPosition, endPosition - startPosition);
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

package info.bliki.wiki;

import info.bliki.wiki.filter.PlainTextConverter;

public class MarkdownConverter extends PlainTextConverter {

    @Override
    public boolean renderLinks() {
        return true;
    }
}

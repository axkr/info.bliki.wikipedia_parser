package info.bliki.wiki.filter;

public class MarkdownConverter extends PlainTextConverter {

    @Override
    public boolean renderLinks() {
        return true;
    }
}

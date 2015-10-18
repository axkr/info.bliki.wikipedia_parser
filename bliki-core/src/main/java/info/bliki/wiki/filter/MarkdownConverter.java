package info.bliki.wiki.filter;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.tags.WPATag;

import java.io.IOException;

public class MarkdownConverter extends PlainTextConverter {
    @Override public boolean renderLinks() {
        return true;
    }

    public void renderLink(WPATag tag, Appendable buf, IWikiModel wikiModel) throws IOException {
        StringBuilder linkBuffer = new StringBuilder();
        tag.renderPlainText(new PlainTextConverter(), linkBuffer, wikiModel);

        String linkTitle = linkBuffer.toString();
        if (linkTitle.contains("#")) {
            linkTitle = linkTitle.substring(0, linkTitle.indexOf("#"));
        }

        buf.append("[").append(linkTitle).append("]");
        buf.append("(").append(getWikiLink(tag, linkTitle)).append(")");
    }

    protected String getWikiLink(WPATag tag, String linkContent) {
       String link = tag.getLink();

        if (link == null) {
            return null;
        } else {
            if (link.trim().isEmpty()) {
                link = linkContent;
            }

            if (tag.getAnchor() != null) {
                return quote(link) + "#" + quote(tag.getAnchor());
            } else {
                return quote(link);
            }
        }
    }

    protected String quote(String link) {
        return link.replace(" ", "_")
                .replace("*", "\\*")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("+", "\\+")
                .replace("-", "\\-")
                .replace(".", "\\.")
                .replace("!", "\\!");
    }
}

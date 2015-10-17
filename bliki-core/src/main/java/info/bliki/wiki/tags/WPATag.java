package info.bliki.wiki.tags;

import info.bliki.wiki.MarkdownConverter;
import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;


public class WPATag extends HTMLTag {
    public static final String HREF = "href";
    public static final String ANCHOR = "anchor";
    public static final String WIKILINK = "wikilink";
    public static final String CLASS = "class";
    public static final String TITLE = "title";

    public WPATag() {
        super("a");
    }

    @Override
    public boolean isReduceTokenStack() {
        return false;
    }


    @Override
    public void renderPlainText(ITextConverter converter, Appendable buf, IWikiModel wikiModel) throws IOException {
        if (converter.renderLinks() &&
            getObjectAttributes().containsKey(WIKILINK) &&
            converter instanceof MarkdownConverter) {

            String link;
            StringBuilder linkBuffer = new StringBuilder();
            super.renderPlainText(converter, linkBuffer, wikiModel);

            link = linkBuffer.toString();
            if (link.contains("#")) {
                link = link.substring(0, link.indexOf("#"));
            }
            buf.append("[").append(link).append("]");
            final String wikiLink = getWikiLink();
            if (wikiLink.equals(link)) {
                buf.append("[]");
            } else {
                buf.append("(").append(wikiLink).append(")");
            }
        } else {
            super.renderPlainText(converter, buf, wikiModel);
        }
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        if (converter.renderLinks()) {
            super.renderHTML(converter, buf, model);
        } else {
            List<Object> children = getChildren();
            if (children.size() != 0) {
                converter.nodesToText(children, buf, model);
            }
        }
    }

    protected String getWikiLink() {
        Object link = getObjectAttributes().get(WIKILINK);
        if (link == null) {
            return null;
        } else {
            if (getAnchor() != null) {
                return link.toString() + "#" + getAnchor();
            } else {
                return link.toString();
            }
        }
    }

    protected String getTitle() {
        return getAttributes().get(TITLE);
    }

    protected String getAnchor() {
        Object anchor = getObjectAttributes().get(ANCHOR);
        return anchor == null ? null : anchor.toString();
    }
}

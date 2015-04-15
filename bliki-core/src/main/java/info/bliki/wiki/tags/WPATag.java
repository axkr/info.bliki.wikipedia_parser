package info.bliki.wiki.tags;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;


public class WPATag extends HTMLTag {
    public static final String HREF = "href";
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
    public void renderHTML(ITextConverter converter, Appendable buf, IWikiModel model) throws IOException {
        if (!converter.noLinks()) {
            super.renderHTML(converter, buf, model);
        } else {
            List<Object> children = getChildren();
            if (children.size() != 0) {
                converter.nodesToText(children, buf, model);
            }
        }
    }
}

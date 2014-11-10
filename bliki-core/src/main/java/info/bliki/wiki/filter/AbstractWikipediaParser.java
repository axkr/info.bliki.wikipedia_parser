package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.WPBoldItalicTag;
import info.bliki.wiki.tags.WPTag;

public abstract class AbstractWikipediaParser extends AbstractParser {
    protected static final int TokenIgnore = -1;
    protected static final int TokenSTART = 0;
    protected static final int TokenEOF = 1;
    protected static final int TokenBOLD = 3;
    protected static final int TokenITALIC = 4;
    protected static final int TokenBOLDITALIC = 5;

    protected static final HTMLTag BOLD = new WPTag("b");
    protected static final HTMLTag ITALIC = new WPTag("i");
    protected static final HTMLTag BOLDITALIC = new WPBoldItalicTag();

    public AbstractWikipediaParser(String stringSource) {
        super(stringSource);
    }

    protected void reduceTokenStack() {
        while (fWikiModel.stackSize() > 0) {
            fWikiModel.popNode();
        }
    }

    /**
     * Copy the read ahead content in the resulting HTML text token.
     *
     * @param diff
     *          subtract <code>diff</code> form the current parser position to get
     *          the HTML text token end position.
     */
    protected void createContentToken(final int diff) {
        if (fWhiteStart) {
            try {
                final int count = fCurrentPosition - diff - fWhiteStartPosition;
                if (count > 0) {
                    fWikiModel.append(new ContentToken(fStringSource.substring(fWhiteStartPosition, fWhiteStartPosition + count)));
                }
            } finally {
                fWhiteStart = false;
            }
        }
    }


    protected boolean parseHTMLCommentTags() {
        if (fStringSource.startsWith("<!--", fCurrentPosition - 1)) {
            int htmlStartPosition = fCurrentPosition;
            fCurrentPosition += 3;
            readUntil("-->");
            createContentToken(fCurrentPosition - htmlStartPosition + 1);
            return true;
        }
        return false;
    }

    protected void reduceTokenStackBoldItalic() {
        boolean found = false;
        while (fWikiModel.stackSize() > 0) {
            TagToken token = fWikiModel.peekNode();//
            if (token.equals(BOLD) || token.equals(ITALIC) || token.equals(BOLDITALIC)) {
                if (fWhiteStart) {
                    found = true;
                    createContentToken(1);
                }
                fWikiModel.popNode();
            } else {
                return;
            }
        }
        if (found) {
            fWhiteStart = true;
            fWhiteStartPosition = fCurrentPosition;
        }
    }

}

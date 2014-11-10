package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.tags.util.TagStack;

import java.util.Map;

import static info.bliki.wiki.filter.ParsedPageName.parsePageName;

public abstract class AbstractParser extends WikipediaScanner {
    /**
     * The current scanned character
     */
    protected char fCurrentCharacter;

    /**
     * The current offset in the character source array
     */
    protected int fCurrentPosition;
    protected boolean fWhiteStart;
    protected int fWhiteStartPosition;

    public AbstractParser(String stringSource) {
        super(stringSource);
        fCurrentPosition = 0;
        fCurrentCharacter = '\000';
        fWhiteStart = false;
        fWhiteStartPosition = 0;
    }

    /**
     * Read the characters until the given string is found and set the current
     * position of the parser behind the found string.
     *
     * @param untilString
     * @return <code>true</code> if the string was found; <code>false</code>
     *         otherwise
     */
    protected final boolean readUntil(String untilString) {
        int index = fStringSource.indexOf(untilString, fCurrentPosition);
        if (index != (-1)) {
            fCurrentPosition = index + untilString.length();
            return true;
        }
        fCurrentPosition = fStringSource.length();
        return false;
    }

    /**
     * Read the characters until the concatenated <i>start</i> and <i>end</i>
     * substring is found. The end substring is matched ignoring case
     * considerations.
     *
     * @param startString
     *          the start string which should be searched in exact case mode
     * @param endString
     *          the end string which should be searched in ignore case mode
     * @return
     */
    protected final int readUntilIgnoreCase(String startString, String endString) {
        int index = Util.indexOfIgnoreCase(fStringSource, startString, endString, fCurrentPosition);
        if (index != (-1)) {
            fCurrentPosition = index + startString.length() + endString.length();
            return startString.length() + endString.length();
        }
        fCurrentPosition = fStringSource.length();
        return 0;
    }

    /**
     * Read the characters until the <code>&lt;</code> character with following
     * <i>end</i> string is found. The end string is matched ignoring case
     * considerations.
     *
     * @param endString
     *          the end string which should be searched in ignore case mode
     * @return
     */

    protected final int readUntilNestedIgnoreCase(String endString) {
        int index = Util.indexOfNestedIgnoreCase(fStringSource, endString, fCurrentPosition);
        if (index != (-1)) {
            fCurrentPosition = index + 2 + endString.length();
            return 2 + endString.length();
        }
        fCurrentPosition = fStringSource.length();
        return 0;
    }

    /**
     * Read until character is found
     *
     * @param testedChar
     *          search the next position of this char
     * @return <code>true</code> if the tested character can be found
     */
    protected final boolean readUntilChar(char testedChar) {
        int temp = fCurrentPosition;
        try {
            while ((fCurrentCharacter = fSource[fCurrentPosition++]) != testedChar) {
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }

    /**
     * Read until character is found or stop at end-of-line
     *
     * @param testedChar
     *          search the next position of this char
     * @return <code>true</code> if the tested character can be found
     */
    protected final boolean readUntilCharOrStopAtEOL(char testedChar) {
        int temp = fCurrentPosition;
        boolean attrMode = false; // allow newline characters in attributes
        try {
            while ((fCurrentCharacter = fSource[fCurrentPosition++]) != testedChar) {
                if (attrMode) {
                    if (fCurrentCharacter == '"') {
                        attrMode = false;
                    }
                } else {
                    if (fCurrentCharacter == '\n' || fCurrentCharacter == '\r') {
                        return false;
                    }
                    if (fCurrentCharacter == '"') {
                        attrMode = true;
                    }
                }
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }

    /**
     * Read until the end-of-line characters (i.e. '\r' or '\n') or the end of the
     * string is reached
     *
     * @return <code>true</code> if the end-of-line characters or the end of the
     *         string is reached
     *
     */
    protected final boolean readUntilEOL() {
        try {
            while (true) {
                fCurrentCharacter = fSource[fCurrentPosition++];
                if (fCurrentCharacter == '\n' || fCurrentCharacter == '\r') {
                    return true;
                }
                if (fCurrentCharacter == '<') {
                    int newPos = readSpecialWikiTags(fCurrentPosition);
                    if (newPos >= 0) {
                        fCurrentPosition = newPos;
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
            --fCurrentPosition;
            return true;
        }
    }

    protected boolean isEmptyLine(int diff) {
        int temp = fCurrentPosition - diff;
        char ch;
        try {
            while (true) {
                ch = fSource[temp++];
                if (!Character.isWhitespace(ch)) {
                    return false;
                }
                if (ch == '\n') {
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // ..
        }
        return true;
    }

    protected int readWhitespaceUntilEndOfLine(int diff) {
        int temp = fCurrentPosition - diff;
        char ch;
        while (fSource.length > temp) {
            ch = fSource[temp];
            if (!Character.isWhitespace(ch)) {
                return -1;
            }
            if (ch == '\n') {
                fCurrentPosition = temp;
                return temp;
            }
            temp++;
        }
        fCurrentPosition = temp - 1;
        return temp;
    }

    protected int readWhitespaceUntilStartOfLine(int diff) {
        int temp = fCurrentPosition - diff;
        char ch;

        while (temp >= 0) {
            ch = fSource[temp];
            if (!Character.isWhitespace(ch)) {
                return -1;
            }
            if (ch == '\n') {
                return temp;
            }
            temp--;
        }

        return temp;
    }

    protected final boolean getNextChar(char testedChar) {
        int temp = fCurrentPosition;
        try {
            fCurrentCharacter = fSource[fCurrentPosition++];
            if (fCurrentCharacter != testedChar) {
                fCurrentPosition = temp;
                return false;
            }
            return true;

        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }

    protected final int getNextChar(char testedChar1, char testedChar2) {
        int temp = fCurrentPosition;
        try {
            int result;
            fCurrentCharacter = fSource[fCurrentPosition++];
            if (fCurrentCharacter == testedChar1)
                result = 0;
            else if (fCurrentCharacter == testedChar2)
                result = 1;
            else {
                fCurrentPosition = temp;
                return -1;
            }
            return result;
        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return -1;
        }
    }

    protected final boolean getNextCharAsDigit() {
        int temp = fCurrentPosition;
        try {
            fCurrentCharacter = fSource[fCurrentPosition++];
            if (!Character.isDigit(fCurrentCharacter)) {
                fCurrentPosition = temp;
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }

    protected final boolean getNextCharAsWhitespace() {
        int temp = fCurrentPosition;
        try {
            fCurrentCharacter = fSource[fCurrentPosition++];
            if (!Character.isWhitespace(fCurrentCharacter)) {
                fCurrentPosition = temp;
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }

    protected final boolean getNextCharAsDigit(int radix) {

        int temp = fCurrentPosition;
        try {
            fCurrentCharacter = fSource[fCurrentPosition++];

            if (Character.digit(fCurrentCharacter, radix) == -1) {
                fCurrentPosition = temp;
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }

    protected final int getNumberOfChar(char testedChar) {
        int number = 0;
        try {
            while ((fCurrentCharacter = fSource[fCurrentPosition++]) == testedChar) {
                number++;
            }
        } catch (IndexOutOfBoundsException e) {

        }
        fCurrentPosition--;
        return number;
    }

    protected final boolean getNextCharAsWikiPluginIdentifierPart() {
        int temp = fCurrentPosition;
        try {
            fCurrentCharacter = fSource[fCurrentPosition++];

            if (!Encoder.isWikiPluginIdentifierPart(fCurrentCharacter)) {
                fCurrentPosition = temp;
                return false;
            }
            return true;
        } catch (IndexOutOfBoundsException e) {
            fCurrentPosition = temp;
            return false;
        }
    }

    public TagStack parseRecursiveInternal(IWikiModel wikiModel, boolean createOnlyLocalStack, boolean noTOC) {
        // local stack for this wiki snippet
        TagStack localStack = new TagStack();
        // global wiki model stack
        TagStack globalWikiModelStack = wikiModel.swapStack(localStack);
        try {
            // fix for infinite recursion

            int level = wikiModel.incrementRecursionLevel();
            // int parserRecursionLevel = wikiModel.incrementParserRecursionLevel();
            // if (parserRecursionLevel > Configuration.PARSER_RECURSION_LIMIT) {
            // TagNode error = new TagNode("span");
            // error.addAttribute("class", "error", true);
            // error.addChild(new
            // ContentToken("Error - total recursion count limit exceeded parsing wiki tags."));
            // localStack.append(error);
            // return localStack;
            // }

            if (level > Configuration.PARSER_RECURSION_LIMIT) {
                TagNode error = new TagNode("span");
                error.addAttribute("class", "error", true);
                error.addChild(new ContentToken("Error - recursion limit exceeded parsing wiki tags."));
                localStack.append(error);
                return localStack;
            }
            // WikipediaParser parser = new WikipediaParser(rawWikitext,
            // wikiModel.isTemplateTopic(), wikiModel.getWikiListener());
            setModel(wikiModel);
            setNoToC(noTOC);
            runParser();
            return localStack;
        } catch (Exception | Error e) {
            e.printStackTrace();
            TagNode error = new TagNode("span");
            error.addAttribute("class", "error", true);
            error.addChild(new ContentToken(e.getClass().getSimpleName()));
            localStack.append(error);
        } finally {
            wikiModel.decrementRecursionLevel();
            // wikiModel.decrementParserRecursionLevel();
            if (!createOnlyLocalStack) {
                // append the resursively parsed local stack to the global wiki
                // model
                // stack
                globalWikiModelStack.append(localStack);
            }
            wikiModel.swapStack(globalWikiModelStack);
        }

        return localStack;
    }

    /**
     * Read the characters until the end position of the current wiki link is
     * found.
     *
     * @return <code>true</code> if the end of the wiki link was found.
     */
    protected final boolean findWikiLinkEnd() {
        char ch;
        int level = 1;
        int position = fCurrentPosition;
        boolean pipeSymbolFound = false;
        try {
            while (true) {
                ch = fSource[position++];
                if (ch == '|') {
                    pipeSymbolFound = true;
                } else if (ch == '[' && fSource[position] == '[') {
                    if (pipeSymbolFound) {
                        level++;
                        position++;
                    } else {
                        return false;
                    }
                } else if (ch == ']' && fSource[position] == ']') {
                    position++;
                    if (--level == 0) {
                        break;
                    }
                } else if (ch == '{' || ch == '}' || ch == '<' || ch == '>') {
                    if (!pipeSymbolFound) {
                        // see
                        // http://en.wikipedia.org/wiki/Help:Page_name#Special_characters
                        return false;
                    }
                }

                if ((!pipeSymbolFound) && (ch == '\n' || ch == '\r')) {
                    return false;
                }
            }
            fCurrentPosition = position;
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public abstract void setNoToC(boolean noToC);

    public abstract void runParser();



    public static String getRedirectedTemplateContent(IWikiModel wikiModel, String redirectedLink,
            Map<String, String> templateParameters) {
        final INamespace namespace = wikiModel.getNamespace();
        ParsedPageName parsedPagename = parsePageName(wikiModel, redirectedLink, namespace.getMain(), false, false);
        // note: don't just get redirect content if the namespace is the template
        // namespace!
        if (!parsedPagename.valid) {
            return null;
        }
        return getRedirectedRawContent(wikiModel, parsedPagename, templateParameters);
    }

    public static String getRedirectedRawContent(IWikiModel wikiModel, ParsedPageName parsedPagename,
            Map<String, String> templateParameters) {
        try {
            int level = wikiModel.incrementRecursionLevel();
            if (level > Configuration.PARSER_RECURSION_LIMIT || !parsedPagename.valid) {
                return "<span class=\"error\">Error - getting content of redirected link: " + parsedPagename.namespace + ":"
                        + parsedPagename.pagename + "<span>";
            }
            try {
                return wikiModel.getRawWikiContent(parsedPagename, templateParameters);
            } catch (WikiModelContentException e) {
                return "<span class=\"error\">Error - getting content of redirected link: " + parsedPagename.namespace + ":"
                        + parsedPagename.pagename + "<span>";
            }
        } finally {
            wikiModel.decrementRecursionLevel();
        }
    }
}

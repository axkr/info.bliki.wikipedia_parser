package info.bliki.wiki.filter;

import info.bliki.htmlcleaner.ContentToken;
import info.bliki.htmlcleaner.TagNode;
import info.bliki.htmlcleaner.TagToken;
import info.bliki.wiki.model.Configuration;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace;
import info.bliki.wiki.namespaces.INamespace.INamespaceValue;
import info.bliki.wiki.namespaces.INamespace.NamespaceCode;
import info.bliki.wiki.tags.HTMLTag;
import info.bliki.wiki.tags.WPBoldItalicTag;
import info.bliki.wiki.tags.WPTag;
import info.bliki.wiki.tags.util.TagStack;
import info.bliki.wiki.template.AbstractTemplateFunction;

import java.util.Map;

public abstract class AbstractParser extends WikipediaScanner {
	public static final String[] TOC_IDENTIFIERS = { "TOC", "NOTOC", "FORCETOC" };

	final static String HEADER_STRINGS[] = { "=", "==", "===", "====", "=====", "======" };

	final static int TokenNotFound = -2;

	final static int TokenIgnore = -1;

	final static int TokenSTART = 0;

	final static int TokenEOF = 1;

	final static int TokenBOLD = 3;

	final static int TokenITALIC = 4;

	final static int TokenBOLDITALIC = 5;

	final static HTMLTag BOLD = new WPTag("b");

	final static HTMLTag ITALIC = new WPTag("i");

	final static HTMLTag BOLDITALIC = new WPBoldItalicTag();

	final static HTMLTag STRONG = new WPTag("strong");

	final static HTMLTag EM = new WPTag("em");

	/**
	 * The current scanned character
	 */
	protected char fCurrentCharacter;

	/**
	 * The current offset in the character source array
	 */
	protected int fCurrentPosition;

	protected boolean fWhiteStart = false;

	protected int fWhiteStartPosition = 0;

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

	protected boolean parsePHPBBCode(String name, StringBuilder bbCode) {
		int index = 1;
		char ch = ' ';

		while (index < name.length()) {
			ch = name.charAt(index++);
			if ('a' <= ch && ch <= 'z') {
				bbCode.append(ch);
			} else {
				break;
			}
		}
		if (ch != '=' && index != name.length()) {
			// no bbcode
			return false;
		}
		String bbStr = bbCode.toString();
		// String bbEndStr = "[/" + bbStr + "]";
		String bbEndStr = bbStr + "]";
		int startPos = fCurrentPosition;

		readUntilIgnoreCase("[/", bbEndStr);

		String bbAttr = null;
		if (ch == '=') {
			bbAttr = name.substring(index, name.length());
			if (bbAttr != null) {
				bbAttr = bbAttr.trim();
			}
		}

		int endPos = fCurrentPosition - bbEndStr.length() - 2;
		String innerTag = fStringSource.substring(startPos, endPos);

		return createBBCode(bbStr, bbAttr, innerTag);
	}

	private int parsePHPBBCodeRecursive(String rawWikitext, int index) {
		char ch = ' ';
		StringBuilder bbCode = new StringBuilder(10);

		while (index < rawWikitext.length()) {
			ch = rawWikitext.charAt(index++);
			if ('a' <= ch && ch <= 'z') {
				bbCode.append(ch);
			} else {
				break;
			}
		}

		String bbStr = bbCode.toString();
		String bbEndStr = bbStr + "]";
		int startPos = index;

		int endIndex = Util.indexOfIgnoreCase(rawWikitext, "[/", bbEndStr, index);
		if (endIndex != (-1)) {

			String bbAttr = null;
			if (ch == '=') {
				bbAttr = rawWikitext.substring(index, endIndex);
				if (bbAttr != null) {
					bbAttr = bbAttr.trim();
				}
			}

			String innerTag = rawWikitext.substring(startPos, endIndex);

			if (createBBCode(bbStr, bbAttr, innerTag)) {
				return endIndex + 3 + bbStr.length();
			}
		}
		return -1;

	}

	private boolean createBBCode(String bbStr, String bbAttr, String innerTag) {
		if (bbStr.equals("code")) {
			TagNode preTagNode = new TagNode("pre");
			preTagNode.addAttribute("class", "code", true);
			preTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(preTagNode);
			return true;
		} else if (bbStr.equals("color")) {
			if (bbAttr == null) {
				return false;
			}
			TagNode fontTagNode = new TagNode("font");
			fontTagNode.addAttribute("color", bbAttr, true);
			fontTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(fontTagNode);

			return true;
		} else if (bbStr.equals("email")) {
			TagNode aTagNode = new TagNode("a");
			aTagNode.addAttribute("href", "emailto:" + innerTag.trim(), true);
			aTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(aTagNode);

			return true;
		} else if (bbStr.equals("list")) {
			int listStart = 0;
			int listEnd = 0;
			TagNode listTagNode;
			if (bbAttr != null) {
				if (bbAttr.equals("a")) {
					listTagNode = new TagNode("ul");
				} else {
					listTagNode = new TagNode("ol");
				}
			} else {
				listTagNode = new TagNode("ul");
			}
			fWikiModel.pushNode(listTagNode);
			try {
				while (listEnd >= 0) {
					listEnd = innerTag.indexOf("[*]", listStart);
					if (listEnd > listStart) {
						if (listStart == 0) {
							parseNextPHPBBCode(innerTag.substring(0, listEnd));
							// fWikiModel.append(new ContentToken(innerTag.substring(0,
							// listEnd)));
						} else {
							listTagNode = new TagNode("li");
							fWikiModel.pushNode(listTagNode);
							try {
								parseNextPHPBBCode(innerTag.substring(listStart, listEnd));
								// listTagNode.addChild(new
								// ContentToken(innerTag.substring(listStart, listEnd)));
							} finally {
								fWikiModel.popNode();
							}
						}
						listStart = listEnd + 3;
					}
				}
				if (listStart == 0) {
					parseNextPHPBBCode(innerTag);
					// fWikiModel.append(new ContentToken(innerTag));
				} else {
					if (listStart < innerTag.length()) {
						listTagNode = new TagNode("li");
						fWikiModel.pushNode(listTagNode);
						try {
							parseNextPHPBBCode(innerTag.substring(listStart, innerTag.length()));
							// listTagNode.addChild(new
							// ContentToken(innerTag.substring(listStart,
							// innerTag.length())));
							// fWikiModel.append(listTagNode);
						} finally {
							fWikiModel.popNode();
						}
					}
				}

			} finally {
				fWikiModel.popNode();
			}
			return true;
		} else if (bbStr.equals("img")) {
			TagNode imgTagNode = new TagNode("img");
			imgTagNode.addAttribute("src", innerTag.trim(), true);
			imgTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(imgTagNode);

			return true;
		} else if (bbStr.equals("quote")) {
			TagNode quoteTagNode = new TagNode("blockquote");
			// quoteTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.pushNode(quoteTagNode);
			try {
				parseNextPHPBBCode(innerTag);
			} finally {
				fWikiModel.popNode();
			}
			return true;
		} else if (bbStr.equals("size")) {
			if (bbAttr == null) {
				return false;
			}
			TagNode fontTagNode = new TagNode("font");
			fontTagNode.addAttribute("size", bbAttr, true);
			fontTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(fontTagNode);

			return true;
		} else if (bbStr.equals("url")) {
			if (bbAttr != null) {
				TagNode aTagNode = new TagNode("a");
				aTagNode.addAttribute("href", bbAttr, true);
				aTagNode.addChild(new ContentToken(innerTag));
				fWikiModel.append(aTagNode);

				return true;
			} else {
				TagNode aTagNode = new TagNode("a");
				aTagNode.addAttribute("href", innerTag.trim(), true);
				aTagNode.addChild(new ContentToken(innerTag));
				fWikiModel.append(aTagNode);

				return true;
			}
		} else if (bbStr.equals("b")) {
			TagNode boldTagNode = new TagNode("b");
			boldTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(boldTagNode);
			return true;
		} else if (bbStr.equals("i")) {
			TagNode italicTagNode = new TagNode("i");
			italicTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(italicTagNode);
			return true;
		} else if (bbStr.equals("u")) {
			TagNode underlineTagNode = new TagNode("u");
			underlineTagNode.addChild(new ContentToken(innerTag));
			fWikiModel.append(underlineTagNode);
			return true;
		}

		return false;
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

	/**
	 * Parse an HTML comment.
	 * 
	 * @return
	 */
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

	private void parseNextPHPBBCode(String rawWikitext) {
		int index = rawWikitext.indexOf('[');
		int lastIndex = 0;
		int tempIndex = -1;
		if (index < 0) {
			fWikiModel.append(new ContentToken(rawWikitext));
			return;
		}
		try {
			while (index >= 0) {
				String temp = rawWikitext.substring(lastIndex, index);
				if (temp.length() > 0) {
					fWikiModel.append(new ContentToken(temp));
				}
				tempIndex = parsePHPBBCodeRecursive(rawWikitext, index + 1);
				if (tempIndex < 0) {
					lastIndex = index + 1;
					index = rawWikitext.indexOf('[', index + 1);
				} else {
					lastIndex = tempIndex;
					index = rawWikitext.indexOf('[', tempIndex);
				}
			}
		} catch (IndexOutOfBoundsException e) {
			//
		}
		if (lastIndex < rawWikitext.length()) {
			fWikiModel.append(new ContentToken(rawWikitext.substring(lastIndex)));
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
		} catch (Exception e) {
			e.printStackTrace();
			TagNode error = new TagNode("span");
			error.addAttribute("class", "error", true);
			error.addChild(new ContentToken(e.getClass().getSimpleName()));
			localStack.append(error);
		} catch (Error e) {
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

	/**
	 * Represents the result of parsing a (potential) page name.
	 * 
	 * Note that {@link #magicWord} takes precedence over {@link #pagename}, i.e.
	 * if {@link #magicWord} if not <tt>null</tt>, the parsed name is a magic
	 * word!
	 * 
	 * @author Nico Kruber, kruber@zib.de
	 */
	public static class ParsedPageName {
		/**
		 * The namespace the page is in.
		 */
		public final INamespaceValue namespace;
		/**
		 * The name of the page (without the namespace).
		 */
		public final String pagename;
		/**
		 * Whether this pagename was successfully parsed or not.
		 */
		public final boolean valid;
		/**
		 * If the pagename was a magic word it will be this, otherwise <tt>null</tt>
		 * .
		 * 
		 * The object type depends on the concrete
		 * {@link info.bliki.wiki.filter.MagicWord} implementation used by the
		 * {@link IWikiModel}, e.g.
		 * {@link info.bliki.wiki.filter.MagicWord.MagicWordE} in case
		 * {@link info.bliki.wiki.filter.MagicWord} is used.
		 */
		public final Object magicWord;
		/**
		 * Parameters of the magic word (<tt>null</tt> if not supplied).
		 */
		public final String magicWordParameter;

		/**
		 * Creates a new parsed page name object (no magic word).
		 * 
		 * @param namespace
		 *          the namespace the page is in
		 * @param pagename
		 *          the name of the page (without the namespace)
		 * @param valid
		 *          whether this pagename was successfully parsed or not
		 */
		public ParsedPageName(INamespaceValue namespace, String pagename, boolean valid) {
			this.namespace = namespace;
			this.pagename = pagename;
			this.valid = valid;
			this.magicWord = null;
			this.magicWordParameter = null;
		}

		/**
		 * Creates a new parsed page name object.
		 * 
		 * @param namespace
		 *          the namespace the page is in
		 * @param pagename
		 *          the name of the page (without the namespace)
		 * @param magicWord
		 *          the magic word object if the pagename was a magic word,
		 *          otherwise <tt>null</tt>
		 * @param magicWordParameter
		 *          parameters of the magic word (<tt>null</tt> if not supplied)
		 * @param valid
		 *          whether this pagename was successfully parsed or not
		 */
		public ParsedPageName(INamespaceValue namespace, String pagename, Object magicWord, String magicWordParameter, boolean valid) {
			this.namespace = namespace;
			this.pagename = pagename;
			this.valid = valid;
			this.magicWord = magicWord;
			this.magicWordParameter = magicWordParameter;
		}
	}

	/**
	 * Parses a given page name into its components, e.g. namespace and pagename
	 * or magic word and parameters.
	 * 
	 * @param wikiModel
	 *          the wiki model to use
	 * @param pagename
	 *          the name in wiki text
	 * @param namespace
	 *          the default namespace to use if there is no namespace in the
	 *          pagename
	 * @param magicWordAllowed
	 *          whether the <tt>pagename</tt> may be a magic word or not (if it is
	 *          a magic word and this is set to <tt>false</tt>, it will be parsed
	 *          as if it is a page name)
	 * 
	 * @return a parsed page name
	 */
	public static ParsedPageName parsePageName(IWikiModel wikiModel, String pagename, INamespaceValue namespace,
			boolean magicWordAllowed, boolean stripOffSection) {
		// if a magic word is recognised, it will be non-null:
		Object magicWord = null;
		String magicWordParameter = null;
		if (pagename.length() > 0 && pagename.charAt(0) == ':') {
			magicWordAllowed = false; // this is not allowed for magic words
			if (pagename.length() > 1 && pagename.charAt(1) == ':') {
				// double "::" are not parsed as template/transclusion
				return new ParsedPageName(namespace, pagename, false);
			}
			// assume main namespace for now:
			namespace = wikiModel.getNamespace().getMain();
			pagename = pagename.substring(1);
		}

		if (stripOffSection) {
			// parse away any "#label" markers which are not supported
			int hashIndex = pagename.indexOf('#');
			if (hashIndex != (-1)) {
				pagename = pagename.substring(0, hashIndex);
			}
		}

		int indx = pagename.indexOf(':');
		if (indx > 0) {
			String maybeNamespaceStr0 = pagename.substring(0, indx);
			INamespaceValue maybeNamespace = wikiModel.getNamespace().getNamespace(maybeNamespaceStr0);
			if (maybeNamespace != null) {
				return new ParsedPageName(maybeNamespace, pagename.substring(indx + 1), true);
			} else if (magicWordAllowed) {
				// no namespace? maybe a magic word with a parameter?
				magicWord = wikiModel.getMagicWord(maybeNamespaceStr0);
				if (magicWord != null) {
					magicWordParameter = pagename.substring(indx + 1);
					if (magicWordParameter.length() != 0) {
						magicWordParameter = AbstractTemplateFunction.parseTrim(magicWordParameter, wikiModel);
					}
				}
			}
		} else if (magicWordAllowed && namespace.isType(NamespaceCode.TEMPLATE_NAMESPACE_KEY)) {
			Object maybeMagicWord = wikiModel.getMagicWord(pagename);
			if (maybeMagicWord != null) {
				magicWord = maybeMagicWord;
			}
		}
		return new ParsedPageName(namespace, pagename, magicWord, magicWordParameter, true);
	}

	public static String getRedirectedTemplateContent(IWikiModel wikiModel, String redirectedLink,
			Map<String, String> templateParameters) {
		final INamespace namespace = wikiModel.getNamespace();
		ParsedPageName parsedPagename = AbstractParser.parsePageName(wikiModel, redirectedLink, namespace.getMain(), false, false);
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

	/**
	 * Check the text for a <code>#REDIRECT [[...]]</code> or
	 * <code>#redirect [[...]]</code> link
	 * 
	 * @param rawWikiText
	 *          the wiki text
	 * @param wikiModel
	 * @return <code>non-null</code> if a redirect was found and further parsing
	 *         should be cancelled according to the model.
	 */
	public static String parseRedirect(String rawWikiText, IWikiModel wikiModel) {
		int redirectStart = -1;
		int redirectEnd = -1;
		for (int i = 0; i < rawWikiText.length(); i++) {
			if (rawWikiText.charAt(i) == '#') {
				if (startsWith(rawWikiText, i + 1, "redirect", true)) {
					redirectStart = rawWikiText.indexOf("[[", i + 8);
					if (redirectStart > i + 8) {
						redirectStart += 2;
						redirectEnd = rawWikiText.indexOf("]]", redirectStart);
					}
				}
				break;
			}
			if (Character.isWhitespace(rawWikiText.charAt(i))) {
				continue;
			}
			break;
		}

		if (redirectEnd >= 0) {
			String redirectedLink = rawWikiText.substring(redirectStart, redirectEnd);
			if (wikiModel.appendRedirectLink(redirectedLink)) {
				return redirectedLink;
			}
		}
		return null;
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

	/**
	 * Reduce the current token stack completely
	 */
	protected void reduceTokenStack() {
		while (fWikiModel.stackSize() > 0) {
			fWikiModel.popNode();
		}
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

	/**
	 * Reduce the current token stack until an allowed parent is at the top of the
	 * stack
	 */
	protected void reduceTokenStack(TagToken node) {
		String allowedParents = node.getParents();
		if (allowedParents != null) {
			TagToken tag;
			int index = -1;

			while (fWikiModel.stackSize() > 0) {
				tag = fWikiModel.peekNode();
				index = allowedParents.indexOf("|" + tag.getName() + "|");
				if (index < 0) {
					fWikiModel.popNode();
					if (tag.getName().equals(node.getName())) {
						// for wrong nested HTML tags like <table> <tr><td>number
						// 1<tr><td>number 2</table>
						break;
					}
				} else {
					break;
				}
			}
		} else {
			while (fWikiModel.stackSize() > 0) {
				fWikiModel.popNode();
			}
		}
	}

}

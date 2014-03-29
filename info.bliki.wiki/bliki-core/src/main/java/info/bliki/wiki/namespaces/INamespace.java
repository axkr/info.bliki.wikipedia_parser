package info.bliki.wiki.namespaces;

import info.bliki.wiki.filter.Encoder;
import info.bliki.wiki.namespaces.Namespace.NamespaceValue;

import java.util.List;
import java.util.ResourceBundle;

/**
 * Mediawiki namespace for a specific language. See <a
 * href="http://www.mediawiki.org/wiki/Manual:Namespace#Built-in_namespaces"
 * >Mediawiki - Manual:Namespace</a>
 * 
 */
public interface INamespace {
	/**
	 * Enum of all valid namespace codes.
	 */
	public enum NamespaceCode {
		/**
		 * Alias for direct links to media files.
		 */
		MEDIA_NAMESPACE_KEY(Integer.valueOf(-2)),

		/**
		 * Holds special pages.
		 */
		SPECIAL_NAMESPACE_KEY(Integer.valueOf(-1)),

		/**
		 * "Real" content; articles. Has no prefix.
		 */
		MAIN_NAMESPACE_KEY(Integer.valueOf(0)),

		/**
		 * Talk pages of "Real" content
		 */
		TALK_NAMESPACE_KEY(Integer.valueOf(1)),

		/**
		 * 
		 */
		USER_NAMESPACE_KEY(Integer.valueOf(2)),

		/**
		 * Talk pages for User Pages
		 */
		USER_TALK_NAMESPACE_KEY(Integer.valueOf(3)),

		/**
		 * Information about the wiki. Prefix is the same as $wgSitename of the PHP
		 * installation.
		 */
		PROJECT_NAMESPACE_KEY(Integer.valueOf(4)),

		/**
		 * 
		 */
		PROJECT_TALK_NAMESPACE_KEY(Integer.valueOf(5)),

		/**
		 * Media description pages.
		 */
		FILE_NAMESPACE_KEY(Integer.valueOf(6)),

		/**
		 * 
		 */
		FILE_TALK_NAMESPACE_KEY(Integer.valueOf(7)),

		/**
		 * Site interface customisation. Protected.
		 */
		MEDIAWIKI_NAMESPACE_KEY(Integer.valueOf(8)),

		/**
		 * 
		 */
		MEDIAWIKI_TALK_NAMESPACE_KEY(Integer.valueOf(9)),

		/**
		 * Template pages.
		 */
		TEMPLATE_NAMESPACE_KEY(Integer.valueOf(10)),

		/**
		 * 
		 */
		TEMPLATE_TALK_NAMESPACE_KEY(Integer.valueOf(11)),

		/**
		 * Help pages.
		 */
		HELP_NAMESPACE_KEY(Integer.valueOf(12)),

		/**
		 * 
		 */
		HELP_TALK_NAMESPACE_KEY(Integer.valueOf(13)),

		/**
		 * Category description pages.
		 */
		CATEGORY_NAMESPACE_KEY(Integer.valueOf(14)),

		/**
		 * 
		 */
		CATEGORY_TALK_NAMESPACE_KEY(Integer.valueOf(15)),

		/**
		 * Portal pages.
		 */
		PORTAL_NAMESPACE_KEY(Integer.valueOf(100)),

		/**
		 * Talk pages for portal pages.
		 */
		PORTAL_TALK_NAMESPACE_KEY(Integer.valueOf(101));

		/**
		 * The integer number code of this namespace.
		 */
		public final Integer code;
		private NamespaceCode(Integer code) {
			this.code = code;
		}
	}

	/**
	 * Interface for all namespace constants.
	 * 
	 * @author Nico Kruber, kruber@zib.de
	 */
	public interface INamespaceValue {

		/**
		 * @return the (internal) integer code of this namespace
		 */
		public abstract NamespaceCode getCode();

		/**
		 * Re-sets the texts used for this namespace. The first will be the
		 * primary text.
		 * 
		 * @param aliases
		 *            all aliases for the namespace
		 */
		public abstract void setTexts(String... aliases);

		/**
		 * Adds a single alias to the namespace.
		 * 
		 * @param alias
		 *            the alias
		 */
		public abstract void addAlias(String alias);

		/**
		 * Provided for convenience.
		 * 
		 * @return the primary text for the namespace, i.e. the first value of
		 *         {@link #getTexts()}.
		 */
		public abstract String getPrimaryText();

		/**
		 * @return the texts
		 */
		public abstract List<String> getTexts();

		/**
		 * @return the associated talk namespace (may be <tt>null</tt>)
		 */
		public abstract NamespaceValue getTalkspace();

		/**
		 * @return the associated content namespace
		 */
		public abstract NamespaceValue getContentspace();

		/**
		 * Prepends the namespace to the given pagename and returns the full
		 * name with a separation character between a (non-empty) namespace and
		 * the page name.
		 * 
		 * @param pageName
		 *            the page name without a namespace, e.g. &quot;Test&quot;
		 * 
		 * @return the full page name, e.g. &quot;Template:Test&quot;
		 */
		public abstract String makeFullPagename(String pageName);

		/**
		 * Checks whether the namespace is a namespace of the given type.
		 * 
		 * @param code
		 *            the code
		 * 
		 * @return <tt>true</tt> if the namespace is of the given code,
		 *         <tt>false</tt> otherwise
		 */
		public abstract boolean isType(NamespaceCode code);

	}
	/**
	 * Get the &quot;Media&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getMedia();

	/**
	 * Get the &quot;Special&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getSpecial();

	/**
	 * The main namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getMain();

	/**
	 * The &quot;Talk&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getTalk();

	/**
	 * The &quot;User&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getUser();

	/**
	 * The &quot;User talk&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getUser_talk();

	/**
	 * The &quot;Meta&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getMeta();

	/**
	 * The &quot;Meta talk&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getMeta_talk();

	/**
	 * The &quot;File&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getImage();

	/**
	 * The &quot;File talk&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getImage_talk();

	/**
	 * The &quot;MediaWiki&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getMediaWiki();

	/**
	 * The &quot;MediaWiki talk&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getMediaWiki_talk();

	/**
	 * The &quot;Template&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getTemplate();

	/**
	 * The &quot;Template talk&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getTemplate_talk();

	/**
	 * The &quot;Help&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getHelp();

	/**
	 * The &quot;Help talk&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getHelp_talk();

	/**
	 * The &quot;Category&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getCategory();

	/**
	 * The &quot;Category talk&quot; namespace for the current language.
	 */
	public INamespaceValue getCategory_talk();

	/**
	 * The &quot;Portal&quot; namespace for the current language.
	 * 
	 * @return the namespace
	 */
	public INamespaceValue getPortal();

	/**
	 * The &quot;Portal talk&quot; namespace for the current language.
	 */
	public INamespaceValue getPortal_talk();

	/**
	 * Checks whether the given string is a valid namespace of the given type.
	 * 
	 * @param namespace
	 *            the potential namespace string
	 * @param code
	 *            the code
	 * 
	 * @return <tt>true</tt> if the given namespace is of the given code,
	 *         <tt>false</tt> otherwise
	 */
	public abstract boolean isNamespace(String namespace, NamespaceCode code);

	/**
	 * Checks whether the given namespace is a namespace of the given type.
	 * 
	 * @param namespace
	 *            the namespace
	 * @param code
	 *            the code
	 * 
	 * @return <tt>true</tt> if the given namespace is of the given code,
	 *         <tt>false</tt> otherwise
	 */
	public abstract boolean isNamespace(INamespaceValue namespace, NamespaceCode code);

	public INamespaceValue getNamespace(String namespace);

	public INamespaceValue getNamespaceByNumber(NamespaceCode numberCode);
	public INamespaceValue getNamespaceByNumber(int numberCode);

	public ResourceBundle getResourceBundle();

	/**
	 * Get the Talk namespace.
	 * 
	 * @param namespace
	 *          the namespace
	 * @return the talk namespace for the given namespace or <tt>null</tt> if the
	 *         namespace is invalid or there is no talk namespace
	 * 
	 * @see #getContentspace(String)
	 */
	public INamespaceValue getTalkspace(String namespace);

	/**
	 * Gets the content namespace for a given (talk) namespace.
	 * 
	 * @param talkNamespace
	 *          the namespace, potentially a talkspace
	 * 
	 * @return the content namespace or <tt>null</tt>
	 * 
	 * @see #getTalkspace(String)
	 */
	public INamespaceValue getContentspace(String talkNamespace);

	/**
	 * Splits the given full title into its namespace and page title components
	 * and normalises both components using
	 * {@link Encoder#normaliseTitle(String, boolean, char, boolean)}. Assumes
	 * <tt>underScoreIsWhitespace</tt>, uses a space as <tt>whiteSpaceChar</tt>
	 * and capitalises the first character.
	 * 
	 * @param fullTitle
	 *            the (full) title including a namespace (if present)
	 * 
	 * @return a 2-element array with the raw namespace string (index 0) and the
	 *         page title (index 1)
	 * @see #splitNsTitle(String, boolean, char, boolean)
	 */
	public abstract String[] splitNsTitle(String fullTitle);

	/**
	 * Splits the given full title into its namespace and page title components
	 * and normalises both components using
	 * {@link Encoder#normaliseTitle(String, boolean, char, boolean)}.
	 * 
	 * @param fullTitle
	 *            the (full) title including a namespace (if present)
	 * @param underScoreIsWhitespace
	 *            whether '_' should be seen as whitespace or not
	 * @param whiteSpaceChar
	 *            the character to replace whitespace with
	 * @param firstCharacterAsUpperCase
	 *          if <code>true</code> convert the first of the title to upper case
	 * 
	 * @return a 2-element array with the raw namespace string (index 0) and the
	 *         page title (index 1)
	 */
	public abstract String[] splitNsTitle(String fullTitle,
			boolean underScoreIsWhitespace, char whiteSpaceChar,
			boolean firstCharacterAsUpperCase);
}
package info.bliki.wiki.namespaces;

import info.bliki.Messages;
import info.bliki.wiki.filter.Encoder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Mediawiki Namespaces. See <a
 * href="http://www.mediawiki.org/wiki/Manual:Namespace#Built-in_namespaces"
 * >Mediawiki - Manual:Namespace</a>
 * 
 */
public class Namespace implements INamespace {
	/**
	 * Maps namespaces case-insensitively to their according
	 * {@link NamespaceValue} objects.
	 */
	protected final Map<String, NamespaceValue> TEXT_TO_NAMESPACE_MAP = new TreeMap<String, NamespaceValue>(
			String.CASE_INSENSITIVE_ORDER);

	/**
	 * Fast access to each {@link NamespaceValue} via an integer index similar to
	 * its number code.
	 * 
	 * @see Namespace#numberCodeToInt(int)
	 * @see Namespace#intToNumberCode(int)
	 */
	protected final NamespaceValue[] INT_TO_NAMESPACE = new NamespaceValue[20];

	/**
	 * The &quot;Media&quot; namespace for the current language.
	 */
	public final NamespaceValue MEDIA = new NamespaceValue(NamespaceCode.MEDIA_NAMESPACE_KEY, null, "Media");
	/**
	 * The &quot;Special&quot; namespace for the current language.
	 */
	public final NamespaceValue SPECIAL = new NamespaceValue(NamespaceCode.SPECIAL_NAMESPACE_KEY, null, "Special");
	/**
	 * The &quot;Talk&quot; namespace for the current language.
	 */
	public final NamespaceValue TALK = new NamespaceValue(NamespaceCode.TALK_NAMESPACE_KEY, true, "Talk");
	/**
	 * The main namespace for the current language.
	 */
	public final NamespaceValue MAIN = new NamespaceValue(NamespaceCode.MAIN_NAMESPACE_KEY, TALK, "");
	/**
	 * The &quot;User talk&quot; namespace for the current language.
	 */
	public final NamespaceValue USER_TALK = new NamespaceValue(NamespaceCode.USER_TALK_NAMESPACE_KEY, true, "User_talk");
	/**
	 * The &quot;User&quot; namespace for the current language.
	 */
	public final NamespaceValue USER = new NamespaceValue(NamespaceCode.USER_NAMESPACE_KEY, USER_TALK, "User");
	/**
	 * The &quot;Meta talk&quot; namespace for the current language.
	 */
	public final NamespaceValue META_TALK = new NamespaceValue(NamespaceCode.PROJECT_TALK_NAMESPACE_KEY, true, "Meta_talk");
	/**
	 * The &quot;Meta&quot; namespace for the current language.
	 */
	public final NamespaceValue META = new NamespaceValue(NamespaceCode.PROJECT_NAMESPACE_KEY, META_TALK, "Meta");
	/**
	 * The &quot;File talk&quot; namespace for the current language.
	 */
	public final NamespaceValue IMAGE_TALK = new NamespaceValue(NamespaceCode.FILE_TALK_NAMESPACE_KEY, true, "File_talk",
			"Image_talk");
	/**
	 * The &quot;File&quot; namespace for the current language.
	 */
	public final NamespaceValue IMAGE = new NamespaceValue(NamespaceCode.FILE_NAMESPACE_KEY, IMAGE_TALK, "File", "Image");
	/**
	 * The &quot;MediaWiki talk&quot; namespace for the current language.
	 */
	public final NamespaceValue MEDIAWIKI_TALK = new NamespaceValue(NamespaceCode.MEDIAWIKI_TALK_NAMESPACE_KEY, true,
			"MediaWiki_talk");
	/**
	 * The &quot;MediaWiki&quot; namespace for the current language.
	 */
	public final NamespaceValue MEDIAWIKI = new NamespaceValue(NamespaceCode.MEDIAWIKI_NAMESPACE_KEY, MEDIAWIKI_TALK, "MediaWiki");
	/**
	 * The &quot;Template talk&quot; namespace for the current language.
	 */
	public final NamespaceValue TEMPLATE_TALK = new NamespaceValue(NamespaceCode.TEMPLATE_TALK_NAMESPACE_KEY, true, "Template_talk");
	/**
	 * The &quot;Template&quot; namespace for the current language.
	 */
	public final NamespaceValue TEMPLATE = new NamespaceValue(NamespaceCode.TEMPLATE_NAMESPACE_KEY, TEMPLATE_TALK, "Template");
	/**
	 * The &quot;Help talk&quot; namespace for the current language.
	 */
	public final NamespaceValue HELP_TALK = new NamespaceValue(NamespaceCode.HELP_TALK_NAMESPACE_KEY, true, "Help_talk");
	/**
	 * The &quot;Help&quot; namespace for the current language.
	 */
	public final NamespaceValue HELP = new NamespaceValue(NamespaceCode.HELP_NAMESPACE_KEY, HELP_TALK, "Help");
	/**
	 * The &quot;Category talk&quot; namespace for the current language.
	 */
	public final NamespaceValue CATEGORY_TALK = new NamespaceValue(NamespaceCode.CATEGORY_TALK_NAMESPACE_KEY, true, "Category_talk");
	/**
	 * The &quot;Category&quot; namespace for the current language.
	 */
	public final NamespaceValue CATEGORY = new NamespaceValue(NamespaceCode.CATEGORY_NAMESPACE_KEY, CATEGORY_TALK, "Category");
	/**
	 * The &quot;Portal talk&quot; namespace for the current language.
	 */
	public final NamespaceValue PORTAL_TALK = new NamespaceValue(NamespaceCode.PORTAL_TALK_NAMESPACE_KEY, true, "Portal_talk");
	/**
	 * The &quot;Portal&quot; namespace for the current language.
	 */
	public final NamespaceValue PORTAL = new NamespaceValue(NamespaceCode.PORTAL_NAMESPACE_KEY, PORTAL_TALK, "Portal");

	/**
	 * Base class for all namespace constants.
	 * 
	 * @author Nico Kruber, kruber@zib.de
	 */
	public class NamespaceValue implements INamespaceValue {
		private final NamespaceCode code;
		private List<String> texts = new ArrayList<String>(2);
		private final NamespaceValue talkspace;
		private NamespaceValue contentspace = null;

		/**
		 * Constructor for talk namespaces.
		 * 
		 * @param code
		 *          the (internal) integer code of this namespace
		 * @param isTalkspace
		 *          must be <tt>true</tt> (needed to distinguish this constructor
		 *          from the other in case of <tt>null</tt> talk spaces)
		 * @param aliases
		 *          all aliases identifying this namespace
		 */
		private NamespaceValue(NamespaceCode code, boolean isTalkspace, String... aliases) {
			assert (isTalkspace);
			this.code = code;
			int arrayPos = numberCodeToInt(code.code);
			assert (INT_TO_NAMESPACE[arrayPos] == null);
			INT_TO_NAMESPACE[arrayPos] = this;
			this.talkspace = this;
			// contentspace is set by the content NamespaceValue
			setTexts(aliases);
		}

		/**
		 * Constructor for content namespaces.
		 * 
		 * @param code
		 *          the (internal) integer code of this namespace
		 * @param talkspace
		 *          the associated talk namespace (must not be <tt>null</tt>)
		 * @param aliases
		 *          all aliases identifying this namespace
		 */
		private NamespaceValue(NamespaceCode code, NamespaceValue talkspace, String... aliases) {
			this.code = code;
			int arrayPos = numberCodeToInt(code.code);
			assert (INT_TO_NAMESPACE[arrayPos] == null);
			INT_TO_NAMESPACE[arrayPos] = this;
			this.talkspace = talkspace;
			// mapping of talkspace to content space is 1:1 if a talkspace exists
			if (this.talkspace != null) {
				assert (this.talkspace.contentspace == null);
				this.talkspace.contentspace = this;
			}
			this.contentspace = this;
			setTexts(aliases);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see info.bliki.wiki.namespaces.INamespaceValue#getCode()
		 */
		@Override
		public NamespaceCode getCode() {
			return code;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * info.bliki.wiki.namespaces.INamespaceValue#setTexts(java.lang.String)
		 */
		@Override
		public void setTexts(String... aliases) {
			assert (aliases.length >= 1);
			// remove old texts:
			for (String text : this.texts) {
				TEXT_TO_NAMESPACE_MAP.remove(text);
				TEXT_TO_NAMESPACE_MAP.remove(text.replace(' ', '_'));
				TEXT_TO_NAMESPACE_MAP.remove(text.replace('_', ' '));
			}
			// note: don't assign the fixed-size list of Arrays.asList to texts!
			texts = new ArrayList<String>(aliases.length);
			for (String alias : aliases) {
				assert (alias != null);
				addAlias(alias);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * info.bliki.wiki.namespaces.INamespaceValue#addAlias(java.lang.String)
		 */
		@Override
		public void addAlias(String alias) {
			if (!TEXT_TO_NAMESPACE_MAP.containsKey(alias)) {
				texts.add(alias);
				TEXT_TO_NAMESPACE_MAP.put(alias, this);
				TEXT_TO_NAMESPACE_MAP.put(alias.replace(' ', '_'), this);
				TEXT_TO_NAMESPACE_MAP.put(alias.replace('_', ' '), this);
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see info.bliki.wiki.namespaces.INamespaceValue#getPrimaryText()
		 */
		@Override
		public String getPrimaryText() {
			return texts.get(0);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see info.bliki.wiki.namespaces.INamespaceValue#getTexts()
		 */
		@Override
		public List<String> getTexts() {
			return texts;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see info.bliki.wiki.namespaces.INamespaceValue#getTalkspace()
		 */
		@Override
		public NamespaceValue getTalkspace() {
			return talkspace;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see info.bliki.wiki.namespaces.INamespaceValue#getContentspace()
		 */
		@Override
		public NamespaceValue getContentspace() {
			return contentspace;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see info.bliki.wiki.namespaces.INamespaceValue#makeFullPagename(String)
		 */
		@Override
		public String makeFullPagename(String pageName) {
			String primaryText = getPrimaryText();
			if (primaryText.isEmpty()) {
				return pageName;
			} else {
				return primaryText + ":" + pageName;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * info.bliki.wiki.namespaces.INamespace.INamespaceValue#isType(info.bliki
		 * .wiki.namespaces.INamespace.NamespaceCode)
		 */
		@Override
		public boolean isType(NamespaceCode code) {
			return this.code == code;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return getPrimaryText();
		}
	}

	protected ResourceBundle fResourceBundle = null;
	protected ResourceBundle fResourceBundleEn = null;

	public Namespace() {
		this((ResourceBundle) null);
	}

	public Namespace(Locale locale) {
		this(Messages.getResourceBundle(locale));
	}

	public Namespace(ResourceBundle resourceBundle) {
		fResourceBundle = resourceBundle;
		fResourceBundleEn = Messages.getResourceBundle(Locale.ENGLISH);
		initializeNamespaces();
	}

	@Override
	public boolean isNamespace(String namespace, NamespaceCode code) {
		NamespaceValue nsVal = getNamespace(namespace);
		if (nsVal != null) {
			return isNamespace(nsVal, code);
		}
		return false;
	}

	@Override
	public boolean isNamespace(INamespaceValue namespace, NamespaceCode code) {
		if (namespace == null) {
			return false;
		}
		return namespace.getCode() == code;
	}

	@Override
	public NamespaceValue getNamespace(String namespace) {
		return TEXT_TO_NAMESPACE_MAP.get(namespace);
	}

	@Override
	public NamespaceValue getNamespaceByNumber(NamespaceCode numberCode) {
		return getNamespaceByNumber(numberCode.code);
	}

	@Override
	public NamespaceValue getNamespaceByNumber(int numberCode) {
		final int arrayPos = numberCodeToInt(numberCode);
		if (arrayPos >= 0 && arrayPos < INT_TO_NAMESPACE.length) {
			return INT_TO_NAMESPACE[arrayPos];
		}
		return null;
	}

	/**
	 * Converts an (external) namespace number code to the position in the
	 * {@link #INT_TO_NAMESPACE} array.
	 * 
	 * @param numberCode
	 *          a code like {@link INamespace#MEDIA_NAMESPACE_KEY}
	 * 
	 * @return an array index
	 */
	protected static int numberCodeToInt(int numberCode) {
		if (numberCode >= -2 && numberCode <= 15) {
			return numberCode + 2;
		} else if (numberCode >= 100 && numberCode <= 101) {
			return numberCode - 100 + 18;
		} else {
			throw new InvalidParameterException("unknown number code: " + numberCode);
		}
	}

	/**
	 * Converts an (internal) namespace number code (the position in the
	 * {@link #INT_TO_NAMESPACE} array) to the external namespace number.
	 * 
	 * @param numberCode
	 *          internal array index
	 * 
	 * @return a number code like {@link INamespace#MEDIA_NAMESPACE_KEY}
	 */
	protected static int intToNumberCode(int numberCode) {
		if (numberCode >= 0 && numberCode <= 17) {
			return numberCode - 2;
		} else if (numberCode >= 18 && numberCode <= 19) {
			return numberCode + 100 - 18;
		} else {
			throw new InvalidParameterException("unknown number code: " + numberCode);
		}
	}

	@Override
	public ResourceBundle getResourceBundle() {
		return fResourceBundle;
	}

	protected enum ExtractType {
		REPLACE_TEXTS, APPEND_AS_ALIASES;
	}
	
	/**
	 * Extracts the two namespace strings from the resource bundle into the
	 * {@link #fNamespaces1} and {@link #fNamespaces2} arrays.
	 * 
	 * @param ns1Id
	 *          the first id in the bundle, e.g. {@link Messages#WIKI_API_MEDIA1}
	 * @param ns2Id
	 *          the first id in the bundle, e.g. {@link Messages#WIKI_API_MEDIA2}
	 * @param code
	 *          the namespace code
	 */
	private void extractFromResource(ResourceBundle resourceBundle, String ns1Id, String ns2Id, NamespaceCode code, ExtractType cmd) {
		NamespaceValue namespace = getNamespaceByNumber(code);
		assert (namespace != null) : "undefined namespace code: " + code;
		String ns1 = Messages.getString(resourceBundle, ns1Id, null);
		if (ns1 != null) {
			String ns2 = Messages.getString(resourceBundle, ns2Id, null);
			switch (cmd) {
				case REPLACE_TEXTS:
					if (ns2 != null) {
						namespace.setTexts(ns1, ns2);
					} else {
						namespace.setTexts(ns1);
					}
				case APPEND_AS_ALIASES:
					namespace.addAlias(ns1);
					if (ns2 != null) {
						namespace.addAlias(ns2);
					}
			}
		}
	}

	protected void extractFromResource(ResourceBundle resource, ExtractType cmd) {
		if (resource == null) {
			return;
		}
		extractFromResource(resource, Messages.WIKI_API_MEDIA1, Messages.WIKI_API_MEDIA2, NamespaceCode.MEDIA_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_SPECIAL1, Messages.WIKI_API_SPECIAL2, NamespaceCode.SPECIAL_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_TALK1, Messages.WIKI_API_TALK2, NamespaceCode.TALK_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_USER1, Messages.WIKI_API_USER2, NamespaceCode.USER_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_USERTALK1, Messages.WIKI_API_USERTALK2, NamespaceCode.USER_TALK_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_META1, Messages.WIKI_API_META2, NamespaceCode.PROJECT_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_METATALK1, Messages.WIKI_API_METATALK2, NamespaceCode.PROJECT_TALK_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_IMAGE1, Messages.WIKI_API_IMAGE2, NamespaceCode.FILE_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_IMAGETALK1, Messages.WIKI_API_IMAGETALK2, NamespaceCode.FILE_TALK_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_MEDIAWIKI1, Messages.WIKI_API_MEDIAWIKI2, NamespaceCode.MEDIAWIKI_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_MEDIAWIKITALK1, Messages.WIKI_API_MEDIAWIKITALK2,
				NamespaceCode.MEDIAWIKI_TALK_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_TEMPLATE1, Messages.WIKI_API_TEMPLATE2, NamespaceCode.TEMPLATE_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_TEMPLATETALK1, Messages.WIKI_API_TEMPLATETALK2, NamespaceCode.TEMPLATE_TALK_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_HELP1, Messages.WIKI_API_HELP2, NamespaceCode.HELP_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_HELPTALK1, Messages.WIKI_API_HELPTALK2, NamespaceCode.HELP_TALK_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_CATEGORY1, Messages.WIKI_API_CATEGORY2, NamespaceCode.CATEGORY_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_CATEGORYTALK1, Messages.WIKI_API_CATEGORYTALK2, NamespaceCode.CATEGORY_TALK_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_PORTAL1, Messages.WIKI_API_PORTAL2, NamespaceCode.PORTAL_NAMESPACE_KEY, cmd);
		extractFromResource(resource, Messages.WIKI_API_PORTALTALK1, Messages.WIKI_API_PORTALTALK2, NamespaceCode.PORTAL_TALK_NAMESPACE_KEY, cmd);
	}

	private void initializeNamespaces() {
		extractFromResource(fResourceBundle, ExtractType.REPLACE_TEXTS);
		extractFromResource(fResourceBundleEn, ExtractType.APPEND_AS_ALIASES);
		
		// Aliases as defined by
		// https://en.wikipedia.org/wiki/Wikipedia:Namespace#Aliases
		META.addAlias("WP");
		META.addAlias("Project");
		META_TALK.addAlias("WT");
		META_TALK.addAlias("Project_talk");
		// already in the English resource bundle:
		// IMAGE.addAlias("Image");
		// IMAGE_TALK.addAlias("Image_talk");
	}

	@Override
	public NamespaceValue getTalkspace(String namespace) {
		NamespaceValue nsVal = getNamespace(namespace);
		if (nsVal != null) {
			return nsVal.getTalkspace();
		}
		return null;
	}

	@Override
	public NamespaceValue getContentspace(String talkNamespace) {
		NamespaceValue nsVal = getNamespace(talkNamespace);
		if (nsVal != null) {
			return nsVal.getContentspace();
		}
		return null;
	}

	@Override
	public INamespaceValue getMedia() {
		return MEDIA;
	}

	@Override
	public INamespaceValue getSpecial() {
		return SPECIAL;
	}

	@Override
	public INamespaceValue getMain() {
		return MAIN;
	}

	@Override
	public INamespaceValue getTalk() {
		return TALK;
	}

	@Override
	public INamespaceValue getUser() {
		return USER;
	}

	@Override
	public INamespaceValue getUser_talk() {
		return USER_TALK;
	}

	@Override
	public INamespaceValue getMeta() {
		return META;
	}

	@Override
	public INamespaceValue getMeta_talk() {
		return META_TALK;
	}

	@Override
	public INamespaceValue getImage() {
		return IMAGE;
	}

	@Override
	public INamespaceValue getImage_talk() {
		return IMAGE_TALK;
	}

	@Override
	public INamespaceValue getMediaWiki() {
		return MEDIAWIKI;
	}

	@Override
	public INamespaceValue getMediaWiki_talk() {
		return MEDIAWIKI_TALK;
	}

	@Override
	public INamespaceValue getTemplate() {
		return TEMPLATE;
	}

	@Override
	public INamespaceValue getTemplate_talk() {
		return TEMPLATE_TALK;
	}

	@Override
	public INamespaceValue getHelp() {
		return HELP;
	}

	@Override
	public INamespaceValue getHelp_talk() {
		return HELP_TALK;
	}

	@Override
	public INamespaceValue getCategory() {
		return CATEGORY;
	}

	@Override
	public INamespaceValue getCategory_talk() {
		return CATEGORY_TALK;
	}

	@Override
	public INamespaceValue getPortal() {
		return PORTAL;
	}

	@Override
	public INamespaceValue getPortal_talk() {
		return PORTAL_TALK;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] splitNsTitle(String fullTitle) {
		return splitNsTitle(fullTitle, true, ' ', true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] splitNsTitle(String fullTitle, boolean underScoreIsWhitespace,
			char whiteSpaceChar, boolean firstCharacterAsUpperCase) {
		int colonIndex = fullTitle.indexOf(':');
		if (colonIndex != (-1)) {
			String maybeNs = Encoder.normaliseTitle(fullTitle.substring(0, colonIndex),
					underScoreIsWhitespace, whiteSpaceChar, firstCharacterAsUpperCase);
			if (getNamespace(maybeNs) != null) {
				// this is a real namespace
				return new String[] {
						maybeNs,
						Encoder.normaliseTitle(fullTitle.substring(colonIndex + 1),
								underScoreIsWhitespace, whiteSpaceChar,
								firstCharacterAsUpperCase) };
			}
			// else: page belongs to the main namespace and only contains a
			// colon
		}
		return new String[] {
				"",
				Encoder.normaliseTitle(fullTitle, underScoreIsWhitespace,
						whiteSpaceChar, firstCharacterAsUpperCase) };
	}
}

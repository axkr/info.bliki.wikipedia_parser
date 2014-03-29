package info.bliki.wiki.filter;

import info.bliki.wiki.model.Configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assume.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class MediaWikiParserTest extends TestCase {

	protected final static String testFileName = "parserTests.txt";
//	protected final static String testFileName = "parserTests-full.txt";

	protected MediaWikiTestModel wikiModel = null;
	static protected HashMap<String, String> db = new HashMap<String, String>();

	protected final String input;
	protected final String expectedResult;
	protected final Map<String, Object> options;
	protected final Map<String, Object> config;

	private boolean AVOID_PAGE_BREAK_IN_TABLE_before;

	protected static final Pattern COMMAND = Pattern.compile("^!!\\s*(\\w+).*");
	protected static final Pattern TEST_DISABLED = Pattern.compile(".*\\bdisabled\\b.*", Pattern.CASE_INSENSITIVE);
	protected static final Pattern NEWLINE_BLOCK = Pattern.compile("^\n<(div|p|li|td|table|ul|ol|th|tr|dl|pre).*", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	/*
	 * foo
	 * foo=bar
	 * foo="bar baz"
	 * foo=[[bar baz]]
	 * foo=bar,"baz quux"
	 */
	protected static final Pattern OPTION = Pattern.compile(
			"\\b" + 
			"([\\w-]+)" + // Key
			"\\b" +
			"(?:\\s*" +
				"=" + // First sub-value
				"\\s*" +
				"(" +
					"\"[^\"]*\"" + // Quoted val
				"|" +
					"\\[\\[[^]]*\\]\\]" + // Link target
				"|" +
					"[\\w-]+" + // Plain word
				")" +
				"(?:\\s*" +
					"," + // Sub-vals 1..N
					"\\s*" +
					"(" +
						"\"[^\"]*\"" + // Quoted val
					"|" +
						"\\[\\[[^]]*\\]\\]" + // Link target
					"|" +
						"[\\w-]+" + // Plain word
					")" +
				")*" +
			")?", Pattern.COMMENTS);

	public MediaWikiParserTest(String test, String input, String result,
			String options, String configs) {
		super(test);
		this.input = input;
		this.expectedResult = result;
		this.options = parseOptions(options);
		this.config = parseConfig(configs);
	}

	protected static MediaWikiTestModel newWikiTestModel(Locale locale) {
		MediaWikiTestModel wikiModel = new MediaWikiTestModel(locale,
				"/wiki/${image}",
				"/wiki/${title}", db);
		wikiModel.setUp();
		return wikiModel;
	}

    /**
     * Splits the given full title at the first colon.
     * 
     * @param fullTitle
     *            the (full) title including a namespace (if present)
     * 
     * @return a 2-element array with the two components - the first may be
     *         empty if no colon is found
     */
    protected static String[] splitAtColon(String fullTitle) {
        int colonIndex = fullTitle.indexOf(':');
        if (colonIndex != (-1)) {
            return new String[] { fullTitle.substring(0, colonIndex),
                    fullTitle.substring(colonIndex + 1) };
        }
        return new String[] { "", fullTitle };
    }

	/**
	 * Set up a test model, which contains predefined templates
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		AVOID_PAGE_BREAK_IN_TABLE_before = Configuration.AVOID_PAGE_BREAK_IN_TABLE;
		Configuration.AVOID_PAGE_BREAK_IN_TABLE = false;

		String language = (String) options.get("language");
		Locale locale = Locale.ENGLISH;
		if (language != null) {
			// only support languages for which we have a localised Messages file:
			if (language.equals("de")) {
				locale = Locale.GERMAN;
				options.remove("language");
			} else if (language.equals("en")) {
				locale = Locale.ENGLISH;
				options.remove("language");
			} else if (language.equals("es")) {
				locale = new Locale("es");
				options.remove("language");
			} else if (language.equals("fr")) {
				locale = Locale.FRENCH;
				options.remove("language");
			} else if (language.equals("it")) {
				locale = Locale.ITALIAN;
				options.remove("language");
			} else if (language.equals("pt_BR")) {
				locale = new Locale("pt_BR");
				options.remove("language");
			}
		}
		
		wikiModel = newWikiTestModel(locale);
		String title = (String) options.get("title");
		if (title == null) {
			title = "Parser test";
		}
		String[] title0 = splitAtColon(title);
		wikiModel.setNamespaceName(title0[0]);
		wikiModel.setPageName(title0[1]);
		// TODO: use (more) options/config
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		Configuration.AVOID_PAGE_BREAK_IN_TABLE = AVOID_PAGE_BREAK_IN_TABLE_before;
		super.tearDown();
	}

	protected void runTest() throws Throwable {
		if (options.get("disabled") == Boolean.TRUE) {
			return;
		}
		assumeTrue(config.isEmpty());
		
		String title = (String) options.get("title");
		if (title != null) {
			options.remove("title");
			wikiModel.setPageName(title);
		}
		assumeTrue(options.isEmpty());
		
		String actualResult = wikiModel.render(input, true);
		Matcher matcher = NEWLINE_BLOCK.matcher(actualResult);
		if (matcher.matches()) {
			actualResult = actualResult.substring(1);
		}
		assertEquals(expectedResult, actualResult);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(MediaWikiParserTest.class.getName());

		FileInputStream is = null;
		BufferedReader br = null;
		String path = null;
		final URL url = MediaWikiParserTest.class.getResource(testFileName);
		if (url != null) {
			path = url.getFile();
		}
		if (path != null) {
			try {
				is = new FileInputStream(path);
				br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				String line = null;
				int lineNr = 0;
				int lineNrStartTest = 0;

				String section = null;
				Map<String, String> data = new HashMap<String, String>();

				while((line = br.readLine()) != null) {
					++lineNr;
					final Matcher matcher = COMMAND.matcher(line);
					if (matcher.matches()) {
						section = matcher.group(1).toLowerCase();

						if (section.equals("endarticle")) {
							if (!data.containsKey("text")) {
								throw new RuntimeException("'endarticle' without 'text' at line " + lineNr + " of " + testFileName);
							}
							if (!data.containsKey("article")) {
								throw new RuntimeException("'endarticle' without 'article' at line " + lineNr + " of " + testFileName);
							}
							db.put(removeNewlineAtEnd(data.get("article")), data.get("text"));
							data.clear();
							section = null;
							continue;
						} else if (section.equals("endhooks")) {
							if (!data.containsKey("hooks")) {
								throw new RuntimeException("'endhooks' without 'hooks' at line " + lineNr + " of " + testFileName);
							}
							// no support for hooks
							break;
						} else if (section.equals("endfunctionhooks")) {
							if (!data.containsKey("functionhooks")) {
								throw new RuntimeException("'endfunctionhooks' without 'functionhooks' at line " + lineNr + " of " + testFileName);
							}
							// no support for function hooks
							break;
						} else if (section.equals("end")) {
							if (!data.containsKey("test")) {
								throw new RuntimeException("'end' without 'test' at line " + lineNr + " of " + testFileName);
							}
							if (!data.containsKey("input")) {
								throw new RuntimeException("'end' without 'input' at line " + lineNr + " of " + testFileName);
							}
							if (!data.containsKey("result")) {
								throw new RuntimeException("'end' without 'result' at line " + lineNr + " of " + testFileName);
							}

							if (!data.containsKey("options")) {
								data.put("options", "");
							}
							if (!data.containsKey("config")) {
								data.put("config", "");
							}

							if (TEST_DISABLED.matcher(data.get("options")).matches()) {
								// disabled test
								data.clear();
								section = null;
								continue;
							}
							suite.addTest(new MediaWikiParserTest(removeNewlineAtEnd(data
									.get("test")) + " (line: " + lineNrStartTest + ")",
									removeNewlineAtEnd(data.get("input")),
									removeNewlineAtEnd(data.get("result")),
									removeNewlineAtEnd(data.get("options")),
									removeNewlineAtEnd(data.get("config"))));
							data.clear();
							section = null;
							continue;
						} else if (section.equals("test")) {
							lineNrStartTest = lineNr;
						}
						if (data.containsKey(section)) {
							throw new RuntimeException("duplicate section '" + section + "' at line " + lineNr + " of " + testFileName);
						}
						data.put(section, "");
						continue;
					}
					if (section != null) {
						data.put(section, data.get(section) + line + "\n"); 
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return suite;
	}
	
	static protected Map<String, Object> parseConfig(String configs) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		for (String config : configs.split("\n")) {
			if (config.length() != 0) {
				String[] entry = config.split("=", 2);
				if (entry[1].startsWith("'")) {
					result.put(entry[0], entry[1].substring(1, entry[1].length() - 1));
				} else if (entry[1].equalsIgnoreCase("true")) {
					result.put(entry[0], true);
				} else if (entry[1].equalsIgnoreCase("false")) {
					result.put(entry[0], false);
				} else {
					try {
						int value = Integer.parseInt(entry[1]);
						result.put(entry[0], value);
					} catch (NumberFormatException e) {
						System.err.println("unknown data type in config: " + config);
						result.put(entry[0], entry[1]);
					}
				}
			}
		}
		return result;
	}
	
	static protected Map<String, Object> parseOptions(String options) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Matcher matcher = OPTION.matcher(options);
		while (matcher.find()) {
			String key = matcher.group(1).toLowerCase();
			if (matcher.group(2) == null && matcher.group(3) == null) {
				result.put(key, true);
			} else if (matcher.group(3) == null) {
				result.put(key, cleanupOption(matcher.group(2)));
			} else {
				List<String> groupValues = new ArrayList<String>(matcher.groupCount() - 1);
				for (int i = 2; i <= matcher.groupCount(); ++i) {
					String group = matcher.group(i);
					if (group != null) {
						groupValues.add(cleanupOption(group));
					}
				}
				result.put(key, groupValues);
			}
		}
		return result;
	}
	
	static protected String cleanupOption(String option) {
		if (option.startsWith("\"")) {
			return option.substring(1, option.length() - 1);
		}
		if (option.startsWith("[[")) {
			return option.substring(2, option.length() - 2);
		}
		return option;
	}
	
	static protected String removeNewlineAtEnd(String value) {
		if (value.endsWith("\n")) {
			return value.substring(0, value.length() - 1);
		}
		return value;
	}
}

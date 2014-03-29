package info.bliki.wiki.filter;

import junit.framework.Test;
import junit.framework.TestSuite;

public class WPListFilterTest extends FilterTestSupport {
	final public static String LIST0 = "*Mixed list\n" + "*# with numbers\n" + "** and bullets\n" + "*# and numbers\n"
			+ "*bullets again\n" + "**bullet level 2\n" + "***bullet level 3\n" + "***#Number on level 4\n" + "**bullet level 2\n"
			+ "**#Number on level 3\n" + "**#Number [[Level:1|one]]s level 3\n" + "*#number level 2\n" + "*Level 1";

	final public static String LIST1 = "*#*";

	final public static String LIST2 = "# first\n##second";

	final public static String LIST3 = "# test 1\n" + "# test 2\n" + "## test 3\n" + "hello\n" + "## test 4";

	final public static String LIST4 = "# first\n  <!-- stupid comment-->  \n#second";

	final public static String LIST4A = "# first\n<!-- stupid comment-->#second";

	final public static String LIST4B = "# first<!-- stupid comment-->\n#second";

	final public static String LIST4C = "# first\n  <!-- stupid comment-->  \n";

	final public static String LIST_CONTINUATION = "* ''Unordered lists'' are easy to do:\n" + "** Start every line with a star.\n"
			+ "*: Previous item continues.";

	public WPListFilterTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(WPListFilterTest.class);
	}

	public void testWPList01() {
		String testString = "\n*#: a nested list\n";

		WikipediaScanner scanner = new WikipediaScanner(testString, 0);
		scanner.setModel(wikiModel);
		WPList wpList = scanner.wpList();

		assertEquals("*-#-:-5|18|*#:\n", wpList.toString());

		assertEquals("\n" + "\n" + "<ul>\n" + "<li>\n" + "<ol>\n" + "<li>\n" + "<dl>\n"
				+ "<dd>a nested list</dd>\n</dl></li>\n</ol></li>\n</ul>", wikiModel.render(testString, false));

	}

	public void testWPList02() {
		String testString = "\n*#; a nested list 1\n*#: a nested list 2\n";

		WikipediaScanner scanner = new WikipediaScanner(testString, 0);
		scanner.setModel(wikiModel);
		WPList wpList = scanner.wpList();

		assertEquals("*-#-;-5|20|*#;\n" + "25|40|*#:\n", wpList.toString());

		assertEquals("\n" + "\n" + "<ul>\n" + "<li>\n" + "<ol>\n" + "<li>\n" + "<dl>\n" + "<dt>a nested list 1</dt>\n"
				+ "<dd>a nested list 2</dd>\n</dl></li>\n</ol></li>\n</ul>", wikiModel.render(testString, false));

	}

	public void testList0() {
		assertEquals("\n" + "<ul>\n" + "<li>Mixed list\n" + "<ol>\n" + "<li>with numbers</li>\n</ol>\n" + "<ul>\n"
				+ "<li>and bullets</li>\n</ul>\n" + "<ol>\n" + "<li>and numbers</li>\n</ol></li>\n" + "<li>bullets again\n" + "<ul>\n"
				+ "<li>bullet level 2\n" + "<ul>\n" + "<li>bullet level 3\n" + "<ol>\n"
				+ "<li>Number on level 4</li>\n</ol></li>\n</ul></li>\n" + "<li>bullet level 2\n" + "<ol>\n" + "<li>Number on level 3</li>\n"
				+ "<li>Number <a href=\"http://www.bliki.info/wiki/Level:1\" title=\"Level:1\">ones</a> level 3</li>\n</ol></li>\n</ul>\n"
				+ "<ol>\n" + "<li>number level 2</li>\n</ol></li>\n" + "<li>Level 1</li>\n</ul>", wikiModel.render(LIST0, false));
	}

	public void testList1() {
		assertEquals("\n" + "<p>*#*</p>", wikiModel.render(LIST1, false));
	}

	public void testList2() {
		assertEquals("\n" + "<ol>\n" + "<li>first\n" + "<ol>\n" + "<li>second</li>\n</ol></li>\n</ol>", wikiModel.render(LIST2, false));
	}

	public void testList3() {
		assertEquals("\n" + "<ol>\n" + "<li>test 1</li>\n" + "<li>test 2\n" + "<ol>\n" + "<li>test 3</li>\n</ol></li>\n</ol>\n"
				+ "<p>hello\n" + "</p>\n" + "<ol>\n" + "<li>\n" + "<ol>\n" + "<li>test 4</li>\n</ol></li>\n</ol>", wikiModel.render(LIST3, false));
	}

	public void testList4() {
		assertEquals("\n" + "<ol>\n" + "<li>first</li>\n" + "<li>second</li>\n</ol>", wikiModel.render(LIST4, false));
	}

	public void testList4A() {
		assertEquals("\n" + "<ol>\n" + "<li>first</li>\n" + "<li>second</li>\n</ol>", wikiModel.render(LIST4A, false));
	}

	public void testList4B() {
		assertEquals("\n" + "<ol>\n" + "<li>first</li>\n" + "<li>second</li>\n</ol>", wikiModel.render(LIST4B, false));
	}

	public void testList4C() {
		assertEquals("\n" + "<ol>\n" + "<li>first</li>\n</ol>", wikiModel.render(LIST4C, false));
	}

	public void testList10() {
		assertEquals("\n" + "<ul>\n" + "<li>a simple test\n" + "x+y\n" + "</li>\n</ul>\n" + "<p>test test</p>", wikiModel
				.render("*a simple test<nowiki>\n" + "x+y\n" + "</nowiki>\n" + "test test", false));
	}

	public void testList11() {
		assertEquals("\n" + "<ul>\n" + "<li>a simple test blabla</li>\n</ul>\n" + "<p>x+y\n" + "test test</p>", wikiModel
				.render("*a simple test <nowiki>blabla\n" + "x+y\n" + "test test", false));
	}

	public void testList12() {
		assertEquals("\n" + "<ul>\n" + "<li>*</li>\n</ul>", wikiModel.render("* *", false));
		assertEquals("\n" + "<ul>\n" + "<li>#</li>\n</ul>", wikiModel.render("* #", false));
		// TODO solve this wrong JUnit test
		// assertEquals("", wikiModel.render("* :*"));
	}

	public void testList13() {
		assertEquals("\n" + 
				"test 1\n" + 
				"test 2\n" + 
				"test 3\n" + 
				"\n" + 
				"hello\n" + 
				"\n" + 
				"\n" + 
				"test 4\n" + 
				"", wikiModel.render(
				new PlainTextConverter(), LIST3, false));
	}

	public void testList14() {
		assertEquals("\n" + 
				"\n" + 
				"<ul>\n" + 
				"<li>item 1\n" + 
				"<ol>\n" + 
				"<li>item 1.1</li>\n" + 
				"<li>item 1.2</li>\n</ol></li>\n" + 
				"<li>item 2</li>\n</ul>", wikiModel.render("\n" + "*item 1\n" + "*# item 1.1\n" + "*# item 1.2\n" + "* item 2", false));
	} 
	
	public void testListContinuation01() {
		assertEquals("\n" + "<dl>\n" + "<dd><span>simple definition</span></dd>\n</dl>", wikiModel
				.render(": <span>simple definition</span>", false));
	}

	public void testListContinuation02() {
		assertEquals("\n" + "<ul>\n" + "<li><i>Unordered lists</i> are easy to do:\n" + "<ul>\n"
				+ "<li>Start every line with a star.</li>\n</ul>\n" + "<dl>\n" + "<dd>Previous item continues.</dd>\n</dl></li>\n</ul>",
				wikiModel.render(LIST_CONTINUATION, false));
	}

	public void testListContinuation03() {
		assertEquals("\n" + "<ul>\n" + "<li>item 1\n" + "<ul>\n" + "<li>item 1.1</li>\n</ul>\n" + "<dl>\n"
				+ "<dd>continuation I am indented just right</dd>\n</dl></li>\n" + "<li>item 1.2</li>\n" + "<li>item 2\n" + "<dl>\n"
				+ "<dd>continuation I am indented too much</dd>\n</dl>\n" + "<ul>\n" + "<li>item 2.1\n" + "<ul>\n"
				+ "<li>item 2.1.1</li>\n</ul></li>\n</ul>\n" + "<dl>\n" + "<dd>continuation I am indented too little</dd>\n</dl></li>\n</ul>",
				wikiModel.render("* item 1\n" + "** item 1.1\n" + "*:continuation I am indented just right\n" + "*item 1.2\n" + "*item 2\n"
						+ "*:continuation I am indented too much\n" + "**item 2.1\n" + "***item 2.1.1\n"
						+ "*:continuation I am indented too little", false));
	}

	public void testListContinuation04() {
		assertEquals("\n" + "\n" + "<dl>\n" + "<dt>definition list 1</dt>\n" + "<dt>definition list 2</dt>\n"
				+ "<dd>definition list 3</dd>\n" + "<dd>definition list 4</dd>\n</dl>", wikiModel.render("\n" + "; definition list 1\n"
				+ "; definition list 2\n" + ": definition list 3\n" + ": definition list 4", false));
	}

	public void testListContinuation05() {
		assertEquals("\n" + "<dl>\n" + "<dt>definition lists</dt>\n" + "<dd>can be \n" + "<dl>\n" + "<dt>nested </dt>\n"
				+ "<dd>too</dd>\n</dl></dd>\n</dl>", wikiModel.render("; definition lists\n" + ": can be \n" + ":; nested : too", false));
	}

	public void testListContinuation06() {
		assertEquals("\n" + "<ul>\n" + "<li>You can even do mixed lists\n" + "<ol>\n" + "<li>and nest them</li>\n"
				+ "<li>inside each other\n" + "<ul>\n" + "<li>or break lines<br />in lists.</li>\n</ul>\n" + "<dl>\n"
				+ "<dt>definition lists</dt>\n" + "<dd>can be \n" + "<dl>\n" + "<dt>nested </dt>\n"
				+ "<dd>too</dd>\n</dl></dd>\n</dl></li>\n</ol></li>\n</ul>", wikiModel.render("* You can even do mixed lists\n"
				+ "*# and nest them\n" + "*# inside each other\n" + "*#* or break lines<br>in lists.\n" + "*#; definition lists\n"
				+ "*#: can be \n" + "*#:; nested : too", false));
	}

}
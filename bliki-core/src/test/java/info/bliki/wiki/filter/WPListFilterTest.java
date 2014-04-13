package info.bliki.wiki.filter;

import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

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


    @Test public void testWPList01() {
        String testString = "\n*#: a nested list\n";

        WikipediaScanner scanner = new WikipediaScanner(testString, 0);
        scanner.setModel(wikiModel);
        WPList wpList = scanner.wpList();

        assertThat(wpList.toString()).isEqualTo("*-#-:-5|18|*#:\n");

        assertThat(wikiModel.render(testString, false)).isEqualTo("\n" + "\n" + "<ul>\n" + "<li>\n" + "<ol>\n" + "<li>\n" + "<dl>\n"
                + "<dd>a nested list</dd>\n</dl></li>\n</ol></li>\n</ul>");

    }

    @Test public void testWPList02() {
        String testString = "\n*#; a nested list 1\n*#: a nested list 2\n";

        WikipediaScanner scanner = new WikipediaScanner(testString, 0);
        scanner.setModel(wikiModel);
        WPList wpList = scanner.wpList();

        assertThat(wpList.toString()).isEqualTo("*-#-;-5|20|*#;\n" + "25|40|*#:\n");

        assertThat(wikiModel.render(testString, false)).isEqualTo("\n" + "\n" + "<ul>\n" + "<li>\n" + "<ol>\n" + "<li>\n" + "<dl>\n" + "<dt>a nested list 1</dt>\n"
                + "<dd>a nested list 2</dd>\n</dl></li>\n</ol></li>\n</ul>");

    }

    @Test public void testList0() {
        assertThat(wikiModel.render(LIST0, false)).isEqualTo("\n" + "<ul>\n" + "<li>Mixed list\n" + "<ol>\n" + "<li>with numbers</li>\n</ol>\n" + "<ul>\n"
                + "<li>and bullets</li>\n</ul>\n" + "<ol>\n" + "<li>and numbers</li>\n</ol></li>\n" + "<li>bullets again\n" + "<ul>\n"
                + "<li>bullet level 2\n" + "<ul>\n" + "<li>bullet level 3\n" + "<ol>\n"
                + "<li>Number on level 4</li>\n</ol></li>\n</ul></li>\n" + "<li>bullet level 2\n" + "<ol>\n" + "<li>Number on level 3</li>\n"
                + "<li>Number <a href=\"http://www.bliki.info/wiki/Level:1\" title=\"Level:1\">ones</a> level 3</li>\n</ol></li>\n</ul>\n"
                + "<ol>\n" + "<li>number level 2</li>\n</ol></li>\n" + "<li>Level 1</li>\n</ul>");
    }

    @Test public void testList1() {
        assertThat(wikiModel.render(LIST1, false)).isEqualTo("\n" + "<p>*#*</p>");
    }

    @Test public void testList2() {
        assertThat(wikiModel.render(LIST2, false)).isEqualTo("\n" + "<ol>\n" + "<li>first\n" + "<ol>\n" + "<li>second</li>\n</ol></li>\n</ol>");
    }

    @Test public void testList3() {
        assertThat(wikiModel.render(LIST3, false)).isEqualTo("\n" + "<ol>\n" + "<li>test 1</li>\n" + "<li>test 2\n" + "<ol>\n" + "<li>test 3</li>\n</ol></li>\n</ol>\n"
                + "<p>hello\n" + "</p>\n" + "<ol>\n" + "<li>\n" + "<ol>\n" + "<li>test 4</li>\n</ol></li>\n</ol>");
    }

    @Test public void testList4() {
        assertThat(wikiModel.render(LIST4, false)).isEqualTo("\n" + "<ol>\n" + "<li>first</li>\n" + "<li>second</li>\n</ol>");
    }

    @Test public void testList4A() {
        assertThat(wikiModel.render(LIST4A, false)).isEqualTo("\n" + "<ol>\n" + "<li>first</li>\n" + "<li>second</li>\n</ol>");
    }

    @Test public void testList4B() {
        assertThat(wikiModel.render(LIST4B, false)).isEqualTo("\n" + "<ol>\n" + "<li>first</li>\n" + "<li>second</li>\n</ol>");
    }

    @Test public void testList4C() {
        assertThat(wikiModel.render(LIST4C, false)).isEqualTo("\n" + "<ol>\n" + "<li>first</li>\n</ol>");
    }

    @Test public void testList10() {
        assertThat(wikiModel
                .render("*a simple test<nowiki>\n" + "x+y\n" + "</nowiki>\n" + "test test", false)).isEqualTo("\n" + "<ul>\n" + "<li>a simple test\n" + "x+y\n" + "</li>\n</ul>\n" + "<p>test test</p>");
    }

    @Test public void testList11() {
        assertThat(wikiModel
                .render("*a simple test <nowiki>blabla\n" + "x+y\n" + "test test", false)).isEqualTo("\n" + "<ul>\n" + "<li>a simple test blabla</li>\n</ul>\n" + "<p>x+y\n" + "test test</p>");
    }

    @Test public void testList12() {
        assertThat(wikiModel.render("* *", false)).isEqualTo("\n" + "<ul>\n" + "<li>*</li>\n</ul>");
        assertThat(wikiModel.render("* #", false)).isEqualTo("\n" + "<ul>\n" + "<li>#</li>\n</ul>");
        // TODO solve this wrong JUnit test
        // assertEquals("", wikiModel.render("* :*"));
    }

    @Test public void testList13() {
        assertThat(wikiModel.render(
                new PlainTextConverter(), LIST3, false)).isEqualTo("\n" +
                "test 1\n" +
                "test 2\n" +
                "test 3\n" +
                "\n" +
                "hello\n" +
                "\n" +
                "\n" +
                "test 4\n" +
                "");
    }

    @Test public void testList14() {
        assertThat(wikiModel.render("\n" + "*item 1\n" + "*# item 1.1\n" + "*# item 1.2\n" + "* item 2", false)).isEqualTo("\n" +
                "\n" +
                "<ul>\n" +
                "<li>item 1\n" +
                "<ol>\n" +
                "<li>item 1.1</li>\n" +
                "<li>item 1.2</li>\n</ol></li>\n" +
                "<li>item 2</li>\n</ul>");
    }

    @Test public void testListContinuation01() {
        assertThat(wikiModel
                .render(": <span>simple definition</span>", false)).isEqualTo("\n" + "<dl>\n" + "<dd><span>simple definition</span></dd>\n</dl>");
    }

    @Test public void testListContinuation02() {
        assertThat(wikiModel.render(LIST_CONTINUATION, false)).isEqualTo("\n" + "<ul>\n" + "<li><i>Unordered lists</i> are easy to do:\n" + "<ul>\n"
                + "<li>Start every line with a star.</li>\n</ul>\n" + "<dl>\n" + "<dd>Previous item continues.</dd>\n</dl></li>\n</ul>");
    }

    @Test public void testListContinuation03() {
        assertThat(wikiModel.render("* item 1\n" + "** item 1.1\n" + "*:continuation I am indented just right\n" + "*item 1.2\n" + "*item 2\n"
                + "*:continuation I am indented too much\n" + "**item 2.1\n" + "***item 2.1.1\n"
                + "*:continuation I am indented too little", false)).isEqualTo("\n" + "<ul>\n" + "<li>item 1\n" + "<ul>\n" + "<li>item 1.1</li>\n</ul>\n" + "<dl>\n"
                + "<dd>continuation I am indented just right</dd>\n</dl></li>\n" + "<li>item 1.2</li>\n" + "<li>item 2\n" + "<dl>\n"
                + "<dd>continuation I am indented too much</dd>\n</dl>\n" + "<ul>\n" + "<li>item 2.1\n" + "<ul>\n"
                + "<li>item 2.1.1</li>\n</ul></li>\n</ul>\n" + "<dl>\n" + "<dd>continuation I am indented too little</dd>\n</dl></li>\n</ul>");
    }

    @Test public void testListContinuation04() {
        assertThat(wikiModel.render("\n" + "; definition list 1\n"
                + "; definition list 2\n" + ": definition list 3\n" + ": definition list 4", false)).isEqualTo("\n" + "\n" + "<dl>\n" + "<dt>definition list 1</dt>\n" + "<dt>definition list 2</dt>\n"
                + "<dd>definition list 3</dd>\n" + "<dd>definition list 4</dd>\n</dl>");
    }

    @Test public void testListContinuation05() {
        assertThat(wikiModel.render("; definition lists\n" + ": can be \n" + ":; nested : too", false)).isEqualTo("\n" + "<dl>\n" + "<dt>definition lists</dt>\n" + "<dd>can be \n" + "<dl>\n" + "<dt>nested </dt>\n"
                + "<dd>too</dd>\n</dl></dd>\n</dl>");
    }

    @Test public void testListContinuation06() {
        assertThat(wikiModel.render("* You can even do mixed lists\n"
                + "*# and nest them\n" + "*# inside each other\n" + "*#* or break lines<br>in lists.\n" + "*#; definition lists\n"
                + "*#: can be \n" + "*#:; nested : too", false)).isEqualTo("\n" + "<ul>\n" + "<li>You can even do mixed lists\n" + "<ol>\n" + "<li>and nest them</li>\n"
                + "<li>inside each other\n" + "<ul>\n" + "<li>or break lines<br />in lists.</li>\n</ul>\n" + "<dl>\n"
                + "<dt>definition lists</dt>\n" + "<dd>can be \n" + "<dl>\n" + "<dt>nested </dt>\n"
                + "<dd>too</dd>\n</dl></dd>\n</dl></li>\n</ol></li>\n</ul>");
    }

}

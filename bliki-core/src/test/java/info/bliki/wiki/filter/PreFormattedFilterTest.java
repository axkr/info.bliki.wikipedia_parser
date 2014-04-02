package info.bliki.wiki.filter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
public class PreFormattedFilterTest extends FilterTestSupport
{
    @Test public void testPreformattedInput1()
    {
        assertEquals("\n" +
                "<pre>This is some\n" +
                "Preformatted text\n" +
                "With <i>italic</i>\n" +
                "And <b>bold</b>\n" +
                "And a <a href=\"http://www.bliki.info/wiki/Main_Page\" title=\"Main Page\">link</a>\n</pre>", wikiModel.render(
                " This is some\n" + " Preformatted text\n" + " With \'\'italic\'\'\n" + " And \'\'\'bold\'\'\'\n"
                        + " And a [[Main Page|link]]", false));
    }

}

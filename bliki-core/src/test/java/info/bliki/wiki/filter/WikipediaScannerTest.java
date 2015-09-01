package info.bliki.wiki.filter;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WikipediaScannerTest {

    @Test public void testFindNestedTemplateEndSimpleTemplate() throws Exception {
        String input = "foo {{template|parameter|another}}123";
        assertThat(findEnd(input, 6)).isEqualTo(input.length()-3);
    }

    @Test public void testFindNestedTemplateEndNoEndReturnsMinusOne() throws Exception {
        String input = "foo {{template|parameter|another";
        assertThat(findEnd(input, 6)).isEqualTo(-1);
    }

    @Test public void testFindNestedTemplateEndWithNestedTemplate() throws Exception {
        String input = "foo {{template|{{nested}}|another}}123";
        assertThat(findEnd(input, 6)).isEqualTo(input.length() - 3);
    }

    @Test public void testFindNestedTemplateEndWithCurlyBracesAsParameters() throws Exception {
        String input = "foo {{template|{|another}}123";
        assertThat(findEnd(input, 6)).isEqualTo(input.length()-3);
    }

    @Test public void testFindNestedTemplateEndWithCurlyBracesAsParameters2() throws Exception {
        String input = "foo {{template|}|another}}123";
        assertThat(findEnd(input, 6)).isEqualTo(input.length()-3);
    }

    @Test public void testFindNestedTemplateEndWithTripleCurlyBracesAsParameters() throws Exception {
        String input = "foo {{template|{{{1}}}|another}}123";
        assertThat(findEnd(input, 6)).isEqualTo(input.length()-3);
    }

    @Test public void testUrlEncode() throws Exception {
        String input = "{{urlencode: \"#$%&'()*,;?[]^`{}}";
        assertThat(findEnd(input, 2)).isEqualTo(input.length());
    }

    @Test public void testWithUnsupportedTemplate() throws Exception {
        String input = "#switch: } \n" +
                " |#|number sign|hash|hash sign|octothorpe|pound sign = [[Unsupported titles/Number sign|{{#if:  | {{{2}}} | #}}]]\n" +
                "\n" +
                " |.|period|full stop = [[Unsupported titles/Full stop|{{#if:  | {{{2}}} | .}}]]\n" +
                "\n" +
                " |..|period period|full stop full stop|periodperiod|double period=[[Unsupported titles/Double period|{{#if:  | {{{2}}} | ..}}]]\n" +
                "\n" +
                " |:| colon = [[Unsupported titles/Colon|{{#if:  | {{{2}}} | :}}]]\n" +
                "\n" +
                " |::|double colon|colon colon|coloncolon=[[Unsupported titles/Double colon|{{#if:  | {{{2}}} | ::}}]]\n" +
                "\n" +
                " |:(|colon left paren|colon left parenthesis=[[Unsupported titles/Colon left paren|{{#if:  | {{{2}}} | :(}}]]\n" +
                "\n" +
                " |:)|colon right paren|colon right parenthesis=[[Unsupported titles/Colon right paren|{{#if:  | {{{2}}} | :)}}]]\n" +
                "\n" +
                " |:-(|colon hyphen left paren|colon hyphen left parenthesis = [[Unsupported titles/Colon hyphen left paren|{{#if:  | {{{2}}} | :-(}}]]\n" +
                "\n" +
                " |:-)|colon hyphen right paren|colon hyphen right parenthesis=[[Unsupported titles/Colon hyphen right paren|{{#if:  | {{{2}}} | :-)}}]]\n" +
                "\n" +
                " |:{{=}}|colon equals = [[Unsupported titles/Colon equals|{{#if:  | {{{2}}} | :&#61;}}]]\n" +
                " |<3|less than three|less than 3|less than sign 3|less than sign three=[[Unsupported titles/Less than three|{{#if:  | {{{2}}} | <3}}]]\n" +
                "\n" +
                " |<>|less than greater than|less than sign greater than sign=[[Unsupported titles/Less than greater than|{{#if:  | {{{2}}} | <>}}]]\n" +
                "\n" +
                " |<·|less than middle dot|less than center dot|less than cdot=[[Unsupported titles/Less than middle dot|{{#if:  | {{{2}}} | <·}}]]\n" +
                "\n" +
                " | < | less than sign|less than = [[Unsupported titles/Less than sign|{{#if:  | {{{2}}} | <}}]]\n" +
                "\n" +
                " | > | greater than sign|greater than = [[Unsupported titles/Greater than sign|{{#if:  | {{{2}}} | >}}]]\n" +
                "\n" +
                " |C#| C sharp = [[Unsupported titles/C sharp|{{#if:  | {{{2}}} | C#}}]]\n" +
                "\n" +
                " | [ | left square bracket = [[Unsupported titles/Left square bracket|{{#if:  | {{{2}}} | [}}]]\n" +
                "\n" +
                " | ] | right square bracket = [[Unsupported titles/Right square bracket|{{#if:  | {{{2}}} | &#93;}}]]\n" +
                "\n" +
                " | [] | [ ] | square brackets = [[Unsupported titles/Square brackets|{{#if:  | {{{2}}} | [ ]}}]]\n" +
                "\n" +
                " | […] | [ … ] | [...] | [ ... ] | square bracketed ellipsis = [[Unsupported titles/Square bracketed ellipsis|{{#if:  | {{{2}}} | […]}}]]\n" +
                "\n" +
                " | [sic] | square bracketed sic = [[Unsupported titles/Square bracketed sic|{{#if:  | {{{2}}} | [sic]}}]]\n" +
                "\n" +
                " | _ | underscore | low line = [[Unsupported titles/Low line|{{#if:  | {{{2}}} | _}}]]\n" +
                "\n" +
                " | _ _ | underscore space underscore | low line space low line = [[Unsupported titles/Low line space low line |{{#if:  | {{{2}}} | _ _}}]]\n" +
                "\n" +
                " | { | left curly bracket = [[Unsupported titles/Left curly bracket|{{#if:  | {{{2}}} | {}}]]\n" +
                "\n" +
                " | } | right curly bracket = [[Unsupported titles/Right curly bracket|{{#if:  | {{{2}}} | &#125;}}]]\n" +
                "\n" +
                " | {} | { } | curly brackets = [[Unsupported titles/Curly brackets|{{#if:  | {{{2}}} | &#123; &#125;}}]]\n" +
                "\n" +
                " | {{!}} | vertical line|pipe = [[Unsupported titles/Vertical line|{{#if:  | {{{2}}} | &#124;}}]]\n" +
                "\n" +
                " | &#x20; | space = [[Unsupported titles/Space|{{#if:  | {{{2}}} | &nbsp;}}]]\n" +
                "\n" +
                " |   | Ogham space = [[Unsupported titles/Ogham space|{{#if:  | {{{2}}} |  }}]]\n" +
                "\n" +
                " | tab = {{#if:  | {{{2}}} | &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;}}\n" +
                "\n" +
                " | ideographic space | fullwidth space = [[Unsupported titles/Ideographic space|{{#if:  | {{{2}}} | 　}}]]\n" +
                "\n" +
                " | Ancient Greek dish = [[Unsupported titles/Ancient Greek dish|λοπαδοτεμαχοσελαχογαλεοκρανιολειψανοδριμυποτριμματοσιλφιοκαραβομελιτοκατακεχυμενοκιχλεπικοσσυφοφαττοπεριστεραλεκτρυονοπτοκεφαλλιοκιγκλοπελειολαγῳοσιραιοβαφητραγανοπτερύγων]]\n" +
                "\n" +
                " | Thai name of Bangkok = [[Unsupported titles/Thai name of Bangkok|กรุงเทพมหานคร อมรรัตนโกสินทร์ มหินทรายุธยามหาดิลกภพ นพรัตน์ราชธานี บุรีรมย์อุดมราชนิเวศน์มหาสถาน อมรพิมานอวตารสถิต สักกะทัตติยะวิษณุกรรมประสิทธิ์]]\n" +
                "\n" +
                " | German law, short name = [[Appendix:List of protologisms/Long words/Rindfleischetikettierungsüberwachungsaufgabenübertragungsgesetz|Rindfleischetikettierungsüberwachungsaufgabenübertragungsgesetz]]\n" +
                "\n" +
                " | German law, long name = [[Appendix:List of protologisms/Long words/Rinderkennzeichnungs- und Rindfleischetikettierungsüberwachungsaufgabenübertragungsgesetz|Rinderkennzeichnungs- und Rindfleischetikettierungsüberwachungsaufgabenübertragungsgesetz]]\n" +
                "\n" +
                " | English formula of titin = [[Appendix:List of protologisms/Long words/Titin|methionyl...isoleucine]]\n" +
                "\n" +
                " | German formula of titin = [[Appendix:List of protologisms/Long words/Titin/German|Methionyl...isoleucin]]\n" +
                "\n" +
                " | / |solidus|slash|shilling= {{#switch:|=[[:/|{{#if:|{{{2}}}|/}}]]}}\n" +
                "\n" +
                " | Replacement character= [[Unsupported titles/Replacement character|&#65533;]]\n" +
                "\n" +
                " | \u007F = {{#switch:|=[[Control characters|{{#if:|{{{2}}}|DELETE}}]]|name=DELETE|appendix=Control characters}}\n" +
                "\n" +
                " |= [[Appendix:Unsupported titles]]\n" +
                " | {{exists|page=Unsupported titles/} |then=[[Unsupported titles/} |{{#if:  | {{{2}}} | } }}]]|else=[[Appendix:Unsupported titles]]}}}}";

        assertThat(findEnd(input, 0)).isEqualTo(input.length());
    }

    private int findEnd(String input, int offset) {
        char[] inputChar = new char[input.length()];
        input.getChars(0, input.length(), inputChar, 0);
        return WikipediaScanner.findNestedTemplateEnd(inputChar, offset);
    }
}

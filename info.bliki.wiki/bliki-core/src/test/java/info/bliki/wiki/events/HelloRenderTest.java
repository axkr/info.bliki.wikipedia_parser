package info.bliki.wiki.events;

import info.bliki.wiki.model.WikiModel;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class HelloRenderTest extends TestCase {

	public final static String HELLO_TEXT = "<noinclude>{{pp-semi-protected|small=yes}}</noinclude>\r\n"
			+ "{{wiktionarypar|hello}}\r\n"
			+ "{{otheruses|Hello (disambiguation)}}\r\n"
			+ "\'\'\'Hello\'\'\' is a [[salutation (greeting)|salutation]] or [[Greeting habits|greeting]] in the [[English language]] and is [[synonym]]ous with other greetings such as \'\'[[wikt:hi|Hi]]\'\' or \'\'[[wikt:hey|Hey]]\'\'. \'\'Hello\'\' was recorded in dictionaries in [[1883]]. <ref name=\"etym\">\r\n"
			+ "{{cite web|url=http://www.etymonline.com/index.php?search=hello&searchmode=none|title=Online Etymology Dictionary}}</ref>\r\n"
			+ "\r\n"
			+ "==First use==\r\n"
			+ "Many stories date the first use of \'\'hello\'\' (with that spelling) to around the time of the invention of the [[telephone]] in 1876. \r\n"
			+ "It was, however, used in print in \'\'[[Roughing It]]\'\' by [[Mark Twain]] in 1872 (written between 1870 and 1871), <ref>{{cite web|url=http://etext.lib.virginia.edu/railton/roughingit/rihp.html|title=Roughing It|publisher=UVa Library}}</ref> so its first use must have predated the telephone:\r\n"
			+ "\r\n"
			+ "\"A miner came out and said: \'Hello!\"\r\n"
			+ "\r\n"
			+ "Earlier uses can be found back to [[1849]] <ref>\r\n"
			+ "{{cite book |last= Foster |first= George G |title= New York in Slices |url= http://name.umdl.umich.edu/AJA2254.0001.001 |accessdate= 2006-08-15 |year= 1849  |publisher= W. F. Burgess|location= New York |pages= [http://www.hti.umich.edu/cgi/t/text/pageviewer-idx?c=moa;cc=moa;g=moagrp;xc=1;q1=hello;rgn=full%20text;idno=aja2254.0001.001;didno=aja2254.0001.001;view=image;seq=0122 p120] }}</ref> and [[1846]]<ref>{{cite web|url=http://books.google.com/|title=Google books}}</ref>:\r\n"
			+ "\r\n"
			+ "\"We meet the boys here, and it is \"Hello, George,\" or \"Hello, Jim.\" We slap the judge of the supreme court on the back with a \"Hello, Joe, how are you?\"<ref>\r\n"
			+ "{{cite book |last= Lester |first= Charles Edwards |title= The Artists of America: A Series of Biographical Sketches of American Artists with Portraits and Designs on Steel |url= http://books.google.com/books?id=IiUEAAAAYAAJ |accessdate= 2007-10-18 |year= 1846  |publisher= Baker & Scribner |location= New York |pages= [http://books.google.com/books?id=IiUEAAAAYAAJ&pg=RA2-PA62&dq=hello+date:0-1876&as_brr=0#PRA2-PA62,M1 p62] }}</ref>\r\n"
			+ "\r\n"
			+ "It was listed in dictionaries by 1883. <ref name=\"etym\">\r\n"
			+ "{{cite web|url=http://www.etymonline.com/index.php?search=hello&searchmode=none|title=Online Etymology Dictionary}}</ref>\r\n"
			+ "\r\n"
			+ "The word was extensively used in literature by the [[1860s]]. <ref>{{cite web|url=http://books.google.com/|title=Google books}}</ref> Two early uses of \'\'hello\'\' can be found as far back as [[1826]]. <ref>{{cite web|url=http://books.google.com/|title=Google books}}</ref>\r\n"
			+ "\r\n"
			+ "*\'\'Report on the trade in foreign corn, and on the agriculture of the north of Europe.\'\' by William Jacob, 1826. page 213\r\n"
			+ "\"On this occasion she switched it on to a patient who was awake and who merely said \'Hello Sister, what\'s the matter with you...\'\"\r\n"
			+ "\r\n"
			+ "*\'\'The Every-day Book: Or Everlasting Calendar of Popular Amusements, Sports, Pastime, Ceremonies,...\'\'By William Hone, 1826 Page 1370\r\n"
			+ "\"Then hello boys! Hello boys! Shout and huzz....\"\r\n"
			+ "\r\n"
			+ "==Etymology==\r\n"
			+ "There are many different theories to the origins of the word. It might be a [[contraction]] of [[Archaism|archaic]] [[English language|English]] \"\'\'whole be thou\'\'\". <ref>{{cite book |author=Bryson, Bill|title=Mother Tongue: English & How It Got That Way |url=http://www.ralphmag.org/mothertongue.html}}</ref> Another source has been suggested to be the phrase \"\'\'Hail, Thou\'\'\", as used in some translations of the \'\'Bible\'\' (see \'\'Luke 1:28\'\' and \'\'Matthew 27:14\'\' for examples). {{Fact|date=September 2007}} <!-- The fact reference needed is who suggested the \"Hail, Thou\" theory. -->\r\n"
			+ "\r\n"
			+ "===Telephone===\r\n"
			+ "The word \'\'hello\'\' has also been credited to [[Thomas Edison]], specifically as a way to greet someone when answering the [[telephone]]; according to one source, he expressed his surprise with a misheard \'\'Hullo\'\'. <ref>{{cite web|url=http://www.collectorcafe.com/article_archive.asp?article=800&id=1507|title=The First “Hello!”: Thomas Edison, the Phonograph and the Telephone – Part 2|author=Allen Koenigsberg|publisher=Antique Phonograph Magazine, Vol.VIII No.6|accessdate=2006-09-13}}</ref> [[Alexander Graham Bell]] initially used \'\'[[Ahoy-hoy|Ahoy]]\'\' (as used on ships) as a telephone greeting. <ref>{{cite web|url=http://www2.cs.uh.edu/~klong/papers/hello.txt|title=All Things Considered|author=Allen Koenigsberg|publisher=National Public Radio|accessdate=2006-09-13|date=1999}}</ref> However, in 1877, Edison wrote to T.B.A. David, the president of the Central District and Printing Telegraph Company of Pittsburg:\r\n"
			+ "\r\n"
			+ "\"Friend David, I do not think we shall need a call bell as Hello! can be heard 10 to 20 feet away.\r\n"
			+ "What you think? Edison - P.S. first cost of sender & receiver to manufacture is only $7.00.\"\r\n"
			+ "\r\n"
			+ "By [[1889]], central telephone exchange operators were known as \'hello-girls\' due to the association between the greeting and the telephone.<ref name=\"etym\" />\r\n"
			+ "\r\n"
			+ "The term \"hello\" is almost exclusively used when answering a phone call as of 2007.{{cn|date=October 2007}} The similar terms \"hi\" or \"hey\" are seldom used, unless the recipient has [[Caller ID]] and knows it is their close friend calling.{{cn|date=October 2007}}\r\n"
			+ "\r\n"
			+ "===Hullo===\r\n"
			+ "\'\'Hello\'\' may also be derived from \'\'Hullo\'\'. \'\'Hullo\'\' was in use before \'\'hello\'\' and was used as a greeting and also an expression of surprise. [[Charles Dickens]] uses it in Chapter 8 of \'\'[[Oliver Twist]]\'\' in [[1838]] when Oliver meets [[the Artful Dodger]]:\r\n"
			+ "\r\n"
			+ "\"Upon this, the boy crossed over; and walking close up to Oliver, said \'Hullo, my covey! What\'s the row?\'\"\r\n"
			+ "\r\n"
			+ "It was in use in both senses by the time \'\'[[Tom Brown\'s Schooldays]]\'\' was published in 1857 (although the book was set in the 1830s so it may have been in use by then):\r\n"
			+ "*\"\'Hullo though,\' says East, pulling up, and taking another look at Tom; \'this\'ll never do...\'\"\r\n"
			+ "*\"Hullo, Brown! where do you come from?\"\r\n"
			+ "Although much less common than it used to be, the word \'\'hullo\'\' is still in use, mainly in [[British English]].\r\n"
			+ "\r\n"
			+ "===Hallo===\r\n"
			+ "\'\'Hello\'\' is alternatively thought to come from the word \'\'hallo\'\' (1840) via \'\'hollo\'\' (also \'\'holla\'\', \'\'holloa\'\', \'\'halloo\'\', \'\'halloa\'\'). <ref name=\"MW\">{{cite web|url=http://www.m-w.com/dictionary/hello|title=Hello|publisher=Merriam-Webster Online}}</ref> The definition of \'\'hollo\'\' is to shout or an [[exclamation]] originally shouted in a [[fox hunt|hunt]] when the quarry was spotted: <ref name=\"MW\" />\r\n"
			+ "\r\n"
			+ "\"If I fly, Marcius,/Halloo me like a hare.\" - \'\'[[Coriolanus (play)|Coriolanus]]\'\' (I.viii.7), [[William Shakespeare]]\r\n"
			+ "\r\n"
			+ "[[Webster\'s dictionary]] from [[1913]] traces the etymology of \'\'holloa\'\' to the Old English \'\'halow\'\' and suggests: \"Perhaps from ah + lo; compare Anglo Saxon ealā.\"\r\n"
			+ "\r\n"
			+ "According to the \'\'[[American Heritage Dictionary]]\'\', \'\'hallo\'\' is a modification of the obsolete \'\'holla\'\' (\'\'stop!\'\'), perhaps from Old French \'\'hola\'\' (\'\'ho\'\', ho! + \'\'la\'\', there, from Latin \'\'illac\'\', that way). <ref>{{cite web|url=http://www.bartelby.com/61/60/H0136000.html|title=Hello|publisher= The American Heritage® Dictionary of the English Language: Fourth Edition.|date=2000|accessdate=2006-09-01}}</ref>\r\n"
			+ "\r\n"
			+ "==External links==\r\n"
			+ "*[http://www.elite.net/~runner/jennifers/hello.htm Hello in more than 800 languages]\r\n"
			+ "\r\n"
			+ "==References==\r\n"
			+ "<!--<nowiki>\r\n"
			+ "  See http://en.wikipedia.org/wiki/Wikipedia:Footnotes for an explanation of how\r\n"
			+ "  to generate footnotes using the <ref> and </ref> tags, and the template below \r\n"
			+ "</nowiki>-->\r\n"
			+ "{{reflist|2}}\r\n"
			+ ":*[http://dictionary.oed.com/cgi/entry/50107205?query_type=word&queryword=hollo&first=1&max_to_show=10&sort_type=alpha&result_place=1&search_id=MJDi-YER2WO-12323&hilite=50107205 OED online entry for \'\'hollo\'\'] (Subscription)\r\n"
			+ ":*[http://www.m-w.com/dictionary/ Merriam-Webster Dictionary: hullo]\r\n" + "</div>\r\n" + "\r\n"
			+ "[[Category:Greetings]]\r\n" + "\r\n" + "[[de:Hallo]]\r\n" + "[[es:Hola]]\r\n" + "[[fr:Bonjour]]\r\n" + "[[it:Ciao]]\r\n"
			+ "[[nl:Hoi]]\r\n" + "[[nn:Hallo]]\r\n" + "[[pt:Oi]]\r\n" + "[[ru:Алло]]\r\n" + "[[simple:Hello]]\r\n"
			+ "[[tr:Günaydin]]\r\n" + "[[vec:Ciao]]\r\n" + "[[zh:Hello]]\r\n" + "";

	protected WikiModel wikiModel = null;

	public HelloRenderTest(String s) {
		super(s);
	}

	/**
	 * Set up a test model, which contains predefined templates
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		wikiModel = new HelloWikiModel("http://www.bliki.info/wiki/${image}", "http://www.bliki.info/wiki/${title}");
	}

	public static Test suite() {
		return new TestSuite(HelloRenderTest.class);
	}

	public void test996() {
		String result = wikiModel.render(HELLO_TEXT, false);
	}

	public void test997() {
		HelloWikiLinkListener listener = new HelloWikiLinkListener();
		wikiModel.parseEvents(listener, HELLO_TEXT);
		assertEquals(listener.getCollectorBuffer().toString(), "salutation (greeting)|salutation\n" + "Greeting habits|greeting\n"
				+ "English language\n" + "synonym\n" + "wikt:hi|Hi\n" + "wikt:hey|Hey\n" + "1883\n" + "telephone\n" + "Roughing It\n"
				+ "Mark Twain\n" + "1849\n" + "1846\n" + "1860s\n" + "1826\n" + "contraction\n" + "Archaism|archaic\n"
				+ "English language|English\n" + "Thomas Edison\n" + "telephone\n" + "Alexander Graham Bell\n" + "Ahoy-hoy|Ahoy\n"
				+ "1889\n" + "Caller ID\n" + "Charles Dickens\n" + "Oliver Twist\n" + "1838\n" + "the Artful Dodger\n"
				+ "Tom Brown\'s Schooldays\n" + "British English\n" + "exclamation\n" + "fox hunt|hunt\n"
				+ "Coriolanus (play)|Coriolanus\n" + "William Shakespeare\n" + "Webster\'s dictionary\n" + "1913\n"
				+ "American Heritage Dictionary\n" + "Category:Greetings\n" + "de:Hallo\n" + "es:Hola\n" + "fr:Bonjour\n" + "it:Ciao\n"
				+ "nl:Hoi\n" + "nn:Hallo\n" + "pt:Oi\n" + "ru:Алло\n" + "simple:Hello\n" + "tr:Günaydin\n" + "vec:Ciao\n" + "zh:Hello\n"
				+ "");
	}

	public void test998() {
		HelloHeadListener listener = new HelloHeadListener();
		wikiModel.parseEvents(listener, HELLO_TEXT);
		assertEquals(listener.getCollectorBuffer().toString(), "First use\n" + "Etymology\n" + "Telephone\n" + "Hullo\n" + "Hallo\n"
				+ "External links\n" + "References\n" + "");
	}
  
	public void test999() {
		HelloTemplateListener listener = new HelloTemplateListener();
		wikiModel.parseEvents(listener, HELLO_TEXT);
		assertEquals(
				listener.getCollectorBuffer().toString(),
				"wiktionarypar|hello\n" + 
				"otheruses|Hello (disambiguation)\n" + 
				"cite web|url=http://www.etymonline.com/index.php?search=hello&searchmode=none|title=Online Etymology Dictionary\n" + 
				"cite web|url=http://etext.lib.virginia.edu/railton/roughingit/rihp.html|title=Roughing It|publisher=UVa Library\n" + 
				"cite book |last= Foster |first= George G |title= New York in Slices |url= http://name.umdl.umich.edu/AJA2254.0001.001 |accessdate= 2006-08-15 |year= 1849  |publisher= W. F. Burgess|location= New York |pages= [http://www.hti.umich.edu/cgi/t/text/pageviewer-idx?c=moa;cc=moa;g=moagrp;xc=1;q1=hello;rgn=full%20text;idno=aja2254.0001.001;didno=aja2254.0001.001;view=image;seq=0122 p120] \n" + 
				"cite web|url=http://books.google.com/|title=Google books\n" + 
				"cite book |last= Lester |first= Charles Edwards |title= The Artists of America: A Series of Biographical Sketches of American Artists with Portraits and Designs on Steel |url= http://books.google.com/books?id=IiUEAAAAYAAJ |accessdate= 2007-10-18 |year= 1846  |publisher= Baker & Scribner |location= New York |pages= [http://books.google.com/books?id=IiUEAAAAYAAJ&pg=RA2-PA62&dq=hello+date:0-1876&as_brr=0#PRA2-PA62,M1 p62] \n" + 
				"cite web|url=http://www.etymonline.com/index.php?search=hello&searchmode=none|title=Online Etymology Dictionary\n" + 
				"cite web|url=http://books.google.com/|title=Google books\n" + 
				"cite web|url=http://books.google.com/|title=Google books\n" + 
				"cite book |author=Bryson, Bill|title=Mother Tongue: English & How It Got That Way |url=http://www.ralphmag.org/mothertongue.html\n" + 
				"Fact|date=September 2007\n" + 
				"cite web|url=http://www.collectorcafe.com/article_archive.asp?article=800&id=1507|title=The First “Hello!”: Thomas Edison, the Phonograph and the Telephone – Part 2|author=Allen Koenigsberg|publisher=Antique Phonograph Magazine, Vol.VIII No.6|accessdate=2006-09-13\n" + 
				"cite web|url=http://www2.cs.uh.edu/~klong/papers/hello.txt|title=All Things Considered|author=Allen Koenigsberg|publisher=National Public Radio|accessdate=2006-09-13|date=1999\n" + 
				"cn|date=October 2007\n" + 
				"cn|date=October 2007\n" + 
				"cite web|url=http://www.m-w.com/dictionary/hello|title=Hello|publisher=Merriam-Webster Online\n" + 
				"cite web|url=http://www.bartelby.com/61/60/H0136000.html|title=Hello|publisher= The American Heritage® Dictionary of the English Language: Fourth Edition.|date=2000|accessdate=2006-09-01\n" + 
				"reflist|2\n");
	}
}

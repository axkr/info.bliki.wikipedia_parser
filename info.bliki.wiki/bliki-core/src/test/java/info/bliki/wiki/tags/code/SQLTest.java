package info.bliki.wiki.tags.code;

import info.bliki.wiki.filter.FilterTestSupport;
import junit.framework.Test;
import junit.framework.TestSuite;

public class SQLTest extends FilterTestSupport {
	public SQLTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(SQLTest.class);
	}

	public void test001() {
		final String result = wikiModel.render("'''SQL Example'''\n" + "<source lang='sql'>create table Hydroxyl (\n"
				+ " id integer primary key,\n" + " name varchar(42)\n" + ")\n" + "</source>", false);
		String expect = "\n" + 
				"<p><b>SQL Example</b>\n" + 
				"</p><pre class=\"sql\"><span style=\"color:#7F0055; font-weight: bold; \">create</span> <span style=\"color:#7F0055; font-weight: bold; \">table</span> Hydroxyl (\n" + 
				" id <span style=\"color:#7F0055; font-weight: bold; \">integer</span> primary <span style=\"color:#7F0055; font-weight: bold; \">key</span>,\n" + 
				" name <span style=\"color:#7F0055; font-weight: bold; \">varchar</span>(42)\n" + 
				")\n" + 
				"</pre>";
		assertEquals("SQL test001", expect, result);
	}

	public void test002() {
		// test for wrong sql text
		final String result = wikiModel.render("'''SQL Example'''\n" + "<source lang='sql'>\n"
				+ "<form><input type=\"button\" onclick=\"alert('Are you sure you want to do this?')\" value=\"Alert\"></form>\n"
				+ "</source>", false);
		String expect = "\n" + 
				"<p><b>SQL Example</b>\n" + 
				"</p><pre class=\"sql\">\n" + 
				"&#60;form&#62;&#60;input type=<span style=\"color:#2A00FF; \">&#34;button&#34;</span> onclick=<span style=\"color:#2A00FF; \">&#34;alert(&#39;Are you sure you want to do this?&#39;)&#34;</span> value=<span style=\"color:#2A00FF; \">&#34;Alert&#34;</span>&#62;&#60;/form&#62;\n" + 
				"</pre>";
		assertEquals("SQL test002", expect, result);
	}
	
	public void test003() {
		final String result = wikiModel.render("<source lang=\"sql\">\n-- a line comment\n" + 
				"select * from testtable WITH UR\n" + 
				"</source>", false);
		String expect = "<pre class=\"sql\"><span style=\"color:#3F7F5F; \">\n" + 
				"-- a line comment\n" + 
				"</span><span style=\"color:#7F0055; font-weight: bold; \">select</span> * <span style=\"color:#7F0055; font-weight: bold; \">from</span> testtable WITH UR\n" + 
				"</pre>";
		assertEquals("SQL test003", expect, result);
	}
	
	public void test004() {
		final String result = wikiModel.render("<source lang=\"sql\">\n" + 
				"coMMEnt on column CALCULATEALL_VEHICLE.ACTIVE is 'Flag indicating if the vehicle is active or not';\n" + 
				"</source>", false);
		String expect = "<pre class=\"sql\">\n" + 
				"<span style=\"color:#7F0055; font-weight: bold; \">comment</span> <span style=\"color:#7F0055; font-weight: bold; \">on</span> <span style=\"color:#7F0055; font-weight: bold; \">column</span> CALCULATEALL_VEHICLE.ACTIVE <span style=\"color:#7F0055; font-weight: bold; \">is</span> <span style=\"color:#2A00FF; \">&#39;Flag indicating if the vehicle is active or not&#39;</span>;\n" + 
				"</pre>";
		assertEquals("SQL test004", expect, result);
	}
}

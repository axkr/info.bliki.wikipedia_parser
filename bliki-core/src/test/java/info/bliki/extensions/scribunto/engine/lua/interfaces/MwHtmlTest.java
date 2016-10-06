package info.bliki.extensions.scribunto.engine.lua.interfaces;

import info.bliki.extensions.scribunto.engine.lua.LuaTestBase;
import org.junit.runner.Description;

public class MwHtmlTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "HtmlLibraryTests.lua";
    }

    @Override public boolean isIgnored(Description testDescription) {
        return testDescription.getMethodName().equals("mw.html strip marker test");
    }
}

package info.bliki.extensions.scribunto.interfaces;

import org.junit.runner.Description;

public class MwHtmlTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "HtmlLibraryTests.lua";
    }

    @Override
    public boolean isIgnored(Description testDescription) {
        return true;
    }
}

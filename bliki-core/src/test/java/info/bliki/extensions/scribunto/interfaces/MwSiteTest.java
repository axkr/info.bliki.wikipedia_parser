package info.bliki.extensions.scribunto.interfaces;

import org.junit.runner.Description;

public class MwSiteTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "SiteLibraryTests.lua";
    }

    @Override
    public boolean isIgnored(Description testDescription) {
        return true;
    }
}

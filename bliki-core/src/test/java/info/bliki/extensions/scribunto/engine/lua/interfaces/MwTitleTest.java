package info.bliki.extensions.scribunto.engine.lua.interfaces;

import org.junit.runner.Description;

public class MwTitleTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "TitleLibraryTests.lua";
    }

    @Override
    public boolean isIgnored(Description testDescription) {
        return true;
    }
}

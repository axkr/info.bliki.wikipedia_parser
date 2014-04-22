package info.bliki.extensions.scribunto.interfaces;

import org.junit.runner.Description;

public class MwLanguageTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "LanguageLibraryTests.lua";
    }

    @Override public boolean isIgnored(Description testDescription) {
        return true;
    }
}

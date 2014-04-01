package info.bliki.extensions.scribunto.interfaces;

import org.junit.runner.Description;

public class MwMessageTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "MessageLibraryTests.lua";
    }

    @Override
    public boolean isIgnored(Description testDescription) {
        return true;
    }
}

package info.bliki.extensions.scribunto.interfaces;

import org.junit.runner.Description;

public class MwCommonTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "CommonTests.lua";
    }

    @Override
    public boolean isIgnored(Description testDescription) {
        return true;
    }
}

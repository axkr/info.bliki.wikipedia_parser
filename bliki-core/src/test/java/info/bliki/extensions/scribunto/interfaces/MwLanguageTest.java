package info.bliki.extensions.scribunto.interfaces;

import org.junit.Ignore;

@Ignore
public class MwLanguageTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "LanguageLibraryTests.lua";
    }
}

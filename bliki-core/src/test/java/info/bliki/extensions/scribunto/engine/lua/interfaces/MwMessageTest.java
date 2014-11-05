package info.bliki.extensions.scribunto.engine.lua.interfaces;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MwMessageTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "MessageLibraryTests.lua";
    }

    @Override public Set<String> ignoredTests() {
        return new HashSet<>(Arrays.asList(
            "exists (1)",
            "inLanguage"
        ));
    }
}

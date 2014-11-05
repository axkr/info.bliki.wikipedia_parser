package info.bliki.extensions.scribunto.engine.lua.interfaces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MwTextTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "TextLibraryTests.lua";
    }

    @Override
    public Set<String> ignoredTests() {
        return new HashSet<>(Arrays.asList(
            "decode named",
            "unstrip",
            "listToText, alternate conjunction",
            "truncate, with adjusted length",
            "truncate, with adjusted length (2)",
            "truncate, ridiculously short",
            "truncate, ridiculously short (2)"
        ));
    }
}

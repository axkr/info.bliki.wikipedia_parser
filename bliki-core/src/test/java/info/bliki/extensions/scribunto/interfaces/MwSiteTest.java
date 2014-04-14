package info.bliki.extensions.scribunto.interfaces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MwSiteTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "SiteLibraryTests.lua";
    }

    @Override public Set<String> ignoredTests() {
        return new HashSet<>(Arrays.asList(
                "Project talk namespace by name (extraneous spaces and underscores)"
        ));
    }
}

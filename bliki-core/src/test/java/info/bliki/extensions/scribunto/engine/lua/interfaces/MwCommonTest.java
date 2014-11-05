package info.bliki.extensions.scribunto.engine.lua.interfaces;

import info.bliki.extensions.scribunto.engine.lua.LuaTestBase;
import org.junit.runner.Description;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MwCommonTest extends LuaTestBase {
    @Override
    public String getLuaTest() {
        return "CommonTests.lua";
    }

    @Override public boolean isIgnored(Description testDescription) {
        final String methodName = testDescription.getMethodName();

        return super.isIgnored(testDescription) ||
                methodName.contains("setfenv") ||
                methodName.contains("getfenv");
    }

    @Override
    public Set<String> ignoredTests() {
        return new HashSet<>(Arrays.asList(
            "string is not string metatable",
            "clone table then modify"
        ));
    }
}

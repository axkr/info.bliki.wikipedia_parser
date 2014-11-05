package info.bliki.extensions.scribunto.engine.lua;

import info.bliki.extensions.scribunto.engine.lua.interfaces.LuaTestRunner;
import info.bliki.wiki.model.WikiModel;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.luaj.vm2.LuaValue.valueOf;

@RunWith(LuaTestRunner.class)
public abstract class LuaTestBase {
    private ScribuntoLuaEngine scribuntoLuaEngine;
    private LuaTable tests;

    public abstract String getLuaTest();

    public boolean isIgnored(Description testDescription) {
        return ignoredTests().contains(testDescription.getMethodName());
    }

    public Set<String> ignoredTests() {
        return Collections.emptySet();
    }

    public void setUp() throws IOException {
        scribuntoLuaEngine = new ScribuntoLuaEngine(new WikiModel("${image}", "${title}"));
        tests    = loadTests();
    }

    public void runTests(RunNotifier notifier)  {
        final int testCount        = tests.get("count").toint();
        final LuaValue provideFunc = tests.get("provide");
        final LuaValue runFunc     = tests.get("run");

        for (int i=1; i<testCount+1; i++) {
            final Varargs provideValue = provideFunc.invoke(valueOf(i));
            final LuaValue name = provideValue.arg(2);
            final String expected = provideValue.arg(3).checkjstring();

            Description testDescription = Description.createTestDescription(getClass(), name.toString());

            if (isIgnored(testDescription)) {
                notifier.fireTestIgnored(testDescription);
                continue;
            }

            notifier.fireTestStarted(testDescription);
            try {
                final String actual = runFunc.call(valueOf(i))
                        .checkjstring()
                        .replaceAll("@?[\\w\\.]+\\.lua:\\d+\\s*", "");

                assertThat(actual).isEqualTo(expected);
                notifier.fireTestFinished(testDescription);
            } catch (Throwable e) {
                notifier.fireTestFailure(new Failure(testDescription, e));
            }
        }
    }

    private LuaTable loadTests() throws IOException {
        InputStream is = null;
        try {
            is = globals().finder.findResource(getLuaTest());
            if (is == null) {
                throw new IOException("test "+getLuaTest()+ " not found");
            }
            return globals().load(is, getLuaTest(), "t", globals()).call().checktable();
        } finally {
            if (is != null) is.close();
        }
    }

    private Globals globals() {
        return scribuntoLuaEngine.getGlobals();
    }
}

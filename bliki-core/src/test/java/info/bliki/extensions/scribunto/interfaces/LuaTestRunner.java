package info.bliki.extensions.scribunto.interfaces;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import java.io.IOException;

public class LuaTestRunner extends Runner {
    private Class<? extends LuaTestBase> klass;
    private LuaTestBase luaTest;

    public LuaTestRunner(Class<? extends LuaTestBase> klass) throws Exception {
        this.klass = klass;
        this.luaTest = klass.newInstance();
    }

    @Override
    public Description getDescription() {
        return Description.createSuiteDescription(klass);
    }

    @Override
    public void run(RunNotifier notifier) {
        Result result = new Result();
        try {
            luaTest.setUp();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        notifier.fireTestRunStarted(getDescription());
        luaTest.runTests(notifier);
        notifier.fireTestRunFinished(result);
    }
}

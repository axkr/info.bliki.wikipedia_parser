package info.bliki.extensions.scribunto.template;

import org.junit.Before;
import org.junit.Test;
import org.luaj.vm2.LuaValue;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class FrameTest {
    private Frame subject;

    @Before public void before() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("a", "a_value");
        params.put("b", "b_value");
        subject = new Frame(null, params, null);
    }

    @Test public void testGetAllArguments() {
        LuaValue arguments = subject.getAllArguments();
        assertThat(arguments.length()).isEqualTo(2);
        assertThat(arguments.get(1).toString()).isEqualTo("a_value");
        assertThat(arguments.get(2).toString()).isEqualTo("b_value");
    }
}

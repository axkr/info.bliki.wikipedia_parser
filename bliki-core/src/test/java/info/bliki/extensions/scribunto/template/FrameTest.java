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
        params.put("a", "a value");
        params.put("b", "b value");
        params.put("1", "numbered param");
        subject = new Frame(null, params, null);
    }

    @Test public void testGetAllArguments() {
        LuaValue arguments = subject.getAllArguments();
        assertThat(arguments.length()).isEqualTo(1);

        assertThat(arguments.get("a").toString()).isEqualTo("a value");
        assertThat(arguments.get("b").toString()).isEqualTo("b value");
        assertThat(arguments.get(LuaValue.valueOf(1)).toString()).isEqualTo("numbered param");
    }
}

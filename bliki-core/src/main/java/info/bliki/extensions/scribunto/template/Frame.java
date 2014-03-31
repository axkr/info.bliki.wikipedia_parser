package info.bliki.extensions.scribunto.template;

import org.luaj.vm2.LuaValue;

import java.util.Map;

public class Frame {
    private final Map<String, String> templateParameters;
    private final Frame parent;

    public Frame(Map<String, String> templateParameters, Frame parent) {
        this.templateParameters = templateParameters;
        this.parent = parent;
    }

    public LuaValue getArgument(String name) {
        String value = templateParameters.get(name);
        if (value != null) {
            return LuaValue.valueOf(value);
        } else {
            return LuaValue.NIL;
        }
    }

    public Frame getParent() {
        return parent;
    }
}

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

    public Frame newChild(Map<String, String> templateParameters, Title title) {
        return new Frame(templateParameters, this);
    }

    public LuaValue getArgument(String name) {
        String value = templateParameters.get(name);
        if (value != null) {
            return LuaValue.valueOf(value);
        } else {
            return LuaValue.NIL;
        }
    }

    public LuaValue getAllArguments() {
        LuaValue[] values = new LuaValue[templateParameters.size()];
        String[]   templateValues = templateParameters.values().toArray(new String[templateParameters.size()]);
        for (int i = 0; i<values.length; i++) {
            values[i] = LuaValue.valueOf(templateValues[i]);
        }
        return LuaValue.listOf(values);
    }

    public Frame getParent() {
        return parent;
    }
}

package info.bliki.extensions.scribunto.template;

import info.bliki.wiki.filter.ParsedPageName;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

public class Frame {
    private final ParsedPageName page;
    private final Map<String, String> templateParameters;
    private final Frame parent;

    public Frame(ParsedPageName page, Map<String, String> templateParameters, Frame parent) {
        this.templateParameters = templateParameters;
        this.page = page;
        this.parent = parent;
    }

    public Frame newChild(ParsedPageName pageName, Map<String, String> templateParameters) {
        return new Frame(pageName, templateParameters, this);
    }

    public LuaValue getArgument(String name) {
        String value = templateParameters != null ? templateParameters.get(name) : null;

        if (value != null) {
            return LuaValue.valueOf(value);
        } else {
            return LuaValue.NIL;
        }
    }

    public Map<String, String> getTemplateParameters() {
        return new HashMap<>(templateParameters);
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

    public String getTitle() {
        return page.pagename;
    }
}

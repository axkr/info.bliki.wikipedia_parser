package info.bliki.wiki.template;

import static info.bliki.wiki.filter.TemplateParser.createSingleParameter;
import static info.bliki.wiki.filter.TemplateParser.mergeParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.extensions.scribunto.template.Frame;
import info.bliki.extensions.scribunto.template.ModuleExecutor;

/**
 * A template parser function for <code>{{ #invoke: ... }}</code> syntax.
 *
 */
public class Invoke extends AbstractTemplateFunction {
    public final static ITemplateFunction CONST = new Invoke();

    public Invoke() {

    }

    @Override
    public String parseFunction(List<String> parts, IWikiModel model,
            char[] src, int beginIndex, int endIndex, boolean isSubst)
            throws IOException {
        if (parts.size() < 2) {
            throw new AssertionError("not enough arguments");
        }

        ModuleExecutor executor = model.getModuleExecutor();
        if (executor == null) {
            throw new AssertionError("no ModuleExecutor defined");
        }
        String module = parts.get(0);
        String method = parts.get(1);
        Frame parent = model.getFrame();
        try {
            Map<String, String> map = getParameters(parts, model);
            System.out.println(module + " - " + method + " - " + map.toString());
            return executor.run(model, module, method, new Frame(map, parent));
        } finally {
            model.setFrame(parent);
        }
    }

    private Map<String, String> getParameters(List<String> parts, IWikiModel model) {
        LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();
        if (parts.size() > 2) {
            List<String> unnamedParameters = new ArrayList<String>();
            for (int i = 2; i < parts.size(); i++) {
                createSingleParameter(parts.get(i), model, parameterMap,
                        unnamedParameters);
            }
            mergeParameters(parameterMap, unnamedParameters);
        }
        return parameterMap;
    }

    @Override
    public String getFunctionDoc() {
        return null;
    }

}

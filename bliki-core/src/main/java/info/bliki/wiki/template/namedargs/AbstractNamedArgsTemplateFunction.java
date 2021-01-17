package info.bliki.wiki.template.namedargs;

import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;

/**
 * An abstract template parser function with named arguments.
 */
public abstract class AbstractNamedArgsTemplateFunction implements INamedArgsTemplateFunction {
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String parseFunction(
            NamedArgs parts, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst)
            throws IOException;

    /**
     * {@inheritDoc}
     */
    @Override
    public String parseFunction(
            List<String> parts, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst)
            throws IOException {
        throw new AssertionError("No implementation.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String getFunctionDoc();
}

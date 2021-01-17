package info.bliki.wiki.template.namedargs;

import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.template.ITemplateFunction;

import java.io.IOException;

/**
 * Interface for a template parser function with named arguments.
 * (i.e.
 * <code>
 *   return function (CSS_page)
 *     return mw.getCurrentFrame():extensionTag{
 * 	     name = "templatestyles", args = { src = CSS_page }
 *     }
 *   end
 * </code> )
 */
public interface INamedArgsTemplateFunction extends ITemplateFunction {
    /**
     * Parse a template function with named arguments.
     *
     * The result is also a text string in Wikipedia syntax notation which will be
     * parsed again (recursively) in the TemplateParser step.
     *
     * @param parts
     *          the parser function arguments
     * @param model
     *          the wiki model
     * @param src
     *          the array of the current Wikipedia source text.
     * @param beginIndex
     *          the beginning index, inclusive.
     * @param endIndex
     *          the ending index, exclusive.
     * @param isSubst
     *          if <code>true</code> the template function was called from
     *          <code>subst</code> or <code>safesubst</code> function and the arguments of
     *          the function are typically not parsed recursively.
     * @return the result string of this template function or <code>null</code> if
     *         the parsing fails or isn't valid.
     * @throws IOException
     */
    String parseFunction(
            NamedArgs parts, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst)
            throws IOException;
}

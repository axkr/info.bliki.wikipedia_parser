package info.bliki.wiki.tags;

import java.io.IOException;

import info.bliki.wiki.filter.ITextConverter;
import info.bliki.wiki.model.IWikiModel;

/**
 * Wiki tag to indicate a template call.
 * <p>
 * TODO: add support for #renderHTML.
 */
public class TemplateTag extends HTMLTag
{
    public TemplateTag(String name)
    {
        super(name);
    }

    @Override
    public void renderHTML(ITextConverter converter, Appendable writer, IWikiModel model) throws IOException
    {
        // TODO: not supported yet
        super.renderHTML(converter, writer, model);
    }

    @Override
    public boolean isReduceTokenStack()
    {
        return false;
    }
}

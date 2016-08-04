package info.bliki.wiki.template;

import info.bliki.wiki.model.IWikiModel;

import java.io.IOException;
import java.util.List;


/**
 * Formats a date according to user preferences; a default can be given as an optional case-sensitive second
 * parameter for users without date preference; can convert a date from an existing format to any of
 * dmy, mdy, ymd or ISO 8601 formats, with the user's preference overriding the specified format.
 *
 * <a href="https://en.wikipedia.org/wiki/Help:Magic_words#Formatting">Help:Magic_words - Formatting</a>
 */
public class FormatDate extends AbstractTemplateFunction  {
    public final static ITemplateFunction CONST = new FormatDate();

    @Override
    public String parseFunction(List<String> parts, IWikiModel model, char[] src, int beginIndex, int endIndex, boolean isSubst) throws IOException {
        if (parts.size() > 0) {
            // don't format anything, just return raw date for now
            return parts.get(0);
        } else {
            return null;
        }
    }
}

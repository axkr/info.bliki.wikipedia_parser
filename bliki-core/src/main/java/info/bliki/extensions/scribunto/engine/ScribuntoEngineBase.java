package info.bliki.extensions.scribunto.engine;

import info.bliki.wiki.filter.ParsedPageName;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import info.bliki.wiki.namespaces.INamespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;

public abstract class ScribuntoEngineBase implements ScribuntoEngine {
    protected static final Charset UTF8 = Charset.forName("UTF-8");

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected final IWikiModel model;
    protected final INamespace.INamespaceValue moduleNamespace;

    protected ScribuntoEngineBase(IWikiModel model) {
        this.model = model;
        this.moduleNamespace = model.getNamespace().getModule();
    }

    protected ParsedPageName pageNameForModule(String moduleName, INamespace.INamespaceValue fallback) {
        if (moduleName.toLowerCase().startsWith(moduleNamespace.getPrimaryText().toLowerCase() + ":")) {
            return ParsedPageName.parsePageName(model, moduleName, moduleNamespace, false, false);
        } else {
            return new ParsedPageName(fallback, moduleName, true);
        }
    }

    protected InputStream getRawWikiContentStream(ParsedPageName pageName) throws FileNotFoundException {
        return new ByteArrayInputStream(getRawWikiContent(pageName).getBytes(UTF8));
    }

    protected String getRawWikiContent(ParsedPageName pageName) throws FileNotFoundException {
        try {
            final String content =  model.getRawWikiContent(pageName, null);
            if (content == null) {
                throw new FileNotFoundException("getRawWikiContent returned null");
            }
            return content;
        } catch (WikiModelContentException ignored) {
        }
        throw new FileNotFoundException("could not find module " + pageName);
    }
}

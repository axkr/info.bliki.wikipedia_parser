package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.template.Title;
import info.bliki.wiki.model.IWikiModel;
import info.bliki.wiki.model.WikiModelContentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class ScribuntoEngineBase implements ScribuntoEngine {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected final IWikiModel model;
    private Map<String, ScribuntoModule> modules = new HashMap<>();

    protected ScribuntoEngineBase(IWikiModel model) {
        this.model = model;
    }

    public ScribuntoModule fetchModuleFromParser(Title title) {
        final String key = title.getPrefixedDBkey();
        if (!modules.containsKey(key)) {
            try {
                String content = model.getRawWikiContent(title.parsedPageName(), null);
                modules.put(key, newModule(content, title.parsedPageName().fullPagename()));
            } catch (WikiModelContentException e) {
                logger.warn("error fetching content");
            }
        }
        return modules.get(key);
    }

    /**
     * Creates a new module object within this engine
     */
    protected abstract ScribuntoModule newModule(String text, String chunkName);
}

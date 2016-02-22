package info.bliki.wiki.model;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;

public class InterWikiMap {
    private static final String GLOBAL_PREFIX = "__global:";
    private static final String SITES_PREFIX = "__sites:";
    private static final String LIST_PREFIX = "__list:";

    private Map<String, InterWiki> wikis;
    private final Map<String, String> sites = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final String wikiId;

    public InterWikiMap(Map<String, String> map, String wikiId) {
        this.wikiId = wikiId;
        this.wikis = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            final String key = entry.getKey();
            final String value = entry.getValue();

            if (key.startsWith(SITES_PREFIX)) {
                final String[] parts = key.split(":", 2);
                if (parts.length != 2) {
                    throw new RuntimeException("Invalid site descriptor: " + value);
                }
                sites.put(parts[1], value);
            } else if (!key.startsWith(LIST_PREFIX) && !value.isEmpty()) {
                wikis.put(key, parseInterWiki(value));
            }
        }
    }

    /**
     * @param prefix the prefix to look for
     * @return the interwiki or null if not found
     */
    @Nullable public InterWiki getInterWiki(String prefix) {
        String site = sites.get(wikiId);
        InterWiki interWiki = wikis.get(wikiId + ":" + prefix);
        if (interWiki == null && site != null) {
            interWiki = wikis.get("_" +site + ":" + prefix);
        }
        if (interWiki == null){
            interWiki = wikis.get(GLOBAL_PREFIX +prefix);
        }
        return interWiki;
    }

    private InterWiki parseInterWiki(String value) {
        final String[] parts = value.split(" ", 2);
        if (parts.length != 2) {
            throw new RuntimeException("invalid entry: "+value);
        }
        final boolean local = "1".equals(parts[0]);
        final String pattern = parts[1];

        return new InterWiki(pattern, local);
    }
}

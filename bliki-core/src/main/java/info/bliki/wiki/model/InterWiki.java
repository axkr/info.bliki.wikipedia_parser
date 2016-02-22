package info.bliki.wiki.model;

public class InterWiki {
    final String pattern;
    final boolean local;

    InterWiki(String pattern, boolean local) {
        this.pattern = pattern;
        this.local = local;
    }
}

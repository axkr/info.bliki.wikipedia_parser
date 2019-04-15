package info.bliki.wiki.model;

/**
 * This exception will be thrown, if there's a problem in getting the raw wiki
 * text.
 *
 */
public class WikiModelContentException extends Exception {
    static final long serialVersionUID = 6992235128508021824L;

    public WikiModelContentException(String message, Throwable exception) {
        super(message, exception);

    }
}

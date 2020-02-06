package info.bliki.extensions.scribunto;

public class ScribuntoException extends Exception {
    static final long serialVersionUID = 6581463908790390798L;

    public ScribuntoException(Exception e) {
        super(e);
    }

    public ScribuntoException(String s) {
        super(s);
    }
}

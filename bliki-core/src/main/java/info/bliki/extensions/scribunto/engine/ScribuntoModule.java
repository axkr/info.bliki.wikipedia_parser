package info.bliki.extensions.scribunto.engine;

import info.bliki.extensions.scribunto.ScribuntoException;
import info.bliki.extensions.scribunto.template.Frame;

public interface ScribuntoModule {

    enum Status {
        OK,
        ERROR
    }

    /**
     * Invoke the function with the specified name.
     *
     * @return string
     */
    String invoke(String functionName, Frame frame) throws ScribuntoException;

    /**
     * Validates the script and returns a Status object containing the syntax
     * errors for the given code.
     *
     * @return Status
     */
    Status validate();
}

package com.b0ve.sig.utils.exceptions;

/**
 * Use SIGException instead.
 * @author borja
 * @deprecated
 */
@Deprecated
public class ExecutionException extends SIGException {

    public ExecutionException(String string, Object associatedObject, Exception nestedException) {
        super("Execution Exception: " + string, associatedObject, nestedException);
    }
}

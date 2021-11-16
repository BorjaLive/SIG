package com.b0ve.sig.utils.exceptions;

/**
 * Use SIGException instead.
 * @author borja
 * @deprecated
 */
@Deprecated
public class ParseException extends SIGException {

    public ParseException(String string, Object associatedObject, Exception nestedException) {
        super("XML Parser Exception: " + string, associatedObject, nestedException);
    }
}

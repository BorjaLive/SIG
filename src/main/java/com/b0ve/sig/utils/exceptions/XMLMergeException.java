package com.b0ve.sig.utils.exceptions;

/**
 * Use SIGException instead.
 * @author borja
 * @deprecated
 */
@Deprecated
public class XMLMergeException extends SIGException {

    public XMLMergeException(String string, Object associatedObject, Exception nestedException) {
        super("XML Merge Exception: " + string, associatedObject, nestedException);
    }
}

package com.b0ve.sig.utils.exceptions;

/**
 * Use SIGException instead.
 * @author borja
 * @deprecated
 */
@Deprecated
public class XPathEvaluationException extends SIGException {

    public XPathEvaluationException(String string, Object associatedObject, Exception nestedException) {
        super("XPath Evaluation Exception: " + string, associatedObject, nestedException);
    }
}

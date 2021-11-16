package com.b0ve.sig.utils.exceptions;

/**
 * Use SIGException instead.
 * @author borja
 * @deprecated
 */
@Deprecated
public class ConfigurationException extends SIGException {

    public ConfigurationException(String string, Object associatedObject, Exception nestedException) {
        super("Configuration Exception: " + string, associatedObject, nestedException);
    }

}

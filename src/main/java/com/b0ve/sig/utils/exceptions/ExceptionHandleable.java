package com.b0ve.sig.utils.exceptions;

import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Exception handler for process
 * @author borja
 */
public interface ExceptionHandleable {

    public void handleException(SIGException exception);
}

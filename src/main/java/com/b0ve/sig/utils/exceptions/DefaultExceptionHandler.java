package com.b0ve.sig.utils.exceptions;

/**
 * Simple ExceptionHandle that outputs the exceptions to stderr
 *
 * @author borja
 */
public class DefaultExceptionHandler implements ExceptionHandleable {

    private final static DefaultExceptionHandler defaultExceptionHandler = new DefaultExceptionHandler();

    @Override
    public void handleException(SIGException exception) {
        System.err.println("SIG Exception: " + exception.getMessage()
                + (exception.getAssociatedObject() == null ? "" : "\nAssociated Object: " + exception.getAssociatedObject().toString())
                + (exception.getNestedException() == null ? "" : "\nNested Exception: "));
        if (exception.getNestedException() != null) {
            exception.getNestedException().printStackTrace();
        }
    }

    /**
     * Returns singleton reference to the handler
     *
     * @return
     */
    public static ExceptionHandleable getHandler() {
        return defaultExceptionHandler;
    }
}

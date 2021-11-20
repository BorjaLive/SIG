package com.b0ve.sig.utils.exceptions;

/**
 * Exception to be used inside the process
 *
 * @author borja
 */
public class SIGException extends Exception {

    private final Object associatedObject;
    private final Exception nestedException;

    /**
     * Creates an exception for SIG Exception Handlers Variants are deprecated,
     * use this class instead with the exception cause on the description text.
     *
     * @param text Description
     * @param associatedObject Message or other object that can relates to the
     * exception
     * @param nestedException Exception wrapped inside, may be the cause of the
     * exception.
     */
    public SIGException(String text, Object associatedObject, Exception nestedException) {
        super(text);
        this.associatedObject = associatedObject;
        this.nestedException = nestedException;
    }

    public Object getAssociatedObject() {
        return associatedObject;
    }

    public Exception getNestedException() {
        return nestedException;
    }

    @Override
    public String toString() {
        return getMessage();
    }

}

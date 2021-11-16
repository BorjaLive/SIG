package com.b0ve.sig.utils.condiciones;

/**
 * Simple class to use with Filter and Distributor Tasks. Value must be different, if it is not the message is droped.
 * @author borja
 */
public class FilterConditionNotEquals extends FilterCondition {

    private final String value;

    public FilterConditionNotEquals(String xpath, String value) {
        super(xpath);
        this.value = value;
    }

    @Override
    protected boolean testValue(String text) {
        return !text.equals(value);
    }
}

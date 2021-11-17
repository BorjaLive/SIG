package com.b0ve.sig.utils.condiciones;

/**
 * Simple class to use with Filter and Distributor Tasks. Value must equal, if it does not the message is droped.
 * @author borja
 */
@Deprecated
public class FilterConditionEquals extends FilterCondition {

    private final String value;

    public FilterConditionEquals(String xpath, String value) {
        super(xpath);
        this.value = value;
    }

    @Override
    protected boolean testValue(String text) {
        return text.equals(value);
    }
}

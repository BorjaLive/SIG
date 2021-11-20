package com.b0ve.sig.utils.condiciones;

import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Configurable class to use with Filter and Distributor Task.
 * @author borja
 */
public class FilterConditionConfigurable extends FilterCondition {

    public static enum CONDITIONS {
        STR_EQUALS,
        STR_DISTINCT,
        INT_EQUALS,
        INT_DISTINCT,
        INT_LESS_THAN,
        INT_GREATER_THAN,
        DECIMAL_LESS_THAN,
        DECIMAL_GRATER_THAN,
        EMPTY,
        NOT_EMPTY,
        EXISTS,
        NOT_EXIST
    }

    private final String value;
    private final CONDITIONS condition;

    public FilterConditionConfigurable(String xpath, CONDITIONS condition, String value) throws SIGException {
        super(xpath);
        this.value = value;
        this.condition = condition;
    }

    @Override
    protected boolean testValue(String text) {
        try {
            switch (condition) {
                case STR_EQUALS:
                    return text != null && text.equals(value);
                case STR_DISTINCT:
                    return text != null && !text.equals(value);
                case INT_EQUALS:
                    return Integer.parseInt(text) == Integer.parseInt(value);
                case INT_DISTINCT:
                    return Integer.parseInt(text) != Integer.parseInt(value);
                case INT_LESS_THAN:
                    return Integer.parseInt(text) < Integer.parseInt(value);
                case INT_GREATER_THAN:
                    return Integer.parseInt(text) > Integer.parseInt(value);
                case DECIMAL_LESS_THAN:
                    return Float.parseFloat(text) < Float.parseFloat(value);
                case DECIMAL_GRATER_THAN:
                    return Float.parseFloat(text) > Float.parseFloat(value);
                case EMPTY:
                    return text.isEmpty();
                case NOT_EMPTY:
                    return !text.isEmpty();
                case EXISTS:
                    return text != null;
                case NOT_EXIST:
                    return text == null;
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return false;
    }
}

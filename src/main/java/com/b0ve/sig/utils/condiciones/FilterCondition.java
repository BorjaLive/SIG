package com.b0ve.sig.utils.condiciones;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Simple base class to use in Filter and Distributor Task.
 * @author borja
 */
public abstract class FilterCondition implements Checkeable {

    private final String xpath;

    public FilterCondition(String xpath) {
        this.xpath = xpath;
    }

    @Override
    public final boolean checkCondition(Message mensaje) throws SIGException {
        return testValue(mensaje.evaluateXPathString(xpath));
    }

    protected abstract boolean testValue(String text);
}

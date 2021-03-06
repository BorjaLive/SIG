package com.b0ve.sig.utils.condiciones;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.xpath.XPathExpression;

/**
 * Simple base class to use in Filter and Distributor Task.
 *
 * @author borja
 */
public abstract class FilterCondition implements Checkeable {

    private final XPathExpression xpath;

    public FilterCondition(String xpath) throws SIGException {
        this.xpath = XMLUtils.compile(xpath);
    }

    @Override
    public final boolean checkCondition(Message mensaje) throws SIGException {
        return testValue(mensaje.evalString(xpath).trim());
    }

    protected abstract boolean testValue(String text);
}

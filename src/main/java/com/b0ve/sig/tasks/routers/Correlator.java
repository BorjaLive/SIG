package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.xpath.XPathExpression;

/**
 * Finds a different message with the same correlation ID on each input. Sends
 * them to the outputs at the same time.
 *
 * @author borja
 */
public final class Correlator extends CorrelatorTemplate {

    private final XPathExpression expresion;

    public Correlator(String expresion) throws SIGException {
        if (expresion == null) {
            this.expresion = null;
        } else {
            this.expresion = XMLUtils.compile(expresion);
        }
    }

    public Correlator() {
        expresion = null;
    }

    @Override
    protected Object extractCorrelation(Message message) throws SIGException {
        if (expresion == null) {
            return super.extractCorrelation(message);
        } else {
            return message.evalString(expresion);
        }
    }

}

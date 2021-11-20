package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLTools;
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
            this.expresion = XMLTools.compile(expresion);
        }
    }

    public Correlator() {
        expresion = null;
    }

    @Override
    protected boolean correlates(Message m1, Message m2) throws SIGException {
        if (expresion == null) {
            return super.correlates(m1, m2);
        } else {
            return m1.evalString(expresion).equals(m2.evalString(expresion));
        }
    }

    @Override
    public void validate() throws SIGException {
        super.validate();
        if (nInputs() != nOutputs()) {
            throw new SIGException("Configuration exception. Correlator requires the same number of inputs and outputs", null, null);
        }
    }

}

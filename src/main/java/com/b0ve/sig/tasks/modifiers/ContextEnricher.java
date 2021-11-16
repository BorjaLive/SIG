package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Merges the body of two messages
 * @author borja
 */
public class ContextEnricher extends ContextEnricherTemplate {

    @Override
    protected void enrich(Message m, Message condition) throws SIGException {
        m.setBody(Message.mergeXML(m.getBody(), condition.getBody()));
    }

}

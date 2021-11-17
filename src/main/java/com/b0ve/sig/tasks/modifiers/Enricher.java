package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;
import org.w3c.dom.Document;

/**
 * Merges a document with the body of a message.
 * @author borja
 */
public class Enricher extends EnricherTemplate {

    private Document staticContent;

    public Enricher(Object staticContent) {
        super();
        if (staticContent instanceof Document) {
            this.staticContent = (Document) staticContent;
        } else {
            try {
                this.staticContent = Message.parseXML((String) staticContent);
            } catch (SIGException ex) {
                handleException(ex);
            }
        }
    }

    @Override
    protected void enrich(Message m) throws SIGException {
        m.setBody(Message.mergeXML(m.getBody(), staticContent));
    }

}

package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import org.w3c.dom.Document;

/**
 * Merges a document with the body of a message.
 *
 * @author borja
 */
public class Enricher extends EnricherTemplate {

    private final Document staticContent;

    public Enricher(Object staticContent) throws SIGException {
        super();
        if (staticContent instanceof Document) {
            this.staticContent = (Document) staticContent;
        } else {
            this.staticContent = XMLUtils.parse((String) staticContent);
        }
    }

    @Override
    protected void enrich(Message m) throws SIGException {
        m.setBody(XMLUtils.merge(m.getBody(), staticContent));
    }

}

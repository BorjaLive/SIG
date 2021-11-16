package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Executes an XSLTransformation passed in the constructor to the body of messages.
 * @author borja
 */
public final class Translator extends TranslatorTemplate {

    private final String xslt;

    public Translator(String xslt) {
        super();
        this.xslt = xslt;
    }

    @Override
    protected void transform(Message m) throws SIGException {
        m.transformBody(xslt);
    }

}

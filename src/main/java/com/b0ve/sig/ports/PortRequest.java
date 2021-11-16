package com.b0ve.sig.ports;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;
import org.w3c.dom.Document;

public class PortRequest extends Port {

    public PortRequest(Adapter adaptador) {
        super(1, 1, adaptador);
    }

    /**
     * Request ports cannot send messages directly to the process, only responses.
     * @param doc 
     */
    @Override
    public void sendProcess(Document doc) {
        throw new UnsupportedOperationException("Output ports cannot send direct messages from adapter to the process.");
    }

    /**
     * If the app responds the port will create a message wit hthe same correlation ID.
     * @param m
     * @throws SIGException 
     */
    @Override
    protected void sendApp(Message m) throws SIGException {
        Document doc = adapter.sendApp(m);
        if (doc != null) {
            Message response = new Message(doc);
            response.setCorrelationID(m.getCorrelationID());
            output(0).push(response);
        }
    }

}

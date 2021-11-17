package com.b0ve.sig.ports;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;
import org.w3c.dom.Document;

public class PortInput extends Port {

    public PortInput(Adapter adaptador) {
        super(-1, 1, adaptador);
    }

    /**
     * Sends a message from the process to the process
     * @param doc 
     */
    public void sendProcess(Document doc) {
        try {
            output(0).push(new Message(doc));
        } catch (SIGException ex) {
            handleException(ex);
        }
    }

    /**
     * Input ports cannot send messages to the adapter
     * @param m 
     */
    @Override
    protected void sendApp(Message m) {
        throw new UnsupportedOperationException("Input ports cannot send messages from process to the adapter.");
    }

}

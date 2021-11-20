package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Adds a static content to the message
 *
 * @author borja
 */
public abstract class EnricherTemplate extends Task {

    public EnricherTemplate() {
        super(1, 1);
    }

    @Override
    public final void process() throws SIGException {
        Buffer output = output(0);
        Buffer input = input(0);
        while (!input.empty()) {
            Message mensaje = input.retrive();
            enrich(mensaje);
            output.push(mensaje);
        }
    }

    protected abstract void enrich(Message m) throws SIGException;

}

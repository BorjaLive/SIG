package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Outputs multiple messages from the content of a message. Number of elements
 * and outputs must match.
 *
 * @author borja
 */
public abstract class ChopperTemplate extends Task {
    
    public ChopperTemplate() {
        super(1, 0);
    }

    @Override
    public final void process() throws SIGException {
        Buffer input = input(0);
        while (!input.empty()) {
            Message mensaje = input.retrive();
            Message[] parts = chop(mensaje);
            for (int i = 0; i < parts.length; i++) {
                output(i).push(parts[i]);
            }
        }
    }

    protected abstract Message[] chop(Message mensaje) throws SIGException;

}

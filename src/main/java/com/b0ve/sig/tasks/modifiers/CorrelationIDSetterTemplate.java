package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Sets the correlation ID messages
 *
 * @author borja
 */
public abstract class CorrelationIDSetterTemplate extends Task {

    public CorrelationIDSetterTemplate() {
        super(1, 1);
    }

    @Override
    public void process() throws SIGException {
        Buffer output = output(0);
        Buffer input = input(0);
        while (!input.empty()) {
            Message m = input.retrive();
            giveCorrelationID(m);
            output.push(m);
        }
    }

    protected abstract void giveCorrelationID(Message m);

}

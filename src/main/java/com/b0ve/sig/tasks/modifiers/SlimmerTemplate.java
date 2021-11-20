package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Base Slimmer. Removes a static content from messages.
 *
 * @author borja
 */
public abstract class SlimmerTemplate extends Task {

    public SlimmerTemplate() {
        super(1, 1);
    }

    @Override
    public final void process() throws SIGException {
        Buffer output = output(0);
        Buffer input = input(0);
        while (!input.empty()) {
            Message m = input.retrive();
            slim(m);
            output.push(m);
        }
    }

    /**
     * Slims the content of the message
     *
     * @param m
     * @throws SIGException
     */
    protected abstract void slim(Message m) throws SIGException;
}

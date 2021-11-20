package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Slims content off of messages, slim configuration is provided by a parallel
 * input.
 *
 * @author borja
 */
public abstract class ContextSlimmerTemplate extends Task {

    public ContextSlimmerTemplate() {
        super(2, 1);
    }

    @Override
    public final void process() throws SIGException {
        Buffer output = output(0);
        Buffer inputBase = input(0);
        Buffer inputCondition = input(1);
        while (!inputBase.empty() && !inputCondition.empty()) {
            Message mBase = inputBase.retrive();
            Message mCondition = inputCondition.retrive();
            slim(mBase, mCondition);
            output.push(mBase);
        }
    }

    protected abstract void slim(Message m, Message condition) throws SIGException;

}

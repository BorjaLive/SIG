package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Forwards messages to different outputs based on conditions.
 * @author borja
 */
public abstract class DistributorTemplate extends Task {

    public DistributorTemplate() {
        super(1, 0);
    }

    @Override
    public final void process() throws SIGException {
        Buffer ouput = input(0);
        while (!ouput.empty()) {
            Message m = ouput.retrive();
            output(check(m)).push(m);
        }
    }

    protected abstract int check(Message mensaje) throws SIGException;

}

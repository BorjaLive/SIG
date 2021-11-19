package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.ListIterator;

/**
 * Outputs multiples copies of every message received on the input to each output.
 * @author borja
 */
public class Replicator extends Task {

    public Replicator() {
        super(1, 0);
    }

    @Override
    public void process() throws SIGException {
        Buffer input = input(0);
        while (!input.empty()) {
            Message m = input.retrive();
            for (ListIterator<Buffer> iter = outputs(); iter.hasNext();) {
                iter.next().push(new Message(m));
                
            }
        }
    }

}

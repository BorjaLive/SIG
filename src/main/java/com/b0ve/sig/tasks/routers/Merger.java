package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.ListIterator;

/**
 * Outputs messages received from multiple inpuits to the one output.
 *
 * @author borja
 */
public class Merger extends Task {

    public Merger() {
        super(0, 1);
    }

    @Override
    public void process() throws SIGException {
        Buffer output = output(0);
        for (ListIterator<Buffer> iter = inputs(); iter.hasNext();) {
            Buffer input = iter.next();
            while (!input.empty()) {
                output.push(input.retrive());
            }

        }
    }

}

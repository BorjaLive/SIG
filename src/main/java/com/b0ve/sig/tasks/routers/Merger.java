package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Outputs messages received from multiple inpuits to the one output.
 * @author borja
 */
public class Merger extends Task {

    public Merger() {
        super(0, 1);
    }

    @Override
    public void process() throws SIGException {
        Buffer output = output(0);
        for (int i = 0; i < nInputs(); i++) {
            Buffer input = input(i);
            while (!input.empty()) {
                output.push(input.retrive());
            }
        }
    }

}

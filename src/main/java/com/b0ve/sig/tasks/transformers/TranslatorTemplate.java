package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Outputs a message with the output of the XSLTransformation performed with the body of received message
 * @author borja
 */
public abstract class TranslatorTemplate extends Task {

    public TranslatorTemplate() {
        super(1, 1);
    }

    @Override
    public final void process() throws SIGException {
        Buffer output = output(0);
        Buffer input = input(0);
        while (!input.empty()) {
            Message m = input.retrive();
            transform(m);
            output.push(m);
        }
    }

    protected abstract void transform(Message m) throws SIGException;

}

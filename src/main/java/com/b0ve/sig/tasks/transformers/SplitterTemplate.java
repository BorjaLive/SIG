package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.xpath.XPathExpression;
/**
 * Outputs multiple messages with content from a single message
 *
 * @author borja
 */
public abstract class SplitterTemplate extends Task {
    
    public SplitterTemplate() throws SIGException {
        super(1, 1);
    }

    @Override
    public final void process() throws SIGException {
        Buffer output = output(0);
        Buffer input = input(0);
        while (!input.empty()) {
            Message message = input.retrive();
            Message[] parts = split(message);
            for (Message part : parts) {
                output.push(part);
            }
        }
    }
    
    protected abstract Message[] split(Message message) throws SIGException;

}

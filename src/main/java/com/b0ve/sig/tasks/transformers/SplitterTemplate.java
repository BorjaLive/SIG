package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.FragmentInfo;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.UUID;
import org.w3c.dom.Document;

/**
 * Outputs multiple messages with content from a single message
 *
 * @author borja
 */
public abstract class SplitterTemplate extends Task {

    public SplitterTemplate() {
        super(1, 1);
    }

    @Override
    public final void process() throws SIGException {
        Buffer output = output(0);
        Buffer input = input(0);
        while (!input.empty()) {
            Message message = input.retrive();
            Document[] parts = split(message);
            UUID fragmentID = UUID.randomUUID();
            for (int i = 0; i < parts.length; i++) {
                Message part = new Message(parts[i]);
                part.addFragmentInfo(message.getFragmentInfoStack());
                part.addFragmentInfo(new FragmentInfo(fragmentID, parts.length));
                output.push(part);
            }
        }
    }

    protected abstract Document[] split(Message m) throws SIGException;

}

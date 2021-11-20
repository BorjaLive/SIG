package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.FragmentInfo;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.UUID;
import org.w3c.dom.Document;

/**
 * Outputs multiple messages from the content of a message. Number of elements
 * and outputs must match.
 *
 * @author borja
 */
public abstract class ChopperTemplate extends Task {

    public ChopperTemplate() {
        super(1, 0);
    }

    @Override
    public final void process() throws SIGException {
        Buffer input = input(0);
        while (!input.empty()) {
            Message mensaje = input.retrive();
            //if(mensaje.getSequenceSize() != 0) throw new ExecutionException("No se puede fragmentar un fragmento de mensaje");
            Document[] parts = chop(mensaje);
            UUID fragmentID = UUID.randomUUID();
            for (int i = 0; i < parts.length; i++) {
                Message parte = new Message(parts[i]);
                parte.addFragmentInfo(mensaje.getFragmentInfoStack());
                parte.addFragmentInfo(new FragmentInfo(fragmentID, parts.length));
                output(i).push(parte);
            }
        }
    }

    protected abstract Document[] chop(Message mensaje) throws SIGException;

}

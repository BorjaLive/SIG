package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Drops messages that do not fullfill a certain condition
 * @author borja
 */
public abstract class FilterTemplate extends Task {

    public FilterTemplate() {
        super(1, 1);
    }

    @Override
    public final void process() throws SIGException {
        Buffer output = output(0);
        Buffer input = input(0);
        while (!input.empty()) {
            Message mensaje = input.retrive();
            if (check(mensaje)) {
                output.push(mensaje);
            } else {
                debugLog("Filtro deleted: " + mensaje.toString());
            }
        }
    }

    protected abstract boolean check(Message mensaje) throws SIGException;

}

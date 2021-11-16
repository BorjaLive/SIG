package com.b0ve.sig.tasks;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;

/**
 * Debugging task. It shows the messages that receives via the debug logging of the process.
 * It can be configured to forward the messages or stop the flow.
 * @author borja
 */
public class TaskDebug extends Task {

    private final boolean continuar;

    public TaskDebug() {
        this(false);
    }

    public TaskDebug(boolean continuar) {
        super(1, 1);
        this.continuar = continuar;
    }

    @Override
    public final void process() throws SIGException {
        Buffer entrada = input(0);
        while (!entrada.empty()) {
            Message mensaje = entrada.retrive();
            debugLog(mensaje.getBodyString());
            if (continuar) {
                output(0).push(mensaje);
            }
        }
    }

}

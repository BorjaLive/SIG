package com.b0ve.sig.ports;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.ProcessSync;
import com.b0ve.sig.utils.exceptions.SIGException;
import org.w3c.dom.Document;

/**
 * Ports are tasks that interact with adapters
 * @author borja
 */
public abstract class Port extends Task {

    protected final Adapter adapter;

    public Port(int entradas, int salidas, Adapter adaptador) {
        super(entradas, salidas);
        this.adapter = adaptador;
        adaptador.setPort(this);
    }

    /**
     * Reads messages from inputs, if it has them, and sends them to the app.
     * Each message is sent in an independent thread.
     * @throws SIGException 
     */
    @Override
    public void process() throws SIGException {
        if (hasInputs()) {
            Buffer entrada = input(0);
            while (!entrada.empty()) {
                Message mensaje = entrada.retrive();
                if (process != null && process instanceof ProcessSync) {
                    sendApp(mensaje);
                } else {
                    (new Thread() {
                        @Override
                        public void run() {
                            try {
                                sendApp(mensaje);
                            } catch (SIGException ex) {
                                handleException(ex);
                            }
                        }
                    }).start();
                }
            }
        }
    }

    /**
     * Sends a message to the app. ot all ports are allowed to do this.
     * @param m
     * @throws SIGException 
     */
    protected abstract void sendApp(Message m) throws SIGException;
    
    /**
     * Sends a message from the adapter to the process. Not all ports are allowed to do this.
     * @param doc
     * @throws SIGException 
     */
    public abstract void sendProcess(Document doc) throws SIGException;

    /**
     * Returns the adapter that is connected to this port.
     * @return 
     */
    public Adapter getAdapter() {
        return adapter;
    }
}

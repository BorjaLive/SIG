package com.b0ve.sig.adapters.mem;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.utils.Process;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Sends a message periodically FORMAT:
 * <pre>
 * {@code
 * <clock>
 *   <epoch />
 *   <epoch-millis />
 *   <iso />
 * </clock>
 * }
 * </pre>
 *
 * @author borja
 */
public class AdapterClock extends Adapter {

    private final Thread thread;

    public AdapterClock(long millis) throws SIGException {
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        try {
                            Instant instant = Instant.now();
                            sendProcess("<clock>"
                                    + "<epoch>"+(instant.toEpochMilli()/1000)+"</epoch>"
                                    + "<epoch-millis>"+instant.toEpochMilli()+"</epoch-millis>"
                                    + "<iso>"+instant.toString()+"</iso>"
                                    + "</clock>");
                        } catch (SIGException ex) {
                            handleException(ex);
                        }
                        sleep(millis);
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(AdapterClock.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
    }

    @Override
    public void iniciate() throws SIGException {
        super.iniciate();
        thread.start();
    }
    
    @Override
    public void halt() throws SIGException {
        super.halt();
        thread.interrupt();
    }


    @Override
    public Process.PORTS getCompatiblePortType() {
        return Process.PORTS.OUTPUT;
    }

}

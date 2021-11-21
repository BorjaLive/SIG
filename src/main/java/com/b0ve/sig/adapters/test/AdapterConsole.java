package com.b0ve.sig.adapters.test;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.utils.Process.PORTS;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.Scanner;

/**
 * Adaptor that introduces messages with the text from the command line. The
 * text is unchanged
 *
 * @author borja
 */
public class AdapterConsole extends Adapter {

    private final Thread hilo;

    public AdapterConsole() {
        hilo = new Thread() {
            @Override
            public void run() {
                Scanner es = new Scanner(System.in);
                String texto = null;
                do {
                    texto = es.nextLine();
                    try {
                        sendProcess(texto);
                    } catch (SIGException ex) {
                        handleException(ex);
                    }
                } while (!texto.equals("exit") && !isInterrupted());
            }
        };
    }

    @Override
    public void iniciate() {
        hilo.start();
    }

    @Override
    public void halt() {
        hilo.interrupt();
    }

    @Override
    public PORTS getCompatiblePortType() {
        return PORTS.INPUT;
    }
}

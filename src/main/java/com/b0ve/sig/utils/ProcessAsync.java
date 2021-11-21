package com.b0ve.sig.utils;

import com.b0ve.sig.ports.Port;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessAsync extends Process {

    private final List<Thread> threads;

    public ProcessAsync(boolean debug) {
        super(debug);
        this.threads = new ArrayList<>();
    }

    public ProcessAsync() {
        this(false);
    }

    @Override
    public void execute() {
        for (Task tarea : tasks) {
            Thread hilo = new Thread(tarea);
            threads.add(hilo);
            hilo.start();
            if (tarea instanceof Port) {
                try {
                    ((Port) tarea).getAdapter().iniciate();
                } catch (SIGException ex) {
                    handleException(ex);
                }
            }
        }
    }

    @Override
    public void waitToEnd() throws InterruptedException {
        for (Thread hilo : threads) {
            hilo.join();
        }
    }

    @Override
    public void shutdown() {
        for (Thread hilo : threads) {
            hilo.interrupt();
        }
        for (Task tarea : tasks) {
            if (tarea instanceof Port) {
                try {
                    ((Port) tarea).getAdapter().halt();
                } catch (SIGException ex) {
                    handleException(ex);
                }
            }
        }
    }

}

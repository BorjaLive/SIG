package com.b0ve.sig.utils;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.ports.Port;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessSync extends Process {

    private Thread ejecucion;

    public ProcessSync(boolean debug) {
        super(debug);
    }

    public ProcessSync() {
        this(false);
    }

    @Override
    public void execute() {
        for (Task tarea : tasks) {
            if (tarea instanceof Port) {
                try {
                    ((Port) tarea).getAdapter().iniciate();
                } catch (SIGException ex) {
                    handleException(ex);
                }
            }
        }
        ejecucion = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    for (Task task : tasks) {
                        try {
                            boolean hasMessages = false;
                            ListIterator<Buffer> iter = task.inputs();
                            while (!hasMessages && iter.hasNext()) {
                                if (!iter.next().empty()) {
                                    hasMessages = true;
                                }
                            }

                            if (hasMessages) {
                                task.process();
                            }
                        } catch (SIGException ex) {
                            handleException(ex);
                        }
                    }
                }
            }

        };
        ejecucion.start();
    }

    @Override
    public void shutdown() {
        ejecucion.interrupt();
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

    @Override
    public void waitToEnd() throws InterruptedException {
        ejecucion.join();
    }

}

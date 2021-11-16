package com.b0ve.sig.utils;

import com.b0ve.sig.ports.Port;
import com.b0ve.sig.tasks.Task;
import java.util.ArrayList;
import java.util.List;

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
                ((Port) tarea).getAdapter().iniciate();
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
                ((Port) tarea).getAdapter().halt();
            }
        }
    }

}

package com.b0ve.sig.adapters.basic;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.utils.Process;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.io.File;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple adapter that sends a message to the process for each file that it
 * finds in the directory specified in the constructor. The file is deleted once
 * its readden.
 *
 * @author borja
 */
public class AdapterDirWhatcher extends Adapter {

    private Thread watcher;

    public AdapterDirWhatcher(String watchDir) {
        if (watchDir != null) {
            File folder = new File(watchDir);
            watcher = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            for (final File fileEntry : folder.listFiles()) {
                                if (fileEntry.isFile()) {
                                    sendProcess(new String(Files.readAllBytes(fileEntry.toPath()), StandardCharsets.UTF_8));
                                    fileEntry.delete();
                                }
                            }
                            sleep(1000);
                        }
                    } catch (IOException ex) {
                        handleException(new SIGException("Error reading file", null, ex));
                    } catch (InterruptedException ex) {
                    } catch (SIGException ex) {
                        handleException(ex);
                    }
                }
            };
        }
    }

    @Override
    public void iniciate() throws SIGException {
        super.iniciate();
        watcher.start();
    }

    @Override
    public void halt() throws SIGException {
        super.halt();
        if (watcher != null) {
            watcher.interrupt();
        }
    }

    @Override
    public Process.PORTS getCompatiblePortType() {
        return Process.PORTS.INPUT;
    }
}

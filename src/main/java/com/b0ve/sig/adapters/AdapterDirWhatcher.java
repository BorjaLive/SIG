package com.b0ve.sig.adapters;

import com.b0ve.sig.utils.Process;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * Simple adapter that sends a message to the process for each file that it
 * finds in the directory specified in the constructor. The file is deleted once
 * its readden.
 *
 * @author borja
 */
public class AdapterDirWhatcher extends Adapter {

    private final Thread watcher;

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
                    } catch (SIGException ex) {
                        handleException(ex);
                    } catch (IOException ex) {
                        handleException(new SIGException("Error reading file", null, ex));
                    } catch (InterruptedException ex) {
                    }
                }
            };
        } else {
            watcher = null;
        }
    }

    @Override
    public void halt() {
        if (watcher != null) {
            watcher.interrupt();
        }
    }

    @Override
    public void iniciate() {
        if (watcher != null) {
            watcher.start();
        }
    }

    @Override
    public Process.PORTS getCompatiblePortType() {
        return Process.PORTS.INPUT;
    }
}

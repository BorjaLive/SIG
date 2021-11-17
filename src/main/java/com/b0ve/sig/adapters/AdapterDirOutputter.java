package com.b0ve.sig.adapters;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.Process;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;

/**
 * Simple adapter that creates a file for each message received. Directory is specified in the constructor, filename is message id, file content is entire message body as XML.
 * @author borja
 */
public class AdapterDirOutputter extends Adapter {

    private final String destdir;

    public AdapterDirOutputter(String destdir) {
        this.destdir = destdir;
    }

    @Override
    public Document sendApp(Message m) {
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(destdir + "/" + m.getID() + ".xml");
            myWriter.write(m.getBodyString());
            myWriter.close();
        } catch (IOException ex) {
            handleException(new SIGException("Error creating file in directory", destdir, ex));
        }
        return null;
    }

    @Override
    public Process.PORTS getCompatiblePortType() {
        return Process.PORTS.OUTPUT;
    }

}

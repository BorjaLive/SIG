package com.b0ve.sig.adapters.basic;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.utils.Process;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.io.FileWriter;
import java.io.IOException;
import org.w3c.dom.Document;

/**
 * Simple adapter that creates a file for each message received. Directory is
 * specified in the constructor, filename is message id, file content is entire
 * message body as XML.
 *
 * @author borja
 */
public class AdapterDirOutputter extends Adapter {

    private final String destdir;
    private int counter;

    public AdapterDirOutputter(String destdir) {
        this.destdir = destdir;
        counter = 0;
    }

    @Override
    public Document sendApp(Document doc) throws SIGException {
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(destdir + "/" + counter + ".xml");
            myWriter.write(XMLUtils.serialize(doc));
            myWriter.close();
        } catch (IOException ex) {
            throw new SIGException("Error creating file in directory", destdir, ex);
        }
        counter++;
        return null;
    }

    @Override
    public Process.PORTS getCompatiblePortType() {
        return Process.PORTS.OUTPUT;
    }

}

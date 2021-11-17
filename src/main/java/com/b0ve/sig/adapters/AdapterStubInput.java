package com.b0ve.sig.adapters;

import com.b0ve.sig.utils.exceptions.SIGException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AdapterStubInput extends Adapter {

    public void sendText(String xml) throws SIGException {
        sendProcess(xml);
    }

    public void sendFile(String path) throws SIGException {
        try {
            sendProcess(Files.readString(Paths.get(path)));
        } catch (IOException ex) {
            throw new SIGException("Could not read the file", path, ex);
        }
    }

    @Override
    public com.b0ve.sig.utils.Process.PORTS getCompatiblePortType() {
        return com.b0ve.sig.utils.Process.PORTS.INPUT;
    }

}

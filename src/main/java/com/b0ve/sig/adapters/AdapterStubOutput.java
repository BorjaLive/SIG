package com.b0ve.sig.adapters;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.ArrayList;
import org.w3c.dom.Document;

public class AdapterStubOutput extends Adapter {

    private final ArrayList<Document> messages;

    public AdapterStubOutput() {
        messages = new ArrayList<>();
    }

    public Document[] getMessages() {
        Document[] data = messages.toArray(new Document[0]);
        messages.clear();
        return data;
    }

    @Override
    public Document sendApp(Document doc) throws SIGException {
        messages.add(doc);
        return null;
    }

    @Override
    public com.b0ve.sig.utils.Process.PORTS getCompatiblePortType() {
        return com.b0ve.sig.utils.Process.PORTS.OUTPUT;
    }
}

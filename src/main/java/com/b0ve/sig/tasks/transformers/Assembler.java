package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Merges the content of fragments received from different inputs and sends the complete message. Roots are not preserved, it can be configured in the constructor.
 * @author borja
 */
public final class Assembler extends AssemblerTemplate {

    private final Object rootName;

    public Assembler(Object rootName) {
        super();
        this.rootName = rootName;
    }

    @Override
    protected Document join(Message[] messages) throws SIGException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element appendPoint = null;
            if (rootName instanceof String) {
                appendPoint = doc.createElement((String) rootName);
                doc.appendChild(appendPoint);
            } else if (rootName instanceof String[]) {
                String[] rootNames = (String[]) rootName;
                for (String name : rootNames) {
                    Element newPoint = doc.createElement(name);
                    if (appendPoint == null) {
                        doc.appendChild(newPoint);
                    } else {
                        appendPoint.appendChild(newPoint);
                    }
                    appendPoint = newPoint;
                }
            } else {
                appendPoint = doc.createElement("list");
                doc.appendChild(appendPoint);
            }
            for (Message message : messages) {
                Node newChild = Message.document2node(message.getBody());
                Node imported = doc.importNode(newChild, true);
                appendPoint.appendChild(imported);
            }
            return doc;
        } catch (ParserConfigurationException ex) {
            throw new SIGException("Messages could not be combined", messages, ex);
        }
    }

}

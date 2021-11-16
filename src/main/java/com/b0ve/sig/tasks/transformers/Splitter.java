package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Divides the content of a message with an XPath expression.
 * @author borja
 */
public final class Splitter extends SplitterTemplate {

    private final String xpath;

    public Splitter(String xpath) {
        super();
        this.xpath = xpath;
    }

    @Override
    protected Document[] split(Message m) throws SIGException {
        NodeList lista = m.evaluateXPath(xpath);
        Document[] partes = new Document[lista.getLength()];
        for (int i = 0; i < lista.getLength(); i++) {
            partes[i] = Message.node2document(lista.item(i));
        }
        return partes;
    }

}

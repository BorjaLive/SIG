package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLTools;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.xpath.XPathExpression;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Dives the content of a message with an XPath expressiuon and outputs individual messages to an output each.
 * @author borja
 */
public final class Chopper extends ChopperTemplate {

    private final XPathExpression xpath;

    public Chopper(String xpath) throws SIGException {
        this.xpath = XMLTools.compile(xpath);
    }

    @Override
    protected Document[] chop(Message mensaje) throws SIGException {
        NodeList lista = mensaje.eval(xpath);
        Document[] partes = new Document[lista.getLength()];
        for (int i = 0; i < lista.getLength(); i++) {
            partes[i] = XMLTools.node2document(lista.item(i));
        }
        return partes;
    }

}

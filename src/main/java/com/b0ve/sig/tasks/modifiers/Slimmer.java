package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Removes all the nodes selected by an array of XPath expressions
 * @author borja
 */
public class Slimmer extends SlimmerTemplate {

    private final String[] xpaths;

    public Slimmer(String[] xpaths) {
        this.xpaths = xpaths;
    }

    @Override
    protected void slim(Message m) throws SIGException {
        Document doc = m.getBody();
        for (String xpath : xpaths) {
            NodeList nodes = Message.evaluateXPath(doc, xpath);
            for (int i = 0; i < nodes.getLength(); i++) {
                nodes.item(i).getParentNode().removeChild(nodes.item(i));
            }
        }
    }

}

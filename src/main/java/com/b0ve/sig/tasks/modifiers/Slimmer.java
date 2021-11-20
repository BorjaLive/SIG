package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLTools;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.xpath.XPathExpression;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Removes all the nodes selected by an array of XPath expressions
 *
 * @author borja
 */
public class Slimmer extends SlimmerTemplate {

    private final XPathExpression[] xpaths;

    public Slimmer(String[] xpaths) throws SIGException {
        this.xpaths = new XPathExpression[xpaths.length];
        for (int i = 0; i < xpaths.length; i++) {
            this.xpaths[i] = XMLTools.compile(xpaths[i]);
        }
    }

    @Override
    protected void slim(Message m) throws SIGException {
        Document doc = m.getBody();
        for (XPathExpression xpath : xpaths) {
            NodeList nodes = XMLTools.eval(doc, xpath);
            for (int i = 0; i < nodes.getLength(); i++) {
                nodes.item(i).getParentNode().removeChild(nodes.item(i));
            }
        }
    }

}

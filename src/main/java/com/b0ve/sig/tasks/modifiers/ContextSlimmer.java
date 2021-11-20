package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLTools;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.xpath.XPathExpression;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Removes all the nodes selected by a list of XPath expressions. FORMAT:
 * <pre>
 * {@code
 * <list>
 *  <item> XPath Expression 1 </item>
 *  <item> XPath Expression 2 </item>
 * ...
 * </list>
 * }
 * </pre>
 *
 * @author borja
 */
public class ContextSlimmer extends ContextSlimmerTemplate {

    XPathExpression divisor;

    public ContextSlimmer(String divisor) throws SIGException {
        this.divisor = XMLTools.compile(divisor);
    }

    public ContextSlimmer() throws SIGException {
        this("/list/item");
    }

    @Override
    protected void slim(Message m, Message condition) throws SIGException {
        Document doc = m.getBody();
        NodeList conditions = condition.eval(divisor);
        for (int j = 0; j < conditions.getLength(); j++) {
            NodeList nodes = XMLTools.eval(doc, conditions.item(j).getTextContent());
            for (int i = 0; i < nodes.getLength(); i++) {
                nodes.item(i).getParentNode().removeChild(nodes.item(i));
            }
        }
    }

}

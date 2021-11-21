package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.xpath.XPathExpression;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Divides the content of a message with an XPath expression.
 *
 * @author borja
 */
public final class Splitter extends SplitterTemplate {

    private final XPathExpression xpath;

    public Splitter(String xpath) throws SIGException {
        this.xpath = XMLUtils.compile(xpath);
    }

    @Override
    protected Document[] split(Message mensaje) throws SIGException {
        return XMLUtils.split(mensaje, xpath);
    }

}

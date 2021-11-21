package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.xpath.XPathExpression;

/**
 *
 * @author borja
 */
public class Splitter extends SplitterTemplate {

    private final XPathExpression xpath;

    public Splitter(String xpath) throws SIGException {
        super();
        this.xpath = XMLUtils.compile(xpath);
    }

    @Override
    protected Message[] split(Message message) throws SIGException {
        return XMLUtils.split(message, xpath);
    }
    
}

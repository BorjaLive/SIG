package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.xpath.XPathExpression;

/**
 *
 * @author borja
 */
public class Chopper extends ChopperTemplate{

    private final XPathExpression xpath;

    public Chopper(String xpath) throws SIGException {
        super();
        this.xpath = XMLUtils.compile(xpath);
    }

    @Override
    protected Message[] chop(Message mensaje) throws SIGException {
        return XMLUtils.split(mensaje, xpath);
    }
    
}

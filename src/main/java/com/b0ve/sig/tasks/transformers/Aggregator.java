package com.b0ve.sig.tasks.transformers;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Collects fragments from a single input and composes the original message.
 * Root can be configured.
 *
 * @author borja
 */
public final class Aggregator extends AggregatorTemplate {

    private final Object rootName;

    public Aggregator(Object rootName) {
        super();
        this.rootName = rootName;
    }

    @Override
    protected Document join(Message[] messages) throws SIGException {
        return XMLUtils.join(messages, rootName);
    }

}

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
 * Merges the content of fragments received from different inputs and sends the
 * complete message. Roots are not preserved, it can be configured in the
 * constructor.
 *
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
        return XMLUtils.join(messages, rootName);
    }

}

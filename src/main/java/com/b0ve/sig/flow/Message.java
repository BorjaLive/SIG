package com.b0ve.sig.flow;

import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.Iterator;
import java.util.Stack;
import java.util.UUID;
import javax.xml.xpath.XPathExpression;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class Message {

    private final UUID ID;
    private UUID correlationID;
    private final Stack<FragmentInfo> fragmentInfo;
    private Document body;

    private Message(UUID ID, UUID correlationID, Document body) throws SIGException {
        this.ID = ID;
        this.correlationID = correlationID;
        this.body = XMLUtils.clone(body);
        this.fragmentInfo = new Stack<>();
    }

    public Message(Document body) throws SIGException {
        this(UUID.randomUUID(), UUID.randomUUID(), body);
    }

    public Message(String body) throws SIGException {
        this(XMLUtils.parse(body));
    }

    public Message(Message m) throws SIGException {
        this(UUID.randomUUID(), m.correlationID, m.body);
        addFragmentInfo(m.getFragmentInfoStack());
    }

    /**
     * Returns ID of the message
     *
     * @return
     */
    public UUID getID() {
        return ID;
    }

    /**
     * Returns the correlation ID of the message
     *
     * @return
     */
    public UUID getCorrelationID() {
        return correlationID;
    }

    /**
     * Returns the body as document
     *
     * @return
     */
    public Document getBody() {
        return body;
    }

    /**
     * Returns the body as XML String
     *
     * @return
     */
    public String getBodyString() {
        return XMLUtils.serialize(body);
    }

    /**
     * Returns the topmost fragment info. Null if there is message is not a
     * fragment.
     *
     * @return
     */
    public FragmentInfo getFragmentInfo() {
        return fragmentInfo.isEmpty() ? null : fragmentInfo.peek();
    }

    /**
     * Returns topmost fragment id
     *
     * @return
     */
    public UUID getFragmentID() {
        return fragmentInfo.isEmpty() ? null : fragmentInfo.peek().getFragmentID();
    }

    /**
     * Returns topmost fragment size
     *
     * @return
     */
    public long getFragmentSize() {
        return fragmentInfo.isEmpty() ? -1 : fragmentInfo.peek().getFragmentSize();
    }

    /**
     * Returns topmost original document
     *
     * @return
     */
    public Document getFragmentOriginalDocument() {
        return fragmentInfo.isEmpty() ? null : fragmentInfo.peek().getOriginalDocument();
    }

    /**
     * Returns topmost original parent node
     *
     * @return
     */
    public Node getFragmentOriginalFather() {
        return fragmentInfo.isEmpty() ? null : fragmentInfo.peek().getOriginalFather();
    }

    /**
     * Returns an iterator to walk over all the fragment info.
     *
     * @return
     */
    public Iterator<FragmentInfo> getFragmentInfoStack() {
        return fragmentInfo.iterator();
    }

    /**
     * Sets the body of the message. Document is passed by reference, be
     * carefull.
     *
     * @param body
     */
    public void setBody(Document body) {
        this.body = body;
    }

    /**
     * Sets correlation ID
     *
     * @param correlationID
     */
    public void setCorrelationID(UUID correlationID) {
        this.correlationID = correlationID;
    }

    /**
     * Adds a layer to the fragment info
     *
     * @param finfo
     */
    public void addFragmentInfo(FragmentInfo finfo) {
        fragmentInfo.push(finfo);
    }

    /**
     * Adds a list of fragment info in the same order
     *
     * @param finfo
     */
    public void addFragmentInfo(Iterator<FragmentInfo> finfo) {
        for (; finfo.hasNext();) {
            fragmentInfo.push(finfo.next());
        }

    }

    /**
     * Removes topmost fragment info
     *
     * @return
     */
    public FragmentInfo removeFragmentInfo() {
        return fragmentInfo.isEmpty() ? null : fragmentInfo.pop();
    }

    /**
     * Returns true if the message is fragmented
     *
     * @return
     */
    public boolean isFragment() {
        return !fragmentInfo.isEmpty();
    }

    /**
     * Returns a nodelist of tags that matches the XPath expression, applied to
     * the body.
     *
     * @param expresion XPath expression
     * @return
     * @throws SIGException
     */
    public NodeList eval(String expresion) throws SIGException {
        return XMLUtils.eval(body, expresion);
    }

    /**
     * Returns the text content of all the nodes selected by the XPath
     * expression, applied to the body.
     *
     * @param expresion XPath expression
     * @return
     * @throws SIGException
     */
    public String evalString(String expresion) throws SIGException {
        return XMLUtils.evalString(body, expresion);
    }

    /**
     * Returns the text content of all the nodes selected by the XPath
     * expression, applied to the body with a default value.2
     *
     * @param expresion XPath expression
     * @param def Default value if no match is found
     * @return
     * @throws SIGException
     */
    public String evalString(String expresion, String def) throws SIGException {
        return XMLUtils.evalString(body, expresion, def);
    }

    /**
     * Returns a nodelist of tags that matches the precompiled XPath expression,
     * applied to the body.
     *
     * @param expresion XPath expression
     * @return
     * @throws SIGException
     */
    public NodeList eval(XPathExpression expresion) throws SIGException {
        return XMLUtils.eval(body, expresion);
    }

    /**
     * Returns the text content of all the nodes selected by the precompiled
     * XPath expression, applied to the body.
     *
     * @param expresion XPath expression
     * @return
     * @throws SIGException
     */
    public String evalString(XPathExpression expresion) throws SIGException {
        return XMLUtils.evalString(body, expresion);
    }

    /**
     * Returns the text content of all the nodes selected by the precompiled
     * XPath expression, applied to the body with a default value.
     *
     * @param expresion XPath expression
     * @param def Default value if no match is found
     * @return
     * @throws SIGException
     */
    public String evalString(XPathExpression expresion, String def) throws SIGException {
        return XMLUtils.evalString(body, expresion, def);
    }

    /**
     * Applies a XSLTransformation to the body of the message, changes are
     * stored in same message.
     *
     * @param style XSLT
     * @throws SIGException
     */
    public void transformBody(String style) throws SIGException {
        body = XMLUtils.transform(body, style);
    }

    @Override
    public String toString() {
        return "Mensaje{" + "ID=" + ID + ", correlationID=" + correlationID + ", fragmentInfo=" + fragmentInfo + ", body=" + XMLUtils.serialize(body) + '}';
    }

    /**
     * Creates a new message with custom ID and Correlation ID. For debugging
     * and testing purposes.
     *
     * @param id Long
     * @param correlationID Long
     * @param body XML String
     * @return
     */
    public static Message newMessage(UUID id, UUID correlationID, String body) {
        try {
            return new Message(id, correlationID, XMLUtils.parse(body));
        } catch (SIGException ex) {
            return null;
        }
    }

    /**
     * Creates a new message with custom ID and Correlation ID. For debugging
     * and testing purposes.
     *
     * @param body XML String
     * @return
     */
    public static Message newMessage(String body) {
        return newMessage(UUID.randomUUID(), UUID.randomUUID(), body);
    }
}

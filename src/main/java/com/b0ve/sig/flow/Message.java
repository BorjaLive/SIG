package com.b0ve.sig.flow;

import com.b0ve.sig.utils.XMLTools;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.Iterator;
import java.util.Stack;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import static com.b0ve.sig.utils.XMLTools.*;
import javax.xml.xpath.XPathExpression;

public final class Message {

    private static int counter = 0;

    private final long ID;
    private long correlationID;
    private final Stack<FragmentInfo> fragmentInfo;
    private Document body;

    private Message(long ID, long correlationID, Document body) throws SIGException {
        this.ID = ID;
        this.correlationID = correlationID;
        this.body = XMLTools.clone(body);
        this.fragmentInfo = new Stack<>();
    }

    public Message(Document body) throws SIGException {
        this(counter, counter, body);
        counter++;
    }

    public Message(String body) throws SIGException {
        this(parse(body));
    }

    public Message(Message m) throws SIGException {
        this(counter++, m.correlationID, m.body);
        addFragmentInfo(m.getFragmentInfoStack());
    }

    /**
     * Returns ID of the message
     *
     * @return
     */
    public long getID() {
        return ID;
    }

    /**
     * Returns the correlation ID of the message
     *
     * @return
     */
    public long getCorrelationID() {
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
        return serialize(body);
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
    public long getFragmentID() {
        return fragmentInfo.isEmpty() ? -1 : fragmentInfo.peek().getFragmentID();
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
    public void setCorrelationID(long correlationID) {
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
        return XMLTools.eval(body, expresion);
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
        return XMLTools.evalString(body, expresion);
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
        return XMLTools.evalString(body, expresion, def);
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
        return XMLTools.eval(body, expresion);
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
        return XMLTools.evalString(body, expresion);
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
        return XMLTools.evalString(body, expresion, def);
    }

    /**
     * Applies a XSLTransformation to the body of the message, changes are
     * stored in same message.
     *
     * @param style XSLT
     * @throws SIGException
     */
    public void transformBody(String style) throws SIGException {
        body = transform(body, style);
    }

    @Override
    public String toString() {
        return "Mensaje{" + "ID=" + ID + ", correlationID=" + correlationID + ", fragmentInfo=" + fragmentInfo + ", body=" + serialize(body) + '}';
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
    public static Message newMessage(long id, int correlationID, String body) {
        try {
            return new Message(id, correlationID, parse(body));
        } catch (SIGException ex) {
            return null;
        }
    }
}

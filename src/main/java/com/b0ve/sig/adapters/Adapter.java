package com.b0ve.sig.adapters;

import com.b0ve.sig.ports.Port;
import com.b0ve.sig.ports.PortInput;
import com.b0ve.sig.utils.Process.PORTS;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import org.w3c.dom.Document;

/**
 * Parent class for all adaptors
 *
 * @author borja
 */
public abstract class Adapter {

    private Port port;
    private boolean running;

    /**
     * Stablishes the port the adapter is connected to.
     *
     * @param port
     */
    public void setPort(Port port) {
        this.port = port;
    }

    /**
     * Sends a message to the app.
     *
     * @param doc Document to send
     * @return A document with the response of the app. Value will be null if
     * application does not need to respond
     * @throws SIGException
     */
    public Document sendApp(Document doc) throws SIGException {
        throw new SIGException("This port does not support sending direct messages to the app", null, null);
    }

    /**
     * Sends a document to the port.
     *
     * @param doc The body of the message
     */
    protected void sendProcess(Document doc) {
        if (port != null && port instanceof PortInput) {
            ((PortInput) port).sendProcess(doc);
        }
    }

    /**
     * Sends a document to the port.
     *
     * @param xml The body of the msasage as a string
     * @throws SIGException
     */
    protected void sendProcess(String xml) throws SIGException {
        Adapter.this.sendProcess(XMLUtils.parse(xml));
    }

    /**
     * Sets up and starts the adapter
     */
    public void iniciate() throws SIGException {
        running = true;
    }

    /**
     * Stops the adapter if it has running threads
     */
    public void halt() throws SIGException {
        running = false;
    }

    /**
     * Returns true if the process is active
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Returns the type of port that the adapter needs.
     *
     * @return
     */
    public abstract PORTS getCompatiblePortType();

    /**
     * Sends an exception to the Exception Handle
     *
     * @param exception
     */
    protected void handleException(SIGException exception) {
        if (port != null) {
            port.handleException(exception);
        }
    }
}

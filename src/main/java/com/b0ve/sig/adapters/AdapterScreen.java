package com.b0ve.sig.adapters;

import com.b0ve.sig.utils.Process.PORTS;
import com.b0ve.sig.utils.XMLTools;
import javax.swing.JOptionPane;
import org.w3c.dom.Document;

/**
 * Shows the body of the messages received on the screen The text is unchanged
 *
 * @author borja
 */
public class AdapterScreen extends Adapter {

    @Override
    public Document sendApp(Document doc) {
        JOptionPane.showMessageDialog(null, XMLTools.serialize(doc), "Message", JOptionPane.INFORMATION_MESSAGE);
        return null;
    }

    @Override
    public PORTS getCompatiblePortType() {
        return PORTS.OUTPUT;
    }

}

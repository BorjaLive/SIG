package com.b0ve.sig.adapters;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.Process;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.HashSet;
import java.util.Set;
import org.w3c.dom.Document;

/**
 * Memmory (HashSet) for the process, 
 * FORMAT:
 * <pre>
 * {@code
 * <query>
 *  <action> {create | delete} </action>
 *  <value> VALUE TO STORE OR DELETE </value>
 * </query>
 * }
 * </pre>
 * RESPONSE: True if the action is create and the value already exists or the action is delete and the value does not exist
 * <pre>
 * {@code
 * <response> {true | false} </response>
 * }
 * </pre>
 * @author borja
 */
public class AdapterSET extends Adapter {

    private final Set<String> set;

    public AdapterSET() {
        set = new HashSet<>();
    }

    @Override
    public Document sendApp(Message m) throws SIGException {
        try {
            String action = m.evaluateXPathString("/query/action");
            String value = m.evaluateXPathString("/query/value");
            boolean result = false;
            if (action.equals("create")) {
                result = set.contains(value);
                if (!result) {
                    set.add(value);
                }
            } else if (action.equals("delete")) {
                result = !set.contains(value);
                if (!result) {
                    set.remove(value);
                }
            }else{
                throw new SIGException("SET doesnt recognize the action", action, null);
            }
            System.out.println("Me preguntan por: " + action + " valor: " + value + " le digo que " + result);
            return Message.parseXML("<response>" + (result ? "true" : "false") + "</response>");
        } catch (SIGException ex) {
            handleException(ex);
        }
        return null;
    }

    @Override
    public Process.PORTS getCompatiblePortType() {
        return Process.PORTS.REQUEST;
    }

}

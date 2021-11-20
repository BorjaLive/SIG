/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import static com.b0ve.sig.flow.Message.newMessage;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author borja
 */
public class CorrelationIDSetterTest {

    @Test
    public void testCorrelationIDSetter1() throws SIGException {
        UUID id1 = UUID.randomUUID(), id2 = UUID.randomUUID(), id3 = UUID.randomUUID();

        Message m1 = newMessage(id1, null, "<m>1</m>");
        Message m2 = newMessage(id2, null, "<m>2</m>");
        Message m3 = newMessage(id3, null, "<m>3</m>");
        CorrelationIDSetter cidSetter = new CorrelationIDSetter();
        Buffer in = new Buffer(null, null);
        cidSetter.addInput(in);
        Buffer out = new Buffer(null, null);
        cidSetter.addOutput(out);

        in.push(m1);
        in.push(m2);
        in.push(m3);

        cidSetter.process();

        assertNotEquals(out.retrive().getCorrelationID(), id1);
        assertNotEquals(out.retrive().getCorrelationID(), id2);
        assertNotEquals(out.retrive().getCorrelationID(), id3);
    }

}

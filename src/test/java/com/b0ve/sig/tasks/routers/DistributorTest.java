/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import static com.b0ve.sig.flow.Message.newMessage;
import com.b0ve.sig.utils.condiciones.FilterConditionEquals;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author borja
 */
public class DistributorTest {

    @Test
    public void testDistributor1() throws SIGException {
        UUID id0 = UUID.randomUUID(), id1 = UUID.randomUUID(), id2 = UUID.randomUUID(), id3 = UUID.randomUUID(), id4 = UUID.randomUUID(), id5 = UUID.randomUUID();

        Distributor distributor = new Distributor(new FilterConditionEquals[]{new FilterConditionEquals("/cid", "0"), new FilterConditionEquals("/cid", "1")});
        Message m1 = newMessage(id0, id0, "<cid>0</cid>");
        Message m2 = newMessage(id1, id1, "<cid>1</cid>");

        Message m3 = newMessage(id2, id2, "<cid>err</cid>"),
                m4 = newMessage(id3, id3, "<cid>2</cid>"),
                m5 = newMessage(id4, id5, "<cid>0</cid>"),
                m6 = newMessage(id5, id4, "<cid>err</cid>");

        Buffer in1 = new Buffer(null, null);
        distributor.addInput(in1);
        Buffer out1 = new Buffer(null, null);
        Buffer out2 = new Buffer(null, null);
        Buffer out3 = new Buffer(null, null);
        distributor.addOutput(out1);
        distributor.addOutput(out2);
        distributor.addOutput(out3);

        in1.push(m1);
        in1.push(m2);
        in1.push(m3);
        in1.push(m4);
        in1.push(m5);
        in1.push(m6);

        distributor.process();

        assertEquals(out1.retrive().getID(), id0);
        assertEquals(out1.retrive().getID(), id4);
        assertNull(out1.retrive());
        assertEquals(out2.retrive().getID(), id1);
        assertNull(out2.retrive());
        assertEquals(out3.retrive().getID(), id2);
        assertEquals(out3.retrive().getID(), id3);
        assertEquals(out3.retrive().getID(), id5);
        assertNull(out3.retrive());
    }

}

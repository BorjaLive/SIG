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
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author borja
 */
public class DistributorTest {

    @Test
    public void testDistributor1() throws SIGException {
        Distributor distributor = new Distributor(new FilterConditionEquals[]{new FilterConditionEquals("/cid", "0"), new FilterConditionEquals("/cid", "1")});
        Message m1 = newMessage(0, 0, "<cid>0</cid>");
        Message m2 = newMessage(1, 1, "<cid>1</cid>");

        Message m3 = newMessage(2, 2, "<cid>err</cid>"),
                m4 = newMessage(3, 3, "<cid>2</cid>"),
                m5 = newMessage(4, 5, "<cid>0</cid>"),
                m6 = newMessage(5, 4, "<cid>err</cid>");

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

        assertEquals(out1.retrive().getID(), 0);
        assertEquals(out1.retrive().getID(), 4);
        assertNull(out1.retrive());
        assertEquals(out2.retrive().getID(), 1);
        assertNull(out2.retrive());
        assertEquals(out3.retrive().getID(), 2);
        assertEquals(out3.retrive().getID(), 3);
        assertEquals(out3.retrive().getID(), 5);
        assertNull(out3.retrive());
    }

}

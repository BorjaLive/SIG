/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.b0ve.sig.tasks.routers;

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
public class MergerTest {

    @Test
    public void testMerger1() throws SIGException {
        UUID id0 = UUID.randomUUID(),
                id1 = UUID.randomUUID(),
                id2 = UUID.randomUUID();

        Message m1 = newMessage(id0, id0, "<cid>0</cid>");
        Message m2 = newMessage(id1, id1, "<cid>1</cid>");
        Message m3 = newMessage(id2, id2, "<cid>2</cid>");

        Merger merger = new Merger();
        Buffer in1 = new Buffer(null, null);
        Buffer in2 = new Buffer(null, null);
        merger.addInput(in1);
        merger.addInput(in2);
        Buffer out1 = new Buffer(null, null);
        merger.addOutput(out1);

        in1.push(m1);
        in2.push(m2);
        in1.push(m3);

        merger.process();

        assertEquals(out1.retrive().getID(), id0);
        assertEquals(out1.retrive().getID(), id2);
        assertEquals(out1.retrive().getID(), id1);
        assertNull(out1.retrive());
    }

}

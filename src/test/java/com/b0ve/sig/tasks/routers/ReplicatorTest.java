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
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author borja
 */
public class ReplicatorTest {

    @Test
    public void testReplicator1() throws SIGException {
        Message m1 = newMessage("<cid>0</cid>");

        Replicator replicator = new Replicator();
        Buffer in1 = new Buffer(null, null);
        replicator.addInput(in1);
        Buffer out1 = new Buffer(null, null);
        Buffer out2 = new Buffer(null, null);
        replicator.addOutput(out1);
        replicator.addOutput(out2);

        in1.push(m1);

        replicator.process();

        assertEquals(out1.retrive().getCorrelationID(), out2.retrive().getCorrelationID());
    }

}

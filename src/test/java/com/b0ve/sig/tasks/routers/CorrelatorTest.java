/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.b0ve.sig.tasks.routers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import static com.b0ve.sig.flow.Message.newMessage;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author borja
 */
public class CorrelatorTest {

    @Test
    public void testCorrelator1() throws Exception {
        UUID id0 = UUID.randomUUID(),
                id1 = UUID.randomUUID(),
                id2 = UUID.randomUUID();

        Message m11 = newMessage(UUID.randomUUID(), id0, "<m>11</m>");
        Message m12 = newMessage(UUID.randomUUID(), id1, "<m>12</m>");
        Message m13 = newMessage(UUID.randomUUID(), id2, "<m>13</m>"),
                m21 = newMessage(UUID.randomUUID(), id2, "<m>21</m>"),
                m22 = newMessage(UUID.randomUUID(), id1, "<m>22</m>"),
                m23 = newMessage(UUID.randomUUID(), id0, "<m>23</m>");
        Correlator correlator = new Correlator();
        Buffer in1 = new Buffer(null, null);
        Buffer in2 = new Buffer(null, null);
        correlator.addInput(in1);
        correlator.addInput(in2);
        Buffer out1 = new Buffer(null, null);
        Buffer out2 = new Buffer(null, null);
        correlator.addOutput(out1);
        correlator.addOutput(out2);

        in1.push(m11);
        in1.push(m12);
        in1.push(m13);
        in2.push(m21);
        in2.push(m22);
        in2.push(m23);

        correlator.process();

        assertEquals(out1.retrive().getCorrelationID(), out2.retrive().getCorrelationID());
        assertEquals(out1.retrive().getCorrelationID(), out2.retrive().getCorrelationID());
        assertEquals(out1.retrive().getCorrelationID(), out2.retrive().getCorrelationID());
    }

    @Test
    public void testCorrelator2() throws Exception {
        Correlator correlator = new Correlator("/cid");
        Message m11 = newMessage("<cid>0</cid>");
        Message m12 = newMessage("<cid>1</cid>");

        Message m13 = newMessage("<cid>2</cid>"),
                m21 = newMessage("<cid>2</cid>"),
                m22 = newMessage("<cid>1</cid>"),
                m23 = newMessage("<cid>0</cid>");

        Buffer in1 = new Buffer(null, null);
        Buffer in2 = new Buffer(null, null);
        correlator.addInput(in1);
        correlator.addInput(in2);
        Buffer out1 = new Buffer(null, null);
        Buffer out2 = new Buffer(null, null);
        correlator.addOutput(out1);
        correlator.addOutput(out2);

        in1.push(m11);
        in1.push(m12);
        in1.push(m13);
        in2.push(m21);
        in2.push(m22);
        in2.push(m23);

        correlator.process();

        assertEquals(out1.retrive().evalString("/cid"), out2.retrive().evalString("/cid"));
        assertEquals(out1.retrive().evalString("/cid"), out2.retrive().evalString("/cid"));
        assertEquals(out1.retrive().evalString("/cid"), out2.retrive().evalString("/cid"));
    }

}

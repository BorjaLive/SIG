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
public class FilterTest {

    @Test
    public void tetFilter1() throws SIGException {
        UUID id0 = UUID.randomUUID(),
                id1 = UUID.randomUUID(),
                id2 = UUID.randomUUID();

        Message m1 = newMessage(id0, id0, "<cid>0</cid>");
        Message m2 = newMessage(id1, id1, "<cid>1</cid>");
        Message m3 = newMessage(id2, id2, "<cid>2</cid>");

        Filter filter = new Filter(new FilterConditionEquals("cid", "1"));
        Buffer in = new Buffer(null, null);
        filter.addInput(in);
        Buffer out = new Buffer(null, null);
        filter.addOutput(out);

        in.push(m1);
        in.push(m2);
        in.push(m3);

        filter.process();

        assertEquals(out.retrive().getID(), id1);
        assertNull(out.retrive());
    }

    @Test
    public void tetFilter2() throws SIGException {
        UUID id0 = UUID.randomUUID(), id1 = UUID.randomUUID(), id2 = UUID.randomUUID();

        Message m1 = newMessage(id0, id0, "<cid>0</cid>");
        Message m2 = newMessage(id1, id1, "<cid>1</cid>");
        Message m3 = newMessage(id2, id2, "<cid>2</cid>");

        Filter filter = new Filter((mensaje) -> {
            return Integer.parseInt(mensaje.evalString("/cid")) >= 1;
        });
        Buffer in = new Buffer(null, null);
        filter.addInput(in);
        Buffer out = new Buffer(null, null);
        filter.addOutput(out);

        in.push(m1);
        in.push(m2);
        in.push(m3);

        filter.process();

        assertEquals(out.retrive().getID(), id1);
        assertEquals(out.retrive().getID(), id2);
        assertNull(out.retrive());
    }

}

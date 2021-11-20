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
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author borja
 */
public class EnricherTest {

    @Test
    public void testEnricher1() throws SIGException {
        Message m = newMessage("<pelicula><titulo>Crimen Ferpecto</titulo></pelicula>");
        Enricher enricher = new Enricher("<pelicula><precio>10.5</precio></pelicula>");
        Buffer in = new Buffer(null, null);
        enricher.addInput(in);
        Buffer out = new Buffer(null, null);
        enricher.addOutput(out);

        in.push(m);

        enricher.process();

        Message response = out.retrive();
        assertNotNull(response);
        assertTrue(response.evalString("/pelicula/titulo").contains("Crimen Ferpecto"));
        assertTrue(response.evalString("/pelicula/precio").contains("10.5"));
    }

}

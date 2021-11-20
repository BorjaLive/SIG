/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.b0ve.sig.tasks.transformers;

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
public class ChopperTest {

    @Test
    public void testChopper1() throws SIGException {
        Message m1 = newMessage("<libros>\n"
                + "	<libro>\n"
                + "		<titulo>Robotica Vision y Control</titulo>\n"
                + "		<precio>70</precio>\n"
                + "	</libro>\n"
                + "	<libro>\n"
                + "		<titulo>Interspecies Reviewers</titulo>\n"
                + "		<precio>12.5</precio>\n"
                + "	</libro>\n"
                + "	<libro>\n"
                + "		<titulo>No lunch break</titulo>\n"
                + "		<precio>25</precio>\n"
                + "	</libro>\n"
                + "</libros>");
        Chopper chopper = new Chopper("/libros/libro");
        Buffer in = new Buffer(null, null);
        chopper.addInput(in);
        Buffer out1 = new Buffer(null, null);
        Buffer out2 = new Buffer(null, null);
        Buffer out3 = new Buffer(null, null);
        chopper.addOutput(out1);
        chopper.addOutput(out2);
        chopper.addOutput(out3);

        in.push(m1);

        chopper.process();

        assertTrue(out1.retrive().evalString("/libro").contains("Robotica Vision y Control"));
        assertTrue(out2.retrive().evalString("/libro").contains("Interspecies Reviewers"));
        assertTrue(out3.retrive().evalString("/libro").contains("No lunch break"));
    }

    @Test
    public void testChopper2() throws SIGException {
        Message m1 = newMessage("<a><b><c>b1c1</c><c>b1c2</c></b><b><c>b2c1</c><c>b2c2</c></b></a>");
        Chopper c1 = new Chopper("/a/b");
        Buffer in = new Buffer(null, null);
        c1.addInput(in);
        Buffer mid1 = new Buffer(null, null);
        Buffer mid2 = new Buffer(null, null);
        c1.addOutput(mid1);
        c1.addOutput(mid2);
        Chopper c2a = new Chopper("/b/c");
        Chopper c2b = new Chopper("/b/c");
        c2a.addInput(mid1);
        c2b.addInput(mid2);
        Buffer out1 = new Buffer(null, null);
        Buffer out2 = new Buffer(null, null);
        Buffer out3 = new Buffer(null, null);
        Buffer out4 = new Buffer(null, null);
        c2a.addOutput(out1);
        c2a.addOutput(out2);
        c2b.addOutput(out3);
        c2b.addOutput(out4);

        in.push(m1);

        c1.process();
        c2a.process();
        c2b.process();

        assertEquals(out1.retrive().evalString("/c"), "b1c1");
        assertEquals(out2.retrive().evalString("/c"), "b1c2");
        assertEquals(out3.retrive().evalString("/c"), "b2c1");
        assertEquals(out4.retrive().evalString("/c"), "b2c2");
    }

}

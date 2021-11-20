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
public class AssemblerTest {

    @Test
    public void testAssembler1() throws SIGException {
        Message m1 = newMessage(0, 0, "<libros>\n"
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
        Buffer mid1 = new Buffer(null, null);
        Buffer mid2 = new Buffer(null, null);
        Buffer mid3 = new Buffer(null, null);
        chopper.addOutput(mid1);
        chopper.addOutput(mid2);
        chopper.addOutput(mid3);

        Assembler assembler = new Assembler("coleccion");
        assembler.addInput(mid1);
        assembler.addInput(mid2);
        assembler.addInput(mid3);
        Buffer out = new Buffer(null, null);
        assembler.addOutput(out);

        in.push(m1);
        in.push(new Message(m1));

        chopper.process();
        mid2.retrive();
        assembler.process();

        assertEquals(out.retrive().eval("/coleccion/libro").getLength(), 3);
        assertTrue(out.empty());
        assertNotNull(mid1.retrive());
        assertNull(mid2.retrive());
        assertNotNull(mid3.retrive());
    }

    @Test
    public void testAssembler2() throws SIGException {
        Message m1 = newMessage(0, 0, "<a><b><c>b1c1</c><c>b1c2</c></b><b><c>b2c1</c><c>b2c2</c></b></a>");
        Chopper c1 = new Chopper("/a/b");
        Buffer in = new Buffer(null, null);
        c1.addInput(in);
        Buffer cmid1 = new Buffer(null, null);
        Buffer cmid2 = new Buffer(null, null);
        c1.addOutput(cmid1);
        c1.addOutput(cmid2);
        Chopper c2a = new Chopper("/b/c");
        Chopper c2b = new Chopper("/b/c");
        c2a.addInput(cmid1);
        c2b.addInput(cmid2);
        Buffer cout1 = new Buffer(null, null);
        Buffer cout2 = new Buffer(null, null);
        Buffer cout3 = new Buffer(null, null);
        Buffer cout4 = new Buffer(null, null);
        c2a.addOutput(cout1);
        c2a.addOutput(cout2);
        c2b.addOutput(cout3);
        c2b.addOutput(cout4);

        Assembler a1a = new Assembler("b");
        Assembler a1b = new Assembler("b");
        a1a.addInput(cout1);
        a1a.addInput(cout2);
        a1b.addInput(cout3);
        a1b.addInput(cout4);
        Buffer amid1 = new Buffer(null, null);
        Buffer amid2 = new Buffer(null, null);
        a1a.addOutput(amid1);
        a1b.addOutput(amid2);
        Assembler a2 = new Assembler("a");
        a2.addInput(amid1);
        a2.addInput(amid2);
        Buffer aout = new Buffer(null, null);
        a2.addOutput(aout);

        in.push(m1);
        in.push(new Message(m1));

        c1.process();
        c2a.process();
        c2b.process();
        cout4.retrive();
        a1a.process();
        a1b.process();
        a2.process();

        assertTrue(aout.retrive().getBodyString().contains("<a><b><c>b1c1</c><c>b1c2</c></b><b><c>b2c1</c><c>b2c2</c></b></a>"));
        assertTrue(aout.empty());
        assertTrue(cout1.empty());
        assertTrue(cout2.empty());
        assertFalse(cout3.empty());
        assertTrue(cout4.empty());
        assertFalse(amid1.empty());
        assertTrue(amid2.empty());
    }

}

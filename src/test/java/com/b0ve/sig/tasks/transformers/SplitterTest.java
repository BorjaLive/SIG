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
public class SplitterTest {

    @Test
    public void testSplitter1() throws SIGException {
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
        Splitter splitter = new Splitter("/libros/libro");
        Buffer in = new Buffer(null, null);
        splitter.addInput(in);
        Buffer out = new Buffer(null, null);
        splitter.addOutput(out);

        in.push(m1);

        splitter.process();

        assertTrue(out.retrive().evalString("/libro").contains("Robotica Vision y Control"));
        assertTrue(out.retrive().evalString("/libro").contains("Interspecies Reviewers"));
        assertTrue(out.retrive().evalString("/libro").contains("No lunch break"));
    }

    @Test
    public void testSplitter2() throws SIGException {
        Message m1 = newMessage(0, 0, "<a><b><c>b1c1</c><c>b1c2</c></b><b><c>b2c1</c></b></a>");
        Splitter s1 = new Splitter("/a/b");
        Buffer in = new Buffer(null, null);
        s1.addInput(in);
        Buffer mid = new Buffer(null, null);
        s1.addOutput(mid);
        Splitter s2 = new Splitter("/b/c");
        s2.addInput(mid);
        Buffer out = new Buffer(null, null);
        s2.addOutput(out);

        in.push(m1);

        s1.process();
        s2.process();

        assertEquals(out.retrive().evalString("/c"), "b1c1");
        assertEquals(out.retrive().evalString("/c"), "b1c2");
        assertEquals(out.retrive().evalString("/c"), "b2c1");
    }
}

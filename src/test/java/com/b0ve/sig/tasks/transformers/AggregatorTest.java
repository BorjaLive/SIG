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
import java.util.Iterator;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author borja
 */
public class AggregatorTest {

    @Test
    public void testAggregator1() throws SIGException {
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
        SplitterTemplate splitter = new Splitter("/libros/libro");
        Buffer in = new Buffer(null, null);
        splitter.addInput(in);
        Buffer mid = new Buffer(null, null);
        splitter.addOutput(mid);

        Aggregator aggregator = new Aggregator();
        aggregator.addInput(mid);
        Buffer out = new Buffer(null, null);
        aggregator.addOutput(out);

        in.push(m1);
        in.push(new Message(m1));

        splitter.process();
        mid.retrive();
        aggregator.process();

        assertEquals(out.retrive().eval("/libros/libro").getLength(), 3);
        assertTrue(out.empty());
        assertNotNull(mid.retrive());
        assertNotNull(mid.retrive());
        assertNull(mid.retrive());
    }

    @Test
    public void testAggregator2() throws SIGException {
        Message m1 = newMessage("<a><b><c>b1c1</c><c>b1c2</c></b><b><c>b2c1</c></b></a>");

        SplitterTemplate s1 = new Splitter("/a/b");
        Buffer sin = new Buffer(null, null);
        s1.addInput(sin);
        Buffer smid = new Buffer(null, null);
        s1.addOutput(smid);
        SplitterTemplate s2 = new Splitter("/b/c");
        s2.addInput(smid);
        Buffer sout = new Buffer(null, null);
        s2.addOutput(sout);

        Aggregator a2 = new Aggregator();
        a2.addInput(sout);
        Buffer amid = new Buffer(null, null);
        a2.addOutput(amid);
        Aggregator a1 = new Aggregator();
        a1.addInput(amid);
        Buffer aout = new Buffer(null, null);
        a1.addOutput(aout);

        sin.push(m1);
        sin.push(new Message(m1));

        s1.process();
        s2.process();

        Iterator<Message> iter = sout.getIterator();
        iter.next();
        iter.next();
        sout.deleteMessage(iter.next());

        a2.process();
        a1.process();

        Message m = aout.retrive();
        assertTrue(m.getBodyString().contains("b1c1"));
        assertTrue(m.getBodyString().contains("b1c2"));
        assertTrue(m.getBodyString().contains("b2c1"));
        assertTrue(aout.empty());
        assertTrue(sout.empty());
        m = amid.retrive();
        assertTrue(m.getBodyString().contains("b1c1"));
        assertTrue(m.getBodyString().contains("b1c2"));
        assertNull(amid.retrive());
    }

}

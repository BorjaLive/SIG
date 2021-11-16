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
import com.b0ve.sig.utils.exceptions.XPathEvaluationException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;

/**
 *
 * @author borja
 */
public class ContextEnricherTest {

    @Test
    public void testContextEnricher1() throws ParserConfigurationException, XPathExpressionException, SAXException, IOException, XPathEvaluationException, SIGException {
        Message m1 = newMessage(0, 0, "<pelicula><titulo>Crimen Ferpecto</titulo></pelicula>");
        Message m2 = newMessage(0, 0, "<pelicula><precio>10.5</precio></pelicula>");

        ContextEnricher enricher = new ContextEnricher();
        Buffer in1 = new Buffer(null, null);
        Buffer in2 = new Buffer(null, null);
        enricher.addInput(in1);
        enricher.addInput(in2);
        Buffer out = new Buffer(null, null);
        enricher.addOutput(out);

        in1.push(m1);
        in2.push(m2);

        enricher.process();

        assertEquals(Message.evaluateXPath(m1.getBody(), "/pelicula").item(0).getTextContent(), "Crimen Ferpecto10.5");
    }

}

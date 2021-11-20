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
public class TranslatorTest {

    @Test
    public void testTranslator1() throws SIGException {
        Message m1 = newMessage(0, 0, "<libro><titulo>Robotica Vision y Control</titulo><autor>Peter Corke</autor><precio>70</precio></libro>");
        Translator translator = new Translator("<?xml version=\"1.0\"?>\n"
                + "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n"
                + "<xsl:template match=\"/libro\">\n"
                + "<libro>\n"
                + "<nombre>\n"
                + "<xsl:value-of select=\"autor\"/> - <xsl:value-of select=\"titulo\"/>\n"
                + "</nombre>\n"
                + "<precio>\n"
                + "<xsl:value-of select=\"precio\"/>\n"
                + "</precio>\n"
                + "</libro>\n"
                + "</xsl:template>\n"
                + "</xsl:stylesheet>");
        Buffer in = new Buffer(null, null);
        translator.addInput(in);
        Buffer out = new Buffer(null, null);
        translator.addOutput(out);

        in.push(m1);

        translator.process();

        assertEquals(out.retrive().evalString("/libro/nombre"), "Peter Corke - Robotica Vision y Control");
    }

}

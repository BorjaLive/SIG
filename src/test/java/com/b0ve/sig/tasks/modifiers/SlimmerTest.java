/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.b0ve.sig.tasks.modifiers;

import com.b0ve.sig.flow.Buffer;
import com.b0ve.sig.flow.Message;
import static com.b0ve.sig.flow.Message.newMessage;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author borja
 */
public class SlimmerTest {

    @Test
    public void testSlimmer1() throws Exception {
        Message m1 = newMessage(0, 0, "<cambios>\n"
                + "	<cambio>\n"
                + "		<tipo>crear</tipo>\n"
                + "		<datos>\n"
                + "			<nombre>Pepe</nombre>\n"
                + "			<dni>40144661E</dni>\n"
                + "			<direccion>Avenida Guatemala 40</direccion>\n"
                + "		</datos>\n"
                + "	</cambio>\n"
                + "	<cambio>\n"
                + "		<tipo>eliminar</tipo>\n"
                + "		<datos>\n"
                + "			<nombre>Borrame1</nombre>\n"
                + "			<dni>Salvame1</dni>\n"
                + "		</datos>\n"
                + "	</cambio>\n"
                + "	<cambio>\n"
                + "		<tipo>crear</tipo>\n"
                + "		<datos>\n"
                + "			<nombre>Borrame2</nombre>\n"
                + "			<dni>40144663C</dni>\n"
                + "			<direccion>Raul Cimas 5</direccion>\n"
                + "		</datos>\n"
                + "	</cambio>\n"
                + "	<cambio>\n"
                + "		<tipo>crear</tipo>\n"
                + "		<datos>\n"
                + "			<nombre>Pepe</nombre>\n"
                + "			<dni>40144661E</dni>\n"
                + "			<direccion>Calle Plus Ultra 9</direccion>\n"
                + "		</datos>\n"
                + "	</cambio>\n"
                + "</cambios>");
        Slimmer slimmer = new Slimmer(new String[]{"/cambios/cambio[tipo='eliminar']/datos/nombre", "/cambios/cambio[3]"});
        Buffer in = new Buffer(null, null);
        slimmer.addInput(in);
        Buffer out = new Buffer(null, null);
        slimmer.addOutput(out);

        in.push(m1);

        slimmer.process();

        Message m = out.retrive();
        assertNotNull(m);
        assertFalse(m.getBodyString().contains("Borrame1"));
        assertFalse(m.getBodyString().contains("Borrame2"));
        assertTrue(m.getBodyString().contains("Salvame1"));
    }

}

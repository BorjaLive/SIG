/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.b0ve.sig.flow;

import com.b0ve.sig.utils.exceptions.SIGException;
import java.util.UUID;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author borja
 */
public class MensajeTest {

    @Test
    public void testFragmentInfo() throws SIGException {
        UUID id1 = UUID.randomUUID(),
                id2 = UUID.randomUUID(),
                id3 = UUID.randomUUID();

        //Se cumple el orden de inserción y extracción
        Message m1 = new Message("<n>1</n>");
        m1.addFragmentInfo(new FragmentInfo(id1, 0));
        m1.addFragmentInfo(new FragmentInfo(id2, 0));
        assertEquals(m1.removeFragmentInfo().getFragmentID(), id2);
        assertEquals(m1.getFragmentInfo().getFragmentID(), id1);
        m1.removeFragmentInfo();
        assertNull(m1.getFragmentInfo());

        //Agregar la pila fragmentos de un mensaje a otro
        m1.addFragmentInfo(new FragmentInfo(id1, 0));
        m1.addFragmentInfo(new FragmentInfo(id2, 0));
        Message m2 = new Message("<n>2</n>");
        m2.addFragmentInfo(m1.getFragmentInfoStack());
        m2.addFragmentInfo(new FragmentInfo(id3, 0));
        assertEquals(m2.removeFragmentInfo().getFragmentID(), id3);
        assertEquals(m2.removeFragmentInfo().getFragmentID(), id2);
        assertEquals(m2.removeFragmentInfo().getFragmentID(), id1);
        assertNull(m2.getFragmentInfo());
    }

}

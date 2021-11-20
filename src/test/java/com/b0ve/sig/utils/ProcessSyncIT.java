/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.b0ve.sig.utils;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.flow.Message;
import com.b0ve.sig.ports.Port;
import com.b0ve.sig.tasks.Task;
import com.b0ve.sig.tasks.modifiers.SlimmerTemplate;
import static com.b0ve.sig.utils.Process.TASKS.CONTEXT_ENRICHER;
import static com.b0ve.sig.utils.Process.TASKS.CORRELATOR;
import static com.b0ve.sig.utils.Process.TASKS.FILTER;
import static com.b0ve.sig.utils.Process.TASKS.MERGER;
import static com.b0ve.sig.utils.Process.TASKS.REPLICATOR;
import static com.b0ve.sig.utils.Process.TASKS.SPLITTER;
import static com.b0ve.sig.utils.Process.TASKS.TRANSLATOR;
import com.b0ve.sig.utils.condiciones.FilterCondition;
import com.b0ve.sig.utils.condiciones.FilterConditionNotEquals;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;
import org.w3c.dom.Document;

/**
 *
 * @author borja
 */
public class ProcessSyncIT {

    Adapter aGMS = new Adapter() {
        @Override
        public void iniciate() {
            try {
                sendProcess("<acta>\n"
                        + "    <asignatura>PVWB</asignatura>\n"
                        + "    <alumnos>\n"
                        + "        <alumno>\n"
                        + "            <id>borja.lopez248</id>\n"
                        + "            <nota>2</nota>\n"
                        + "        </alumno>\n"
                        + "        <alumno>\n"
                        + "            <id>spit.fire144</id>\n"
                        + "            <nota>10</nota>\n"
                        + "        </alumno>\n"
                        + "    </alumnos>\n"
                        + "</acta>");
            } catch (SIGException ex) {
                Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public Process.PORTS getCompatiblePortType() {
            return Process.PORTS.INPUT;
        }
    };
    Adapter aMySQL = new Adapter() {
        @Override
        public Document sendApp(Document doc) {
            try {
                if (XMLUtils.evalString(doc, "/sql").contains("borja.lopez248")) {
                    return XMLUtils.parse("<Results><Row><Email>email1@example.org</Email><Telefono>telf1</Telefono></Row></Results>");
                } else {
                    return XMLUtils.parse("<Results><Row><Email>email2@example.org</Email><Telefono>null</Telefono></Row></Results>");
                }
            } catch (SIGException ex) {
                Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public Process.PORTS getCompatiblePortType() {
            return Process.PORTS.REQUEST;
        }
    };
    Adapter aMail = new Adapter() {
        @Override
        public Document sendApp(Document doc) throws SIGException {
            correosEnviados.add(XMLUtils.evalString(doc, "/alumno/email"));
            return null;
        }

        @Override
        public Process.PORTS getCompatiblePortType() {
            return Process.PORTS.OUTPUT;
        }
    };
    Adapter aSMS = new Adapter() {
        @Override
        public Document sendApp(Document doc) throws SIGException {
            smssEnviados.add(XMLUtils.evalString(doc, "/alumno/telefono"));
            return null;
        }

        @Override
        public Process.PORTS getCompatiblePortType() {
            return Process.PORTS.OUTPUT;
        }
    };

    String confEj1Translator1 = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"2.0\">\n"
            + "    <xsl:template match=\"/acta\">\n"
            + "        <alumnos>\n"
            + "            <xsl:for-each select=\"alumnos/alumno\">\n"
            + "                <alumno>\n"
            + "                    <asignatura>\n"
            + "                        <xsl:value-of select=\"/acta/asignatura\"/>\n"
            + "                    </asignatura>\n"
            + "                    <id>\n"
            + "                        <xsl:value-of select=\"id\"/>\n"
            + "                    </id>\n"
            + "                    <nota>\n"
            + "                        <xsl:value-of select=\"nota\"/>\n"
            + "                    </nota>\n"
            + "                </alumno>\n"
            + "            </xsl:for-each>\n"
            + "        </alumnos>\n"
            + "    </xsl:template>\n"
            + "</xsl:stylesheet>";
    String confEj1Translator2 = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n"
            + "    <xsl:template match=\"/alumno\">\n"
            + "        <sql>\n"
            + "            SELECT `Email`, `Telefono` FROM `alumnos` WHERE `ID` = '<xsl:value-of select=\"id\"/>'\n"
            + "        </sql>\n"
            + "    </xsl:template>\n"
            + "</xsl:stylesheet>";
    String confEj1Translator3 = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n"
            + "    <xsl:template match=\"/Results/Row\">\n"
            + "        <alumno>\n"
            + "            <email><xsl:value-of select=\"Email\"/></email>\n"
            + "            <telefono><xsl:value-of select=\"Telefono\"/></telefono>\n"
            + "        </alumno>\n"
            + "    </xsl:template>\n"
            + "</xsl:stylesheet>";

    ArrayList<String> correosEnviados = new ArrayList<>();
    ArrayList<String> smssEnviados = new ArrayList<>();

    @Test
    public void testProcSyncEj1() throws SIGException {
        Process p = new ProcessSync();

        correosEnviados.clear();
        smssEnviados.clear();

        Port pGMS,
                pMySQL,
                pMail,
                pSMS;
        try {
            pGMS = p.createPort(aGMS);
            pMySQL = p.createPort(aMySQL);
            pMail = p.createPort(aMail);
            pSMS = p.createPort(aSMS);
        } catch (SIGException ex) {
            Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            fail("Port setup failed");
            return;
        }

        Task translator1 = p.createTask(TRANSLATOR, confEj1Translator1);
        Task splitter = p.createTask(SPLITTER, "/alumnos/alumno");
        Task replicator1 = p.createTask(REPLICATOR);
        Task translator2 = p.createTask(TRANSLATOR, confEj1Translator2);
        Task translator3 = p.createTask(TRANSLATOR, confEj1Translator3);
        Task correlator = p.createTask(CORRELATOR);
        Task enricher = p.createTask(CONTEXT_ENRICHER);
        Task replicator2 = p.createTask(REPLICATOR);
        Task filter = p.createTask(FILTER, new FilterConditionNotEquals("/alumno/telefono", "null"));

        p.connect(pGMS, translator1);
        p.connect(translator1, splitter);
        p.connect(splitter, replicator1);
        p.connect(replicator1, translator2);
        p.connect(replicator1, correlator);
        p.connect(translator2, pMySQL);
        p.connect(pMySQL, translator3);
        p.connect(translator3, correlator);
        p.connect(correlator, enricher);
        p.connect(correlator, enricher);
        p.connect(enricher, replicator2);
        p.connect(replicator2, pMail);
        p.connect(replicator2, filter);
        p.connect(filter, pSMS);

        try {
            p.validate();
        } catch (SIGException ex) {
            Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            fail("Proceso invalido");
        }
        p.execute();
        try {
            Thread.sleep(5000);
            p.shutdown();
            p.waitToEnd();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            fail("Problema esperando a que termine el proceso");
        }

        assertTrue(correosEnviados.contains("email1@example.org"));
        assertTrue(correosEnviados.contains("email2@example.org"));
        assertTrue(smssEnviados.contains("telf1"));
        assertFalse(smssEnviados.contains("null"));
    }

    Adapter aEMS1 = new Adapter() {
        @Override
        public void iniciate() {
            try {
                sendProcess("<medida>\n"
                        + "    <ts>" + Instant.now().getEpochSecond() + "</ts>\n"
                        + "    <lugar>Av. Guatemala, 40</lugar>\n"
                        + "    <valor>144</valor>\n"
                        + "</medida>");
                sendProcess("<medida>\n"
                        + "    <ts>" + Instant.now().getEpochSecond() + "</ts>\n"
                        + "    <lugar>Av. Guatemala, 41</lugar>\n"
                        + "    <valor>400</valor>\n"
                        + "</medida>");
            } catch (SIGException ex) {
                Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public Process.PORTS getCompatiblePortType() {
            return Process.PORTS.INPUT;
        }
    };
    Adapter aEMS2 = new Adapter() {
        @Override
        public void iniciate() {
            try {
                sendProcess("<medida>\n"
                        + "    <ts>" + (Instant.now().getEpochSecond() - (15 * 61)) + "</ts>\n"
                        + "    <lugar>Av. Guatemala, 42</lugar>\n"
                        + "    <valor>250</valor>\n"
                        + "</medida>");
                sendProcess("<medida>\n"
                        + "    <ts>" + Instant.now().getEpochSecond() + "</ts>\n"
                        + "    <lugar>Av. Guatemala, 43</lugar>\n"
                        + "    <valor>260</valor>\n"
                        + "</medida>");
            } catch (SIGException ex) {
                Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public Process.PORTS getCompatiblePortType() {
            return Process.PORTS.INPUT;
        }
    };
    Adapter aEMS3 = new Adapter() {
        @Override
        public void iniciate() {
            try {
                sendProcess("<medida>\n"
                        + "    <ts>" + (Instant.now().getEpochSecond() - (15 * 61)) + "</ts>\n"
                        + "    <lugar>37.2533675195021, -6.93658688591096</lugar>\n"
                        + "    <valor>120</valor>\n"
                        + "</medida>");
                sendProcess("<medida>\n"
                        + "    <ts>" + Instant.now().getEpochSecond() + "</ts>\n"
                        + "    <lugar>37.25740833367934, -6.954137427758186</lugar>\n"
                        + "    <valor>110</valor>\n"
                        + "</medida>");
            } catch (SIGException ex) {
                Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public Process.PORTS getCompatiblePortType() {
            return Process.PORTS.INPUT;
        }
    };
    Adapter aEstimador = new Adapter() {
        @Override
        public Document sendApp(Document doc) {
            try {
                String[] cords = XMLUtils.evalString(doc, "/medida/lugar").split(",");
                Double.parseDouble(cords[0]);
                Double.parseDouble(cords[1]);
                medidas.add(XMLUtils.evalString(doc, "/medida/valor"));
            } catch (Exception e) {
                fail("El mensaje no llego con las coordenadas en su sitio: " + XMLUtils.serialize(doc));
            }
            return null;
        }

        @Override
        public Process.PORTS getCompatiblePortType() {
            return Process.PORTS.OUTPUT;
        }
    };
    Adapter aToGPS = new Adapter() {
        @Override
        public Document sendApp(Document doc) throws SIGException {
            return XMLUtils.parse("<Results><Row><Coordenadas>37.2533675195021, -6.93658688591096</Coordenadas></Row></Results>");
        }

        @Override
        public Process.PORTS getCompatiblePortType() {
            return Process.PORTS.REQUEST;
        }
    };
    String confEj2Translator1 = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n"
            + "    <xsl:template match=\"/medida\">\n"
            + "        <sql>\n"
            + "            SELECT `Coordenadas` FROM `lugares` WHERE `Lugar` = '<xsl:value-of select=\"lugar\"/>'\n"
            + "        </sql>\n"
            + "    </xsl:template>\n"
            + "</xsl:stylesheet>";
    String confEj2Translator2 = "<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n"
            + "    <xsl:template match=\"/Results/Row\">\n"
            + "        <medida>\n"
            + "            <lugar><xsl:value-of select=\"Coordenadas\"/></lugar>\n"
            + "        </medida>\n"
            + "    </xsl:template>\n"
            + "</xsl:stylesheet>";
    Task slimmerEj2 = new SlimmerTemplate() {
        @Override
        protected void slim(Message mensaje) throws SIGException {
            mensaje.transformBody("<xsl:stylesheet xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" version=\"1.0\">\n"
                    + "    <xsl:template match=\"/medida\">\n"
                    + "        <medida>\n"
                    + "            <ts><xsl:value-of select=\"ts\"/></ts>\n"
                    + "            <valor><xsl:value-of select=\"valor\"/></valor>\n"
                    + "        </medida>\n"
                    + "    </xsl:template>\n"
                    + "</xsl:stylesheet>");
        }
    };

    ArrayList<String> medidas = new ArrayList<>();

    @Test
    public void testProcSyncEj2() throws SIGException {
        Process p = new ProcessSync();

        medidas.clear();

        Port pEMS1,
                pEMS2,
                pEMS3,
                pEstimador,
                pToGPS;
        try {
            pEMS1 = p.createPort(aEMS1);
            pEMS2 = p.createPort(aEMS2);
            pEMS3 = p.createPort(aEMS3);
            pEstimador = p.createPort(aEstimador);
            pToGPS = p.createPort(aToGPS);
        } catch (SIGException ex) {
            Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            fail("Failed to setup ports.");
            return;
        }

        Task merger1 = p.createTask(MERGER);
        Task replicator = p.createTask(REPLICATOR);
        Task translator1 = p.createTask(TRANSLATOR, confEj2Translator1);
        Task translator2 = p.createTask(TRANSLATOR, confEj2Translator2);
        Task slimmer = p.addTask(slimmerEj2);
        Task correlator = p.createTask(CORRELATOR);
        Task enricher = p.createTask(CONTEXT_ENRICHER);
        Task merger2 = p.createTask(MERGER);
        Task filter = p.createTask(FILTER, new FilterCondition("/medida/ts") {
            @Override
            protected boolean testValue(String text) {
                return Long.parseLong(text) > Instant.now().getEpochSecond() - (15 * 60);
            }
        });

        p.connect(pEMS1, merger1);
        p.connect(pEMS2, merger1);
        p.connect(merger1, replicator);
        p.connect(replicator, translator1);
        p.connect(replicator, slimmer);
        p.connect(slimmer, correlator);
        p.connect(translator1, pToGPS);
        p.connect(pToGPS, translator2);
        p.connect(translator2, correlator);
        p.connect(correlator, enricher);
        p.connect(correlator, enricher);
        p.connect(enricher, merger2);
        p.connect(pEMS3, merger2);
        p.connect(merger2, filter);
        p.connect(filter, pEstimador);

        try {
            p.validate();
        } catch (SIGException ex) {
            Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            fail("Proceso invalido");
        }
        p.execute();
        try {
            Thread.sleep(5000);
            p.shutdown();
            p.waitToEnd();
        } catch (InterruptedException ex) {
            Logger.getLogger(ProcessSyncIT.class.getName()).log(Level.SEVERE, null, ex);
            fail("Problema esperando a que termine el proceso");
        }

        assertTrue(medidas.contains("144"));
        assertTrue(medidas.contains("400"));
        assertTrue(medidas.contains("260"));
        assertTrue(medidas.contains("110"));
        assertEquals(medidas.size(), 4);
    }
}

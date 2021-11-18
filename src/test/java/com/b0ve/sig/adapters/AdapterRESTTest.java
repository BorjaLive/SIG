/*
 * The MIT License
 *
 * Copyright 2021 borja.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.b0ve.sig.adapters;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.Process;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 *
 * @author borja
 */
public class AdapterRESTTest {

    public AdapterRESTTest() {
    }

    @Test
    public void testSendApp() throws Exception {
        /*
        Document doc = Message.parseXML("<request>\n"
                + "<url>https://b0ve-dev-ed.my.salesforce.com/services/data/v53.0/limits</url>\n"
                + "<method>POST</method>\n"
                + "<authorization>\n"
                + "<type>OAuth2</type>\n"
                + "<data>\n"
                + "<grant_type>client_credentials</grant_type>\n"
                + "<auth_url>https://login.salesforce.com/services/oauth2/authorize</auth_url>\n"
                + "<client_id></client_id>\n"
                + "<client_secret></client_secret>\n"
                + "<scope>api</scope>\n"
                + "</data>\n"
                + "</authorization>\n"
                + "<body>\n"
                + "</body>\n"
                + " * </request>");
        
        AdapterREST rest = new AdapterREST();
        rest.sendApp(doc);
         */
    }

}

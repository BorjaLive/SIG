package com.b0ve.sig.adapters.basic;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.utils.Process.PORTS;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.UnknownHostException;
import org.w3c.dom.Document;

/**
 * Adapter that makes request to a Web API. Endpoint is specified in
 * constructor, the body of the message is sent in json and response is received
 * in json but transformed to xml before it is returned in a message.
 *
 * @author borja
 */
public class AdapterWebAPI extends Adapter {

    private final String gateway;

    public AdapterWebAPI(String gateway) {
        this.gateway = gateway;
    }

    @Override
    public Document sendApp(Document doc) throws SIGException {
        return XMLUtils.json2doc(request(XMLUtils.doc2json(doc)));
    }

    private String request(String data) throws SIGException {
        try {
            URL url = new URL(gateway);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                con.disconnect();
                return response.toString();
            }
        } catch (MalformedURLException | ProtocolException | UnknownHostException ex) {
            throw new SIGException("Could not make request to gateway", gateway, ex);
        } catch (IOException ex) {
            throw new SIGException("IO Making request", data, ex);
        }
    }

    @Override
    public PORTS getCompatiblePortType() {
        return PORTS.REQUEST;
    }

}

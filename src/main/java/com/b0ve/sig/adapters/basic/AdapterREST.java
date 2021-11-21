package com.b0ve.sig.adapters.basic;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.utils.Process;
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
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Adapter for RESTFULL APIs FORMAT:
 * <pre>
 * {@code
 *   <request>
 *       <url>http://example.org/path/to/api</url>
 *       <method>GET|POST|PUT|DELETE|HEAD|CONNECT|OPTIONS|TRACE|PATCH</method> <!-- OPTIONAL default is GET -->
 *       <headers>
 *           <header>
 *               <key></key>
 *               <value></value>
 *           </header>
 *       </headers>
 *       <authorization>
 *           <type>Basic</type>
 *           <data>
 *              <!-- Basic -->
 *               <username> </username>
 *               <password> </password>
 *           </data>
 *       </authorization>
 *       <body>
 *       </body>
 * </request>
 * }
 * </pre> RESPONSE:
 * <pre>
 * {@code
 *  <response>
 *      <status> </status>
 *      <headers>
 *          <header>
 *               <key></key>
 *               <value></value>
 *          </header>
 *          ...
 *      </headers>
 *      <body>
 *      </body>
 *  </response>
 * }
 * </pre>
 *
 * @author borja
 */
public class AdapterREST extends Adapter {

    @Override
    public Document sendApp(Document doc) throws SIGException {
        //Obtain basic information about request
        String requestUrl = XMLUtils.evalString(doc, "/request/url");
        String method = XMLUtils.evalString(doc, "/request/method", "GET").toUpperCase();
        NodeList headers = XMLUtils.eval(doc, "/request/headers/header");
        String authentication = XMLUtils.evalString(doc, "/request/authorization/type", "none").toUpperCase();
        NodeList body = XMLUtils.eval(doc, "/request/body/*");
        String bodyJson = null;
        if (body.getLength() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < body.getLength(); i++) {
                sb.append(XMLUtils.serialize(XMLUtils.node2document(body.item(i))));
            }
            bodyJson = XMLUtils.doc2json(sb.toString());
        }

        try {
            //URL de destino
            URL url = new URL(requestUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            //Metodo
            con.setRequestMethod(method);

            //Cabecera fija para recibir json
            con.setRequestProperty("Accept", "application/json");
            if(bodyJson != null){
                //Establecer el contenido a json
                con.setRequestProperty("Content-Type", "application/json; utf-8");
            }

            //Cabecera personalizada
            for (int i = 0; i < headers.getLength(); i++) {
                String hName = XMLUtils.evalString(XMLUtils.node2document(headers.item(i)), "/header/key");
                String hValue = XMLUtils.evalString(XMLUtils.node2document(headers.item(i)), "/header/value");
                con.setRequestProperty(hName, hValue);
            }

            //Autorizacion
            switch (authentication) {
                case "BASIC":
                    String userAndPass = XMLUtils.evalString(doc, "/request/authorization/data/username", "") + ":" + XMLUtils.evalString(doc, "/request/authorization/data/password", "");
                    con.setRequestProperty("Authorization", "Basic " + Base64.encodeBase64String(userAndPass.getBytes()));
                default:
                    break;
            }

            //Configuracion adicional
            con.setDoOutput(true);
            con.setConnectTimeout(60000);
            con.setReadTimeout(60000);

            //Escribir el cuerpo del mensaje
            if (bodyJson != null) {
                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = bodyJson.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
            }

            //Leer la respuesta
            System.out.println(con.getHeaderFields());
            try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                con.disconnect();
                if(response.toString().isEmpty())
                    return XMLUtils.json2doc("{\"status\":\"ok\"}");
                else
                    return XMLUtils.json2doc(response.toString());
            }
        } catch (MalformedURLException | ProtocolException | UnknownHostException ex) {
            throw new SIGException("Could not make request to gateway", requestUrl, ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new SIGException("IO Making request", bodyJson, ex);
        }
    }

    @Override
    public Process.PORTS getCompatiblePortType() {
        return Process.PORTS.REQUEST;
    }

}

package com.b0ve.sig.adapters;

import com.b0ve.sig.utils.Process;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.w3c.dom.Document;

/**
 * Adapter for RESTFULL APIs FORMAT:
 * <pre>
 * {@code
 *   <request>
 *       <url>http[s]://example.org/path/to/api</url>
 *       <method>[GET|POST|PUT|DELETE|HEAD|CONNECT|OPTIONS|TRACE|PATCH]</method> <!-- OPTIONAL default is GET -->
 *       <headers>
 *           <header>
 *               <key></key>
 *               <value></value>
 *           </header>
 *           ...
 *       </headers>
 *       <authorization>
 *           <type>[Basic|OAuth2]</type>
 *           <data>
 *              <!-- Basic -->
 *               <username> </username>
 *               <password> </password>
 *              <!-- OAuth2 -->
 *               <auth_url> </auth_url>
 *               <grant_type> </grant_type>
 *               <client_id> </client_id>
 *               <client_secret> </client_secret>
 *               <scope> </scope>
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
        /*
        //Obtain basic information about request
        String url = evaluateXPathString(doc, "/request/url");
        String method = evaluateXPathString(doc, "/request/method", "GET").toUpperCase();
        String authentication = evaluateXPathString(doc, "/request/authorization/type", "none").toUpperCase();

        try {
            //Client
            CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContextBuilder.create().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build(), NoopHostnameVerifier.INSTANCE)).build();

            //Authentication
            switch (authentication) {
                case "OAUTH2":
                    String authUrl = evaluateXPathString(doc, "/request/authorization/data/auth_url");
                    String grantType = evaluateXPathString(doc, "/request/authorization/data/grant_type");
                    String clientID = evaluateXPathString(doc, "/request/authorization/data/client_id");
                    String clientSecret = evaluateXPathString(doc, "/request/authorization/data/client_secret");
                    String scope = evaluateXPathString(doc, "/request/authorization/data/scope");

                    System.out.println("Getting configuration... "+authUrl);
                    HttpGet configurationRequest = new HttpGet(authUrl);
                    CloseableHttpResponse configurationResponse = client.execute(configurationRequest);
                    System.out.println("Configuration response: ");
                    
                    String text = IOUtils.toString(configurationResponse.getEntity().getContent(), StandardCharsets.UTF_8.name());
                    System.out.println(text);
                    
                    JsonObject configurarionObject = Json.createReader(configurationResponse.getEntity().getContent()).readObject();
                    String tokenEndpoint = configurarionObject.getString("token_endpoint");
                    configurationResponse.close();

                    System.out.println("Getting an access token...");
                    HttpPost tokenRequest = new HttpPost(tokenEndpoint);
                    List<NameValuePair> data = new ArrayList<>();
                    data.add(new BasicNameValuePair("grant_type", grantType));
                    data.add(new BasicNameValuePair("client_id", clientID));
                    data.add(new BasicNameValuePair("client_secret", clientSecret));
                    tokenRequest.setEntity(new UrlEncodedFormEntity(data));
                    CloseableHttpResponse tokenResponse = client.execute(tokenRequest);

                    JsonObject tokenObject = Json.createReader(tokenResponse.getEntity().getContent()).readObject();
                    String accessToken = tokenObject.getString("access_token");
                    Integer expiresIn = tokenObject.getInt("expires_in");
                    Integer refreshExpires = tokenObject.getInt("refresh_expires_in");
                    String refreshToken = tokenObject.getString("refresh_token");

                    System.out.printf("Access token: %s%n", accessToken);
                    System.out.printf("Expires in: %d%n", expiresIn);
                    System.out.printf("Refresh expires in: %d%n", refreshExpires);
                    System.out.printf("Refresh token: %s%n", refreshToken);

                    System.out.println("Calling oauth secured service...");
                    HttpGet serviceRequest = new HttpGet(url);
                    serviceRequest.addHeader("Authorization", "Bearer " + accessToken);
                    CloseableHttpResponse serviceResponse = client.execute(serviceRequest);

                    JsonObject serviceObject = Json.createReader(serviceResponse.getEntity().getContent()).readObject();

                    System.out.printf("Result: %s%n", serviceObject.toString());

                    break;
            }

            //Actual request
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AdapterREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(AdapterREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyManagementException ex) {
            Logger.getLogger(AdapterREST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AdapterREST.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        return null;
    }

    @Override
    public Process.PORTS getCompatiblePortType() {
        return Process.PORTS.REQUEST;
    }

}

package com.b0ve.sig.adapters;

import com.b0ve.sig.utils.Process.PORTS;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.xpath.XPathExpression;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Adapter that executes a list of queries in a MySQL server. Response is not
 * returned. FORMAT:
 * <pre>
 * {@code
 * <queries>
 * <sql> QUERY 1 </sql>
 * <sql> QUERY 2 </sql>
 * <queries>
 * }
 * </pre>
 *
 * @author borja
 */
public class AdapterMySQLmultyQuery extends Adapter {

    private Connection conn;
    private final String ip,
            db,
            user,
            pass;
    private final int puerto;
    private final XPathExpression queryXPath;

    public AdapterMySQLmultyQuery(String ip, int puerto, String db, String user, String pass) throws SIGException {
        this.ip = ip;
        this.puerto = puerto;
        this.db = db;
        this.user = user;
        this.pass = pass;
        queryXPath = XMLUtils.compile("/queries/sql");
    }

    @Override
    public Document sendApp(Document doc) {
        try {
            NodeList queries = XMLUtils.eval(doc, queryXPath);
            for (int i = 0; i < queries.getLength(); i++) {
                String sql = queries.item(i).getTextContent();
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
                stmt.close();
            }
        } catch (SQLException ex) {
            handleException(new SIGException("SQL Exception ", XMLUtils.serialize(doc), ex));
        } catch (SIGException ex) {
            handleException(ex);
        }
        return null;
    }

    @Override
    public void iniciate() {
        super.iniciate();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + puerto + "/" + db, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            handleException(new SIGException("Error connecting to DB", null, ex));
            conn = null;
        }
    }

    @Override
    public void halt() {
        super.halt();
        try {
            conn.close();
        } catch (SQLException ex) {
            handleException(new SIGException("Error disconnecting from DB", null, ex));
        }
    }

    @Override
    public PORTS getCompatiblePortType() {
        return PORTS.OUTPUT;
    }

}

package com.b0ve.sig.adapters;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.Process.PORTS;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Adapter that executes a list of queries in a MySQL server. Response is not returned.
 * FORMAT:
 * <pre>
 * {@code
 * <queries>
 * <sql> QUERY 1 </sql>
 * <sql> QUERY 2 </sql>
 * <queries>
 * }
 * </pre>
 * @author borja
 */
public class AdapterMySQLmultyQuery extends Adapter {

    private Connection conn;
    private final String ip, db, user, pass;
    private final int puerto;

    public AdapterMySQLmultyQuery(String ip, int puerto, String db, String user, String pass) {
        this.ip = ip;
        this.puerto = puerto;
        this.db = db;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public Document sendApp(Message m) {
        try {
            NodeList queries = m.evaluateXPath("/queries/sql");
            for (int i = 0; i < queries.getLength(); i++) {
                String sql = queries.item(i).getTextContent();
                Statement stmt = conn.createStatement();
                stmt.executeQuery(sql);
                stmt.close();
            }
        } catch (SQLException ex) {
            handleException(new SIGException("SQL Exception ", m, ex));
        } catch (SIGException ex) {
            handleException(ex);
        }
        return null;
    }

    @Override
    public void iniciate() {
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

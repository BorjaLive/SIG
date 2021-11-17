package com.b0ve.sig.adapters;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.JDBCUtil;
import com.b0ve.sig.utils.Process.PORTS;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;

/**
 * Adapter that executes a query in MySQL server. The response is returned as response, selected column names are not changed.
 * FORMAT: 
 * <pre>
 * {@code
 * <sql> QUERY </sql>
 * }
 * </pre>
 * RESPONSE:
 * <pre>
 * {@code
 * <Results>
 *  <Row>
 *    ONE TAG PER COLUMN WITH ITS ORIGINAL NAME
 *  </Row>
 * </Results>
 * }
 * </pre>
 * @author borja
 */
public class AdapterMySQL extends Adapter {

    private Connection conn;
    private final String ip, db, user, pass;
    private final int puerto;

    public AdapterMySQL(String ip, int puerto, String db, String user, String pass) {
        this.ip = ip;
        this.puerto = puerto;
        this.db = db;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public Document sendApp(Message m) {
        try {
            String sql = m.evaluateXPathString("/sql");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Document doc = JDBCUtil.toDocument(rs);
            rs.close();
            stmt.close();
            return doc;
        } catch (SQLException ex) {
            handleException(new SIGException("SQL Exception ", m, ex));
        } catch (ParserConfigurationException ex) {
            handleException(new SIGException("JDBCUtil Exception ", m, ex));
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
            ex.printStackTrace();
            conn = null;
        }
    }

    @Override
    public void halt() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(AdapterMySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public PORTS getCompatiblePortType() {
        return PORTS.REQUEST;
    }

}

package com.b0ve.sig.adapters.basic;

import com.b0ve.sig.adapters.Adapter;
import com.b0ve.sig.utils.Process.PORTS;
import com.b0ve.sig.utils.XMLUtils;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpression;
import org.w3c.dom.Document;

/**
 * Adapter that executes a query in MySQL server. The response is returned as
 * response, selected column names are not changed. FORMAT:
 * <pre>
 * {@code
 * <sql> QUERY </sql>
 * }
 * </pre> RESPONSE:
 * <pre>
 * {@code
 * <Results>
 *  <Row>
 *    ONE TAG PER COLUMN WITH ITS ORIGINAL NAME
 *  </Row>
 * </Results>
 * }
 * </pre>
 *
 * @author borja
 */
public class AdapterMySQL extends Adapter {

    private Connection conn;
    private final String ip,
            db,
            user,
            pass;
    private final int puerto;
    private final XPathExpression queryXPath;

    public AdapterMySQL(String ip, int puerto, String db, String user, String pass) throws SIGException {
        this.ip = ip;
        this.puerto = puerto;
        this.db = db;
        this.user = user;
        this.pass = pass;
        queryXPath = XMLUtils.compile("/sql");
    }

    @Override
    public Document sendApp(Document doc) throws SIGException {
        try {
            String sql = XMLUtils.evalString(doc, queryXPath);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            Document res = XMLUtils.rs2doc(rs);
            rs.close();
            stmt.close();
            return res;
        } catch (SQLException ex) {
            throw new SIGException("SQL Exception ", XMLUtils.serialize(doc), ex);
        } catch (ParserConfigurationException ex) {
            throw new SIGException("JDBCUtil Exception ", XMLUtils.serialize(doc), ex);
        }
    }

    @Override
    public void iniciate() throws SIGException {
        super.iniciate();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + puerto + "/" + db, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new SIGException("Error connecting to DB", null, ex);
        }
    }

    @Override
    public void halt() throws SIGException {
        super.halt();
        try {
            conn.close();
        } catch (SQLException ex) {
            throw new SIGException("Error disconnecting from DB", null, ex);
        }
    }

    @Override
    public PORTS getCompatiblePortType() {
        return PORTS.REQUEST;
    }

}

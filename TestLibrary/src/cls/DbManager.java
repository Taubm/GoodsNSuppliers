package cls;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author CherIA
 */
import java.sql.*;

public class DbManager {

    private static Connection con = null;
    private static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String url = Config.dbPath;
    private static String dbName = null;

    public DbManager(String _dbName) {
        dbName = _dbName;
        if (!dbExists()) {
            try {
                con = DriverManager.getConnection(url + dbName + ";create=true");
            } catch (SQLException e) {
            }
        }
    }

    private Boolean dbExists() {
        Boolean exists = false;
        try {
            con = DriverManager.getConnection(url + dbName);
            exists = true;
        } catch (SQLException e) {
        }
        return (exists);
    }

    //Запрос на обновление базы данных  (INSERT, UPDATE, CREATE TABLE и т.п.)
    public void executeUpdate(String sql) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate(sql);
        stmt.close();
    }

    //Запрос на выборку данных из базы
    public ResultSet executeQuery(String sql) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet result = stmt.executeQuery(sql);
        return result;
    }

    public void dbPrepare() {
        try {
            executeUpdate("CREATE TABLE Suppliers (Id integer NOT NULL, Name varchar(30) NOT NULL, phone numeric(13,0) NOT NULL, Upd_date date NOT NULL, PRIMARY KEY (id))");
        } catch (SQLException e) {
        }
        try {
            executeUpdate("CREATE TABLE Goods (Id integer NOT NULL, Caption varchar(20) NOT NULL, Cost real NOT NULL, Description varchar(140) NOT NULL, Supplier_id integer not null, PRIMARY KEY (id))");
        } catch (SQLException e) {
        }
    }

    public boolean checkReady() {
        return con != null;
    }
}

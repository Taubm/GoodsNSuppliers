package cls;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Иван
 */
abstract class Row {

    String id;
    String table_name;

    public String getId() {
        return id;
    }

    ResultSet dbSelect(DbManager db) {
        String sql = "select * from " + table_name;
        ResultSet res = null;
        try {
            res = db.executeQuery(sql);
        } catch (SQLException ex) {

        }
        return res;
    }

    abstract String sqlString();

}

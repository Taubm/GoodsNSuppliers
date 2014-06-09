package cls;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author CherIA
 */
public class Table {

    private String name = "";
    public ArrayList<Row> arr;

    public Table(String _name) {
        this.arr = new ArrayList<>();
        name = _name;
    }

    public void clearTable(DbManager db) {
        try {
            db.executeUpdate("delete from " + name);
        } catch (SQLException e) {

        }
    }

    public ResultSet dbSelect(DbManager db) {
        String sql = "select * from " + name;
        ResultSet res = null;
        try {
            res = db.executeQuery(sql);
        } catch (SQLException e) {

        }
        return res;
    }

    public String dbUpdate(DbManager db, String mode) {
        String errors = "";
        String sql = "";
        String ids = "(";
        for (int i = 0; i < arr.size(); i++) {
            ids += arr.get(i).getId();
            ids += (i < arr.size() - 1) ? "," : ")";
        }

        switch (mode) {
            case "hard":
                sql = "delete from " + name + " where id in " + ids;
                break;

            case "complete":
                sql = "delete from " + name;
                break;

            case "soft":
                sql = "select * from " + name + " where id in " + ids;
                break;
        }
        if (!mode.equals("soft")) {
            try {
                db.executeUpdate(sql);
            } catch (SQLException e) {

            }
        } else {
            ResultSet res = null;
            ArrayList<String> checkIds = new ArrayList<>();
            try {
                res = db.executeQuery(sql);
            } catch (SQLException e) {
            }
            try {
                while (res.next()) {
                    checkIds.add(res.getString("id"));
                }
            } catch (Exception e) {
            }

            if (!checkIds.isEmpty()) {
                for (int i = arr.size() - 1; i >= 0; i--) {
                    if (checkIds.contains(arr.get(i).getId())) {
                        errors += "В таблице " + name + " уже существует запись с id: " + arr.get(i).getId() + "<br>";
                        arr.remove(i);
                    }
                }
            }
        }

        sql = "insert into " + name + " values ";

        for (int i = 0; i < arr.size(); i++) {
            sql += arr.get(i).sqlString();
            sql += (i < arr.size() - 1) ? "," : "";
        }
        try {
            db.executeUpdate(sql);
        } catch (SQLException e) {

        }
        return errors;
    }

    public boolean containsId(String id) {
        for (Row r : arr) {
            if (r.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

}

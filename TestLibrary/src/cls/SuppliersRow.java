package cls;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author CherIA
 */
public class SuppliersRow extends Row {

    private String Name = "";
    private String Phone = "";
    private String Upd_date = "";

    public SuppliersRow(String line) throws Exception {
        table_name = Config.tSuppliersName;
        String[] parts = line.split(";");
        if (parts.length == 4) {
            id = parts[0];
            Name = parts[1];
            Phone = parts[2];
            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            Upd_date = df.format(java.util.Calendar.getInstance().getTime());

            Validate.checkNumber(id);
            Validate.checkNumber(Phone);

        } else {
            throw new Exception("Неверный формат поставщика: ");
        }
    }

    public SuppliersRow() {
    }

    public static ArrayList<SuppliersRow> getFromDb(ResultSet res) {
        ArrayList<SuppliersRow> result = new ArrayList<>();
        try {
            while (res.next()) {
                String line = res.getString("id") + ";" + res.getString("name") + ";" + res.getString("phone") + ";" + res.getDate("upd_date");
                SuppliersRow s = new SuppliersRow(line);
                result.add(s);
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public String sqlString() {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String UD = df.format(java.util.Calendar.getInstance().getTime());
        String result = "(" + id + ",'" + Name + "'," + Phone + ",'" + UD + "'" + ")";
        return result;
    }

    @Override
    public String toString() {
        String line = id + ";" + Name + ";" + Phone + ";" + Upd_date;
        return line;
    }

}

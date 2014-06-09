package cls;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author CherIA
 */
public class GoodsRow extends Row {
    
    private String caption = "";
    private String cost = "";
    private String supplierId = "";
    private String description = "";

    public GoodsRow(String line) throws Exception {
        table_name = Config.tGoodsName;
        //Разберем строку и заполним поля объекта
        String[] parts = line.split(";");
        if (parts.length == 5) {
            id = parts[0];
            caption = parts[1];
            cost = parts[2];
            supplierId = parts[3];
            description = parts[4];

            //Проверим на валидность поля
            Validate.checkNumber(id);
            Validate.checkCost(cost);
            Validate.checkNumber(supplierId);

        } else throw new Exception("Неверный формат товара: ");
    }

    public GoodsRow() {
    }
    
    public String getSupplier_id() {
        return supplierId;
    }

    //Метод преобразует выборку из БД в список товаров
    public static ArrayList<GoodsRow> getFromDb(ResultSet res) {
        ArrayList<GoodsRow> result = new ArrayList<>();
        try {
            while (res.next()) {
                String line = res.getString("id") + ";" + res.getString("caption") + ";" + res.getString("cost") + ";" + res.getString("supplier_id") + ";" + res.getString("description");
                GoodsRow g = new GoodsRow(line);
                result.add(g);
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public String sqlString() {
        String result = "(" + id + ",'" + caption + "'," + cost + ",'" + description + "',"+ supplierId+")";
        return result;
    }

    @Override
    public String toString() {
        String line = id + ";" + caption + ";" + cost + ";" + supplierId + ";" + description;
        return line;
    }
}

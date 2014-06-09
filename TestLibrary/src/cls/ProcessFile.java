package cls;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 *
 * @author CherIA
 */
public class ProcessFile {

    //Метод извлечет информацию из БД и сохранит в файл
    public static void prepare(DbManager db, String path) throws IOException {
        BufferedWriter writer;
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));

        //Извлечение и сохранение поставщиков
        writer.write("Suppliers");
        writer.newLine();
        Table Suppliers = new Table(Config.tSuppliersName);
        ArrayList<SuppliersRow> suppliersArray = SuppliersRow.getFromDb(Suppliers.dbSelect(db));
        for (SuppliersRow sr : suppliersArray) {
            writer.write(sr.toString());
            writer.newLine();
        }

        //Извлечение и сохранение товаров
        writer.write("Goods");
        writer.newLine();

        Table Goods = new Table(Config.tGoodsName);
        ArrayList<GoodsRow> goodsArray = GoodsRow.getFromDb(Goods.dbSelect(db));
        for (int i = 0; i < goodsArray.size(); i++) {
            writer.write(goodsArray.get(i).toString());
            if (i < goodsArray.size() - 1) {
                writer.newLine();
            }
        }
        writer.close();
    }

    public static ImportResult extract(DbManager db, File uploadedFile, String mode) throws Exception {
        ArrayList<String> suppliersIds = new ArrayList<>();

        Table tGoods = new Table(Config.tGoodsName);
        Table tSuppliers = new Table(Config.tSuppliersName);

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(uploadedFile), "UTF-8"));

        ImportResult result = new ImportResult(0, 0, "");

        /*
         Формат файла:
         Suppliers
         <список поставщиков>
         Goods
         <список товаров>
         */
        //Извлечем из файла поставщиков
        String line;
        if (!(line = br.readLine()).contains("Suppliers")) {
            result.addError("Неверный формат файла.");
            return result;
        }

        while ((line = br.readLine()) != null && !"Goods".equals(line)) {
            try {
                SuppliersRow s = new SuppliersRow(line);
                if (!tSuppliers.containsId(s.id)) {
                    tSuppliers.arr.add(s);
                } else {
                    result.addError("Поставщик с указанным id уже был описан в файле: "+line+"<br>");
                }
            } catch (java.lang.Exception e) {
                result.addError(e.getMessage() + line + "<br>");
            }
        }
        //Добавим в БД
        result.addError(tSuppliers.dbUpdate(db, mode));

        //Получим из БД id существующих поставщиков
        ArrayList<SuppliersRow> existSuppliers = SuppliersRow.getFromDb(tSuppliers.dbSelect(db));
        for (SuppliersRow s : existSuppliers) {
            suppliersIds.add(s.getId());
        }

        //Извлечем из файла товары
        while ((line = br.readLine()) != null) {
            try {
                GoodsRow g = new GoodsRow(line);
                //Проверим, есть ли поставщик с указанным id
                if (suppliersIds.contains(g.getSupplier_id())) {
                    if (!tGoods.containsId(g.id)) {
                    tGoods.arr.add(g);
                    } else {
                        result.addError("Товар с указанным id уже был описан в файле: "+line+"<br>");
                    }
                } else {
                    throw new Exception("Указанный у товара id поставщика не существует: ");
                }

            } catch (java.lang.Exception e) {
                result.addError(e.getMessage() + line + "<br>");
            }
        }
        //Добавим в БД
        result.addError(tGoods.dbUpdate(db, mode));
        br.close();

        result.setImportedGoodsCount(tGoods.arr.size());
        result.setImportedSuppliersCount(tSuppliers.arr.size());
        return result;
    }

}

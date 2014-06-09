/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testimport;

import cls.Config;
import cls.DbManager;
import cls.ImportResult;
import cls.ProcessFile;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morana Nika
 */
public class App {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Config.getConfig();
        ImportResult ir = new ImportResult();

        System.out.println("Файл import.txt будет импортирован в БД, нажмите Enter для продолжения.");
        try {
            System.in.read();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

        File file = new File(System.getProperty("user.dir")+"\\import.txt");
        if (!file.exists()) {
            exitProcedure("Файл для импорта не найден.");
        }

        DbManager db = new DbManager(Config.dbName);
        if (!db.checkReady()) {
            exitProcedure("Не удалось присоединиться к БД.");
        }
        try {
            ir = ProcessFile.extract(db, file, "soft");
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Результаты импорта: " + ir.getImportedSuppliersCount());
        System.out.println("Импортировано поставщиков: " + ir.getImportedSuppliersCount());
        System.out.println("Импортировано товаров: " + ir.getImportedGoodsCount());
        System.out.println("Ошибки при импорте: \n" + ir.getErrors().replaceAll("<br>", "\n"));

        exitProcedure("");

    }

    private static void exitProcedure(String message) {
        System.out.println(message);
        System.out.println("Нажмите Enter для выхода.");
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }

}

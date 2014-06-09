/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testexport;

import cls.Config;
import cls.DbManager;
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
        System.out.println("В файл export.txt будут экспортированы данные из БД, нажмите Enter для продолжения.");
        try {
            System.in.read();
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        DbManager db = new DbManager(Config.dbName);
        if (!db.checkReady()) {
            exitProcedure("Не удалось присоединиться к БД.");
        }
        File f = new File("export.txt");
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            ProcessFile.prepare(db, "export.txt");
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

        exitProcedure("Экспорт завершен.");
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

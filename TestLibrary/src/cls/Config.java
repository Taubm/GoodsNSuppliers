package cls;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;

/**
 *
 * @author CherIA
 */
public class Config {

    private static boolean done = false;

    //Путь к бд
    public static String dbPath = "";
    public static String dbName = "";

    //Наименования таблиц
    public static String tGoodsName = "";
    public static String tSuppliersName = "";

    public static void getConfig() {

        String confPath = "app.properties";

        Properties prop = new Properties();

        //Загрузим параметры, если не загружали их ранее
        if (!done) {
            if ((new File(confPath)).exists()) {
                try {
                    //Читаем файл настроек
                    prop.load(new InputStreamReader(new FileInputStream(confPath), "UTF-8"));

                    //И выставляем параметры
                    dbPath = prop.getProperty("dbPath");
                    dbName = prop.getProperty("dbName");
                    tGoodsName = prop.getProperty("tGoodsName");
                    tSuppliersName = prop.getProperty("tSuppliersName");
                    done = true;
                } catch (IOException ex) {
                }
            } else {
                try {
                    //Если файла не существует - создаем его и заполняем значениями по умолчанию
                    prop.setProperty("dbPath", "jdbc:derby://localhost:1527/");
                    prop.setProperty("dbName", "db");
                    prop.setProperty("tGoodsName", "goods");
                    prop.setProperty("tSuppliersName", "suppliers");

                    prop.store(new BufferedWriter(new OutputStreamWriter(new FileOutputStream("app.properties"), "UTF-8")), null);

                    getConfig();
                } catch (IOException ex) {
                }

            }
        }
    }

}

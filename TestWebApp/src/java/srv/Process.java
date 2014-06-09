package srv;

import cls.Config;
import cls.DbManager;
import cls.ProcessFile;
import cls.ImportResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class Process extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DbManager db = new DbManager(Config.dbName);

        //Устанавливаем заголовок для ответа
        response.setHeader("Content-Disposition", "attachment; filename=\"db.txt\"");

        //Подготавливаем файл для отправки
        String filePath = getServletContext().getRealPath("/upload/db.txt");
        ProcessFile.prepare(db, filePath);
        File f = new File(filePath);

        //Отсылаем файл
        OutputStream out = response.getOutputStream();
        FileInputStream in = new FileInputStream(f);
        byte[] buffer = new byte[4096];
        int length;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Проверяем, является ли полученный запрос multipart/form-data
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        //Создаём класс-фабрику
        DiskFileItemFactory factory = new DiskFileItemFactory();

        //Максимальный буфера данных в байтах
        factory.setSizeThreshold(1024 * 1024);

        File tempDir = (File) getServletContext().getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(tempDir);
        ServletFileUpload upload = new ServletFileUpload(factory);
        //Максимальный размер данных который разрешено загружать в байтах
        upload.setSizeMax(1024 * 1024 * 10);

        //Получим параметры get-запроса и обработаем в зависимости от типа
        try {
            List items = upload.parseRequest(request);
            Iterator iter = items.iterator();
            while (iter.hasNext()) {
                FileItem item = (FileItem) iter.next();
                if (item.isFormField()) {
                    processFormField(item);
                } else {
                    processUploadedFile(item);
                }
            }
        } catch (Exception e) {

        }

        //Подготовим и отошлем ответ клиенту
        request.setAttribute("suppliers_count", ir.getImportedSuppliersCount());
        request.setAttribute("goods_count", ir.getImportedGoodsCount());
        request.setAttribute("errors", ir.getErrors());
        request.getRequestDispatcher("/response.jsp").forward(request, response);
    }

    private void processUploadedFile(FileItem item) throws Exception {
        if (item.getSize() == 0) {
            ir.addError("Не выбран файл.");
            throw new Exception("Не выбран файл.<br>");
        }
        if (!mode.equals("hard")
                && !mode.equals("soft")
                && !mode.equals("complete")) {
            ir.addError("Неверно указан метод обновления: " + mode+"<br>");
            throw new Exception();
        }

        //Сохраним полученный файл
        String path = getServletContext().getRealPath("/upload/import.txt");
        File uploadedFile = new File(path);
        uploadedFile.createNewFile();
        item.write(uploadedFile);

        DbManager db = new DbManager("db");

        ir = ProcessFile.extract(db, uploadedFile, mode);

    }

    //Обработка строковых параметров
    private void processFormField(FileItem item) {
        if (item.getFieldName().equals("mode")) {
            mode = item.getString();
        }
    }

    String mode = "";

    ImportResult ir = new ImportResult();
}

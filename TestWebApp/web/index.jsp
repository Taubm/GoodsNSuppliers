<%-- 
    Document   : index
    Created on : 04.06.2014, 14:30:19
    Author     : CherIA
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="cls.Config"%>
<%@page import="cls.DbManager"%>
<%@page import="java.sql.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Тестовое задание</title>
    </head>
    <body>
        <div style="width: 30%;float:left">
            <div style="padding: 10%">
                <p>Импорт в БД</p>
                <form action="http://localhost:8080/TestWebApp/process" method="post" enctype="multipart/form-data">
                    <input type="radio" name="mode" value="complete" checked="checked" /> Заменить <br>
                    <input type="radio" name="mode" value="soft" /> Добавить <br>
                    <input type="radio" name="mode" value="hard"/> Добавить с заменой <br>
                    <input name="data" type="file"><br>
                    <input type="submit" value="Отправить"><br>
                </form>
            </div>
            <div style="padding: 10%">
                <p>Экспорт из БД</p>
                <form action="http://localhost:8080/TestWebApp/process" method="get">
                    <input type="submit" value="Получить файл БД"><br>
                </form>
            </div>
        </div>
        <div style="width: 70%;float:left">
            <p>Товары:</p>
            <table border="1">
                <tr>
                    <td>id</td>
                    <td>Наименование</td>
                    <td>Стоимость</td>
                    <td>Поставщик</td>
                    <td>Описание</td>
                </tr>
                <%
                    Config.getConfig();
                    DbManager db = new DbManager(Config.dbName);
                    db.dbPrepare();
                    ResultSet rs = null;
                    String url = Config.dbPath + Config.dbName + ";create=true";

                    try {
                        rs = db.executeQuery("select * from " + Config.tGoodsName + " g join " + Config.tSuppliersName + " s on g.supplier_id = s.id order by g.id");
                        while (rs.next()) {
                            out.println("<tr><td>" + rs.getString("id") + "</td>");
                            out.println("<td>" + rs.getString("caption") + "</td>");
                            out.println("<td>" + rs.getString("cost") + "</td>");
                            out.println("<td>" + rs.getString("name") + "</td>");
                            out.println("<td>" + rs.getString("description") + "</td></tr>");
                        }
                    } catch (Exception e) {
                    }

                %>
            </table>
            <br>
            <p>Поставщики</p>
            <table border="1">
                <tr>
                    <td>id</td>
                    <td>Название</td>
                    <td>Телефон</td>
                    <td>Дата обновления сведений</td>
                </tr>
                <%                    try {
                        rs = db.executeQuery("select * from " + Config.tSuppliersName + " order by id");
                        while (rs.next()) {
                            out.println("<tr><td>" + rs.getString("id") + "</td>");
                            out.println("<td>" + rs.getString("name") + "</td>");
                            out.println("<td>" + rs.getString("phone") + "</td>");
                            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                            String date = df.format(rs.getDate("upd_date"));
                            out.println("<td>" + date + "</td></tr>");
                        }
                    } catch (Exception e) {
                    }

                %>
            </table>
        </div>
    </body>
</html>

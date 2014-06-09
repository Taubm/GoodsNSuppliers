<%-- 
    Document   : response
    Created on : 06.06.2014, 13:43:44
    Author     : CherIA
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Результат импорта</title>
    </head>
    <body>
        <h1>Результаты импорта:</h1>
        <p>Импортировано поставщиков: ${suppliers_count}</p>
        <p>Импортировано товаров: ${goods_count}</p>
        <h2>Ошибки при импорте:</h2>
        <p>${errors}</p>
        <a href="index.jsp">Вернуться</a>
    </body>
</html>

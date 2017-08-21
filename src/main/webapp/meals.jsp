<%@ page import="ru.javawebinar.topjava.util.TimeUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Meals</title>
    <style>
        ."exceeded" {
        color:red;
        }
        ."normal" {
        color:black;
        }
    </style>
</head>


<body>

    <h3><a href="index.html">Home</a></h3>
    <h2>meals List</h2>
    <div>
        <table border="1" cellpadding="8" cellspacing="0">
            <thead>
                <tr>
                    <th>time</th>
                    <th>Description</th>
                    <th>Calories</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach items="${meals}" var="meal">
                <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.model.MealWithExceed"/>
                <tr class="${meal.exceed ? "exceeded" : "normal"}">
                    <%-- Separated date    and  Time!--%>
                    <td>${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}</td>

                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                </tr>
            </c:forEach>
            </tbody>



        </table>
    </div>


</body>
</html>
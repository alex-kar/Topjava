<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Meal's list</h2>
<table style="text-align: center; border: 1px solid; border-collapse: collapse;" cellpadding="5">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th>id</th>
        <th>Update</th>
        <th>Delete</th>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <c:if test="${meal.excess == true}">
            <tr style="background-color: red">
        </c:if>
        <c:if test="${meal.excess == false}">
            <tr style="background-color:green">
        </c:if>
        <td>${meal.dateTime.format(formatter)}</td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td>${meal.id}</td>
        <td><a href="change?id=${meal.id}">UPDATE</a></td>
        <td><a href="delete?id=${meal.id}">DELETE</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="change">INSERT</a></p>

</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Form</title>
</head>
<body>

<h1>Change list</h1>

<form action="change" method="post">
    <label> Date-time
        <input type="datetime-local" name="dateTime" value="${meal.dateTime}">
    </label>
    <label> Description
        <input type="text" name="description" value="${meal.description}">
    </label>
    <label> Calories
        <input type="text" name="calories" value="${meal.calories}">
    </label>
    <input type="hidden" name="id" value="${meal.id}">
    <input type="submit" value="submit">
</form>

</body>
</html>

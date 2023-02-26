<!doctype html>
<html lang="en">
<head>
    <title th:text="${Errormessage}"></title>
</head>
<body>
<table border="1">
    <tr><td>Error Message</td><td th:text="${message}"></td></tr>
    <tr><td>Status Code</td><td th:text="${status}"></td></tr>
    <tr><td>Exception</td><td th:text="${exception}"></td></tr>
    <tr><td>Stacktrace</td><td><pre th:text="${trace}"></pre></td></tr>
    <tr><td>Binding Errors</td><td th:text="${errors}"></td></tr>
</table>
<form>
    <input type="button" value="Go back!" onclick="history.back()">
</form>
</body>
</html>
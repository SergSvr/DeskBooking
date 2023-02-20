<html>

<head>
    <title>First Web Application</title>
</head>

<body>
<span style="color: red; ">${errorMessage}</span>
<form method="post" action="/api/login">
    Name : <input type="text" name="username" />
    Password : <input type="password" name="password" />
    <input type="submit" />
</form>
<%
    String params  = request.getParameter("err");
    if(params!=null)
    out.println("Incorrect Login or Password");
%>
</body>

</html>
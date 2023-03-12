<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="/docs/4.0/assets/img/favicons/favicon.ico">

    <title>BookingApp</title>
    <!-- Bootstrap core CSS -->
    <link href="/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/starter-template.css" rel="stylesheet">
</head>

<body>
<jsp:include page="navbar.jsp" />


<main role="main" class="container">

    <h3>Profile</h3>
    <form class="form" method="post" action="/profile">
        Email: ${user.email}<br/><br/>
        Name: <input type="text" id="name" name="name" placeholder="" value="${user.name}" autofocus><br/><br/>
        Password: <input type="text" id="password" name="password" placeholder="Edit to change" autofocus><br/><br/>
        Position: <input type="text" id="position" name="position" placeholder="" value="${user.position}" autofocus><br/><br/>
        <button class="btn" type="submit">Change profile</button>
    </form>
    <div style="color: red"><c:out value="${error}"/></div>
</main><!-- /.container -->

<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script src="/js/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script>window.jQuery || document.write('<script src="/js/jquery-3.2.1.slim.min.js"><\/script>')</script>
<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>
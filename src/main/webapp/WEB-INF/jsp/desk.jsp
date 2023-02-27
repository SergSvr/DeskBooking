<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
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

    <div align="center">
        <table border="1" cellpadding="5">
            <tr>
                <th>Room</th>
                <th>Desk number</th>
                <th>Desk label</th>
                <th>Delete</th>
            </tr>
            <c:forEach var="i" items="${desks}">
                <tr>
                    <td><c:out value="${i.roomNumber}" /></td>
                    <td><c:out value="${i.number}" /></td>
                    <td><c:out value="${i.label}" /></td>
                    <td><a href="/desk/delete?id=${i.id}">Delete</a></td>
                </tr>
            </c:forEach>
        </table>
        </br>
        <h3>Create Desk</h3>
        <form method="post"  action="/desk">
            <select class="form-select" aria-label="Room Number" name="roomNumber" id="roomNumber" aria-label="Default select example">
                <c:forEach var="i" items="${rooms}">
                    <option value="${i.number}">${i.number}</option>
                </c:forEach>
            </select>
            <input type="text" id="number" name="number" placeholder="Desk Number" required autofocus>
            <input type="text" id="label" name="label" placeholder="Desk label(optional)"  autofocus>
            <button class="btn" type="submit">Create Desk</button>
        </form>
        <c:out value="${result}" />
        <c:out value="${error}" />
    </div>
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
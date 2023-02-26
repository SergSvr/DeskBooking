<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Please sign in</title>
    <link href="/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link href="/signin.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="navbar.jsp" />
<div class="container">

    <form class="form-signin" method="post" action="/api/login">
        <h2 class="form-signin-heading">Please sign in</h2>
        <p>
            <label for="username" class="sr-only">Username</label>
            <input type="text" id="username" name="username" class="form-control" placeholder="Username" required autofocus>
        </p>
        <p>
            <label for="password" class="sr-only">Password</label>
            <input type="password" id="password" name="password" class="form-control" placeholder="Password" required>
        </p>
        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
        <p class="text-center text-muted mt-5 mb-0">Need to register? <a href="/register"
                                                                                class="fw-bold text-body"><u>Register
            here</u></a></p>
    <span style="color: red; ">	<%
        String params  = request.getParameter("err");
        if(params!=null)
            out.println("Incorrect Login or Password");
    %></span>
    </form>
</div>
</body></html>
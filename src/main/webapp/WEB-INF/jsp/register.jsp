<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Please sign in</title>
    <link href="/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="/signin.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="navbar.jsp" />
<div class="container">
    <section>

        <div class="mask d-flex align-items-center h-100 gradient-custom-3">
            <div class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 col-md-9 col-lg-7 col-xl-6">
                        <div class="card" style="border-radius: 15px;">
                            <div class="card-body p-5">
                                <h2 class="text-uppercase text-center mb-5">Create an account</h2>

                                <form class="form-signup" method="post" action="/register">

                                    <div class="form-outline mb-4">
                                        <input type="text" id="name" name="name"  class="form-control form-control-lg"/>
                                        <label class="form-label" for="name">Your Name</label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="text" id="email" name="email" class="form-control form-control-lg"/>
                                        <label class="form-label" for="email">Your Email</label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="text" id="position"  name="position"
                                               class="form-control form-control-lg"/>
                                        <label class="form-label" for="position">Position</label>
                                    </div>

                                    <div class="form-outline mb-4">
                                        <input type="password" id="password" name="password"
                                               class="form-control form-control-lg"/>
                                        <label class="form-label" for="password">Password</label>
                                    </div>

                                    <div class="d-flex justify-content-center">
                                        <button type="submit"
                                                class="btn btn-success btn-block btn-lg gradient-custom-4 text-body">
                                            Register
                                        </button>
                                    </div>

                                    <p class="text-center text-muted mt-5 mb-0">Have already an account? <a href="/login"
                                                                                                            class="fw-bold text-body"><u>Login
                                        here</u></a></p>

                                </form>

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
</body>
</html>
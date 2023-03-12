<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="/">BookingApp</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault"
            aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbar">
        <ul class="navbar-nav mr-auto">
            <sec:authorize access="isAuthenticated()">
                <li class="nav-item">
                    <a class="nav-link" href="/profile">Profile</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/logout">Logout</a>
                </li>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_USER')">
                <li class="nav-item">
                    <a class="nav-link" href="/mybookings">My Bookings</a>
                </li>
            </sec:authorize>
            <sec:authorize access="!isAuthenticated()">
                <li class="nav-item">
                    <a class="nav-link" href="/login">Login</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/register">Register</a>
                </li>
            </sec:authorize>

            <sec:authorize access="hasRole('ROLE_ADMIN')">
                <li class="nav-item">
                    <div class="nav-link" style="color: red">ADMIN</div>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/users">Users</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/bookings">Bookings</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/room">Rooms</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/desk">Desks</a>
                </li>
            </sec:authorize>
        </ul>
    </div>
</nav>
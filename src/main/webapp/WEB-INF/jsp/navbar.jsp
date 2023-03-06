<nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
    <a class="navbar-brand" href="/">BookingApp</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <ul class="navbar-nav mr-auto">
            <% if (request.getAttribute("name")!=null) { %>
            <li class="nav-item">
                <a class="nav-link" href="/profile">Profile <span class="sr-only">(current)</span></a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/logout">Logout <span class="sr-only">(current)</span></a>
            </li>
            <% } else { %>
            <li class="nav-item">
                <a class="nav-link" href="/login">Login <span class="sr-only">(current)</span></a>
            </li>
            <% } %>
            <li class="nav-item">
                <a class="nav-link" href="/bookings">Bookings</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/room">Rooms</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/desk">Desks</a>
            </li>
        </ul>
    </div>
</nav>
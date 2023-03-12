<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<!doctype html>
<html>
<head>
    <meta charset="UTF-8"/>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>BookingApp</title>
    <!-- Bootstrap core CSS -->
    <link href="/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="/starter-template.css" rel="stylesheet">
    <link href="/jquery.timepicker.css" rel="stylesheet"/>

    <link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <script src="/js/jquery.js"></script>
    <script src="/js/jquery-ui.js"></script>
    <script  type="text/javascript" src="/js/jquery.timepicker.js"></script>
    <script>
        jQuery(function($){
            $.datepicker.regional['ru'] = {
                closeText: 'Закрыть',
                prevText: 'Пред',
                nextText: 'След',
                currentText: 'Сегодня',
                monthNames: ['Январь','Февраль','Март','Апрель','Май','Июнь',
                    'Июль','Август','Сентябрь','Октябрь','Ноябрь','Декабрь'],
                monthNamesShort: ['Янв','Фев','Мар','Апр','Май','Июн',
                    'Июл','Авг','Сен','Окт','Ноя','Дек'],
                dayNames: ['воскресенье','понедельник','вторник','среда','четверг','пятница','суббота'],
                dayNamesShort: ['вск','пнд','втр','срд','чтв','птн','сбт'],
                dayNamesMin: ['Вс','Пн','Вт','Ср','Чт','Пт','Сб'],
                weekHeader: 'Нед',
                dateFormat: 'dd.mm.yy',
                firstDay: 1,
                isRTL: false,
                showMonthAfterYear: false,
                yearSuffix: ''};
            $.datepicker.setDefaults($.datepicker.regional['ru']);
        });
        $( function() {
            $( "#date" ).datepicker();
            $( "#date" ).datepicker( "option",$.datepicker.regional[ "ru"] );
            $('#timeFrom').timepicker({ 'scrollDefault': 'now' });
            $('#timeFrom').timepicker({ 'timeFormat': 'H:i' });
            $('#timeTo').timepicker({ 'scrollDefault': 'now' });
            $('#timeTo').timepicker({ 'timeFormat': 'H:i' });
        } );
    </script>
</head>

<body>
<jsp:include page="navbar.jsp"/>
<main role="main" class="container">
    <h3>My Bookings</h3>
    <div align="center">
        <table border="1" cellpadding="5"  class="table">
            <tr class="table-secondary">
                <th>Date</th>
                <th>Desk</th>
                <th>Time From</th>
                <th>Time To</th>
                <th>User</th>
                <th>Cancel</th>
            </tr>
            <c:forEach var="i" items="${bookings}">
                <tr>
                    <td><c:out value="${i.bookingDate}"/></td>
                    <td><c:out value="${i.desk.number}"/></td>
                    <td><c:out value="${i.startTime}"/></td>
                    <td><c:out value="${i.endTime}"/></td>
                    <td><c:out value="${i.user.name}"/></td>
                    <td><a href="/booking/delete?id=${i.id}">Cancel Booking</a></td>
                </tr>
            </c:forEach>
        </table>
        </br>
        <h3>Create Booking</h3>
        <form class="form" method="post" action="/bookings">
            <input type="text" value="${date}" id="date" name="date" required>
            <input type="text" id="deskNumber" name="deskNumber" placeholder="Desk" required autofocus>
            <input type="text" id="timeFrom" name="timeFrom" placeholder="timeFrom" required autofocus>
            <input type="text" id="timeTo" name="timeTo" placeholder="timeTo" required autofocus>
            <button class="btn" type="submit">Create Booking</button>
        </form>
        <c:out value="${result}"/>
        <c:out value="${error}"/>
    </div>


</main><!-- /.container -->
<script>$('#datepicker').datepicker('show');</script>
<!-- Bootstrap core JavaScript
================================================== -->
<!-- Placed at the end of the document so the pages load faster -->


<script src="/js/popper.min.js"></script>
<script src="/js/bootstrap.min.js"></script>
</body>
</html>
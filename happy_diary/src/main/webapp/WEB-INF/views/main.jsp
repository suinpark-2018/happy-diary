<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main Page</title>
</head>
<link rel="stylesheet" href="/resources/css/main.css">
<body>
    <div id="menu">
        <ul>
            <li><a href="#" class="app-name">HAPPY DIARY</a></li>
            <li><a href="#">Private Space</a>
                <ul>
                    <li><a href="/board/list?visibility=private&pno=1">Own Your Wins</a></li>
                    <li><a href="#">Today's Note</a></li>
                </ul>
            </li>
            <li><a href="#">Shared Space</a>
                <ul>
                    <li><a href="/board/list?visibility=public&pno=1">Celebrate Their Wins</a></li>
                    <li><a href="#">Peek into Diaries</a></li>
                </ul>
            </li>
            <li><a href="#">Community</a>
                <ul>
                    <li><a href="#">Notice</a></li>
                    <li><a href="#">General</a></li>
                </ul>
            </li>
            <li><a href="#">Personal Info</a>
                <ul>
                    <li><a href="#">Edit</a></li>
                    <li><a href="#">Unsubscribe</a></li>
                </ul>
            </li>
        </ul>
        <div class="user-info">
            <ul>
                <a href="#" id="user-name">${sessionScope.userName}</a>
                <button type="button" id="logout-btn">LOGOUT</button>
            </ul>
        </div>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="/resources/js/main.js"></script>
</html>


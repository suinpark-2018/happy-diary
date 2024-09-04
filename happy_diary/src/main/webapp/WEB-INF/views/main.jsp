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
            <li><a href="#">Category1</a>
                <ul>
                    <li><a href="#">Sub-1</a></li>
                    <li><a href="#">Sub-2</a></li>
                </ul>
            </li>
            <li><a href="#">Category2</a>
                <ul>
                    <li><a href="#">Sub-1</a></li>
                    <li><a href="#">Sub-2</a></li>
                </ul>
            </li>
            <li><a href="#">Category3</a>
                <ul>
                    <li><a href="#">Sub-1</a></li>
                    <li><a href="#">Sub-2</a></li>
                </ul>
            </li>
            <li><a href="#">Category4</a>
                <ul>
                    <li><a href="#">Sub-1</a></li>
                    <li><a href="#">Sub-2</a></li>
                </ul>
            </li>
        </ul>
        <div class="user-info">
            <ul>
                <a href="#" id="user-name">${sessionScope.userName}</a>
                <button type="button" id="logout-btn">로그아웃</button>
            </ul>
        </div>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/main.js"></script>
</html>


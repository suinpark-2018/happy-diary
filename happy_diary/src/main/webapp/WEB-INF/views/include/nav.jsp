<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<nav>
    <div id="menu">
        <ul>
            <li><a href="/board/home" class="app-name">HAPPY DIARY</a></li>
            <li><a href="/board/list?visibility=private&pno=1">PRIVATE</a>
                <ul>
                    <li><a href="/board/list?visibility=private&pno=1">To Me</a></li>
                    <li><a href="#">My Diary</a></li>
                </ul>
            </li>
            <li><a href="/board/list?visibility=public&pno=1">PUBLIC</a>
                <ul>
                    <li><a href="/board/list?visibility=public&pno=1">To You</a></li>
                    <li><a href="#">X's Diary</a></li>
                </ul>
            </li>
            <li><a href="/board/notice">BOARD</a>
                <ul>
                    <li><a href="/board/notice">Notice</a></li>
                    <li><a href="#">Community</a></li>
                </ul>
            </li>
            <li><a href="/userInfo/modify">SETTING</a>
                <ul>
                    <li><a href="/userInfo/modify">Edit</a></li>
                    <li><a href="#" id="logout">Logout</a></li>
                    <li><a href="/delete/account" id="accountDeletion">Unsubscribe</a></li>
                </ul>
            </li>
        </ul>
        <div class="user-info">
            <ul>
                <a href="#" class="happy">HAPPY</a>
                <a href="#" class="user-name">${sessionScope.userName}</a>
            </ul>
        </div>
    </div>
</nav>
</html>

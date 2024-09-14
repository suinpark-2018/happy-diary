<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<nav>
    <div id="menu">
        <ul>
            <li><a href="/board/home" class="app-name">HAPPY DIARY</a></li>
            <li><a href="/board/list?visibility=private&pno=1">개인 공간</a>
                <ul>
                    <li><a href="/board/list?visibility=private&pno=1">나에게</a></li>
                    <li><a href="#">나의 일기</a></li>
                </ul>
            </li>
            <li><a href="/board/list?visibility=public&pno=1">공용 공간</a>
                <ul>
                    <li><a href="/board/list?visibility=public&pno=1">너에게</a></li>
                    <li><a href="#">X의 일기</a></li>
                </ul>
            </li>
            <li><a href="#">게시판</a>
                <ul>
                    <li><a href="#">공지사항</a></li>
                    <li><a href="#">커뮤니티</a></li>
                </ul>
            </li>
            <li><a href="#">설정</a>
                <ul>
                    <li><a href="#">회원정보 수정</a></li>
                    <li><a href="#" id="logout">로그아웃</a></li>
                    <li><a href="#">회원탈퇴</a></li>
                </ul>
            </li>
        </ul>
        <div class="user-info">
            <ul>
                <a href="#" id="user-name">HAPPY ${sessionScope.userName}</a>
            </ul>
        </div>
    </div>
</nav>
</html>

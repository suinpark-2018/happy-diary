<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main|Happy Diary</title>
</head>
<link rel="stylesheet" href="/resources/css/nav.css">
<link rel="stylesheet" href="/resources/css/main.css">

<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp"/>

    <div class="container">
        <div class="left-side">
            <p class="question-sentence">How was your day?</p>
            <p class="temporary-text">해당 서비스는 추후 제공 예정입니다.</p>
<%--            <table class="latest-posts">--%>
<%--                <c:forEach var="diary" items="${diaries}" begin="0" end="5">--%>
<%--                    <tr>--%>
<%--                        <td>${diary.writer}</td>--%>
<%--                        <td>${diary.title}</td>--%>
<%--                    </tr>--%>
<%--                </c:forEach>--%>
<%--            </table>--%>
        </div>
        <div class="main-board">
            <img src="/resources/img/main.webp" alt="main_board_img">
        </div>
        <div class="right-side">
            <p class="question-sentence">Who would you like to praise?</p>
            <c:if test="${empty boards}">
                <p class="temporary-text">최근 게시물이 존재하지 않습니다.</p>
            </c:if>
            <table class="latest-posts">
                <c:forEach var="board" items="${boards}" begin="0" end="5">
                    <tr>
                        <td>${board.writer}</td>
                        <td><a href="/board/detail?visibility=public&bno=${board.bno}&pno=1" id="board-title">${board.title}</a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="/resources/js/nav.js"></script>
</html>


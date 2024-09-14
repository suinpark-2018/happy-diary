<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Found</title>
</head>
<link rel="stylesheet" href="/resources/css/nav.css">
<link rel="stylesheet" href="/resources/css/foundBoards.css">
<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp"/>
    <div class="wrapper">
        <h2>검색된 게시물 목록</h2>
        <div class="btn-wrapper">
            <button type="button" class="btn" onclick="location.href='/board/home'">Home</button>
            <button type="button" class="btn" onclick="location.href='/board/list?visibility=${visibility}&pno=1'">전체목록</button>
        </div>
        <table class="board">
            <thead>
            <tr>
                <th>No.</th>
                <th>제목</th>
                <th>To.X</th>
                <th>작성자</th>
                <th>작성일</th>
                <th>조회수</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="board" items="${foundBoards}">
                <tr>
                    <td>${board.bno}</td>
                    <td><a href="/board/detail?visibility=${visibility}&bno=${board.bno}&pno=${pno}">${board.title}</a></td>
                    <td>${board.praise_target}</td>
                    <td>${board.writer}</td>
                    <td><fmt:formatDate value="${board.reg_date}" pattern="yyyy-MM-dd" /></td>
                    <td>${board.view_cnt}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <%-- Pagination --%>
        <div class="pagination">
            <c:if test="${pageDto.prev}">
                <span onclick="location.href='/board/find?pno=${pageDto.start - 1}'">PREV</span>
            </c:if>
            <c:forEach begin="${pageDto.start}" end="${pageDto.end}" var="num">
                <c:choose>
                    <c:when test="${pageDto.page == num}">
                        <span class="active">${num}</span>
                    </c:when>
                    <c:otherwise>
                        <span onclick="location.href='/board/find?pno=${num}&visibility=${visibility}&option=${option}&keyword=${keyword}'">${num}</span>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${pageDto.next}">
                <span onclick="location.href='/board/find?pno=${pageDto.end + 1}&option=${option}&keyword=${keyword}'">NEXT</span>
            </c:if>
        </div>
    </div>
</body>
<script src="/resources/js/nav.js"></script>
</html>

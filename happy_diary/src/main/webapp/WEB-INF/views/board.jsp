<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>게시판|Happy Diary</title>
</head>
<link rel="stylesheet" href="/resources/css/nav.css">
<link rel="stylesheet" href="/resources/css/board.css">
<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp"/>
    <div class="wrapper">
        <c:if test="${param.visibility eq 'private'}"><h1>Private Space</h1></c:if>
        <c:if test="${param.visibility eq 'public'}"><h1>Shared Space</h1></c:if>

        <div class="search-wrapper">
            <form action="/board/find" method="GET">
                <input type="hidden" name="visibility" value="${visibility}">
                <select name="option">
                    <option value="title">제목</option>
                    <option value="writer">작성자</option>
                    <option value="title_or_writer">제목+작성자</option>
                </select>
                <input type="text" name="keyword" placeholder="검색어를 입력하세요">
                <button type="submit">검색</button>
            </form>
        </div>
        <div class="btn-wrapper">
            <a href="/board/home" class="btn">HOME</a>
            <a href="/board/create?visibility=${visibility}" class="btn">글쓰기</a>
        </div>
        <table class="board">
            <thead>
                <tr>
                    <th>To.X</th>
                    <th>제목</th>
                    <th>작성자</th>
                    <th>작성일</th>
                    <th>조회수</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="board" items="${boards}">
                    <tr>
                        <td>${board.praise_target}</td>
                        <td><a href="/board/detail?visibility=${visibility}&bno=${board.bno}&pno=${param.pno}">${board.title}</a></td>
                        <td>${board.writer}</td>
                        <td><fmt:formatDate value="${board.reg_date}" pattern="yyyy-MM-dd" /></td>
                        <td>${board.view_cnt}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <%-- 페이지 번호 --%>
        <div class="pagination">
            <c:if test="${pageDto.prev}">
                <span onclick="location.href='/board/list?visibility=${visibility}&pno=${pageDto.start - 1}'">PREV</span>
            </c:if>
            <c:forEach begin="${pageDto.start}" end="${pageDto.end}" var="num">
                <c:choose>
                    <c:when test="${pageDto.page == num}">
                        <span class="active">${num}</span>
                    </c:when>
                    <c:otherwise>
                        <span onclick="location.href='/board/list?visibility=${visibility}&pno=${num}'">${num}</span>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${pageDto.next}">
                <span onclick="location.href='/board/list?visibility=${visibility}&pno=${pageDto.end + 1}'">NEXT</span>
            </c:if>
        </div>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="/resources/js/nav.js"></script>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시물 수정|Happy Diary</title>
</head>
<link rel="stylesheet" href="/resources/css/nav.css">
<link rel="stylesheet" href="/resources/css/modifyBoard.css">
<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp"/>
    <div class="header">게시판 수정</div>
    <div class="wrapper">
        <form action="/board/modify?bno=${board.bno}&pno=${pno}" method="POST">
            <table>
                <tr>
                    <td><label for="praise_target">To. </label></td>
                    <td><input type="text" id="praise_target" name="praise_target" value="${board.praise_target}"></td>
                <tr>
                    <td><label for="title">제목</label></td>
                    <td><input type="text" id="title" name="title" value="${board.title}" required></td>
                </tr>
                <tr>
                    <td><label for="content">내용</label></td>
                    <td><textarea id="content" name="content" required>${board.content}</textarea></td>
                </tr>
                <tr>
                    <td>공개범위</td>
                    <c:if test="${board.is_public eq 'Y'}">
                        <td>
                            <label><input type="radio" name="is_public" value="Y" checked required>전체공개</label>
                            <label><input type="radio" name="is_public" value="N" required>비공개</label>
                        </td>
                    </c:if>
                    <c:if test="${board.is_public eq 'N'}">
                        <td>
                            <label><input type="radio" name="is_public" value="Y" required>전체공개</label>
                            <label><input type="radio" name="is_public" value="N" checked required>비공개</label>
                        </td>
                    </c:if>
                </tr>
                <tr>
                    <td colspan="3" class="actions">
                        <button type="button" id="list-btn" onclick="location.href='/board/list?visibility=${param.visibility}&pno=${param.pno}'">목록</button>
                        <button type="button" id="cancel-btn" onclick="location.href='/board/detail?visibility=${param.visibility}&bno=${board.bno}&pno=${param.pno}'">취소</button>
                        <button type="submit" id="submit-btn">저장</button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="/resources/js/nav.js"></script>
</html>

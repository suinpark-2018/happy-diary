<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시물|Happy Diary</title>
</head>
<link rel="stylesheet" href="/resources/css/nav.css">
<link rel="stylesheet" href="/resources/css/boardDetail.css">
<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp"/>
    <c:if test="${param.visibility eq 'private'}"><div class="header">For Myself</div></c:if>
    <c:if test="${param.visibility eq 'public'}"><div class="header">To Someone</div></c:if>
    <div class="wrapper">
        <ul class="visible-range"><c:if test="${param.visibility eq 'private'}">Private</c:if></ul>
        <ul class="visible-range"><c:if test="${param.visibility eq 'public'}">Public</c:if></ul>
        <br>
        <p class="subject">To. ${board.praise_target}</p>
        <p class="title">제목</p>
        <p class="title-box">${board.title}</p><br>
        <p class="content">내용</p>
        <p class="content-box">${board.content}</p>

        <ul class="view_cnt">조회수: ${board.view_cnt}</ul>

        <div class="actions">
            <button type="button" id="list-btn">
                <a href="/board/list?visibility=${param.visibility}&pno=${param.pno}">목록</a>
            </button>

            <c:if test="${isAuthor}">
                <button type="button" id="delete-btn">
                    <a href="/board/delete?visibility=${param.visibility}&bno=${board.bno}&pno=${param.pno}" onclick="return confirmDelete()">삭제</a>
                </button>
                <button type="button" id="modify-btn">
                    <a href="/board/modify?visibility=${param.visibility}&bno=${board.bno}&pno=${param.pno}">수정</a>
                </button>
            </c:if>
        </div>

        <!-- 댓글 입력 폼 -->
        <div class="comment-form">
            <form action="/comment/add?pno=${param.pno}&visibility=${param.visibility}" method="post">
                <input type="hidden" name="bno" value="${board.bno}">
                <input type="hidden" name="pno" value="${param.pno}">
                <textarea name="content" placeholder="댓글을 입력하세요" rows="3"></textarea>
                <input type="hidden" name="writer" value="${sessionScope.userId}">
                <button type="submit" class="submit-btn">댓글 달기</button>
            </form>
        </div>

        <!-- 댓글 목록 -->
        <div class="comments">
            <p class="comment-title">댓글</p>
            <c:forEach var="comment" items="${comments}">
                <c:if test="${comment.parent_cno == null}">
                    <div id="comment-${comment.cno}" class="comment">
                        <p><strong>${comment.writer}</strong> (${comment.reg_date}):</p>
                        <span id="comment-content-${comment.cno}">${comment.content}</span>
                        <c:if test="${comment.writer eq sessionScope.userId}">
                            <button type="button" class="modify-comment" value="${comment.cno}">수정</button>
                            <button type="button" class="delete-comment" value="${comment.cno}">삭제</button>
                        </c:if>

                        <!-- 댓글 수정 폼 -->
                        <div id="modify-comment-form">
                            <textarea id="modify-comment-content">${comment.content}</textarea>
                            <button type="button" id="save-modified-comment" value="${comment.cno}">저장</button>
                        </div>

                        <!-- 답글 목록 -->
                        <c:forEach var="reply" items="${comment.replies}">
                            <div id="reply-${reply.cno}" class="reply">
                                <p><strong>${reply.writer}</strong> (${reply.reg_date}):</p>
                                <span id="reply-content-${reply.cno}">${reply.content}</span>
                                <c:if test="${reply.writer eq sessionScope.userId}">
                                    <button type="button" class="modify-reply" value="${reply.cno}">수정</button>
                                    <button type="button" class="delete-reply" value="${reply.cno}">삭제</button>
                                </c:if>
                            </div>

                            <!-- 답글 수정 폼 -->
                            <div id="modify-reply-form">
                                <textarea id="modify-reply-content">${reply.content}</textarea>
                                <button type="button" id="save-modified-reply" value="${reply.cno}">저장</button>
                            </div>
                        </c:forEach>

                        <!-- 답글 입력 폼 -->
                        <div class="reply-form">
                            <form action="/comment/reply?pno=${param.pno}&visibility=${param.visibility}" method="post">
                                <input type="hidden" name="bno" value="${board.bno}">
                                <input type="hidden" name="cno" value="${comment.cno}">
                                <input type="hidden" name="parent_cno" value="${comment.cno}">
                                <input type="hidden" name="writer" value="${sessionScope.userId}">
                                <textarea name="content" placeholder="답글을 입력하세요" rows="2"></textarea>
                                <button type="submit" class="submit-btn">답글 달기</button>
                            </form>
                        </div>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="/resources/js/nav.js"></script>
<script src="/resources/js/boardDetail.js"></script>
</html>

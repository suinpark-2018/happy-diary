<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<link rel="stylesheet" href="/resources/css/nav.css">
<link rel="stylesheet" href="/resources/css/createBoard.css"/>
<body>
    <jsp:include page="/WEB-INF/views/include/nav.jsp"/>
    <div class="wrapper">
        <h1>글쓰기</h1>
        <form action="/board/create" method="post">
            <table>
                <tr>
                    <td><label for="praise_target">To. </label></td>
                    <td><input type="text" id="praise_target" name="praise_target"></td>
                </tr>
                <tr>
                    <td><label for="title">제목</label></td>
                    <td><input type="text" id="title" name="title" required></td>
                </tr>
                <tr>
                    <td><label for="content">내용</label></td>
                    <td><textarea id="content" name="content" required></textarea></td>
                </tr>
                <tr>
                    <td><label for="writer">작성자</label></td>
                    <td><input type="text" id="writer" name="writer" value=${userId} readonly></td>
                </tr>
                <tr>
                    <td>공개범위</td>
                    <td>
                        <label><input type="radio" name="is_public" value="Y" required><span class="visibility-type">전체공개</span></label>
                        <label><input type="radio" name="is_public" value="N" required><span class="visibility-type">비공개</span></label>
                    </td>
                </tr>
                <input type="hidden" name="is_notice" value="N">
            </table>
            <button type="submit">SUBMIT</button>
            <button type="button" onclick="location.href='/board/list?visibility=${visibility}&pno=1'">CANCEL</button>
        </form>
    </div>
</body>
<script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
<script src="/resources/js/nav.js"></script>
</html>

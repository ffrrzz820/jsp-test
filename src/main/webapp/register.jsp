<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2024-09-11
  Time: 오후 10:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
    <h2>회원가입</h2>
    <form action="RegisterServlet" method="post">
        <label for="username">아이디:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">비밀번호:</label>
        <input type="text" id="password" name="password" required><br><br>

        <label for="email">이메일:</label>
        <input type="text" id="email" name="email" required><br><br>

        <label for="name">이름:</label>
        <input type="text" id="name" name="name" required><br><br>

        <label for="nickname">닉네임:</label>
        <input type="text" id="nickname" name="nickname" required><br><br>

        <label for="gender">성별:</label>
        <select name="gender" id="gender">
            <option value="male">남성</option>
            <option value="female">여성</option>
        </select><br><br>

        <input type="submit" value="가입하기">
    </form>
</body>
</html>

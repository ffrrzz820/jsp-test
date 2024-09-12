<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 2024-09-12
  Time: 오전 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입 결과</title>
</head>
<body>
    <h2>회원가입 결과</h2>
    <p>
        <%= request.getAttribute("message")%>
    </p>
    <a href="register.jsp">다시 회원가입</a>
</body>
</html>

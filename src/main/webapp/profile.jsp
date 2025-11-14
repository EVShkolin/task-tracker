<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page session="false" %>
<html>
<head>
  <title>Profile</title>
</head>
<body>
  <h1>Профиль</h1>
  <h2>
    <c:out value="${user.username}"></c:out>
  </h2>
  <p>
    <c:out value="${user.id}"></c:out>
  </p>
  <br>
  <form id="username-form">
    <input type="text" name="newUsername" placeholder="New username" required>
    <button type="submit">Change Username</button>
  </form>
  <br>
  <form id="password-form">
    <input type="password" name="newPassword" placeholder="New password" required/>
    <input type="password" name="confirmPassword" placeholder="Confirm password" required/>
    <button type="submit">Change Password</button>
  </form>
  <br>
  <button id="delete-account-button">Delete account</button>

<script>
    window.APP_CONFIG = {
        contextPath: "${pageContext.request.contextPath}",
        userId: "${user.id}"
    };
</script>
<script src="js/profile.js"></script>
</body>
</html>

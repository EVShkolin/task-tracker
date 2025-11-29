<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page session="false" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
  <title>Login</title>
</head>
<body>
<div class="form-container">
  <form action="${pageContext.request.contextPath}/login" method="POST" class="form">
    <h1 class="page-type">Log in</h1>
    <div class="form form__username">
      <label for="username">Username: </label>
      <input class="input" type="text" name="username" id="username" required min="5" />
    </div>

    <div class="form ">
      <label for="password">Password: </label>
      <input class="input" type="password" name="password" id="password" required />
    </div>

    <div class="form">
      <input class="input form__submit-button" type="submit" value="Log in" />
    </div>
    <p>Don't have account? <a class="sign-up" href="${pageContext.request.contextPath}/register">Sign up</a></p>
  </form>
</div>
</body>
</html>
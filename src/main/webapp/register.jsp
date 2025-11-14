<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>
<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>Register</title>
</head>
<body>
<form class="form" method="post" action="${pageContext.request.contextPath}/register">
  <h1 class="page-type">Create account</h1>
  <div class="form-field">
    <label for="username">Username: </label>
    <input class="input" type="text" name="username" id="username" minlength="5" required
        title="Username must be at least 5 characters long"/>
  </div>

  <div class="form-field">
    <label for="password">Password: </label>
    <input class="input" type="password" name="password" id="password" minlength="5" required
        title="Password must be at least 5 characters long"/>
  </div>

  <div class="checkbox-container">
    <label for="checkbox">Your data is not yours anymore</label>
    <input class="checkbox" type="checkbox" id="checkbox" required/>
  </div>

  <div class="form">
    <input class="input form__submit-button" type="submit" value="Create account"/>
  </div>
</form>
</body>
</html>
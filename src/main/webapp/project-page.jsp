<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page session="false" %>
<html>
<head>
  <title>Project</title>
</head>
<body>
  <h2>
    <c:out value="${project.title}"></c:out>
  </h2>
  <p>
    <c:out value="id: ${project.id}"></c:out>
  </p>
  <br>
  <p>
    <c:out value="${project.description}"></c:out>
  </p>
</body>
</html>

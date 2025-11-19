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
  <br>
  <div class="members">
    <c:choose>
      <c:when test="${empty project.members}">
        <p>No members</p>
      </c:when>
      <c:otherwise>
        <h5>Members:</h5>
        <c:forEach items="${project.members}" var="member">
          <div id="member_${member.id}" class="member">
            <c:out value="${member.username}"></c:out>
            <c:out value="${member.role}"></c:out>
          </div>
        </c:forEach>
      </c:otherwise>
    </c:choose>
  </div>

</body>
</html>

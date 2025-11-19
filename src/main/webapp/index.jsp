<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
  <title>Main</title>
</head>
<body>
<div class="projects">
  <c:choose>
    <c:when test="${empty user.projects}">
      <p>No projects</p>
    </c:when>
    <c:otherwise>
      <h5>Projects:</h5>
      <c:forEach items="${user.projects}" var="project">
        <div id="project_${project.id}" class="project">
          <a href="${pageContext.request.contextPath}/projects/${project.id}">
              <c:out value="${project.title}"></c:out>
          </a>
        </div>
      </c:forEach>
    </c:otherwise>
  </c:choose>
</div>
</body>
</html>
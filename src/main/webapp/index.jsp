<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/project-header.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/popups.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/project-default.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css"/>

  <title>Main</title>
</head>
<body>
<header>
  <div class="header">
    <div class="logo">
      <img class="image" src="https://cdn-icons-png.flaticon.com/512/5968/5968875.png" alt="Logo"/>
    </div>
    <div>
      <a class="profile" href="${pageContext.request.contextPath}/profile">
        <img class="image" src="https://static.vecteezy.com/system/resources/thumbnails/032/176/191/small/business-avatar-profile-black-icon-man-of-user-symbol-in-trendy-flat-style-isolated-on-male-profile-people-diverse-face-for-social-network-or-web-vector.jpg" alt="Profile">
        <p>Username</p>
      </a>
    </div>
  </div>
</header>

<div id="projects" class="projects">
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

<div>
  <button id="addProjectBtn" class="btn">New project</button>
</div>

<div id="projectPopup" class="popup">
  <div class="popup-content">
    <span class="close-btn">&times;</span>
    <h2 class="popup-action-text">Create new project</h2>
    <form id="projectForm" class="task-form">
      <div class="form-group">
        <label for="projectTitle">Add a title</label>
        <input type="text" id="projectTitle" name="projectTitle" placeholder="Title" required maxlength="50" />
      </div>

      <div class="form-group">
        <label for="projectDescription">Description</label>
        <input type="text" id="projectDescription" name="projectDescription" placeholder="Type your description here..." maxlength="255"/>
      </div>

      <div class="form-group">
        <button id="submitProjectBtn" type="submit" class="btn create-btn">Create</button>
      </div>
    </form>

  </div>
</div>

<script>
  const contextPath = '${pageContext.request.contextPath}';
  const userId = ${user.id};
</script>

<script src="${pageContext.request.contextPath}/js/index.js"></script>
</body>
</html>
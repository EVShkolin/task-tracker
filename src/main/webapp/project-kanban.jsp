<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page session="false" %>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport"
        content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">

  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/project-header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/project-kanban.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/popups.css">

  <title>${project.title}</title>
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

<section class="project-header">
  <div class="project-info">
    <h1 class="project-title">Project name</h1>
  </div>
  <nav class="nav">
    <ul class="menu">
      <li class="menu-item">
        <a href="${pageContext.request.contextPath}/projects/${project.id}">Main</a>
      </li>
      <li class="menu-item menu-item__active">
        <a href="${pageContext.request.contextPath}/projects/${project.id}/kanban">Kanban desk</a>
      </li>
    </ul>
  </nav>
</section>

<main class="kanban-main">
  <section class="kanban-section">
    <div class="kanban-board">
      <c:forEach items="${project.cards}" var="card">
        <div id="card-${card.id}" class="kanban-card">
          <div class="card-header">
            <div class="status-indicator" style="background-color: ${card.color}"></div>
            <h3 class="card-title">${card.title}</h3>
          </div>
          <p class="card-description">${card.description}</p>
          <ul class="task-list">
            <c:forEach items="${card.tasks}" var="task">
              <li id="task-${task.id}" class="task-item" draggable="true">
                <div class="task-title">${task.title}</div>
              </li>
            </c:forEach>
          </ul>
          <button class="add-task-btn">+ Add task</button>
        </div>
      </c:forEach>
      <div class="add-card-btn-container">
        <button id="addCardBtn" class="add-card-btn">Add Card</button>
      </div>
    </div>
  </section>
</main>

<div id="taskPopup" class="popup">
  <div class="popup-content">
    <span class="close-btn">&times;</span>
    <h2 class="popup-action-text">Create new task</h2>
    <form id="taskForm" class="task-form">
      <div class="form-group">
        <label for="taskTitle">Add a title</label>
        <input type="text" id="taskTitle" name="taskTitle" placeholder="Title" required maxlength="50" />
      </div>

      <div class="form-group">
        <label for="taskDescription">Description</label>
        <textarea id="taskDescription" name="taskDescription" rows="4" placeholder="Type your description here..."></textarea>
      </div>

      <div class="form-group">
        <button id="submitTaskBtn" type="submit" class="btn create-btn">Create</button>
      </div>
    </form>
  </div>
</div>

<div id="cardPopup" class="popup">
  <div class="popup-content">
    <span class="close-btn">&times;</span>
    <h2 class="popup-action-text">Create new card</h2>
    <form id="cardForm" class="card-form">
      <div class="form-group">
        <label for="cardTitle">Title</label>
        <input type="text" id="cardTitle" name="cardTitle" placeholder="Title" required maxlength="15" />
      </div>
      <div class="form-group">
        <label>Color</label>
        <div class="color-options">
          <label class="color-option">
            <input type="radio" name="cardColor" value="#9198a1" required>
            <span class="color-circle" style="background-color: #9198a1"></span>
          </label>
          <label class="color-option">
            <input type="radio" name="cardColor" value="#4493f8" required>
            <span class="color-circle" style="background-color: #4493f8"></span>
          </label>
          <label class="color-option">
            <input type="radio" name="cardColor" value="#3fb950" required>
            <span class="color-circle" style="background-color: #3fb950"></span>
          </label>
          <label class="color-option">
            <input type="radio" name="cardColor" value="#d29922" required>
            <span class="color-circle" style="background-color: #d29922"></span>
          </label>
          <label class="color-option">
            <input type="radio" name="cardColor" value="#db6d28" required>
            <span class="color-circle" style="background-color: #db6d28"></span>
          </label>
          <label class="color-option">
            <input type="radio" name="cardColor" value="#f85149" required>
            <span class="color-circle" style="background-color: #f85149"></span>
          </label>
          <label class="color-option">
            <input type="radio" name="cardColor" value="#db61a2" required>
            <span class="color-circle" style="background-color: #db61a2"></span>
          </label>
          <label class="color-option">
            <input type="radio" name="cardColor" value="#ab7df8" required>
            <span class="color-circle" style="background-color: #ab7df8"></span>
          </label>
        </div>
      </div>

      <div class="form-group">
        <label for="cardDescription">Description</label>
        <textarea id="cardDescription" name="cardDescription" placeholder="Description" maxlength="100" rows="3"></textarea>
      </div>

      <div class="create-btn-container form-group">
        <button type="submit" class="btn create-btn">Create</button>
      </div>
    </form>
  </div>
</div>

<script>
  const contextPath = "${pageContext.request.contextPath}";
  const projectId = "${project.id}";
</script>
<script src="${pageContext.request.contextPath}/js/drag.js"></script>
<script src="${pageContext.request.contextPath}/js/project-kanban.js"></script>
</body>
</html>
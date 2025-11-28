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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/project-main.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/popups.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/task-info-popup.css"/>

  <title>${project.title}</title>
</head>
<body>
<header>
  <div class="header">
    <div class="logo">
      <img class="image" src="https://static.vecteezy.com/system/resources/previews/009/481/029/non_2x/geometric-icon-logo-geometric-abstract-element-free-vector.jpg" alt="Logo"/>
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
    <h1 class="project-title">${project.title}</h1>
  </div>
  <nav class="nav">
    <ul class="menu">
      <li class="menu-item menu-item__active">
        <a href="${pageContext.request.contextPath}/projects/${project.id}">Main</a>
      </li>
      <li class="menu-item">
        <a href="${pageContext.request.contextPath}/projects/${project.id}/kanban">Kanban desk</a>
      </li>
    </ul>
  </nav>
</section>

<main class="container">
  <section class="section task-section">
    <div class="section-header">
      <h3 class="section-header__title">Tasks</h3>
      <button id="addTaskBtn" class="btn section-header__button">Add Task</button>
    </div>
    <div class="task-list-container">
      <ul class="task-list">
        <c:forEach items="${project.cards}" var="card">
          <c:forEach items="${card.tasks}" var="task">
            <li id="task-${task.id}" class="task-item">
              <div class="task-title-container">
                <p class="task-title">${task.title}</p>
                <div class="status-badge " style="background-color: ${card.color}">${card.title}</div>
              </div>
              <button class="delete-task-btn" data-task-id="${task.id}">×</button>
            </li>
          </c:forEach>
        </c:forEach>
      </ul>
    </div>
  </section>

  <section class="section member-section">
    <div class="section-header">
      <h3 class="section-header__title">Members</h3>
      <button id="inviteBtn" class="btn section-header__button">Invite Member</button>
    </div>
    <div class="member-list-container">
      <ul class="member-list">
        <c:forEach items="${project.members}" var="member">
          <li id="member-${member.id}" class="member-item">
            <img class="member-avatar" src="https://static.vecteezy.com/system/resources/thumbnails/032/176/191/small/business-avatar-profile-black-icon-man-of-user-symbol-in-trendy-flat-style-isolated-on-male-profile-people-diverse-face-for-social-network-or-web-vector.jpg" alt="User">
            <div class="member-info">
              <div class="member-name">${member.username}</div>
              <div class="member-role">${member.role}</div>
            </div>
          </li>
        </c:forEach>
      </ul>
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
<div id="invitePopup" class="popup">
  <div class="popup-content">
    <span class="close-btn">&times;</span>
    <h2 class="popup-action-text">Invite new member</h2>
    <form id="inviteForm" class="task-form">
      <div class="form-group">
        <label for="inviteUser">User id</label>
        <input type="text" id="inviteUser" name="user-id" placeholder="User id" required maxlength="10" />
      </div>
      <div class="form-group">
        <button type="submit" class="btn create-btn">Invite</button>
      </div>
    </form>
  </div>
</div>

<jsp:include page="task-info-popup.jsp"/>

<script>
    const contextPath = '${pageContext.request.contextPath}';
    const projectId = ${project.id};
    console.log('ContextPath = ' + contextPath + ' projectId = ' + projectId);

    const members = [
        <c:forEach items="${project.members}" var="member" varStatus="loop">
        {
            id: ${member.id},
            username: "${member.username}",
            role: "${member.role}"
        }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];
    console.log('Members ' + members.length);

    const cards = [
        <c:forEach items="${project.cards}" var="card" varStatus="loop">
        {
            id: ${card.id},
            title: "${card.title}",
            description: "${card.description}",
            color: "${card.color}",
            displayOrder: ${card.displayOrder},
            projectId: ${card.projectId}
        }<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];
    console.log('Cards ' + cards.length);

    const noStatusCard = cards.find(card => card.displayOrder === 0);
    const noStatusCardId = noStatusCard ? noStatusCard.id : null;
    console.log('No status card ID:', noStatusCardId);
</script>
<script src="${pageContext.request.contextPath}/js/project-main.js"></script>
<script src="${pageContext.request.contextPath}/js/task-info-popup.js"></script>
</body>
</html>

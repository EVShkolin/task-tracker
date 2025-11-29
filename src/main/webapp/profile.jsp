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
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/popups.css">

  <title>Profile - ${user.username}</title>
</head>
<body>
<header>
  <div class="header">
    <a class="logo" href="${pageContext.request.contextPath}/home">
      <img class="image" src="https://cdn-icons-png.flaticon.com/512/5968/5968875.png" alt="Logo"/>
    </a>
    <div>
      <a class="profile" href="${pageContext.request.contextPath}/profile">
        <img class="image" src="https://static.vecteezy.com/system/resources/thumbnails/032/176/191/small/business-avatar-profile-black-icon-man-of-user-symbol-in-trendy-flat-style-isolated-on-male-profile-people-diverse-face-for-social-network-or-web-vector.jpg" alt="Profile">
        <p>${user.username}</p>
      </a>
    </div>
  </div>
</header>

<main class="profile-container">
  <section class="section profile-section">
    <div class="section-header">
      <h3 class="section-header__title">Profile Information</h3>
    </div>
    <div class="profile-content">
      <div class="profile-avatar">
        <img src="https://static.vecteezy.com/system/resources/thumbnails/032/176/191/small/business-avatar-profile-black-icon-man-of-user-symbol-in-trendy-flat-style-isolated-on-male-profile-people-diverse-face-for-social-network-or-web-vector.jpg"
             alt="Profile Avatar" class="profile-avatar__image">
      </div>
      <div class="profile-info">
        <div class="profile-field">
          <label class="profile-field__label">User ID</label>
          <p class="profile-field__value">${user.id}</p>
        </div>
        <div class="profile-field">
          <label class="profile-field__label">Username</label>
          <p class="profile-field__value">${user.username}</p>
        </div>
      </div>
    </div>
  </section>

  <section class="section account-management-section">
    <div class="section-header">
      <h3 class="section-header__title">Account Management</h3>
    </div>
    <div class="account-management-buttons">
      <button id="change-username-btn" class="btn btn-secondary">Change Username</button>
      <button id="change-password-btn" class="btn btn-secondary">Change Password</button>
      <a class="btn btn-secondary" href="${pageContext.request.contextPath}/logout">Logout</a>
      <button id="delete-account-btn" class="btn btn-danger">Delete Account</button>
    </div>
  </section>

  <section class="section invitations-section">
    <div class="section-header">
      <h3 class="section-header__title">Project Invitations</h3>
    </div>
    <div class="invitations-list-container">
      <c:choose>
        <c:when test="${not empty invitations}">
          <ul class="invitations-list">
            <c:forEach items="${invitations}" var="invitation">
              <li id="invitation-${invitation.id}" class="invitation-item">
                <div class="invitation-info">
                  <h4 class="invitation-project-title">${invitation.projectTitle}</h4>
                  <div class="invitation-meta">
                    <span class="invitation-date">Invited on: ${invitation.createdAt}</span>
                    <span class="invitation-status">Status: ${invitation.status}</span>
                  </div>
                </div>
                <div class="invitation-actions">
                  <c:if test="${invitation.status == 'PENDING'}">
                    <button class="btn btn-accept"
                            data-invitation-id="${invitation.id}"
                            data-project-id="${invitation.projectId}">
                      Accept
                    </button>
                    <button class="btn btn-decline"
                            data-invitation-id="${invitation.id}"
                            data-project-id="${invitation.projectId}">

                      Decline
                    </button>
                  </c:if>
                  <c:if test="${invitation.status != 'PENDING'}">
                    <span class="invitation-resolved">${invitation.status}</span>
                  </c:if>
                </div>
              </li>
            </c:forEach>
          </ul>
        </c:when>
        <c:otherwise>
          <div class="no-invitations">
            <p>No pending invitations</p>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </section>
</main>

<div id="usernamePopup" class="popup">
  <div class="popup-content">
    <span class="close-btn">&times;</span>
    <h2 class="popup-action-text">Change Username</h2>
    <form id="username-form" class="profile-form">
      <div class="form-group">
        <label for="newUsername">New Username</label>
        <input type="text" id="newUsername" name="newUsername" placeholder="Enter new username" required>
      </div>
      <div class="form-group">
        <button type="submit" class="btn btn-primary">Update Username</button>
      </div>
    </form>
  </div>
</div>

<div id="passwordPopup" class="popup">
  <div class="popup-content">
    <span class="close-btn">&times;</span>
    <h2 class="popup-action-text">Change Password</h2>
    <form id="password-form" class="profile-form">
      <div class="form-group">
        <label for="newPassword">New Password</label>
        <input type="password" id="newPassword" name="newPassword" placeholder="Enter new password" required>
      </div>
      <div class="form-group">
        <label for="confirmPassword">Confirm Password</label>
        <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm new password" required>
      </div>
      <div class="form-group">
        <button type="submit" class="btn btn-primary">Update Password</button>
      </div>
    </form>
  </div>
</div>

<div id="deleteAccountPopup" class="popup">
  <div class="popup-content">
    <span class="close-btn">&times;</span>
    <h2 class="popup-action-text">Delete Account</h2>
    <div class="delete-confirmation">
      <p>Are you sure you want to delete your account? This action cannot be undone.</p>
      <div class="confirmation-buttons">
        <button id="confirm-delete-btn" class="btn btn-danger">Yes, Delete Account</button>
        <button id="cancel-delete-btn" class="btn btn-secondary">Cancel</button>
      </div>
    </div>
  </div>
</div>

<script>
    const contextPath = '${pageContext.request.contextPath}';
    const userId = ${user.id};
</script>

<script src="${pageContext.request.contextPath}/js/profile.js"></script>
</body>
</html>
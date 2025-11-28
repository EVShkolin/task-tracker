<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page session="false" %>

<div id="taskInfoPopup" class="task-info-popup">
  <div class="task-info-header">
    <h1 id="tName"></h1> <!-- Task name -->
    <div>
      <button class="edit-btn">Edit</button>
    </div>
    <span class="close-info-btn">&times;</span>
  </div>

  <div class="task-info-content">
    <div class="task-info-description">
      <h3>Description</h3>
      <p id="tContent"></p>
    </div>

    <div class="task-info">
      <ul class="task-details">
        <li class="task-detail-item">
          <p class="task-detail-label">Status</p>
          <p id="tStatus" class="task-detail-value"></p>
        </li>
        <li class="task-detail-item">
          <p class="task-detail-label">Created</p>
          <p id="tDate" class="task-detail-value"></p>
        </li>
      </ul>

      <div class="assignee-header">
        <h3 class="assignee-title">Assignees</h3>
        <button class="add-assignee-btn" title="Add assignee">+</button>
      </div>

      <ul id="assigneeList" class="assignee-list"></ul>

    </div>
  </div>

  <div class="task-info-comments">
    <h3>Comments</h3>
    <form id="commentForm" class="comment-form">
      <div class="comment-input-container">
        <textarea
            class="comment-textarea"
            name="comment-content"
            placeholder="Write a comment..."
            rows="4"
        ></textarea>
        <button class="comment-submit-btn">Send</button>
      </div>
    </form>

    <ul id="commentList" class="comment-list"></ul>
  </div>
</div>

<div id="assigneeSelectPopup" class="popup">
  <div class="popup-content">
    <span class="close-btn assignee-select-close">&times;</span>
    <h2 class="popup-action-text">Select Assignee</h2>
    <ul id="assigneeSelectList" class="assignee-select-list"></ul>
  </div>
</div>

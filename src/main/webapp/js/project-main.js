function openPopup(popupElement) {
    popupElement.style.display = 'block';
}

function closePopup(popupElement) {
    popupElement.style.display = 'none';
}

function initPopup(popupId, openButtonId) {
    const popup = document.getElementById(popupId);
    const openBtn = document.getElementById(openButtonId);
    const closeBtn = popup.querySelector('.close-btn');

    openBtn.addEventListener('click', () => openPopup(popup));
    closeBtn.addEventListener('click', () => closePopup(popup));

    popup.addEventListener('click', (e) => {
        if (e.target === popup) {
            closePopup(popup);
        }
    });
}

initPopup('taskPopup', 'addTaskBtn');
initPopup('invitePopup', 'inviteBtn');

document.getElementById('taskForm').addEventListener('submit', async function handleTaskSubmit(e) {
    e.preventDefault();

    const formData = new FormData(this);
    const data = {
        title: formData.get('taskTitle'),
        content: formData.get('taskDescription'),
        card: {
            id: noStatusCardId
        }
    };

    try {
        const response = await fetch(`${contextPath}/api/v1/tasks`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        const responseJson = await response.json();
        addNewTask(responseJson);

        closePopup(document.getElementById('taskPopup'));
        this.reset();
    } catch (error) {
        console.log(error);
    }
});

function addNewTask(data) {
    console.log('Adding new task');
    const taskList = document.querySelector('.task-list');

    const newTaskItem = document.createElement('li');
    newTaskItem.id = `task-${data.id}`;
    newTaskItem.className = 'task-item';

    newTaskItem.innerHTML = `
        <div class="task-title-container">
          <p class="task-title">${data.title}</p>
          <div class="status-badge" style="background-color: #434544;">No Status</div>
        </div>
        <button class="delete-task-btn" data-task-id="${data.id}">×</button>
    `;
    taskList.appendChild(newTaskItem);

    newTaskItem.querySelector('.delete-task-btn').addEventListener('click', (e) => {
        e.stopPropagation();
        deleteTask(data.id);
    });
}

document.getElementById('inviteForm').addEventListener('submit', async function handleInvite(e) {
    e.preventDefault();

    const formData = new FormData(this);
    const data = {
        userId: formData.get('user-id'),
        projectId: projectId,
        projectTitle: projectTitle
    };

    const response = await fetch(`${contextPath}/api/v1/invitations`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    if (response.ok) {
        this.reset();
        alert('Invited successfully')
    }
});


const deleteButtons = document.querySelectorAll('.delete-task-btn');
deleteButtons.forEach(button => {
    const taskId = button.getAttribute('data-task-id');
    button.addEventListener('click', (e) => {
        e.stopPropagation();
        deleteTask(taskId);
    });
});

async function deleteTask(taskId) {
    const response = await fetch(`${contextPath}/api/v1/tasks/${taskId}`, {
        method: 'DELETE'
    });

    if (response.ok) {
        document.getElementById('task-' + taskId).remove();
    } else {
        console.error('Could not delete task');
    }
}

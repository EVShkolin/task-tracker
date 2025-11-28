const tasks = document.querySelectorAll('.task-item');
const popup = document.getElementById('taskInfoPopup');

const assigneeSelectPopup = document.getElementById('assigneeSelectPopup');
const assigneeSelectList = document.getElementById('assigneeSelectList');
let currentTaskId;

function openTaskInfo(task) {
    popup.style.display = 'flex';
    loadTaskDataToPopup(task);
    currentTaskId = task.id.replace('task-', '');
}

function closeTaskInfo() {
    popup.style.display = 'none';
}

popup.addEventListener('click', e => {
    if (e.target === popup || e.target.classList.contains('close-btn')) {
        closeTaskInfo();
    }
})

tasks.forEach(task => {
    task.addEventListener('click', () => openTaskInfo(task));
});

async function loadTaskDataToPopup(task) {
    const taskId = task.id.replace('task-', '');

    try {
        const response = await fetch(`${contextPath}/api/v1/tasks/${taskId}`);
        const taskData = await response.json();


        document.getElementById('tName').textContent = taskData.title;
        document.getElementById('tContent').textContent = taskData.content;
        document.getElementById('tStatus').textContent = taskData.card.title;
        document.getElementById('tDate').textContent = formatDate(taskData.createdAt);
        console.log(taskData);

        updateAssigneeList(taskData.assignees);
    } catch (error) {
        console.log(error);
    }
}

function updateAssigneeList(assignees) {
    const assigneeList = document.getElementById('assigneeList');
    assigneeList.innerHTML = '';

    if (assignees || assignees.length > 0) {
        assignees.forEach(assignee => {
            const assigneeItem = createAssigneeItem(assignee);
            assigneeList.appendChild(assigneeItem);
        })
    }
}

function formatDate(instant) {
    const date = new Date(instant);
    return date.toLocaleDateString('ru-RU', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
    });
}

function createAssigneeItem(assignee) {
    const item = document.createElement('li');
    item.id = `member-${assignee.id}`;
    item.className = 'assignee-item';
    item.innerHTML = `<p class="assignee-name">${assignee.username}</p>`;
    return item;
}

// ------------------

function openAssigneeSelectPopup() {
    populateAssigneeSelectList(members);
    assigneeSelectPopup.style.display = 'block';
}

function closeAssigneeSelectPopup() {
    assigneeSelectPopup.style.display = 'none';
}

function populateAssigneeSelectList(membersList) {
    assigneeSelectList.innerHTML = '';

    if (membersList && membersList.length > 0) {
        console.log(membersList.length);
        membersList.forEach(member => {
            const listItem = document.createElement('li');
            listItem.className = 'assignee-select-item';
            listItem.innerHTML = `
                <div class="assignee-select-name">${member.username}</div>
                <div class="assignee-select-role">${member.role}</div>
            `;
            listItem.addEventListener('click', () => assignMemberToTask(member.id));
            assigneeSelectList.appendChild(listItem);
        });
    } else {
        log.wait('No members in the project');
    }
}

async function assignMemberToTask(memberId) {
    try {
        console.log(`Current task id ${currentTaskId}, member id ${memberId}`)
        const response = await fetch(`${contextPath}/api/v1/tasks/${currentTaskId}/assign/${memberId}`, {
            method: 'POST'
        });

        if (!response.ok) {
            throw new Error(`Couldn't assign member`);
        }
        closeAssigneeSelectPopup();

        // Обновляем данные задачи чтобы показать нового исполнителя
        await reloadTaskData();

    } catch (error) {
        console.error( error);
    }
}

async function reloadTaskData() {
    if (!currentTaskId) return;

    try {
        const response = await fetch(`${contextPath}/api/v1/tasks/${currentTaskId}`);
        if (!response.ok) throw new Error('Failed to reload task data');

        const taskData = await response.json();
        updateAssigneeList(taskData.assignees);
    } catch (error) {
        console.error(error);
    }
}

assigneeSelectPopup.querySelector('.close-btn').addEventListener('click', closeAssigneeSelectPopup);

popup.querySelector('.add-assignee-btn').addEventListener('click',openAssigneeSelectPopup);

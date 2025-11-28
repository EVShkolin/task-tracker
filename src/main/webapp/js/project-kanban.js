const newTaskButtons = document.querySelectorAll('.add-task-btn');
const newTaskPopup = document.getElementById('taskPopup');
const taskForm = document.getElementById('taskForm');
let currentCard;

function openTaskPopup() {
    newTaskPopup.style.display = 'block';
}

function closeTaskPopup() {
    newTaskPopup.style.display = 'none';
    taskForm.reset();
}

newTaskButtons.forEach(btn => {
    btn.addEventListener('click', () => {
        openTaskPopup();
        currentCard = btn.closest('.kanban-card')
    });
});

newTaskPopup.addEventListener('click', (e) => {
    if (e.target === newTaskPopup || e.target.classList.contains('close-btn')) {
        closeTaskPopup();
        currentCard = null;
    }
});

taskForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData(taskForm);
    const cardId = currentCard.id.replace('card-', '');

    const data = {
        title: formData.get('taskTitle'),
        content: formData.get('taskDescription'),
        card: {
            id: cardId
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

        if (response.ok) {
            const responseData = await response.json();
            addNewTaskToCard(responseData);
            closeTaskPopup();
        }
    } catch (error) {
        console.log(error);
    }
});

function addNewTaskToCard(task) {
    console.log('Adding new task');
    if (!currentCard) return;

    const taskList = currentCard.querySelector('.task-list');

    const newTaskItem = document.createElement('li');
    newTaskItem.id = `task-${task.id}`;
    newTaskItem.className = 'task-item';
    newTaskItem.draggable = true;
    newTaskItem.innerHTML = `<div class="task-title">${task.title}</div>`;
    taskList.appendChild(newTaskItem);
}


const newCardBtn = document.getElementById('addCardBtn');
const newCardPopup = document.getElementById('cardPopup');
const cardForm = document.getElementById('cardForm');

function openCardPopup() {
    newCardPopup.style.display = 'block';
}

function closeCardPopup() {
    newCardPopup.style.display = 'none';
    cardForm.reset();
}

newCardBtn.addEventListener('click', () => {
   openCardPopup();
});

newCardPopup.addEventListener('click', (e) => {
    if (e.target === newCardPopup || e.target.classList.contains('close-btn')) {
        closeCardPopup();
    }
});

cardForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData(cardForm);
    const displayOrder = document.querySelectorAll('.kanban-card').length + 1;

    const data = {
        title: formData.get('cardTitle'),
        description: formData.get('cardDescription'),
        projectId: `${projectId}`,
        color: formData.get('cardColor'),
        displayOrder: displayOrder
    };

    try {
        const response = await fetch(`${contextPath}/api/v1/kanban-cards`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const newCard = await response.json();
            addNewCardToBoard(newCard);
            closeCardPopup();
        }
    } catch (error) {
        console.log(error);
    }
});

function addNewCardToBoard(card) {
    console.log('Adding new card');

    const board = document.querySelector('.kanban-board');
    const newCardItem = document.createElement('div');
    const addCardBtn = board.querySelector('.add-card-btn-container');

    newCardItem.id = `card-${card.id}`;
    newCardItem.className = 'kanban-card';
    newCardItem.innerHTML = `
        <div class="card-header">
          <div class="status-indicator" style="background-color: ${card.color}"></div>
          <h3 class="card-title">${card.title}</h3>
        </div>
          <p class="card-description">${card.description}</p>
          <ul class="task-list"></ul>
          <button class="add-task-btn">+ Add task</button>
    `;

    board.insertBefore(newCardItem, addCardBtn);

    const newButton = newCardItem.querySelector('.add-task-btn');
    newButton.addEventListener('click', () => {
        openTaskPopup();
        currentCard = newCardItem;
    });

    const taskList = newCardItem.querySelector('.task-list');
    taskList.addEventListener('dragover', (e) => {
        e.preventDefault();

        const els = taskList.querySelectorAll('.task-item:not(.is-dragging)');
        let closestTask = null;
        let closestOffset = Number.NEGATIVE_INFINITY;

        els.forEach((task) => {
            const { top } = task.getBoundingClientRect();
            const offset = e.clientY - top;

            if (offset < 0 && offset > closestOffset) {
                closestTask = task;
                closestOffset = offset;
            }
        });

        const curTask = document.querySelector('.is-dragging');
        if (!closestTask) {
            taskList.appendChild(curTask);
        } else {
            taskList.insertBefore(curTask, closestTask);
        }
    });
}
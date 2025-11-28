const draggables = document.querySelectorAll('.task-item');
const droppables = document.querySelectorAll('.task-list');

let sourceCard;

draggables.forEach((task) => {
    task.addEventListener('dragstart', () => {
        task.classList.add('is-dragging');
        sourceCard = task.closest('.kanban-card');
    });
    task.addEventListener('dragend', () => {
        task.classList.remove('is-dragging');
        const targetCard = task.closest('.kanban-card');

        if (sourceCard && targetCard && sourceCard !== targetCard) {
            sendTaskUpdate(task, targetCard);
        }

        sourceCard = null;
    })
});

droppables.forEach((zone) => {
    zone.addEventListener('dragover', (e) => {
        e.preventDefault();

        const bottomTask = insertAboveTask(zone, e.clientY);
        const curTask = document.querySelector('.is-dragging');

        if (!bottomTask) {
            zone.appendChild(curTask);
        } else {
            zone.insertBefore(curTask, bottomTask);
        }
    });
});

const insertAboveTask = (zone, mouseY) => {
    const els = zone.querySelectorAll('.task-item:not(.is-dragging)');

    let closestTask = null;
    let closestOffset = Number.NEGATIVE_INFINITY;

    els.forEach((task) => {
        const { top } = task.getBoundingClientRect();
        const offset = mouseY - top;

        if (offset < 0 && offset > closestOffset) {
            closestTask = task;
            closestOffset = offset;
        }
    });
    return closestTask;
};

async function sendTaskUpdate (task, card) {
    const taskId = task.id.split('-')[1];
    const cardId = card.id.split('-')[1];
    const data = {
        cardId: parseInt(cardId)
    };

    try {
        const response = await fetch(`${contextPath}/api/v1/tasks/${taskId}/status`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            console.log("Status changed to ", card.querySelector('.card-title').textContent);
        }

    } catch (error) {
        console.log(error);
    }
}
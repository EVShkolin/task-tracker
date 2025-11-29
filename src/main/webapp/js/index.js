const addProjectBtn = document.getElementById('addProjectBtn');
const projectPopup = document.getElementById('projectPopup');
const projectForm = document.getElementById('projectForm');

function openPopup() {
    projectPopup.style.display = 'block';
}

function closePopup() {
    projectPopup.style.display = 'none';
    projectForm.reset();
}

addProjectBtn.addEventListener('click', openPopup);

projectPopup.addEventListener('click', function (e) {
   if (e.target === this || e.target.classList.contains('close-btn')) {
       closePopup();
   }
});

projectForm.addEventListener('submit', async function(e) {
    e.preventDefault();

    const formData = new FormData(this);
    console.log('===', userId, formData.get('projectTitle'), formData.get('projectDescription'));
    const data = {
        creatorId: userId,
        title: formData.get('projectTitle'),
        description: formData.get('projectDescription')
    };

    const response = await fetch(`${contextPath}/api/v1/projects`, {
       method: 'POST',
       headers: {
           'Content-Type': 'application/json'
       },
        body: JSON.stringify(data)
    });

    const project = await response.json();

    if (response.ok) {
        addNewProject(project);
        closePopup();
    }
});

function addNewProject(project) {
    const projectItem = document.createElement('div');
    const projectList = document.getElementById('projects');
    projectItem.id = `project_${project.id}`;
    projectItem.className = project;
    project.innerHTML = `
        <a href="${contextPath}/projects/${project.id}">
            <p>${project.title}</p>
        </a> 
    `;

    projectList.appendChild(projectItem);
}
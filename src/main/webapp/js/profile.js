function openPopup(popupId) {
    document.getElementById(popupId).style.display = 'block';
}

function closePopup(popupId) {
    document.getElementById(popupId).style.display = 'none';
}

document.getElementById('change-username-btn').addEventListener('click', () => openPopup('usernamePopup'));
document.getElementById('change-password-btn').addEventListener('click', () => openPopup('passwordPopup'));
document.getElementById('delete-account-btn').addEventListener('click', () => openPopup('deleteAccountPopup'));

document.querySelectorAll('.popup .close-btn').forEach(btn => {
    btn.addEventListener('click', function() {
        closePopup(this.closest('.popup').id);
    });
});

document.getElementById('cancel-delete-btn').addEventListener('click', () => closePopup('deleteAccountPopup'));

document.querySelectorAll('.popup').forEach(popup => {
    popup.addEventListener('click', function(e) {
        if (e.target === this) {
            closePopup(this.id);
        }
    });
});


document.getElementById('username-form').addEventListener('submit', async function(e) {
   e.preventDefault();
   const formData = new FormData(this);
   const data = {
       newUsername: formData.get('newUsername')
   };

    try {
        const response = await fetch(`${contextPath}/api/v1/users/${userId}/username`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert('Username changed successfully');
        }
    } catch (error) {
        console.log('Error: ', error);
    }
});

document.getElementById('password-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    const data = {
        newPassword: formData.get('newPassword'),
        confirmPassword: formData.get('confirmPassword')
    };

    if (data.confirmPassword !== data.newPassword) {
        alert('Passwords dont match');
        return;
    }

    try {
        const response = await fetch(`${contextPath}/api/v1/users/${userId}/password`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            alert('Password changed successfully');
        }
    } catch (error) {
        console.log('Error: ', e);
    }
});

document.getElementById('confirm-delete-btn').addEventListener('click', function (e) {
    const confirmed = confirm('Your account will be deleted');
    if (confirmed) {
        deleteAccount();
    }
});

async function deleteAccount() {
    try {
        const response = await fetch(`${contextPath}/api/v1/users/${userId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert('Account deleted successfully');
            window.location.href = `${contextPath}/logout`;
        }

    } catch (error) {
        console.log('Error: ', error);
    }
}


document.querySelectorAll('.btn-accept').forEach(button => {
    button.addEventListener('click', function() {
        const invitationId = this.getAttribute('data-invitation-id');
        const projectId = this.getAttribute('data-project-id');
        handleInvitationAction(invitationId, projectId, 'ACCEPTED');
    });
});

document.querySelectorAll('.btn-decline').forEach(button => {
    button.addEventListener('click', function() {
        const invitationId = this.getAttribute('data-invitation-id');
        const projectId = this.getAttribute('data-project-id');
        console.log('Project id : ', projectId)
        handleInvitationAction(invitationId, projectId, 'DECLINED');
    });
});

async function handleInvitationAction(invitationId, projectId, action) {
    try {
        const data = {
            id: invitationId,
            userId: userId,
            projectId: projectId,
            status: action
        };
        const response = await fetch(`${contextPath}/api/v1/invitations/${invitationId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        });

        if (response.ok) {
            const invitationItem = document.getElementById(`invitation-${invitationId}`);
            if (invitationItem) {
                invitationItem.remove();
            }


        } else {
            throw new Error('Failed to process invitation');
        }
    } catch (error) {
        console.error(error);
    }
}
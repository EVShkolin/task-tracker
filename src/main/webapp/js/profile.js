const contextPath = window.APP_CONFIG.contextPath;
const userId = window.APP_CONFIG.userId;

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

document.getElementById('delete-account-button').addEventListener('click', function (e) {
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
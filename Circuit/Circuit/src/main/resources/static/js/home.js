document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('/api/me');
        if (!response.ok) {
            window.location.href = '/login';
            return;
        }
        const user = await response.json();

        const welcomeTitle = document.querySelector('.welcome-title');
        if (welcomeTitle && user.nome)
            welcomeTitle.innerHTML = `Bem-vindo de volta, ${user.nome.split(' ')[0]}!`;

    } catch (e) {
        window.location.href = '/login';
    }

    document.getElementById('btnLogout')?.addEventListener('click', () => {
        window.location.href = '/logout';
    });
});
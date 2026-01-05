
const user = JSON.parse(localStorage.getItem('user'));
if (!user) {
    window.location.href = '/login.html';
} else {
    document.addEventListener('DOMContentLoaded', () => {
        const nomeSidebar = document.getElementById('nomeUsuarioExibicao');
        if (nomeSidebar && user.nome) {
            nomeSidebar.innerText = user.nome;
        }
        const welcomeTitle = document.querySelector('.welcome-title');
        if (welcomeTitle && user.nome) {
            welcomeTitle.innerHTML = `Bem-vindo de volta, ${user.nome.split(' ')[0]}!`;
        }
        const avatar = document.querySelector('.user-avatar');
        if (avatar && user.nome) {
            avatar.innerText = user.nome.charAt(0).toUpperCase();
        }
        document.getElementById('btnLogout').addEventListener('click', () => {
            localStorage.removeItem('user');
            window.location.href = '/login.html';
        });
    });
}
document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetch('/api/me');
        if (!response.ok) {
            window.location.href = '/login';
            return;
        }
        const user = await response.json();

        const nomeSidebar = document.getElementById('nomeUsuarioExibicao');
        if (nomeSidebar && user.nome) {
            nomeSidebar.innerText = user.nome;
        }
        const avatar = document.querySelector('.user-avatar');
        if (avatar && user.nome) {
            avatar.innerText = user.nome.charAt(0).toUpperCase();
        }
    } catch (e) {
        console.error('Erro ao carregar informações do usuário:', e);
        window.location.href = '/login';
    }
});
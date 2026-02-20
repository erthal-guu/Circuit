function togglePassword(id) {
    const input = document.getElementById(id);
    const container = input.parentElement;
    const eyeOpen = container.querySelector('.eye-open');
    const eyeClosed = container.querySelector('.eye-closed');

    if (input.type === 'password') {
        input.type = 'text';
        eyeOpen.classList.add('hide');
        eyeClosed.classList.remove('hide');
    } else {
        input.type = 'password';
        eyeOpen.classList.remove('hide');
        eyeClosed.classList.add('hide');
    }
}

const inputUsername = document.getElementById('username');
if (inputUsername) {
    inputUsername.addEventListener('input', function(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 11) value = value.slice(0, 11);
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d)/, '$1.$2');
        value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        e.target.value = value;
    });
}

const form = document.getElementById('loginForm');
const msgDiv = document.getElementById('mensagem');

if (form) {
    form.addEventListener('submit', () => {
        if (msgDiv) {
            msgDiv.innerText = "Verificando...";
            msgDiv.style.color = "#333";
        }
    });
}
const form = document.getElementById('loginForm');
const msgDiv = document.getElementById('mensagem');

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

document.getElementById('cpf').addEventListener('input', function(e) {
    let value = e.target.value.replace(/\D/g, '');
    if (value.length > 11) value = value.slice(0, 11);
    value = value.replace(/(\d{3})(\d)/, '$1.$2');
    value = value.replace(/(\d{3})(\d)/, '$1.$2');
    value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
    e.target.value = value;
});

form.addEventListener('submit', async (event) => {
    event.preventDefault();

    const cpfComMascara = document.getElementById('cpf').value;
    const senha = document.getElementById('password').value;

    msgDiv.innerText = "Verificando...";
    msgDiv.style.color = "#333";

    try {
        const response = await fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                cpf: cpfComMascara,
                senha: senha
            })
        });

        if (response.ok) {
            const user = await response.json();
            msgDiv.style.color = "green";
            msgDiv.innerText = "Sucesso! Entrando...";
            localStorage.setItem('user', JSON.stringify(user));
            
            setTimeout(() => {
                window.location.href = '/home';
            }, 1000);
        } else {
            const erroMsg = await response.text();
            msgDiv.style.color = "red";
            msgDiv.innerText = erroMsg;
        }
    } catch (error) {
        msgDiv.style.color = "red";
        msgDiv.innerText = "Erro: Servidor offline.";
        console.error(error);
    }
});

const btnLogout = document.getElementById('btnLogout');
if (btnLogout) {
    btnLogout.addEventListener('click', () => {
        localStorage.removeItem('user');
        window.location.href = '/login.html';
    });
}
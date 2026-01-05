const form = document.getElementById('loginForm');
const msgDiv = document.getElementById('mensagem');

form.addEventListener('submit', async (event) => {
    event.preventDefault();

    const cpf = document.getElementById('cpf').value;
    const senha = document.getElementById('senha').value;

    msgDiv.innerText = "Verificando...";
    msgDiv.style.color = "#333";

    try {
        const response = await fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ cpf, senha })
        });

        if (response.ok) {
            const user = await response.json();
            msgDiv.style.color = "green";
            msgDiv.innerText = "Sucesso! Entrando...";

            localStorage.setItem('user', JSON.stringify(user));

            setTimeout(() => {
                window.location.href = 'home.html';
            }, 1000);

        } else {
            const erroMsg = await response.text();
            msgDiv.style.color = "red";
            msgDiv.innerText = erroMsg;
        }

    } catch (error) {
        msgDiv.style.color = "red";
        msgDiv.innerText = "Erro: Não foi possível conectar ao servidor.";
        console.error("Erro na requisição:", error);
    }
        document.getElementById('cpf').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length <= 11) {
                value = value.replace(/(\d{3})(\d)/, '$1.$2');
                value = value.replace(/(\d{3})(\d)/, '$1.$2');
                value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
                e.target.value = value;
            }
        });
});
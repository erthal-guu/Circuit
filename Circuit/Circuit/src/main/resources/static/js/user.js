// Seletores Globais
const formCadastro = document.getElementById('userForm');
const modal = document.getElementById('userModal');
const msgDiv = document.getElementById('mensagem');

function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(c => {
        c.style.display = 'none';
        c.classList.remove('active');
    });

    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    const abaSelecionada = tabName === 'ativos' ? 'tabAtivos' : 'tabInativos';
    const conteudo = document.getElementById(abaSelecionada);

    if (conteudo) {
        conteudo.style.display = 'block';
        conteudo.classList.add('active');
    }

    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }
}

function openModal() {
    if (modal) {
        modal.classList.add('active');
        modal.style.display = 'flex';
    }

    if (msgDiv) msgDiv.innerText = "";
    if (msgDiv) msgDiv.style.color = "";
}

function closeModal() {
    if (modal) {
        modal.classList.remove('active');
        modal.style.display = 'none';
    }
}
function abrirModalNovo() {
    if (formCadastro) formCadastro.reset();

    document.getElementById('userId').value = '';
    document.getElementById('modalTitle').innerText = 'Novo Usuário';

    const statusField = document.getElementById('userStatus');
    if(statusField) statusField.value = 'true';

    openModal();
}


const cpfInput = document.getElementById('userCPF');

function aplicarMascaras() {
    if (cpfInput) cpfInput.dispatchEvent(new Event('input'));
}

if (cpfInput) {
    cpfInput.addEventListener('input', function (e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length <= 11) {
            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d)/, '$1.$2');
            value = value.replace(/(\d{3})(\d{1,2})$/, '$1-$2');
        }
        e.target.value = value;
    });
}

function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    if (input) {
        input.type = input.type === "password" ? "text" : "password";
    }
}

if (formCadastro) {
    formCadastro.addEventListener('submit', function (event) {
        const senha = document.getElementById('userPassword').value;
        const senhaConfirm = document.getElementById('userPasswordConfirm').value;
        const userId = document.getElementById('userId').value;

        // Validação: Senhas iguais
        if (senha !== senhaConfirm) {
            event.preventDefault();
            msgDiv.style.color = "red";
            msgDiv.innerText = "As senhas não coincidem!";
            return;
        }

        if (!userId && !senha) {
            event.preventDefault();
            msgDiv.style.color = "red";
            msgDiv.innerText = "Senha é obrigatória para novos usuários!";
            return;
        }

    });
}
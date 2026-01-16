
const formCadastro = document.getElementById('userForm');
const modal = document.getElementById('userModal');
const msgDiv = document.getElementById('mensagem');
const cpfInput = document.getElementById('userCPF');
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
    if (msgDiv) {
        msgDiv.innerText = "";
        msgDiv.style.color = "";
    }
}

function closeModal() {
    if (modal) {
        modal.classList.remove('active');
        modal.style.display = 'none';
    }
}
function abrirModalNovo() {
    if (formCadastro) {
        formCadastro.reset();
        formCadastro.action = "/usuarios/cadastrar";
    }

    document.getElementById('userId').value = '';
    document.getElementById('modalTitle').innerText = 'Novo Usuário';
    const statusField = document.getElementById('userStatus');
    if (statusField) statusField.value = 'true';

    openModal();
}
function abrirModalEdicao(btn) {
    if (formCadastro) {
        formCadastro.action = "/usuarios/editar";
    }
    const id = btn.getAttribute('data-id');
    const nome = btn.getAttribute('data-nome');
    const cpf = btn.getAttribute('data-cpf');
    const cargo = btn.getAttribute('data-cargo');
    const ativo = btn.getAttribute('data-ativo');
    document.getElementById('userId').value = id;
    document.getElementById('userName').value = nome;
    document.getElementById('userCPF').value = cpf;
    const profileSelect = document.getElementById('userProfile');
    if (profileSelect) profileSelect.value = cargo;
    const statusSelect = document.getElementById('userStatus');
    if (statusSelect) statusSelect.value = (ativo === 'true') ? 'true' : 'false';
    document.getElementById('userPassword').value = '';
    document.getElementById('userPasswordConfirm').value = '';

    document.getElementById('modalTitle').innerText = 'Editar Usuário';
    aplicarMascaras();
    openModal();
}
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
document.addEventListener("DOMContentLoaded", function (){
    pesquisarUsuarios('searchInputAtivos','userTable');
    pesquisarUsuarios('searchInputInativos','userTableInativos');
});
function pesquisarUsuarios(inputId,tableId){
    const input = document.getElementById(inputId)
    const table = document.getElementById(tableId)

    input.addEventListener('keyup',function (){
        const termo = input.value.toLowerCase();
        const linhas = table.querySelectorAll('tbody tr');

        linhas.forEach(function (linha){
            const textoLinha = linha.innerText.toLowerCase();
            if (textoLinha.includes(termo)) {
                linha.style.display = '';
            } else {
                linha.style.display = 'none';
            }
        });
    });
}
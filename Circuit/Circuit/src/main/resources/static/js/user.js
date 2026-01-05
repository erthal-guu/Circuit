const formCadastro = document.getElementById('userForm');
const msgDiv = document.getElementById('mensagem');
const userTableBody = document.querySelector('#userTable tbody');
const userTableInativosBody = document.querySelector('#userTableInativos tbody');

let usuarioIdEdicao = null;

const nomesCargos = {
    'ADMIN': 'Administrador',
    'TECNICO': 'Técnico',
    'VENDEDOR': 'Vendedor',
    'GERENTE': 'Gerente'
};

function switchTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    if (tabName === 'ativos') {
        document.getElementById('tabAtivos').classList.add('active');
        event.currentTarget.classList.add('active');
        carregarUsuarios();
    } else {
        document.getElementById('tabInativos').classList.add('active');
        event.currentTarget.classList.add('active');
        carregarInativos();
    }
}

function aplicarMascaras() {
    const cpf = document.getElementById('userCPF');
    if (cpf) cpf.dispatchEvent(new Event('input'));
}

function openModal() {
    const modal = document.getElementById('userModal');
    if (modal) modal.classList.add('active');
    if (!usuarioIdEdicao) {
        document.getElementById('modalTitle').textContent = 'Novo Usuário';
        if (formCadastro) formCadastro.reset();
    }
    if (msgDiv) msgDiv.innerText = "";
}

function closeModal() {
    const modal = document.getElementById('userModal');
    if (modal) modal.classList.remove('active');
    usuarioIdEdicao = null;
}

function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    input.type = input.type === "password" ? "text" : "password";
}

const cpfInput = document.getElementById('userCPF');
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

async function carregarUsuarios() {
    try {
        const res = await fetch('http://localhost:8080/usuarios/listar-ativos');
        const lista = await res.json();
        renderizarTabela(lista, userTableBody, true);
    } catch (error) {
        console.error(error);
    }
}

async function carregarInativos() {
    try {
        const res = await fetch('http://localhost:8080/usuarios/listar-inativos');
        const lista = await res.json();
        renderizarTabela(lista, userTableInativosBody, false);
    } catch (error) {
        console.error(error);
    }
}

function renderizarTabela(usuarios, tbody, isAtivo) {
    if (!tbody) return;
    tbody.innerHTML = '';

    usuarios.forEach(user => {
        const row = document.createElement('tr');
        const cargoExibicao = nomesCargos[user.cargo] || user.cargo;

        const botoes = isAtivo ?
            `<button class="btn-action btn-edit" onclick="editUser(${user.id})">✏️</button>
             <button class="btn-action btn-delete" onclick="deleteUser(${user.id})">🗑️</button>` :
            `<button class="btn-action btn-restore" onclick="restaurarUser(${user.id})">🔄</button>`;

        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.nome}</td>
            <td>${user.cpf}</td>
            <td>${cargoExibicao}</td>
            <td><span class="badge ${isAtivo ? 'badge-active' : 'badge-inactive'}">${isAtivo ? 'Ativo' : 'Inativo'}</span></td>
            <td><div class="action-buttons">${botoes}</div></td>
        `;
        tbody.appendChild(row);
    });
}

async function filterTable(tableId) {
    const isAtivo = tableId === 'userTable';
    const inputId = isAtivo ? 'searchInputAtivos' : 'searchInputInativos';
    const termo = document.getElementById(inputId).value;

    if (termo.length === 0) {
        if (isAtivo) {
            carregarUsuarios();
        } else {
            carregarInativos();
        }
        return;
    }

    const endpoint = isAtivo ? 'pesquisar-ativos' : 'pesquisar-inativos';

    try {
        const response = await fetch(`http://localhost:8080/usuarios/${endpoint}?nome=${termo}`);
        const resultados = await response.json();
        const tbody = document.querySelector(`#${tableId} tbody`);
        renderizarTabela(resultados, tbody, isAtivo);
    } catch (error) {
        console.error(error);
    }
}

async function editUser(id) {
    try {
        const response = await fetch('http://localhost:8080/usuarios/listar-ativos');
        const usuarios = await response.json();
        const user = usuarios.find(u => u.id === id);

        if(user) {
            usuarioIdEdicao = id;
            document.getElementById('userName').value = user.nome;
            document.getElementById('userCPF').value = user.cpf;
            document.getElementById('userProfile').value = user.cargo;
            document.getElementById('userStatus').value = user.ativo ? 'ativo' : 'inativo';
            document.getElementById('userPassword').value = "";
            document.getElementById('userPasswordConfirm').value = "";

            aplicarMascaras();
            document.getElementById('modalTitle').textContent = 'Editar Usuário';
            openModal();
        }
    } catch (error) { console.error(error); }
}

async function restaurarUser(id) {
    if (confirm("Deseja restaurar este usuário?")) {
        try {
            const response = await fetch(`http://localhost:8080/usuarios/reativar/${id}`, {
                method: 'PUT'
            });
            if (response.ok) {
                carregarInativos();
            }
        } catch (error) {
            console.error(error);
        }
    }
}

async function deleteUser(id) {
    if (confirm("Deseja desativar este usuário?")) {
        try {
            const response = await fetch(`http://localhost:8080/usuarios/excluir/${id}`, { method: 'DELETE' });
            if (response.ok) {
                carregarUsuarios();
            }
        } catch (error) {
            console.error(error);
        }
    }
}

if (formCadastro) {
    formCadastro.addEventListener('submit', async (event) => {
        event.preventDefault();
        const elSenha = document.getElementById('userPassword');
        const elSenhaConfirm = document.getElementById('userPasswordConfirm');

        if (!usuarioIdEdicao && !elSenha.value) {
            msgDiv.style.color = "red";
            msgDiv.innerText = "Senha obrigatória!";
            return;
        }
        if (elSenha.value && elSenha.value !== elSenhaConfirm.value) {
            msgDiv.style.color = "red";
            msgDiv.innerText = "Senhas não coincidem!";
            return;
        }

        const dados = {
            nome: document.getElementById('userName').value,
            cpf: document.getElementById('userCPF').value,
            senha: elSenha.value || null,
            cargo: document.getElementById('userProfile').value,
            ativo: document.getElementById('userStatus').value === 'ativo'
        };

        const url = usuarioIdEdicao ? `http://localhost:8080/usuarios/editar/${usuarioIdEdicao}` : 'http://localhost:8080/usuarios/cadastrar';
        const res = await fetch(url, {
            method: usuarioIdEdicao ? 'PUT' : 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });

        if (res.ok) {
            closeModal();
            carregarUsuarios();
        }
    });
}

const searchInputAtivos = document.getElementById('searchInputAtivos');
if (searchInputAtivos) {
    searchInputAtivos.addEventListener('input', () => filterTable('userTable'));
}

const searchInputInativos = document.getElementById('searchInputInativos');
if (searchInputInativos) {
    searchInputInativos.addEventListener('input', () => filterTable('userTableInativos'));
}

carregarUsuarios();
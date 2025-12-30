
const formCadastro = document.getElementById('userForm');
const msgDiv = document.getElementById('mensagem');
const userTableBody = document.querySelector('#userTable tbody');

let usuarioIdEdicao = null;

const nomesCargos = {
    'ADMIN': 'Administrador',
    'TECNICO': 'Técnico',
    'VENDEDOR': 'Vendedor',
    'GERENTE': 'Gerente'
};

function aplicarMascaras() {
    const cpf = document.getElementById('userCPF');
    if (cpf) cpf.dispatchEvent(new Event('input'));
}

function openModal() {
    const modal = document.getElementById('userModal');
    if (modal) modal.classList.add('active');

    if (!usuarioIdEdicao) {
        document.getElementById('modalTitle').textContent = 'Novo Usuário';
        if(formCadastro) formCadastro.reset();
    }
    if(msgDiv) msgDiv.innerText = "";
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
    cpfInput.addEventListener('input', function(e) {
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
        const response = await fetch('http://localhost:8080/usuarios/listar');
        if (!response.ok) throw new Error('Erro ao buscar dados');

        const usuarios = await response.json();
        userTableBody.innerHTML = '';

        usuarios.forEach(user => {
            const row = document.createElement('tr');
            const cargoExibicao = nomesCargos[user.cargo] || user.cargo;

            row.innerHTML = `
                <td>${user.id}</td>
                <td>${user.nome}</td>
                <td>${user.cpf}</td>
                <td>${cargoExibicao}</td>
                <td>
                    <span class="badge ${user.ativo ? 'badge-active' : 'badge-inactive'}">
                        ${user.ativo ? 'Ativo' : 'Inativo'}
                    </span>
                </td>
                <td>
                    <div class="action-buttons">
                        <button class="btn-action btn-edit" onclick="editUser(${user.id})">✏️</button>
                        <button class="btn-action btn-delete" onclick="deleteUser(${user.id})">🗑️</button>
                    </div>
                </td>
            `;
            userTableBody.appendChild(row);
        });
    } catch (error) {
        console.error("Erro ao carregar tabela:", error);
    }
}

async function editUser(id) {
    try {
        const response = await fetch('http://localhost:8080/usuarios/listar');
        const usuarios = await response.json();
        const user = usuarios.find(u => u.id === id);

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
    } catch (error) {
        console.error(error);
    }
}

async function deleteUser(id) {
    if (confirm("Deseja realmente desativar este usuário?")) {
        try {
            const response = await fetch(`http://localhost:8080/usuarios/excluir/${id}`, {
                method: 'DELETE'
            });

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
            msgDiv.innerText = "A senha é obrigatória para novos usuários!";
            return;
        }

        if (elSenha.value && elSenha.value !== elSenhaConfirm.value) {
            msgDiv.style.color = "red";
            msgDiv.innerText = "As senhas não coincidem!";
            return;
        }

        const dadosUsuario = {
            nome: document.getElementById('userName').value,
            cpf: document.getElementById('userCPF').value,
            senha: elSenha.value || null,
            cargo: document.getElementById('userProfile').value,
            ativo: document.getElementById('userStatus').value === 'ativo'
        };

        const metodo = usuarioIdEdicao ? 'PUT' : 'POST';
        const url = usuarioIdEdicao
            ? `http://localhost:8080/usuarios/editar/${usuarioIdEdicao}`
            : 'http://localhost:8080/usuarios/cadastrar';

        try {
            const response = await fetch(url, {
                method: metodo,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dadosUsuario)
            });

            if (response.ok) {
                setTimeout(() => {
                    closeModal();
                    carregarUsuarios();
                }, 500);
            }
        } catch (error) {
            console.error(error);
        }
    });
}

carregarUsuarios();
const formCadastro = document.getElementById('funcionarioForm');
const msgDiv = document.getElementById('mensagem');
const tableBodyAtivos = document.querySelector('#funcionarioTable tbody');
const tableBodyInativos = document.querySelector('#funcionarioTableInativos tbody');

let funcionarioIdEdicao = null;
const API_URL = '/funcionarios';

function configurarMascaras() {
    const masks = {
        funcCpf: (v) => v.replace(/\D/g, '').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d{1,2})$/, '$1-$2'),
        funcTelefone: (v) => v.replace(/\D/g, '').replace(/^(\d{2})(\d)/g, '($1) $2').replace(/(\d{5})(\d)/, '$1-$2'),
        funcCep: (v) => v.replace(/\D/g, '').replace(/(\d{5})(\d)/, '$1-$2')
    };

    Object.keys(masks).forEach(id => {
        const input = document.getElementById(id);
        if (input) {
            input.addEventListener('input', (e) => {
                e.target.value = masks[id](e.target.value);
            });
        }
    });
}

async function buscarCep(valor) {
    const cep = valor.replace(/\D/g, '');
    if (cep.length !== 8) return;

    const campos = ['funcLogradouro', 'funcBairro', 'funcCidade', 'funcEstado'];
    campos.forEach(id => document.getElementById(id).value = "...");

    try {
        const response = await fetch(`${API_URL}/consulta-cep-funcionarios/${cep}`);
        if (!response.ok) throw new Error("CEP não encontrado");

        const dados = await response.json();

        document.getElementById('funcLogradouro').value = dados.logradouro || '';
        document.getElementById('funcBairro').value = dados.bairro || '';
        document.getElementById('funcCidade').value = dados.localidade || '';
        document.getElementById('funcEstado').value = dados.uf || '';
        document.getElementById('funcNumero').focus();
    } catch (error) {
        alert(error.message);
        campos.forEach(id => document.getElementById(id).value = "");
    }
}

formCadastro.addEventListener('submit', async (e) => {
    e.preventDefault();

    const dados = {
        nome: document.getElementById('funcNome').value,
        cpf: document.getElementById('funcCpf').value,
        cargo: document.getElementById('funcCargo').value,
        email: document.getElementById('funcEmail').value,
        telefone: document.getElementById('funcTelefone').value,
        dataAdmissao: document.getElementById('funcDataAdmissao').value,
        ativo: document.getElementById('funcAtivo').value === 'true',
        cep: document.getElementById('funcCep').value,
        logradouro: document.getElementById('funcLogradouro').value,
        numero: document.getElementById('funcNumero').value,
        bairro: document.getElementById('funcBairro').value,
        cidade: document.getElementById('funcCidade').value,
        estado: document.getElementById('funcEstado').value
    };

    const url = funcionarioIdEdicao ? `${API_URL}/editar/${funcionarioIdEdicao}` : `${API_URL}/cadastrar`;
    const method = funcionarioIdEdicao ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });

        if (res.ok) {
            closeModal();
            carregarAtivos();
            carregarInativos();
        } else {
            const erroMsg = await res.text();
            msgDiv.style.color = "red";
            msgDiv.innerText = erroMsg;
        }
    } catch (err) {
        console.error(err);
    }
});

async function carregarAtivos() {
    try {
        const res = await fetch(`${API_URL}/listar-Ativos`);
        const lista = await res.json();
        renderizarTabela(lista, tableBodyAtivos, true);
    } catch (error) {
        console.error(error);
    }
}

async function carregarInativos() {
    try {
        const res = await fetch(`${API_URL}/listar-Inativos`);
        const lista = await res.json();
        renderizarTabela(lista, tableBodyInativos, false);
    } catch (error) {
        console.error(error);
    }
}

function renderizarTabela(lista, tbody, isAtivo) {
    if (!tbody) return;
    tbody.innerHTML = '';
    lista.forEach(func => {
        const row = document.createElement('tr');
        const statusClass = func.ativo ? 'badge-active' : 'badge-inactive';
        const statusText = func.ativo ? 'Ativo' : 'Inativo';

        const botoes = isAtivo ?
            `<button class="btn-action btn-edit" onclick="editFuncionario(${func.id})">✏️</button>
             <button class="btn-action btn-delete" onclick="excluirFuncionario(${func.id})">🗑️</button>` :
            `<button class="btn-action btn-restore" onclick="reativarFuncionario(${func.id})">🔄</button>`;

        row.innerHTML = `
            <td>${func.id}</td>
            <td>${func.nome}</td>
            <td>${func.cargo}</td>
            <td>${func.telefone || '-'}</td>
            <td>${func.email}</td>
            <td><span class="badge ${statusClass}">${statusText}</span></td>
            <td><div class="action-buttons">${botoes}</div></td>
        `;
        tbody.appendChild(row);
    });
}

async function editFuncionario(id) {
    const res = await fetch(`${API_URL}/listar-Ativos`);
    const lista = await res.json();
    const f = lista.find(func => func.id === id);

    if (f) {
        funcionarioIdEdicao = id;
        document.getElementById('funcNome').value = f.nome;
        document.getElementById('funcCpf').value = f.cpf;
        document.getElementById('funcCargo').value = f.cargo;
        document.getElementById('funcEmail').value = f.email;
        document.getElementById('funcTelefone').value = f.telefone || '';
        document.getElementById('funcDataAdmissao').value = f.dataAdmissao || '';
        document.getElementById('funcAtivo').value = f.ativo.toString();
        document.getElementById('funcCep').value = f.cep || '';
        document.getElementById('funcLogradouro').value = f.logradouro || '';
        document.getElementById('funcNumero').value = f.numero || '';
        document.getElementById('funcBairro').value = f.bairro || '';
        document.getElementById('funcCidade').value = f.cidade || '';
        document.getElementById('funcEstado').value = f.estado || '';
        document.getElementById('modalTitle').textContent = 'Editar Funcionário';
        openModal();
    }
}

async function excluirFuncionario(id) {
    if (confirm("Desativar este funcionário?")) {
        await fetch(`${API_URL}/excluir/${id}`, { method: 'DELETE' });
        carregarAtivos();
        carregarInativos();
    }
}

async function reativarFuncionario(id) {
    if (confirm("Deseja restaurar este funcionário?")) {
        const res = await fetch(`${API_URL}/restaurar/${id}`, { method: 'PUT' });
        if(res.ok) {
            carregarAtivos();
            carregarInativos();
        }
    }
}

function openModal() {
    document.getElementById('funcionarioModal').classList.add('active');
    msgDiv.innerText = "";
}

function closeModal() {
    document.getElementById('funcionarioModal').classList.remove('active');
    funcionarioIdEdicao = null;
    formCadastro.reset();
}

function switchTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    if (tabName === 'ativos') {
        document.getElementById('tabAtivos').classList.add('active');
        document.querySelector('button[onclick*="ativos"]').classList.add('active');
        carregarAtivos();
    } else {
        document.getElementById('tabInativos').classList.add('active');
        document.querySelector('button[onclick*="inativos"]').classList.add('active');
        carregarInativos();
    }
}


async function pesquisarAtivos(termo) {
    if (!termo) {
        carregarAtivos();
        return;
    }
    try {
        const res = await fetch(`${API_URL}/pesquisar-ativos?nome=${encodeURIComponent(termo)}`);

        if (res.ok) {
            const lista = await res.json();
            renderizarTabela(lista, tableBodyAtivos, true);
        }
    } catch (error) {
        console.error("Erro na pesquisa de ativos:", error);
    }
}

async function pesquisarInativos(termo) {
    if (!termo) {
        carregarInativos();
        return;
    }
    try {
        const res = await fetch(`${API_URL}/pesquisar-inativos?nome=${encodeURIComponent(termo)}`);

        if (res.ok) {
            const lista = await res.json();
            renderizarTabela(lista, tableBodyInativos, false);
        }
    } catch (error) {
        console.error("Erro na pesquisa de inativos:", error);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    configurarMascaras();
    carregarAtivos();
    carregarInativos();

    const inputBuscaAtivos = document.querySelector('input[placeholder*="Buscar ativos"]');
    if (inputBuscaAtivos) {
        inputBuscaAtivos.addEventListener('input', (e) => pesquisarAtivos(e.target.value));
    }

    const inputBuscaInativos = document.querySelector('input[placeholder*="Buscar inativos"]');
    if (inputBuscaInativos) {
        inputBuscaInativos.addEventListener('input', (e) => pesquisarInativos(e.target.value));
    }
});
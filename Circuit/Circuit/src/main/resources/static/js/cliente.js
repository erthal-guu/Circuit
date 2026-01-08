const formCadastro = document.getElementById('clienteForm');
const msgDiv = document.getElementById('mensagem');
const tableBodyAtivos = document.querySelector('#clienteTable tbody');
const tableBodyInativos = document.querySelector('#clienteTableInativos tbody');
const inputBuscaAtivos = document.getElementById('searchInputAtivos');
const inputBuscaInativos = document.getElementById('searchInputInativos');

let clienteIdEdicao = null;
let listaClientesAtivos = [];
let listaClientesInativos = [];
const API_URL = '/clientes';

function configurarMascaras() {
    const masks = {
        cliCpf: (v) => v.replace(/\D/g, '').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d{1,2})$/, '$1-$2'),
        cliTelefone: (v) => v.replace(/\D/g, '').replace(/^(\d{2})(\d)/g, '($1) $2').replace(/(\d{5})(\d)/, '$1-$2'),
        cliCep: (v) => v.replace(/\D/g, '').replace(/(\d{5})(\d)/, '$1-$2')
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

function filtrarTabela(input, tbody) {
    const termo = input.value.toLowerCase();
    const linhas = tbody.querySelectorAll('tr');

    linhas.forEach(linha => {
        const texto = linha.innerText.toLowerCase();
        linha.style.display = texto.includes(termo) ? '' : 'none';
    });
}

async function buscarCep(valor) {
    const cep = valor.replace(/\D/g, '');
    if (cep.length !== 8) return;

    try {
        const response = await fetch(`https://viacep.com.br/ws/${cep}/json/`);
        const dados = await response.json();

        if (!dados.erro) {
            document.getElementById('cliLogradouro').value = dados.logradouro;
            document.getElementById('cliBairro').value = dados.bairro;
            document.getElementById('cliCidade').value = dados.localidade;
            document.getElementById('cliEstado').value = dados.uf;
            document.getElementById('cliNumero').focus();
        }
    } catch (error) {
        console.error(error);
    }
}

formCadastro.addEventListener('submit', async (e) => {
    e.preventDefault();

    const dados = {
        nome: document.getElementById('cliNome').value,
        cpf: document.getElementById('cliCpf').value,
        email: document.getElementById('cliEmail').value,
        telefone: document.getElementById('cliTelefone').value,
        cep: document.getElementById('cliCep').value,
        logradouro: document.getElementById('cliLogradouro').value,
        numero: document.getElementById('cliNumero').value,
        bairro: document.getElementById('cliBairro').value,
        cidade: document.getElementById('cliCidade').value,
        estado: document.getElementById('cliEstado').value,
        ativo: document.getElementById('cliAtivo').value === 'true'
    };

    const url = clienteIdEdicao ? `${API_URL}/editar/${clienteIdEdicao}` : `${API_URL}/cadastrar`;
    const method = clienteIdEdicao ? 'PUT' : 'POST';

    try {
        const res = await fetch(url, {
            method: method,
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
    const res = await fetch(`${API_URL}/listar-ativos`);
    if (res.ok) {
        listaClientesAtivos = await res.json();
        renderizarTabela(listaClientesAtivos, tableBodyAtivos, true);
    }
}

async function carregarInativos() {
    const res = await fetch(`${API_URL}/listar-inativos`);
    if (res.ok) {
        listaClientesInativos = await res.json();
        renderizarTabela(listaClientesInativos, tableBodyInativos, false);
    }
}

function renderizarTabela(lista, tbody, isAtivo) {
    if (!tbody) return;
    tbody.innerHTML = '';

    lista.forEach(cli => {
        const row = document.createElement('tr');
        const botoes = isAtivo ?
            `<button class="btn-action btn-edit" onclick="editCliente(${cli.id})">✏️</button>
             <button class="btn-action btn-delete" onclick="excluirCliente(${cli.id})">🗑️</button>` :
            `<button class="btn-action btn-restore" onclick="reativarCliente(${cli.id})">🔄</button>`;

        row.innerHTML = `
            <td>${cli.id}</td>
            <td>${cli.nome}</td>
            <td>${cli.cpf}</td>
            <td>${cli.telefone || '-'}</td>
            <td>${cli.email}</td>
            <td><span class="badge ${cli.ativo ? 'badge-active' : 'badge-inactive'}">${cli.ativo ? 'Ativo' : 'Inativo'}</span></td>
            <td><div class="action-buttons">${botoes}</div></td>
        `;
        tbody.appendChild(row);
    });
}

function openModal() {
    document.getElementById('clienteModal').classList.add('active');
    document.getElementById('modalTitle').innerText = clienteIdEdicao ? "Editar Cliente" : "Novo Cliente";
}

function closeModal() {
    document.getElementById('clienteModal').classList.remove('active');
    clienteIdEdicao = null;
    formCadastro.reset();
    msgDiv.innerText = "";
}

function switchTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    if (tabName === 'ativos') {
        document.getElementById('tabAtivos').classList.add('active');
        document.querySelectorAll('.tab-btn')[0].classList.add('active');
        carregarAtivos();
    } else {
        document.getElementById('tabInativos').classList.add('active');
        document.querySelectorAll('.tab-btn')[1].classList.add('active');
        carregarInativos();
    }
}

function editCliente(id) {
    const c = listaClientesAtivos.find(item => item.id === id);
    if (c) {
        clienteIdEdicao = id;
        document.getElementById('cliNome').value = c.nome;
        document.getElementById('cliCpf').value = c.cpf;
        document.getElementById('cliEmail').value = c.email;
        document.getElementById('cliTelefone').value = c.telefone;
        document.getElementById('cliCep').value = c.cep;
        document.getElementById('cliLogradouro').value = c.logradouro;
        document.getElementById('cliNumero').value = c.numero;
        document.getElementById('cliBairro').value = c.bairro;
        document.getElementById('cliCidade').value = c.cidade;
        document.getElementById('cliEstado').value = c.estado;
        document.getElementById('cliAtivo').value = c.ativo.toString();
        openModal();
    }
}

async function excluirCliente(id) {
    if (confirm("Deseja realmente desativar este cliente?")) {
        const res = await fetch(`${API_URL}/excluir/${id}`, { method: 'DELETE' });
        if (res.ok) {
            carregarAtivos();
            carregarInativos();
        }
    }
}

async function reativarCliente(id) {
    if (confirm("Deseja realmente restaurar este cliente para a lista de ativos?")) {
        const res = await fetch(`${API_URL}/restaurar/${id}`, { method: 'PUT' });
        if (res.ok) {
            carregarAtivos();
            carregarInativos();
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    configurarMascaras();
    carregarAtivos();

    if (inputBuscaAtivos) {
        inputBuscaAtivos.addEventListener('keyup', () => filtrarTabela(inputBuscaAtivos, tableBodyAtivos));
    }
    if (inputBuscaInativos) {
        inputBuscaInativos.addEventListener('keyup', () => filtrarTabela(inputBuscaInativos, tableBodyInativos));
    }
});
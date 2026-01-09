const formCadastro = document.getElementById('fornecedorForm');
const msgDiv = document.getElementById('mensagem');
const tableBodyAtivos = document.querySelector('#fornecedorTable tbody');
const tableBodyInativos = document.querySelector('#fornecedorTableInativos tbody');

let fornecedorIdEdicao = null;
const API_URL = '/fornecedores';

function configurarMascaras() {
    const masks = {
        fornCnpj: (v) => v.replace(/\D/g, '')
                           .replace(/^(\d{2})(\d)/, '$1.$2')
                           .replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3')
                           .replace(/\.(\d{3})(\d)/, '.$1/$2')
                           .replace(/(\d{4})(\d)/, '$1-$2')
                           .slice(0, 18),
        fornTelefone: (v) => v.replace(/\D/g, '').replace(/^(\d{2})(\d)/g, '($1) $2').replace(/(\d{5})(\d)/, '$1-$2'),
        fornCep: (v) => v.replace(/\D/g, '').replace(/(\d{5})(\d)/, '$1-$2')
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

    const campos = ['fornLogradouro', 'fornBairro', 'fornCidade', 'fornEstado'];
    campos.forEach(id => document.getElementById(id).value = "...");

    try {
        const response = await fetch(`${API_URL}/consulta-cep-fornecedor/${cep}`);

        if (!response.ok) throw new Error("CEP não encontrado");

        const dados = await response.json();
        if (dados.erro) throw new Error("CEP inexistente");

        document.getElementById('fornLogradouro').value = dados.logradouro || '';
        document.getElementById('fornBairro').value = dados.bairro || '';
        document.getElementById('fornCidade').value = dados.localidade || '';
        document.getElementById('fornEstado').value = dados.uf || '';
        document.getElementById('fornNumero').focus();
    } catch (error) {
        alert("Erro ao buscar CEP: " + error.message);
        campos.forEach(id => document.getElementById(id).value = "");
    }
}

formCadastro.addEventListener('submit', async (e) => {
    e.preventDefault();

    const dados = {
        nomeFantasia: document.getElementById('fornNomeFantasia').value,
        razaoSocial: document.getElementById('fornRazaoSocial').value,
        cnpj: document.getElementById('fornCnpj').value,
        email: document.getElementById('fornEmail').value,
        telefone: document.getElementById('fornTelefone').value,
        ativo: document.getElementById('fornAtivo').value === 'true',
        cep: document.getElementById('fornCep').value,
        logradouro: document.getElementById('fornLogradouro').value,
        numero: document.getElementById('fornNumero').value,
        bairro: document.getElementById('fornBairro').value,
        cidade: document.getElementById('fornCidade').value,
        estado: document.getElementById('fornEstado').value
    };

    const url = fornecedorIdEdicao ? `${API_URL}/editar/${fornecedorIdEdicao}` : `${API_URL}/cadastrar`;
    const method = fornecedorIdEdicao ? 'PUT' : 'POST';

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
        const res = await fetch(`${API_URL}/listar-ativos`);
        const lista = await res.json();
        renderizarTabela(lista, tableBodyAtivos, true);
    } catch (error) {
        console.error(error);
    }
}

async function carregarInativos() {
    try {
        const res = await fetch(`${API_URL}/listar-inativos`);
        const lista = await res.json();
        renderizarTabela(lista, tableBodyInativos, false);
    } catch (error) {
        console.error(error);
    }
}

function renderizarTabela(lista, tbody, isAtivo) {
    if (!tbody) return;
    tbody.innerHTML = '';
    lista.forEach(forn => {
        const row = document.createElement('tr');
        const statusClass = forn.ativo ? 'badge-active' : 'badge-inactive';
        const statusText = forn.ativo ? 'Ativo' : 'Inativo';

        const botoes = isAtivo ?
            `<button class="btn-action btn-edit" onclick="editFornecedor(${forn.id})">✏️</button>
             <button class="btn-action btn-delete" onclick="excluirFornecedor(${forn.id})">🗑️</button>` :
            `<button class="btn-action btn-restore" onclick="reativarFornecedor(${forn.id})">🔄</button>`;

        row.innerHTML = `
            <td>${forn.id}</td>
            <td>${forn.nomeFantasia}</td>
            <td>${forn.cnpj}</td>
            <td>${forn.telefone || '-'}</td>
            <td>${forn.email}</td>
            <td><span class="badge ${statusClass}">${statusText}</span></td>
            <td><div class="action-buttons">${botoes}</div></td>
        `;
        tbody.appendChild(row);
    });
}

async function editFornecedor(id) {
    const res = await fetch(`${API_URL}/listar-ativos`);
    const lista = await res.json();
    const f = lista.find(item => item.id === id);

    if (f) {
        fornecedorIdEdicao = id;
        document.getElementById('fornNomeFantasia').value = f.nomeFantasia;
        document.getElementById('fornRazaoSocial').value = f.razaoSocial;
        document.getElementById('fornCnpj').value = f.cnpj;
        document.getElementById('fornEmail').value = f.email;
        document.getElementById('fornTelefone').value = f.telefone || '';
        document.getElementById('fornAtivo').value = f.ativo.toString();
        document.getElementById('fornCep').value = f.cep || '';
        document.getElementById('fornLogradouro').value = f.logradouro || '';
        document.getElementById('fornNumero').value = f.numero || '';
        document.getElementById('fornBairro').value = f.bairro || '';
        document.getElementById('fornCidade').value = f.cidade || '';
        document.getElementById('fornEstado').value = f.estado || '';

        document.getElementById('modalTitle').textContent = 'Editar Fornecedor';
        openModal();
    }
}

async function excluirFornecedor(id) {
    if (confirm("Deseja excluir este fornecedor?")) {
        await fetch(`${API_URL}/excluir/${id}`, { method: 'DELETE' });
        carregarAtivos();
        carregarInativos();
    }
}

async function reativarFornecedor(id) {
    if (confirm("Deseja restaurar este fornecedor?")) {
        const res = await fetch(`${API_URL}/restaurar/${id}`, { method: 'PUT' });
        if(res.ok) {
            carregarAtivos();
            carregarInativos();
        }
    }
}

function openModal() {
    document.getElementById('fornecedorModal').classList.add('active');
    msgDiv.innerText = "";
}

function closeModal() {
    document.getElementById('fornecedorModal').classList.remove('active');
    fornecedorIdEdicao = null;
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

    const inputBuscaAtivos = document.getElementById('searchInputAtivos');
    if (inputBuscaAtivos) {
        inputBuscaAtivos.addEventListener('input', (e) => pesquisarAtivos(e.target.value));
    }

    const inputBuscaInativos = document.getElementById('searchInputInativos');
    if (inputBuscaInativos) {
        inputBuscaInativos.addEventListener('input', (e) => pesquisarInativos(e.target.value));
    }
});
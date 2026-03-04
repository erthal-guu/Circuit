let listaContasReceber = [];
let listaClientes = [];

document.addEventListener('DOMContentLoaded', function() {
    carregarContasReceber();
    carregarClientes();
    setupSearch('searchContas', 'tabelaContasReceber', 'tr');
});

function carregarContasReceber() {
    fetch('/api/contas-receber')
        .then(response => response.json())
        .then(data => {
            listaContasReceber = data;
            renderizarTabelaContasReceber();
        })
        .catch(error => console.error('Erro ao carregar contas a receber:', error));
}

function carregarClientes() {
    fetch('/api/clientes')
        .then(response => response.json())
        .then(data => {
            listaClientes = data;
            preencherSelectCliente();
        })
        .catch(error => console.error('Erro ao carregar clientes:', error));
}

function renderizarTabelaContasReceber() {
    const tbody = document.querySelector('#tabelaContasReceber tbody');
    tbody.innerHTML = '';

    listaContasReceber.forEach(conta => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><strong>#${conta.id}</strong></td>
            <td>${conta.cliente?.nome || '-'}</td>
            <td>${conta.descricao || '-'}</td>
            <td>${formatarData(conta.dataVencimento)}</td>
            <td>R$ ${formatarMoeda(conta.valor)}</td>
            <td><span class="badge ${getBadgeClass(conta.status)}">${conta.status}</span></td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" type="button" onclick="editarContaReceber(${conta.id})">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                        </svg>
                    </button>
                    <button class="btn-action btn-delete" type="button" onclick="deletarContaReceber(${conta.id})">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <polyline points="3 6 5 6 21 6"></polyline>
                            <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                        </svg>
                    </button>
                </div>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function preencherSelectCliente() {
    const select = document.getElementById('cliente');
    select.innerHTML = '<option value="">Selecione um cliente</option>';
    listaClientes.forEach(cliente => {
        const option = document.createElement('option');
        option.value = cliente.id;
        option.textContent = cliente.nome;
        select.appendChild(option);
    });
}

function abrirModalNovaContaReceber() {
    document.getElementById('formContaReceber').reset();
    document.getElementById('contaReceberId').value = '';
    document.getElementById('modalContaReceber').style.display = 'block';
}

function editarContaReceber(id) {
    const conta = listaContasReceber.find(c => c.id === id);
    if (conta) {
        document.getElementById('contaReceberId').value = conta.id;
        document.getElementById('cliente').value = conta.cliente?.id || '';
        document.getElementById('descricao').value = conta.descricao || '';
        document.getElementById('valor').value = conta.valor || '';
        document.getElementById('dataVencimento').value = conta.dataVencimento || '';
        document.getElementById('status').value = conta.status || 'PENDENTE';
        document.getElementById('modalContaReceber').style.display = 'block';
    }
}

function salvarContaReceber() {
    const id = document.getElementById('contaReceberId').value;
    const contaData = {
        cliente: { id: parseInt(document.getElementById('cliente').value) },
        descricao: document.getElementById('descricao').value,
        valor: parseFloat(document.getElementById('valor').value),
        dataVencimento: document.getElementById('dataVencimento').value,
        status: document.getElementById('status').value
    };

    const url = id ? `/api/contas-receber/${id}` : '/api/contas-receber';
    const method = id ? 'PUT' : 'POST';

    fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(contaData)
    })
    .then(response => {
        if (response.ok) {
            carregarContasReceber();
            fecharModalContaReceber();
            alert('Conta salva com sucesso!');
        } else {
            alert('Erro ao salvar conta.');
        }
    })
    .catch(error => console.error('Erro ao salvar conta:', error));
}

function deletarContaReceber(id) {
    if (confirm('Tem certeza que deseja excluir esta conta?')) {
        fetch(`/api/contas-receber/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                carregarContasReceber();
                alert('Conta excluída com sucesso!');
            } else {
                alert('Erro ao excluir conta.');
            }
        })
        .catch(error => console.error('Erro ao excluir conta:', error));
    }
}

function fecharModalContaReceber() {
    document.getElementById('modalContaReceber').style.display = 'none';
}

function formatarData(data) {
    if (!data) return '-';
    return new Date(data).toLocaleDateString('pt-BR');
}

function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function getBadgeClass(status) {
    switch(status) {
        case 'PENDENTE': return 'badge-warning';
        case 'VENCIDO': return 'badge-danger';
        case 'RECEBIDO': return 'badge-success';
        default: return 'badge-secondary';
    }
}

function setupSearch(searchInputId, tableId, rowSelector) {
    const searchInput = document.getElementById(searchInputId);
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const filter = this.value.toLowerCase();
            const table = document.getElementById(tableId);
            const rows = table.querySelectorAll(rowSelector);

            rows.forEach(row => {
                const text = row.textContent.toLowerCase();
                row.style.display = text.includes(filter) ? '' : 'none';
            });
        });
    }
}

window.onclick = function(event) {
    const modal = document.getElementById('modalContaReceber');
    if (event.target == modal) {
        fecharModalContaReceber();
    }
}

let listaContasPagar = [];
let listaFornecedores = [];

document.addEventListener('DOMContentLoaded', function() {
    carregarContasPagar();
    carregarFornecedores();
    setupSearch('searchContas', 'tabelaContasPagar', 'tr');
});

function carregarContasPagar() {
    fetch('/api/contas-pagar')
        .then(response => response.json())
        .then(data => {
            listaContasPagar = data;
            renderizarTabelaContasPagar();
        })
        .catch(error => console.error('Erro ao carregar contas a pagar:', error));
}

function carregarFornecedores() {
    fetch('/api/fornecedores')
        .then(response => response.json())
        .then(data => {
            listaFornecedores = data;
            preencherSelectFornecedor();
        })
        .catch(error => console.error('Erro ao carregar fornecedores:', error));
}

function renderizarTabelaContasPagar() {
    const tbody = document.querySelector('#tabelaContasPagar tbody');
    tbody.innerHTML = '';

    listaContasPagar.forEach(conta => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td><strong>#${conta.id}</strong></td>
            <td>${conta.fornecedor?.nome || '-'}</td>
            <td>${conta.descricao || '-'}</td>
            <td>${formatarData(conta.dataVencimento)}</td>
            <td>R$ ${formatarMoeda(conta.valor)}</td>
            <td><span class="badge ${getBadgeClass(conta.status)}">${conta.status}</span></td>
            <td>
                <div class="action-buttons">
                    <button class="btn-action btn-edit" type="button" onclick="editarContaPagar(${conta.id})">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                            <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"></path>
                            <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"></path>
                        </svg>
                    </button>
                    <button class="btn-action btn-delete" type="button" onclick="deletarContaPagar(${conta.id})">
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

function preencherSelectFornecedor() {
    const select = document.getElementById('fornecedor');
    select.innerHTML = '<option value="">Selecione um fornecedor</option>';
    listaFornecedores.forEach(fornecedor => {
        const option = document.createElement('option');
        option.value = fornecedor.id;
        option.textContent = fornecedor.nome;
        select.appendChild(option);
    });
}

function abrirModalNovaContaPagar() {
    document.getElementById('formContaPagar').reset();
    document.getElementById('contaPagarId').value = '';
    document.getElementById('modalContaPagar').style.display = 'block';
}

function editarContaPagar(id) {
    const conta = listaContasPagar.find(c => c.id === id);
    if (conta) {
        document.getElementById('contaPagarId').value = conta.id;
        document.getElementById('fornecedor').value = conta.fornecedor?.id || '';
        document.getElementById('descricao').value = conta.descricao || '';
        document.getElementById('valor').value = conta.valor || '';
        document.getElementById('dataVencimento').value = conta.dataVencimento || '';
        document.getElementById('status').value = conta.status || 'PENDENTE';
        document.getElementById('modalContaPagar').style.display = 'block';
    }
}

function salvarContaPagar() {
    const id = document.getElementById('contaPagarId').value;
    const contaData = {
        fornecedor: { id: parseInt(document.getElementById('fornecedor').value) },
        descricao: document.getElementById('descricao').value,
        valor: parseFloat(document.getElementById('valor').value),
        dataVencimento: document.getElementById('dataVencimento').value,
        status: document.getElementById('status').value
    };

    const url = id ? `/api/contas-pagar/${id}` : '/api/contas-pagar';
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
            carregarContasPagar();
            fecharModalContaPagar();
            alert('Conta salva com sucesso!');
        } else {
            alert('Erro ao salvar conta.');
        }
    })
    .catch(error => console.error('Erro ao salvar conta:', error));
}

function deletarContaPagar(id) {
    if (confirm('Tem certeza que deseja excluir esta conta?')) {
        fetch(`/api/contas-pagar/${id}`, {
            method: 'DELETE'
        })
        .then(response => {
            if (response.ok) {
                carregarContasPagar();
                alert('Conta excluída com sucesso!');
            } else {
                alert('Erro ao excluir conta.');
            }
        })
        .catch(error => console.error('Erro ao excluir conta:', error));
    }
}

function fecharModalContaPagar() {
    document.getElementById('modalContaPagar').style.display = 'none';
}

function abrirModalPagar(id, valor, fornecedor) {
    document.getElementById('pagarId').value = id;
    document.getElementById('pagarFornecedor').value = fornecedor;
    document.getElementById('pagarValor').value = valor;
    document.getElementById('pagarValorPago').value = valor;
    
    // Define a data de pagamento como hoje
    const hoje = new Date().toISOString().split('T')[0];
    document.getElementById('pagarDataPagamento').value = hoje;
    
    // Limpa a forma de pagamento
    document.getElementById('pagarFormaPagamento').value = '';
    
    document.getElementById('modalPagar').style.display = 'block';
}

function fecharModalPagar() {
    document.getElementById('modalPagar').style.display = 'none';
    document.getElementById('formPagar').reset();
}

async function confirmarPagar() {
    const id = document.getElementById('pagarId').value;
    const valorPago = document.getElementById('pagarValorPago').value;
    const dataPagamento = document.getElementById('pagarDataPagamento').value;
    const formaPagamento = document.getElementById('pagarFormaPagamento').value;
    
    // Validação
    if (!valorPago || valorPago <= 0) {
        alert('Por favor, informe o valor pago.');
        return;
    }
    
    if (!dataPagamento) {
        alert('Por favor, informe a data do pagamento.');
        return;
    }
    
    if (!formaPagamento) {
        alert('Por favor, selecione a forma de pagamento.');
        return;
    }
    
    try {
        const response = await fetch(`/contas-pagar/pagar`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: new URLSearchParams({
                id: id,
                valorPago: valorPago,
                dataPagamento: dataPagamento,
                formaPagamento: formaPagamento
            })
        });
        
        if (response.ok) {
            alert('Pagamento realizado com sucesso!');
            fecharModalPagar();
            location.reload();
        } else {
            alert('Erro ao realizar pagamento. Tente novamente.');
        }
    } catch (error) {
        console.error('Erro:', error);
        alert('Erro ao realizar pagamento. Tente novamente.');
    }
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
        case 'PAGO': return 'badge-success';
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
    const modalContaPagar = document.getElementById('modalContaPagar');
    const modalPagar = document.getElementById('modalPagar');
    if (event.target == modalContaPagar) {
        fecharModalContaPagar();
    }
    if (event.target == modalPagar) {
        fecharModalPagar();
    }
}

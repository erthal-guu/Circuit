document.addEventListener('DOMContentLoaded', function() {
    setupSearch('searchContas', 'tabelaContasPagar', 'tr');
});

function abrirModalNovaContaPagar() {
    document.getElementById('formContaPagar').reset();
    document.getElementById('contaPagarId').value = '';
    document.getElementById('modalContaPagar').style.display = 'block';
}

function fecharModalContaPagar() {
    document.getElementById('modalContaPagar').style.display = 'none';
}

function fecharModalPagar() {
    document.getElementById('modalPagar').style.display = 'none';
}

function editarContaPagar(id) {
    // Esta função pode ser implementada posteriormente para editar contas
    console.log('Editar conta:', id);
    alert('Funcionalidade de edição será implementada em breve.');
}

function deletarContaPagar(id) {
    // Esta função pode ser implementada posteriormente para deletar contas
    console.log('Deletar conta:', id);
    alert('Funcionalidade de exclusão será implementada em breve.');
}

function salvarContaPagar() {
    // Esta função pode ser implementada posteriormente para salvar contas
    console.log('Salvar conta');
    alert('Funcionalidade de salvamento será implementada em breve.');
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

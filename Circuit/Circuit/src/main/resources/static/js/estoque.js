const modal = document.getElementById('estoqueModal');
const formEstoque = document.getElementById('estoqueForm');

function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    if (tabName === 'ativos') {
        document.getElementById('tabAtivos').classList.add('active');
    } else {
        document.getElementById('tabInativos').classList.add('active');
    }

    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }
}

function abrirModalNovo() {
    formEstoque.reset();
    document.getElementById('prodId').value = '';
    document.getElementById('modalTitle').innerText = 'Cadastrar Produto';
    if (modal) {
        modal.classList.add('active');
    }
}

function abrirModalEdicao(btn) {
    document.getElementById('modalTitle').innerText = 'Editar Produto';

    document.getElementById('prodId').value = btn.getAttribute('data-id') || '';
    document.getElementById('prodNome').value = btn.getAttribute('data-nome') || '';
    document.getElementById('prodCodigoBarras').value = btn.getAttribute('data-codigobarras') || '';
    document.getElementById('prodCategoria').value = btn.getAttribute('data-categoria') || '';
    document.getElementById('prodFornecedor').value = btn.getAttribute('data-fornecedor') || '';
    document.getElementById('prodQuantidade').value = btn.getAttribute('data-quantidade') || '';
    document.getElementById('prodQuantidadeMinima').value = btn.getAttribute('data-minima') || '';
    document.getElementById('prodPrecoCompra').value = btn.getAttribute('data-compra') || '';
    document.getElementById('prodPrecoVenda').value = btn.getAttribute('data-venda') || '';
    document.getElementById('prodAtivo').value = 'true';

    if (modal) {
        modal.classList.add('active');
    }
}

function closeModal() {
    if (modal) {
        modal.classList.remove('active');
    }
}

window.onclick = function(event) {
    if (event.target === modal) {
        closeModal();
    }
};
const modal = document.getElementById('estoqueModal');
const formEstoque = document.getElementById('estoqueForm');

function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }
    const displayTotal = document.getElementById('displayTotal');
    const tituloTotal = document.getElementById('stat-title');
    const colorCard = document.getElementById("stat-total")

    if (tabName === 'ativos') {
        document.getElementById('tabAtivos').classList.add('active');
        displayTotal.innerText = displayTotal.getAttribute('data-ativos');
        tituloTotal.innerText = "Total de produtos Ativos";
        colorCard.style.borderLeft = "4px solid forestgreen";

    } else {
        document.getElementById('tabInativos').classList.add('active');
        displayTotal.innerText = displayTotal.getAttribute('data-inativos');
        tituloTotal.innerText = "Total de produtos Inativos";
        colorCard.style.borderLeft = "4px solid red";
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
const modalAlimentar = document.getElementById('modalAlimentar');

function abrirModalAlimentar(btn) {
    const id = btn.getAttribute('data-id');
    const nome = btn.getAttribute('data-nome');

    document.getElementById('alimentarId').value = id;
    document.getElementById('alimentarNome').value = nome;

    if (modalAlimentar) {
        modalAlimentar.classList.add('active');
    }
}

function fecharModalAlimentar() {
    if (modalAlimentar) {
        modalAlimentar.classList.remove('active');
    }
}
const modalRetirar = document.getElementById('modalRetirar');

function abrirModalRetirar(btn) {
    document.getElementById('retirarId').value = btn.getAttribute('data-id');
    document.getElementById('retirarNome').value = btn.getAttribute('data-nome');
    modalRetirar.classList.add('active');
}

function fecharModalRetirar() {
    modalRetirar.classList.remove('active');
}
function configurarPesquisaLocal(inputId, tableId) {
    const input = document.getElementById(inputId);
    const table = document.getElementById(tableId);
    if (!input || !table) return;
    input.addEventListener('keyup', function () {
        const termo = input.value.toLowerCase();
        const linhas = table.querySelectorAll('tbody tr');

        linhas.forEach(linha => {
            linha.style.display = linha.innerText.toLowerCase().includes(termo) ? '' : 'none';
        });
    });
}

document.addEventListener("DOMContentLoaded", function() {
    configurarPesquisaLocal('searchInputAtivos', 'estoqueTable');
    configurarPesquisaLocal('searchInputInativos', 'estoqueTableInativos');
});
const modalMovimentacoes = document.getElementById('modalMovimentacoes');
const contadorSelecionados = document.getElementById('contadorSelecionados');

function abrirModalMovimentacoes() {
    if(modalMovimentacoes) modalMovimentacoes.classList.add('active');
}

function fecharModalMovimentacoes() {
    if(modalMovimentacoes) modalMovimentacoes.classList.remove('active');
    document.querySelectorAll('.produto-check').forEach(cb => cb.checked = false);
    document.getElementById('checkMaster').checked = false;
    atualizarContador();
}
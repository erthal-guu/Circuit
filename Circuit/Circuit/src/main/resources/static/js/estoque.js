const modal = document.getElementById('estoqueModal');
const formEstoque = document.getElementById('estoqueForm');


function obterTipoEstoque(){
    return document.getElementById("tipoEstoqueGlobal").value;
}
function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }
    const displayTotal = document.getElementById('displayTotal');
    const tituloTotal = document.getElementById('stat-title');
    const colorCard = document.getElementById("stat-total");
    const colorSvg = document.getElementById("icon-status")
    const tipo = obterTipoEstoque();
    const isProduto = (tipo === "produto");

    if (isProduto) {
        if (tabName === 'ativos') {
            document.getElementById('tabAtivos').classList.add('active');
            displayTotal.innerText = displayTotal.getAttribute('data-ativos');
            tituloTotal.innerText = "Total de produtos Ativos";
            colorCard.style.borderLeft = "5px solid #282cb2";
            colorSvg.style.color = "#282cb2";
        } else {
            document.getElementById('tabInativos').classList.add('active');
            displayTotal.innerText = displayTotal.getAttribute('data-inativos');
            tituloTotal.innerText = "Total de produtos Inativos";
            colorCard.style.borderLeft = "5px solid red";
            colorSvg.style.color = "red";
        }
    } else {
        if (tabName === 'ativos') {
            document.getElementById('tabAtivos').classList.add('active');
            displayTotal.innerText = displayTotal.getAttribute('data-ativos');
            tituloTotal.innerText = "Total de peças Ativas";
            colorCard.style.borderLeft = "5px solid #282cb2";
            colorSvg.style.color = "#282cb2";
        } else {
            document.getElementById('tabInativos').classList.add('active');
            displayTotal.innerText = displayTotal.getAttribute('data-inativos');
            tituloTotal.innerText = "Total de peças Inativas";
            colorCard.style.borderLeft = "5px solid red";
            colorSvg.style.color = "red";
        }
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

function filtrarTabelaMovimentacao() {
    const termo = document.getElementById('searchMovimentacao').value.toLowerCase();
    const linhas = document.querySelectorAll('#tabelaMovimentacao tbody tr');

    linhas.forEach(linha => {
        const texto = linha.innerText.toLowerCase();
        linha.style.display = texto.includes(termo) ? '' : 'none';
    });
}
function atualizarContador() {
    const total = document.querySelectorAll('.produto-check:checked').length;
    const contador = document.getElementById('contadorSelecionados');
    if(contador) contador.innerText = total;
}

function toggleSelecionarTodos() {
    const master = document.getElementById('checkMaster');
    const checks = document.querySelectorAll('.produto-check');

    checks.forEach(check => {
        if(check.closest('tr').style.display !== 'none') {
            check.checked = master.checked;
        }
    });
    atualizarContador();
}
document.addEventListener('change', function(e) {
    if(e.target.classList.contains('produto-check')) {
        atualizarContador();
    }
});
document.getElementById('formMovimentacao').addEventListener('submit', function(event) {
    const selecionados = document.querySelectorAll('.produto-check:checked').length;
    const quantidade = document.getElementById('inputQtdMassa').value;

    if (selecionados === 0) {
        event.preventDefault();
        alert("Selecione pelo menos um produto!");
        return;
    }

    if (!quantidade || quantidade < 1) {
        event.preventDefault();
        alert("Digite uma quantidade válida!");
    }
});
document.addEventListener("DOMContentLoaded", function() {
    const alertas = document.querySelectorAll('.auto-close');
    alertas.forEach(alerta => {
        setTimeout(() => {
            alerta.style.opacity = '0';
            setTimeout(() => {
                alerta.remove();
            }, 500);

        }, 3000);
    });
});
function mascaraMoeda(event) {
    const input = event.target;
    let valor = input.value;
    valor = valor.replace(/\D/g, "");
    valor = (valor / 100).toFixed(2) + "";
    valor = valor.replace(".", ",");
    valor = valor.replace(/(\d)(\d{3})(\d{3}),/g, "$1.$2.$3,");
    valor = valor.replace(/(\d)(\d{3}),/g, "$1.$2,");

    input.value = valor;
}
document.addEventListener("DOMContentLoaded", function() {
    const camposPreco = [document.getElementById('prodPrecoCompra'), document.getElementById('prodPrecoVenda')];

    camposPreco.forEach(campo => {
        if (campo) {
            campo.addEventListener('input', mascaraMoeda);
        }
    });
});
function exportarParaPDF() {
    const abaAtiva = document.querySelector('.tab-content.active');
    if (!abaAtiva) {
        alert("Nenhuma tabela visível para exportar.");
        return;
    }
    const tabelaOriginal = abaAtiva.querySelector('table');
    const tabelaClone = tabelaOriginal.cloneNode(true);
    const ths = tabelaClone.querySelectorAll('th');
    if (ths.length > 0) ths[ths.length - 1].remove();
    tabelaClone.querySelectorAll('tr').forEach(tr => {
        const tds = tr.querySelectorAll('td');
        if (tds.length > 0) tds[tds.length - 1].remove();
    });
    const container = document.createElement('div');
    container.style.padding = '20px';
    container.style.fontFamily = 'Arial, sans-serif';
    container.style.color = '#000';
    const dataHoje = new Date().toLocaleDateString('pt-BR');
    const titulo = document.querySelector('.page-title').innerText;
    const subtitulo = abaAtiva.id === 'tabAtivos' ? 'Listagem de Itens Ativos' : 'Listagem de Itens Inativos';

    container.innerHTML = `
        <div style="text-align: center; margin-bottom: 20px; border-bottom: 2px solid #001d3d; padding-bottom: 10px;">
            <h2 style="color: #001d3d; margin: 0;">${titulo}</h2>
            <h4 style="color: #666; margin: 5px 0;">${subtitulo}</h4>
            <small>Gerado em: ${dataHoje}</small>
        </div>
    `;
    container.appendChild(tabelaClone);
    const opt = {
        margin:       10,
        filename:     `Listagem de estoque ${dataHoje.replace(/\//g, '-')}.pdf`,
        image:        { type: 'jpeg', quality: 0.98 },
        html2canvas:  { scale: 2 },
        jsPDF:        { unit: 'mm', format: 'a4', orientation: 'portrait' }
    };
    html2pdf().set(opt).from(container).save();
}
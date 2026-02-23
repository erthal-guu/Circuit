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
    const colorSvg = document.getElementById("icon-status");
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
    if (modal) modal.classList.add('active');
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
    if (modal) modal.classList.add('active');
}

function closeModal() {
    if (modal) modal.classList.remove('active');
}

window.onclick = function(event) {
    if (event.target === modal) closeModal();
};

const modalAlimentar = document.getElementById('modalAlimentar');

function abrirModalAlimentar(btn) {
    const id = btn.getAttribute('data-id');
    const nome = btn.getAttribute('data-nome');
    document.getElementById('alimentarId').value = id;
    document.getElementById('alimentarNome').value = nome;
    if (modalAlimentar) modalAlimentar.classList.add('active');
}

function fecharModalAlimentar() {
    if (modalAlimentar) modalAlimentar.classList.remove('active');
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

const modalMovimentacoes = document.getElementById('modalMovimentacoes');

function abrirModalMovimentacoes() {
    if (modalMovimentacoes) modalMovimentacoes.classList.add('active');
}

function fecharModalMovimentacoes() {
    if (modalMovimentacoes) modalMovimentacoes.classList.remove('active');
    document.querySelectorAll('.produto-check').forEach(cb => cb.checked = false);
    document.getElementById('checkMaster').checked = false;
    atualizarContador();
}

function filtrarTabelaMovimentacao() {
    const termo = document.getElementById('searchMovimentacao').value.toLowerCase();
    const linhas = document.querySelectorAll('#tabelaMovimentacao tbody tr');
    linhas.forEach(linha => {
        linha.style.display = linha.innerText.toLowerCase().includes(termo) ? '' : 'none';
    });
}

function atualizarContador() {
    const total = document.querySelectorAll('.produto-check:checked').length;
    const contador = document.getElementById('contadorSelecionados');
    if (contador) contador.innerText = total;
}

function toggleSelecionarTodos() {
    const master = document.getElementById('checkMaster');
    const checks = document.querySelectorAll('.produto-check');
    checks.forEach(check => {
        if (check.closest('tr').style.display !== 'none') {
            check.checked = master.checked;
        }
    });
    atualizarContador();
}

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
        margin: 10,
        filename: `Listagem de estoque ${dataHoje.replace(/\//g, '-')}.pdf`,
        image: { type: 'jpeg', quality: 0.98 },
        html2canvas: { scale: 2 },
        jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
    };
    html2pdf().set(opt).from(container).save();
}

const TIPO_ESTOQUE = 'PRODUTO';

function abrirModalNotificacoes() {
    const modalNotif = document.getElementById('modalNotificacoes');
    if (modalNotif) modalNotif.style.display = 'flex';
    carregarNotificacoes();
}

function fecharModalNotificacoes() {
    const modalNotif = document.getElementById('modalNotificacoes');
    if (modalNotif) modalNotif.style.display = 'none';
}

function carregarNotificacoes() {
    fetch(`/notificacoes/pendentes?tipo=${TIPO_ESTOQUE}`)
        .then(res => res.json())
        .then(data => {
            atualizarBadge(data.length);
            renderizarNotificacoes(data);
        })
        .catch(() => console.error('Erro ao carregar notificações'));
}

function atualizarBadge(total) {
    const badge = document.getElementById('badge-notificacao');
    if (!badge) return;
    if (total > 0) {
        badge.textContent = total;
        badge.style.display = 'flex';
    } else {
        badge.style.display = 'none';
    }
}

function renderizarNotificacoes(data) {
    const lista = document.getElementById('lista-notificacoes');
    if (!lista) return;

    if (data.length === 0) {
        lista.innerHTML = `
            <div style="padding: 32px 20px; text-align: center; color: #94a3b8;">
                <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" stroke-width="1.5" style="margin-bottom:8px;">
                    <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path>
                    <path d="M13.73 21a2 2 0 0 1-3.46 0"></path>
                </svg>
                <p style="font-size: 13px; margin: 0;">Nenhuma notificação pendente.</p>
            </div>`;
        return;
    }

    lista.innerHTML = data.map(n => `
        <div style="padding: 16px 20px; border-bottom: 1px solid #f1f5f9; display: flex; gap: 12px; align-items: flex-start; background: #fffbeb; transition: background 0.2s;"
             onmouseover="this.style.background='#fef3c7'" onmouseout="this.style.background='#fffbeb'">
            <div style="background: #f59e0b; color: white; padding: 8px; border-radius: 50%; display: flex; align-items: center; justify-content: center; flex-shrink: 0;">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                    <polyline points="7 10 12 15 17 10"></polyline>
                    <line x1="12" y1="15" x2="12" y2="3"></line>
                </svg>
            </div>
            <div style="flex: 1;">
                <div style="display: flex; justify-content: space-between; align-items: flex-start;">
                    <h4 style="margin: 0 0 4px 0; font-size: 13px; color: #1e293b; font-weight: 600;">Entrada Pendente</h4>
                    <span style="font-size: 11px; color: #94a3b8; font-weight: 500;">${n.tempoAtras}</span>
                </div>
                <p style="margin: 0 0 12px 0; font-size: 12px; color: #475569; line-height: 1.4;">
                    O pedido <strong style="color: #0f172a;">${n.codigo}</strong> foi recebido. Efetive para atualizar o saldo.
                </p>
                <div style="display: flex; gap: 8px;">
                    <button onclick="efetivarEntrada(${n.id})"
                            style="background: #10b981; color: white; border: none; padding: 6px 12px; border-radius: 6px; font-size: 12px; font-weight: 600; cursor: pointer; display: flex; align-items: center; gap: 6px;"
                            onmouseover="this.style.background='#059669'" onmouseout="this.style.background='#10b981'">
                        <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
                            <polyline points="20 6 9 17 4 12"></polyline>
                        </svg>
                        Efetivar Entrada
                    </button>
                    <button onclick="fecharModalNotificacoes()"
                            style="background: white; color: #475569; border: 1px solid #cbd5e1; padding: 6px 12px; border-radius: 6px; font-size: 12px; font-weight: 600; cursor: pointer;">
                        Revisar
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function efetivarEntrada(id) {
    fetch(`/notificacoes/efetivar/${id}`, { method: 'POST' })
        .then(res => {
            if (res.ok) {
                carregarNotificacoes();
                setTimeout(() => location.reload(), 500);
            } else {
                alert('Erro ao efetivar entrada. Tente novamente.');
            }
        })
        .catch(() => alert('Erro de conexão.'));
}
document.addEventListener("DOMContentLoaded", function () {
    configurarPesquisaLocal('searchInputAtivos', 'estoqueTable');
    configurarPesquisaLocal('searchInputInativos', 'estoqueTableInativos');

    // Auto-close nos alertas
    document.querySelectorAll('.auto-close').forEach(alerta => {
        setTimeout(() => {
            alerta.style.opacity = '0';
            setTimeout(() => alerta.remove(), 500);
        }, 3000);
    });

    const camposPreco = [document.getElementById('prodPrecoCompra'), document.getElementById('prodPrecoVenda')];
    camposPreco.forEach(campo => {
        if (campo) campo.addEventListener('input', mascaraMoeda);
    });

    document.addEventListener('change', function (e) {
        if (e.target.classList.contains('produto-check')) atualizarContador();
    });
    const formMov = document.getElementById('formMovimentacao');
    if (formMov) {
        formMov.addEventListener('submit', function (event) {
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
    }
    const modalNotif = document.getElementById('modalNotificacoes');
    if (modalNotif) {
        modalNotif.addEventListener('click', function (e) {
            if (e.target === this) fecharModalNotificacoes();
        });
    }
    carregarNotificacoes();
    setInterval(() => {
        fetch(`/notificacoes/pendentes?tipo=${TIPO_ESTOQUE}`)
            .then(res => res.json())
            .then(data => atualizarBadge(data.length))
            .catch(() => {});
    }, 60000);
});
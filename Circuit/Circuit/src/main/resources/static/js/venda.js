let elValorBruto, elValorLiquido, elPorcentagemHidden, elMotivoHidden, formVenda;
let listaItens = [];

document.addEventListener("DOMContentLoaded", function () {
    elValorBruto = document.getElementById('valorBruto');
    elValorLiquido = document.getElementById('valorLiquido');
    elPorcentagemHidden = document.getElementById('porcentagemDesconto');
    elMotivoHidden = document.getElementById('motivoDesconto');
    formVenda = document.getElementById('formVenda');

    configurarPesquisaLocal('searchTodas', 'tabelaTodas');
    configurarPesquisaLocal('searchPendentes', 'tabelaPendentes');
    configurarPesquisaLocal('searchConcluidas', 'tabelaConcluidas');

    document.querySelectorAll('.auto-close').forEach(alerta => {
        setTimeout(() => {
            alerta.style.opacity = '0';
            setTimeout(() => { alerta.remove(); }, 500);
        }, 3000);
    });
});

function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(c => {
        c.style.display = 'none';
        c.classList.remove('active');
    });
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    const tabId = 'tab' + tabName.charAt(0).toUpperCase() + tabName.slice(1);
    const target = document.getElementById(tabId);
    if (target) {
        target.style.display = 'block';
        target.classList.add('active');
    }
    if (event) event.currentTarget.classList.add('active');
}

function calcularTotalVenda() {
    if (!elValorBruto || !elValorLiquido || !elPorcentagemHidden) return;

    const valorBruto = parseFloat(elValorBruto.value) || 0;
    const porcentagem = parseFloat(elPorcentagemHidden.value) || 0;
    const valorDesconto = valorBruto * (porcentagem / 100);
    const valorLiquido = valorBruto - valorDesconto;

    elValorLiquido.value = Math.max(0, valorLiquido).toFixed(2);

    const infoDiv = document.getElementById('infoFinanceira');
    const resumoTxt = document.getElementById('resumoValores');

    if (infoDiv && resumoTxt) {
        if (valorBruto > 0) {
            resumoTxt.innerText = `Valor Bruto: R$ ${valorBruto.toFixed(2)} | Desconto: -R$ ${valorDesconto.toFixed(2)}`;
            infoDiv.style.display = 'block';
        } else {
            infoDiv.style.display = 'none';
        }
    }
}

function abrirModalNovaVenda() {
    formVenda.reset();
    formVenda.action = "/vendas/cadastrar";
    document.getElementById('vendaId').value = '';
    document.getElementById('valorBruto').value = '0';
    document.getElementById('porcentagemDesconto').value = '0';
    document.getElementById('motivoDesconto').value = '';
    document.getElementById('listaItensVenda').innerHTML = '';

    const statusInput = document.querySelector('input[name="status"]');
    if (statusInput) statusInput.value = 'PENDENTE';

    document.getElementById('modalTitle').innerText = 'Nova Venda';
    calcularTotalVenda();

    document.getElementById('modalNovaVenda').style.display = 'flex';
}

function abrirModalEdicao(btn) {
    formVenda.action = "/vendas/editar";
    document.getElementById('modalTitle').innerText = 'Editar Venda';

    const id = btn.getAttribute('data-id');
    const valorBruto = parseFloat(btn.getAttribute('data-valor-bruto')) || 0;
    const porcentagem = parseFloat(btn.getAttribute('data-porcentagem')) || 0;
    const motivo = btn.getAttribute('data-motivo') || '';
    const valorLiquido = parseFloat(btn.getAttribute('data-valor-liquido')) || 0;

    document.getElementById('vendaId').value = id;
    document.getElementById('valorBruto').value = valorBruto.toFixed(2);
    document.getElementById('porcentagemDesconto').value = porcentagem;
    document.getElementById('motivoDesconto').value = motivo;

    const statusInput = document.querySelector('input[name="status"]');
    if (statusInput) statusInput.value = 'PENDENTE';

    document.getElementById('valorBrutoInput').value = valorBruto.toFixed(2);
    document.getElementById('valorLiquidoInput').value = valorLiquido.toFixed(2);

    const infoDiv = document.getElementById('infoFinanceira');
    const resumoTxt = document.getElementById('resumoValores');
    if (infoDiv && resumoTxt) {
        resumoTxt.innerText = `Valor Total salvo: R$ ${valorLiquido.toFixed(2)}`;
        infoDiv.style.display = 'block';
    }

    calcularTotalVenda();
    document.getElementById('modalNovaVenda').style.display = 'flex';
}

function abrirModalDesconto() {
    document.getElementById('modalDesconto').style.display = 'flex';
    document.getElementById('inputPorcentagem').value = elPorcentagemHidden.value;
    document.getElementById('inputMotivo').value = elMotivoHidden.value;
}

function confirmarDesconto() {
    const p = document.getElementById('inputPorcentagem').value;
    const m = document.getElementById('inputMotivo').value;
    elPorcentagemHidden.value = p || 0;
    elMotivoHidden.value = m || '';
    calcularTotalVenda();
    fecharModalDesconto();
}

function abrirModalStatus(idVenda, statusAtual) {
    const inputId = document.getElementById('statusVendaId');
    const badge = document.getElementById('badgeStatusAtual');
    const modal = document.getElementById('modalStatus');

    if (inputId && badge && modal) {
        inputId.value = idVenda;
        badge.innerText = statusAtual.replace('_', ' ');
        badge.className = 'badge';
        if (statusAtual === 'PENDENTE') badge.classList.add('badge-active');
        else if (statusAtual === 'CONCLUIDA') badge.classList.add('badge-success');
        else if (statusAtual === 'CANCELADA') badge.classList.add('badge-inactive');
        else if (statusAtual === 'EM_PROCESSAMENTO') badge.classList.add('badge-info');
        else badge.classList.add('badge-warning');

        modal.style.display = 'flex';
    }
}

function selecionarStatus(novoStatus) {
    const inputNovo = document.getElementById('novoStatus');
    const form = document.getElementById('formStatus');
    if (inputNovo && form) {
        inputNovo.value = novoStatus;
        form.submit();
    }
}

function configurarPesquisaLocal(inputId, tableId) {
    const input = document.getElementById(inputId);
    const table = document.getElementById(tableId);
    if (!input || !table) return;
    input.addEventListener('keyup', () => {
        const termo = input.value.toLowerCase();
        table.querySelectorAll('tbody tr').forEach(linha => {
            linha.style.display = linha.innerText.toLowerCase().includes(termo) ? '' : 'none';
        });
    });
}

function closeModalVenda() { document.getElementById('modalNovaVenda').style.display = 'none'; }
function fecharModalDesconto() { document.getElementById('modalDesconto').style.display = 'none'; }
function fecharModalStatus() { document.getElementById('modalStatus').style.display = 'none'; }

function abrirModalAdicionarItens() {
    const container = document.getElementById('tbodyModalItensVenda');
    container.innerHTML = '<div style="text-align: center; padding: 20px;">Selecione uma categoria para carregar os produtos.</div>';
    document.getElementById('countSelecionadosVenda').innerText = '0';
    document.getElementById('subtotalModalVenda').innerText = 'R$ 0,00';
    document.getElementById('modalAdicionarItensVenda').style.display = 'flex';
}

function fecharModalAdicionarItens() { document.getElementById('modalAdicionarItensVenda').style.display = 'none'; }

function carregarProdutosVenda() {
    const container = document.getElementById('tbodyModalItensVenda');

    container.innerHTML = '<div style="text-align: center; padding: 20px;">Carregando produtos...</div>';

    fetch('/produtos/json')
        .then(res => res.json())
        .then(data => {
            container.innerHTML = '';
            if (!data || data.length === 0) {
                container.innerHTML = '<div style="text-align: center; padding: 20px;">Nenhum produto encontrado.</div>';
                return;
            }
            data.forEach(produto => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td class="text-center">
                        <input type="checkbox" class="check-item-venda" onchange="calcularResumoModalVenda()" 
                            data-id="${produto.id}" data-nome="${produto.nome}" data-preco="${produto.preco}">
                    </td>
                    <td>
                        <div class="font-600">${produto.nome}</div>
                        <div class="small-text text-muted">REF: ${produto.id}</div>
                    </td>
                    <td><input type="text" class="input-modal-erp preco-modal-venda" value="${produto.preco.toFixed(2)}" oninput="calcularResumoModalVenda()"></td>
                    <td><input type="number" class="input-modal-erp qtd-modal-venda" value="1" min="1" oninput="calcularResumoModalVenda()"></td>
                `;
                container.appendChild(tr);
            });
            calcularResumoModalVenda();
        })
        .catch(err => {
            container.innerHTML = '<div style="text-align: center; padding: 20px; color: #dc2626;">Erro de conexão.</div>';
        });
}

function calcularResumoModalVenda() {
    const checks = document.querySelectorAll('.check-item-venda:checked');
    let subtotal = 0;
    checks.forEach(check => {
        const tr = check.closest('tr');
        const preco = parseFloat(tr.querySelector('.preco-modal-venda').value.replace(',', '.')) || 0;
        const qtd = parseFloat(tr.querySelector('.qtd-modal-venda').value) || 0;
        subtotal += (preco * qtd);
    });
    const countEl = document.getElementById('countSelecionadosVenda');
    if (countEl) countEl.innerText = checks.length;
    const subtotalEl = document.getElementById('subtotalModalVenda');
    if (subtotalEl) subtotalEl.innerText = `R$ ${subtotal.toFixed(2).replace('.', ',')}`;
}

function adicionarItensSelecionados() {
    document.querySelectorAll('.check-item-venda:checked').forEach(check => {
        const tr = check.closest('tr');
        const id = check.getAttribute('data-id');
        const nome = check.getAttribute('data-nome');
        const preco = parseFloat(tr.querySelector('.preco-modal-venda').value.replace(',', '.')) || 0;
        const qtd = parseFloat(tr.querySelector('.qtd-modal-venda').value) || 0;
        inserirLinhaItensVenda(id, nome, qtd, preco, (preco * qtd));
    });
    fecharModalAdicionarItens();
}

function inserirLinhaItensVenda(id, nome, qtd, preco, total) {
    const container = document.getElementById('listaItensVenda');
    const itemDiv = document.createElement('div');
    itemDiv.className = 'item-venda-item';
    itemDiv.innerHTML = `
        <div class="item-venda-info">
            <input type="text" placeholder="Nome do Produto" value="${nome}" 
                   onchange="atualizarItemVenda(${id}, 'nome', this.value)"
                   style="width: 200px; margin-bottom: 5px;">
            <div style="display: flex; gap: 10px; align-items: center;">
                <input type="number" placeholder="Qtd" value="${qtd}" min="1"
                       onchange="atualizarItemVenda(${id}, 'quantidade', this.value)"
                       style="width: 60px;">
                <input type="number" placeholder="Preço (R$)" value="${preco}" step="0.01"
                       onchange="atualizarItemVenda(${id}, 'preco', this.value)"
                       style="width: 100px;">
                <button type="button" class="btn-remove" onclick="removerItemVenda(${id})">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <line x1="18" y1="6" x2="6" y2="18"></line>
                        <line x1="6" y1="6" x2="18" y2="18"></line>
                    </svg>
                </button>
            </div>
        </div>
    `;
    container.appendChild(itemDiv);
    calcularTotalVenda();
}
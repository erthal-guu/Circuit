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
    const container = document.getElementById('listaItensVenda');
    if (!container) return;
    const itens = container.querySelectorAll('.item-venda-item');
    let valorBruto = 0;
    
    itens.forEach(item => {
        const precoInput = item.querySelector('input[placeholder="Preço (R$)"]');
        const qtdInput = item.querySelector('input[placeholder="Qtd"]');
        if (precoInput && qtdInput) {
            const preco = parseFloat(precoInput.value) || 0;
            const qtd = parseFloat(qtdInput.value) || 0;
            valorBruto += (preco * qtd);
        }
    });

    elValorBruto.value = valorBruto.toFixed(2);
    const valorBrutoInput = document.getElementById('valorBrutoInput');
    if (valorBrutoInput) valorBrutoInput.value = valorBruto.toFixed(2);

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
    atualizarVisibilidadeItensVenda();
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
    document.getElementById('valorLiquido').value = valorLiquido.toFixed(2);

    const infoDiv = document.getElementById('infoFinanceira');
    const resumoTxt = document.getElementById('resumoValores');
    if (infoDiv && resumoTxt) {
        resumoTxt.innerText = `Valor Total salvo: R$ ${valorLiquido.toFixed(2)}`;
        infoDiv.style.display = 'block';
    }

    atualizarVisibilidadeItensVenda();
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
    container.innerHTML = '<div style="text-align: center; padding: 20px;">Carregando produtos...</div>';
    document.getElementById('countSelecionadosVenda').innerText = '0';
    document.getElementById('subtotalModalVenda').innerText = 'R$ 0,00';
    document.getElementById('modalAdicionarItensVenda').style.display = 'flex';
    carregarProdutosVenda();
}

function fecharModalAdicionarItens() { document.getElementById('modalAdicionarItensVenda').style.display = 'none'; }

function carregarProdutosVenda() {
    const container = document.getElementById('tbodyModalItensVenda');

    container.innerHTML = '<div style="text-align: center; padding: 20px;">Carregando produtos...</div>';

    fetch('/estoque/todos-disponiveis')
        .then(res => res.json())
        .then(data => {
            container.innerHTML = '';
            if (!data || data.length === 0) {
                container.innerHTML = '<div style="text-align: center; padding: 20px;">Nenhum produto encontrado.</div>';
                return;
            }
            data.forEach(produto => {
                const card = document.createElement('div');
                card.className = 'checkbox-item-card';
                card.innerHTML = `
                    <input type="checkbox" class="check-item-venda" onchange="calcularResumoModalVenda(); toggleCardSelection(this)" 
                        data-id="${produto.id}" data-nome="${produto.nome}" data-preco="${produto.preco}">
                    <div style="flex: 1; min-width: 0;">
                        <div style="font-weight: 600; font-size: 0.9rem; color: #334155;">${produto.nome}</div>
                        <div style="font-size: 0.8rem; color: #64748b;">REF: ${produto.id}</div>
                        <div style="display: flex; gap: 10px; margin-top: 5px;">
                            <input type="text" class="input-modal-erp preco-modal-venda" value="${produto.preco.toFixed(2)}" oninput="calcularResumoModalVenda()" 
                                style="width: 80px; padding: 4px 8px; font-size: 0.85rem;" placeholder="Preço">
                            <input type="number" class="input-modal-erp qtd-modal-venda" value="1" min="1" oninput="calcularResumoModalVenda()" 
                                style="width: 60px; padding: 4px 8px; font-size: 0.85rem;" placeholder="Qtd">
                        </div>
                    </div>
                `;
                card.addEventListener('click', (e) => {
                    if (e.target.tagName !== 'INPUT') {
                        const checkbox = card.querySelector('.check-item-venda');
                        checkbox.checked = !checkbox.checked;
                        calcularResumoModalVenda();
                        toggleCardSelection(checkbox);
                        calcularTotalVenda();
                    }
                });
                container.appendChild(card);
            });
            calcularResumoModalVenda();
        })
        .catch(err => {
            container.innerHTML = '<div style="text-align: center; padding: 20px; color: #dc2626;">Erro de conexão.</div>';
        });
}

function toggleCardSelection(checkbox) {
    const card = checkbox.closest('.checkbox-item-card');
    if (checkbox.checked) {
        card.classList.add('selected');
    } else {
        card.classList.remove('selected');
    }
}

function calcularResumoModalVenda() {
    const checks = document.querySelectorAll('.check-item-venda:checked');
    let subtotal = 0;
    checks.forEach(check => {
        const card = check.closest('.checkbox-item-card');
        const preco = parseFloat(card.querySelector('.preco-modal-venda').value.replace(',', '.')) || 0;
        const qtd = parseFloat(card.querySelector('.qtd-modal-venda').value) || 0;
        subtotal += (preco * qtd);
    });
    const countEl = document.getElementById('countSelecionadosVenda');
    if (countEl) countEl.innerText = checks.length;
    const subtotalEl = document.getElementById('subtotalModalVenda');
    if (subtotalEl) subtotalEl.innerText = `R$ ${subtotal.toFixed(2).replace('.', ',')}`;
}

function adicionarItensSelecionados() {
    document.querySelectorAll('.check-item-venda:checked').forEach(check => {
        const card = check.closest('.checkbox-item-card');
        const id = check.getAttribute('data-id');
        const nome = check.getAttribute('data-nome');
        const preco = parseFloat(card.querySelector('.preco-modal-venda').value.replace(',', '.')) || 0;
        const qtd = parseFloat(card.querySelector('.qtd-modal-venda').value) || 0;
        inserirLinhaItensVenda(id, nome, qtd, preco, (preco * qtd));
    });
    calcularTotalVenda();
    fecharModalAdicionarItens();
}

function inserirLinhaItensVenda(id, nome, qtd, preco, total) {
    const container = document.getElementById('listaItensVenda');
    const itemDiv = document.createElement('div');
    itemDiv.className = 'item-venda-item';
    itemDiv.dataset.id = id;
    itemDiv.innerHTML = `
        <div class="item-venda-info">
            <input type="text" placeholder="Nome do Produto" value="${nome}" 
                   onchange="atualizarItemVenda(${id}, 'nome', this.value)"
                   style="width: 200px; margin-bottom: 5px;">
            <div style="display: flex; gap: 10px; align-items: center;">
                <input type="number" placeholder="Qtd" value="${qtd}" min="1"
                       oninput="atualizarItemVenda(${id}, 'quantidade', this.value)"
                       style="width: 60px;">
                <input type="number" placeholder="Preço (R$)" value="${preco}" step="0.01"
                       oninput="atualizarItemVenda(${id}, 'preco', this.value)"
                       style="width: 100px;">
                <button type="button" class="close-btn" onclick="removerItemVenda(${id})">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <line x1="18" y1="6" x2="6" y2="18"></line>
                        <line x1="6" y1="6" x2="18" y2="18"></line>
                    </svg>
                </button>
            </div>
        </div>
    `;
    container.appendChild(itemDiv);
    atualizarVisibilidadeItensVenda();
    calcularTotalVenda();
}

function atualizarVisibilidadeItensVenda() {
    const container = document.getElementById('listaItensVenda');
    const itens = container.querySelectorAll('.item-venda-item');
    
    if (itens.length > 0) {
        container.style.display = 'block';
    } else {
        container.style.display = 'none';
    }
}

function removerItemVenda(id) {
    const container = document.getElementById('listaItensVenda');
    const itemDiv = container.querySelector(`.item-venda-item[data-id="${id}"]`);
    if (itemDiv) {
        itemDiv.remove();
        atualizarVisibilidadeItensVenda();
        calcularTotalVenda();
    }
}

function atualizarItemVenda(id, campo, valor) {
    const itemDiv = document.querySelector(`.item-venda-item[data-id="${id}"]`);
    if (!itemDiv) return;
    
    if (campo === 'nome') {
        const inputNome = itemDiv.querySelector('input[placeholder="Nome do Produto"]');
        if (inputNome) inputNome.value = valor;
    } else if (campo === 'quantidade') {
        const inputQtd = itemDiv.querySelector('input[placeholder="Qtd"]');
        if (inputQtd) inputQtd.value = valor;
    } else if (campo === 'preco') {
        const inputPreco = itemDiv.querySelector('input[placeholder="Preço (R$)"]');
        if (inputPreco) inputPreco.value = valor;
    }
    
    calcularTotalVenda();
}
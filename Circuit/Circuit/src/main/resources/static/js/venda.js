    let elValorBruto, elValorTotal, elPorcentagemHidden, elMotivoHidden, formVenda;
let listaItens = [];

document.addEventListener("DOMContentLoaded", function () {
    elValorBruto = document.getElementById('valorBruto');
    elValorTotal = document.getElementById('valorTotal');
    elPorcentagemHidden = document.getElementById('porcentagemDesconto');
    elMotivoHidden = document.getElementById('motivoDesconto');
    formVenda = document.getElementById('formVenda');

    configurarPesquisaLocal('searchTodas', 'tabelaTodas');
    configurarPesquisaLocal('searchPendentes', 'tabelaPendentes');
    configurarPesquisaLocal('searchConcluidas', 'tabelaConcluidas');
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
    if (!elValorBruto || !elValorTotal || !elPorcentagemHidden) return;
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
    const valorTotal = valorBruto - valorDesconto;

    elValorTotal.value = Math.max(0, valorTotal).toFixed(2);

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

function gerarCodigoVenda() {
    const random = Math.floor(Math.random() * 65535).toString(16).toUpperCase();
    return `VD-${random.padStart(4, '0')}`;
}

function abrirModalNovaVenda() {
    formVenda.reset();
    formVenda.action = "/vendas/cadastrar";
    document.getElementById('vendaId').value = '';
    document.getElementById('valorBruto').value = '0';
    document.getElementById('porcentagemDesconto').value = '';
    document.getElementById('motivoDesconto').value = '';
    document.getElementById('listaItensVenda').innerHTML = '';

    const statusInput = document.querySelector('input[name="status"]');
    if (statusInput) statusInput.value = 'PENDENTE';

    const dataVendaInput = document.getElementById('dataVenda');
    if (dataVendaInput) {
        const hoje = new Date();
        const dataFormatada = hoje.toISOString().split('T')[0];
        dataVendaInput.value = dataFormatada;
    }
    const codigoInput = document.getElementById('codigo');
    if (codigoInput) {
        codigoInput.value = gerarCodigoVenda();
    }

    document.getElementById('modalTitle').innerText = 'Nova Venda';
    atualizarVisibilidadeItensVenda();
    calcularTotalVenda();

    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainer');
    const parcelasContainer = document.getElementById('parcelasContainer');
    if (condicaoPagamentoContainer) {
        condicaoPagamentoContainer.style.display = 'none';
        condicaoPagamentoContainer.style.opacity = '0';
        condicaoPagamentoContainer.style.transform = 'translateY(-20px)';
        const condicaoAVistaRadio = document.getElementById('condicaoAVista');
        if (condicaoAVistaRadio) {
            condicaoAVistaRadio.checked = true;
        }
    }
    if (parcelasContainer) parcelasContainer.style.display = 'none';

    document.getElementById('modalNovaVenda').style.display = 'flex';
}

    async function abrirModalEdicao(btn) {
        formVenda.action = "/vendas/atualizar";

        const id = btn.getAttribute('data-id');
        const clienteId = btn.getAttribute('data-cliente');
        const funcionarioId = btn.getAttribute('data-funcionario');
        const valorBrutoStr = btn.getAttribute('data-valor-bruto') || '0';
        const valorTotalStr = btn.getAttribute('data-valor-total') || '0';
        const porcentagemStr = btn.getAttribute('data-porcentagem') || '0';
        const motivo = btn.getAttribute('data-motivo') || '';
        const formaPagamento = btn.getAttribute('data-forma-pagamento') || 'AVISTA';
        const condicaoPagamento = btn.getAttribute('data-condicao-pagamento') || 'AVISTA';
        const numeroParcelas = parseInt(btn.getAttribute('data-numero-parcelas')) || 1;
        const status = btn.getAttribute('data-status') || 'PENDENTE';
        const dataVenda = btn.getAttribute('data-data-venda');
        const codigo = btn.getAttribute('data-codigo');
        const valorBruto = parseFloat(valorBrutoStr.replace(',', '.')) || 0;
        const valorTotal = parseFloat(valorTotalStr.replace(',', '.')) || 0;
        const porcentagem = parseFloat(porcentagemStr.replace(',', '.')) || 0;

        document.getElementById('vendaId').value = id;
        document.getElementById('valorBruto').value = valorBruto.toFixed(2);
        document.getElementById('porcentagemDesconto').value = porcentagem;
        document.getElementById('motivoDesconto').value = motivo;
        document.getElementById('vendaCliente').value = clienteId;
        document.getElementById('vendaFuncionario').value = funcionarioId;

        const statusInput = document.querySelector('input[name="status"]');
        if (statusInput) statusInput.value = status;

        document.getElementById('valorBrutoInput').value = valorBruto.toFixed(2);
        document.getElementById('valorTotal').value = valorTotal.toFixed(2);

        const dataVendaInput = document.getElementById('dataVenda');
        if (dataVendaInput) dataVendaInput.value = dataVenda;

        const codigoInput = document.getElementById('codigo');
        if (codigoInput) codigoInput.value = codigo;

        const infoDiv = document.getElementById('infoFinanceira');
        const resumoTxt = document.getElementById('resumoValores');
        if (infoDiv && resumoTxt) {
            resumoTxt.innerText = `Valor Total salvo: R$ ${valorTotal.toFixed(2)}`;
            infoDiv.style.display = 'block';
        }

        const formaPagamentoSelect = document.getElementById('formaPagamento');
        if (formaPagamentoSelect) {
            formaPagamentoSelect.value = formaPagamento;
            handleFormaPagamentoChange();
        }

        const condicaoAVistaRadio = document.getElementById('condicaoAVista');
        const condicaoParceladoRadio = document.getElementById('condicaoParcelado');
        const parcelasContainer = document.getElementById('parcelasContainer');
        const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainer');

        if (condicaoPagamentoContainer) {
            condicaoPagamentoContainer.style.display = 'block';
            condicaoPagamentoContainer.style.opacity = '1';
            condicaoPagamentoContainer.style.transform = 'translateY(0)';
        }

        if (condicaoPagamento === 'AVISTA') {
            if (condicaoAVistaRadio) condicaoAVistaRadio.checked = true;
            if (condicaoParceladoRadio) condicaoParceladoRadio.checked = false;
            if (parcelasContainer) parcelasContainer.style.display = 'none';
        } else {
            if (condicaoAVistaRadio) condicaoAVistaRadio.checked = false;
            if (condicaoParceladoRadio) condicaoParceladoRadio.checked = true;
            if (parcelasContainer) parcelasContainer.style.display = 'block';
        }

        if (parcelasContainer) {
            const parcelasSelect = parcelasContainer.querySelector('select[name="numeroParcelas"]');
            if (parcelasSelect) parcelasSelect.value = numeroParcelas;
        }

        const listaItens = document.getElementById('listaItensVenda');
        if (listaItens) listaItens.innerHTML = '';

        await carregarItensVendaExistente(id);
        calcularTotalVenda();
        document.getElementById('modalNovaVenda').style.display = 'flex';
    }

function abrirModalDetalhes(btn) {
    const id = btn.getAttribute('data-id') || '';
    const codigo = btn.getAttribute('data-codigo') || '';
    const cliente = btn.getAttribute('data-cliente') || '';
    const responsavel = btn.getAttribute('data-responsavel') || '';
    const data = btn.getAttribute('data-data') || '';
    const status = btn.getAttribute('data-status') || '';
    const formaPagamento = btn.getAttribute('data-forma-pagamento') || '';
    const condicaoPagamento = btn.getAttribute('data-condicao-pagamento') || '';
    const numeroParcelas = btn.getAttribute('data-numero-parcelas') || '';
    const valorBruto = btn.getAttribute('data-valor-bruto') || 'R$ 0,00';
    const desconto = btn.getAttribute('data-desconto') || 'R$ 0,00';
    const valorTotal = btn.getAttribute('data-valor-total') || 'R$ 0,00';

    const detId = document.getElementById('det-id');
    if (detId) detId.innerText = id;
    const detCodigo = document.getElementById('det-codigo-detalhes');
    if (detCodigo) detCodigo.innerText = codigo;
    const detCliente = document.getElementById('det-cliente');
    if (detCliente) detCliente.innerText = cliente;
    const detResponsavel = document.getElementById('det-responsavel');
    if (detResponsavel) detResponsavel.innerText = responsavel;
    const detData = document.getElementById('det-data');
    if (detData) detData.innerText = data;
    const detFormaPagamento = document.getElementById('det-forma-pagamento');
    if (detFormaPagamento) detFormaPagamento.innerText = formaPagamento;
    const detCondicaoPagamento = document.getElementById('det-condicao-pagamento');
    if (detCondicaoPagamento) detCondicaoPagamento.innerText = condicaoPagamento;
    const detNumeroParcelas = document.getElementById('det-numero-parcelas');
    if (detNumeroParcelas) detNumeroParcelas.innerText = numeroParcelas;
    const detValorBruto = document.getElementById('det-valor-bruto');
    if (detValorBruto) detValorBruto.innerText = valorBruto;
    const detDesconto = document.getElementById('det-desconto');
    if (detDesconto) detDesconto.innerText = desconto;
    const detValorTotal = document.getElementById('det-valor-total-venda');
    if (detValorTotal) detValorTotal.innerText = "$" + valorTotal;
    detValorTotal.style.color = 'green';

    const badgeDiv = document.getElementById('det-status-badge');
    if (badgeDiv) {
        let cl = status === 'PENDENTE' ? 'badge-analysis' :
               (status === 'CONCLUIDA' ? 'badge-success' :
               (status === 'CANCELADA' ? 'badge-inactive' : 'badge-inactive'));
        badgeDiv.innerHTML = `<span class="badge ${cl}">${status}</span>`;
    }

    const tbody = document.getElementById('det-tbody-itens');
    if (tbody) tbody.innerHTML = '<tr><td colspan="4" class="text-center">Processando itens...</td></tr>';

    fetch(`/vendas/${id}/itens-json-venda`)
        .then(res => {
            if (!res.ok) throw new Error("Erro na API");
            return res.json();
        })
        .then(itens => {
            if (!tbody) return;
            tbody.innerHTML = '';
            if (!Array.isArray(itens) || itens.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum item vinculado.</td></tr>';
                return;
            }
            
            itens.forEach(item => {
                const qtd = Number(item.quantidade || 0);
                const preco = Number(item.precoUnitario || 0);
                const subtotal = qtd * preco;

                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td class="font-600">${item.nome || '-'}</td>
                    <td class="text-center">${qtd}</td>
                    <td>R$ ${preco.toFixed(2)}</td>
                    <td class="font-600">R$ ${subtotal.toFixed(2)}</td>
                `;
                tbody.appendChild(tr);
            });
        })
        .catch(err => {
            console.error('Erro ao buscar itens:', err);
            if (tbody) tbody.innerHTML = '<tr><td colspan="4" class="text-center">Erro ao carregar itens.</td></tr>';
        });

    const modal = document.getElementById('modalDetalhesVenda');
    if (modal) modal.style.display = 'flex';
}

function fecharModalDetalhesVenda() {
    const modal = document.getElementById('modalDetalhesVenda');
    if (modal) modal.style.display = 'none';
}

function parseItemVendaData(item) {
    const qtd = Number(item.quantidade || item.qtd || item.quantidadeItens || 1);
    const preco = Number(item.precoUnitario || item.valorUnitario || item.preco || 0);

    let nome = item.descricao || item.nomeItem || item.nome || 'Item Desconhecido';
    let idItem = item.itemId || item.id || '';

    if (item.produto && item.produto.nome) {
        nome = item.produto.nome;
        idItem = item.produto.id;
    } else {
        idItem = item.id || '';
    }

    return { idItem, nome, qtd, preco };
}

    async function abrirEdicaoVenda(btn) {
        document.getElementById('main-title').innerText = 'Editar Venda';
        
        const btnSalvar = document.getElementById('btn-salvar-global');
        if (btnSalvar) btnSalvar.innerHTML = btnSalvar.innerHTML.replace('Salvar Venda', 'Atualizar Venda');
        
        switchTab('form');
        
        const form = document.getElementById('formVenda');
        if (form) form.action = '/vendas/atualizar';
        
        const id = btn.getAttribute('data-id');
        const clienteId = btn.getAttribute('data-cliente');
        const funcionarioId = btn.getAttribute('data-funcionario');
        const codigo = btn.getAttribute('data-codigo');
        const dataVenda = btn.getAttribute('data-data-venda');
        const valorBruto = btn.getAttribute('data-valor-bruto');
        const valorTotal = btn.getAttribute('data-valor-total');
        const porcentagem = btn.getAttribute('data-porcentagem');
        const motivo = btn.getAttribute('data-motivo');
        const formaPagamento = btn.getAttribute('data-forma-pagamento');
        const condicaoPagamento = btn.getAttribute('data-condicao-pagamento');
        const numeroParcelas = btn.getAttribute('data-numero-parcelas');
        const status = btn.getAttribute('data-status');
        
        // Preencher campos do formulário
        const inputId = document.getElementById('vendaId');
        if (inputId) inputId.value = id;
        
        const selectCliente = document.getElementById('clienteId');
        if (selectCliente) selectCliente.value = clienteId;
        
        const selectFuncionario = document.getElementById('funcionarioId');
        if (selectFuncionario) selectFuncionario.value = funcionarioId;
        
        const inputCodigo = document.getElementById('codigo');
        if (inputCodigo) inputCodigo.value = codigo || '';
        
        const inputDataVenda = document.getElementById('dataVenda');
        if (inputDataVenda) {
            let rawDate = dataVenda;
            if (rawDate && rawDate.includes('/')) {
                const parts = rawDate.split('/');
                if (parts.length === 3) {
                    rawDate = `${parts[2]}-${parts[1]}-${parts[0]}`;
                }
            }
            inputDataVenda.value = rawDate;
        }
        
        const inputValorBruto = document.getElementById('valorBruto');
        if (inputValorBruto) inputValorBruto.value = valorBruto || '0';
        
        const inputValorTotal = document.getElementById('valorTotal');
        if (inputValorTotal) inputValorTotal.value = valorTotal || '0';
        
        const inputPorcentagem = document.getElementById('porcentagemDesconto');
        if (inputPorcentagem) inputPorcentagem.value = porcentagem || '0';
        
        const inputMotivo = document.getElementById('motivoDesconto');
        if (inputMotivo) inputMotivo.value = motivo || '';
        
        const statusInput = document.querySelector('input[name="status"]');
        if (statusInput) statusInput.value = status || 'PENDENTE';
        
        const selectFormaPagamento = document.getElementById('formaPagamento');
        if (selectFormaPagamento) selectFormaPagamento.value = formaPagamento || '';
        
        const selectCondicaoPagamento = document.getElementById('condicaoPagamento');
        if (selectCondicaoPagamento) selectCondicaoPagamento.value = condicaoPagamento || '';
        
        const inputNumeroParcelas = document.getElementById('numeroParcelas');
        if (inputNumeroParcelas) inputNumeroParcelas.value = numeroParcelas || '';
        
        // Carregar itens
        const tbody = document.getElementById('listaItensVenda');
        if (tbody) tbody.innerHTML = '<tr><td colspan="4" class="text-center">Carregando itens...</td></tr>';
        
        fetch(`/vendas/${id}/itens-json-venda`)
            .then(res => {
                if (!res.ok) throw new Error("Erro na API");
                return res.json();
            })
            .then(itens => {
                if (!tbody) return;
                tbody.innerHTML = '';
                if (!Array.isArray(itens) || itens.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum item vinculado.</td></tr>';
                    return;
                }
                itens.forEach(item => {
                    const qtd = Number(item.quantidade || 0);
                    const preco = Number(item.precoUnitario || 0);
                    const subtotal = qtd * preco;
                    
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td class="font-600">${item.nome || '-'}</td>
                        <td class="text-center">${qtd}</td>
                        <td>R$ ${preco.toFixed(2)}</td>
                        <td class="font-600">R$ ${subtotal.toFixed(2)}</td>
                    `;
                    tbody.appendChild(tr);
                });
                calcularTotalVenda();
            })
            .catch(err => {
                console.error('Erro ao buscar itens:', err);
                if (tbody) tbody.innerHTML = '<tr><td colspan="4" class="text-center">Erro ao carregar itens.</td></tr>';
            });
    }

    async function carregarItensVendaExistente(id) {
        const container = document.getElementById('listaItensVenda');
        if (!container) return;

        container.innerHTML = '';

        try {
            const response = await fetch(`/vendas/${id}/itens-json-venda`);
            if (!response.ok) throw new Error("Erro na API");
            const itens = await response.json();


            container.innerHTML = '';

            if (Array.isArray(itens) && itens.length > 0) {
                itens.forEach(item => {
                    const parsed = parseItemVendaData(item);
                    inserirLinhaItensVenda(parsed.idItem, parsed.nome, parsed.qtd, parsed.preco, (parsed.qtd * parsed.preco)); // ← função correta
                });
            }
        } catch (err) {
            container.innerHTML = '';
        }
        calcularTotalVenda();
    }

function inserirLinhaTabelaVenda(id, nome, qtd, preco, total) {
    const container = document.getElementById('listaItensVenda');
    if (!container) return;

    const tr = document.createElement('tr');
    tr.className = 'item-venda-item';
    tr.innerHTML = `
        <td>
            ${nome}
            <input type="hidden" name="itensId" value="${id}">
        </td>
        <td>
            <input type="number" name="quantidadeItens" class="form-control" value="${qtd}" min="1" oninput="calcularTotalVenda()">
        </td>
        <td>
            <input type="text" name="precoItens" class="form-control" value="${preco.toFixed(2).replace('.', ',')}" oninput="calcularTotalVenda()">
        </td>
        <td class="total-linha">${total.toFixed(2).replace('.', ',')}</td>
        <td class="text-center">
            <button type="button" class="btn-icon remove" onclick="this.closest('tr').remove(); calcularTotalVenda();">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
            </button>
        </td>
    `;
    container.appendChild(tr);
    calcularTotalVenda();
}

function abrirModalDesconto() {
    document.getElementById('modalDesconto').style.display = 'flex';
    const valorPorcentagem = elPorcentagemHidden.value;
    if (valorPorcentagem && valorPorcentagem !== '0') {
        document.getElementById('inputPorcentagem').value = valorPorcentagem;
    } else {
        document.getElementById('inputPorcentagem').value = '';
    }
    document.getElementById('inputMotivo').value = elMotivoHidden.value;
}

function confirmarDesconto() {
    const p = document.getElementById('inputPorcentagem').value;
    const m = document.getElementById('inputMotivo').value;
    const porcentagemNumerica = p.replace('%', '') || 0;
    elPorcentagemHidden.value = porcentagemNumerica;
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
    atualizarCamposItens();
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
    atualizarCamposItens();
}

function atualizarCamposItens() {
    const container = document.getElementById('listaItensVenda');
    const itens = container.querySelectorAll('.item-venda-item');
    const itensIdInput = document.getElementById('itensId');
    const quantidadeItensInput = document.getElementById('quantidadeItens');

    if (!itensIdInput || !quantidadeItensInput) return;

    const itensId = [];
    const quantidadeItens = [];

    itens.forEach(item => {
        const id = item.dataset.id;
        const qtdInput = item.querySelector('input[placeholder="Qtd"]');
        if (id && qtdInput) {
            itensId.push(id);
            quantidadeItens.push(qtdInput.value);
        }
    });

    itensIdInput.value = itensId.join(',');
    quantidadeItensInput.value = quantidadeItens.join(',');
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
        atualizarCamposItens();
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

function handleFormaPagamentoChange() {
    const formaPagamento = document.getElementById('formaPagamento');
    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainer');
    const parcelasContainer = document.getElementById('parcelasContainer');

    if (!formaPagamento || !condicaoPagamentoContainer || !parcelasContainer) return;

    const selectedValue = formaPagamento.value;

    if (selectedValue === 'CARTAO_CREDITO') {
        condicaoPagamentoContainer.style.display = 'block';
        condicaoPagamentoContainer.style.opacity = '1';
        condicaoPagamentoContainer.style.transform = 'translateY(0)';
        setTimeout(() => {
            condicaoPagamentoContainer.classList.add('modal-animate-show');
        }, 10);
        handleCondicaoPagamentoChange();
    } else {
        condicaoPagamentoContainer.style.display = 'none';
        condicaoPagamentoContainer.style.opacity = '0';
        condicaoPagamentoContainer.style.transform = 'translateY(-20px)';
        condicaoPagamentoContainer.classList.remove('modal-animate-show');
        condicaoPagamentoContainer.classList.add('modal-animate');
    }
}

function handleCondicaoPagamentoChange() {
    const condicaoPagamento = document.querySelector('input[name="condicaoPagamento"]:checked');
    const parcelasContainer = document.getElementById('parcelasContainer');
    const labelAVista = document.getElementById('labelCondicaoAVista');
    const labelParcelado = document.getElementById('labelCondicaoParcelado');

    if (!condicaoPagamento || !parcelasContainer || !labelAVista || !labelParcelado) return;

    if (condicaoPagamento.value === 'PARCELADO') {
        parcelasContainer.style.display = 'block';
    } else {
        parcelasContainer.style.display = 'none';
    }
}

function removerPorcentagem(input) {
    input.value = input.value.replace('%', '');
}

function adicionarPorcentagem(input) {
    let valor = input.value.replace(/[^0-9]/g, '');

    if (valor === '') {
        input.value = '';
        return;
    }

    if (parseInt(valor) > 100) {
        valor = '100';
    }

    const cursorPos = input.selectionStart;
    input.value = valor + '%';
    input.setSelectionRange(cursorPos, cursorPos);
}

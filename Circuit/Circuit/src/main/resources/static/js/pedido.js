function gerarCodigoHex() {
    const input = document.getElementById("numeroPedido");
    const random = Math.floor(Math.random() * 65535).toString(16).toUpperCase();
    input.style.transition = "background-color 0.2s";
    input.style.backgroundColor = "white";
    input.value = `PO-${random.padStart(4, '0')}`;
    setTimeout(() => {
        input.style.backgroundColor = "";
    }, 300);
}

function atualizarDadosFornecedor(select) {
    if (!select || select.selectedIndex < 0) return;
    const option = select.options[select.selectedIndex];
    if (!option) return;
    const displayNome = document.getElementById("fornecedorNome");
    const inputCnpj = document.getElementById("inputCnpjFornecedor");
    const nome = option.getAttribute("data-nome");
    const cnpj = option.getAttribute("data-cnpj");
    if (displayNome) {
        displayNome.innerText = nome ? `Fornecedor: ${nome}` : "Fornecedor:";
    }
    if (inputCnpj) {
        if (cnpj) {
            inputCnpj.value = formatarCNPJ(cnpj);
            inputCnpj.style.borderColor = "#10b981";
        } else {
            inputCnpj.value = "";
            inputCnpj.style.borderColor = "#e2e8f0";
        }
    }
}

function formatarCNPJ(v) {
    if(!v) return "";
    v=v.replace(/\D/g,"");
    if(v.length !== 14) return v;
    return v.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, "$1.$2.$3/$4-$5");
}

function switchTab(tab) {
    const viewLista = document.getElementById('view-lista');
    const viewForm = document.getElementById('view-form');
    const btnLista = document.getElementById('btn-tab-lista');
    const btnForm = document.getElementById('btn-tab-form');
    const btnSalvar = document.getElementById('btn-salvar-global');
    const mainTitle = document.getElementById('main-title');

    if (tab === 'lista') {
        viewLista.style.display = 'block';
        viewForm.style.display = 'none';
        btnLista.classList.add('active');
        btnForm.classList.remove('active');
        if (btnSalvar) btnSalvar.style.display = 'none';
        mainTitle.innerText = 'Pedidos';

        const form = document.getElementById('formPedido');
        if (form) form.reset();
        const inputId = document.getElementById('pedidoIdEdit');
        if (inputId) inputId.remove();
        const tbodyItens = document.getElementById('tbodyItens');
        if (tbodyItens) tbodyItens.innerHTML = '';
        const inputCnpj = document.getElementById('inputCnpjFornecedor');
        if (inputCnpj) inputCnpj.value = '';
        const spanTotal = document.getElementById('spanTotalFinal');
        if (spanTotal) spanTotal.value = '0.00';

        if (btnSalvar) btnSalvar.innerHTML = btnSalvar.innerHTML.replace('Atualizar Pedido', 'Salvar Pedido');

    } else {
        viewLista.style.display = 'none';
        viewForm.style.display = 'block';
        btnLista.classList.remove('active');
        btnForm.classList.add('active');
        if (btnSalvar) btnSalvar.style.display = 'flex';

        if(mainTitle.innerText !== 'Editar Pedido') {
            mainTitle.innerText = 'Novo Pedido';
            if (btnSalvar) btnSalvar.innerHTML = btnSalvar.innerHTML.replace('Atualizar Pedido', 'Salvar Pedido');
            const numPedido = document.getElementById('numeroPedido');
            if(numPedido && !numPedido.value) {
                gerarCodigoHex();
            }
        }
    }
}

function filtrarFornecedores() {
    const radioSelecionado = document.querySelector('input[name="tipoPedido"]:checked');
    if (!radioSelecionado) return;
    const tipoDesejado = radioSelecionado.value.toUpperCase();
    const select = document.getElementById('selectFornecedor');
    if (!select) return;
    const options = select.options;

    for (let i = 0; i < options.length; i++) {
        const opt = options[i];
        if (opt.value === "") continue;
        const dadoBruto = opt.getAttribute('data-tipo');
        const tipoFornecedor = dadoBruto ? dadoBruto.toUpperCase() : "";
        const deveMostrar = (tipoFornecedor.includes(tipoDesejado) || tipoFornecedor === 'AMBOS');
        opt.hidden = !deveMostrar;
        opt.disabled = !deveMostrar;
    }
}

function parseItemData(item) {
    const qtd = Number(item.quantidade || item.qtd || item.quantidadeItens || 1);
    const preco = Number(item.precoUnitario || item.valorUnitario || item.precoCompra || item.preco || 0);

    let nome = item.descricao || item.nomeItem || item.nome || 'Item Desconhecido';
    let idItem = item.itemId || item.id || '';

    if (item.produto && item.produto.nome) {
        nome = item.produto.nome;
        idItem = item.produto.id;
    } else if (item.peca && item.peca.nome) {
        nome = item.peca.nome;
        idItem = item.peca.id;
    }

    return { idItem, nome, qtd, preco };
}

function abrirModalAdicionarItens() {
    const select = document.getElementById('selectFornecedor');
    const fornecedorId = select.value;
    if (!fornecedorId) {
        alert("Selecione um fornecedor primeiro!");
        return;
    }
    const option = select.options[select.selectedIndex];
    const nome = option.getAttribute('data-nome');
    document.getElementById('fornecedorNome').innerText = "Fornecedor: " + nome;
    carregarItensDoFornecedor(fornecedorId);
    document.getElementById('modalItemPedido').style.display = 'block';
}

function fecharModal() {
    document.getElementById('modalItemPedido').style.display = 'none';
}

function carregarItensDoFornecedor(idFornecedor) {
    const container = document.getElementById('tbodyModalItens');
    const radioTipo = document.querySelector('input[name="tipoPedido"]:checked');
    const tipoNorm = radioTipo ? radioTipo.value : '';
    const url = `/fornecedores/json/${idFornecedor}/itens-vinculados?tipo=${tipoNorm}`;

    container.innerHTML = '<tr><td colspan="4" class="text-center">Carregando itens...</td></tr>';

    fetch(url)
        .then(res => res.json())
        .then(data => {
            container.innerHTML = '';
            if (!data || data.length === 0) {
                container.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum item encontrado.</td></tr>';
                return;
            }
            data.forEach(item => {
                const parsed = parseItemData(item);
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td class="text-center">
                        <input type="checkbox" class="check-item" onchange="calcularResumoModal()" 
                            data-id="${parsed.idItem}" data-nome="${parsed.nome}">
                    </td>
                    <td>
                        <div class="font-600">${parsed.nome}</div>
                        <div class="small-text text-muted">REF: ${parsed.idItem}</div>
                    </td>
                    <td><input type="text" class="input-modal-erp preco-modal" value="${parsed.preco.toFixed(2)}" oninput="calcularResumoModal()"></td>
                    <td><input type="number" class="input-modal-erp qtd-modal" value="1" min="1" oninput="calcularResumoModal()"></td>
                `;
                container.appendChild(tr);
            });
            calcularResumoModal();
        })
        .catch(err => {
            container.innerHTML = '<tr><td colspan="4" class="text-center text-danger">Erro de conexão.</td></tr>';
        });
}

function calcularResumoModal() {
    const checks = document.querySelectorAll('.check-item:checked');
    let subtotal = 0;
    checks.forEach(check => {
        const tr = check.closest('tr');
        const preco = parseFloat(tr.querySelector('.preco-modal').value.replace(',', '.')) || 0;
        const qtd = parseFloat(tr.querySelector('.qtd-modal').value) || 0;
        subtotal += (preco * qtd);
    });
    const countEl = document.getElementById('countSelecionados');
    if (countEl) countEl.innerText = checks.length;
    const subtotalEl = document.getElementById('subtotalModal');
    if (subtotalEl) subtotalEl.innerText = `R$ ${subtotal.toFixed(2).replace('.', ',')}`;
}

function adicionarSelecionados() {
    document.querySelectorAll('.check-item:checked').forEach(check => {
        const tr = check.closest('tr');
        const id = check.getAttribute('data-id');
        const nome = check.getAttribute('data-nome');
        const preco = parseFloat(tr.querySelector('.preco-modal').value.replace(',', '.')) || 0;
        const qtd = parseFloat(tr.querySelector('.qtd-modal').value) || 0;
        inserirLinhaTabelaPrincipal(id, nome, qtd, preco, (preco * qtd));
    });
    fecharModal();
}

function inserirLinhaTabelaPrincipal(id, nome, qtd, preco, total) {
    const tbody = document.getElementById('tbodyItens');
    if (!tbody) return;
    const tr = document.createElement('tr');
    tr.innerHTML = `
        <td>${nome} <input type="hidden" name="itensId" value="${id}"></td>
        <td><input type="number" name="quantidadeItens" class="form-control" value="${qtd}" oninput="recalcularTotalPedido()"></td>
        <td><input type="text" name="precoItens" class="form-control" value="${preco.toFixed(2)}" oninput="recalcularTotalPedido()"></td>
        <td class="total-linha">${total.toFixed(2)}</td>
        <td class="text-center">
            <button type="button" class="btn-icon remove" onclick="this.closest('tr').remove(); recalcularTotalPedido();">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="3 6 5 6 21 6"></polyline><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path><line x1="10" y1="11" x2="10" y2="17"></line><line x1="14" y1="11" x2="14" y2="17"></line></svg>
            </button>
        </td>
    `;
    tbody.appendChild(tr);
    recalcularTotalPedido();
}

function recalcularTotalPedido() {
    let subtotal = 0;
    document.querySelectorAll('.total-linha').forEach(td => {
        subtotal += parseFloat(td.innerText) || 0;
    });
    const totalInput = document.getElementById('spanTotalFinal');
    if(totalInput) totalInput.value = subtotal.toFixed(2);
}

function prepararDadosParaEnvio() {
    const tbody = document.getElementById('tbodyItens');
    if (tbody && tbody.querySelectorAll('tr').length === 0) {
        alert("Adicione pelo menos um item!");
        return false;
    }
    const inputTotal = document.getElementById('spanTotalFinal');
    if (inputTotal) inputTotal.value = inputTotal.value.replace(',', '.');
    document.querySelectorAll('input[name="precoItens"]').forEach(input => {
        input.value = input.value.replace(',', '.');
    });
    return true;
}

async function abrirEdicaoPedido(btn) {
    document.getElementById('main-title').innerText = 'Editar Pedido';

    const btnSalvar = document.getElementById('btn-salvar-global');
    if (btnSalvar) btnSalvar.innerHTML = btnSalvar.innerHTML.replace('Salvar Pedido', 'Atualizar Pedido');

    switchTab('form');

    const form = document.getElementById('formPedido');
    if (form) form.action = '/pedidos/salvar';

    const checkNotificar = document.getElementById('notificar');
    if (checkNotificar) checkNotificar.checked = true;

    const tipo = btn.getAttribute('data-tipo');
    const radio = document.querySelector(`input[name="tipoPedido"][value="${tipo}"]`);
    if (radio) {
        radio.checked = true;
        filtrarFornecedores();
    }

    const inputNumero = document.getElementById('numeroPedido');
    if (inputNumero) inputNumero.value = btn.getAttribute('data-codigo');

    const selectForn = document.getElementById('selectFornecedor');
    if (selectForn) {
        selectForn.value = btn.getAttribute('data-fornecedor');
        atualizarDadosFornecedor(selectForn);
    }

    const selectResp = document.getElementById('selectResponsavel');
    if (selectResp) selectResp.value = btn.getAttribute('data-responsavel');

    const inputData = document.getElementById('dataPedido') || document.querySelector('input[name="dataPedido"]');
    if (inputData) {
        let rawDate = btn.getAttribute('data-data');
        if (rawDate && rawDate.includes('/')) {
            const parts = rawDate.split('/');
            if (parts.length === 3) {
                rawDate = `${parts[2]}-${parts[1]}-${parts[0]}`;
            }
        }
        inputData.value = rawDate;
    }

    const inputObs = document.getElementById('observacaoPedido') || document.querySelector('textarea[name="observacao"]');
    const obsVal = btn.getAttribute('data-obs');
    if (inputObs) inputObs.value = (obsVal && obsVal !== 'null') ? obsVal : "";

    const inputTotal = document.getElementById('spanTotalFinal');
    if (inputTotal) {
        const rawTotal = btn.getAttribute('data-valorTotal');
        inputTotal.value = rawTotal ? parseFloat(rawTotal).toFixed(2) : "0.00";
    }

    let inputId = document.getElementById('pedidoIdEdit');
    if(!inputId) {
        inputId = document.createElement('input');
        inputId.type = 'hidden';
        inputId.name = 'id';
        inputId.id = 'pedidoIdEdit';
        if (form) form.appendChild(inputId);
    }
    if (inputId) inputId.value = btn.getAttribute('data-id');

    const tbody = document.getElementById('tbodyItens');
    if (tbody) tbody.innerHTML = '<tr><td colspan="5" class="text-center">Carregando itens...</td></tr>';

    try {
        const response = await fetch(`/pedidos/${btn.getAttribute('data-id')}/itens-json`);
        if (!response.ok) throw new Error("Erro na API");
        const itens = await response.json();

        if (tbody) tbody.innerHTML = '';

        if (Array.isArray(itens) && itens.length > 0) {
            itens.forEach(item => {
                const parsed = parseItemData(item);
                inserirLinhaTabelaPrincipal(parsed.idItem, parsed.nome, parsed.qtd, parsed.preco, (parsed.qtd * parsed.preco));
            });
        } else {
            if (tbody) tbody.innerHTML = '<tr><td colspan="5" class="text-center">O pedido não contém itens registrados.</td></tr>';
        }
    } catch (err) {
        if (tbody) tbody.innerHTML = '<tr><td colspan="5" class="text-center text-danger">Falha ao carregar itens do pedido.</td></tr>';
    }
}

function abrirModalStatus(id, statusAtual) {
    const statusInput = document.getElementById('statusPedidoId');
    if (statusInput) statusInput.value = id;
    const badge = document.getElementById('badgeStatusAtual');
    if (badge) {
        badge.innerText = statusAtual;
        badge.className = 'badge';
        if (statusAtual === 'PENDENTE') badge.classList.add('badge-analysis');
        else if (statusAtual === 'CONFIRMADO') badge.classList.add('badge-active');
        else if (statusAtual === 'RECEBIDO') badge.classList.add('badge-success');
        else if (statusAtual === 'CANCELADO') badge.classList.add('badge-inactive');
    }
    const modal = document.getElementById('modalStatus');
    if (modal) modal.style.display = 'flex';
}

function fecharModalStatus() {
    const modal = document.getElementById('modalStatus');
    if (modal) modal.style.display = 'none';
}

function selecionarStatus(status) {
    const inputStatus = document.getElementById('novoStatusInput');
    const formStatus = document.getElementById('formStatus');
    if (inputStatus && formStatus) {
        inputStatus.value = status;
        formStatus.submit();
    }
}

function verDetalhes(btn) {
    const codigo = btn.getAttribute('data-codigo') || '';
    const fornecedor = btn.getAttribute('data-fornecedor') || '';
    const data = btn.getAttribute('data-data') || '';
    const responsavel = btn.getAttribute('data-responsavel') || '';
    const total = btn.getAttribute('data-total') || 'R$ 0,00';
    const status = btn.getAttribute('data-status') || '';
    const obs = btn.getAttribute('data-obs');
    const tipo = btn.getAttribute('data-tipo') || '';

    const detCodigo = document.getElementById('det-codigo');
    if (detCodigo) detCodigo.innerText = `Pedido ${codigo}`;
    const detFornecedor = document.getElementById('det-fornecedor');
    if (detFornecedor) detFornecedor.innerText = fornecedor;
    const detData = document.getElementById('det-data');
    if (detData) detData.innerText = data;
    const detResp = document.getElementById('det-responsavel');
    if (detResp) detResp.innerText = responsavel;
    const detTotal = document.getElementById('det-total');
    if (detTotal) detTotal.innerText = total;
    const detObs = document.getElementById('det-obs');
    if (detObs) detObs.innerText = (obs && obs !== 'null' && obs.trim() !== '') ? obs : "Nenhuma observação informada.";
    const detTipo = document.getElementById('det-tipo');
    if (detTipo) detTipo.innerText = (tipo === "PECA") ? "Peças" : "Produtos";

    const badgeDiv = document.getElementById('det-status-badge');
    if (badgeDiv) {
        let cl = status === 'PENDENTE' ? 'badge-analysis' : (status === 'CONFIRMADO' ? 'badge-active' : (status === 'RECEBIDO' ? 'badge-success' : 'badge-inactive'));
        badgeDiv.innerHTML = `<span class="badge ${cl}">${status}</span>`;
    }

    const tbody = document.getElementById('det-tbody-itens');
    if (tbody) tbody.innerHTML = '<tr><td colspan="3" class="text-center">Processando itens...</td></tr>';

    fetch(`/pedidos/${btn.getAttribute('data-id')}/itens-json`)
        .then(res => {
            if (!res.ok) throw new Error("Erro na API");
            return res.json();
        })
        .then(itens => {
            if (!tbody) return;
            tbody.innerHTML = '';
            if (!Array.isArray(itens) || itens.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3" class="text-center">Nenhum item vinculado.</td></tr>';
                return;
            }
            itens.forEach(item => {
                const parsed = parseItemData(item);
                const subtotal = parsed.qtd * parsed.preco;

                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td class="font-600">${item.nome || '-'}</td>
                    <td class="text-center">${parsed.qtd}</td>
                    <td>R$ ${parsed.preco.toFixed(2)}</td>
                    <td class="font-600">R$ ${subtotal.toFixed(2)}</td>
                `;
                tbody.appendChild(tr);
            });
        })
        .catch(err => {
            if (tbody) tbody.innerHTML = '<tr><td colspan="3" class="text-center text-danger">Falha ao acessar detalhes dos itens.</td></tr>';
        });

    const modal = document.getElementById('modalDetalhes');
    if (modal) modal.style.display = 'flex';
}

function fecharModalDetalhes() {
    const modal = document.getElementById('modalDetalhes');
    if (modal) modal.style.display = 'none';
}

function configurarPesquisaLocal(inputId, tableId) {
    const input = document.getElementById(inputId);
    const table = document.getElementById(tableId);
    if (!input || !table) return;

    input.addEventListener('keyup', function () {
        const termo = input.value.toLowerCase();
        const linhas = table.querySelectorAll('tbody tr');

        linhas.forEach(linha => {
            linha.style.display =
                linha.innerText.toLowerCase().includes(termo) ? '' : 'none';
        });
    });
}

document.addEventListener("DOMContentLoaded", function() {
    filtrarFornecedores();
    configurarPesquisaLocal('searchInput', 'minhaTabela');
});
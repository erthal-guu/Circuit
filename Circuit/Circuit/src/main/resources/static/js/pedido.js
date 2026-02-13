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
    const option = select.options[select.selectedIndex];
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
    if (tab !== 'lista') {
        const check = document.getElementById('notificarFornecedor');
        if(check) check.checked = false;
    }
    if (tab === 'lista') {
        viewLista.style.display = 'block';
        viewForm.style.display = 'none';
        btnLista.classList.add('active');
        btnForm.classList.remove('active');
        btnSalvar.style.display = 'none';
        mainTitle.innerText = 'Pedidos';
    } else {
        viewLista.style.display = 'none';
        viewForm.style.display = 'block';
        btnLista.classList.remove('active');
        btnForm.classList.add('active');
        btnSalvar.style.display = 'flex';
        mainTitle.innerText = 'Novo Pedido';

        if(!document.getElementById('numeroPedido').value) {
            gerarCodigoHex();
        }
    }
}

function filtrarFornecedores() {
    const radioSelecionado = document.querySelector('input[name="tipoPedido"]:checked');
    if (!radioSelecionado) return;
    const tipoDesejado = radioSelecionado.value.toUpperCase();

    const select = document.getElementById('selectFornecedor');
    const options = select.options;
    select.value = "";

    const inputCnpj = document.getElementById('inputCnpjFornecedor');
    if(inputCnpj) inputCnpj.value = "";

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

function abrirModalAdicionarItens() {
    const select = document.getElementById('selectFornecedor');
    const fornecedorId = select.value;

    if (!fornecedorId) {
        alert("Selecione um fornecedor primeiro!");
        return;
    }

    const option = select.options[select.selectedIndex];
    const nome = option.getAttribute('data-nome');

    const displayNomeModal = document.getElementById('fornecedorNome');
    if (displayNomeModal) {
        displayNomeModal.innerText = "Fornecedor: " + nome;
    }

    carregarItensDoFornecedor(fornecedorId);
    document.getElementById('modalItemPedido').style.display = 'block';
}

function fecharModal() {
    document.getElementById('modalItemPedido').style.display = 'none';
}

function carregarItensDoFornecedor(idFornecedor) {
    const container = document.getElementById('tbodyModalItens');
    const radioTipo = document.querySelector('input[name="tipoPedido"]:checked');

    if (!container) return;

    const tipoNorm = radioTipo.value;
    const url = `/fornecedores/json/${idFornecedor}/itens-vinculados?tipo=${tipoNorm}`;

    container.innerHTML = '<tr><td colspan="4" class="text-center">Carregando itens...</td></tr>';

    fetch(url)
        .then(res => res.json())
        .then(data => {
            container.innerHTML = '';

            if (!data || data.length === 0) {
                container.innerHTML = '<tr><td colspan="4" class="text-center">Nenhum item encontrado para este fornecedor.</td></tr>';
                return;
            }

            data.forEach(item => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td class="text-center">
                        <input type="checkbox" class="check-item" onchange="calcularResumoModal()" 
                            data-id="${item.id}" data-nome="${item.nome}">
                    </td>
                    <td>
                        <div class="font-600">${item.nome || 'Sem Nome'}</div>
                        <div class="small-text text-muted">REF: ${item.id}</div>
                    </td>
                    <td>
                        <input type="text" class="input-modal-erp preco-modal" value="${item.precoCompra || '0.00'}" oninput="calcularResumoModal()">
                    </td>
                    <td>
                        <input type="number" class="input-modal-erp qtd-modal" value="1" min="1" oninput="calcularResumoModal()">
                    </td>
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
        const precoInput = tr.querySelector('.preco-modal').value;
        const preco = parseFloat(precoInput.replace(',', '.')) || 0;
        const qtd = parseFloat(tr.querySelector('.qtd-modal').value) || 0;
        subtotal += (preco * qtd);
    });

    const countElem = document.getElementById('countSelecionados');
    const subtotalElem = document.getElementById('subtotalModal');

    if(countElem) countElem.innerText = checks.length;
    if(subtotalElem) subtotalElem.innerText = `R$ ${subtotal.toFixed(2).replace('.', ',')}`;
}

function adicionarSelecionados() {
    const checks = document.querySelectorAll('.check-item:checked');
    checks.forEach(check => {
        const tr = check.closest('tr');
        const id = check.getAttribute('data-id');
        const nome = check.getAttribute('data-nome');
        const preco = parseFloat(tr.querySelector('.preco-modal').value.replace(',', '.')) || 0;
        const qtd = parseFloat(tr.querySelector('.qtd-modal').value) || 0;
        const total = preco * qtd;

        inserirLinhaTabelaPrincipal(id, nome, qtd, preco, total);
    });

    fecharModal();
}

function inserirLinhaTabelaPrincipal(id, nome, qtd, preco, total) {
    const tbody = document.getElementById('tbodyItens');
    const tr = document.createElement('tr');

    tr.innerHTML = `
        <td>${nome} <input type="hidden" name="itensId" value="${id}"></td>
        <td><input type="number" name="quantidadeItens" class="form-control" value="${qtd}" oninput="recalcularTotalPedido() " readonly></td>
        <td><input type="text" name="precoItens" class="form-control" value="${preco.toFixed(2)}" oninput="recalcularTotalPedido()" readonly></td>
        <td class="total-linha">${total.toFixed(2)}</td>
        <td class="text-center">
            <button type="button" class="btn-icon remove" onclick="this.closest('tr').remove(); recalcularTotalPedido();"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="feather feather-trash-2">
    <polyline points="3 6 5 6 21 6"></polyline>
    <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
    <line x1="10" y1="11" x2="10" y2="17"></line>
    <line x1="14" y1="11" x2="14" y2="17"></line>
</svg></button>
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

    const inputSubtotal = document.getElementById('inputSubtotal');
    const spanTotalFinal = document.getElementById('spanTotalFinal');

    if(inputSubtotal) inputSubtotal.value = subtotal.toFixed(2);
    if(spanTotalFinal) spanTotalFinal.value = subtotal.toFixed(2);
}

document.addEventListener("DOMContentLoaded", function() {
    const inputNum = document.getElementById("numeroPedido");
    if (inputNum && (inputNum.value === "" || inputNum.value === "AUTO-00123")) {
        gerarCodigoHex();
    }
    filtrarFornecedores();
});
function abrirModalStatus(id, statusAtual) {
    document.getElementById('statusPedidoId').value = id;
    const badge = document.getElementById('badgeStatusAtual');

    badge.innerText = statusAtual;
    badge.className = 'badge';
    if (statusAtual === 'PENDENTE') badge.classList.add('badge-info');
    else if (statusAtual === 'CONFIRMADO') badge.classList.add('badge-active');
    else if (statusAtual === 'RECEBIDO') badge.classList.add('badge-success');
    else if (statusAtual === 'CANCELADO') badge.classList.add('badge-inactive');

    document.getElementById('modalStatus').style.display = 'flex';
}

function fecharModalStatus() {
    document.getElementById('modalStatus').style.display = 'none';
}

function selecionarStatus(status) {
    document.getElementById('novoStatusInput').value = status;
    document.getElementById('formStatus').submit();
}
function verDetalhes(btn) {
    const codigo = btn.getAttribute('data-codigo');
    const fornecedor = btn.getAttribute('data-fornecedor');
    const data = btn.getAttribute('data-data');
    const responsavel = btn.getAttribute('data-responsavel');
    const total = btn.getAttribute('data-total');
    const status = btn.getAttribute('data-status');
    const obs = btn.getAttribute('data-obs');
    const tipo = btn.getAttribute('data-tipo');
    document.getElementById('det-codigo').innerText = `Pedido ${codigo}`;
    document.getElementById('det-fornecedor').innerText = fornecedor;
    document.getElementById('det-data').innerText = data;
    document.getElementById('det-responsavel').innerText = responsavel;
    document.getElementById('det-total').innerText = total;
    document.getElementById('det-obs').innerText = (obs && obs !== 'null') ? obs : "Nenhuma observação informada.";
    document.getElementById('det-tipo').innerText = (tipo === "PECA") ? "Peças" : "Produtos";
    const badgeDiv = document.getElementById('det-status-badge');
    let classeBadge = "";
    if (status === 'PENDENTE') {
        classeBadge = 'badge-info';
    } else if (status === 'CONFIRMADO') {
        classeBadge = 'badge-active';
    } else if (status === 'RECEBIDO') {
        classeBadge = 'badge-success';
    } else if (status === 'CANCELADO') {
        classeBadge = 'badge-inactive';
    } else {
        classeBadge = 'badge-analysis';
    }
    badgeDiv.innerHTML = `<span class="badge ${classeBadge}">${status}</span>`;
    document.getElementById('modalDetalhes').style.display = 'flex';
}

function fecharModalDetalhes() {
    document.getElementById('modalDetalhes').style.display = 'none';
}

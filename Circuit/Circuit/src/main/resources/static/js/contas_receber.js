
function abrirModalNovaContaReceber() {
    document.getElementById('formContaReceber').reset();
    document.getElementById('contaReceberId').value = '';
    document.getElementById('valorRecebido').value = '';
    document.getElementById('dataPagamento').value = '';
    document.getElementById('origem').value = '';
    document.getElementById('origemId').value = '';
    document.getElementById('numeroParcelas').value = '1';
    

    const modalTitle = document.getElementById('modalContaReceberTitle');
    if (modalTitle) {
        modalTitle.textContent = 'Nova Conta a Receber';
    }
    
    document.getElementById('modalContaReceber').classList.add('active');
}

function pesquisarContas() {
    const termoBusca = document.getElementById('searchContas').value.toLowerCase();
    const tabela = document.getElementById('tabelaContasReceber');
    const linhas = tabela.getElementsByTagName('tr');
    
    for (let i = 1; i < linhas.length; i++) {
        const linha = linhas[i];
        const textoLinha = linha.textContent.toLowerCase();
        
        if (termoBusca === '') {
            linha.style.display = '';
        } else if (textoLinha.includes(termoBusca)) {
            linha.style.display = '';
        } else {
            linha.style.display = 'none';
        }
    }
}

document.getElementById('searchContas').addEventListener('input', pesquisarContas);

function recarregarPagina() {
    window.location.reload();
}

function fecharModalContaReceber() {
    document.getElementById('modalContaReceber').classList.remove('active');
}

function salvarContaReceber() {
    const id = document.getElementById('contaReceberId').value;
    const cliente = document.getElementById('cliente').value;
    const valor = document.getElementById('valor').value;
    const valorRecebido = document.getElementById('valorRecebido').value;
    const dataVencimento = document.getElementById('dataVencimento').value;
    const dataPagamento = document.getElementById('dataPagamento').value;
    const formaPagamento = document.getElementById('formaPagamento').value;
    const origem = document.getElementById('origem').value;
    const origemId = document.getElementById('origemId').value;
    const condicaoPagamentoRadio = document.querySelector('input[name="condicaoPagamentoEditar"]:checked');
    const condicaoPagamento = condicaoPagamentoRadio ? condicaoPagamentoRadio.value : null;

    const numeroParcelas = document.getElementById('numeroParcelas').value;

    if (!cliente || !valor || !dataVencimento) {
        alert('Por favor, preencha todos os campos obrigatórios.');
        return;
    }

    const dados = {
        id: id || null,
        cliente: cliente,
        valor: valor,
        valorRecebido: valorRecebido || 0,
        dataVencimento: dataVencimento,
        dataPagamento: dataPagamento || null,
        formaPagamento: formaPagamento || null,
        origem: origem || null,
        origemId: origemId || null,
        condicaoPagamento: condicaoPagamento || null,
        numeroParcelas: numeroParcelas || 1
    };

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/contas-receber/editar';

    for (const key in dados) {
        if (dados[key] !== null && dados[key] !== '') {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = dados[key];
            form.appendChild(input);
        }
    }

    document.body.appendChild(form);
    form.submit();
}

function abrirModalReceber(btn) {
    const id = btn.getAttribute("data-id");
    const valor = btn.getAttribute("data-valorPagar");
    const cliente = btn.getAttribute("data-cliente");
    const valorRecebido = btn.getAttribute("data-valorRecebido");
    const formaPagamento = btn.getAttribute("data-formaPagamento");
    const origem = btn.getAttribute("data-origem");

    const receberCliente = document.getElementById('receberCliente');
    const receberValor = document.getElementById('receberValor');
    const receberValorRecebido = document.getElementById('receberValorRecebido');
    const receberId = document.getElementById('receberId');
    const receberDataPagamento = document.getElementById('receberDataPagamento');
    const receberDataPagamentoRow = document.getElementById('receberDataPagamentoRow');

    if (receberCliente) receberCliente.value = cliente;
    if (receberValor) receberValor.value = valor;
    if (receberValorRecebido) receberValorRecebido.value = valorRecebido;
    if (receberId) receberId.value = id;

    const hoje = new Date().toISOString().split('T')[0];

    if (origem === 'Ordem de serviço') {
        if (receberDataPagamento) receberDataPagamento.value = hoje;
    } else {
        if (receberDataPagamentoRow) receberDataPagamentoRow.value = hoje;
    }

    const condicaoAVistaReceber = document.getElementById('condicaoAVistaReceber');
    const parcelasReceber = document.getElementById('parcelasReceber');
    if (condicaoAVistaReceber) {
        condicaoAVistaReceber.checked = true;
    }
    if (parcelasReceber) {
        parcelasReceber.value = '1';
    }

    const formaPagamentoRow = document.getElementById('formaPagamentoRow');
    const dataPagamentoRow = document.getElementById('dataPagamentoRow');
    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainerReceber');
    const parcelasContainer = document.getElementById('parcelasContainerReceber');
    const valorParcelaContainer = document.getElementById('valorParcelaContainer');
    const receberFormaPagamento = document.getElementById('receberFormaPagamento');

    if (origem === 'Ordem de serviço') {
        if (formaPagamentoRow) {
            formaPagamentoRow.style.display = 'flex';
        }
        if (dataPagamentoRow) {
            dataPagamentoRow.style.display = 'none';
        }
        if (receberFormaPagamento) {
            receberFormaPagamento.value = '';
        }
        if (condicaoPagamentoContainer) {
            condicaoPagamentoContainer.style.display = 'none';
            condicaoPagamentoContainer.style.opacity = '0';
            condicaoPagamentoContainer.style.transform = 'translateY(-20px)';
        }
        if (parcelasContainer) {
            parcelasContainer.style.display = 'none';
        }
        if (valorParcelaContainer) {
            valorParcelaContainer.style.display = 'none';
        }
    } else {
        if (formaPagamentoRow) {
            formaPagamentoRow.style.display = 'flex';
        }
        if (dataPagamentoRow) {
            dataPagamentoRow.style.display = 'none';
        }
        if (receberFormaPagamento) {
            receberFormaPagamento.value = formaPagamento || '';
            if (formaPagamento) {
                receberFormaPagamento.dispatchEvent(new Event('change'));
            }
        }
        if (condicaoPagamentoContainer) {
            condicaoPagamentoContainer.style.display = 'none';
            condicaoPagamentoContainer.style.opacity = '0';
            condicaoPagamentoContainer.style.transform = 'translateY(-20px)';
        }
        if (parcelasContainer) {
            parcelasContainer.style.display = 'none';
        }
        if (valorParcelaContainer) {
            valorParcelaContainer.style.display = 'none';
        }
    }

    if (receberDataPagamento) receberDataPagamento.value = hoje;

    document.getElementById('modalReceber').classList.add('active');
}


function fecharModalReceber() {
    document.getElementById('modalReceber').classList.remove('active');

    const formReceber = document.getElementById('formReceber');
    if (formReceber) {
        formReceber.reset();
    }

    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainerReceber');
    const parcelasContainer = document.getElementById('parcelasContainerReceber');
    const valorParcelaContainer = document.getElementById('valorParcelaContainer');

    if (condicaoPagamentoContainer) {
        condicaoPagamentoContainer.style.display = 'none';
        condicaoPagamentoContainer.style.opacity = '0';
        condicaoPagamentoContainer.style.transform = 'translateY(-20px)';
        condicaoPagamentoContainer.classList.remove('modal-animate-show');
    }

    if (parcelasContainer) {
        parcelasContainer.style.display = 'none';
    }

    if (valorParcelaContainer) {
        valorParcelaContainer.style.display = 'none';
    }
}

function handleFormaPagamentoChangeReceber() {
    const formaPagamento = document.getElementById('receberFormaPagamento');
    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainerReceber');
    const parcelasContainer = document.getElementById('parcelasContainerReceber');
    const receberDataPagamento = document.getElementById('receberDataPagamento');

    if (!formaPagamento || !condicaoPagamentoContainer || !parcelasContainer) return;

    const selectedValue = formaPagamento.value;

    if (selectedValue === 'CARTAO_CREDITO') {
        condicaoPagamentoContainer.style.display = 'block';
        condicaoPagamentoContainer.style.opacity = '1';
        condicaoPagamentoContainer.style.transform = 'translateY(0)';
        setTimeout(() => {
            condicaoPagamentoContainer.classList.add('modal-animate-show');
        }, 10);
        handleCondicaoPagamentoChangeReceber();
    } else {
        condicaoPagamentoContainer.style.display = 'none';
        condicaoPagamentoContainer.style.opacity = '0';
        condicaoPagamentoContainer.style.transform = 'translateY(-20px)';
        condicaoPagamentoContainer.classList.remove('modal-animate-show');
        condicaoPagamentoContainer.classList.add('modal-animate');
        parcelasContainer.style.display = 'none';
    }
}

function handleCondicaoPagamentoChangeReceber() {
    const condicaoPagamento = document.querySelector('input[name="condicaoPagamentoReceber"]:checked');
    const parcelasContainer = document.getElementById('parcelasContainerReceber');
    const valorParcelaContainer = document.getElementById('valorParcelaContainer');
    const labelAVista = document.getElementById('labelCondicaoAVistaReceber');
    const labelParcelado = document.getElementById('labelCondicaoParceladoReceber');

    if (!condicaoPagamento || !parcelasContainer || !labelAVista || !labelParcelado) return;

    if (condicaoPagamento.value === 'PARCELADO') {
        parcelasContainer.style.display = 'block';
        if (valorParcelaContainer) {
            valorParcelaContainer.style.display = 'block';
        }
        calcularValorParcela();
    } else {
        parcelasContainer.style.display = 'none';
        if (valorParcelaContainer) {
            valorParcelaContainer.style.display = 'none';
        }
        const receberValor = document.getElementById('receberValor');
        const receberValorRecebido = document.getElementById('receberValorRecebido');
        if (receberValor && receberValorRecebido) {
            receberValorRecebido.value = receberValor.value;
        }
    }
}

function calcularValorParcela() {
    const receberValor = document.getElementById('receberValor');
    const receberValorRecebido = document.getElementById('receberValorRecebido');
    const parcelasReceber = document.getElementById('parcelasReceber');
    const valorParcela = document.getElementById('valorParcela');

    if (!receberValor || !receberValorRecebido || !parcelasReceber || !valorParcela) return;

    const valorTotal = parseFloat(receberValor.value) || 0;
    const numeroParcelas = parseInt(parcelasReceber.value) || 1;
    
    if (numeroParcelas > 0) {
        const valorPorParcela = valorTotal / numeroParcelas;
        valorParcela.value = valorPorParcela.toFixed(2);
        receberValorRecebido.value = valorTotal.toFixed(2);
    }
}

async function confirmarReceber() {
    const id = document.getElementById('receberId').value;
    const valorRecebido = document.getElementById('receberValorRecebido').value;
    const formaPagamento = document.getElementById('receberFormaPagamento').value;
    const condicaoPagamento = document.querySelector('input[name="condicaoPagamentoReceber"]:checked')?.value;
    const numeroParcelas = document.getElementById('parcelasReceber').value;

    const dataPagamentoRow = document.getElementById('receberDataPagamentoRow');
    const dataPagamentoInput = dataPagamentoRow && dataPagamentoRow.offsetParent !== null
        ? dataPagamentoRow
        : document.getElementById('receberDataPagamento');

    const dataPagamento = dataPagamentoInput ? dataPagamentoInput.value : '';

    if (!valorRecebido || !dataPagamento) {
        alert('Por favor, preencha todos os campos obrigatórios.');
        return;
    }

    const formData = new FormData();
    formData.append('id', id);
    formData.append('valorRecebido', valorRecebido);
    formData.append('dataPagamento', dataPagamento);

    if (formaPagamento) {
        formData.append('formaPagamento', formaPagamento);
    }

    if (condicaoPagamento) {
        formData.append('condicaoPagamento', condicaoPagamento);
    }

    if (numeroParcelas && condicaoPagamento === 'PARCELADO') {
        formData.append('numeroParcelas', numeroParcelas);
    }
}

function formatarData(data) {
    if (!data) return '-';
    return new Date(data).toLocaleDateString('pt-BR');
}

function handleFormaPagamentoChangeEditar() {
    const formaPagamento = document.getElementById('formaPagamento');
    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainerEditar');
    const parcelasContainer = document.getElementById('parcelasContainerEditar');

    if (!formaPagamento || !condicaoPagamentoContainer || !parcelasContainer) return;

    const selectedValue = formaPagamento.value;

    if (selectedValue === 'CARTAO_CREDITO') {
        condicaoPagamentoContainer.style.display = 'block';
        condicaoPagamentoContainer.style.opacity = '1';
        condicaoPagamentoContainer.style.transform = 'translateY(0)';
        setTimeout(() => {
            condicaoPagamentoContainer.classList.add('modal-animate-show');
        }, 10);
        handleCondicaoPagamentoChangeEditar();
    } else {
        condicaoPagamentoContainer.style.display = 'none';
        condicaoPagamentoContainer.style.opacity = '0';
        condicaoPagamentoContainer.style.transform = 'translateY(-20px)';
        condicaoPagamentoContainer.classList.remove('modal-animate-show');
        condicaoPagamentoContainer.classList.add('modal-animate');
        parcelasContainer.style.display = 'none';
    }
}

function handleCondicaoPagamentoChangeEditar() {
    const condicaoPagamento = document.querySelector('input[name="condicaoPagamentoEditar"]:checked');
    const parcelasContainer = document.getElementById('parcelasContainerEditar');
    const labelAVista = document.getElementById('labelCondicaoAVistaEditar');
    const labelParcelado = document.getElementById('labelCondicaoParceladoEditar');

    if (!condicaoPagamento || !parcelasContainer || !labelAVista || !labelParcelado) return;

    if (condicaoPagamento.value === 'PARCELADO') {
        parcelasContainer.style.display = 'block';
    } else {
        parcelasContainer.style.display = 'none';
        const numeroParcelas = document.getElementById('numeroParcelas');
        if (numeroParcelas) {
            numeroParcelas.value = '1';
        }
    }
}

function editarContaReceber(btn) {
    const id = btn.getAttribute("data-id");
    const clienteId = btn.getAttribute("data-clienteId");
    const clienteNome = btn.getAttribute("data-cliente");
    const valor = btn.getAttribute("data-valor");
    const valorRecebido = btn.getAttribute("data-valorRecebido");
    const dataVencimento = btn.getAttribute("data-vencimento");
    const dataPagamento = btn.getAttribute("data-pagamento");
    const status = btn.getAttribute("data-status");
    const formaPagamento = btn.getAttribute("data-formaPagamento");
    const origem = btn.getAttribute("data-origem");
    const origemId = btn.getAttribute("data-origemId");
    const condicaoPagamento = btn.getAttribute("data-condicaoPagamento");
    const numeroParcelas = btn.getAttribute("data-numeroParcelas");
    document.getElementById('contaReceberId').value = id;
    const clienteSelect = document.getElementById('cliente');
    clienteSelect.value = clienteId;
    document.getElementById('valor').value = valor;
    const valorRecebidoInput = document.getElementById('valorRecebido');
    if (valorRecebidoInput) {
        valorRecebidoInput.value = valorRecebido || 0;
    }
    if (dataVencimento) {
        const dataVencimentoObj = new Date(dataVencimento);
        const dataVencimentoFormatada = dataVencimentoObj.toISOString().split('T')[0];
        document.getElementById('dataVencimento').value = dataVencimentoFormatada;
    }
    const dataPagamentoInput = document.getElementById('dataPagamento');
    if (dataPagamentoInput && dataPagamento) {
        const dataPagamentoObj = new Date(dataPagamento);
        const dataPagamentoFormatada = dataPagamentoObj.toISOString().split('T')[0];
        dataPagamentoInput.value = dataPagamentoFormatada;
    }

    const formaPagamentoSelect = document.getElementById('formaPagamento');
    if (formaPagamentoSelect) {
        formaPagamentoSelect.value = formaPagamento || '';
        formaPagamentoSelect.dispatchEvent(new Event('change'));
    }
    const origemInput = document.getElementById('origem');
    if (origemInput) {
        origemInput.value = origem || '';
    }
    const origemIdInput = document.getElementById('origemId');
    if (origemIdInput) {
        origemIdInput.value = origemId || '';
    }

    const condicaoAVistaRadio = document.getElementById('condicaoAVistaEditar');
    const condicaoParceladoRadio = document.getElementById('condicaoParceladoEditar');
    if (condicaoPagamento) {
        if (condicaoPagamento === 'AVISTA') {
            if (condicaoAVistaRadio) condicaoAVistaRadio.checked = true;
        } else if (condicaoPagamento === 'PARCELADO') {
            if (condicaoParceladoRadio) condicaoParceladoRadio.checked = true;
        }
    } else {
        if (condicaoAVistaRadio) condicaoAVistaRadio.checked = true;
    }

    const numeroParcelasInput = document.getElementById('numeroParcelas');
    if (numeroParcelasInput) {
        numeroParcelasInput.value = numeroParcelas || 1;
    }
    
    const modalTitle = document.querySelector('#modalContaReceber .modal-header h2');
    if (modalTitle) {
        modalTitle.textContent = 'Editar Conta a Receber';
    }
    document.getElementById('modalContaReceber').classList.add('active');
}

document.addEventListener('DOMContentLoaded', function() {
});


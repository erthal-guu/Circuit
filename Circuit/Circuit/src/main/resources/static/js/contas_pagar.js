
function abrirModalNovaContaPagar() {
    document.getElementById('formContaPagar').reset();
    document.getElementById('contaPagarId').value = '';
    document.getElementById('valorPago').value = '';
    document.getElementById('dataPagamento').value = '';
    document.getElementById('origem').value = '';
    document.getElementById('origemId').value = '';
    document.getElementById('numeroParcelas').value = '1';
    

    const modalTitle = document.getElementById('modalContaPagarTitle');
    if (modalTitle) {
        modalTitle.textContent = 'Nova Conta a Pagar';
    }
    
    document.getElementById('modalContaPagar').classList.add('active');
}

function pesquisarContas() {
    const termoBusca = document.getElementById('searchContas').value.toLowerCase();
    const tabela = document.getElementById('tabelaContasPagar');
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

function fecharModalContaPagar() {
    document.getElementById('modalContaPagar').classList.remove('active');
}

function salvarContaPagar() {
    const id = document.getElementById('contaPagarId').value;
    const fornecedor = document.getElementById('fornecedor').value;
    const valor = document.getElementById('valor').value;
    const valorPago = document.getElementById('valorPago').value;
    const dataVencimento = document.getElementById('dataVencimento').value;
    const dataPagamento = document.getElementById('dataPagamento').value;
    const formaPagamento = document.getElementById('formaPagamento').value;
    const origem = document.getElementById('origem').value;
    const origemId = document.getElementById('origemId').value;
    const condicaoPagamentoRadio = document.querySelector('input[name="condicaoPagamentoEditar"]:checked');
    const condicaoPagamento = condicaoPagamentoRadio ? condicaoPagamentoRadio.value : null;

    const numeroParcelas = document.getElementById('numeroParcelas').value;

    if (!fornecedor || !valor || !dataVencimento) {
        alert('Por favor, preencha todos os campos obrigatórios.');
        return;
    }

    if (dataPagamento && dataVencimento) {
        const dataPagamentoObj = new Date(dataPagamento);
        const dataVencimentoObj = new Date(dataVencimento);
        if (dataPagamentoObj < dataVencimentoObj) {
            alert('Data de pagamento não pode ser anterior à data de vencimento.');
            return;
        }
    }

    const dados = {
        id: id || null,
        fornecedor: fornecedor,
        valor: valor,
        valorPago: valorPago || 0,
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
    form.action = '/contas-pagar/editar';

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

function abrirModalPagar(btn) {
    const id = btn.getAttribute("data-id");
    const valor = btn.getAttribute("data-valorPagar");
    const fornecedor = btn.getAttribute("data-fornecedor");
    const valorPago = btn.getAttribute("data-valorPago");
    const formaPagamento = btn.getAttribute("data-formaPagamento");
    const origem = btn.getAttribute("data-origem");

    const pagarFornecedor = document.getElementById('pagarFornecedor');
    const pagarValor = document.getElementById('pagarValor');
    const pagarValorPago = document.getElementById('pagarValorPago');
    const pagarId = document.getElementById('pagarId');
    const pagarDataPagamento = document.getElementById('pagarDataPagamento');
    const pagarDataPagamentoRow = document.getElementById('pagarDataPagamentoRow');

    if (pagarFornecedor) {
        pagarFornecedor.value = fornecedor;
    }
    if (pagarValor) {
        pagarValor.value = valor;
    }
    if (pagarValorPago) {
        pagarValorPago.value = valorPago;
    }
    if (pagarId) {
        pagarId.value = id;
    }

    const hoje = new Date().toISOString().split('T')[0];

    if (origem === 'Pedido') {
        if (pagarDataPagamento) {
            pagarDataPagamento.value = hoje;
        }
    } else {
        if (pagarDataPagamentoRow) {
            pagarDataPagamentoRow.value = hoje;
        }
    }

    const condicaoAVistaPagar = document.getElementById('condicaoAVistaPagar');
    const parcelasPagar = document.getElementById('parcelasPagar');
    if (condicaoAVistaPagar) {
        condicaoAVistaPagar.checked = true;
    }
    if (parcelasPagar) {
        parcelasPagar.value = '1';
    }

    const formaPagamentoRow = document.getElementById('formaPagamentoRow');
    const dataPagamentoRow = document.getElementById('dataPagamentoRow');
    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainerPagar');
    const parcelasContainer = document.getElementById('parcelasContainerPagar');
    const valorParcelaContainer = document.getElementById('valorParcelaContainer');
    const pagarFormaPagamento = document.getElementById('pagarFormaPagamento');

    if (origem === 'Pedido') {
        if (formaPagamentoRow) {
            formaPagamentoRow.style.display = 'flex';
        }
        if (dataPagamentoRow) {
            dataPagamentoRow.style.display = 'none';
        }
        if (pagarFormaPagamento) {
            pagarFormaPagamento.value = '';
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
        if (pagarFormaPagamento) {
            pagarFormaPagamento.value = formaPagamento || '';
            if (formaPagamento) {
                pagarFormaPagamento.dispatchEvent(new Event('change'));
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

    if (pagarDataPagamento) {
        pagarDataPagamento.value = hoje;
    }

    document.getElementById('modalPagar').classList.add('active');
}


function fecharModalPagar() {
    document.getElementById('modalPagar').classList.remove('active');

    const formPagar = document.getElementById('formPagar');
    if (formPagar) {
        formPagar.reset();
    }

    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainerPagar');
    const parcelasContainer = document.getElementById('parcelasContainerPagar');
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

function handleFormaPagamentoChangePagar() {
    const formaPagamento = document.getElementById('pagarFormaPagamento');
    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainerPagar');
    const parcelasContainer = document.getElementById('parcelasContainerPagar');
    const pagarDataPagamento = document.getElementById('pagarDataPagamento');

    if (!formaPagamento || !condicaoPagamentoContainer || !parcelasContainer) {
        return;
    }

    const selectedValue = formaPagamento.value;

    if (selectedValue === 'CARTAO_CREDITO') {
        condicaoPagamentoContainer.style.display = 'block';
        condicaoPagamentoContainer.style.opacity = '1';
        condicaoPagamentoContainer.style.transform = 'translateY(0)';
        setTimeout(() => {
            condicaoPagamentoContainer.classList.add('modal-animate-show');
        }, 10);
        handleCondicaoPagamentoChangePagar();
    } else {
        condicaoPagamentoContainer.style.display = 'none';
        condicaoPagamentoContainer.style.opacity = '0';
        condicaoPagamentoContainer.style.transform = 'translateY(-20px)';
        condicaoPagamentoContainer.classList.remove('modal-animate-show');
        condicaoPagamentoContainer.classList.add('modal-animate');
        parcelasContainer.style.display = 'none';
    }
}

function handleCondicaoPagamentoChangePagar() {
    const condicaoPagamento = document.querySelector('input[name="condicaoPagamentoPagar"]:checked');
    const parcelasContainer = document.getElementById('parcelasContainerPagar');
    const valorParcelaContainer = document.getElementById('valorParcelaContainer');
    const labelAVista = document.getElementById('labelCondicaoAVistaPagar');
    const labelParcelado = document.getElementById('labelCondicaoParceladoPagar');

    if (!condicaoPagamento || !parcelasContainer || !labelAVista || !labelParcelado) {
        return;
    }

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
        const pagarValor = document.getElementById('pagarValor');
        const pagarValorPago = document.getElementById('pagarValorPago');
        if (pagarValor && pagarValorPago) {
            pagarValorPago.value = pagarValor.value;
        }
    }
}

function calcularValorParcela() {
    const pagarValor = document.getElementById('pagarValor');
    const pagarValorPago = document.getElementById('pagarValorPago');
    const parcelasPagar = document.getElementById('parcelasPagar');
    const valorParcela = document.getElementById('valorParcela');

    if (!pagarValor || !pagarValorPago || !parcelasPagar || !valorParcela) {
        return;
    }

    const valorTotal = parseFloat(pagarValor.value) || 0;
    const numeroParcelas = parseInt(parcelasPagar.value) || 1;
    
    if (numeroParcelas > 0) {
        const valorPorParcela = valorTotal / numeroParcelas;
        valorParcela.value = valorPorParcela.toFixed(2);
        pagarValorPago.value = valorTotal.toFixed(2);
    }
}

async function confirmarPagar() {
    const id = document.getElementById('pagarId').value;
    const valorPago = document.getElementById('pagarValorPago').value;
    const formaPagamento = document.getElementById('pagarFormaPagamento').value;
    const condicaoPagamento = document.querySelector('input[name="condicaoPagamentoPagar"]:checked')?.value;
    const numeroParcelas = document.getElementById('parcelasPagar').value;

    const dataPagamentoRow = document.getElementById('pagarDataPagamentoRow');
    const dataPagamentoInput = dataPagamentoRow && dataPagamentoRow.offsetParent !== null
        ? dataPagamentoRow
        : document.getElementById('pagarDataPagamento');

    const dataPagamento = dataPagamentoInput ? dataPagamentoInput.value : '';

    if (!valorPago || !dataPagamento) {
        alert('Por favor, preencha todos os campos obrigatórios.');
        return;
    }

    const formData = new FormData();
    formData.append('id', id);
    formData.append('valorPago', valorPago);
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

    if (!formaPagamento || !condicaoPagamentoContainer || !parcelasContainer) {
        return;
    }

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

    if (!condicaoPagamento || !parcelasContainer || !labelAVista || !labelParcelado) {
        return;
    }

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

function editarContaPagar(btn) {
    const id = btn.getAttribute("data-id");
    const fornecedorId = btn.getAttribute("data-fornecedorId");
    const fornecedorNome = btn.getAttribute("data-fornecedor");
    const valor = btn.getAttribute("data-valor");
    const valorPago = btn.getAttribute("data-valorPago");
    const dataVencimento = btn.getAttribute("data-vencimento");
    const dataPagamento = btn.getAttribute("data-pagamento");
    const status = btn.getAttribute("data-status");
    const formaPagamento = btn.getAttribute("data-formaPagamento");
    const origem = btn.getAttribute("data-origem");
    const origemId = btn.getAttribute("data-origemId");
    const condicaoPagamento = btn.getAttribute("data-condicaoPagamento");
    const numeroParcelas = btn.getAttribute("data-numeroParcelas");
    document.getElementById('contaPagarId').value = id;
    const fornecedorSelect = document.getElementById('fornecedor');
    fornecedorSelect.value = fornecedorId;
    document.getElementById('valor').value = valor;
    const valorPagoInput = document.getElementById('valorPago');
    if (valorPagoInput) {
        valorPagoInput.value = valorPago || 0;
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
            if (condicaoAVistaRadio) {
                condicaoAVistaRadio.checked = true;
            }
        } else if (condicaoPagamento === 'PARCELADO') {
            if (condicaoParceladoRadio) {
                condicaoParceladoRadio.checked = true;
            }
        }
    } else {
        if (condicaoAVistaRadio) {
            condicaoAVistaRadio.checked = true;
        }
    }
    
    const numeroParcelasInput = document.getElementById('numeroParcelas');
    if (numeroParcelasInput) {
        numeroParcelasInput.value = numeroParcelas || 1;
    }
    
    const modalTitle = document.querySelector('#modalContaPagar .modal-header h2');
    if (modalTitle) {
        modalTitle.textContent = 'Editar Conta a Pagar';
    }
    document.getElementById('modalContaPagar').classList.add('active');
}

document.addEventListener('DOMContentLoaded', function() {
    // carregarDashboard(); // Comentado pois a função pode não existir neste contexto
});

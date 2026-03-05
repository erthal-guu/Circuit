
document.addEventListener('DOMContentLoaded', function() {

});
function abrirModalNovaContaReceber() {
    document.getElementById('formContaReceber').reset();
    document.getElementById('contaReceberId').value = '';
    document.getElementById('modalContaReceber').classList.add('active');
}

function fecharModalContaReceber() {
    document.getElementById('modalContaReceber').classList.remove('active');
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
    
    if (receberCliente) receberCliente.value = cliente;
    if (receberValor) receberValor.value = valor;
    if (receberValorRecebido) receberValorRecebido.value = valorRecebido;
    if (receberId) receberId.value = id;
    
    const hoje = new Date().toISOString().split('T')[0];
    
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
    
    if (origem === 'ORDEM_SERVICO') {
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
            formaPagamentoRow.style.display = 'none';
        }
        if (dataPagamentoRow) {
            dataPagamentoRow.style.display = 'flex';
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
    document.getElementById('formReceber').reset();

    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainerReceber');
    const parcelasContainer = document.getElementById('parcelasContainerReceber');
    
    if (condicaoPagamentoContainer) {
        condicaoPagamentoContainer.style.display = 'none';
        condicaoPagamentoContainer.style.opacity = '0';
        condicaoPagamentoContainer.style.transform = 'translateY(-20px)';
        condicaoPagamentoContainer.classList.remove('modal-animate-show');
    }
    
    if (parcelasContainer) {
        parcelasContainer.style.display = 'none';
    }
}

function handleFormaPagamentoChangeReceber() {
    const formaPagamento = document.getElementById('receberFormaPagamento');
    const condicaoPagamentoContainer = document.getElementById('condicaoPagamentoContainerReceber');
    const parcelasContainer = document.getElementById('parcelasContainerReceber');

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

function confirmarReceber() {
    const id = document.getElementById('receberId').value;
    const valorRecebido = document.getElementById('receberValorRecebido').value;
    const dataPagamento = document.getElementById('receberDataPagamento').value;
    const formaPagamento = document.getElementById('receberFormaPagamento').value;
    const condicaoPagamento = document.querySelector('input[name="condicaoPagamentoReceber"]:checked')?.value;
    const numeroParcelas = document.getElementById('parcelasReceber').value;

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

    fetch('/contas-receber/receber', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (response.ok) {
            window.location.href = '/contas-receber';
        } else {
            alert('Erro ao receber pagamento. Tente novamente.');
        }
    })
    .catch(error => {
        console.error('Erro:', error);
        alert('Erro ao receber pagamento. Tente novamente.');
    });
}

function formatarData(data) {
    if (!data) return '-';
    return new Date(data).toLocaleDateString('pt-BR');
}

function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function getBadgeClass(status) {
    switch(status) {
        case 'PENDENTE': return 'badge-warning';
        case 'VENCIDO': return 'badge-danger';
        case 'RECEBIDO': return 'badge-success';
        default: return 'badge-secondary';
    }
}

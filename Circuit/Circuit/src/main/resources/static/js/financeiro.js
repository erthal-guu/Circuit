let fluxoCaixaChart = null;
let contasChart = null;

document.addEventListener('DOMContentLoaded', function() {
    atualizarDashboard();
});

function atualizarDashboard() {
    const dataInicio = document.getElementById('dataInicio').value;
    const dataFim = document.getElementById('dataFim').value;

    fetch(`/api/financeiro/dashboard?dataInicio=${dataInicio}&dataFim=${dataFim}`)
        .then(response => response.json())
        .then(data => {
            atualizarCards(data);
            atualizarTabelas(data);
            renderizarGraficos(data);
        })
        .catch(error => console.error('Erro ao atualizar dashboard:', error));
}

function atualizarCards(data) {
    document.querySelector('.receita-total').textContent = 'R$ ' + formatarMoeda(data.receitaTotal);
    document.querySelector('.despesa-total').textContent = 'R$ ' + formatarMoeda(data.despesaTotal);
    document.querySelector('.saldo-liquido').textContent = 'R$ ' + formatarMoeda(data.saldoLiquido);
    document.querySelector('.lucro-liquido').textContent = 'R$ ' + formatarMoeda(data.lucroLiquido);
}

function atualizarTabelas(data) {
    const tbodyReceber = document.querySelector('#tabelaContasReceber tbody');
    tbodyReceber.innerHTML = '';

    data.contasReceberProximos.forEach(conta => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${conta.cliente?.nome || '-'}</td>
            <td>R$ ${formatarMoeda(conta.valor)}</td>
            <td>${formatarData(conta.dataVencimento)}</td>
        `;
        tbodyReceber.appendChild(tr);
    });

    const tbodyPagar = document.querySelector('#tabelaContasPagar tbody');
    tbodyPagar.innerHTML = '';

    data.contasPagarProximos.forEach(conta => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${conta.fornecedor?.nome || '-'}</td>
            <td>R$ ${formatarMoeda(conta.valor)}</td>
            <td>${formatarData(conta.dataVencimento)}</td>
        `;
        tbodyPagar.appendChild(tr);
    });
}

function renderizarGraficos(data) {
    renderizarFluxoCaixa(data.fluxoCaixa);
    renderizarContas(data.contasPagarTotal, data.contasReceberTotal);
}

function renderizarFluxoCaixa(fluxoCaixa) {
    const ctx = document.getElementById('fluxoCaixaChart').getContext('2d');

    if (fluxoCaixaChart) {
        fluxoCaixaChart.destroy();
    }

    fluxoCaixaChart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: fluxoCaixa.labels,
            datasets: [{
                label: 'Receita',
                data: fluxoCaixa.receita,
                backgroundColor: 'rgba(16, 185, 129, 0.8)',
                borderColor: 'rgba(16, 185, 129, 1)',
                borderWidth: 1
            }, {
                label: 'Despesa',
                data: fluxoCaixa.despesa,
                backgroundColor: 'rgba(239, 68, 68, 0.8)',
                borderColor: 'rgba(239, 68, 68, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top'
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return 'R$ ' + value.toLocaleString('pt-BR');
                        }
                    }
                }
            }
        }
    });
}

function renderizarContas(contasPagar, contasReceber) {
    const ctx = document.getElementById('contasChart').getContext('2d');

    if (contasChart) {
        contasChart.destroy();
    }

    contasChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Contas a Pagar', 'Contas a Receber'],
            datasets: [{
                data: [contasPagar, contasReceber],
                backgroundColor: [
                    'rgba(239, 68, 68, 0.8)',
                    'rgba(16, 185, 129, 0.8)'
                ],
                borderColor: [
                    'rgba(239, 68, 68, 1)',
                    'rgba(16, 185, 129, 1)'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'top'
                }
            }
        }
    });
}

function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function formatarData(data) {
    if (!data) return '-';
    return new Date(data).toLocaleDateString('pt-BR');
}

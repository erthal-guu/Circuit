document.addEventListener('DOMContentLoaded', function() {
    const ctxFluxoCaixa = document.getElementById('fluxoCaixaChart');
    const ctxDistribuicao = document.getElementById('distribuicaoChart');

    if (ctxFluxoCaixa && ctxDistribuicao) {
        Promise.all([
            fetch('/financeiro/api/fluxo-caixa').then(response => response.json()),
            fetch('/financeiro/api/distribuicao-despesas').then(response => response.json()),
            fetch('/financeiro/api/contas-receber').then(response => response.json()),
            fetch('/financeiro/api/contas-pagar').then(response => response.json())
        ]).then(([fluxoCaixaData, distribuicaoData, contasReceberData, contasPagarData]) => {
            criarGraficoFluxoCaixa(ctxFluxoCaixa, fluxoCaixaData);
            criarGraficoDistribuicao(ctxDistribuicao, distribuicaoData);
            atualizarContasReceber(contasReceberData);
            atualizarContasPagar(contasPagarData);
        }).catch(error => {
            console.error('Erro ao carregar dados dos gráficos:', error);
        });
    }
});

function atualizarContasReceber(data) {
    // Atualizar resumo
    const pendentesElement = document.getElementById('contas-receber-pendentes');
    const vencidasElement = document.getElementById('contas-receber-vencidas');
    const totalElement = document.getElementById('contas-receber-total');
    
    if (pendentesElement) pendentesElement.textContent = data.pendentes || 0;
    if (vencidasElement) vencidasElement.textContent = data.vencidas || 0;
    if (totalElement) totalElement.textContent = formatarMoeda(data.total);
    
    // Atualizar lista de contas
    const listaElement = document.getElementById('contas-receber-list');
    if (listaElement && data.contas && data.contas.length > 0) {
        listaElement.innerHTML = '';
        data.contas.forEach(conta => {
            const itemElement = criarElementoContaReceber(conta);
            listaElement.appendChild(itemElement);
        });
    }
}

function atualizarContasPagar(data) {
    // Atualizar resumo
    const pendentesElement = document.getElementById('contas-pagar-pendentes');
    const vencidasElement = document.getElementById('contas-pagar-vencidas');
    const totalElement = document.getElementById('contas-pagar-total');
    
    if (pendentesElement) pendentesElement.textContent = data.pendentes || 0;
    if (vencidasElement) vencidasElement.textContent = data.vencidas || 0;
    if (totalElement) totalElement.textContent = formatarMoeda(data.total);
    
    // Atualizar lista de contas
    const listaElement = document.getElementById('contas-pagar-list');
    if (listaElement && data.contas && data.contas.length > 0) {
        listaElement.innerHTML = '';
        data.contas.forEach(conta => {
            const itemElement = criarElementoContaPagar(conta);
            listaElement.appendChild(itemElement);
        });
    }
}

function criarElementoContaReceber(conta) {
    const item = document.createElement('div');
    item.className = 'account-item';
    
    const infoDiv = document.createElement('div');
    infoDiv.className = 'account-info';
    
    const avatarDiv = document.createElement('div');
    avatarDiv.className = 'account-avatar';
    avatarDiv.textContent = conta.iniciais || '??';
    
    const detalhesDiv = document.createElement('div');
    
    const nomeP = document.createElement('p');
    nomeP.className = 'account-name';
    nomeP.textContent = conta.nomeCliente || 'Cliente não informado';
    
    const descP = document.createElement('p');
    descP.className = 'account-desc';
    descP.textContent = `${conta.origem || 'Origem'} #${conta.origemId || 'N/A'}`;
    
    detalhesDiv.appendChild(nomeP);
    detalhesDiv.appendChild(descP);
    
    infoDiv.appendChild(avatarDiv);
    infoDiv.appendChild(detalhesDiv);
    
    const amountDiv = document.createElement('div');
    amountDiv.className = 'account-amount account-amount-positive';
    
    const valorP = document.createElement('p');
    valorP.className = 'amount-value';
    valorP.textContent = formatarMoeda(conta.valor);
    
    const dataP = document.createElement('p');
    dataP.className = 'amount-date';
    dataP.textContent = formatarData(conta.dataVencimento);
    
    amountDiv.appendChild(valorP);
    amountDiv.appendChild(dataP);
    
    item.appendChild(infoDiv);
    item.appendChild(amountDiv);
    
    return item;
}

function criarElementoContaPagar(conta) {
    const item = document.createElement('div');
    item.className = 'account-item';
    
    const infoDiv = document.createElement('div');
    infoDiv.className = 'account-info';
    
    const avatarDiv = document.createElement('div');
    avatarDiv.className = 'account-avatar account-avatar-company';
    avatarDiv.textContent = conta.iniciais || '??';
    
    const detalhesDiv = document.createElement('div');
    
    const nomeP = document.createElement('p');
    nomeP.className = 'account-name';
    nomeP.textContent = conta.nomeFornecedor || 'Fornecedor não informado';
    
    const descP = document.createElement('p');
    descP.className = 'account-desc';
    descP.textContent = conta.descricao || 'Sem descrição';
    
    detalhesDiv.appendChild(nomeP);
    detalhesDiv.appendChild(descP);
    
    infoDiv.appendChild(avatarDiv);
    infoDiv.appendChild(detalhesDiv);
    
    const amountDiv = document.createElement('div');
    amountDiv.className = 'account-amount account-amount-negative';
    
    const valorP = document.createElement('p');
    valorP.className = 'amount-value';
    valorP.textContent = formatarMoeda(conta.valor);
    
    const dataP = document.createElement('p');
    dataP.className = 'amount-date';
    dataP.textContent = formatarData(conta.dataVencimento);
    
    amountDiv.appendChild(valorP);
    amountDiv.appendChild(dataP);
    
    item.appendChild(infoDiv);
    item.appendChild(amountDiv);
    
    return item;
}

function formatarMoeda(valor) {
    if (valor === null || valor === undefined) return 'R$ 0,00';
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function formatarData(data) {
    if (!data) return 'Data não informada';
    const dataObj = new Date(data);
    return dataObj.toLocaleDateString('pt-BR');
}

function criarGraficoFluxoCaixa(ctx, data) {
    const meses = data.map(item => item.mes);
    const receitas = data.map(item => item.receitas !== null ? item.receitas : 0);
    const despesas = data.map(item => item.despesas !== null ? item.despesas : 0);

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: meses,
            datasets: [
                {
                    label: 'Receitas',
                    data: receitas,
                    backgroundColor: '#0A315A',
                    borderColor: '#0A315A',
                    borderWidth: 1
                },
                {
                    label: 'Despesas',
                    data: despesas,
                    backgroundColor: '#4A6FA5',
                    borderColor: '#4A6FA5',
                    borderWidth: 1
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.dataset.label || '';
                            if (label) {
                                label += ': ';
                            }
                            if (context.parsed.y !== null) {
                                label += 'R$ ' + context.parsed.y.toFixed(2);
                            }
                            return label;
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return 'R$ ' + value.toFixed(2);
                        }
                    }
                }
            }
        }
    });
}

function criarGraficoDistribuicao(ctx, data) {
    const categorias = data.map(item => item.categoria);
    const valores = data.map(item => item.total !== null ? item.total : 0);

    const cores = [
        '#0A315A',
        '#4A6FA5',
        '#7BA3C9',
        '#A8D0E6',
        '#D4E5F7',
        '#1E3A5F',
        '#2D5A87',
        '#3C7AB5',
        '#5B9AE3',
        '#7AB9F1'
    ];

    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: categorias,
            datasets: [{
                data: valores,
                backgroundColor: cores.slice(0, categorias.length),
                borderColor: '#ffffff',
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'right',
                    labels: {
                        padding: 15,
                        usePointStyle: true,
                        pointStyle: 'circle'
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.label || '';
                            if (label) {
                                label += ': ';
                            }
                            const value = context.parsed;
                            const total = context.dataset.data.reduce((a, b) => a + b, 0);
                            const percentage = total > 0 ? ((value / total) * 100).toFixed(1) : 0;
                            label += 'R$ ' + value.toFixed(2) + ' (' + percentage + '%)';
                            return label;
                        }
                    }
                }
            }
        }
    });
}


document.addEventListener('DOMContentLoaded', function() {
    initTipoRelatorioChange();
    loadUserName();
});

const relatoriosConfig = {
    vendas: [
        {
            id: 'vendas-mensal',
            titulo: 'Análise de Vendas Mensal',
            descricao: 'Relatório analítico com métricas, gráficos e comparações mensais de vendas'
        },
        {
            id: 'vendas-performance',
            titulo: 'Relatório de Performance de Vendas',
            descricao: 'Análise comparativa de performance com indicadores de crescimento e tendências'
        }
    ],
    estoque: [
        {
            id: 'estoque-movimentacao',
            titulo: 'Relatório de Movimentação de Estoque',
            descricao: 'Análise completa de entradas, saídas e giro de estoque por período'
        },
        {
            id: 'estoque-critico',
            titulo: 'Análise de Estoque Crítico',
            descricao: 'Relatório de itens abaixo do mínimo com previsão de reposição e impacto'
        }
    ],
    financeiro: [
        {
            id: 'financeiro-balanco',
            titulo: 'Balanço Financeiro',
            descricao: 'Análise completa de receitas vs despesas com margem de lucro e projeções'
        },
        {
            id: 'financeiro-fluxo',
            titulo: 'Fluxo de Caixa',
            descricao: 'Relatório de movimentação financeira ao longo do tempo com tendências'
        }
    ]
};

let opcaoSelecionada = null;

function initTipoRelatorioChange() {
    const tipoRelatorio = document.getElementById('tipoRelatorio');
    const opcoesRelatorio = document.getElementById('opcoesRelatorio');
    const radioOptions = document.getElementById('radioOptions');
    const btnGerar = document.getElementById('btnGerar');

    tipoRelatorio.addEventListener('change', function() {
        const tipo = this.value;

        opcaoSelecionada = null;
        btnGerar.disabled = true;

        if (tipo && relatoriosConfig[tipo]) {
            opcoesRelatorio.style.display = 'block';
            renderizarOpcoes(relatoriosConfig[tipo]);
        } else {
            opcoesRelatorio.style.display = 'none';
            radioOptions.innerHTML = '';
        }
    });
}

function renderizarOpcoes(opcoes) {
    const radioOptions = document.getElementById('radioOptions');

    radioOptions.innerHTML = opcoes.map(opcao => `
        <label class="radio-option" data-id="${opcao.id}">
            <input type="radio" name="opcaoRelatorio" value="${opcao.id}">
            <div class="radio-indicator"></div>
            <div>
                <div class="radio-label">${opcao.titulo}</div>
                <div class="radio-description">${opcao.descricao}</div>
            </div>
        </label>
    `).join('');

    const radioLabels = radioOptions.querySelectorAll('.radio-option');
    radioLabels.forEach(label => {
        label.addEventListener('click', function() {
            radioLabels.forEach(l => l.classList.remove('selected'));
            this.classList.add('selected');

            const radio = this.querySelector('input[type="radio"]');
            radio.checked = true;

            opcaoSelecionada = {
                id: this.getAttribute('data-id'),
                titulo: this.querySelector('.radio-label').textContent,
                descricao: this.querySelector('.radio-description').textContent
            };

            document.getElementById('btnGerar').disabled = false;
        });
    });
}

function gerarRelatorio() {
    if (!opcaoSelecionada) {
        showNotification('Por favor, selecione uma opção de relatório', 'error');
        return;
    }

    const btnGerar = document.getElementById('btnGerar');
    const textoOriginal = btnGerar.innerHTML;
    btnGerar.innerHTML = `
        <div class="loading-spinner"></div>
        Gerando...
    `;
    btnGerar.disabled = true;

    setTimeout(() => {
        btnGerar.innerHTML = textoOriginal;
        btnGerar.disabled = false;

        mostrarPreview(opcaoSelecionada);
        showNotification('Relatório gerado com sucesso!', 'success');
    }, 1500);
}

function mostrarPreview(opcao) {
    const reportPreview = document.getElementById('reportPreview');
    const previewTitle = document.getElementById('previewTitle');
    const previewDescription = document.getElementById('previewDescription');
    const previewDate = document.getElementById('previewDate');

    previewTitle.textContent = opcao.titulo;
    previewDescription.textContent = opcao.descricao;
    previewDate.textContent = new Date().toLocaleDateString('pt-BR', {
        day: '2-digit',
        month: 'long',
        year: 'numeric'
    });

    reportPreview.style.display = 'block';
    reportPreview.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

function fecharPreview() {
    document.getElementById('reportPreview').style.display = 'none';
}

function baixarPDF() {
    if (!opcaoSelecionada) {
        showNotification('Nenhum relatório selecionado', 'error');
        return;
    }

    showNotification('Iniciando download do PDF...', 'success');
    console.log('Baixando PDF:', opcaoSelecionada.id);

    setTimeout(() => {
        showNotification('PDF baixado com sucesso!', 'success');
    }, 1000);
}

function loadUserName() {
    const userNameElement = document.getElementById('nomeUsuarioExibicao');
    if (userNameElement) {
        const userName = localStorage.getItem('userName');
        userNameElement.textContent = userName || 'Usuário';
    }
}

function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    notification.innerHTML = `
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            ${type === 'success' 
                ? '<polyline points="20 6 9 17 4 12"></polyline>' 
                : '<circle cx="12" cy="12" r="10"></circle><line x1="12" y1="8" x2="12" y2="12"></line><line x1="12" y1="16" x2="12.01" y2="16"></line>'
            }
        </svg>
        <span>${message}</span>
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.style.animation = 'slideOutRight 0.3s ease';
        setTimeout(() => {
            if (document.body.contains(notification)) {
                document.body.removeChild(notification);
            }
        }, 300);
    }, 3000);
}
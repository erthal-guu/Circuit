document.addEventListener("DOMContentLoaded", function () {
    configurarPesquisaLocal('searchTodas', 'tabelaTodas');
    configurarPesquisaLocal('searchAbertas', 'tabelaAbertas');
    configurarPesquisaLocal('searchFinalizadas', 'tabelaFinalizadas');

    const alertas = document.querySelectorAll('.auto-close');
    alertas.forEach(alerta => {
        setTimeout(() => {
            alerta.style.opacity = '0';
            setTimeout(() => { alerta.remove(); }, 500);
        }, 3000);
    });
});

function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(content => {
        content.style.display = 'none';
        content.classList.remove('active');
    });

    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });

    const tabId = 'tab' + tabName.charAt(0).toUpperCase() + tabName.slice(1);
    const targetDiv = document.getElementById(tabId);
    if (targetDiv) {
        targetDiv.style.display = 'block';
        targetDiv.classList.add('active');
    }

    if (event) {
        event.currentTarget.classList.add('active');
    }
}

function filtrarPorData() {
    const abaAtiva = document.querySelector('.tab-content.active');
    if (!abaAtiva) return;

    let idInicio, idFim, idTabela;

    if (abaAtiva.id === 'tabTodas') {
        idInicio = 'dataInicio';
        idFim = 'dataFim';
        idTabela = 'tabelaTodas';
    } else if (abaAtiva.id === 'tabAbertas') {
        idInicio = 'dataInicioAbertas';
        idFim = 'dataFimAbertas';
        idTabela = 'tabelaAbertas';
    } else if (abaAtiva.id === 'tabFinalizadas') {
        idInicio = 'dataInicioFinalizadas';
        idFim = 'dataFimFinalizadas';
        idTabela = 'tabelaFinalizadas';
    }

    const inicioVal = document.getElementById(idInicio).value;
    const fimVal = document.getElementById(idFim).value;

    if (!inicioVal && !fimVal) return;

    const dataInicio = inicioVal ? new Date(inicioVal + 'T00:00:00') : null;
    const dataFim = fimVal ? new Date(fimVal + 'T23:59:59') : null;

    const tabela = document.getElementById(idTabela);
    const linhas = tabela.querySelectorAll('tbody tr');

    linhas.forEach(linha => {
        const celulaData = linha.cells[3].innerText.trim();

        if (!celulaData) return;

        const partes = celulaData.split('/');
        const dataLinha = new Date(partes[2], partes[1] - 1, partes[0]);

        let mostrar = true;

        if (dataInicio && dataLinha < dataInicio) mostrar = false;
        if (dataFim && dataLinha > dataFim) mostrar = false;

        linha.style.display = mostrar ? '' : 'none';
    });
}

function limparFiltroData() {
    const abaAtiva = document.querySelector('.tab-content.active');
    if (!abaAtiva) return;

    if (abaAtiva.id === 'tabTodas') {
        document.getElementById('dataInicio').value = '';
        document.getElementById('dataFim').value = '';
    } else if (abaAtiva.id === 'tabAbertas') {
        document.getElementById('dataInicioAbertas').value = '';
        document.getElementById('dataFimAbertas').value = '';
    } else if (abaAtiva.id === 'tabFinalizadas') {
        document.getElementById('dataInicioFinalizadas').value = '';
        document.getElementById('dataFimFinalizadas').value = '';
    }

    const tabela = abaAtiva.querySelector('table tbody');
    if (tabela) {
        tabela.querySelectorAll('tr').forEach(linha => linha.style.display = '');
    }
}

function configurarPesquisaLocal(inputId, tableId) {
    const input = document.getElementById(inputId);
    const table = document.getElementById(tableId);
    if (!input || !table) return;

    input.addEventListener('keyup', function () {
        const termo = input.value.toLowerCase();
        const linhas = table.querySelectorAll('tbody tr');

        linhas.forEach(linha => {
            const texto = linha.innerText.toLowerCase();
            linha.style.display = texto.includes(termo) ? '' : 'none';
        });
    });
}

const modalOs = document.getElementById('modalNovaOs');
const formOs = document.getElementById('formOs');

function abrirModalNovaOs() {
    formOs.reset();

    const selectAparelho = document.getElementById('osAparelho');
    selectAparelho.innerHTML = '<option value="">Selecione um Cliente primeiro...</option>';
    selectAparelho.disabled = true;

    modalOs.classList.add('active');
    modalOs.style.display = 'flex';
}

function closeModalOs() {
    modalOs.classList.remove('active');
    modalOs.style.display = 'none';
}

function carregarAparelhosCliente(clienteId) {
    const selectAparelho = document.getElementById('osAparelho');

    if (!clienteId) {
        selectAparelho.innerHTML = '<option value="">Selecione um Cliente primeiro...</option>';
        selectAparelho.disabled = true;
        return;
    }

    selectAparelho.innerHTML = '<option value="">Carregando...</option>';
    selectAparelho.disabled = true;

    fetch(`/aparelhos/json/cliente/${clienteId}`)
        .then(response => {
            if (!response.ok) throw new Error('Erro ao buscar aparelhos');
            return response.json();
        })
        .then(aparelhos => {
            selectAparelho.innerHTML = '<option value="">Selecione o Aparelho...</option>';

            if (aparelhos.length === 0) {
                const option = document.createElement('option');
                option.text = "Nenhum aparelho cadastrado para este cliente";
                selectAparelho.add(option);
            } else {
                aparelhos.forEach(ap => {
                    const option = document.createElement('option');
                    option.value = ap.id;
                    option.text = ap.modelo ? ap.modelo.nome : 'Aparelho sem modelo';
                    selectAparelho.add(option);
                });
            }
            selectAparelho.disabled = false;
        })
        .catch(error => {
            console.error(error);
            selectAparelho.innerHTML = '<option value="">Erro ao carregar</option>';
        });
}

const modalStatus = document.getElementById('modalStatus');

function abrirModalStatus(idOs, statusAtual) {
    document.getElementById('statusOsId').value = idOs;

    const badge = document.getElementById('badgeStatusAtual');
    badge.innerText = statusAtual;
    badge.className = 'badge';
    if (statusAtual === 'ABERTA' || statusAtual === 'EM_ANALISE') badge.classList.add('badge-active');
    else if (statusAtual === 'FINALIZADA') badge.classList.add('badge-success');
    else if (statusAtual === 'CANCELADA') badge.classList.add('badge-inactive');
    else badge.classList.add('badge-warning');
    modalStatus.classList.add('active');
    modalStatus.style.display = 'flex';
}

function fecharModalStatus() {
    modalStatus.classList.remove('active');
    modalStatus.style.display = 'none';
}

function selecionarStatus(novoStatus) {
    if(confirm('Tem certeza que deseja alterar o status para: ' + novoStatus + '?')) {
        document.getElementById('novoStatus').value = novoStatus;
        document.getElementById('formStatus').submit();
    }
}
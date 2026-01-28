const formAparelho = document.getElementById('aparelhoForm');
const modalAparelho = document.getElementById('aparelhoModal');

document.addEventListener("DOMContentLoaded", function () {
    configurarPesquisa('searchInputAtivos', 'aparelhosTable');
    configurarPesquisa('searchInputInativos', 'aparelhosTableInativos');

    const alertas = document.querySelectorAll('.auto-close');
    alertas.forEach(alerta => {
        setTimeout(() => {
            alerta.style.opacity = '0';
            setTimeout(() => {
                alerta.remove();
            }, 500);
        }, 3000);
    });
});

function openModal() {
    if (modalAparelho) {
        modalAparelho.classList.add('active');
        modalAparelho.style.display = 'flex';
    }
}

function closeModal() {
    if (modalAparelho) {
        modalAparelho.classList.remove('active');
        modalAparelho.style.display = 'none';
    }
}

window.onclick = function(event) {
    if (event.target == modalAparelho) {
        closeModal();
    }
}

function abrirModalNovo() {
    if (formAparelho) {
        formAparelho.reset();
        formAparelho.action = "/aparelhos/cadastrar";
    }
    document.getElementById('apaId').value = '';
    document.getElementById('apaAtivo').value = 'true';
    document.getElementById('modalTitle').innerText = 'Novo Aparelho';
    openModal();
}

function abrirModalEdicao(btn) {
    if (formAparelho) formAparelho.action = "/aparelhos/cadastrar";

    document.getElementById('modalTitle').innerText = 'Editar Aparelho';

    const id = btn.getAttribute('data-id');
    const serie = btn.getAttribute('data-serie');
    const modeloId = btn.getAttribute('data-modeloid');
    const clienteId = btn.getAttribute('data-clienteid');
    const obs = btn.getAttribute('data-obs');
    const ativo = btn.getAttribute('data-ativo');

    document.getElementById('apaId').value = id;
    document.getElementById('apaSerie').value = serie;
    document.getElementById('apaModelo').value = modeloId;
    document.getElementById('apaCliente').value = clienteId;
    document.getElementById('apaObs').value = obs;
    document.getElementById('apaAtivo').value = ativo;

    openModal();
}

function configurarPesquisa(inputId, tableId) {
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

function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(c => c.style.display = 'none');
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    const id = tabName === 'ativos' ? 'tabAtivos' : 'tabInativos';
    const tab = document.getElementById(id);
    if (tab) {
        tab.style.display = 'block';
    }
    if (event) {
        event.currentTarget.classList.add('active');
    }
}
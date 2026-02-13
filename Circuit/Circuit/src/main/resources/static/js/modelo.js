const formModelo = document.getElementById('modeloForm');
const modalModelo = document.getElementById('modeloModal');

document.addEventListener("DOMContentLoaded", function () {
    configurarPesquisa('searchInputAtivos', 'modelosTable');
    configurarPesquisa('searchInputInativos', 'modelosTableInativos');

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
    if (modalModelo) {
        modalModelo.classList.add('active');
        modalModelo.style.display = 'flex';
    }
}

function closeModal() {
    if (modalModelo) {
        modalModelo.classList.remove('active');
        modalModelo.style.display = 'none';
    }
}

function abrirModalNovo() {
    if (formModelo) {
        formModelo.reset();
        formModelo.action = "/modelos/cadastrar";
    }
    document.getElementById('modId').value = '';
    document.getElementById('modAtivo').value = 'true';
    document.getElementById('modalTitle').innerText = 'Novo Modelo';
    openModal();
}

function abrirModalEdicao(btn) {
    if (formModelo) formModelo.action = "/modelos/editar";

    document.getElementById('modalTitle').innerText = 'Editar Modelo';

    const id = btn.getAttribute('data-id');
    const nome = btn.getAttribute('data-nome');
    const marcaId = btn.getAttribute('data-marcaid');
    const ativo = btn.getAttribute('data-ativo');

    document.getElementById('modId').value = id;
    document.getElementById('modNome').value = nome;
    document.getElementById('modMarca').value = marcaId;
    document.getElementById('modAtivo').value = ativo;

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

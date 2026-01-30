const modalServico = document.getElementById('modalServico');
const formServico = modalServico.querySelector('form');

document.addEventListener("DOMContentLoaded", function () {
    configurarPesquisaLocal('searchAtivos', 'tabAtivos');
    configurarPesquisaLocal('searchInativos', 'tabInativos');

    const alertas = document.querySelectorAll('.auto-close');
    alertas.forEach(alerta => {
        setTimeout(() => {
            alerta.style.opacity = '0';
            setTimeout(() => { alerta.remove(); }, 500);
        }, 3000);
    });
});

function abrirModalServico() {
    formServico.reset();
    formServico.action = "/servicos/cadastrar";
    document.getElementById('modalTitle').innerText = 'Novo Serviço';

    const inputId = document.getElementById('servId');
    if(inputId) inputId.value = '';

    modalServico.classList.add('active');
    modalServico.style.display = 'flex';
}

function fecharModalServico() {
    modalServico.classList.remove('active');
    modalServico.style.display = 'none';
    formServico.reset();
}

function abrirModalEdicao(btn) {
    formServico.action = "/servicos/editar";
    document.getElementById('modalTitle').innerText = 'Editar Serviço';

    const id = btn.getAttribute('data-id');
    const nome = btn.getAttribute('data-nome');
    const valor = btn.getAttribute('data-valor');
    const ativo = btn.getAttribute('data-ativo');

    document.getElementById('servId').value = id;
    document.querySelector('input[name="nome"]').value = nome;
    document.querySelector('input[name="valorBase"]').value = valor;

    const selectAtivo = document.querySelector('select[name="ativo"]');
    if(selectAtivo) selectAtivo.value = ativo;

    const pecasIdsRaw = btn.getAttribute('data-pecas');

    formServico.querySelectorAll('input[name="pecasId"]').forEach(cb => cb.checked = false);

    if (pecasIdsRaw) {
        const pecasIds = JSON.parse(pecasIdsRaw);
        pecasIds.forEach(pecaId => {
            const checkbox = document.getElementById('peca-' + pecaId);
            if (checkbox) checkbox.checked = true;
        });
    }

    modalServico.classList.add('active');
    modalServico.style.display = 'flex';
}

function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.remove('active');
    });
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    document.getElementById('tab' + tabName.charAt(0).toUpperCase() + tabName.slice(1)).classList.add('active');
    event.currentTarget.classList.add('active');
}

function configurarPesquisaLocal(inputId, containerId) {
    const input = document.getElementById(inputId);
    const container = document.getElementById(containerId);
    if (!input || !container) return;

    input.addEventListener('keyup', function () {
        const termo = input.value.toLowerCase();
        const linhas = container.querySelectorAll('tbody tr');
        linhas.forEach(linha => {
            const texto = linha.innerText.toLowerCase();
            linha.style.display = texto.includes(termo) ? '' : 'none';
        });
    });

}
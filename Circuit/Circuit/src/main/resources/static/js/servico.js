const modal = document.getElementById('modalServico');

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
function abrirModalServico() {
    const forms = modal.querySelectorAll('form');
    forms.forEach(form => form.reset()); // Limpa o formulário
    modal.classList.add('active');
    modal.style.display = 'flex';
}

function fecharModalServico() {
    modal.classList.remove('active');
    modal.style.display = 'none';
}
document.addEventListener("DOMContentLoaded", function () {
    const alertas = document.querySelectorAll('.auto-close');
    alertas.forEach(alerta => {
        setTimeout(() => {
            alerta.style.opacity = '0';
            setTimeout(() => { alerta.remove(); }, 500);
        }, 3000);
    });
});
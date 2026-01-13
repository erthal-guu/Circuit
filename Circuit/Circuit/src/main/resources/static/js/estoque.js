
const modal = document.getElementById('estoqueModal');
const formEstoque = document.getElementById('estoqueForm');

function switchTab(tabName) {
    document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    if (tabName === 'ativos') {
        document.getElementById('tabAtivos').classList.add('active');
    } else {
        document.getElementById('tabInativos').classList.add('active');
    }
    if (event) {
        event.currentTarget.classList.add('active');
    }
}
function openModal() {
    if (modal) {
        modal.classList.add('active');
    }
}
function closeModal() {
    if (modal) {
        modal.classList.remove('active');
    }
    if (formEstoque) formEstoque.reset();
    const preview = document.querySelector('.image-upload-label img');
    if (preview) preview.remove();
    document.getElementById('fileNameDisplay').innerText = "Nenhum arquivo selecionado";
}
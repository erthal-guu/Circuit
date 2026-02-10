
function gerarCodigoHex() {
    const input = document.getElementById("numeroPedido");
    const random = Math.floor(Math.random() * 65535).toString(16).toUpperCase();
    input.style.transition = "background-color 0.2s";
    input.style.backgroundColor = "white";
    input.value = `PO-${random.padStart(4, '0')}`;
    setTimeout(() => {
        input.style.backgroundColor = "";
    }, 300);
}
document.addEventListener("DOMContentLoaded", function() {
    const input = document.getElementById("numeroPedido");
    if (input && (input.value === "" || input.value === "AUTO-00123")) {
        gerarCodigoHex();
    }
});

function atualizarDadosFornecedor(select) {
    const optionSelecionada = select.options[select.selectedIndex];
    const inputCnpj = document.getElementById("inputCnpjFornecedor");
    const cnpj = optionSelecionada.getAttribute("data-cnpj");
    if (cnpj) {
        inputCnpj.value = formatarCNPJ(cnpj);
        inputCnpj.style.backgroundColor = "#fff";
        inputCnpj.style.borderColor = "#10b981";
    } else {
        inputCnpj.value = "";
        inputCnpj.style.backgroundColor = "#f1f5f9";
        inputCnpj.style.borderColor = "#e2e8f0";
    }
}
function formatarCNPJ(v) {
    if(!v) return "";
    v=v.replace(/\D/g,"");
    if(v.length !== 14) return v;
    return v.replace(/^(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, "$1.$2.$3/$4-$5");
}
function switchTab(tab) {
    const viewLista = document.getElementById('view-lista');
    const viewForm = document.getElementById('view-form');
    const btnLista = document.getElementById('btn-tab-lista');
    const btnForm = document.getElementById('btn-tab-form');
    const btnSalvar = document.getElementById('btn-salvar-global');
    const mainTitle = document.getElementById('main-title');

    if (tab === 'lista') {
        viewLista.style.display = 'block';
        viewForm.style.display = 'none';
        btnLista.classList.add('active');
        btnForm.classList.remove('active');
        btnSalvar.style.display = 'none';
        mainTitle.innerText = 'Pedidos';
    } else {
        viewLista.style.display = 'none';
        viewForm.style.display = 'block';
        btnLista.classList.remove('active');
        btnForm.classList.add('active');
        btnSalvar.style.display = 'flex';
        mainTitle.innerText = 'Novo Pedido';

        if(!document.getElementById('numeroPedido').value) {
            gerarCodigoHex();
        }
    }
}
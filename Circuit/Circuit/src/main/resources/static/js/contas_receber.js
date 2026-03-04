
document.addEventListener('DOMContentLoaded', function() {

});
function abrirModalNovaContaReceber() {
    document.getElementById('formContaReceber').reset();
    document.getElementById('contaReceberId').value = '';
    document.getElementById('modalContaReceber').classList.add('active');
}

function fecharModalContaReceber() {
    document.getElementById('modalContaReceber').classList.remove('active');
}

function abrirModalReceber(id, valor, cliente) {
    document.getElementById('receberId').value = id;
    document.getElementById('receberCliente').value = cliente;
    document.getElementById('receberValor').value = valor;
    document.getElementById('receberValorRecebido').value = valor;
    const hoje = new Date().toISOString().split('T')[0];
    document.getElementById('receberDataPagamento').value = hoje;
    document.getElementById('receberFormaPagamento').value = '';
    
    document.getElementById('modalReceber').classList.add('active');
}

function fecharModalReceber() {
    document.getElementById('modalReceber').classList.remove('active');
    document.getElementById('formReceber').reset();
}

function formatarData(data) {
    if (!data) return '-';
    return new Date(data).toLocaleDateString('pt-BR');
}

function formatarMoeda(valor) {
    return valor.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });
}

function getBadgeClass(status) {
    switch(status) {
        case 'PENDENTE': return 'badge-warning';
        case 'VENCIDO': return 'badge-danger';
        case 'RECEBIDO': return 'badge-success';
        default: return 'badge-secondary';
    }
}

const modal = document.getElementById('estoqueModal');
const formEstoque = document.getElementById('estoqueForm');

function switchTab(tabName){
document.querySelectorAll('.tab-content').forEach(c => c.classlist.remove('active'));
document.querySelectorAll('.tab-btn').forEach(b => b.classlist.remove('active'));

}
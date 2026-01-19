const formCli = document.getElementById('clienteForm');
const modalCli = document.getElementById('clienteModal');

document.addEventListener("DOMContentLoaded", function () {
    aplicarMascaras();
    const cepInput = document.getElementById('cliCep');
    if (cepInput) {
        cepInput.addEventListener('blur', function() {
            buscarCep(this.value);
        });
    }
    configurarPesquisaLocal('searchInputAtivos', 'clienteTable');
    configurarPesquisaLocal('searchInputInativos', 'clienteTableInativos');
});

function openModal() {
    modalCli.classList.add('active');
    modalCli.style.display = 'flex';
}

function closeModal() {
    modalCli.classList.remove('active');
    modalCli.style.display = 'none';
    formCli.reset();
}

function abrirModalNovo() {
    formCli.reset();
    formCli.action = "/clientes/cadastrar";
    document.getElementById('cliId').value = '';
    document.getElementById('modalTitle').innerText = 'Novo Cliente';
    openModal();
}

function abrirModalEdicao(btn) {
    formCli.action = "/clientes/editar";
    formCli.method = "POST";
    document.getElementById('modalTitle').innerText = 'Editar Cliente';

    const campos = ['id', 'nome', 'cpf', 'email', 'telefone', 'cep', 'logradouro', 'numero', 'bairro', 'cidade', 'estado', 'ativo'];

    campos.forEach(campo => {
        const valor = btn.getAttribute(`data-${campo}`);
        const idInput = campo === 'id' ? 'cliId' : `cli${campo.charAt(0).toUpperCase() + campo.slice(1)}`;
        const input = document.getElementById(idInput);
        if (input) {
            input.value = valor || '';
            input.dispatchEvent(new Event('input'));
        }
    });

    openModal();
}

function buscarCep(valor) {
    const cep = valor.replace(/\D/g, '');
    if (cep.length !== 8) return;
    document.getElementById('cliLogradouro').value = '...';
    document.getElementById('cliBairro').value = '...';
    document.getElementById('cliCidade').value = '...';
    document.getElementById('cliEstado').value = '...';

    fetch(`/clientes/consulta-cep-clientes/${cep}`)
        .then(response => {
            if (!response.ok) throw new Error('Erro na busca');
            return response.json();
        })
        .then(dados => {
            if (dados.erro) {
                alert('CEP não encontrado!');
                limparCamposCep();
                return;
            }
            document.getElementById('cliLogradouro').value = dados.logradouro || '';
            document.getElementById('cliBairro').value = dados.bairro || '';
            document.getElementById('cliCidade').value = dados.localidade || '';
            document.getElementById('cliEstado').value = dados.uf || '';
            document.getElementById('cliNumero').focus();
        })
        .catch(error => {
            console.error('Erro:', error);
            limparCamposCep();
        });
}

function limparCamposCep() {
    document.getElementById('cliLogradouro').value = '';
    document.getElementById('cliBairro').value = '';
    document.getElementById('cliCidade').value = '';
    document.getElementById('cliEstado').value = '';
}

function aplicarMascaras() {
    const masks = {
        cliCpf: v => v.replace(/\D/g, '').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d)/, '$1.$2').replace(/(\d{3})(\d{1,2})$/, '$1-$2').slice(0, 14),
        cliTelefone: v => v.replace(/\D/g, '').replace(/^(\d{2})(\d)/g, '($1) $2').replace(/(\d{5})(\d)/, '$1-$2').slice(0, 15),
        cliCep: v => v.replace(/\D/g, '').replace(/(\d{5})(\d)/, '$1-$2').slice(0, 9)
    };

    Object.keys(masks).forEach(id => {
        const el = document.getElementById(id);
        if (el) el.addEventListener('input', e => e.target.value = masks[id](e.target.value));
    });
}

function configurarPesquisaLocal(inputId, tableId) {
    const input = document.getElementById(inputId);
    const table = document.getElementById(tableId);
    if (!input || !table) return;

    input.addEventListener('keyup', function () {
        const termo = input.value.toLowerCase();
        const linhas = table.querySelectorAll('tbody tr');
        linhas.forEach(linha => {
            linha.style.display = linha.innerText.toLowerCase().includes(termo) ? '' : 'none';
        });
    });
}

function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(c => c.style.display = 'none');
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
    const id = tabName === 'ativos' ? 'tabAtivos' : 'tabInativos';
    document.getElementById(id).style.display = 'block';
    if (event) event.currentTarget.classList.add('active');
}
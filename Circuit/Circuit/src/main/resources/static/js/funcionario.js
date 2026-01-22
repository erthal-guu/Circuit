const formFunc = document.getElementById('funcionarioForm');
const modalFunc = document.getElementById('funcionarioModal');
const msgDiv = document.getElementById('mensagem');

document.addEventListener("DOMContentLoaded", function () {
    configurarPesquisa('searchInputAtivos', 'funcionarioTable');
    configurarPesquisa('searchInputInativos', 'funcionarioTableInativos');
    aplicarMascaras();
    const cepInput = document.getElementById('funcCep');
    if (cepInput) {
        cepInput.addEventListener('blur', function() {
            buscarCep(this.value);
        });
    }
});

function openModal() {
    if (modalFunc) {
        modalFunc.classList.add('active');
        modalFunc.style.display = 'flex';
    }
}

function closeModal() {
    if (modalFunc) {
        modalFunc.classList.remove('active');
        modalFunc.style.display = 'none';
    }
}

function abrirModalNovo() {
    if (formFunc) {
        formFunc.reset();
        formFunc.action = "/funcionarios/cadastrar";
    }
    document.getElementById('funcId').value = '';
    document.getElementById('modalTitle').innerText = 'Novo Funcionário';
    openModal();
}

function abrirModalEdicao(btn) {
    if (formFunc) formFunc.action = "/funcionarios/editar";

    document.getElementById('modalTitle').innerText = 'Editar Funcionário';

    const campos = [
        'id', 'nome', 'cpf', 'cargo', 'email', 'telefone',
        'cep', 'logradouro', 'numero', 'bairro', 'localidade', 'uf', 'dataAdmissao', 'ativo'
    ];

    campos.forEach(campo => {
        const valor = btn.getAttribute(`data-${campo}`);
        const idCampo = campo === 'id' ? 'funcId' : `func${campo.charAt(0).toUpperCase() + campo.slice(1)}`;
        const input = document.getElementById(idCampo);
        if (input) input.value = valor || '';
    });

    openModal();
}

function buscarCep(valor) {
    const cep = valor.replace(/\D/g, '');
    if (cep.length !== 8) return;

    fetch(`/funcionarios/consulta-cep-funcionarios/${cep}`)
        .then(response => response.json())
        .then(dados => {
            if (dados.erro) return;
            document.getElementById('funcLogradouro').value = dados.logradouro || '';
            document.getElementById('funcBairro').value = dados.bairro || '';
            document.getElementById('funcCidade').value = dados.localidade || '';
            document.getElementById('funcEstado').value = dados.uf || '';
            document.getElementById('funcNumero').focus();
        })
        .catch(error => console.error(error));
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
    document.getElementById(id).style.display = 'block';
    event.currentTarget.classList.add('active');
}
function aplicarMascaras() {
    const masks = {
        funcCpf: v => v.replace(/\D/g, '')
            .replace(/(\d{3})(\d)/, '$1.$2')
            .replace(/(\d{3})(\d)/, '$1.$2')
            .replace(/(\d{3})(\d{1,2})$/, '$1-$2')
            .slice(0, 14),

        funcTelefone: v => {
            v = v.replace(/\D/g, '').slice(0, 11);
            return v.length > 10
                ? v.replace(/^(\d{2})(\d{5})(\d{4})/, '($1) $2-$3')
                : v.replace(/^(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3');
        },

        funcCep: v => v.replace(/\D/g, '')
            .replace(/^(\d{5})(\d)/, '$1-$2')
            .slice(0, 9)
    };

    Object.keys(masks).forEach(id => {
        const el = document.getElementById(id);
        if (el) {
            el.addEventListener('input', e => {
                e.target.value = masks[id](e.target.value);
            });
        }
    });
}
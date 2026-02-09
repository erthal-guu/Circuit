const formForn = document.getElementById('fornecedorForm');
const modalForn = document.getElementById('fornecedorModal');

document.addEventListener("DOMContentLoaded", function () {
    aplicarMascaras();
    const cepInput = document.getElementById('fornCep');
    if (cepInput) {
        cepInput.addEventListener('blur', function() {
            buscarCep(this.value);
        });
    }
    configurarPesquisaLocal('searchInputAtivos', 'fornecedorTable');
    configurarPesquisaLocal('searchInputInativos', 'fornecedorTableInativos');
});

function openModal() {
    modalForn.classList.add('active');
    modalForn.style.display = 'flex';
}

function closeModal() {
    modalForn.classList.remove('active');
    modalForn.style.display = 'none';
    formForn.reset();
}

function abrirModalNovo() {
    formForn.reset();
    formForn.action = "/fornecedores/cadastrar";
    document.getElementById('fornId').value = '';
    document.getElementById('modalTitle').innerText = 'Novo Fornecedor';
    openModal();
}

function abrirModalEdicao(btn) {
    formForn.action = "/fornecedores/editar";
    formForn.method = "POST";
    document.getElementById('modalTitle').innerText = 'Editar Fornecedor';
    const campos = ['id', 'nomeFantasia', 'razaoSocial', 'cnpj','tipo', 'email', 'telefone', 'cep', 'logradouro', 'numero', 'bairro', 'cidade', 'estado', 'ativo'];

    campos.forEach(campo => {
        const valor = btn.getAttribute(`data-${campo}`);
        const idInput = campo === 'id' ? 'fornId' : `forn${campo.charAt(0).toUpperCase() + campo.slice(1)}`;
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
    document.getElementById('fornLogradouro').value = '...';
    document.getElementById('fornBairro').value = '...';
    document.getElementById('fornCidade').value = '...';
    document.getElementById('fornEstado').value = '...';
    fetch(`/fornecedores/consulta-cep-fornecedor/${cep}`)
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
            document.getElementById('fornLogradouro').value = dados.logradouro || '';
            document.getElementById('fornBairro').value = dados.bairro || '';
            document.getElementById('fornCidade').value = dados.localidade || '';
            document.getElementById('fornEstado').value = dados.uf || '';
            document.getElementById('fornNumero').focus();
        })
        .catch(error => {
            console.error('Erro:', error);
            limparCamposCep();
        });
}

function aplicarMascaras() {
    const masks = {
        fornCnpj: v => v.replace(/\D/g, '').replace(/^(\d{2})(\d)/, '$1.$2').replace(/^(\d{2})\.(\d{3})(\d)/, '$1.$2.$3').replace(/\.(\d{3})(\d)/, '.$1/$2').replace(/(\d{4})(\d)/, '$1-$2').slice(0, 18),
        fornTelefone: v => v.replace(/\D/g, '').replace(/^(\d{2})(\d)/g, '($1) $2').replace(/(\d{5})(\d)/, '$1-$2').slice(0, 15),
        fornCep: v => v.replace(/\D/g, '').replace(/(\d{5})(\d)/, '$1-$2').slice(0, 9)
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
document.addEventListener("DOMContentLoaded", function() {
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
function abrirModalVincular() {
    document.getElementById('formVinculo').reset();
    document.getElementById('areaVinculoDinamica').style.display = 'none';
    document.getElementById('btnSalvarVinculo').disabled = true;
    document.getElementById('modalVincularItens').style.display = 'flex';

}

function fecharModalVinculo() {
    document.getElementById('modalVincularItens').style.display = 'none';
}
function carregarItensPorTipoFornecedor(selectElement) {
    const containerArea = document.getElementById('areaVinculoDinamica');
    const listaContainer = document.getElementById('listaItensParaVincular');
    const loading = document.getElementById('loadingVinculo');
    const tituloLabel = document.getElementById('tituloListaItens');
    const msgNenhum = document.getElementById('msgNenhumItem');
    const btnSalvar = document.getElementById('btnSalvarVinculo');
    const campoTipoHidden = document.getElementById('tipoVinculoAtual');

    const selectedOption = selectElement.options[selectElement.selectedIndex];
    const tipoOriginal = selectedOption.getAttribute('data-tipo');

    listaContainer.innerHTML = '';
    containerArea.style.display = 'none';
    msgNenhum.style.display = 'none';
    btnSalvar.disabled = true;

    if (!selectElement.value || !tipoOriginal) {
        campoTipoHidden.value = '';
        return;
    }

    campoTipoHidden.value = tipoOriginal;

    const tipoNormalizado = tipoOriginal.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, "");

    let urlFetch = '';
    if (tipoNormalizado === 'pecas') {
        urlFetch = '/estoquePecas/todas-disponiveis';
        tituloLabel.innerText = 'Selecione as Peças que ele fornece:';
    } else if (tipoNormalizado === 'produtos') {
        urlFetch = '/estoque/todos-disponiveis';
        tituloLabel.innerText = 'Selecione os Produtos que ele fornece:';
    } else {
        campoTipoHidden.value = '';
        return;
    }

    containerArea.style.display = 'block';
    loading.style.display = 'flex';

    fetch(urlFetch)
        .then(response => {
            if (!response.ok) throw new Error();
            return response.json();
        })
        .then(data => {
            loading.style.display = 'none';

            if (!data || data.length === 0) {
                msgNenhum.style.display = 'block';
                return;
            }

            data.forEach(item => {
                const labelCard = document.createElement('label');
                labelCard.className = 'checkbox-item-card';

                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.name = 'itensIds';
                checkbox.value = item.id;

                checkbox.addEventListener('change', function() {
                    labelCard.classList.toggle('selected', this.checked);
                    const algumMarcado = listaContainer.querySelectorAll('input[type="checkbox"]:checked').length > 0;
                    btnSalvar.disabled = !algumMarcado;
                });

                const spanNome = document.createElement('span');
                spanNome.innerText = item.nome;

                labelCard.appendChild(checkbox);
                labelCard.appendChild(spanNome);
                listaContainer.appendChild(labelCard);
            });
        })
        .catch(() => {
            loading.style.display = 'none';
            msgNenhum.innerText = 'Erro ao carregar dados do servidor.';
            msgNenhum.style.display = 'block';
        });
}
function switchTabVincular(tabName, event) {
    document.querySelectorAll('.tab-content-modal').forEach(c => c.style.display = 'none');
    document.querySelectorAll('.tab-btn-modal').forEach(b => b.classList.remove('active'));
    const id = tabName === 'vincular' ? 'tabVincular' : 'tabVinculados';
    const alvo = document.getElementById(id);
    if (alvo) {
        alvo.style.display = 'block';
    } else {
        console.error("Erro: Não encontrei a aba com ID:", id);
    }
    if (event && event.currentTarget) {
        event.currentTarget.classList.add('active');
    }
}
function carregarItensVinculados(idFornecedor) {
    const area = document.getElementById('areaItensVinculados');
    const loading = document.getElementById('loadingVinculados');
    const list = document.getElementById('listaItensVinculados');
    const msgEmpty = document.getElementById('msgNenhumVinculado');

    // Pega o tipo do select selecionado
    const select = document.getElementById('selectFornecedorVinculados');
    const selectedOption = select.options[select.selectedIndex];
    const tipoOriginal = selectedOption.getAttribute('data-tipo');

    // Reseta visual
    area.style.display = 'none';
    list.innerHTML = '';
    msgEmpty.style.display = 'none';

    if (!idFornecedor || !tipoOriginal) return;

    const tipoNorm = tipoOriginal.toLowerCase().normalize('NFD').replace(/[\u0300-\u036f]/g, "");

    area.style.display = 'block';
    loading.style.display = 'flex';

    // Chama Endpoint Java
    const url = `/fornecedores/json/${idFornecedor}/itens-vinculados?tipo=${tipoNorm}`;

    fetch(url)
        .then(res => {
            if(!res.ok) throw new Error("Erro ao buscar vinculados");
            return res.json();
        })
        .then(data => {
            loading.style.display = 'none';
            if (!data || data.length === 0) {
                msgEmpty.style.display = 'block';
                return;
            }

            data.forEach(item => {
                const div = document.createElement('div');
                div.className = 'card-vinculado';
                div.style.display = 'flex';
                div.style.justifyContent = 'space-between';
                div.style.alignItems = 'center';
                div.style.padding = '10px';
                div.style.borderBottom = '1px solid #eee';

                div.innerHTML = `
                    <span style="font-weight: 500;">${item.nome}</span>
                    <button type="button" class="btn-desvincular" 
                        style="background: #fee2e2; color: #dc2626; border: none; padding: 5px 10px; border-radius: 10px; cursor: pointer;"
                        onclick="desvincularItem(${idFornecedor}, ${item.id}, '${tipoNorm}', this)">
                        Desvincular
                    </button>
                `;
                list.appendChild(div);
            });
        })
        .catch(err => {
            console.warn(err);
            loading.style.display = 'none';
            msgEmpty.innerText = "Não foi possível carregar os itens vinculados.";
            msgEmpty.style.display = 'block';
        });
}

function desvincularItem(fornId, itemId, tipo, btnElement) {
    if(!confirm("Remover este vínculo?")) return;

    const row = btnElement.closest('div');
    btnElement.innerText = '...';
    btnElement.disabled = true;

    fetch('/fornecedores/desvincular-item', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ fornecedorId: fornId, itemId: itemId, tipo: tipo })
    })
        .then(res => {
            if(res.ok) {
                row.remove();
                const list = document.getElementById('listaItensVinculados');
                if (list.children.length === 0) {
                    document.getElementById('msgNenhumVinculado').style.display = 'block';
                }
            } else {
                alert("Erro ao desvincular.");
                btnElement.innerText = 'Desvincular';
                btnElement.disabled = false;
            }
        })
        .catch(err => {
            console.error(err);
            alert("Erro de conexão.");
            btnElement.innerText = 'Desvincular';
            btnElement.disabled = false;
        });
}
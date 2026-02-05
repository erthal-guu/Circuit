let elMaoDeObra, elTotal, elPorcentagemHidden, elMotivoHidden, formOs;

document.addEventListener("DOMContentLoaded", function () {
    elMaoDeObra = document.getElementById('valorMaoDeObra');
    elTotal = document.getElementById('osValorTotal');
    elPorcentagemHidden = document.getElementById('osPorcentagemDesconto');
    elMotivoHidden = document.getElementById('osMotivoDesconto');
    formOs = document.getElementById('formOs');

    configurarPesquisaLocal('searchTodas', 'tabelaTodas');
    configurarPesquisaLocal('searchAbertas', 'tabelaAbertas');
    configurarPesquisaLocal('searchFinalizadas', 'tabelaFinalizadas');

    document.querySelectorAll('.auto-close').forEach(alerta => {
        setTimeout(() => {
            alerta.style.opacity = '0';
            setTimeout(() => { alerta.remove(); }, 500);
        }, 3000);
    });
});

function switchTab(tabName, event) {
    document.querySelectorAll('.tab-content').forEach(c => {
        c.style.display = 'none';
        c.classList.remove('active');
    });
    document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));

    const tabId = 'tab' + tabName.charAt(0).toUpperCase() + tabName.slice(1);
    const target = document.getElementById(tabId);
    if (target) {
        target.style.display = 'block';
        target.classList.add('active');
    }
    if (event) event.currentTarget.classList.add('active');
}

function calcularTotalOS() {
    if (!elMaoDeObra || !elTotal || !elPorcentagemHidden) return;

    const maoDeObra = parseFloat(elMaoDeObra.value) || 0;
    const porcentagem = parseFloat(elPorcentagemHidden.value) || 0;
    const elPecasOriginal = document.getElementById('valorPecasOriginal');
    const valorPecasOriginal = elPecasOriginal ? parseFloat(elPecasOriginal.value) || 0 : 0;

    let totalPecas = 0;
    const pecasSelecionadas = document.querySelectorAll('input[name="pecasIds"]:checked');

    if (pecasSelecionadas.length > 0) {
        pecasSelecionadas.forEach(cb => {
            totalPecas += parseFloat(cb.getAttribute('data-preco')) || 0;
        });
    } else {
        totalPecas = valorPecasOriginal;
    }

    const subtotal = maoDeObra + totalPecas;
    const valorDesconto = subtotal * (porcentagem / 100);
    const totalFinal = subtotal - valorDesconto;

    elTotal.value = Math.max(0, totalFinal).toFixed(2);

    const infoDiv = document.getElementById('infoFinanceira');
    const resumoTxt = document.getElementById('resumoValores');

    if (infoDiv && resumoTxt) {
        if (subtotal > 0) {
            resumoTxt.innerText = `Subtotal: R$ ${subtotal.toFixed(2)} | Desconto: -R$ ${valorDesconto.toFixed(2)}`;
            infoDiv.style.display = 'block';
        } else {
            infoDiv.style.display = 'none';
        }
    }
}

function abrirModalNovaOs() {
    formOs.reset();
    formOs.action = "/ordens-servico/cadastrar";
    document.getElementById('osId').value = '';

    const elPecasOriginal = document.getElementById('valorPecasOriginal');
    if (elPecasOriginal) elPecasOriginal.value = "0.00";

    const statusInput = document.querySelector('input[name="status"]');
    if (statusInput) statusInput.value = 'ABERTA';

    document.getElementById('modalTitle').innerText = 'Lançar Nova Ordem de Serviço';

    const selectAparelho = document.getElementById('osAparelho');
    selectAparelho.innerHTML = '<option value="">Selecione um Cliente primeiro...</option>';
    selectAparelho.disabled = true;

    document.getElementById('modalNovaOs').style.display = 'flex';
}

function abrirModalEdicao(btn) {
    formOs.action = "/ordens-servico/editar";
    document.getElementById('modalTitle').innerText = 'Editar Ordem de Serviço';

    const id = btn.getAttribute('data-id');
    const status = btn.getAttribute('data-status');
    const vMaoObra = parseFloat(btn.getAttribute('data-valor')) || 0;
    const vTotal = parseFloat(btn.getAttribute('data-total')) || 0;
    const porcentagem = parseFloat(elPorcentagemHidden.value) || 0;
    const pecasIds = btn.getAttribute('data-pecas') || "";

    const fator = 1 - (porcentagem / 100);
    const subtotalOriginal = fator > 0 ? vTotal / fator : vTotal;
    const valorPecasNoBanco = Math.max(0, subtotalOriginal - vMaoObra);

    const elPecasOriginal = document.getElementById('valorPecasOriginal');
    if (elPecasOriginal) elPecasOriginal.value = valorPecasNoBanco.toFixed(2);

    document.getElementById('osId').value = id;

    const statusInput = document.querySelector('input[name="status"]');
    if (statusInput && status) statusInput.value = status;

    document.getElementById('osCliente').value = btn.getAttribute('data-cliente');
    document.getElementById('osFuncionario').value = btn.getAttribute('data-tecnico');
    document.getElementById('osServico').value = btn.getAttribute('data-servico');
    document.getElementById('osDefeito').value = btn.getAttribute('data-defeito');
    document.getElementById('osSenha').value = btn.getAttribute('data-senha') || '';
    document.getElementById('osEstado').value = btn.getAttribute('data-estado') || '';
    document.getElementById('osPrevisao').value = btn.getAttribute('data-previsao') || '';

    document.getElementById('valorMaoDeObra').value = vMaoObra.toFixed(2);
    document.getElementById('osValorTotal').value = vTotal.toFixed(2);

    const infoDiv = document.getElementById('infoFinanceira');
    const resumoTxt = document.getElementById('resumoValores');
    if (infoDiv && resumoTxt) {
        resumoTxt.innerText = `Valor Total salvo: R$ ${vTotal.toFixed(2)}`;
        infoDiv.style.display = 'block';
    }

    carregarAparelhosCliente(btn.getAttribute('data-cliente'), btn.getAttribute('data-aparelho'));
    buscarPecasVinculadas(btn.getAttribute('data-servico'), pecasIds, true);

    document.getElementById('modalNovaOs').style.display = 'flex';
}

function carregarAparelhosCliente(clienteId, aparelhoIdSelecionado = null) {
    const selectAparelho = document.getElementById('osAparelho');
    if (!clienteId) {
        selectAparelho.innerHTML = '<option value="">Selecione um Cliente primeiro...</option>';
        selectAparelho.disabled = true;
        return;
    }

    fetch(`/aparelhos/json/cliente/${clienteId}`)
        .then(response => response.json())
        .then(aparelhos => {
            selectAparelho.innerHTML = '<option value="">Selecione o Aparelho...</option>';
            aparelhos.forEach(ap => {
                const option = document.createElement('option');
                option.value = ap.id;
                option.text = ap.modelo ? ap.modelo.nome : 'Aparelho sem modelo';
                if (aparelhoIdSelecionado && ap.id == aparelhoIdSelecionado) option.selected = true;
                selectAparelho.add(option);
            });
            selectAparelho.disabled = false;
        });
}

function buscarPecasVinculadas(servicoId, idsSelecionadosStr = "", isInitialLoad = false) {
    const container = document.getElementById('containerPecas');
    const lista = document.getElementById('listaCheckboxesPecas');
    lista.innerHTML = '';
    const idsSelecionados = idsSelecionadosStr ? idsSelecionadosStr.split(',').map(Number) : [];

    if (!servicoId) {
        container.style.display = 'none';
        return;
    }

    if (!isInitialLoad) {
        fetch(`/servicos/json/valor/${servicoId}`).then(res => res.json()).then(data => {
            if (data.valorBase && elMaoDeObra) {
                elMaoDeObra.value = data.valorBase.toFixed(2);
                calcularTotalOS();
            }
        });
    }

    fetch(`/servicos/json/${servicoId}/pecas`).then(res => res.json()).then(pecas => {
        if (pecas.length > 0) {
            pecas.forEach(p => {
                const isChecked = idsSelecionados.includes(p.id);
                const item = document.createElement('label');
                item.className = `peca-checkbox-item ${isChecked ? 'selected' : ''}`;
                item.innerHTML = `
                    <div class="peca-info">
                        <input type="checkbox" name="pecasIds" value="${p.id}" data-preco="${p.preco}" 
                               ${isChecked ? 'checked' : ''} onchange="atualizarEstiloPeca(this)">
                        <span class="peca-name">${p.nome}</span>
                        <span class="peca-price">R$ ${p.preco.toFixed(2)}</span>
                    </div>`;
                lista.appendChild(item);
            });
            container.style.display = 'block';
        } else {
            container.style.display = 'none';
        }
    });
}

function atualizarEstiloPeca(input) {
    input.closest('.peca-checkbox-item').classList.toggle('selected', input.checked);
    calcularTotalOS();
}

function abrirModalDesconto() {
    document.getElementById('modalDesconto').style.display = 'flex';
    document.getElementById('inputPorcentagem').value = elPorcentagemHidden.value;
    document.getElementById('inputMotivo').value = elMotivoHidden.value;
}

function confirmarDesconto() {
    const p = document.getElementById('inputPorcentagem').value;
    const m = document.getElementById('inputMotivo').value;
    elPorcentagemHidden.value = p || 0;
    elMotivoHidden.value = m || '';
    calcularTotalOS();
    fecharModalDesconto();
}

function abrirModalStatus(idOs, statusAtual) {
    const inputId = document.getElementById('statusOsId');
    const badge = document.getElementById('badgeStatusAtual');
    const modal = document.getElementById('modalStatus');

    if (inputId && badge && modal) {
        inputId.value = idOs;
        badge.innerText = statusAtual.replace('_', ' '); // Formata o texto

        // Aplica a cor correta baseada no status
        badge.className = 'badge';
        if (statusAtual === 'ABERTA') badge.classList.add('badge-active');
        else if (statusAtual === 'FINALIZADA') badge.classList.add('badge-success');
        else if (statusAtual === 'CANCELADA') badge.classList.add('badge-inactive');
        else if (statusAtual === 'EM_ANALISE') badge.classList.add('badge-analysis');
        else if (statusAtual === 'EM_REPARO') badge.classList.add('badge-repair');
        else badge.classList.add('badge-info');

        modal.style.display = 'flex';
    }
}

function selecionarStatus(novoStatus) {
    const inputNovo = document.getElementById('novoStatus');
    const form = document.getElementById('formStatus');
    if (inputNovo && form) {
        inputNovo.value = novoStatus;
        form.submit();
    }
}

function configurarPesquisaLocal(inputId, tableId) {
    const input = document.getElementById(inputId);
    const table = document.getElementById(tableId);
    if (!input || !table) return;
    input.addEventListener('keyup', () => {
        const termo = input.value.toLowerCase();
        table.querySelectorAll('tbody tr').forEach(linha => {
            linha.style.display = linha.innerText.toLowerCase().includes(termo) ? '' : 'none';
        });
    });
}

function closeModalOs() { document.getElementById('modalNovaOs').style.display = 'none'; }
function fecharModalDesconto() { document.getElementById('modalDesconto').style.display = 'none'; }
function fecharModalStatus() { document.getElementById('modalStatus').style.display = 'none'; }
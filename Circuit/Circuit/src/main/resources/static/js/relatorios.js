document.addEventListener('DOMContentLoaded', () => {
    const inputInicio = document.getElementById('dataInicio');
    const inputFim = document.getElementById('dataFim');
    const tipoSelect = document.getElementById('tipoRelatorio');
    const btnGerar = document.getElementById('btnGerar');

    if (inputInicio && inputFim) {
        const hoje = new Date().toISOString().split('T')[0];
        const amanha = new Date();
        amanha.setDate(amanha.getDate() + 1);
        const amanhaStr = amanha.toISOString().split('T')[0];

        inputInicio.value = hoje;
        inputFim.value = amanhaStr;
    }

    tipoSelect.addEventListener('change', () => {
        btnGerar.disabled = !tipoSelect.value;
    });
});

async function gerarRelatorio() {
    const tipo = document.getElementById('tipoRelatorio').value;
    const inicio = document.getElementById('dataInicio').value;
    const fim = document.getElementById('dataFim').value;
    const btnGerar = document.getElementById('btnGerar');

    try {
        btnGerar.innerText = "Processando...";
        btnGerar.disabled = true;

        const response = await fetch(`/api/relatorios/${tipo}?inicio=${inicio}&fim=${fim}`);
        if (!response.ok) throw new Error("Erro ao buscar dados");

        const dados = await response.json();

        if (dados.length === 0) {
            alert("Nenhum dado encontrado para o período selecionado.");
            return;
        }

        renderizarPDF(tipo, dados, inicio, fim);

    } catch (error) {
        console.error(error);
        alert("Falha ao gerar relatório.");
    } finally {
        btnGerar.innerText = "Gerar Relatório PDF";
        btnGerar.disabled = false;
    }
}

function renderizarPDF(tipo, dados, inicio, fim) {
    const { jsPDF } = window.jspdf || window.jsPDF;
    if (!jsPDF) return;

    const doc = new jsPDF();
    const titulos = {
        vendas: "Relatório: Funcionários que mais venderam",
        estoque: "Relatório: Produtos/Peças mais utilizados",
        financeiro: "Relatório: Clientes com mais compras"
    };

    doc.setFontSize(18);
    doc.setTextColor(37, 99, 235); // Azul Circuit
    doc.text("Circuit - Sistema de Gestão", 14, 20);

    doc.setFontSize(12);
    doc.setTextColor(0, 0, 0);
    doc.text(titulos[tipo] || "Relatório Geral", 14, 30);
    doc.text(`Período: ${inicio} até ${fim}`, 14, 38);

    const headers = [Object.keys(dados[0]).map(key => key.toUpperCase())];
    const rows = dados.map(obj => Object.values(obj));

    doc.autoTable({
        head: headers,
        body: rows,
        startY: 45,
        theme: 'striped',
        headStyles: { fillColor: [37, 99, 235] }
    });

    document.getElementById('reportPreview').style.display = 'block';
    document.getElementById('previewTitle').innerText = titulos[tipo];
    document.getElementById('previewDate').innerText = `Gerado em: ${new Date().toLocaleDateString()}`;

    doc.save(`relatorio_${tipo}_${inicio}.pdf`);
}

function fecharPreview() {
    document.getElementById('reportPreview').style.display = 'none';
}
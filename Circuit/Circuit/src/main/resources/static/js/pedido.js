
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
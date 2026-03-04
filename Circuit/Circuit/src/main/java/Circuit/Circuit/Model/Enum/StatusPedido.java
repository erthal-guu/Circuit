package Circuit.Circuit.Model.Enum;

public enum StatusPedido {
    PENDENTE("📝 Pendente"),
    CONFIRMADO("✅ Confirmado"),
    RECEBIDO("📦 Recebido"),
    CANCELADO("❌ Cancelado");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
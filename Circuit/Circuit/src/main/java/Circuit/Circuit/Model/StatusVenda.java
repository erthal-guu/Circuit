package Circuit.Circuit.Model;

public enum StatusVenda {
    PENDENTE("Pendente"),
    CONCLUIDA("Concluída"),
    CANCELADA("Cancelada"),
    EM_PROCESSAMENTO("Em Processamento");

    private final String descricao;

    StatusVenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

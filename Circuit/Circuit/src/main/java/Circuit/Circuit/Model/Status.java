package Circuit.Circuit.Model;

public enum Status {
    ABERTA("Aberta"),
    EM_ANALISE("Em análise"),
    AGUARDANDO_APROVACAO("Aguardando aprovação"),
    EM_REPARO("Em reparo"),
    AGUARDANDO_PECA("Aguardando Peça"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");

    private final String description;

    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

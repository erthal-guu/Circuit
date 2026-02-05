package Circuit.Circuit.Model;

public enum Cargo {
    ADMIN("Administrador"),
    TECNICO("Técnico"),
    VENDEDOR("Vendedor"),
    GERENTE("Gerente"),
    AUXILIAR("Auxiliar"),
    RECEPCIONISTA("Recepcionista");

    private final String descricao;

    Cargo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
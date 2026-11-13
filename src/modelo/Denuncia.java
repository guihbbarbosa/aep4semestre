package modelo;

// Denuncia de irregularidade. Tambem e uma Publicacao,
// mas em vez de dar nota ela tira pontos da empresa.
public class Denuncia implements Publicacao {

    private int id;
    private int usuarioId;
    private int empresaId;
    private String comentario;
    private String categoria;
    private int gravidade;
    private String statusModeracao;

    public Denuncia(int id, int usuarioId, int empresaId, String comentario,
                    String categoria, int gravidade, String statusModeracao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.empresaId = empresaId;
        this.comentario = comentario;
        this.categoria = categoria;
        this.gravidade = gravidade;
        this.statusModeracao = statusModeracao;
    }

    // mesmo nome de metodo da Avaliacao, mas a conta e outra
    @Override
    public double calcularPontuacao() {
        return gravidade * -0.5;
    }

    @Override
    public String resumo() {
        return "[DENUNCIA] " + categoria + " - gravidade " + gravidade
                + " - impacto " + String.format("%.1f", calcularPontuacao())
                + " - " + statusModeracao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public int getEmpresaId() {
        return empresaId;
    }

    public String getComentario() {
        return comentario;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getGravidade() {
        return gravidade;
    }

    public String getStatusModeracao() {
        return statusModeracao;
    }
}

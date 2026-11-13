package modelo;

// Trabalhador herda de Usuario
public class Trabalhador extends Usuario {

    private String cidadeUf;
    private String setorAtuacao;
    private int tempoExperiencia;

    public Trabalhador(int id, String nome, String email, String senhaHash,
                       String cidadeUf, String setorAtuacao, int tempoExperiencia) {
        super(id, nome, email, senhaHash);
        this.cidadeUf = cidadeUf;
        this.setorAtuacao = setorAtuacao;
        this.tempoExperiencia = tempoExperiencia;
    }

    @Override
    public String descreverPerfil() {
        return "Trabalhador: " + getNome() + " | " + setorAtuacao + " | " + cidadeUf
                + " | " + tempoExperiencia + " anos de experiencia";
    }

    @Override
    public String getTipoUsuario() {
        return "TRABALHADOR";
    }

    public String getCidadeUf() {
        return cidadeUf;
    }

    public String getSetorAtuacao() {
        return setorAtuacao;
    }

    public int getTempoExperiencia() {
        return tempoExperiencia;
    }
}

package modelo;

import java.util.ArrayList;
import java.util.List;

// Avaliacao de uma empresa. E a entidade principal do sistema.
// Cada avaliacao tem varias notas dentro (uma por criterio).
public class Avaliacao implements Publicacao {

    private int id;
    private int usuarioId;
    private int empresaId;
    private String comentario;
    private boolean anonima;
    private String vinculo;
    private String statusModeracao;
    private List<NotaCriterio> notas = new ArrayList<>();

    public Avaliacao(int id, int usuarioId, int empresaId, String comentario,
                     boolean anonima, String vinculo, String statusModeracao) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.empresaId = empresaId;
        this.comentario = comentario;
        this.anonima = anonima;
        this.vinculo = vinculo;
        this.statusModeracao = statusModeracao;
    }

    // media ponderada das notas. multiplico por 2 no final
    // porque a nota vai de 1 a 5 e a pontuacao final e de 0 a 10
    @Override
    public double calcularPontuacao() {
        if (notas.isEmpty()) {
            return 0;
        }

        double somaNotas = 0;
        double somaPesos = 0;

        for (NotaCriterio n : notas) {
            somaNotas = somaNotas + (n.getNota() * n.getPeso());
            somaPesos = somaPesos + n.getPeso();
        }

        return (somaNotas / somaPesos) * 2;
    }

    @Override
    public String resumo() {
        String autor = "Usuario " + usuarioId;
        if (anonima) {
            autor = "Anonimo";
        }
        return "[AVALIACAO] " + autor + " - nota " + String.format("%.1f", calcularPontuacao())
                + " - " + statusModeracao;
    }

    public void adicionarNota(NotaCriterio nota) {
        notas.add(nota);
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

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public boolean isAnonima() {
        return anonima;
    }

    public String getVinculo() {
        return vinculo;
    }

    public String getStatusModeracao() {
        return statusModeracao;
    }

    public List<NotaCriterio> getNotas() {
        return notas;
    }
}

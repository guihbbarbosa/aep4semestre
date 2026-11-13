package modelo;

// Uma nota dada para um criterio dentro da avaliacao
public class NotaCriterio {

    private int criterioId;
    private String criterioNome;
    private int nota;
    private double peso;

    public NotaCriterio(int criterioId, String criterioNome, int nota, double peso) {
        this.criterioId = criterioId;
        this.criterioNome = criterioNome;
        this.nota = nota;
        this.peso = peso;
    }

    public int getCriterioId() {
        return criterioId;
    }

    public String getCriterioNome() {
        return criterioNome;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public double getPeso() {
        return peso;
    }
}

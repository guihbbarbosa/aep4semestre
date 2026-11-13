package modelo;

// Interface que a Avaliacao e a Denuncia implementam.
// Cada uma calcula a pontuacao do seu jeito.
public interface Publicacao {

    double calcularPontuacao();

    String resumo();
}

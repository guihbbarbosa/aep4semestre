package modelo;

// Moderador tambem herda de Usuario, mas aprova as publicacoes
public class Moderador extends Usuario {

    private String registroInterno;

    public Moderador(int id, String nome, String email, String senhaHash, String registroInterno) {
        super(id, nome, email, senhaHash);
        this.registroInterno = registroInterno;
    }

    @Override
    public String descreverPerfil() {
        return "Moderador: " + getNome() + " | registro " + registroInterno;
    }

    @Override
    public String getTipoUsuario() {
        return "MODERADOR";
    }

    public String getRegistroInterno() {
        return registroInterno;
    }
}

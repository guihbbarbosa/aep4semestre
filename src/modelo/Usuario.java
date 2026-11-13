package modelo;

// Classe abstrata. Nao da pra criar um Usuario direto,
// so Trabalhador ou Moderador.
public abstract class Usuario {

    private int id;
    private String nome;
    private String email;
    private String senhaHash;

    public Usuario(int id, String nome, String email, String senhaHash) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
    }

    // as filhas sao obrigadas a escrever esses dois metodos
    public abstract String descreverPerfil();

    public abstract String getTipoUsuario();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }
}

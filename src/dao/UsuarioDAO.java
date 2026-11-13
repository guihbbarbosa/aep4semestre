package dao;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import modelo.Moderador;
import modelo.Trabalhador;
import modelo.Usuario;

// Tudo que mexe com usuario no banco
public class UsuarioDAO {

    // transforma a senha em hash pra nao salvar ela em texto puro
    public static String gerarHash(String senha) throws Exception {
        MessageDigest algoritmo = MessageDigest.getInstance("SHA-256");
        byte[] bytes = algoritmo.digest(senha.getBytes("UTF-8"));

        String resultado = "";
        for (byte b : bytes) {
            resultado = resultado + String.format("%02x", b);
        }
        return resultado;
    }

    public boolean emailJaCadastrado(String email) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT id FROM usuarios WHERE email = ?";
        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setString(1, email);

        ResultSet resultado = comando.executeQuery();
        boolean existe = resultado.next();

        conexao.close();
        return existe;
    }

    public void inserirTrabalhador(Trabalhador trabalhador) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "INSERT INTO usuarios "
                + "(nome, email, senha_hash, tipo_usuario, cidade_uf, setor_atuacao, tempo_experiencia) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        comando.setString(1, trabalhador.getNome());
        comando.setString(2, trabalhador.getEmail());
        comando.setString(3, trabalhador.getSenhaHash());
        comando.setString(4, trabalhador.getTipoUsuario());
        comando.setString(5, trabalhador.getCidadeUf());
        comando.setString(6, trabalhador.getSetorAtuacao());
        comando.setInt(7, trabalhador.getTempoExperiencia());
        comando.executeUpdate();

        // pega o id que o banco gerou
        ResultSet chave = comando.getGeneratedKeys();
        if (chave.next()) {
            trabalhador.setId(chave.getInt(1));
        }

        conexao.close();
    }

    // login: devolve Trabalhador ou Moderador dependendo do tipo_usuario
    public Usuario autenticar(String email, String senha) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT * FROM usuarios WHERE email = ? AND senha_hash = ?";
        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setString(1, email);
        comando.setString(2, gerarHash(senha));

        ResultSet resultado = comando.executeQuery();

        Usuario usuario = null;

        if (resultado.next()) {
            String tipo = resultado.getString("tipo_usuario");

            if (tipo.equals("MODERADOR")) {
                usuario = new Moderador(
                        resultado.getInt("id"),
                        resultado.getString("nome"),
                        resultado.getString("email"),
                        resultado.getString("senha_hash"),
                        resultado.getString("registro_interno"));
            } else {
                usuario = new Trabalhador(
                        resultado.getInt("id"),
                        resultado.getString("nome"),
                        resultado.getString("email"),
                        resultado.getString("senha_hash"),
                        resultado.getString("cidade_uf"),
                        resultado.getString("setor_atuacao"),
                        resultado.getInt("tempo_experiencia"));
            }
        }

        conexao.close();
        return usuario;
    }
}

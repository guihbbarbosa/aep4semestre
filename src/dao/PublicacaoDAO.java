package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.Avaliacao;
import modelo.Denuncia;
import modelo.NotaCriterio;
import modelo.Publicacao;

// CRUD das publicacoes (avaliacoes e denuncias).
// A avaliacao e a entidade principal, entao ela tem as 4 operacoes.
public class PublicacaoDAO {

    // ---------- INSERIR ----------

    public void inserirAvaliacao(Avaliacao avaliacao) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "INSERT INTO publicacoes "
                + "(usuario_id, empresa_id, tipo_publicacao, comentario, anonima, vinculo, status_moderacao) "
                + "VALUES (?, ?, 'AVALIACAO', ?, ?, ?, 'PENDENTE')";

        PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        comando.setInt(1, avaliacao.getUsuarioId());
        comando.setInt(2, avaliacao.getEmpresaId());
        comando.setString(3, avaliacao.getComentario());
        comando.setBoolean(4, avaliacao.isAnonima());
        comando.setString(5, avaliacao.getVinculo());
        comando.executeUpdate();

        ResultSet chave = comando.getGeneratedKeys();
        if (chave.next()) {
            avaliacao.setId(chave.getInt(1));
        }

        // agora grava as notas de cada criterio
        String sqlNota = "INSERT INTO notas_criterio (publicacao_id, criterio_id, nota) VALUES (?, ?, ?)";
        PreparedStatement comandoNota = conexao.prepareStatement(sqlNota);

        for (NotaCriterio nota : avaliacao.getNotas()) {
            comandoNota.setInt(1, avaliacao.getId());
            comandoNota.setInt(2, nota.getCriterioId());
            comandoNota.setInt(3, nota.getNota());
            comandoNota.executeUpdate();
        }

        conexao.close();
    }

    public void inserirDenuncia(Denuncia denuncia) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "INSERT INTO publicacoes "
                + "(usuario_id, empresa_id, tipo_publicacao, comentario, anonima, categoria, gravidade, status_moderacao) "
                + "VALUES (?, ?, 'DENUNCIA', ?, 1, ?, ?, 'PENDENTE')";

        PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        comando.setInt(1, denuncia.getUsuarioId());
        comando.setInt(2, denuncia.getEmpresaId());
        comando.setString(3, denuncia.getComentario());
        comando.setString(4, denuncia.getCategoria());
        comando.setInt(5, denuncia.getGravidade());
        comando.executeUpdate();

        ResultSet chave = comando.getGeneratedKeys();
        if (chave.next()) {
            denuncia.setId(chave.getInt(1));
        }

        conexao.close();
    }

    // ---------- LISTAR / BUSCAR ----------

    public List<Avaliacao> listarAvaliacoesDoUsuario(int usuarioId) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT * FROM publicacoes "
                + "WHERE usuario_id = ? AND tipo_publicacao = 'AVALIACAO' ORDER BY id DESC";

        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setInt(1, usuarioId);
        ResultSet resultado = comando.executeQuery();

        List<Avaliacao> avaliacoes = new ArrayList<>();

        while (resultado.next()) {
            Avaliacao avaliacao = new Avaliacao(
                    resultado.getInt("id"),
                    resultado.getInt("usuario_id"),
                    resultado.getInt("empresa_id"),
                    resultado.getString("comentario"),
                    resultado.getBoolean("anonima"),
                    resultado.getString("vinculo"),
                    resultado.getString("status_moderacao"));
            avaliacoes.add(avaliacao);
        }

        conexao.close();

        // busca as notas de cada avaliacao
        for (Avaliacao avaliacao : avaliacoes) {
            carregarNotas(avaliacao);
        }

        return avaliacoes;
    }

    public Avaliacao buscarAvaliacaoPorId(int id) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT * FROM publicacoes WHERE id = ? AND tipo_publicacao = 'AVALIACAO'";
        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setInt(1, id);

        ResultSet resultado = comando.executeQuery();

        Avaliacao avaliacao = null;

        if (resultado.next()) {
            avaliacao = new Avaliacao(
                    resultado.getInt("id"),
                    resultado.getInt("usuario_id"),
                    resultado.getInt("empresa_id"),
                    resultado.getString("comentario"),
                    resultado.getBoolean("anonima"),
                    resultado.getString("vinculo"),
                    resultado.getString("status_moderacao"));
        }

        conexao.close();

        if (avaliacao != null) {
            carregarNotas(avaliacao);
        }

        return avaliacao;
    }

    // Lista avaliacoes e denuncias juntas, numa lista de Publicacao.
    // E aqui que o polimorfismo acontece: o Main chama calcularPontuacao()
    // sem saber se o objeto e uma avaliacao ou uma denuncia.
    public List<Publicacao> listarPorEmpresa(int empresaId) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT * FROM publicacoes WHERE empresa_id = ? ORDER BY id";
        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setInt(1, empresaId);

        ResultSet resultado = comando.executeQuery();

        List<Publicacao> publicacoes = new ArrayList<>();
        List<Avaliacao> avaliacoes = new ArrayList<>();

        while (resultado.next()) {
            if (resultado.getString("tipo_publicacao").equals("AVALIACAO")) {
                Avaliacao avaliacao = new Avaliacao(
                        resultado.getInt("id"),
                        resultado.getInt("usuario_id"),
                        resultado.getInt("empresa_id"),
                        resultado.getString("comentario"),
                        resultado.getBoolean("anonima"),
                        resultado.getString("vinculo"),
                        resultado.getString("status_moderacao"));
                publicacoes.add(avaliacao);
                avaliacoes.add(avaliacao);
            } else {
                Denuncia denuncia = new Denuncia(
                        resultado.getInt("id"),
                        resultado.getInt("usuario_id"),
                        resultado.getInt("empresa_id"),
                        resultado.getString("comentario"),
                        resultado.getString("categoria"),
                        resultado.getInt("gravidade"),
                        resultado.getString("status_moderacao"));
                publicacoes.add(denuncia);
            }
        }

        conexao.close();

        for (Avaliacao avaliacao : avaliacoes) {
            carregarNotas(avaliacao);
        }

        return publicacoes;
    }

    public List<Publicacao> listarPendentes() throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT * FROM publicacoes WHERE status_moderacao = 'PENDENTE' ORDER BY id";
        PreparedStatement comando = conexao.prepareStatement(sql);
        ResultSet resultado = comando.executeQuery();

        List<Publicacao> publicacoes = new ArrayList<>();
        List<Avaliacao> avaliacoes = new ArrayList<>();

        while (resultado.next()) {
            if (resultado.getString("tipo_publicacao").equals("AVALIACAO")) {
                Avaliacao avaliacao = new Avaliacao(
                        resultado.getInt("id"),
                        resultado.getInt("usuario_id"),
                        resultado.getInt("empresa_id"),
                        resultado.getString("comentario"),
                        resultado.getBoolean("anonima"),
                        resultado.getString("vinculo"),
                        resultado.getString("status_moderacao"));
                publicacoes.add(avaliacao);
                avaliacoes.add(avaliacao);
            } else {
                Denuncia denuncia = new Denuncia(
                        resultado.getInt("id"),
                        resultado.getInt("usuario_id"),
                        resultado.getInt("empresa_id"),
                        resultado.getString("comentario"),
                        resultado.getString("categoria"),
                        resultado.getInt("gravidade"),
                        resultado.getString("status_moderacao"));
                publicacoes.add(denuncia);
            }
        }

        conexao.close();

        for (Avaliacao avaliacao : avaliacoes) {
            carregarNotas(avaliacao);
        }

        return publicacoes;
    }

    // ---------- ATUALIZAR ----------

    // atualiza o comentario e as notas. a avaliacao volta pra PENDENTE
    // porque o conteudo mudou e precisa ser moderado de novo (RN01)
    public void atualizarAvaliacao(Avaliacao avaliacao) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "UPDATE publicacoes SET comentario = ?, status_moderacao = 'PENDENTE' WHERE id = ?";
        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setString(1, avaliacao.getComentario());
        comando.setInt(2, avaliacao.getId());
        comando.executeUpdate();

        String sqlNota = "UPDATE notas_criterio SET nota = ? WHERE publicacao_id = ? AND criterio_id = ?";
        PreparedStatement comandoNota = conexao.prepareStatement(sqlNota);

        for (NotaCriterio nota : avaliacao.getNotas()) {
            comandoNota.setInt(1, nota.getNota());
            comandoNota.setInt(2, avaliacao.getId());
            comandoNota.setInt(3, nota.getCriterioId());
            comandoNota.executeUpdate();
        }

        conexao.close();
    }

    // usado pelo moderador pra aprovar ou reprovar
    public void atualizarStatus(int publicacaoId, String novoStatus) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "UPDATE publicacoes SET status_moderacao = ? WHERE id = ?";
        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setString(1, novoStatus);
        comando.setInt(2, publicacaoId);
        comando.executeUpdate();

        conexao.close();
    }

    // ---------- EXCLUIR ----------

    // as notas somem junto por causa do ON DELETE CASCADE do schema.sql
    public void excluir(int publicacaoId) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "DELETE FROM publicacoes WHERE id = ?";
        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setInt(1, publicacaoId);
        comando.executeUpdate();

        conexao.close();
    }

    // busca as notas da avaliacao e coloca dentro do objeto
    private void carregarNotas(Avaliacao avaliacao) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT nc.criterio_id, nc.nota, c.nome, c.peso "
                + "FROM notas_criterio nc "
                + "INNER JOIN criterios c ON c.id = nc.criterio_id "
                + "WHERE nc.publicacao_id = ? ORDER BY nc.criterio_id";

        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setInt(1, avaliacao.getId());
        ResultSet resultado = comando.executeQuery();

        while (resultado.next()) {
            NotaCriterio nota = new NotaCriterio(
                    resultado.getInt("criterio_id"),
                    resultado.getString("nome"),
                    resultado.getInt("nota"),
                    resultado.getDouble("peso"));
            avaliacao.adicionarNota(nota);
        }

        conexao.close();
    }
}

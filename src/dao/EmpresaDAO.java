package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import modelo.Empresa;

// Tudo que mexe com empresa no banco
public class EmpresaDAO {

    public boolean cnpjJaCadastrado(String cnpj) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT id FROM empresas WHERE cnpj = ?";
        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setString(1, cnpj);

        ResultSet resultado = comando.executeQuery();
        boolean existe = resultado.next();

        conexao.close();
        return existe;
    }

    public void inserir(Empresa empresa) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "INSERT INTO empresas (razao_social, cnpj, setor, porte, cidade_uf) "
                + "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        comando.setString(1, empresa.getRazaoSocial());
        comando.setString(2, empresa.getCnpj());
        comando.setString(3, empresa.getSetor());
        comando.setString(4, empresa.getPorte());
        comando.setString(5, empresa.getCidadeUf());
        comando.executeUpdate();

        ResultSet chave = comando.getGeneratedKeys();
        if (chave.next()) {
            empresa.setId(chave.getInt(1));
        }

        conexao.close();
    }

    public List<Empresa> listarTodas() throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT * FROM empresas ORDER BY razao_social";
        PreparedStatement comando = conexao.prepareStatement(sql);
        ResultSet resultado = comando.executeQuery();

        List<Empresa> empresas = new ArrayList<>();

        while (resultado.next()) {
            Empresa empresa = new Empresa(
                    resultado.getInt("id"),
                    resultado.getString("razao_social"),
                    resultado.getString("cnpj"),
                    resultado.getString("setor"),
                    resultado.getString("porte"),
                    resultado.getString("cidade_uf"));
            empresas.add(empresa);
        }

        conexao.close();
        return empresas;
    }

    // busca por parte do nome, do setor ou da cidade
    public List<Empresa> buscar(String termo) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT * FROM empresas "
                + "WHERE razao_social LIKE ? OR setor LIKE ? OR cidade_uf LIKE ? "
                + "ORDER BY razao_social";

        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setString(1, "%" + termo + "%");
        comando.setString(2, "%" + termo + "%");
        comando.setString(3, "%" + termo + "%");

        ResultSet resultado = comando.executeQuery();

        List<Empresa> empresas = new ArrayList<>();

        while (resultado.next()) {
            Empresa empresa = new Empresa(
                    resultado.getInt("id"),
                    resultado.getString("razao_social"),
                    resultado.getString("cnpj"),
                    resultado.getString("setor"),
                    resultado.getString("porte"),
                    resultado.getString("cidade_uf"));
            empresas.add(empresa);
        }

        conexao.close();
        return empresas;
    }

    public Empresa buscarPorId(int id) throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT * FROM empresas WHERE id = ?";
        PreparedStatement comando = conexao.prepareStatement(sql);
        comando.setInt(1, id);

        ResultSet resultado = comando.executeQuery();

        Empresa empresa = null;

        if (resultado.next()) {
            empresa = new Empresa(
                    resultado.getInt("id"),
                    resultado.getString("razao_social"),
                    resultado.getString("cnpj"),
                    resultado.getString("setor"),
                    resultado.getString("porte"),
                    resultado.getString("cidade_uf"));
        }

        conexao.close();
        return empresa;
    }

    // Ranking do Indice de Emprego Justo.
    // O calculo da media ponderada e feito pelo proprio banco.
    // O HAVING garante a regra RN02 (minimo de 5 avaliacoes aprovadas).
    public List<String> listarRanking() throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT e.razao_social, "
                + "SUM(nc.nota * c.peso) / SUM(c.peso) * 2 AS indice, "
                + "COUNT(DISTINCT p.id) AS total_avaliacoes "
                + "FROM empresas e "
                + "INNER JOIN publicacoes p ON p.empresa_id = e.id "
                + "INNER JOIN notas_criterio nc ON nc.publicacao_id = p.id "
                + "INNER JOIN criterios c ON c.id = nc.criterio_id "
                + "WHERE p.tipo_publicacao = 'AVALIACAO' AND p.status_moderacao = 'APROVADA' "
                + "GROUP BY e.razao_social "
                + "HAVING COUNT(DISTINCT p.id) >= 5 "
                + "ORDER BY indice DESC";

        PreparedStatement comando = conexao.prepareStatement(sql);
        ResultSet resultado = comando.executeQuery();

        List<String> linhas = new ArrayList<>();

        while (resultado.next()) {
            String linha = resultado.getString("razao_social")
                    + " - indice " + String.format("%.1f", resultado.getDouble("indice"))
                    + " (" + resultado.getInt("total_avaliacoes") + " avaliacoes)";
            linhas.add(linha);
        }

        conexao.close();
        return linhas;
    }
}

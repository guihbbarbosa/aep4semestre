package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import modelo.NotaCriterio;

// Le os 5 criterios de avaliacao que estao no banco
public class CriterioDAO {

    // devolve os criterios com nota 0, o usuario preenche depois
    public List<NotaCriterio> listarParaAvaliacao() throws Exception {
        Connection conexao = ConexaoBanco.abrir();

        String sql = "SELECT id, nome, peso FROM criterios ORDER BY id";
        PreparedStatement comando = conexao.prepareStatement(sql);
        ResultSet resultado = comando.executeQuery();

        List<NotaCriterio> criterios = new ArrayList<>();

        while (resultado.next()) {
            NotaCriterio criterio = new NotaCriterio(
                    resultado.getInt("id"),
                    resultado.getString("nome"),
                    0,
                    resultado.getDouble("peso"));
            criterios.add(criterio);
        }

        conexao.close();
        return criterios;
    }
}

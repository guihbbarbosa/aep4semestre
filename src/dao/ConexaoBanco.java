package dao;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

// Abre a conexao com o MySQL.
// Os dados ficam no arquivo config.properties pra senha nao ir pro GitHub.
public class ConexaoBanco {

    public static Connection abrir() throws Exception {
        Properties config = new Properties();

        FileInputStream arquivo = new FileInputStream("config.properties");
        config.load(arquivo);
        arquivo.close();

        String url = config.getProperty("db.url");
        String usuario = config.getProperty("db.usuario");
        String senha = config.getProperty("db.senha");

        return DriverManager.getConnection(url, usuario, senha);
    }
}

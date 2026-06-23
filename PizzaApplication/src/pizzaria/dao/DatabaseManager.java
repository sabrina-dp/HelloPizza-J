package pizzaria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gerencia a conexão única com o banco de dados SQLite.
 * Padrão Singleton: garante que apenas uma conexão seja aberta por vez.
 * https://www.geeksforgeeks.org/advance-java/jdbc-using-model-object-and-singleton-class/
 * https://gist.github.com/EhsanulHaqueSiam/ac6782050850c9a1204009a12598a51d
 * https://dev.to/thisismemukul/singleton-design-pattern-in-java-4m9p
 */
public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:pizzaria.db";

    private static DatabaseManager instancia;
    private Connection conexao;

    private DatabaseManager() {
        this.conexao = createConexao();
    }

    private static Connection createConexao() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(URL);
            // Habilita FK a cada conexão, pois SQLite não persiste essa configuração
            conn.createStatement().execute("PRAGMA foreign_keys = ON");
            return conn;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar ao SQLite: " + e.getMessage(), e);
        }
    }

    public static synchronized DatabaseManager getInstancia() {
        if (instancia == null) {
            instancia = new DatabaseManager();
        }
        return instancia;
    }

    public Connection getConexao() {
        try {
            if (conexao == null || conexao.isClosed()) {
                conexao = createConexao();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter conexão: " + e.getMessage(), e);
        }
        return conexao;
    }

    public void closeConexao() {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
                System.out.println("Conexão com o banco encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexão: " + e.getMessage());
        }
    }
}

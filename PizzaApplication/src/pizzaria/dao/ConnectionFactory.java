package pizzaria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL =
        "jdbc:mysql://localhost:3306/pizzaria?serverTimezone=America/Sao_Paulo";

    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    private static ConnectionFactory instancia;
    private Connection conexao;

    private ConnectionFactory() {
        this.conexao = criarConexao();
    }

    private static Connection criarConexao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(
                "Erro ao conectar ao MySQL: " + e.getMessage(), e
            );
        }
    }

    public static synchronized ConnectionFactory getInstancia() {
        if (instancia == null) {
            instancia = new ConnectionFactory();
        }

        return instancia;
    }

    public Connection getConexao() {
        try {
            if (conexao == null || conexao.isClosed()) {
                conexao = criarConexao();
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                "Erro ao obter conexão: " + e.getMessage(), e
            );
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
            System.err.println(
                "Erro ao fechar conexão: " + e.getMessage()
            );
        }
    }
}
package pizzaria.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * CLASSE DE CONFIGURAÇÃO DO BANCO DE DADOS (SQLite)
 * Cria as tabelas do banco SQLite na primeira execução, caso ainda não existam.
 */
public class InicializadorBanco {
    public static void inicializar() {
        Connection conexao = DatabaseManager.getInstancia().getConexao();
        try (Statement stmt = conexao.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cliente (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    sobrenome TEXT NOT NULL,
                    telefone TEXT NOT NULL UNIQUE)""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS sabor (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nome TEXT NOT NULL,
                    tipo TEXT NOT NULL CHECK(tipo IN ('SIMPLES','ESPECIAL','PREMIUM')))""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tabela_preco (
                    id INTEGER PRIMARY KEY CHECK(id = 1),
                    preco_simples REAL NOT NULL,
                    preco_especial REAL NOT NULL,
                    preco_premium  REAL NOT NULL)""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pedido (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    cliente_id INTEGER NOT NULL,
                    estado TEXT NOT NULL DEFAULT 'ABERTO' CHECK(estado IN ('ABERTO','A_CAMINHO','ENTREGUE','CANCELADO')),
                    preco_total REAL NOT NULL DEFAULT 0.0,
                    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE)""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pizza (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    forma TEXT NOT NULL CHECK(forma IN ('Circular','Quadrado','Triangulo')),
                    dimensao REAL NOT NULL,
                    sabor1_id INTEGER NOT NULL,
                    sabor2_id INTEGER,
                    FOREIGN KEY (sabor1_id) REFERENCES sabor(id),
                    FOREIGN KEY (sabor2_id) REFERENCES sabor(id))""");

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS item_pedido (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    pedido_id INTEGER NOT NULL,
                    pizza_id INTEGER NOT NULL,
                    FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
                    FOREIGN KEY (pizza_id) REFERENCES pizza(id) ON DELETE CASCADE)""");

            stmt.execute("INSERT OR IGNORE INTO tabela_preco (id, preco_simples, preco_especial, preco_premium) VALUES (1, 0.10, 0.20, 0.35)");

            System.out.println("Banco de dados inicializado com sucesso.");

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inicializar banco de dados: " + e.getMessage(), e);
        }
    }
}

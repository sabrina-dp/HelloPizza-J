package pizzaria.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InicializadorBanco {

    public static void inicializar() {

        Connection conexao =
            ConnectionFactory.getInstancia().getConexao();

        try (Statement stmt = conexao.createStatement()) {

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cliente (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nome VARCHAR(100) NOT NULL,
                    sobrenome VARCHAR(100) NOT NULL,
                    telefone VARCHAR(30) NOT NULL UNIQUE
                ) ENGINE=InnoDB
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS sabor (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nome VARCHAR(100) NOT NULL,
                    tipo VARCHAR(20) NOT NULL
                    CHECK(tipo IN ('SIMPLES','ESPECIAL','PREMIUM'))
                ) ENGINE=InnoDB
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS tabela_preco (
                    id INT PRIMARY KEY CHECK(id = 1),
                    preco_simples DOUBLE NOT NULL,
                    preco_especial DOUBLE NOT NULL,
                    preco_premium DOUBLE NOT NULL
                ) ENGINE=InnoDB
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pedido (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    cliente_id INT NOT NULL,
                    estado VARCHAR(20) NOT NULL DEFAULT 'ABERTO'
                    CHECK(estado IN ('ABERTO','A_CAMINHO','ENTREGUE','CANCELADO')),
                    preco_total DOUBLE NOT NULL DEFAULT 0.0,
                    FOREIGN KEY (cliente_id)
                    REFERENCES cliente(id) ON DELETE CASCADE
                ) ENGINE=InnoDB
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS pizza (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    forma VARCHAR(20) NOT NULL
                    CHECK(forma IN ('Circular','Quadrado','Triangulo')),
                    dimensao DOUBLE NOT NULL,
                    sabor1_id INT NOT NULL,
                    sabor2_id INT,
                    FOREIGN KEY (sabor1_id) REFERENCES sabor(id),
                    FOREIGN KEY (sabor2_id) REFERENCES sabor(id)
                ) ENGINE=InnoDB
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS item_pedido (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    pedido_id INT NOT NULL,
                    pizza_id INT NOT NULL,
                    FOREIGN KEY (pedido_id)
                    REFERENCES pedido(id) ON DELETE CASCADE,
                    FOREIGN KEY (pizza_id)
                    REFERENCES pizza(id) ON DELETE CASCADE
                ) ENGINE=InnoDB
            """);

            stmt.execute("""
                INSERT IGNORE INTO tabela_preco
                (id, preco_simples, preco_especial, preco_premium)
                VALUES (1, 0.10, 0.20, 0.35)
            """);

            System.out.println(
                "Banco de dados inicializado com sucesso."
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                "Erro ao inicializar banco de dados: "
                + e.getMessage(), e
            );
        }
    }
}
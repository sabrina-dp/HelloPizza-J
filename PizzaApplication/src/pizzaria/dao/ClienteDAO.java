package pizzaria.dao;

import pizzaria.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) responsável pelas operações de banco de dados relacionadas à entidade Cliente.
 * Implementa as operações CRUD (Create, Read, Update, Delete).
 * https://www.javaguides.net/2019/03/jsp-servlet-jdbc-mysql-crud-example-tutorial.html
 * https://github.com/naman14310/Design_Patterns_In_Java_CRUD/blob/master/UserinfoDAO.java         
 */
public class ClienteDAO {

    private final Connection conexao;

    public ClienteDAO() {
        this.conexao = DatabaseManager.getInstancia().getConexao(); // pega conexão já aberta, ao invés de abrir uma nova
    }

    /**
     * Cliente a ser inserido (ID ignorado pois é gerado pelo banco).
     */
    public int inserir(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO cliente (nome, sobrenome, telefone) VALUES (?, ?, ?)";

        // prepareStatement com RETURN_GENERATED_KEYS faz o banco devolver o ID gerado automaticamente
        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Os '?' do SQL são preenchidos em ordem aqui
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.executeUpdate();  // executeUpdate() devolve quantas linhas foram afetadas

            // Recupera o ID gerado pelo banco
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Cliente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultado(rs);
                }
            }
        }
        return null;
    }

    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente ORDER BY nome, sobrenome";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearResultado(rs));
            }
        }
        return clientes;
    }

    public List<Cliente> buscarPorNome(String nome) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE nome LIKE ? OR sobrenome LIKE ? ORDER BY nome";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            String filtro = "%" + nome + "%"; // % permite buscar qualquer parte do nome
            stmt.setString(1, filtro);
            stmt.setString(2, filtro);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearResultado(rs));  // rs percorre as linhas retornadas, como um cursor
                }
            }
        }
        return clientes;
    }

    public Cliente buscarPorTelefone(String telefone) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE telefone = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, telefone);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearResultado(rs);
                }
            }
        }
        return null;
    }

    /**
     * Filtra clientes por sobrenome, parte do sobrenome ou telefone usando um único termo.
     */
    public List<Cliente> buscarFiltrado(String termo) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente WHERE sobrenome LIKE ? OR telefone LIKE ? ORDER BY nome";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            String curinga = "%" + termo + "%";
            stmt.setString(1, curinga);
            stmt.setString(2, curinga);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearResultado(rs));
                }
            }
        }
        return clientes;
    }

    public boolean atualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE cliente SET nome = ?, sobrenome = ?, telefone = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getSobrenome());
            stmt.setString(3, cliente.getTelefone());
            stmt.setInt(4, cliente.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean remover(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0; 
        }
    }

    /**
     * Mapeia uma linha do ResultSet para um objeto Cliente.
     */
    private Cliente mapearResultado(ResultSet rs) throws SQLException {
        return new Cliente(
            rs.getInt("id"),
            rs.getString("nome"),
            rs.getString("sobrenome"),
            rs.getString("telefone")
        );
    }
}

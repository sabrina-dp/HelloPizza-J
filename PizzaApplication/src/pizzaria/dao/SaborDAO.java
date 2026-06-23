package pizzaria.dao;

import pizzaria.model.Sabor;
import pizzaria.model.TipoPizza;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO responsável pelas operações de banco da entidade Sabor.
 * https://www.javaguides.net/2019/03/jsp-servlet-jdbc-mysql-crud-example-tutorial.html
 * https://github.com/naman14310/Design_Patterns_In_Java_CRUD/blob/master/UserinfoDAO.java         
 */
public class SaborDAO {

    private final Connection conexao;

    public SaborDAO() {
        this.conexao = DatabaseManager.getInstancia().getConexao();
    }

    public int inserir(Sabor sabor) throws SQLException {
        String sql = "INSERT INTO sabor (nome, tipo) VALUES (?, ?)";

        try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getTipo().name()); // .name() converte o enum para String: SIMPLES, ESPECIAL ou PREMIUM
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        return -1;
    }

    public Sabor buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM sabor WHERE id = ?";

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

    public List<Sabor> listarTodos() throws SQLException {
        List<Sabor> sabores = new ArrayList<>();
        String sql = "SELECT * FROM sabor ORDER BY nome";

        try (PreparedStatement stmt = conexao.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                sabores.add(mapearResultado(rs));
            }
        }
        return sabores;
    }

    public List<Sabor> listarPorTipo(TipoPizza tipo) throws SQLException {
        List<Sabor> sabores = new ArrayList<>();
        String sql = "SELECT * FROM sabor WHERE tipo = ? ORDER BY nome";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, tipo.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    sabores.add(mapearResultado(rs));
                }
            }
        }
        return sabores;
    }

    public boolean atualizar(Sabor sabor) throws SQLException {
        String sql = "UPDATE sabor SET nome = ?, tipo = ? WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, sabor.getNome());
            stmt.setString(2, sabor.getTipo().name());
            stmt.setInt(3, sabor.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean remover(int id) throws SQLException {
        String sql = "DELETE FROM sabor WHERE id = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Sabor mapearResultado(ResultSet rs) throws SQLException {
        return new Sabor(
            rs.getInt("id"),
            rs.getString("nome"),
            TipoPizza.valueOf(rs.getString("tipo")) // converte a String do banco de volta para o enum
        );
    }
}

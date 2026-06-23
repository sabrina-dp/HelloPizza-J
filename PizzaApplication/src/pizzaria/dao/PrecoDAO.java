package pizzaria.dao;

import java.sql.*;
import pizzaria.model.TabelaPreco;

/**
 * DAO responsável pelas operações de banco da entidade TabelaPreco.
 * A tabela de preços possui sempre uma única linha (id = 1), criada na
 * inicialização do banco. Por isso, so busca e atualização são necessárias.
 */
public class PrecoDAO {

    private final Connection conexao;

    public PrecoDAO() {
        this.conexao = DatabaseManager.getInstancia().getConexao();
    }

    public TabelaPreco buscar() {
        String sql = "SELECT * FROM tabela_preco WHERE id = 1";
        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new TabelaPreco(
                    rs.getDouble("preco_simples"),
                    rs.getDouble("preco_especial"),
                    rs.getDouble("preco_premium")
                );
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar tabela de preços: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizar(TabelaPreco tabela) {
        String sql = "UPDATE tabela_preco SET preco_simples = ?, preco_especial = ?, preco_premium = ? WHERE id = 1";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setDouble(1, tabela.getPrecoSimples());
            ps.setDouble(2, tabela.getPrecoEspecial());
            ps.setDouble(3, tabela.getPrecoPremium());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar tabela de preços: " + e.getMessage());
        }
        return false;
    }
}

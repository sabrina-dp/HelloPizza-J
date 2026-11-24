package pizzaria.dao;

import java.sql.SQLException;
import pizzaria.model.TabelaPreco;

/**
 * Não estende DAO porque a tabela de preços tem uma única linha (id = 1):
 * só existem as operações de buscar e atualizar, sem inserir, listar ou remover.
 */
public interface PrecoDAO {
    TabelaPreco buscar() throws SQLException;
    boolean atualizar(TabelaPreco tabela) throws SQLException;
}
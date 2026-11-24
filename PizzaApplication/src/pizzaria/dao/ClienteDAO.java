package pizzaria.dao;

import pizzaria.model.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO extends DAO<Cliente> {
    List<Cliente> buscarFiltrado(String termo) throws SQLException;
}

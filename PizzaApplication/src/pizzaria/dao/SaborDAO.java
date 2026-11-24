package pizzaria.dao;

import pizzaria.model.Sabor;
import pizzaria.model.TipoPizza;

import java.sql.SQLException;
import java.util.List;

public interface SaborDAO extends DAO<Sabor> {
    List<Sabor> listarPorTipo(TipoPizza tipo) throws SQLException;
}
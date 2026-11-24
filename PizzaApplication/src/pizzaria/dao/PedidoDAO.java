package pizzaria.dao;

import java.sql.SQLException;
import java.util.List;
import pizzaria.model.EstadoPedido;
import pizzaria.model.Pedido;

public interface PedidoDAO extends DAO<Pedido> {
    Pedido buscarPorCliente(int clienteId) throws SQLException;
    boolean atualizarEstado(int idPedido, EstadoPedido estado) throws SQLException;
}


package pizzaria.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pizzaria.model.Cliente;
import pizzaria.model.EstadoPedido;
import pizzaria.model.Pedido;



public class PedidoDAO {

    private static final String URL =
            "jdbc:mysql://localhost:3306/pizzaria";

    private static final String USER = "root";

    private static final String PASSWORD = "";

    // não sei muito ainda sobre o banco de dados, Sabrina, por favor corrija para o seu certo

    public static boolean inserir(Pedido pedido) {

        String sql =
            "INSERT INTO pedido(id, cliente_id, estado, preco_total) VALUES (?, ?, ?, ?)";

        try {

            Connection con =
                DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, pedido.getId());

            ps.setInt(
                2,
                pedido.getCliente().GetID()
            );

            ps.setString(
                3,
                pedido.getEstado().name()
            );

            ps.setDouble(
                4,
                pedido.calcularTotal()
            );

            ps.executeUpdate();

            con.close();

            return true;

        } catch(Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public static boolean excluir(int id) {

        String sql =
            "DELETE FROM pedido WHERE id = ?";

        try {

            Connection con =
                DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();

            con.close();

            return true;

        } catch(Exception e) {

            e.printStackTrace();

            return false;
        }
    }

   

    public static Pedido buscarPorId(int id) {

        String sql =
            "SELECT * FROM pedido WHERE id = ?";

        try {

            Connection con =
                DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setInt(1, id);

            ResultSet rs =
                ps.executeQuery();

            if(rs.next()) {

                int clienteId =
                    rs.getInt("cliente_id");

                Cliente cliente =
                    ClienteDAO.buscarPorId(clienteId);

                Pedido pedido =
                    new Pedido(
                        rs.getInt("id"),
                        cliente
                    );

                pedido.mudarEstado(
                    EstadoPedido.valueOf(
                        rs.getString("estado")
                    )
                );

                con.close();

                return pedido;
            }

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return null;
    }


    public static List<Pedido> listarTodos() {

        List<Pedido> pedidos =
            new ArrayList<>();

        String sql =
            "SELECT * FROM pedido";

        try {

            Connection con =
                DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps =
                con.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

            while(rs.next()) {

                int clienteId =
                    rs.getInt("cliente_id");

                Cliente cliente =
                    ClienteDAO.buscarPorId(clienteId);

                Pedido pedido =
                    new Pedido(
                        rs.getInt("id"),
                        cliente
                    );

                pedido.mudarEstado(
                    EstadoPedido.valueOf(
                        rs.getString("estado")
                    )
                );

                pedidos.add(pedido);
            }

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return pedidos;
    }


    public static boolean atualizarEstado(
            int idPedido,
            EstadoPedido estado) {

        String sql =
            "UPDATE pedido SET estado = ? WHERE id = ?";

        try {

            Connection con =
                DriverManager.getConnection(URL, USER, PASSWORD);

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(
                1,
                estado.name()
            );

            ps.setInt(
                2,
                idPedido
            );

            ps.executeUpdate();

            con.close();

            return true;

        } catch(Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}


package pizzaria.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import pizzaria.model.Cliente;
import pizzaria.model.EstadoPedido;
import pizzaria.model.ItemPedido;
import pizzaria.model.Pedido;
import pizzaria.model.Pizza;
import pizzaria.model.Sabor;
import pizzaria.model.forma.Circulo;
import pizzaria.model.forma.Forma;
import pizzaria.model.forma.Quadrado;
import pizzaria.model.forma.Triangulo;
import pizzaria.model.TabelaPreco;

/**
 * DAO responsável pelas operações de banco de dados relacionadas às entidades Pedido, Pizza e ItemPedido.
 * As operações de Pizza e ItemPedido são gerenciadas aqui por coesão, já que são sempre carregadas junto com o Pedido.
 * Implementa as operações CRUD e métodos auxiliares de busca.
 * https://www.javaguides.net/2019/03/jsp-servlet-jdbc-mysql-crud-example-tutorial.html
 * https://github.com/naman14310/Design_Patterns_In_Java_CRUD/blob/master/UserinfoDAO.java
 */
public class PedidoDAO {

    private final Connection conexao;

    public PedidoDAO() {
        this.conexao = ConnectionFactory.getInstancia().getConexao();
    }

    public boolean inserir(Pedido pedido) throws SQLException {
        conexao.setAutoCommit(false);
        try {
            String sql = "INSERT INTO pedido (cliente_id, estado, preco_total) VALUES (?, ?, ?)";
            // RETURN_GENERATED_KEYS faz o banco devolver o id criado automaticamente pelo AUTOINCREMENT
            try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, pedido.getCliente().getId());
                ps.setString(2, pedido.getEstado().name());
                ps.setDouble(3, pedido.calcularTotal());
                ps.executeUpdate();

                // getGeneratedKeys() lê o id gerado, só exitse depois do executeUpdate()
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int idGerado = keys.getInt(1);
                        // itens só podem ser inseridos agora, pois precisam do id real do pedido como fk
                        for (ItemPedido item : pedido.getItens()) {
                            inserirPizza(item.getPizza());
                            inserirItemPedido(idGerado, item.getPizza().getId());
                        }
                        conexao.commit();
                        return true;
                    }
                }
            }
            conexao.rollback();
            return false;
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.setAutoCommit(true);
        }
    }

    public boolean atualizar(Pedido pedido) throws SQLException {
        conexao.setAutoCommit(false);
        try {
            List<Integer> pizzaIdsAntigos = buscarPizzaIdsDoPedido(pedido.getId());

            String deletarItens = "DELETE FROM item_pedido WHERE pedido_id = ?";
            try (PreparedStatement ps = conexao.prepareStatement(deletarItens)) {
                ps.setInt(1, pedido.getId());
                ps.executeUpdate();
            }

            deletarPizzas(pizzaIdsAntigos);

            for (ItemPedido item : pedido.getItens()) {
                inserirPizza(item.getPizza());
                inserirItemPedido(pedido.getId(), item.getPizza().getId());
            }

            String sqlUpdatePreco = "UPDATE pedido SET preco_total = ? WHERE id = ?";
            try (PreparedStatement ps = conexao.prepareStatement(sqlUpdatePreco)) {
                ps.setDouble(1, pedido.calcularTotal());
                ps.setInt(2, pedido.getId());
                boolean atualizado = ps.executeUpdate() > 0;
                conexao.commit();
                return atualizado;
            }
        } catch (SQLException e) {
            conexao.rollback();
            throw e;
        } finally {
            conexao.setAutoCommit(true);
        }
    }

    public Pedido buscarPorCliente(int clienteId) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE cliente_id = ? AND estado = 'ABERTO' LIMIT 1";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearPedido(rs);
            }
        }
        return null;
    }

    public List<Pedido> listarTodos() throws SQLException {
        List<Pedido> pedidos = new ArrayList<>();

        String sql = """
            SELECT p.id, p.estado, p.preco_total, p.cliente_id, 
                   c.nome, c.sobrenome, c.telefone 
            FROM pedido p 
            INNER JOIN cliente c ON p.cliente_id = c.id
        """;

        try (PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // 1. Cria o objeto Cliente direto com os dados trazidos pelo JOIN
                Cliente cliente = new Cliente(
                    rs.getInt("cliente_id"),
                    rs.getString("nome"),
                    rs.getString("sobrenome"),
                    rs.getString("telefone")
                );

                Pedido pedido = new Pedido(rs.getInt("id"), cliente);
                pedido.mudarEstado(EstadoPedido.valueOf(rs.getString("estado")));
                pedido.setPrecoTotal(rs.getDouble("preco_total"));

                for (ItemPedido item : carregarItensDoPedido(pedido.getId())) {
                    pedido.adicionarItem(item);
                }

                pedidos.add(pedido);
            }
        }
        return pedidos;
    }
    
    public boolean atualizarEstado(int idPedido, EstadoPedido estado) throws SQLException {
        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, estado.name());
            ps.setInt(2, idPedido);
            return ps.executeUpdate() > 0;
        }
    }

    // --------------------- Pizza e ItemPedido

    private void inserirPizza(Pizza pizza) throws SQLException {
        String sql = "INSERT INTO pizza (forma, dimensao, sabor1_id, sabor2_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, pizza.getForma().getNome());
            ps.setDouble(2, pizza.getForma().getDimensao());
            ps.setInt(3, pizza.getSabor1().getId());
            if (pizza.getSabor2() != null) {
                ps.setInt(4, pizza.getSabor2().getId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) pizza.setId(keys.getInt(1));
            }
        }
    }

    private void inserirItemPedido(int pedidoId, int pizzaId) throws SQLException {
        String sql = "INSERT INTO item_pedido (pedido_id, pizza_id) VALUES (?, ?)";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            ps.setInt(2, pizzaId);
            ps.executeUpdate();
        }
    }

    private List<Integer> buscarPizzaIdsDoPedido(int pedidoId) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        String sql = "SELECT pizza_id FROM item_pedido WHERE pedido_id = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt("pizza_id"));
            }
        }
        return ids;
    }

    private void deletarPizzas(List<Integer> pizzaIds) throws SQLException {
        if (pizzaIds.isEmpty()) return;
        String sql = "DELETE FROM pizza WHERE id = ?";
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            for (int id : pizzaIds) {
                ps.setInt(1, id);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private List<ItemPedido> carregarItensDoPedido(int pedidoId) throws SQLException {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = """
            SELECT p.id, p.forma, p.dimensao, p.sabor1_id, p.sabor2_id,
                   s1.nome AS s1_nome, s1.tipo AS s1_tipo,
                   s2.nome AS s2_nome, s2.tipo AS s2_tipo
            FROM item_pedido ip
            JOIN pizza p ON ip.pizza_id = p.id
            JOIN sabor s1 ON p.sabor1_id = s1.id
            LEFT JOIN sabor s2 ON p.sabor2_id = s2.id
            WHERE ip.pedido_id = ?
        """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, pedidoId);
            try (ResultSet rs = ps.executeQuery()) {
                PrecoDAO precoDAO = new PrecoDAO();
                TabelaPreco tabela = precoDAO.buscar();
                while (rs.next()) {
                    Forma forma = reconstruirForma(rs.getString("forma"), rs.getDouble("dimensao"));
                    Sabor sabor1 = new Sabor(rs.getInt("sabor1_id"), rs.getString("s1_nome"),
                            pizzaria.model.TipoPizza.valueOf(rs.getString("s1_tipo")));
                    Sabor sabor2 = null;
                    if (rs.getObject("sabor2_id") != null) { // getObject() em vez de getInt() porque getInt() retorna 0 quando o campo é NULL, aí não dá pra distinguir de id real
                        sabor2 = new Sabor(rs.getInt("sabor2_id"), rs.getString("s2_nome"),
                                pizzaria.model.TipoPizza.valueOf(rs.getString("s2_tipo")));
                    }
                    Pizza pizza = new Pizza(rs.getInt("id"), forma, sabor1, sabor2, tabela);
                    itens.add(new ItemPedido(pizza));
                }
            }
        }
        return itens;
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        ClienteDAO clienteDAO = new ClienteDAO(); // reconstrói o objeto Cliente completo a partir do id armazenado no pedido
        Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
        
        Pedido pedido = new Pedido(rs.getInt("id"), cliente);
        pedido.mudarEstado(EstadoPedido.valueOf(rs.getString("estado"))); 
        pedido.setPrecoTotal(rs.getDouble("preco_total")); 

        for (ItemPedido item : carregarItensDoPedido(pedido.getId())) {
            pedido.adicionarItem(item);
        }
        return pedido;
    }
    
    private Forma reconstruirForma(String nome, double dimensao) {
        return switch (nome) {
            case "Circular" -> new Circulo(dimensao);
            case "Quadrado" -> new Quadrado(dimensao);
            case "Triangular" -> new Triangulo(dimensao);
            default -> throw new IllegalArgumentException("Forma desconhecida: " + nome);
        };
    }
}
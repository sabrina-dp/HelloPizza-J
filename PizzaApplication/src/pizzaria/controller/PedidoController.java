/*
 * O controlador central que orquestra a criação do pedido, adição de itens (pizzas montadas com formas e sabores) e fechamento do valor total.
 */

package pizzaria.controller;

import java.util.ArrayList;
import java.util.List;
import Pedido;
import Cliente;
import ItemPedido;
import Pizza;
import forma.Forma;
import Sabor;
import TabelaPreco;

public class PedidoController {
    private List<Pedido> todosPedidos;
    private PrecoController precoController;

    public PedidoController(PrecoController precoController) {
        this.todosPedidos = new ArrayList<>();
        this.precoController = precoController;
    }

    // Inicia um novo pedido para um determinado cliente
    public Pedido iniciarNovoPedido(int idPedido, Cliente cliente) {
        Pedido novoPedido = new Pedido(idPedido, cliente);
        todosPedidos.add(novoPedido);
        return novoPedido;
    }

    // Cria a pizza, gera o item correspondente e adiciona diretamente ao pedido
    public void adicionarPizzaAoPedido(Pedido pedido, Forma forma, Sabor s1, Sabor s2) {
        TabelaPreco tabela = precoController.getTabelaAtual();
        
        // Monta a estrutura conceitual mapeada no diagrama
        Pizza novaPizza = new Pizza(forma, s1, s2, tabela);
        ItemPedido novoItem = new ItemPedido(novaPizza);
        
        pedido.adicionarItem(novoItem);
    }

    // Fecha o pedido e retorna o preço calculado final
    public double fecharECalcularTotal(Pedido pedido) {
        if (pedido != null) {
            return pedido.calcularTotal();
        }
        return 0.0;
    }

    public List<Pedido> obterTodosPedidos() {
        return todosPedidos;
    }
}

package pizzaria.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private int id;
    private EstadoPedido estado;
    private double precoTotal;
    private Cliente cliente;
    private List<ItemPedido> itens;

    public Pedido(int id, Cliente cliente) {
        this.id = id;
        this.cliente = cliente;
        this.estado = EstadoPedido.ABERTO; // Todo pedido inicia aberto
        this.itens = new ArrayList<>();
        this.precoTotal = 0.0;
    }

    public void adicionarItem(ItemPedido item) {
        if (item != null) {
            this.itens.add(item);
        }
    }

    // Calcula o preço final percorrendo a composição de itens
    public double calcularTotal() {
        this.precoTotal = 0.0;
        for (ItemPedido item : itens) {
            this.precoTotal += item.getPrecoItem();
        }
        return this.precoTotal;
    }

    public void mudarEstado(EstadoPedido novoEstado) {
        if (novoEstado != null) {
            this.estado = novoEstado;
        }
    }

    public int getId() { return id; }
    public EstadoPedido getEstado() { return estado; }
    public double getPrecoTotal() { return precoTotal; }
    public void setPrecoTotal(double precoTotal) { this.precoTotal = precoTotal; }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return itens; }
}

package pizzaria.model;

public class ItemPedido {
    private int id;
    private Pizza pizza;

    public ItemPedido(int id, Pizza pizza) {
        if (pizza == null) throw new IllegalArgumentException("O item do pedido deve conter uma pizza.");
        this.id = id;
        this.pizza = pizza;
    }

    // inicializa id como 0 antes de realmente existir (ser gerado pelo banco)
    public ItemPedido(Pizza pizza) {
        this(0, pizza);
    }

    public double getPrecoItem() {
        return pizza.calcularPreco();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Pizza getPizza() { return pizza; }
}

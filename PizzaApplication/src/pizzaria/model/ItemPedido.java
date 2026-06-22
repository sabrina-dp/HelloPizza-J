/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaria.model;

/**
 *
 * @author sabrina
 */
public class ItemPedido {
    private Pizza pizza;

    public ItemPedido(Pizza pizza) {
        if (pizza == null) throw new IllegalArgumentException("O item do pedido deve conter uma pizza.");
        this.pizza = pizza;
    }

    public double getPrecoItem() {
        return pizza.calcularPreco();
    }

    public Pizza getPizza() { return pizza; }
}

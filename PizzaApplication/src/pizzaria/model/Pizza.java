/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaria.model;

/**
 *
 * @author Harumi Ivanaga
 */
// Pizza.java
public class Pizza {
    private Forma forma;
    private Sabor sabor1;
    private Sabor sabor2; // Pode ser null se a pizza for de apenas 1 sabor
    private TabelaPreco tabelaPreco;

    public Pizza(Forma forma, Sabor sabor1, Sabor sabor2, TabelaPreco tabelaPreco) {
        if (forma == null) throw new IllegalArgumentException("A pizza deve ter uma forma geométrica.");
        if (sabor1 == null) throw new IllegalArgumentException("A pizza deve ter pelo menos um sabor principal.");
        if (tabelaPreco == null) throw new IllegalArgumentException("A tabela de preços é obrigatória.");
        
        this.forma = forma;
        this.sabor1 = sabor1;
        this.sabor2 = sabor2;
        this.tabelaPreco = tabelaPreco;
    }

    public double calcularPreco() {
        double area = forma.calcularArea();
        double valorSabor1 = obterPrecoPorUnidadeArea(sabor1);
        
        double valorFinalPorArea = valorSabor1;
        
        // Se houver dois sabores, calcula a média simples do valor por cm² deles
        if (sabor2 != null) {
            double valorSabor2 = obterPrecoPorUnidadeArea(sabor2);
            valorFinalPorArea = (valorSabor1 + valorSabor2) / 2.0;
        }

        return area * valorFinalPorArea;
    }

    private double obterPrecoPorUnidadeArea(Sabor sabor) {
        if (sabor == null || sabor.getTipo() == null) return 0.0;
        switch (sabor.getTipo()) {
            case SIMPLES: return tabelaPreco.getPrecoSimples();
            case ESPECIAL: return tabelaPreco.getPrecoEspecial();
            case PREMIUM: return tabelaPreco.getPrecoPremium();
            default: return 0.0;
        }
    }

    public Forma getForma() { return forma; }
    public Sabor getSabor1() { return sabor1; }
    public Sabor getSabor2() { return sabor2; }
}

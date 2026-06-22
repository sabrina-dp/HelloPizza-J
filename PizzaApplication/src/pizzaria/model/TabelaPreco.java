/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaria.model;

/**
 *
 * @author Harumi Ivanaga
 */
public class TabelaPreco {
    private double precoSimples;
    private double precoEspecial;
    private double precoPremium;

    public TabelaPreco(double precoSimples, double precoEspecial, double precoPremium) {
        this.precoSimples = precoSimples;
        this.precoEspecial = precoEspecial;
        this.precoPremium = precoPremium;
    }

    public double getPrecoSimples() { return precoSimples; }
    public void setPrecoSimples(double precoSimples) { this.precoSimples = precoSimples; }
    
    public double getPrecoEspecial() { return precoEspecial; }
    public void setPrecoEspecial(double precoEspecial) { this.precoEspecial = precoEspecial; }
    
    public double getPrecoPremium() { return precoPremium; }
    public void setPrecoPremium(double precoPremium) { this.precoPremium = precoPremium; }
}

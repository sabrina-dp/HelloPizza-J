package pizzaria.model.forma;

/**
 * Representa a forma geométrica de uma pizza.
 * Subclasses definem o cálculo de área e os limites de dimensão.
 */
public abstract class Forma {
    public static final double AREA_MIN = 100.0;
    public static final double AREA_MAX = 1600.0;

    protected double dimensao;

    public Forma(double dimensao, double dimensaoMin, double dimensaoMax, String nomeDimensao) {
        if (dimensao < dimensaoMin || dimensao > dimensaoMax)
            throw new IllegalArgumentException(
                String.format("%s deve estar entre %.0f e %.0f cm.", nomeDimensao, dimensaoMin, dimensaoMax));
        this.dimensao = dimensao;
    }

    public double getDimensao() { return this.dimensao; }
    public abstract double calcularArea();
    public abstract String getNome();
    public abstract double calcularDimensaoPorArea(double area);

    @Override
    public String toString() {
        return String.format("%s | Dimensão: %.2f cm | Área: %.2f cm²", getNome(), getDimensao(), calcularArea());
    }
}
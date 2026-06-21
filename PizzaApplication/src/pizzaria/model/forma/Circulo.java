package pizzaria.model.forma;

public class Circulo extends Forma {
    public static final double RAIO_MIN = 7.0;
    public static final double RAIO_MAX = 23.0;

    public Circulo(double raio) {
        super(raio, RAIO_MIN, RAIO_MAX, "O raio");
    }

    @Override 
    public double calcularArea() { return Math.PI * dimensao * dimensao; }
    
    @Override 
    public String getNome() { return "Circular"; }

    @Override 
    public double calcularDimensaoPorArea(double area) { 
        return Math.sqrt(area / Math.PI); 
    }
}

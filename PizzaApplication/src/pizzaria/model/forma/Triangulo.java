package pizzaria.model.forma;

public class Triangulo extends Forma {
    public static final double LADO_MIN = 20.0;
    public static final double LADO_MAX = 60.0;

    public Triangulo(double lado) {
        super(lado, LADO_MIN, LADO_MAX, "O lado do triângulo");
    }

    @Override 
    public double calcularArea() { 
        return (Math.sqrt(3.0) / 4.0) * dimensao * dimensao; 
    }

    @Override 
    public String getNome() { return "Triangular"; }

    @Override 
    public double calcularDimensaoPorArea(double area) { 
        return Math.sqrt((4.0 * area) / Math.sqrt(3.0)); 
    }
}

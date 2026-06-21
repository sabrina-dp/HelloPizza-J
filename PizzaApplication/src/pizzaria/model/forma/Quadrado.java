package pizzaria.model.forma;

public class Quadrado extends Forma {
    public static final double LADO_MIN = 10.0;
    public static final double LADO_MAX = 40.0;

    public Quadrado(double lado) {
        super(lado, LADO_MIN, LADO_MAX, "O lado do quadrado");
    }

    @Override 
    public double calcularArea() { return dimensao * dimensao; }

    @Override 
    public String getNome() { return "Quadrado"; }

    @Override 
    public double calcularDimensaoPorArea(double area) { 
        return Math.sqrt(area); 
    }
}

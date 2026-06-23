
package pizzaria.model;

public class Sabor {
       
    private int id;
    private String nome;
    private TipoPizza tipo;
  
    public Sabor(int id, String nome, TipoPizza tipo) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
    }
        
    public int getId(){
        return id;
    }
      
    public void setId(int id) {
        this.id = id;
    }
      
    public String getNome(){
        return nome;
    }
    
    public void setNome(String nome) {
        if(nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome;
    }
    
    public TipoPizza getTipo (){
        return tipo;
    }
    
    public void setTipo(TipoPizza tipo) {
        if(tipo == null) {
            throw new IllegalArgumentException("Tipo da pizza é obrigatório.");
        }
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return nome + " (" + tipo + ")";
    }
}

package pizzaria.model;


public class Cliente {
    private int ID;
    private String nome;
    private String sobrenome;
    private String telefone;
    
    
    public Cliente () {}
    //atributos do cliente
    public Cliente (int ID, String nome, String sobrenome, String telefone ){
        this.ID = ID;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
            }
    //métodos, getters e setters.
    public int GetID(){
        return ID;
    }
    
      
    public void SetID(int ID){
        this.ID = ID;
    }
    
    public String Getnome(){
        return nome;
    }
    
   public void setnome(String nome) {
        if(nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        }
        this.nome = nome;
    }
    
    public String Getsobrenome(){
        return sobrenome;
    }
    
     public void setsobrenome(String sobrenome) {
        if( sobrenome == null || sobrenome.trim().isEmpty()) {
            throw new IllegalArgumentException("Sobrenome não pode ser vazio.");
        }
        this.sobrenome = sobrenome;
    }
     
    public String Gettelefone(){
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        if(telefone == null || telefone.trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone inválido.");
        }
        this.telefone = telefone;
    }

    public String getNomeCompleto() {
        return nome + " " + sobrenome;
    }

    @Override
    public String toString() {
        return nome + " " + sobrenome + " - " + telefone;
    }
}
    
    



package pizzaria.model;


public class Cliente {
    private int ID;
    private String nome;
    private String sobrenome;
    private String telefone;
    
    //atributos do cliente
    public Cliente (int ID, String nome, String sobrenome, String telefone ){
        this.ID = ID;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
    }

    //métodos, getters e setters.
    public int getId(){
        return ID;
    }
    
    public void setId(int ID){
        this.ID = ID;
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
    
    public String getSobrenome(){
        return sobrenome;
    }
    
     public void setSobrenome(String sobrenome) {
        if( sobrenome == null || sobrenome.trim().isEmpty()) {
            throw new IllegalArgumentException("Sobrenome não pode ser vazio.");
        }
        this.sobrenome = sobrenome;
    }
     
    public String getTelefone(){
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
    
    



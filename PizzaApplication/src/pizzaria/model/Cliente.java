package pizzaria.model;


public class Cliente {
    private int ID;
    private String nome;
    private String sobrenome;
    private String telefone;
    
    //atributos do cliente
    public Cliente(int ID, String nome, String sobrenome, String telefone) {
        this.ID = ID;
        setNome(nome);      
        setSobrenome(sobrenome);
        setTelefone(telefone);
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
        if (nome == null || nome.trim().isEmpty())
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        if (!nome.matches("^[A-Za-zÀ-ÿ\\s]+$"))
            throw new IllegalArgumentException("Nome deve conter apenas letras.");
        this.nome = nome;
    }
    
    public String getSobrenome(){
        return sobrenome;
    }
    
     public void setSobrenome(String sobrenome) {
        if (sobrenome == null || sobrenome.trim().isEmpty())
            throw new IllegalArgumentException("Sobrenome não pode ser vazio.");
        if (!sobrenome.matches("^[A-Za-zÀ-ÿ\\s]+$"))
            throw new IllegalArgumentException("Sobrenome deve conter apenas letras.");
        this.sobrenome = sobrenome;
    }
     
    public String getTelefone(){
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty())
            throw new IllegalArgumentException("Telefone não pode ser vazio.");
        if (!telefone.matches("^[0-9\\-\\(\\)\\s]{8,15}$"))
            throw new IllegalArgumentException("Telefone inválido. Use apenas números.");
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
    
    



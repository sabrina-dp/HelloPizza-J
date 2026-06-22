/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaria.model;

/**
 *
 * @author sabrina
 */
public class Cliente {
    private int ID;
    private String nome;
    private String sobrenome;
    private String telefone;
    
    
    public Cliente () {}
    
    public Cliente (int ID, String nome, String sobrenome, String telefone ){
        this.ID = ID;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.telefone = telefone;
            }
    
    public int GetID(){
        return ID;
    }
    
      
    public void SetID(){
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
    
    



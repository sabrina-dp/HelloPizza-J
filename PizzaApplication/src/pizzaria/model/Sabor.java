/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pizzaria.model;

/**
 *
 * @author sabrina
 */
public class Sabor {
       
    private int id;
    private String nome;
    private TipoPizza tipo;
  
    public Sabor(){}
    
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
    
}
}

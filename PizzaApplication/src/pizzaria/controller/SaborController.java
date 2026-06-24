/*
 * Controla os sabores disponíveis na pizzaria.
 */

package pizzaria.controller;

import java.util.ArrayList;
import java.util.List;
import Sabor;
import TipoPizza;

public class SaborController {
    private List<Sabor> saboresDisponiveis;

    public SaborController() {
        this.saboresDisponiveis = new ArrayList<>();
    }

    public void cadastrarSabor(Sabor sabor) {
        if (sabor != null) {
            this.saboresDisponiveis.add(sabor);
        }
    }

    public List<Sabor> listarSabores() {
        return this.saboresDisponiveis;
    }
    
    public Sabor buscarPorNome(String nome) {
        for (Sabor s : saboresDisponiveis) {
            if (s.Getnome().equalsIgnoreCase(nome)) {
                return s;
            }
        }
        return null;
    }
}

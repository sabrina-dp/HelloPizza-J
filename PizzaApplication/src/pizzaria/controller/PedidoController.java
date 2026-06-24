/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
 * Gerencia as taxas e valores por unidade de área configuradas na TabelaPreco.
 */

package pizzaria.controller;

import TabelaPreco;

public class PrecoController {
    private TabelaPreco tabelaPreco;

    public PrecoController(TabelaPreco tabelaPreco) {
        this.tabelaPreco = tabelaPreco;
    }

    public TabelaPreco getTabelaAtual() {
        return this.tabelaPreco;
    }

    // Atualiza os valores dinamicamente se necessário
    public void atualizarValores(double simples, double especial, double premium) {
        this.tabelaPreco.setPrecoSimples(simples);
        this.tabelaPreco.setPrecoEspecial(especial);
        this.tabelaPreco.setPrecoPremium(premium);
    }
}

/*
 * Gerencia as taxas e valores por unidade de área configuradas na TabelaPreco.
 */

package pizzaria.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import pizzaria.dao.PrecoDAO;
import pizzaria.model.TabelaPreco;

public class PrecoController {

    @FXML
    private TextField txtPrecoSimples;

    @FXML
    private TextField txtPrecoEspecial;

    @FXML
    private TextField txtPrecoPremium;

    @FXML
    private Label lblStatus;

    private PrecoDAO precoDAO;

    @FXML
    public void initialize() {

        precoDAO = new PrecoDAO();

        carregarPrecos();
    }

    @FXML
    private void carregarPrecos() {

        TabelaPreco tabela = precoDAO.buscar();

        if (tabela != null) {

            txtPrecoSimples.setText(
                    String.valueOf(tabela.getPrecoSimples()));

            txtPrecoEspecial.setText(
                    String.valueOf(tabela.getPrecoEspecial()));

            txtPrecoPremium.setText(
                    String.valueOf(tabela.getPrecoPremium()));
        }
    }

    @FXML
    private void salvarPrecos() {

        try {

            TabelaPreco tabela = new TabelaPreco(
                    Double.parseDouble(txtPrecoSimples.getText()),
                    Double.parseDouble(txtPrecoEspecial.getText()),
                    Double.parseDouble(txtPrecoPremium.getText()));

            if (precoDAO.atualizar(tabela)) {

                lblStatus.setText("Preços atualizados com sucesso.");

            } else {

                lblStatus.setText("Erro ao atualizar preços.");
            }

        } catch (NumberFormatException e) {

            lblStatus.setText("Digite apenas números válidos.");
        }
    }
}

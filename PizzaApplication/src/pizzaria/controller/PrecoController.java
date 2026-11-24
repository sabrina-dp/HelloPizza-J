package pizzaria.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import pizzaria.dao.DAOFactory;
import pizzaria.dao.DAOType;
import pizzaria.dao.PrecoDAO;
import pizzaria.model.TabelaPreco;
import java.sql.SQLException;

public class PrecoController {

    @FXML private TextField txtPrecoSimples;
    @FXML private TextField txtPrecoEspecial;
    @FXML private TextField txtPrecoPremium;

    private PrecoDAO precoDAO;

    @FXML
    public void initialize() {
        precoDAO = DAOFactory.getPrecoDAO(DAOType.SQL);
        carregarPrecos();
    }

    @FXML
    private void carregarPrecos() {
        try {
            TabelaPreco tabela = precoDAO.buscar();
            if (tabela != null) {
                txtPrecoSimples.setText(String.valueOf(tabela.getPrecoSimples()));
                txtPrecoEspecial.setText(String.valueOf(tabela.getPrecoEspecial()));
                txtPrecoPremium.setText(String.valueOf(tabela.getPrecoPremium()));
            }
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar preços: " + e.getMessage());
        }
    }

    @FXML
    private void salvarPrecos() {
        String simplesStr = txtPrecoSimples.getText().trim();
        String especialStr = txtPrecoEspecial.getText().trim();
        String premiumStr = txtPrecoPremium.getText().trim();

        if (simplesStr.isEmpty() || especialStr.isEmpty() || premiumStr.isEmpty()) {
            mostrarErro("Preencha todos os campos.");
            return;
        }

        try {
            double simples = Double.parseDouble(simplesStr);
            double especial = Double.parseDouble(especialStr);
            double premium = Double.parseDouble(premiumStr);

            if (simples < 0 || especial < 0 || premium < 0) {
                mostrarErro("Os valores de preço devem ser maiores ou iguais a zero.");
                return;
            }

            TabelaPreco tabela = new TabelaPreco(simples, especial, premium);

            if (precoDAO.atualizar(tabela)) {
                mostrarInfo("Preços atualizados com sucesso!");
                carregarPrecos();
            } else {
                mostrarErro("Não foi possível atualizar os preços no banco de dados.");
            }
        } catch (NumberFormatException e) {
            mostrarErro("Digite apenas valores numéricos válidos.");
        } catch (SQLException e) {
            mostrarErro("Erro de banco de dados ao salvar preços: " + e.getMessage());
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
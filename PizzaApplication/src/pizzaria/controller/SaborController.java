/*
 * Controla os sabores disponíveis na pizzaria.
 */

package pizzaria.controller;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import pizzaria.dao.SaborDAO;
import pizzaria.model.Sabor;
import pizzaria.model.TipoPizza;

public class SaborController {

    @FXML
    private TextField txtNomeSabor;

    @FXML
    private TextField txtPesquisa;

    @FXML
    private ComboBox<TipoPizza> cbTipoPizza;

    @FXML
    private TableView<Sabor> tblSabores;

    @FXML
    private TableColumn<Sabor, Integer> colId;

    @FXML
    private TableColumn<Sabor, String> colNome;

    @FXML
    private TableColumn<Sabor, TipoPizza> colTipo;

    @FXML
    private Label lblTotalSabores;

    private SaborDAO saborDAO;

    @FXML
    public void initialize() {

        saborDAO = new SaborDAO();

        cbTipoPizza.getItems().addAll(TipoPizza.values());

        carregarSabores();
    }

    @FXML
    private void cadastrarSabor() {

        try {

            Sabor sabor = new Sabor(
                    0,
                    txtNomeSabor.getText(),
                    cbTipoPizza.getValue());

            saborDAO.inserir(sabor);

            carregarSabores();

            limparCampos();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void pesquisarSabor() {

        String nome = txtPesquisa.getText();

        try {

            var lista = saborDAO.listarTodos();

            var filtrada = lista.stream()
                    .filter(s -> s.getNome().toLowerCase()
                            .contains(nome.toLowerCase()))
                    .toList();

            tblSabores.setItems(
                    FXCollections.observableArrayList(filtrada));

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void excluirSabor() {

        Sabor selecionado =
                tblSabores.getSelectionModel().getSelectedItem();

        if (selecionado == null)
            return;

        try {

            saborDAO.remover(selecionado.getId());

            carregarSabores();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void atualizarSabor() {

        Sabor selecionado =
                tblSabores.getSelectionModel().getSelectedItem();

        if (selecionado == null)
            return;

        try {

            Sabor atualizado = new Sabor(
                    selecionado.getId(),
                    txtNomeSabor.getText(),
                    cbTipoPizza.getValue());

            saborDAO.atualizar(atualizado);

            carregarSabores();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void limparCampos() {

        txtNomeSabor.clear();
        txtPesquisa.clear();
        cbTipoPizza.setValue(null);
    }

    private void carregarSabores() {

        try {

            tblSabores.setItems(
                    FXCollections.observableArrayList(
                            saborDAO.listarTodos()));

            lblTotalSabores.setText(
                    "Total de sabores: "
                    + saborDAO.listarTodos().size());

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
}

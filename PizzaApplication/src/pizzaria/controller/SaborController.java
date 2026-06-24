/*
 * Controla os sabores disponíveis na pizzaria.
 */

package pizzaria.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import pizzaria.model.Sabor;
import pizzaria.model.TipoPizza;

public class SaborController {

    private List<Sabor> saboresDisponiveis;

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

    public SaborController() {

        saboresDisponiveis = new ArrayList<>();

    }

    @FXML
    public void initialize() {

        cbTipoPizza.getItems().addAll(
                TipoPizza.values()
        );

        atualizarTotal();

    }

    @FXML
    private void cadastrarSabor() {

        String nome = txtNomeSabor.getText();
        TipoPizza tipo = cbTipoPizza.getValue();

        System.out.println("Cadastrar sabor:");
        System.out.println(nome);
        System.out.println(tipo);

    }

    @FXML
    private void atualizarSabor() {

        System.out.println("Atualizar sabor");

    }

    @FXML
    private void excluirSabor() {

        System.out.println("Excluir sabor");

    }

    @FXML
    private void pesquisarSabor() {

        String pesquisa = txtPesquisa.getText();

        System.out.println("Pesquisar: " + pesquisa);

    }

    @FXML
    private void limparCampos() {

        txtNomeSabor.clear();
        cbTipoPizza.setValue(null);

    }

    private void atualizarTotal() {

        lblTotalSabores.setText(
                "Total de sabores: " + saboresDisponiveis.size()
        );

    }

    public List<Sabor> listarSabores() {

        return saboresDisponiveis;

    }

    public Sabor buscarPorNome(String nome) {

        for (Sabor s : saboresDisponiveis) {

            if (s.getNome().equalsIgnoreCase(nome)) {

                return s;

            }

        }

        return null;

    }

}

package pizzaria.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;
import java.util.List;

import pizzaria.dao.DAOFactory;
import pizzaria.dao.DAOType;
import pizzaria.dao.SaborDAO;
import pizzaria.model.Sabor;
import pizzaria.model.TipoPizza;

public class SaborController {

    @FXML private TextField txtNomeSabor;
    @FXML private TextField txtPesquisa;
    @FXML private ComboBox<TipoPizza> cbTipoPizza;
    @FXML private TableView<Sabor> tblSabores;
    @FXML private TableColumn<Sabor, Integer> colId;
    @FXML private TableColumn<Sabor, String> colNome;
    @FXML private TableColumn<Sabor, String> colTipo;
    @FXML private Label lblTotalSabores;

    private final SaborDAO saborDAO = DAOFactory.getSaborDAO(DAOType.SQL);
    private final ObservableList<Sabor> listaSabores = FXCollections.observableArrayList();
    private Sabor saborSelecionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colTipo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTipo().name()));

        cbTipoPizza.getItems().addAll(TipoPizza.values());

        tblSabores.setItems(listaSabores);

        // Clicando em uma linha preenche os campos para edição
        tblSabores.getSelectionModel().selectedItemProperty().addListener((obs, anterior, selecionado) -> {
            if (selecionado != null) {
                saborSelecionado = selecionado;
                txtNomeSabor.setText(selecionado.getNome());
                cbTipoPizza.setValue(selecionado.getTipo());
            }
        });

        carregarSabores();
    }

    @FXML
    private void cadastrarSabor() {
        String nome = txtNomeSabor.getText().trim();
        TipoPizza tipo = cbTipoPizza.getValue();

        if (nome.isEmpty() || tipo == null) {
            mostrarErro("Preencha todos os campos.");
            return;
        }

        try {
            saborDAO.inserir(new Sabor(0, nome, tipo));
            limparCampos();
            carregarSabores();
            mostrarInfo("Sabor cadastrado com sucesso!");
        } catch (SQLException e) {
            mostrarErro("Erro ao cadastrar sabor: " + e.getMessage());
        }
    }

    @FXML
    private void atualizarSabor() {
        if (saborSelecionado == null) {
            mostrarErro("Selecione um sabor na tabela para atualizar.");
            return;
        }

        String nome = txtNomeSabor.getText().trim();
        TipoPizza tipo = cbTipoPizza.getValue();

        if (nome.isEmpty() || tipo == null) {
            mostrarErro("Preencha todos os campos.");
            return;
        }

        try {
            saborDAO.atualizar(new Sabor(saborSelecionado.getId(), nome, tipo));
            limparCampos();
            carregarSabores();
            mostrarInfo("Sabor atualizado com sucesso!");
        } catch (SQLException e) {
            mostrarErro("Erro ao atualizar sabor: " + e.getMessage());
        }
    }

    @FXML
    private void excluirSabor() {
        if (saborSelecionado == null) {
            mostrarErro("Selecione um sabor na tabela para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Excluir o sabor \"" + saborSelecionado.getNome() + "\"?");
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                try {
                    saborDAO.remover(saborSelecionado.getId());
                    limparCampos();
                    carregarSabores();
                    mostrarInfo("Sabor excluído com sucesso!");
                } catch (SQLException e) {
                    if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                        mostrarErro("Não é possível excluir este sabor. Ele já está vinculado a uma pizza ou pedido existente no sistema.");
                    } else {
                        mostrarErro("Erro ao excluir sabor: " + e.getMessage());
                    }
                }
            }
        });
    }

    @FXML
    private void pesquisarSabor() {
        String filtro = txtPesquisa.getText().trim();

        if (filtro.isEmpty()) {
            carregarSabores();
            return;
        }

        try {
            List<Sabor> resultado = saborDAO.listarTodos();
            List<Sabor> filtrada = resultado.stream()
                    .filter(s -> s.getNome().toLowerCase().contains(filtro.toLowerCase()))
                    .toList();
            listaSabores.setAll(filtrada);
            lblTotalSabores.setText("Total de sabores: " + filtrada.size());
        } catch (SQLException e) {
            mostrarErro("Erro ao pesquisar: " + e.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        txtNomeSabor.clear();
        txtPesquisa.clear();
        cbTipoPizza.setValue(null);
        saborSelecionado = null;
        tblSabores.getSelectionModel().clearSelection();
    }

    private void carregarSabores() {
        try {
            List<Sabor> sabores = saborDAO.listarTodos();
            listaSabores.setAll(sabores);
            lblTotalSabores.setText("Total de sabores: " + sabores.size());
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar sabores: " + e.getMessage());
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

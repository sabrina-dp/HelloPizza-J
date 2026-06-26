package pizzaria.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;
import java.util.List;

import pizzaria.dao.ClienteDAO;
import pizzaria.model.Cliente;

public class ClienteController {

    @FXML private TextField txtNome;
    @FXML private TextField txtSobrenome;
    @FXML private TextField txtTelefone;
    @FXML private TextField txtFiltroSobrenome;
    @FXML private TextField txtFiltroTelefone;
    @FXML private TableView<Cliente> tblClientes;
    @FXML private TableColumn<Cliente, Integer> colId;
    @FXML private TableColumn<Cliente, String> colNome;
    @FXML private TableColumn<Cliente, String> colSobrenome;
    @FXML private TableColumn<Cliente, String> colTelefone;

    final ClienteDAO clienteDAO = new ClienteDAO();
    final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private Cliente clienteSelecionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colNome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNome()));
        colSobrenome.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSobrenome()));
        colTelefone.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefone()));

        tblClientes.setItems(listaClientes);

        // Quando o usuário clica em uma linha, preenche os campos para edição
        tblClientes.getSelectionModel().selectedItemProperty().addListener((obs, anterior, selecionado) -> {
            if (selecionado != null) {
                clienteSelecionado = selecionado;
                txtNome.setText(selecionado.getNome());
                txtSobrenome.setText(selecionado.getSobrenome());
                txtTelefone.setText(selecionado.getTelefone());
            }
        });

        carregarClientes();
    }

    @FXML
    private void cadastrarCliente() {
        String nome = txtNome.getText().trim();
        String sobrenome = txtSobrenome.getText().trim();
        String telefone = txtTelefone.getText().trim();

        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            mostrarErro("Preencha todos os campos.");
            return;
        }

        try {
            Cliente novo = new Cliente(0, nome, sobrenome, telefone);
            clienteDAO.inserir(novo);
            limparCampos();
            carregarClientes();
            mostrarInfo("Cliente cadastrado com sucesso!");
        } catch (SQLException e) {
            mostrarErro("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    @FXML
    private void atualizarCliente() {
        if (clienteSelecionado == null) {
            mostrarErro("Selecione um cliente na tabela para atualizar.");
            return;
        }

        String nome = txtNome.getText().trim();
        String sobrenome = txtSobrenome.getText().trim();
        String telefone = txtTelefone.getText().trim();

        if (nome.isEmpty() || sobrenome.isEmpty() || telefone.isEmpty()) {
            mostrarErro("Preencha todos os campos.");
            return;
        }

        try {
            clienteSelecionado.setNome(nome);
            clienteSelecionado.setSobrenome(sobrenome);
            clienteSelecionado.setTelefone(telefone);
            clienteDAO.atualizar(clienteSelecionado);
            limparCampos();
            carregarClientes();
            mostrarInfo("Cliente atualizado com sucesso!");
        } catch (SQLException e) {
            mostrarErro("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    @FXML
    private void excluirCliente() {
        if (clienteSelecionado == null) {
            mostrarErro("Selecione um cliente na tabela para excluir.");
            return;
        }

        // Pede confirmação antes de excluir
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Excluir " + clienteSelecionado.getNomeCompleto() + "? Seus pedidos também serão excluídos.");
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.OK) {
                try {
                    clienteDAO.remover(clienteSelecionado.getId());
                    limparCampos();
                    carregarClientes();
                    mostrarInfo("Cliente excluído com sucesso!");
                } catch (SQLException e) {
                    mostrarErro("Erro ao excluir cliente: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void pesquisarCliente() {
        String filtro = txtFiltroSobrenome.getText().trim();

        if (filtro.isEmpty()) {
            filtro = txtFiltroTelefone.getText().trim();
        }

        if (filtro.isEmpty()) {
            carregarClientes();
            return;
        }

        try {
            List<Cliente> resultado = clienteDAO.buscarFiltrado(filtro);
            listaClientes.setAll(resultado);
        } catch (SQLException e) {
            mostrarErro("Erro ao pesquisar: " + e.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        txtNome.clear();
        txtSobrenome.clear();
        txtTelefone.clear();
        clienteSelecionado = null;
        tblClientes.getSelectionModel().clearSelection();
    }

    private void carregarClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            listaClientes.setAll(clientes);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar clientes: " + e.getMessage());
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

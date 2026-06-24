package pizzaria.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import pizzaria.model.Cliente;

public class ClienteController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtSobrenome;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtFiltroSobrenome;

    @FXML
    private TextField txtFiltroTelefone;

    @FXML
    private TableView<Cliente> tblClientes;

    @FXML
    private TableColumn<Cliente,Integer> colId;

    @FXML
    private TableColumn<Cliente,String> colNome;

    @FXML
    private TableColumn<Cliente,String> colSobrenome;

    @FXML
    private TableColumn<Cliente,String> colTelefone;

    @FXML
    public void initialize() {

        System.out.println("Tela Cliente carregada.");

    }

    @FXML
    private void cadastrarCliente() {

        String nome = txtNome.getText();
        String sobrenome = txtSobrenome.getText();
        String telefone = txtTelefone.getText();

        System.out.println("Cadastrar Cliente");
        System.out.println(nome);
        System.out.println(sobrenome);
        System.out.println(telefone);

    }

    @FXML
    private void atualizarCliente() {

        System.out.println("Atualizar Cliente");

    }

    @FXML
    private void excluirCliente() {

        System.out.println("Excluir Cliente");

    }

    @FXML
    private void pesquisarCliente() {

        System.out.println("Pesquisar Cliente");

    }

    @FXML
    private void limparCampos() {

        txtNome.clear();
        txtSobrenome.clear();
        txtTelefone.clear();

    }

}

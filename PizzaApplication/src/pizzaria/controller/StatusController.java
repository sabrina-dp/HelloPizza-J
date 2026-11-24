package pizzaria.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import pizzaria.dao.DAOFactory;
import pizzaria.dao.DAOType;
import pizzaria.dao.PedidoDAO;
import pizzaria.model.Pedido;
import pizzaria.model.EstadoPedido;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StatusController {

    @FXML
    private TextField txtPesquisaCliente;

    @FXML
    private TableView<Pedido> tblPedidos;

    @FXML
    private TableColumn<Pedido, Integer> colId;

    @FXML
    private TableColumn<Pedido, String> colCliente;

    @FXML
    private TableColumn<Pedido, String> colTelefone;

    @FXML
    private TableColumn<Pedido, Double> colValor;

    @FXML
    private TableColumn<Pedido, String> colEstado;

    @FXML
    private Label lblPedidoSelecionado;

    @FXML
    private ComboBox<EstadoPedido> cbEstado;

    private ObservableList<Pedido> pedidosExibidos;
    private PedidoDAO pedidoDAO;

    public StatusController() {
        this.pedidosExibidos = FXCollections.observableArrayList();
        this.pedidoDAO = DAOFactory.getPedidoDAO(DAOType.SQL);
    }

    @FXML
    public void initialize() {
        // Vincula as colunas
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCliente() != null ? cellData.getValue().getCliente().getNome() + " " + cellData.getValue().getCliente().getSobrenome() : "Sem Cliente"
        ));
        
        colTelefone.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCliente() != null ? cellData.getValue().getCliente().getTelefone() : "N/A"
        ));
        
        colValor.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecoTotal()).asObject());
        
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));

        tblPedidos.setItems(pedidosExibidos);
        cbEstado.setItems(FXCollections.observableArrayList(EstadoPedido.values()));

        // Listener de seleção na tabela
        tblPedidos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lblPedidoSelecionado.setText("Pedido #" + newValue.getId() + " selecionado.");
                cbEstado.setValue(newValue.getEstado());
            } else {
                lblPedidoSelecionado.setText("Nenhum pedido selecionado");
            }
        });

        // Duplo clique na linha para editar o pedido
        tblPedidos.setRowFactory(tv -> {
            TableRow<Pedido> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    abrirTelaPedido(row.getItem());
                }
            });
            return row;
        });

        carregarPedidosDoBanco();
    }

    @FXML
    public void buscarPedidos() {
        String termoBusca = txtPesquisaCliente.getText().trim().toLowerCase();
        pedidosExibidos.clear();

        try {
            List<Pedido> todosPedidos = pedidoDAO.listarTodos();
            for (Pedido p : todosPedidos) {
                if (termoBusca.isEmpty()) {
                    pedidosExibidos.add(p);
                } else if (p.getCliente() != null) {
                    String nome = (p.getCliente().getNome() + " " + p.getCliente().getSobrenome()).toLowerCase();
                    String telefone = p.getCliente().getTelefone().toLowerCase();
                    
                    if (nome.contains(termoBusca) || telefone.contains(termoBusca)) {
                        pedidosExibidos.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Falha ao buscar pedidos no banco de dados.");
            e.printStackTrace();
        }
    }

    @FXML
    public void atualizarEstado() {
        Pedido pedidoSelecionado = tblPedidos.getSelectionModel().getSelectedItem();
        EstadoPedido novoEstado = cbEstado.getValue();

        if (pedidoSelecionado == null) {
            mostrarAlerta("Erro", "Por favor, selecione um pedido na tabela primeiro.");
            return;
        }

        if (novoEstado == null) {
            mostrarAlerta("Erro", "Por favor, selecione o novo estado no campo de seleção.");
            return;
        }

        try {
            // Atualiza no banco de dados
            boolean sucesso = pedidoDAO.atualizarEstado(pedidoSelecionado.getId(), novoEstado);
            if (sucesso) {
                pedidoSelecionado.mudarEstado(novoEstado);
                tblPedidos.refresh();
                mostrarAlerta("Sucesso", "O estado do pedido #" + pedidoSelecionado.getId() + " foi alterado para " + novoEstado);
            } else {
                mostrarAlerta("Erro", "Não foi possível atualizar o estado no banco de dados.");
            }
        } catch (SQLException e) {
            mostrarAlerta("Erro", "Erro ao atualizar estado no banco de dados.");
            e.printStackTrace();
        }
    }

    @FXML
    public void cadastrarPedido() {
        abrirTelaPedido(null);
    }

    @FXML
    public void editarPedidoSelecionado() {
        Pedido pedido = tblPedidos.getSelectionModel().getSelectedItem();
        if (pedido != null) {
            abrirTelaPedido(pedido);
        } else {
            mostrarAlerta("Aviso", "Selecione um pedido na tabela para editar.");
        }
    }

    private void abrirTelaPedido(Pedido pedido) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pizzaria/view/PedidoView.fxml"));
            Parent root = loader.load();

            // Pega o controlador da tela de pedido para injetar o pedido existente
            PedidoController controller = loader.getController();
            if (pedido != null) {
                controller.carregarPedido(pedido); 
            }

            Stage stage = new Stage();
            stage.setTitle(pedido == null ? "Cadastrar Novo Pedido" : "Alterar Pedido #" + pedido.getId());
            stage.setScene(new Scene(root));
            
            // Recarrega a tabela de status quando a janela de pedido for fechada (caso haja alterações)
            stage.setOnHidden(e -> carregarPedidosDoBanco()); 
            
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Erro", "Não foi possível abrir a tela de pedidos.");
            e.printStackTrace();
        }
    }

    private void carregarPedidosDoBanco() {
        txtPesquisaCliente.clear();
        buscarPedidos(); 
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
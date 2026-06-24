/*
 * Focado exclusivamente em atualizar e verificar a situação/etapa em que o pedido se encontra no fluxo de produção.
 */

package pizzaria.controller;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import pizzaria.model.Pedido;
import pizzaria.model.EstadoPedido;
import java.util.ArrayList;
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

    private List<Pedido> listaGeralPedidos;
    private ObservableList<Pedido> pedidosExibidos;

    public StatusController() {
        this.listaGeralPedidos = new ArrayList<>();
        this.pedidosExibidos = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Vincula o ID do Pedido
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        
        // Vincula o Nome do Cliente (getNomeCompleto() bate certinho com a sua classe)
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCliente() != null ? cellData.getValue().getCliente().getNomeCompleto() : "Sem Cliente"
        ));
        
        // CORRIGIDO: Agora chama getTelefone() com "g" minúsculo, alinhado com a sua nova classe Cliente!
        colTelefone.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCliente() != null ? cellData.getValue().getCliente().getTelefone() : "N/A"
        ));
        
        // Vincula o valor total do pedido
        colValor.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecoTotal()).asObject());
        
        // Vincula o Estado do Pedido
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));

        tblPedidos.setItems(pedidosExibidos);

        // Preenche o ComboBox com as opções do seu Enum EstadoPedido
        cbEstado.setItems(FXCollections.observableArrayList(EstadoPedido.values()));

        tblPedidos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lblPedidoSelecionado.setText("Pedido ID: " + newValue.getId() + " selecionado.");
                cbEstado.setValue(newValue.getEstado());
            } else {
                lblPedidoSelecionado.setText("Nenhum pedido selecionado");
            }
        });
    }

    @FXML
    public void buscarPedidos() {
        String termoBusca = txtPesquisaCliente.getText().trim().toLowerCase();
        pedidosExibidos.clear();

        for (Pedido p : listaGeralPedidos) {
            if (termoBusca.isEmpty()) {
                pedidosExibidos.add(p);
            } else if (p.getCliente() != null) {
                String nomeCompleto = p.getCliente().getNomeCompleto().toLowerCase();
                
                // CORRIGIDO: Chamando getTelefone() com "g" minúsculo aqui na busca também
                String telefone = p.getCliente().getTelefone().toLowerCase();
                
                if (nomeCompleto.contains(termoBusca) || telefone.contains(termoBusca)) {
                    pedidosExibidos.add(p);
                }
            }
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

        pedidoSelecionado.mudarEstado(novoEstado);
        
        tblPedidos.refresh();
        mostrarAlerta("Sucesso", "O estado do pedido #" + pedidoSelecionado.getId() + " foi alterado para " + novoEstado);
    }

    public void setListaGeralPedidos(List<Pedido> pedidos) {
        this.listaGeralPedidos = pedidos;
        for (Pedido p : listaGeralPedidos) {
            p.calcularTotal();
        }
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

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
import Pedido;
import EstadoPedido;
import java.util.ArrayList;
import java.util.List;

public class StatusController {

    // 1. Elementos da interface mapeados pelo FXML das suas amigas
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

    // Lista em memória de todos os pedidos do sistema (simulando o banco de dados)
    private List<Pedido> listaGeralPedidos;
    
    // Lista observável que alimenta visualmente a tabela do JavaFX
    private ObservableList<Pedido> pedidosExibidos;

    /**
     * Construtor padrão exigido pelo JavaFX
     */
    public StatusController() {
        this.listaGeralPedidos = new ArrayList<>();
        this.pedidosExibidos = FXCollections.observableArrayList();
    }

    /**
     * Método executado de forma automática ao carregar a tela.
     * Configura como as colunas vão ler os atributos do objeto 'Pedido'.
     */
    @FXML
    public void initialize() {
        // Vincula as colunas aos atributos do modelo Pedido e Cliente
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        
        colCliente.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCliente() != null ? cellData.getValue().getCliente().getNomeCompleto() : "Sem Cliente"
        ));
        
        colTelefone.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getCliente() != null ? cellData.getValue().getCliente().Gettelefone() : "N/A"
        ));
        
        colValor.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrecoTotal()).asObject());
        
        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));

        // Alimenta a tabela
        tblPedidos.setItems(pedidosExibidos);

        // Preenche o ComboBox com as opções do Enum EstadoPedido
        cbEstado.setItems(FXCollections.observableArrayList(EstadoPedido.values()));

        // Listener: Quando o usuário clica em uma linha da tabela, atualiza o Label inferior
        tblPedidos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                lblPedidoSelecionado.setText("Pedido ID: " + newValue.getId() + " selecionado.");
                cbEstado.setValue(newValue.getEstado());
            } else {
                lblPedidoSelecionado.setText("Nenhum pedido selecionado");
            }
        });
    }

    /**
     * Ação do botão "Buscar" (onAction="#buscarPedidos")
     */
    @FXML
    public void buscarPedidos() {
        String termoBusca = txtPesquisaCliente.getText().trim().toLowerCase();
        pedidosExibidos.clear();

        for (Pedido p : listaGeralPedidos) {
            // Se o campo estiver vazio, traz todos. Se não, filtra por nome ou telefone do cliente
            if (termoBusca.isEmpty()) {
                pedidosExibidos.add(p);
            } else if (p.getCliente() != null) {
                String nomeCompleto = p.getCliente().getNomeCompleto().toLowerCase();
                String telefone = p.getCliente().Gettelefone().toLowerCase();
                
                if (nomeCompleto.contains(termoBusca) || telefone.contains(termoBusca)) {
                    pedidosExibidos.add(p);
                }
            }
        }
    }

    /**
     * Ação do botão "Atualizar Estado" (onAction="#atualizarEstado")
     */
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

        // Modifica o estado do objeto usando o método que criamos
        pedidoSelecionado.mudarEstado(novoEstado);
        
        // Atualiza a tabela visualmente para refletir a mudança
        tblPedidos.refresh();
        mostrarAlerta("Sucesso", "O estado do pedido #" + pedidoSelecionado.getId() + " foi alterado para " + novoEstado);
    }

    /**
     * Método utilitário para permitir que o sistema central/Main envie a lista 
     * de pedidos ativos para dentro deste controller.
     */
    public void setListaGeralPedidos(List<Pedido> pedidos) {
        this.listaGeralPedidos = pedidos;
        // Força o recálculo do total de cada pedido antes de listar
        for (Pedido p : listaGeralPedidos) {
            p.calcularTotal();
        }
        buscarPedidos(); // Atualiza a tabela visual com os dados recebidos
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}

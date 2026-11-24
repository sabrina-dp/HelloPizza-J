package pizzaria.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.sql.SQLException;
import java.util.List;

import pizzaria.dao.ClienteDAO;
import pizzaria.dao.DAOFactory;
import pizzaria.dao.DAOType;
import pizzaria.dao.PedidoDAO;
import pizzaria.dao.SaborDAO;
import pizzaria.dao.PrecoDAO;
import pizzaria.model.*;
import pizzaria.model.forma.*;

public class PedidoController {

    @FXML private TextField txtTelefone;
    @FXML private Label lblCliente;
    @FXML private RadioButton rbCirculo, rbQuadrado, rbTriangulo;
    @FXML private TextField txtDimensao, txtArea;
    @FXML private ComboBox<Sabor> cbSabor1, cbSabor2;
    @FXML private Label lblAreaCalculada, lblPrecoPizza, lblTotalPedido;
    @FXML private TableView<ItemPedido> tblItensPedido;
    @FXML private TableColumn<ItemPedido, String> colForma, colSabores;
    @FXML private TableColumn<ItemPedido, Double> colArea, colPreco;

    private Cliente clienteAtual;
    private Pedido pedidoAtual;
    final ObservableList<ItemPedido> itensObservable = FXCollections.observableArrayList();

    final ClienteDAO clienteDAO = DAOFactory.getClienteDAO(DAOType.SQL);
    final PedidoDAO pedidoDAO = DAOFactory.getPedidoDAO(DAOType.SQL);
    final SaborDAO saborDAO = DAOFactory.getSaborDAO(DAOType.SQL);
    final PrecoDAO precoDAO = DAOFactory.getPrecoDAO(DAOType.SQL);

    final ToggleGroup grupoForma = new ToggleGroup();

    @FXML
    public void initialize() {
        // Garante que só uma forma pode ser selecionada por vez
        rbCirculo.setToggleGroup(grupoForma);
        rbQuadrado.setToggleGroup(grupoForma);
        rbTriangulo.setToggleGroup(grupoForma);
        rbCirculo.setSelected(true);

        configurarColunas();
        tblItensPedido.setItems(itensObservable);

        // Recalcula o preview quando o usuário sai do campo
        txtDimensao.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) atualizarPreview();
        });
        txtArea.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            if (!isFocused) atualizarPreviewPorArea();
        });

        carregarSabores();
    }

    private void configurarColunas() {
        colForma.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPizza().getForma().getNome()));

        colSabores.setCellValueFactory(c -> {
            Pizza p = c.getValue().getPizza();
            String texto = p.getSabor1().getNome();
            if (p.getSabor2() != null) {
                texto = texto + " + " + p.getSabor2().getNome();
            }
            return new SimpleStringProperty(texto);
        });

        colArea.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPizza().getForma().calcularArea()).asObject());

        colPreco.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrecoItem()).asObject());
    }

    @FXML
    public void buscarCliente(ActionEvent event) {
        String telefone = txtTelefone.getText().trim();

        if (telefone.isEmpty()) {
            mostrarErro("Digite um telefone para buscar.");
            return;
        }

        try {
            List<Cliente> resultado = clienteDAO.buscarFiltrado(telefone);

            if (resultado.isEmpty()) {
                lblCliente.setText("Cliente não encontrado.");
                clienteAtual = null;
                pedidoAtual = null;
                itensObservable.clear();
                return;
            }

            clienteAtual = resultado.get(0);
            lblCliente.setText(clienteAtual.getNomeCompleto());

            Pedido existente = pedidoDAO.buscarPorCliente(clienteAtual.getId());
            if (existente != null) {
                pedidoAtual = existente;
                itensObservable.setAll(existente.getItens());
            } else {
                pedidoAtual = new Pedido(0, clienteAtual);
                itensObservable.clear();
            }

            atualizarTotal();

        } catch (SQLException e) {
            mostrarErro("Erro ao buscar cliente: " + e.getMessage());
        }
    }

    @FXML
    public void adicionarPizza(ActionEvent event) {
        if (clienteAtual == null) {
            mostrarErro("Selecione um cliente antes de adicionar pizzas.");
            return;
        }

        try {
            Forma forma = construirForma();

            Sabor sabor1 = cbSabor1.getValue();
            Sabor sabor2 = cbSabor2.getValue();

            if (sabor1 == null) {
                mostrarErro("Selecione ao menos o Sabor 1.");
                return;
            }

            TabelaPreco tabela = precoDAO.buscar();
            Pizza pizza = new Pizza(0, forma, sabor1, sabor2, tabela);
            ItemPedido item = new ItemPedido(pizza);

            pedidoAtual.adicionarItem(item);
            itensObservable.add(item);
            atualizarTotal();
            limparFormularioPizza();

        } catch (IllegalArgumentException e) {
            mostrarErro(e.getMessage());
        } catch (SQLException e) {
            mostrarErro("Erro ao buscar a tabela de preços no banco de dados: " + e.getMessage());
        }
    }

    @FXML
    public void removerPizza(ActionEvent event) {
        ItemPedido selecionado = tblItensPedido.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            mostrarErro("Selecione uma pizza para remover.");
            return;
        }

        pedidoAtual.getItens().remove(selecionado);
        itensObservable.remove(selecionado);
        atualizarTotal();
    }

    @FXML
    public void salvarPedido(ActionEvent event) {
        if (pedidoAtual == null || pedidoAtual.getItens().isEmpty()) {
            mostrarErro("O pedido está vazio.");
            return;
        }

        try {
            boolean sucesso;
            if (pedidoAtual.getId() == 0) {
                // inserir() devolve o id gerado (ou -1 se falhou), então converte para boolean
                sucesso = pedidoDAO.inserir(pedidoAtual) > 0;
            } else {
                sucesso = pedidoDAO.atualizar(pedidoAtual);
            }

            if (sucesso) {
                mostrarInfo("Pedido salvo com sucesso!");
            } else {
                mostrarErro("Erro ao salvar pedido.");
            }

        } catch (SQLException e) {
            mostrarErro("Erro ao salvar pedido: " + e.getMessage());
        }
    }

    // --- Métodos auxiliares ---

    private Forma construirForma() {
        String dimStr = txtDimensao.getText().trim();

        if (dimStr.isEmpty()) {
            throw new IllegalArgumentException("Informe a dimensão da pizza.");
        }

        double dimensao;
        try {
            dimensao = Double.parseDouble(dimStr.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Dimensão inválida. Use apenas números.");
        }

        if (dimensao <= 0) {
            throw new IllegalArgumentException("A dimensão deve ser maior que zero.");
        }

        if (rbCirculo.isSelected()) return new Circulo(dimensao);
        if (rbQuadrado.isSelected()) return new Quadrado(dimensao);
        if (rbTriangulo.isSelected()) return new Triangulo(dimensao);

        throw new IllegalArgumentException("Selecione uma forma.");
    }

    private void atualizarPreview() {
        try {
            Forma forma = construirForma();
            double area = forma.calcularArea();
            lblAreaCalculada.setText(String.format("Área: %.2f cm²", area));

            Sabor sabor1 = cbSabor1.getValue();
            if (sabor1 != null) {
                TabelaPreco tabela = precoDAO.buscar();
                Pizza pizza = new Pizza(0, forma, sabor1, cbSabor2.getValue(), tabela);
                lblPrecoPizza.setText(String.format("Preço: R$ %.2f", pizza.calcularPreco()));
            }
        } catch (Exception e) {
            // Campo ainda incompleto, não exibe erro
        }
    }

    private void atualizarPreviewPorArea() {
        String areaStr = txtArea.getText().trim();
        if (areaStr.isEmpty()) return;

        double area;
        try {
            area = Double.parseDouble(areaStr.replace(",", "."));
        } catch (NumberFormatException e) {
            mostrarErro("Área inválida. Use apenas números.");
            return;
        }

        if (area < Forma.AREA_MIN || area > Forma.AREA_MAX) {
            mostrarErro(String.format("A área deve estar entre %.0f e %.0f cm².", Forma.AREA_MIN, Forma.AREA_MAX));
            return;
        }

        // Instância temporária só para usar o método calcularDimensaoPorArea
        Forma temp;
        if (rbCirculo.isSelected()) temp = new Circulo(7);
        else if (rbQuadrado.isSelected()) temp = new Quadrado(10);
        else temp = new Triangulo(20);

        double dimensao = temp.calcularDimensaoPorArea(area);
        txtDimensao.setText(String.format("%.2f", dimensao).replace(".", ","));
        atualizarPreview();
    }

    private void atualizarTotal() {
        if (pedidoAtual != null) {
            double total = pedidoAtual.calcularTotal();
            lblTotalPedido.setText(String.format("R$ %.2f", total));
        } else {
            lblTotalPedido.setText("R$ 0,00");
        }
    }

    private void carregarSabores() {
        try {
            List<Sabor> lista = saborDAO.listarTodos();
            ObservableList<Sabor> sabores = FXCollections.observableArrayList(lista);

            cbSabor1.setItems(sabores);

            // Sabor 2 começa com opção vazia pois é opcional
            ObservableList<Sabor> sabores2 = FXCollections.observableArrayList();
            sabores2.add(null);
            sabores2.addAll(lista);
            cbSabor2.setItems(sabores2);

            cbSabor2.setCellFactory(lv -> celulaSabor());
            cbSabor2.setButtonCell(celulaSabor());

        } catch (SQLException e) {
            mostrarErro("Erro ao carregar sabores: " + e.getMessage());
        }
    }

    // Mostra "— Nenhum —" no lugar da linha em branco do sabor2
    private ListCell<Sabor> celulaSabor() {
        return new ListCell<Sabor>() {
            @Override
            protected void updateItem(Sabor sabor, boolean empty) {
                super.updateItem(sabor, empty);
                if (empty || sabor == null) {
                    setText("— Nenhum —");
                } else {
                    setText(sabor.toString());
                }
            }
        };
    }

    private void limparFormularioPizza() {
        txtDimensao.clear();
        txtArea.clear();
        cbSabor1.setValue(null);
        cbSabor2.setValue(null);
        lblAreaCalculada.setText("Área: 0 cm²");
        lblPrecoPizza.setText("Preço: R$ 0,00");
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
    
    public void carregarPedido(Pedido pedido) {
        if (pedido == null) return;
        
        this.pedidoAtual = pedido;
        this.clienteAtual = pedido.getCliente();

        if (this.clienteAtual != null) {
            txtTelefone.setText(this.clienteAtual.getTelefone());
            lblCliente.setText(this.clienteAtual.getNomeCompleto());
        }
        
        itensObservable.setAll(pedido.getItens());
        atualizarTotal();
    }
}
package pizzaria.controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class HomeController {

    private void abrirTela(String arquivoFXML, String titulo) {
        try {
            Parent root = FXMLLoader.load(
                getClass().getResource("/pizzaria/view/" + arquivoFXML));
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Erro ao abrir tela " + titulo + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void abrirPedido(ActionEvent event) {
        abrirTela("PedidoView.fxml", "Cadastrar Pedido"); 
    }

    public void abrirStatus(ActionEvent event) {
        abrirTela("StatusView.fxml", "Gerenciar Pedidos"); 
    }

    public void abrirClientes(ActionEvent event) { 
        abrirTela("ClienteView.fxml", "Gerenciar Clientes"); 
    }

    public void abrirSabores(ActionEvent event) { 
        abrirTela("SaborView.fxml", "Gerenciar Sabores"); 
    }

    public void abrirPrecos(ActionEvent event) {
        abrirTela("PrecoView.fxml", "Gerenciar Preços"); 
    }

    public void sair(ActionEvent event) {
        System.exit(0);
    }
}

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

    public void abrirClientes(ActionEvent event) { 
        abrirTela("ClienteView.fxml", "Clientes"); 
    }

    public void abrirPedidos(ActionEvent event) {
        abrirTela("PedidoView.fxml", "Pedidos"); 
    }

    public void abrirSabores(ActionEvent event) { 
        abrirTela("SaborView.fxml", "Sabores"); 
    }

    public void abrirPrecos(ActionEvent event) {
        abrirTela("PrecoView.fxml", "Preços"); 
    }

    public void abrirStatus(ActionEvent event) {
        abrirTela("StatusView.fxml", "Status dos Pedidos"); 
    }

    public void sair(ActionEvent event) {
        System.exit(0);
    }
}

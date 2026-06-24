package pizzaria.controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class HomeController {

    private void abrirTela(String arquivoFXML) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource("/pizzaria/view/" + arquivoFXML));

            Stage stage = new Stage();

            stage.setScene(new Scene(root));

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void abrirClientes(ActionEvent event) {
        abrirTela("ClienteView.fxml");
    }

    public void abrirPedidos(ActionEvent event) {
        abrirTela("PedidoView.fxml");
    }

    public void abrirSabores(ActionEvent event) {
        abrirTela("SaborView.fxml");
    }

    public void abrirPrecos(ActionEvent event) {
        abrirTela("PrecoView.fxml");
    }

    public void abrirStatus(ActionEvent event) {
        abrirTela("StatusView.fxml");
    }

    public void sair(ActionEvent event) {
        System.exit(0);
    }
}

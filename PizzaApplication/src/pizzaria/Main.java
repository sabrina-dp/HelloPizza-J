package pizzaria;

import pizzaria.dao.InicializadorBanco;
import java.sql.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // TESTE: se uma janela abrir, o JavaFX está funcionando. Deletar depois.
        stage.setTitle("JavaFX OK!");
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        InicializadorBanco.inicializar();
        
        // TESTE: se printar "SQLite OK!" no console, o banco está funcionando. Deletar depois.
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        System.out.println("SQLite OK!");
        conn.close();

        launch(args);
    }
}

/*
 * Gerencia as taxas e valores por unidade de área configuradas na TabelaPreco.
 */

package pizzaria.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import TabelaPreco; // Verifique o pacote correto se houver

public class PrecoController {

    // 1. Mapeamento dos elementos visuais através do fx:id do FXML
    @FXML
    private TextField txtPrecoSimples;

    @FXML
    private TextField txtPrecoEspecial;

    @FXML
    private TextField txtPrecoPremium;

    @FXML
    private Label lblStatus;

    // O modelo/regra de negócio continua aqui
    private TabelaPreco tabelaPreco;

    /**
     * O JavaFX exige um construtor vazio para conseguir instanciar o controlador 
     * no momento em que carrega o FXML.
     */
    public PrecoController() {
        // Inicializa com valores padrão caso o sistema comece do zero
        this.tabelaPreco = new TabelaPreco(0.0, 0.0, 0.0);
    }

    /**
     * Método executado automaticamente pelo JavaFX assim que a tela abre.
     * Excelente lugar para carregar os preços iniciais nos campos.
     */
    @FXML
    public void initialize() {
        carregarPrecos();
    }

    // Retorna a tabela associada ao controller
    public TabelaPreco getTabelaAtual() {
        return this.tabelaPreco;
    }

    /**
     * Ação disparada pelo clique no botão "Salvar" (onAction="#salvarPrecos")
     */
    @FXML
    public void salvarPrecos() {
        try {
            // Converte os textos digitados pelas usuárias para double
            double simples = Double.parseDouble(txtPrecoSimples.getText().replace(",", "."));
            double especial = Double.parseDouble(txtPrecoEspecial.getText().replace(",", "."));
            double premium = Double.parseDouble(txtPrecoPremium.getText().replace(",", "."));

            // Salva no objeto de modelo
            this.tabelaPreco.setPrecoSimples(simples);
            this.tabelaPreco.setPrecoEspecial(especial);
            this.tabelaPreco.setPrecoPremium(premium);

            lblStatus.setText("Preços salvos com sucesso!");
            lblStatus.setStyle("-fx-text-fill: green;");
            
        } catch (NumberFormatException e) {
            lblStatus.setText("Erro: Insira apenas valores numéricos válidos.");
            lblStatus.setStyle("-fx-text-fill: red;");
        }
    }

    /**
     * Ação disparada pelo clique no botão "Restaurar" (onAction="#carregarPrecos")
     * Também usada na inicialização da tela.
     */
    @FXML
    public void carregarPrecos() {
        if (tabelaPreco != null) {
            txtPrecoSimples.setText(String.valueOf(tabelaPreco.getPrecoSimples()));
            txtPrecoEspecial.setText(String.valueOf(tabelaPreco.getPrecoEspecial()));
            txtPrecoPremium.setText(String.valueOf(tabelaPreco.getPrecoPremium()));
            lblStatus.setText("Preços carregados da tabela.");
            lblStatus.setStyle("-fx-text-fill: blue;");
        }
    }
    
    // Permite injetar uma tabela já existente vinda do PizzaSystem/Main
    public void setTabelaPreco(TabelaPreco tabelaPreco) {
        this.tabelaPreco = tabelaPreco;
        carregarPrecos();
    }
}

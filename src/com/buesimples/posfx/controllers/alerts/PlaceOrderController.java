package com.buesimples.posfx.controllers.alerts;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author sienekib
 */
public class PlaceOrderController implements Initializable {

    private static PlaceOrderController instance;

    private final String styleFalta = "-fx-text-fill: #FF6A6F; -fx-font-size: 24";

    private final String styleMatch = "-fx-text-fill: #15E698; -fx-font-size: 24";

    private JFXDialog dialog;

    @FXML
    private JFXButton btnClose;

    @FXML
    private JFXButton btnDesconto;

    @FXML
    private Label txtAlertTitle;

    @FXML
    private Label txtAlertBody;

    @FXML
    private JFXButton btnExit;

    @FXML
    private Label labelValorEntregue;

    @FXML
    private Label labelValorTroco;

    @FXML
    private Label labelValorTotal;

    @FXML
    private Label labelTextTotal;

    @FXML
    private JFXTextField inputDinheiro;

    @FXML
    private JFXTextField inputMulticaixa;

    @FXML
    private JFXTextField inputCheque;

    @FXML
    private JFXTextField inputAdiantado;

    double valorTotal;

    public PlaceOrderController() {
        instance = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public static PlaceOrderController getInstance() {
        return instance;
    }

    public void setAlertData(String title, String body, JFXDialog dialog) {
        this.dialog = dialog;
    }

    @FXML
    void aplicarDisconto(MouseEvent event) {

    }

    @FXML
    void efectuarPagamentoAndClose(MouseEvent event) {

    }

    public void valorTotalToPay(String valor) {
        labelValorTotal.setText(
                String.format("%.2f", Double.parseDouble(valor))
        );
        valorTotal = Double.parseDouble(labelValorTotal.getText());
    }

    @FXML
    void fecharModal(MouseEvent event) {
        this.dialog.close();
    }

    @FXML
    void pagamentoFormAdiantado(KeyEvent event) {

    }

    @FXML
    void pagamentoFormCheque(KeyEvent event) {
        actualizarValorPagamento(3, event);
    }

    @FXML
    void pagamentoFormDinheiro(KeyEvent event) {
        actualizarValorPagamento(1, event);
    }

    @FXML
    void pagamentoFormMulticaixa(KeyEvent event) {
        actualizarValorPagamento(2, event);
    }

    void actualizarValorPagamento(int idFormaPagamento, KeyEvent event) {

        if (!event.getCode().isDigitKey() && event.getCode() != KeyCode.BACK_SPACE) {
            event.consume();
            return;
        }

        JFXTextField inputField;

        // Determina qual campo de entrada está sendo atualizado
        switch (idFormaPagamento) {
            case 1:
                inputField = inputDinheiro;
                break;
            case 2:
                inputField = inputMulticaixa;
                break;
            case 3:
                inputField = inputCheque;
                break;
            default:
                inputField = inputAdiantado;
                break;
        }

        String valorAtual = inputField.getText().replaceAll("[^\\d]", ""); // Remove qualquer não dígito

        // Formata o valor para duas casas decimais com separador de milhares e vírgula para decimal
        if (!valorAtual.isEmpty()) {
            double valor = Double.parseDouble(valorAtual) / 100; // Divide por 100 para simular centavos
            inputField.setText(formatarValorMonetario(valor)); // Atualiza o campo com o valor formatado
            inputField.positionCaret(inputField.getText().length()); // Coloca o cursor no final
        }

        // Atualiza a soma total
        actualizarSomaTotal();
    }

    void actualizarSomaTotal() {
        double valorDinheiro = parseInputValue(inputDinheiro);
        double valorMulticaixa = parseInputValue(inputMulticaixa);
        double valorCheque = parseInputValue(inputCheque);
        double valorAdiantado = parseInputValue(inputAdiantado);

        // Soma os valores dos campos
        double somaTotal = valorDinheiro + valorMulticaixa + valorCheque + valorAdiantado;

        // Atualiza o label de valor total com a formatação correta
        labelValorEntregue.setText(formatarValorMonetario(somaTotal));
        actualizarLabelsParamento();
    }

    double parseInputValue(JFXTextField input) {
        String text = input.getText().replaceAll("[^\\d]", "");
        if (text.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(text) / 100;
    }

    String formatarValorMonetario(double valor) {
        // Utiliza Locale para formatação personalizada (separação por espaço e vírgula)
        NumberFormat nf = NumberFormat.getInstance(new Locale("pt", "AO"));
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("#,##0.00"); // Padrão: separa milhares por espaço e usa vírgula para decimal
        df.setGroupingUsed(true); // Ativa a separação de milhares
        String valorFormatado = df.format(valor);

        return valorFormatado.replace('.', ' ');
    }

    void actualizarLabelsParamento() {
        String entregue = labelValorEntregue.getText();
        double valorEntregue = Double.parseDouble(formatarParaNumerico(entregue));
        if (valorEntregue < this.valorTotal) {
            labelTextTotal.setStyle(styleFalta);
            labelValorTotal.setStyle(styleFalta);
            labelValorTroco.setText("0.0"); // Não há troco se o valor é insuficiente
        } else {
            labelTextTotal.setStyle(styleMatch);
            labelValorTotal.setStyle(styleMatch);

            if (valorEntregue > this.valorTotal) {
                String valorTroco = formatarValorMonetario(valorEntregue - this.valorTotal);
                labelValorTroco.setText(valorTroco);
            } else {
                labelValorTroco.setText("0.0"); // Não há troco se o valor é exato
            }
        }
    }

    public String formatarParaNumerico(String valorFormatado) {
        // Remove os espaços que separam os milhares
        valorFormatado = valorFormatado.replace(" ", "");

        // Substitui a vírgula decimal por um ponto
        valorFormatado = valorFormatado.replace(',', '.');

        return valorFormatado;
    }

}

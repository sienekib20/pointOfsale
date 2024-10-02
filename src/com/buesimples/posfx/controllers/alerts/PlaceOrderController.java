package com.buesimples.posfx.controllers.alerts;

import com.buesimples.posfx.controllers.IndexController;
import com.buesimples.posfx.controllers.cart.CarrinhoController;
import com.buesimples.posfx.controllers.cart.CheckoutItemController;
import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.models.ModoPagamento;
import com.buesimples.posfx.services.OrderControlService;
import com.buesimples.posfx.services.map.ItemData;
import com.buesimples.posfx.session.SessionUtils;
import com.buesimples.posfx.utils.config.Config;
import com.buesimples.posfx.utils.config.printer.Printers;
import com.buesimples.posfx.utils.formatter.InputFormatter;
import com.buesimples.posfx.utils.hashing.Hash;
import com.buesimples.posfx.utils.json.JsonUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

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
    private AnchorPane panelWating;

    @FXML
    private JFXButton btnPayment;

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
    private Label labelValorEmFalta;

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

    private int lastInsertedId;

    private Map<Integer, ModoPagamento> modoPagmentoList = new HashMap<>();

    private int nItems = 0;

    public PlaceOrderController() {
        instance = this;
    }

    public void setCheckoutItems(int nItems) {
        this.nItems = nItems;
        System.out.println("Quantidades Recebidas: " + nItems);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public static PlaceOrderController getInstance() {
        return instance;
    }

    public void setAlertData(String title, String body, JFXDialog dialog) {
        this.dialog = dialog;
        btnPayment.setDisable(true);
        panelWating.toBack();
    }

    @FXML
    void aplicarDisconto(MouseEvent event) {

    }

    @FXML
    void efectuarPagamentoAndClose(MouseEvent event) {
        panelWating.toFront();
        // Printers.getInstance().pageSetup(((Stage)
        // panelWating.getScene().getWindow()));
        // gerarHash()

        int sessionId = (int) Double.parseDouble(Config.getMap("localStorage",
                "sessionId"));

        int idSerie = Integer.parseInt(IndexController.getInstance().getDefinitionSingle("serie"));

        lastInsertedId = DatabaseHelpers.build().gerarDocumento(
                2,
                sessionId,
                CarrinhoController.getInstance().getClientId(),
                1,
                idSerie,
                SessionUtils.currentTimestamp(),
                InputFormatter.valorMonetarioToDouble(labelValorEntregue.getText().trim()),
                CarrinhoController.getInstance().getTotalIliquido(),
                0,
                InputFormatter.valorMonetarioToDouble(labelValorTotal.getText()),
                InputFormatter.valorMonetarioToDouble(labelValorTroco.getText()),
                CarrinhoController.getInstance().getTotalImposto());

        Map<Integer, ItemData> map = OrderControlService.getInstance().getItemMap();

        map.forEach((key, item) -> {
            gerarArtigoDoc(lastInsertedId, item);
        });

        // Processa os modos de pagamento
        criarModoPagamento(lastInsertedId);

        // Imprimir
        Printers.getInstance().gerarPDF(lastInsertedId, nItems);
    }

    void gerarHash() {
        // 356
        List<Map<String, Object>> map = DatabaseHelpers.build().querySelect("viewdocumento", "POST",
                "{\n"
                        + "    \"idDocumento\": " + 356 + "\n"
                        + "}");
        String codigoDocumento = "";
        String dataDoc = "";
        double total = 0;
        String prevHash = null;

        for (Map<String, Object> item : map) {
            dataDoc = item.get("dataDoc").toString();
            total = Double.parseDouble(item.get("total").toString());
            codigoDocumento = item.get("codigoDocumento").toString();
        }

        Hash.buildHash(dataDoc, codigoDocumento, total, prevHash).generateHashSaft();
    }

    public void valorTotalToPay(String valor) {
        labelValorTotal.setText(
                String.format("%.2f", Double.parseDouble(valor)));
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
        actualizarValorPagamento(3, event, "Cheque");
    }

    @FXML
    void pagamentoFormDinheiro(KeyEvent event) {
        actualizarValorPagamento(1, event, "Dinheiro");
    }

    @FXML
    void pagamentoFormMulticaixa(KeyEvent event) {
        actualizarValorPagamento(2, event, "Multicaixa");
    }

    void actualizarValorPagamento(int idFormaPagamento, KeyEvent event, String modoPagamento) {

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
        int idModoPagamento = pegarIdModoPagamento(modoPagamento);
        double valor = 0.0;

        // Formata o valor para duas casas decimais com separador de milhares e vírgula
        // para decimal
        if (!valorAtual.isEmpty()) {
            valor = Double.parseDouble(valorAtual) / 100; // Divide por 100 para simular centavos
            inputField.setText(formatarValorMonetario(valor)); // Atualiza o campo com o valor formatado
            inputField.positionCaret(inputField.getText().length()); // Coloca o cursor no final
        }
        modoPagmentoList.put(idModoPagamento, new ModoPagamento(lastInsertedId, idModoPagamento, valor));

        // Atualiza a soma total
        actualizarSomaTotal();
    }

    int pegarIdModoPagamento(String nomeModoPagamento) {
        int idModoPagamento = -1;
        List<Map<String, Object>> map = DatabaseHelpers.build().querySelect(
                "modopagamento",
                "POST",
                "{\n"
                        + "    \"nome\": \"" + nomeModoPagamento + "\"\n"
                        + "}");

        for (Map<String, Object> item : map) {
            idModoPagamento = (int) Double.parseDouble(item.get("idModoPagamento").toString());
        }

        return idModoPagamento;
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
            labelValorEmFalta.setText(formatarValorMonetario(this.valorTotal - valorEntregue));
            labelTextTotal.setStyle(styleFalta);
            labelValorTotal.setStyle(styleFalta);
            labelValorTroco.setText("0.0"); // Não há troco se o valor é insuficiente
        } else {
            labelValorEmFalta.setText("0.0");
            labelTextTotal.setStyle(styleMatch);
            labelValorTotal.setStyle(styleMatch);
            btnPayment.setDisable(false);

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

    void gerarArtigoDoc(int idDocumento, ItemData artigo) {
        Map<String, Object> data = new HashMap<>();

        double valorTotalSet = artigo.getValorTotal();

        data.put("idDocumento", idDocumento);
        data.put("idArtigo", artigo.getIdArtigo());
        data.put("idImposto", artigo.getIdImposto());
        data.put("descricao", artigo.getDescricao());
        data.put("dataDocumento", SessionUtils.currentTimestamp());
        data.put("qtd", artigo.getQuantidade());
        data.put("preco", artigo.getPrecoInicial());
        data.put("tipoDesconto", "valor");
        data.put("desconto", calcularDesconto(0, valorTotalSet, 0.0));
        data.put("imposto", artigo.getValorImposto());
        data.put("total", artigo.getValorTotal());

        String jsonString = JsonUtils.mapToJsonString(data);
        DatabaseHelpers.build().insert("artigodocumento", "POST", jsonString);
    }

    double calcularDesconto(int tipoDesconto, double valorTotal, double valorDesconto) {

        // Tipo 1 = valor | Tipo 2 = Porcentagem
        //
        if (tipoDesconto == 1) {
            return valorTotal - valorDesconto;
        }

        if (tipoDesconto == 2) {

            double result = (valorTotal * valorDesconto) / 100;

            return valorTotal - result;
        }

        return 0.0;
    }

    void criarModoPagamento(int idDocumento) {
        Map<String, Object> data = new HashMap<>();
        modoPagmentoList.forEach((idModoPagamento, modoPagamento) -> {
            if (modoPagamento.getValor() > 0.0) {
                data.put("idDocumento", idDocumento);
                data.put("idModoPagamento", idModoPagamento);
                data.put("valor", modoPagamento.getValor());
                String jsonString = JsonUtils.mapToJsonString(data);
                DatabaseHelpers.build().insert("modopagamentodocumento", "POST", jsonString);
            }
        });
    }

}

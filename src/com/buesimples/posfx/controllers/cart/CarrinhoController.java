package com.buesimples.posfx.controllers.cart;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.buesimples.posfx.alerts.AlertBuilder;
import com.buesimples.posfx.alerts.AlertType;
import com.buesimples.posfx.controllers.alerts.AlertBuilderController;
import com.buesimples.posfx.controllers.alerts.PlaceOrderController;
import com.buesimples.posfx.models.Checkout;
import com.buesimples.posfx.utils.constants.Constants;
import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CarrinhoController implements Initializable {

    private static CarrinhoController instance;

    @FXML
    private Label labelCliente;

    @FXML
    private Label labelNPedido;

    @FXML
    private VBox vboxGrid;

    @FXML
    private Label labelValorSubtotal;

    @FXML
    private Label labelValorTaxa;

    @FXML
    private Label labelValorDesconto;

    @FXML
    private Label labelValorTotal;

    @FXML
    private JFXButton btnDesconto;

    @FXML
    private JFXButton btnLimpar;

    @FXML
    private JFXButton btnCancelar;

    @FXML
    private JFXButton btnPagamento;

    private Map<Integer, HBox> itemMap = new HashMap<>();

    private Map<Integer, CheckoutItemController> controllerMap = new HashMap<>();

    private Map<Integer, Double> discountMap = new HashMap<>();

    private Map<Integer, Double> valorInicialDesconto = new HashMap<>();

    private Map<Integer, Double> valorInicialTaxa = new HashMap<>();

    private boolean actionCancelar;

    public CarrinhoController() {
        instance = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void updateTax(double price, double taxa, int indice, boolean somarResultado) {
        double valorActualTaxa = 0;

        if (!labelValorTaxa.getText().isEmpty()) {
            valorActualTaxa = Double.parseDouble(labelValorTaxa.getText());
        }

        double resultado = (price * taxa) / 100;
        resultado = resultado * indice;

        resultado = somarResultado == true ? valorActualTaxa + resultado : (-1 * resultado);

        labelValorTaxa.setText(
                String.format("%.1f", resultado));
    }

    public double getValorInicialTaxa(int id) {
        return valorInicialTaxa.get(id);
    }

    public static CarrinhoController getInstance() {
        return instance;
    }

    @FXML
    void applyDiscount(MouseEvent event) {

    }

    void confirmaAlert() {
        AlertBuilder.build().confirm(AlertType.WARNING, btnCancelar, "¿Deseja continuar com esta operacao?");
    }

    @FXML
    void cancelarPedido(MouseEvent event) {
        confirmaAlert();
        actionCancelar = true;
    }

    @FXML
    void limparCarrinho(MouseEvent event) {
        confirmaAlert();
        actionCancelar = false;
    }

    public void limparDadosCarrinho() {
        if (vboxGrid.getChildren().size() > 0) {
            vboxGrid.getChildren().clear();
            itemMap.clear();
            controllerMap.clear();
            discountMap.clear();
            valorInicialDesconto.clear();
            valorInicialTaxa.clear();
            if (actionCancelar) {
                labelCliente.setText("Consumidor Final");
            }
            resetLabelValue();
        }
    }

    void resetLabelValue() {
        labelValorDesconto.setText("0.0");
        labelValorSubtotal.setText("0.0");
        labelValorTaxa.setText("0.0");
        labelValorTotal.setText("0.0");
    }

    @FXML
    void realizarPagamento(MouseEvent event) {
        if (vboxGrid.getChildren().size() > 0) {
            AlertBuilder.build().placeOrder(AlertType.INFORMATION, labelCliente, "");
            PlaceOrderController.getInstance().valorTotalToPay(labelValorTotal.getText());
            return;
        }
        AlertBuilder.build().create(AlertType.WARNING, labelCliente, "Carrinho vazio, por favor adicione item!");
    }

    public void adicionarItemNoCarrinho(ArrayList<Checkout> item, int id) {
        try {
            FXMLLoader fxml = new FXMLLoader();
            fxml.setLocation(getClass().getResource(Constants.view("cart.CheckoutItem")));
            HBox box = fxml.load();
            CheckoutItemController itemController = fxml.getController();

            item.stream().forEach(data -> {
                if (verificarPrecoArtigo(
                        Double.parseDouble(data.getCheckoutPrecoArtigo())) == 0) {
                    return;
                }

                double precoItem = 0;
                if (itemMap.containsKey(id)) {

                    HBox hbox = itemMap.get(id);

                    if (hbox != null) {
                        Label articleQtdLabel = getHboxLabel(hbox, "txtQtd"),
                                articlePriceLabel = getHboxLabel(hbox, "txtPreco");

                        if (articleQtdLabel != null && articlePriceLabel != null) {
                            // Obter e atualizar a quantidade
                            String articleQtdValue = articleQtdLabel.getText();
                            int currentQuantity = Integer.parseInt(articleQtdValue);
                            currentQuantity += 1; // Incrementar a quantidade
                            articleQtdLabel.setText(String.valueOf(currentQuantity));

                            // Obter o preço unitário do controlador armazenado
                            CheckoutItemController existingController = controllerMap.get(id);
                            double unitPrice = existingController.getUnitPrice(); // Obter o preço unitário

                            double sum = discountMap.size() > 0 ? discountMap.get(id) : 0;

                            // Calcular o novo preço total
                            // double newTotalPrice = unitPrice + existingController.getArticlePrice();
                            // //Double.parseDouble(item.getCheckoutPrecoArtigo());
                            double newTotalPrice = 0;
                            if (sum == 0) {
                                newTotalPrice = Double.parseDouble(articlePriceLabel.getText())
                                        + (Double.parseDouble(data.getCheckoutPrecoArtigo()) - sum);
                                precoItem = Double.parseDouble(data.getCheckoutPrecoArtigo());
                            } else {
                                actualizarValorDescontoById(id, 1);
                                newTotalPrice = currentQuantity * unitPrice;
                                precoItem = unitPrice;
                            }
                            // double newTotalPrice = currentQuantity * unitPrice;
                            articlePriceLabel.setText(String.format("%.1f", newTotalPrice));
                        }

                    }
                } else {
                    precoItem = Double.parseDouble(data.getCheckoutPrecoArtigo());
                    itemController.setData(data, id);
                    actionVboxGrid(1, id, box, itemController);
                    // vboxGrid.getChildren().add(box);
                    // itemMap.put(id, box);
                    // controllerMap.put(id, itemController);
                }

                updateTax(precoItem, data.getTaxa(), 1, true);

                if (!valorInicialTaxa.containsKey(id)) {
                    valorInicialTaxa.put(id, data.getTaxa());
                }
                definirValorSubTotal(1, id);
            });

        } catch (IOException e) {
            System.err.println("CarrinhoControoler->adicoinarItemNoCarrinho: " + e.getMessage());
        }

    }

    void actionVboxGrid(int actionGrid, int id, HBox box, CheckoutItemController itemController) {
        if (actionGrid == 1) {
            vboxGrid.getChildren().add(box);
            itemMap.put(id, box);
            controllerMap.put(id, itemController);
        } else {
            vboxGrid.getChildren().remove(box);
            itemMap.remove(id);
            controllerMap.remove(id);
        }
    }

    int verificarPrecoArtigo(double preco) {
        if (preco == 0) {
            AlertBuilder.build().create(AlertType.ERROR, btnPagamento, "Preco Inválido");
            return 0;
        }
        return 1;
    }

    public void actualizarValorDescontoById(int id, int indice) {
        if (labelValorDesconto.getText().trim() != null) {
            labelValorDesconto.setText(
                    String.valueOf(
                            Double.parseDouble(labelValorDesconto.getText().trim())
                            + (indice * this.valorInicialDesconto.get(id))));
        } else {
            System.out.println("Invalid: CheckountController - 480");
        }
    }

    private void definirValorSubTotal(int somarSubTotal, int id) {

        if (labelValorTaxa.getText().contains("-")) {
            labelValorTaxa.setText(
                    labelValorTaxa
                    .getText()
                    .replaceAll("-", ""));
        }

        if (controllerMap.isEmpty()) {
            labelValorSubtotal.setText("0.0");
            labelValorDesconto.setText("0.0");
            labelValorTaxa.setText("0.0");
            labelValorTotal.setText("0.0");
            return;
        } else {

            CheckoutItemController currentItemController = controllerMap.get(id);

            if (currentItemController.getArticlePriceLabel() instanceof Label) {
                double valorImposto = Double.parseDouble(labelValorTaxa.getText());
                double valorDesconto = Double.parseDouble(labelValorDesconto.getText());
                double valorExistenteSubtotal = Double.parseDouble(labelValorSubtotal.getText());
                double precoSemImposto = currentItemController.getPrecoInitialArtigo();

                double valorCalculado = (somarSubTotal == 1)
                        ? precoSemImposto + valorExistenteSubtotal
                        : (precoSemImposto - valorExistenteSubtotal) * -1;

                labelValorSubtotal.setText(String.format("%.1f", valorCalculado));
                if (labelValorSubtotal.getText().contains("-")) {
                    labelValorSubtotal.setText(
                            labelValorSubtotal.getText().replace("-", ""));
                }
                actualizarValorTotal(Double.parseDouble(labelValorSubtotal.getText()), valorDesconto, valorImposto);
            }
        }

    }

    private void actualizarValorTotal(double valorSubtotal, double valorDesconto, double valorImposto) {
        double valorTotalPagar = (valorSubtotal + valorImposto) - valorDesconto;
        labelValorTotal.setText(String.format("%.2f", valorTotalPagar));
    }

    public void updateTable(int id) {
        if (itemMap.containsKey(id)) {
            HBox hbox = itemMap.get(id);

            if (hbox != null) {
                // Atualiza o desconto do item, se aplicável
                if (discountMap.containsKey(id)) {
                    actualizarValorDescontoById(id, -1);
                }

                Label articleQtdLabel = getHboxLabel(hbox, "txtQtd");
                Label articlePriceLabel = getHboxLabel(hbox, "txtPreco");

                if (articleQtdLabel != null && articlePriceLabel != null) {
                    int quantidadeAtual = Integer.parseInt(articleQtdLabel.getText());

                    // Reduzir o subtotal e recalcular para o item removido
                    definirValorSubTotal(-1, id);

                    if (quantidadeAtual > 1) {
                        // Atualiza a quantidade e o preço do item
                        quantidadeAtual -= 1;
                        articleQtdLabel.setText(String.valueOf(quantidadeAtual));

                        CheckoutItemController controllerAtual = controllerMap.get(id);
                        double precoUnitario = controllerAtual.getUnitPrice();
                        double precoAtual = Double.parseDouble(articlePriceLabel.getText().trim());

                        // Atualiza o preço total do item após a remoção
                        double novoPrecoTotal = precoAtual - precoUnitario;
                        articlePriceLabel.setText(String.format("%.1f", novoPrecoTotal));

                        // Atualiza a taxa com base no preço unitário e quantidade
                        updateTax(precoUnitario, valorInicialTaxa.get(id), -1, true);

                    } else {
                        // Remover o item do carrinho
                        actionVboxGrid(0, id, hbox, null);

                        // Remove o item dos mapas de controle
                        itemMap.remove(id);
                        controllerMap.remove(id);
                        discountMap.remove(id);
                        valorInicialTaxa.remove(id);

                        // Recalcula a taxa total com base nos itens restantes
                        recalcularTaxaTotal();
                    }

                    // Recalcula o valor total com base nos itens restantes
                    recalcularTotal();
                }
            }
        }
    }

    private void recalcularTaxaTotal() {
        double novaTaxaTotal = 0;

        // Recalcula a taxa para todos os itens restantes no carrinho
        for (Map.Entry<Integer, CheckoutItemController> entry : controllerMap.entrySet()) {
            int itemId = entry.getKey();
            CheckoutItemController controller = entry.getValue();
            double taxa = valorInicialTaxa.getOrDefault(itemId, 0.0);
            double precoItem = controller.getUnitPrice();
            novaTaxaTotal += (precoItem * taxa) / 100;
        }

        labelValorTaxa.setText(String.format("%.1f", novaTaxaTotal));
    }

    private void recalcularTotal() {
        double subtotal = 0;
        double valorDesconto = Double.parseDouble(labelValorDesconto.getText());
        double valorImposto = Double.parseDouble(labelValorTaxa.getText());

        // Recalcula o subtotal com base nos itens restantes
        for (CheckoutItemController controller : controllerMap.values()) {
            subtotal += controller.getArticlePrice() * Integer.parseInt(controller.getArticleQtdLabel().getText());
            // Multiplica pelo número de itens restantes
        }

        labelValorSubtotal.setText(String.format("%.1f", subtotal));

        // Recalcula o valor total a pagar
        double valorTotalPagar = (subtotal + valorImposto) - valorDesconto;
        labelValorTotal.setText(String.format("%.2f", valorTotalPagar));
    }

    Label getHboxLabel(HBox hbox, String field) {
        Label labelName = null;
        for (Node node : hbox.getChildren()) {
            if (node instanceof Label) {
                Label label = (Label) node;
                if (field.equals(label.getId())) {
                    labelName = label;
                }
            }
        }
        return labelName;
    }

}

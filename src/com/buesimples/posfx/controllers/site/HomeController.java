package com.buesimples.posfx.controllers.site;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.buesimples.posfx.alerts.AlertBuilder;
import com.buesimples.posfx.alerts.AlertType;
import com.buesimples.posfx.animations.Animations;
import com.buesimples.posfx.controllers.IndexController;
import com.buesimples.posfx.controllers.cart.CarrinhoController;
import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.models.Artigos;
import com.buesimples.posfx.models.Checkout;
import com.buesimples.posfx.services.CartService;
import com.buesimples.posfx.utils.constants.Constants;
import com.buesimples.posfx.utils.loader.NodeLoader;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HomeController implements Initializable {

    private static HomeController instance;

    @FXML
    private StackPane panelRoot;

    @FXML
    private AnchorPane panelMain;

    @FXML
    private AnchorPane panelCart;

    @FXML
    private AnchorPane panelArticles;

    @FXML
    private VBox vboxGrid;

    @FXML
    private JFXTextField inputPesquisa;

    @FXML
    private JFXButton btnVoltar;

    private ObservableList<Artigos> listArtigos;

    private ObservableList<Artigos> filterArtigos;

    @FXML
    private TableView<Artigos> articleTable;

    @FXML
    private TableColumn<Artigos, Integer> cellId;

    @FXML
    private TableColumn<Artigos, String> cellName;

    @FXML
    private TableColumn<Artigos, Integer> cellQtd;

    @FXML
    private TableColumn<Artigos, Double> cellPrice;

    @FXML
    private JFXButton btnAdicionar;

    @FXML
    private JFXButton btnRemover;

    public static int currentId;

    private int qtdArtigoSelecionado;

    int idArtigoSelecionado;

    private ArrayList<Checkout> checkoutList;

    private static IndexController instanceIndex;

    public HomeController() {
        instance = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadContent();
        createClass();
    }

    public static HomeController getInstance() {
        return instance;
    }

    void loadContent() {
        if (CartService.getInstance().getItems().isEmpty()) {
            NodeLoader.getInstance().loadNode1(panelCart, "cart.Checkout");
        }
//        NodeLoader.getInstance().loadNode(panelCart, "cart.Checkout");
        getFamilias();
        panelArticles.toBack();
        filterArtigos = FXCollections.observableArrayList();
    }

    private void createClass() {
        instanceIndex = IndexController.getInstance();
    }

    void getFamilias() {
        try {
            List<Map<String, Object>> map = DatabaseHelpers.build().select(
                    "familia", "GET");
            if (map != null && !map.isEmpty()) {

                HBox boxItems = createBoxItem();

                for (Map<String, Object> item : map) {
                    FXMLLoader fxml = new FXMLLoader();
                    fxml.setLocation(getClass().getResource(Constants.view("site.familia.FamiliaItem")));

                    JFXButton itemFamilia = fxml.load();

                    FamiliaItemController itemController = fxml.getController();
                    Number idFamilia = (Number) item.get("idFamilia");
                    String nome = (String) item.get("nome");
                    itemController.setItems(idFamilia.intValue(), nome, Constants.MEDIA_PACKAGE + "sale.png");

                    if (boxItems.getChildren().size() == 5) {
                        vboxGrid.getChildren().add(boxItems);
                        boxItems = createBoxItem();
                    }
                    // boxItems.setPadding(new Insets(0, 0, 0, 18.4));
                    boxItems.getChildren().add(itemFamilia);

                }

                if (!boxItems.getChildren().isEmpty()) {
                    vboxGrid.getChildren().add(boxItems);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    HBox createBoxItem() {
        HBox box = new HBox();
        box.setPrefWidth(580);
        box.setPrefHeight(180);
        box.setSpacing(10);
        box.setMinWidth(580);
        return box;
    }

    void showArticles(int requestId) {
        loadData(requestId);

        cellId.setCellValueFactory(new PropertyValueFactory<>("id"));
        cellName.setCellValueFactory(new PropertyValueFactory<>("article"));
        cellPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        cellQtd.setCellValueFactory(new PropertyValueFactory<>("qtdStock"));

        panelArticles.toFront();
        inputPesquisa.setText("");
        Animations.fadeIn(articleTable, 5.0);
        actionTable();
    }

    private void loadData(int requestId) {
        ArrayList<Artigos> list = new ArrayList<>();

        int linhaPrecoId = Integer.parseInt(
                DatabaseHelpers.build().getTableValue(
                        instanceIndex.getDefinition(),
                        "idDefinicao",
                        2,
                        "valor"));

        List<Map<String, Object>> artigos = DatabaseHelpers.build().querySelectData("POST", "{\n"
                + "    \"tabela\": \"artigo\",\n"
                + "    \"join\": [\n"
                + "        {\n"
                + "            \"tipo\": \"inner\",\n"
                + "            \"tabela\": \"precoartigo\",\n"
                + "            \"juncao\": \"precoartigo.idArtigo = artigo.idArtigo\"\n"
                + "        },\n"
                + "        {\n"
                + "            \"tipo\": \"inner\",\n"
                + "            \"tabela\": \"imposto\",\n"
                + "            \"juncao\": \"artigo.idImposto = imposto.idImposto\"\n"
                + "        },\n"
                + "        {\n"
                + "            \"tipo\": \"inner\",\n"
                + "            \"tabela\": \"artigostock\",\n"
                + "            \"juncao\": \"artigo.idArtigo = artigostock.idArtigo\"\n"
                + "        }\n"
                + "    ],\n"
                + "    \"coluna\": [\n"
                + "        {\n"
                + "            \"idArtigo\": \"artigo.idArtigo\",\n"
                + "            \"idFamilia\": \"artigo.idFamilia\",\n"
                + "            \"nome\": \"artigo.nome\",\n"
                + "            \"indice\": \"precoartigo.indice\",\n"
                + "            \"semImposto\": \"precoartigo.semImposto\",\n"
                + "            \"taxa\": \"imposto.taxa\",\n"
                + "            \"idImposto\": \"artigo.idImposto\",\n"
                + "            \"qtdDisponivel\": \"artigostock.qtdDisponivel\"\n"
                + "        }\n"
                + "    ]\n"
                + "}");

        // Usar um Map para garantir que só exista um artigo por ID
        Map<Integer, Artigos> artigosMap = new HashMap<>();

        for (Map<String, Object> artigo : artigos) {
            int idFamilia = (int) Double.parseDouble(artigo.get("idFamilia").toString());

            if (idFamilia == requestId) {
                int id = (int) Double.parseDouble(artigo.get("idArtigo").toString());
                int valorLinhaPreco = (int) Double.parseDouble(artigo.get("indice").toString());

                int qtdStock = (artigo.get("qtdDisponivel") == null) ? 0
                        : (int) Double.parseDouble(artigo.get("qtdDisponivel").toString());
                
                String articleName = (String) artigo.get("nome");
                String taxa = artigo.get("taxa") != null ? artigo.get("taxa").toString() : "0";
                int idImposto = (int) Double.parseDouble(artigo.get("idImposto").toString());
                
                String price;

                if (valorLinhaPreco == linhaPrecoId) {
                    price = String.valueOf(((Number) artigo.get("semImposto")).doubleValue()) + "0";
                } else {
                    price = "0.0";
                }

                if (!artigosMap.containsKey(id) || valorLinhaPreco == linhaPrecoId) {
                    artigosMap.put(id, new Artigos(id, articleName, price, taxa, qtdStock, idImposto));
                }
            }
        }

        list.addAll(artigosMap.values());

        listArtigos = FXCollections.observableArrayList(list);
        articleTable.setItems(listArtigos);
    }

    @FXML
    void adicionarProduto(MouseEvent event) {
        int allowAddItem = -1;
        int stockNegativoValue = Integer.parseInt(
                DatabaseHelpers.build().getTableValue(instanceIndex.getDefinition(),
                        "idDefinicao",
                        10,
                        "valor"));

        if (qtdArtigoSelecionado < 1) {
            if (stockNegativoValue == 0) {
                AlertBuilder.build().create(AlertType.ERROR, articleTable,
                        "Impossivel adicionar. O Stock Negativo desabilitabo");
                return;
            }
            allowAddItem = 1;
        } else {
            allowAddItem = 1;
        }

        if (allowAddItem == 1) {
            CarrinhoController.getInstance().adicionarItemNoCarrinho(checkoutList, idArtigoSelecionado);
        }

    }

    @FXML
    void filtrarTabela(KeyEvent event) {
        String filterName = inputPesquisa.getText().trim();
        if (filterName.isEmpty()) {
            articleTable.setItems(listArtigos);
        } else {
            filterArtigos.clear();
            for (Artigos a : listArtigos) {
                if (a.getArticle().toLowerCase().contains(filterName.toLowerCase())) {
                    filterArtigos.add(a);
                }
            }
            articleTable.setItems(filterArtigos);
        }
    }

    @FXML
    void removeProduto(MouseEvent event) {
        CarrinhoController.getInstance().updateTable(idArtigoSelecionado);
    }

    @FXML
    void showInicio(MouseEvent event) {
        panelArticles.toBack();
        Animations.fadeOut(articleTable, 5.0);
    }

    private void actionTable() {
        articleTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Artigos selectedArticle = articleTable.getSelectionModel().getSelectedItem();
                if (selectedArticle != null) {
                    // Obtendo o valor da coluna "id" (colRef)
                    qtdArtigoSelecionado = selectedArticle.getQtdStock();
                    idArtigoSelecionado = selectedArticle.getId();
                    String articleName = selectedArticle.getArticle();
                    String price = selectedArticle.getPrice();
                    int idImposto = selectedArticle.getIdImposto();
                    String taxa = selectedArticle.getTaxa();
                    addToCheckoutList(idArtigoSelecionado, articleName, price, idImposto, Double.parseDouble(taxa));
                }
            }
        });
    }

    private void addToCheckoutList(int idArtigoSelecionado, String nomeArtigo, String precoArtigo, int idImposto, double taxa) {
        // CheckoutController checkoutController = CheckoutController.getInstance();
        checkoutList = new ArrayList<>();
        checkoutList.add(new Checkout(idArtigoSelecionado, nomeArtigo, 1, precoArtigo, idImposto, taxa, qtdArtigoSelecionado));
    }

}

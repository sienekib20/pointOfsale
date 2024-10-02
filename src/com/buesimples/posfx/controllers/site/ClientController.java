package com.buesimples.posfx.controllers.site;

import com.buesimples.posfx.controllers.IndexController;
import com.buesimples.posfx.controllers.cart.CarrinhoController;
import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.database.ValueExtractor;
import com.buesimples.posfx.models.Entidade;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author sienekib
 */
public class ClientController implements Initializable {

    private static ClientController instance;

    private List<Map<String, Object>> map;

    @FXML
    private JFXTextField inputPesquisa;

    @FXML
    private JFXButton btnNew;

    private ObservableList<Entidade> entidadeList;

    private ObservableList<Entidade> filteredList;

    @FXML
    private TableView<Entidade> entidadeTable;

    @FXML
    private TableColumn<Entidade, Integer> cellRef;

    @FXML
    private TableColumn<Entidade, String> cellNome;

    @FXML
    private TableColumn<Entidade, String> cellNIF;

    @FXML
    private TableColumn<Entidade, String> cellMorada;

    public ClientController() {
        instance = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarDadosClient();
        filteredList = FXCollections.observableArrayList();
    }

    public static ClientController getInstance() {
        return instance;
    }
    
    void actionTable() {
        entidadeTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Entidade e = entidadeTable.getSelectionModel().getSelectedItem();
                CarrinhoController.getInstance().selectClient(e.getRefEntidade(), e.getNome());
                IndexController.getInstance().openWindow(event, 1, "site.Home");
            }
        });
    }

    void carregarDadosClient() {
        map = DatabaseHelpers.build().querySelect(
                "entidade", "POST",
                "{\"idTipoEntidade\": 2}"
        );
        loadTable();
        cellRef.setCellValueFactory(new PropertyValueFactory<>("refEntidade"));
        cellNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        cellNIF.setCellValueFactory(new PropertyValueFactory<>("NIF"));
        cellMorada.setCellValueFactory(new PropertyValueFactory<>("morada"));
    }

    void loadTable() {
        ArrayList<Entidade> list = new ArrayList<>();

        for (Map<String, Object> item : map) {
            int refEntidade = (int) Double.parseDouble(item.get("idEntidade").toString());
            String nome = item.get("nome").toString();
            String NIF = item.get("nif").toString();
            String morada = item.get("morada").toString();

            list.add(new Entidade(refEntidade, nome, NIF, morada));
        }
        entidadeList = FXCollections.observableArrayList(list);
        entidadeTable.setItems(entidadeList);
    }

    @FXML
    void filtrarTabela(KeyEvent event) {
        String filterName = inputPesquisa.getText().trim();
        if (filterName.isEmpty()) {
            entidadeTable.setItems(entidadeList);
        } else {
            filteredList.clear();
            for (Entidade e : entidadeList) {
                if (e.getNome().toLowerCase().contains(filterName.toLowerCase())
                        || e.getNIF().toLowerCase().contains(filterName.toLowerCase())) {
                    filteredList.add(e);
                }
            }
            entidadeTable.setItems(filteredList);
        }
    }

    @FXML
    void novoEntidade(MouseEvent event) {

    }

}

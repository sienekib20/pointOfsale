package com.buesimples.posfx.controllers.setting;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author sienekib
 */
public class SetDatabaseController implements Initializable {

    @FXML
    private AnchorPane panelDatabase;

    @FXML
    private JFXTextField txtHost;

    @FXML
    private JFXTextField txtPort;

    @FXML
    private JFXComboBox<?> txtCombo;

    @FXML
    private JFXToggleButton toggleSincronizacao;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnReset;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
    }
    
    void initData() {
        txtHost.setText("http://localhost:8084/pdv/api");
        txtPort.setText("8084");
    }

    @FXML
    void resetDatabaseSettings(MouseEvent event) {

    }

    @FXML
    void saveDatabaseSettings(MouseEvent event) {

    }

    @FXML
    void setPort(KeyEvent event) {
        System.out.println("CHAR: " + event.getCharacter());
    }

    @FXML
    void toggleSync(MouseEvent event) {

    }

}

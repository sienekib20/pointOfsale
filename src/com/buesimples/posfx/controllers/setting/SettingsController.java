package com.buesimples.posfx.controllers.setting;

import com.buesimples.posfx.utils.loader.NodeLoader;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author sienekib
 */
public class SettingsController implements Initializable {

    @FXML
    private JFXButton btnGeral;

    @FXML
    private JFXButton btnViewport;

    @FXML
    private JFXButton btnDatabase;

    @FXML
    private JFXButton btnSecurity;

    @FXML
    private AnchorPane panelExtended;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showPanel();
    }

    void activeBtn(MouseEvent event) {
        activeBtn(event, btnGeral);
        activeBtn(event, btnViewport);
        activeBtn(event, btnDatabase);
        activeBtn(event, btnSecurity);
    }

    void showPanel() {
        btnGeral.getStyleClass().add("active");
        NodeLoader.getInstance().load(panelExtended, "site.setting.General");
    }

    @FXML
    void showDatabase(MouseEvent event) {
        showWindow(event, "site.setting.Database");
    }

    @FXML
    void showGeral(MouseEvent event) {
        showWindow(event, "site.setting.General");
    }

    @FXML
    void showSecurity(MouseEvent event) {
        showWindow(event, "site.setting.Security");
    }

    @FXML
    void showViewport(MouseEvent event) {
        showWindow(event, "site.setting.Viewport");
    }

    void activeBtn(MouseEvent event, JFXButton btn) {
        if (event.getSource().equals(btn)) {
            btn.getStyleClass().add("active");
        } else {
            btn.getStyleClass().remove("active");
        }
    }
    
    void showWindow(MouseEvent event, String window) {
        activeBtn(event);
        NodeLoader.getInstance().load(panelExtended, window);
    }
    
}

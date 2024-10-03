package com.buesimples.posfx.controllers.setting;

import com.buesimples.posfx.alerts.AlertBuilder;
import com.buesimples.posfx.alerts.AlertType;
import com.buesimples.posfx.utils.config.printer.Printers;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

import javax.print.*;

/**
 *
 * @author sienekib
 */
public class SetGeralController implements Initializable {

    @FXML
    private JFXComboBox<String> txtComboImpressoras;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnReset;
    
     @FXML
    private JFXTextField txtNomeEmpresa;

    @FXML
    private JFXComboBox<String> txtCategoriaEmpresa;

    @FXML
    private JFXTextField txtEndereco;

    @FXML
    private JFXTextField txtWebsite;

    @FXML
    private JFXTextField txtTelefone;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtRodape;

    @FXML
    private JFXTextField txtNIF;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        carregarImpressoras();
    }

    void carregarImpressoras() {
        PrintService[] printServices = Printers.loadPrint();
        for (PrintService service : printServices) {
            txtComboImpressoras.getItems().add(service.getName());
        }
        txtComboImpressoras.getSelectionModel().selectFirst();
        // txtComboImpressoras.setStyle("");
    }

    @FXML
    void resetDatabaseSettings(MouseEvent event) {

    }

    @FXML
    void saveDatabaseSettings(MouseEvent event) {
        if (txtComboImpressoras.getSelectionModel().getSelectedItem() != null) {
            int result = Printers.initConfigurations().savePrinterConfig(txtComboImpressoras.getSelectionModel().getSelectedItem());
            if (result == 1) {
                AlertBuilder.build().confirm(AlertType.SUCCESS, btnSave, "Configurações Salvas com Sucesso");
            } else {
                AlertBuilder.build().confirm(AlertType.SUCCESS, btnSave, "Erro ao Salvar as Configurações");
            }
        }
    }

}

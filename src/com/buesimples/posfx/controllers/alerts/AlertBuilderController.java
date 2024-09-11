package com.buesimples.posfx.controllers.alerts;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class AlertBuilderController implements Initializable {

   @FXML
   private AnchorPane panelNode;

   @FXML
   private JFXButton btnClose;

   @FXML
   private Label txtAlertTitle;

   @FXML
   private Label txtAlertBody;

   private JFXDialog dialog;

   @Override
   public void initialize(URL location, ResourceBundle resources) {

   }

   @FXML
   void closeAlertManager(MouseEvent event) {
      this.dialog.close();
   }

   public void setAlertData(String title, String body, JFXDialog dialog) {
      txtAlertTitle.setText(title);
      txtAlertBody.setText(body);
      this.dialog = dialog;
   }
}

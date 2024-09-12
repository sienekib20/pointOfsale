package com.buesimples.posfx.controllers.alerts;

import java.net.URL;
import java.util.ResourceBundle;

import com.buesimples.posfx.controllers.cart.CarrinhoController;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ConfirmationController implements Initializable {

   private JFXDialog dialog;

   @FXML
   private AnchorPane panelNode;

   @FXML
   private JFXButton btnOk;

   @FXML
   private JFXButton btnNo;

   @FXML
   private Label txtAlertTitle;

   @FXML
   private Label txtAlertBody;

   @Override
   public void initialize(URL location, ResourceBundle resources) {

   }

   @FXML
   void noCloseAlert(MouseEvent event) {
      this.dialog.close();
   }

   @FXML
   void okCloseAlert(MouseEvent event) {
      CarrinhoController.getInstance().limparDadosCarrinho();      
      this.dialog.close();
   }

   public void setAlertData(String title, String body, JFXDialog dialog) {
      txtAlertTitle.setText(title);
      txtAlertBody.setText(body);
      this.dialog = dialog;
   }

}

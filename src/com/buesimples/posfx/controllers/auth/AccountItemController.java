package com.buesimples.posfx.controllers.auth;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class AccountItemController implements Initializable {

   private static AccountItemController instance;

   @FXML
   private JFXButton btnUsuario;

   @FXML
   private Label labelUsername;

   public AccountItemController() {
      instance = this;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {

   }

   public static AccountItemController getInstance() {
      return instance;
   }

   @FXML
   void actionSelectUsuario(MouseEvent event) {
      LoginController login = LoginController.getInstance();
      login.setUsuarioSelecionado(labelUsername.getText().trim());
      login.mudarPaneis(0);
   }

   public void setUsername(String username) {
      labelUsername.setText(username.trim());
   }

}

package com.buesimples.posfx.controllers.cart;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CarrinhoItemController implements Initializable {

   private static CarrinhoItemController instance;

   @FXML
   private JFXButton btnItem;

   @FXML
   private ImageView imgFamilia;

   @FXML
   private Label labelName;

   @FXML
   private Label labelStock;

   @FXML
   private JFXCheckBox familySelect;

   public CarrinhoItemController() {
      instance = this;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {

   }

   public static CarrinhoItemController getInstance() {
      return instance;
   }

   @FXML
   void showArtigos(MouseEvent event) {

   }

}

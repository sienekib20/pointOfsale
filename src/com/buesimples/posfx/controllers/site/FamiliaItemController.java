package com.buesimples.posfx.controllers.site;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class FamiliaItemController implements Initializable {

   private static FamiliaItemController instance;

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

   private int idFamilia;

   public FamiliaItemController() {
      instance = this;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {

   }

   public static FamiliaItemController getInstance() {
      return instance;
   }

   @FXML
   void showArtigos(MouseEvent event) {
      HomeController.getInstance().showArticles(idFamilia);
   }

   void setItems(int id, String familyName, String image) {
      idFamilia = id;
      labelName.setText(familyName.trim());
      imgFamilia.setImage(new Image(image));
   }

   int getIdFamilia() {
      return this.idFamilia;
   }
}

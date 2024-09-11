package com.buesimples.posfx.controllers.cart;

import java.net.URL;
import java.util.ResourceBundle;

import com.buesimples.posfx.models.Checkout;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class CheckoutItemController implements Initializable {

   private static CheckoutItemController instance;

   @FXML
   private HBox hboxItem;

   @FXML
   private Label txtProduto;

   @FXML
   private Label txtQtd;

   @FXML
   private Label txtPreco;

   private double txtPrecoSemImposto;

   private double txtPrecoComImposto;

   private double txtPrecoComDesconto;

   private double unitPrice, articlePrice_;

   public CheckoutItemController() {
      instance = this;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {

   }

   public static CheckoutItemController getInstance() {
      return instance;
   }

   public void setData(Checkout item, int id) {
      txtProduto.setText(item.getCheckoutNomeArtigo());
      unitPrice = Double.parseDouble(item.getCheckoutPrecoArtigo());
      txtPreco.setText(String.valueOf(unitPrice));
      articlePrice_ = unitPrice;
      txtPrecoSemImposto = Double.parseDouble(item.getCheckoutPrecoArtigo());
      txtQtd.setText(String.valueOf(item.getCheckoutQtdArtigo()));

   }

   public double getPrecoComImposto() {
      return txtPrecoComImposto;
   }

   public double getPrecoComDesconto() {
      return txtPrecoComDesconto;
   }

   public double getUnitPrice() {
      return unitPrice;
   }

   public double getArtclePrice() {
      return articlePrice_;
   }

   public void setUnitPrice(double unitPrice) {
      this.unitPrice = unitPrice;
   }

   public Label getArticleQtdLabel() {
      return txtQtd;
   }

   public Label getArticlePriceLabel() {
      return txtPreco;
   }

   public void setArticlePriceLabel(String price) {
      txtPreco.setText(price);
   }

   public double getPrecoInitialArtigo() {
      return txtPrecoSemImposto;
   }

}

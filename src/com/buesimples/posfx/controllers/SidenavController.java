package com.buesimples.posfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

public class SidenavController implements Initializable {

   @FXML
   private JFXButton btnHome;

   @FXML
   private JFXButton btnOrder;

   @FXML
   private JFXButton btnReports;

   @FXML
   private JFXButton btnClients;

   @FXML
   private JFXButton btnSettings;

   @FXML
   private JFXButton btnExit;

   @Override
   public void initialize(URL location, ResourceBundle resources) {

   }

   
   
   void setDisable(MouseEvent event) {
      setDisable(event, btnHome);
      setDisable(event, btnOrder);
      setDisable(event, btnReports);
      setDisable(event, btnClients);
      setDisable(event, btnSettings);
   }

   @FXML
   void actionExit(MouseEvent event) {

   }

   @FXML
   void actionReduceWindow(MouseEvent event) {

   }

   @FXML
   void showClients(MouseEvent event) {
      setDisable(event);
   }

   @FXML
   void showHome(MouseEvent event) {
      setDisable(event);
   }

   @FXML
   void showOrders(MouseEvent event) {

   }

   @FXML
   void showReports(MouseEvent event) {

   }

   @FXML
   void showSettings(MouseEvent event) {

   }

   void setDisable(MouseEvent event, JFXButton button) {
      if (event.getSource().equals(button)) {
         button.getStyleClass().remove("nav-link");
         button.getStyleClass().add("nav-link-active");
         button.setDisable(true);
      } else {
         button.getStyleClass().remove("nav-link-active");
         button.getStyleClass().add("nav-link");
         button.setDisable(false);
      }
   } 

}

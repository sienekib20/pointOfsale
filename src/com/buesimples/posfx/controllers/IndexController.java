package com.buesimples.posfx.controllers;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.utils.loader.NodeLoader;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class IndexController implements Initializable {

   private static IndexController instance;

   @FXML
   private StackPane panelRoot;

   @FXML
   private AnchorPane panelMain;

   @FXML
   private AnchorPane panelSidemenu;

   @FXML
   private Label iconRoot;

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

   @FXML
   private AnchorPane panelExtended;

   private NodeLoader loader;

   // private List<Map<String, Object>> idDefinicao;

   private List<Map<String, Object>> configurations;

   public IndexController() {
      instance = this;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      loader = NodeLoader.getInstance();
      showMainWindow();
   }

   public static IndexController getInstance() {
      return instance;
   }

   public StackPane getRootNode() {
      return panelRoot;
   }

   public AnchorPane getExtendedNode() {
      return panelExtended;
   }

   @FXML
   void setDisablebtns(MouseEvent event) {
      setDisablebtns(event, btnHome);
      setDisablebtns(event, btnOrder);
      setDisablebtns(event, btnReports);
      setDisablebtns(event, btnClients);
      setDisablebtns(event, btnSettings);
   }

   void showMainWindow() {
      btnHome.setDisable(true);
      loader.loadNode(panelExtended, "site.Home");
      configurations = DatabaseHelpers.build().select(
            "definicao",
            "GET");
      System.out.println("RETRIEVED DATA: " + configurations);
   }

   @FXML
   void actionExit(MouseEvent event) {
      System.exit(0);
   }

   @FXML
   void actionReduceWindow(MouseEvent event) {

   }

   @FXML
   void showClients(MouseEvent event) {
      showWindow(event, "site.Client");
   }

   @FXML
   void showHome(MouseEvent event) {
      showWindow(event, "site.Home");
   }

   @FXML
   void showOrders(MouseEvent event) {
      showWindow(event, "site.Order");
   }

   @FXML
   void showReports(MouseEvent event) {
      showWindow(event, "site.Report");
   }

   @FXML
   void showSettings(MouseEvent event) {
      showWindow(event, "site.Setting");
   }

   void showWindow(MouseEvent event, String fxml) {
      setDisablebtns(event);
      loader.load(panelExtended, fxml);
   }

   void setDisablebtns(MouseEvent event, JFXButton button) {
      if (event.getSource().equals(button)) {
         button.setDisable(true);
      } else {
         button.setDisable(false);
      }
   }

   public List<Map<String, Object>> getDefinition() {
      return configurations;
   }

}

package com.buesimples.posfx.controllers;

import com.buesimples.posfx.utils.loader.NodeLoader;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.util.List;
import java.util.Arrays;

public class MainController {

   @FXML
   private StackPane panelRoot;

   @FXML
   private AnchorPane panelMain;

   @FXML
   private AnchorPane panelExtented;

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

   // Lista de todos os botões de navegação
   private List<JFXButton> navButtons;

   @FXML
   private void initialize() {
      // Adicionar todos os botões de navegação à lista
      navButtons = Arrays.asList(btnHome, btnOrder, btnReports, btnClients, btnSettings);

      // Ativar o botão inicial (Home) por padrão
      setActiveButton(btnHome);
   }

   // Ações de navegação
   @FXML
   private void showHome() {
      loadView("Home");
      setActiveButton(btnHome);
   }

   @FXML
   private void showOrder() {
      loadView("Order");
      setActiveButton(btnOrder);
   }

   @FXML
   private void showReports() {
      loadView("Reports");
      setActiveButton(btnReports);
   }

   @FXML
   private void showClients() {
      // loadView("Clients");
      setActiveButton(btnClients);
   }

   @FXML
   private void showSettings() {
      loadView("Settings");
      setActiveButton(btnSettings);
   }

   // Ação para sair do aplicativo
   @FXML
   private void actionExit() {
      System.exit(0);
   }

   // Ação para minimizar a janela
   @FXML
   private void actionReduceWindow() {
      panelRoot.getScene().getWindow().hide();
   }

   // Método para carregar uma nova view no painel central
   private void loadView(String viewName) {
      NodeLoader.getInstance().load(panelExtented, viewName);
   }

   // Método para ativar o botão atual e desativar os outros
   private void setActiveButton(JFXButton activeButton) {
      // Remova o estado ativo de todos os botões
      for (JFXButton button : navButtons) {
          button.getStyleClass().remove("active-btn");
      }
      // Adicione o estado ativo ao botão atual
      activeButton.getStyleClass().add("active-btn");
  }
  
}

package com.buesimples.posfx.controllers.auth;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.buesimples.posfx.animations.Animations;
import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.utils.config.Config;
import com.buesimples.posfx.utils.constants.Constants;
import com.buesimples.posfx.utils.hashing.Hash;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable {

   private static LoginController instance;

   @FXML
   private AnchorPane panelRoot;

   @FXML
   private AnchorPane panelLogin;

   @FXML
   private Label inputSelectedUser;

   @FXML
   private JFXPasswordField inputSenha;

   @FXML
   private JFXButton btnLogin;

   @FXML
   private Label btnExitPanel;

   @FXML
   private Label btnInformar;

   @FXML
   private Label btnExit;

   @FXML
   private AnchorPane panelUsuarios;

   @FXML
   private JFXTextField inputPesquisa;

   @FXML
   private VBox vboxLista;

   

   public LoginController() {
      instance = this;
   }

   @Override
   public void initialize(URL location, ResourceBundle resources) {
      this.carregarUsuarios();
   }

   public static LoginController getInstance() {
      return instance;
   }

   private void carregarUsuarios() {
      List<Map<String, Object>> map = DatabaseHelpers.build().select(
            "usuario", "GET");
      Config.putInCache("usuario", map);

      for (Map<String, Object> item : map) {

         String username = (String) item.get("nome");

         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(getClass().getResource(Constants.view("auth.AccountItem")));
         JFXButton button = null;
         try {
            button = loader.load();
         } catch (IOException e) {
            e.printStackTrace();
         }
         AccountItemController accountItem = loader.getController();
         accountItem.setUsername(username);

         vboxLista.getChildren().add(button);
      }
   }

   @FXML
   void actionExit(MouseEvent event) {
      ((Stage) btnExit.getScene().getWindow()).close();
   }

   @FXML
   void actionExitPanel(MouseEvent event) {
      mudarPaneis(0);
   }

   public void setUsuarioSelecionado(String username) {
      inputSelectedUser.setText(username.trim());
   }

   @FXML
   void actionFiltrarDados(KeyEvent event) {

   }

   @FXML
   void actionInformarGestor(MouseEvent event) {

   }

   @FXML
   void actionLogin(MouseEvent event) {
      if (inputSenha.getText().isEmpty()) {

         System.out.println("Alerta de Erro");

      } else {
         String nomeUsuario = DatabaseHelpers.getColumnValue(
               Config.getFromCache("usuario"),
               "nome");
         String senhaUsuario = DatabaseHelpers.getColumnValue(
               Config.getFromCache("usuario"), "senha");

         if (inputSelectedUser.getText().toLowerCase().trim().equals(nomeUsuario.trim().toLowerCase())) {
            if (Hash.verify(inputSenha.getText(), senhaUsuario)) {
               Stage primaryStage = ((Stage) btnLogin.getScene().getWindow());
               try {
                  FXMLLoader fxml = new FXMLLoader();
                  //fxml.setLocation(getClass().getResource(Constants.MAIN_VIEW));
                  fxml.setLocation(getClass().getResource(Constants.view("Main")));

                  Parent root = fxml.load();

                  Stage stage = new Stage(StageStyle.UNDECORATED);
                  stage.getIcons().add(new Image(Constants.STAGE_ICON));
                  stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                  stage.setFullScreen(true);
                  stage.setMinHeight(Constants.MIN_HEIGHT);
                  stage.setMinWidth(Constants.MIN_WIDTH);
                  stage.setResizable(false);
                  stage.setTitle(Constants.TITLE);
                  stage.setScene(new Scene(root));
                  stage.show();

                  primaryStage.close();

                  root.setOnKeyPressed((KeyEvent e) -> {
                     if (e.getCode().equals(KeyCode.F11)) {
                        stage.setFullScreen(!stage.isFullScreen());
                     }
                  });

                  stage.setOnCloseRequest(ev -> {
                     /*
                      * DatabaseHelper.logout();
                      * ProductsController.closeStage();
                      */
                  });

               } catch (IOException e) {
                  e.printStackTrace();
               }

            }
         }

      }
   }

   @FXML
   void actionLoginWithKey(KeyEvent event) {

   }

   @FXML
   void actionSelectUsuario(MouseEvent event) {
      mudarPaneis(1);
   }

   void mudarPaneis(int type) {
      if (type == 1) {
         panelLogin.toBack();
         panelUsuarios.setVisible(true);
         panelUsuarios.toFront();
         Animations.fadeIn(panelUsuarios, 5);
      } else {
         panelLogin.toFront();
         panelUsuarios.setVisible(false);
         panelUsuarios.toBack();
      }

   }

}

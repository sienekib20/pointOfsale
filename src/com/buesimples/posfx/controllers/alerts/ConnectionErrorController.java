package com.buesimples.posfx.controllers.alerts;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class ConnectionErrorController implements Initializable {

   @FXML
   private Label btnExit;

   @Override
   public void initialize(URL location, ResourceBundle resources) {
   }

   @FXML
   void closeAlert(MouseEvent event) {
      ((Stage) btnExit.getScene().getWindow()).close();

      System.exit(0);
   }

}

package com.buesimples.posfx;

import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.utils.loader.NodeLoader;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

   @Override
   public void start(Stage primaryStage) throws Exception {
      NodeLoader loader = NodeLoader.getInstance();

      if (DatabaseHelpers.build().checkConnection() == false) {
         loader.showWindow(primaryStage, "alerts.ConnectionError");
         return;
      }
      this.pretifyFont();
      loader.showWindow(primaryStage, "auth.Login");
   }

   public static void main(String[] args) {
      launch(args);
   }

   private void pretifyFont() {
      System.setProperty("prism.lcdtext", "true");
      System.setProperty("prism.text", "t2k");
      System.setProperty("prism.subpixeltext", "true");
      System.setProperty("prism.allowhidpi", "true");
   }

}

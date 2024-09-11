package com.buesimples.posfx.utils.constants;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.effect.BoxBlur;

public class Constants {

   public static final String TITLE = "Buesimples Pos";
   public static final Double MIN_WIDTH = 1040.00;
   public static final Double MIN_HEIGHT = 640.00;

   public static final String SOURCE_PACKAGES = "/com/buesimples/posfx";
   public static final String MEDIA_PACKAGE = "/resources/media/";
   public static final String VIEWS_PACKAGE = SOURCE_PACKAGES + "/views/";
   public static final String PROFILE_PICTURES_PACKAGE = MEDIA_PACKAGE + "profiles/";
   public static final String FONTS_PACKAGE = SOURCE_PACKAGES + "/fonts/";

   public static final String LOGIN_VIEW = VIEWS_PACKAGE + "LoginView.fxml";
   public static final String START_VIEW = VIEWS_PACKAGE + "StartView.fxml";
   public static final String MAIN_VIEW = VIEWS_PACKAGE + "MainView.fxml";
   public static final String STAGE_ICON = MEDIA_PACKAGE + "favicon.png";

   public static final String JSON_PATH = SOURCE_PACKAGES + "/config/";
   public static final String JSON_SETTINGS = JSON_PATH + "env.json";

   public static final Map<String, String> VIEW_NODE_MAP = new HashMap<>();

   public static final BoxBlur BOX_BLUR_EFFECT = new BoxBlur(3, 3, 3);

   static {
      VIEW_NODE_MAP.put("inicio", "Inicio");
      VIEW_NODE_MAP.put("setting", "settings.Definicao");
   }

   public static final String getJsonFile(String filename) {
      return JSON_PATH + filename;
   }

   /**
    *
    * @param view
    * @return
    */
   public static final String view(String view) {
      if (view == null || view.isEmpty()) {
         throw new IllegalArgumentException("A view não pode ser nula ou vazia.");
      }

      if (view.contains(".")) {
         view = view.replace('.', '/');
      }
      return VIEWS_PACKAGE + view + "View.fxml";
   }

}

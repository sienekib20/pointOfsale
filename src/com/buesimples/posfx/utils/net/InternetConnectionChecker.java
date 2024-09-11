package com.buesimples.posfx.utils.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternetConnectionChecker {

   public static boolean isConnected() {
      try {
         // Tentar conectar a um site confiável (como o Google)
         URL url = new URL("https://www.google.com");
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("HEAD");
         conn.setConnectTimeout(5000); // 5 segundos para tentar conectar
         conn.setReadTimeout(5000); // 5 segundos para ler os dados
         int responseCode = conn.getResponseCode();

         // Se a resposta é 200, estamos conectados à internet
         return (responseCode == 200);
      } catch (IOException e) {
         // Se ocorre uma exceção, consideramos que não há conexão
         return false;
      }
   }

}

package com.buesimples.posfx.database.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class Api {

   private static Api instance = null;

   public Api() {
      instance = this;
   }

   public static Api getInstance() {
      if (instance == null) {
         instance = new Api();
      }
      return instance;
   }

   public String makeRequest(String urlString, String requestMethod, String... body) throws Exception {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod(requestMethod);

      // Configura o cabeçalho para aceitar JSON (opcional, se necessário)
      conn.setRequestProperty("Content-Type", "application/json");

      // Verifica se o corpo foi passado e se o método é POST
      if (body.length > 0 && ("POST".equalsIgnoreCase(requestMethod) || "PUT".equalsIgnoreCase(requestMethod))) {
         conn.setDoOutput(true);
         try (OutputStream os = conn.getOutputStream()) {
            byte[] input = body[0].getBytes("utf-8");
            os.write(input, 0, input.length);
         }
      }

      StringBuilder content;
      try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
         String inputLine;
         content = new StringBuilder();
         while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
         }
      }
      return content.toString();
   }

   public List<Map<String, Object>> parseResponse(String jsonResponse) {
      Gson gson = new Gson();
      JsonElement jsonElement = JsonParser.parseString(jsonResponse);

      Type listType = new TypeToken<List<Map<String, Object>>>() {
      }.getType();
      if (jsonElement.isJsonArray()) {
         // JSON é um array de objetos
         return gson.fromJson(jsonResponse, listType);
      } else if (jsonElement.isJsonObject()) {
         // JSON é um único objeto
         Map<String, Object> singleObject = gson.fromJson(jsonResponse, new TypeToken<Map<String, Object>>() {
         }.getType());
         return Collections.singletonList(singleObject);
      } else {
         // JSON pode ser de outro tipo (ex.: valor primitivo, string, etc.)
         return Collections.emptyList();
      }
   }

   public List<Map<String, Object>> get(String url, String method) {

      try {
         String jsonResponse = makeRequest(url, method);
         List<Map<String, Object>> data = parseResponse(jsonResponse);

         return data;

      } catch (Exception e) {
         System.out.println("Error in get: " + e.getMessage());
      }
      return Collections.emptyList();
   }

   public boolean pass(String url, String method) {

      try {
         String jsonResponse = makeRequest(url, method);

         // List<Map<String, Object>> data = parseResponse(jsonResponse);
         parseResponse(jsonResponse);

         return true;

      } catch (Exception e) {
         return false;
      }
   }

   public List<Map<String, Object>> post(String url, String method, String... body) {
      try {
         String jsonResponse = makeRequest(url, method, body);
         List<Map<String, Object>> data = parseResponse(jsonResponse);

         return data;

      } catch (Exception e) {
         System.out.println("Error in post: " + e.getMessage());
      }
      // String url = "http://localhost/pos/api/querySelect?tabela=artigo";
      // String body = "{\'idFamilia\' = 1}";
      // String jsonResponse = client.makeRequest(url, "POST", body);
      return Collections.emptyList();
   }

   public List<Map<String, Object>> select(String query, String... params) {

      return Collections.emptyList();

   }

}

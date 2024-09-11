package com.buesimples.posfx.utils.config;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buesimples.posfx.utils.constants.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Config {

   private static final String CACHE_FILE = Constants.getJsonFile("cache.json");

   private static Map<String, List<Map<String, Object>>> cache;

   static {
      loadCache();
   }

   public static Map<String, Object> load() {
      Gson gson = new Gson();
      try (InputStream inputStream = Config.class.getResourceAsStream(Constants.getJsonFile("env.json"));
            Reader reader = new InputStreamReader(inputStream, "UTF-8")) {
         Map<String, Object> data = gson.fromJson(
               reader, new TypeToken<Map<String, Object>>() {
               }.getType());

         return data;
      } catch (Exception e) {
         e.printStackTrace(); // Para ajudar no debug
         System.out.println("Erro In Config: " + e.getMessage() + " Linha de Erro: Config.java - 25");
      }

      return new HashMap<>();
   }

   public static String get(String index) {

      return (String) load().get(index);
   }

   public static void set(Object item) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      Map<String, Object> data = new HashMap<>();
      // data.put("nome", "John Doe");
      // data.put("idade", 30);
      // data.put("email", "johndoe@example.com");

      try (Writer writer = new FileWriter("output.json")) {
         gson.toJson(data, writer);
      } catch (Exception e) {
      }
   }

   public static void putInCache(String tableName, List<Map<String, Object>> map) {
      cache.put(tableName, map);
      saveCache();
   }

   // Método para carregar o cache do arquivo JSON
   private static void loadCache() {

      Gson gson = new Gson();
      try (InputStream inputStream = Config.class.getResourceAsStream(CACHE_FILE);
            Reader reader = new InputStreamReader(inputStream, "UTF-8")) {

         Type cacheType = new TypeToken<Map<String, List<Map<String, Object>>>>() {
         }.getType();
         cache = gson.fromJson(reader, cacheType);

         if (cache == null) {
            cache = new HashMap<>();
         }
      } catch (Exception e) {
         System.out.println("Não foi possível carregar o cache: " + e.getMessage());
         cache = new HashMap<>();
      }
   }

   // Método para salvar o cache no arquivo JSON
   private static void saveCache() {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();

      String projectDir = System.getProperty("user.dir");
      String cacheFilePath = projectDir + "/src" + CACHE_FILE;

      // Crie o diretório se não existir
      // File cacheDir = new File(projectDir + "/cache");
      // if (!cacheDir.exists()) {
      //    cacheDir.mkdirs();
      // }

      try (FileOutputStream fileOutputStream = new FileOutputStream(cacheFilePath);
            Writer writer = new OutputStreamWriter(fileOutputStream, "UTF-8")) {

         gson.toJson(cache, writer);
         System.out.println("Cache salvo em: " + cacheFilePath);

      } catch (IOException e) {
         System.out.println("ERROR ao salvar o cache: " + e.getMessage());
      }
   }

   // Método para obter dados do cache
   public static List<Map<String, Object>> getFromCache(String tableName) {
      return cache.get(tableName);
   }

   // Método para atualizar o cache
   public static void updateCache(String tableName, List<Map<String, Object>> data) {
      cache.put(tableName, data);
      saveCache(); // Salva o cache atualizado no arquivo JSON
   }

   // Método para obter o valor de uma coluna específica em uma tabela do cache
   @SuppressWarnings("unchecked")
   public static <T> T getColumnValue(String tableName, String columnName, int rowIndex, Class<T> type) {
      // Obtém os dados do cache para a tabela
      List<Map<String, Object>> tableData = cache.get(tableName);

      // Verifica se a tabela e o índice da linha existem
      if (tableData != null && rowIndex >= 0 && rowIndex < tableData.size()) {
         Map<String, Object> row = tableData.get(rowIndex);

         // Verifica se a coluna existe na linha e faz o cast para o tipo desejado
         if (row.containsKey(columnName)) {
            return (T) row.get(columnName); // Cast para o tipo genérico T
         }
      }

      return null; // Retorna null se não encontrar o valor
   }

}

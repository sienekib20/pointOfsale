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
import java.io.File;

public class Config {

    private static final String CACHE_FILE = Constants.getJsonFile("cache");

    private static final String LOCAL_STORAGE = Constants.getJsonFile("localStorage");

    private static final String CONFIG_FILE = Constants.getJsonFile("setting");

    private static Map<String, List<Map<String, Object>>> cache;

    private static Map<String, Object> localStorageData;

    static {
        loadCache();
    }

    public static Map<String, Object> load(String file) {
        Gson gson = new Gson();
        try (InputStream inputStream = Config.class.getResourceAsStream(Constants.getJsonFile(file));
                Reader reader = new InputStreamReader(inputStream, "UTF-8")) {
            Map<String, Object> data = gson.fromJson(
                    reader, new TypeToken<Map<String, Object>>() {
            }.getType());

            return data;
        } catch (Exception e) {
            // e.printStackTrace(); // Para ajudar no debug
            System.out.println("Erro In Config: " + e.getMessage() + " - Linha de Erro: Config.java - 25");
        }

        return new HashMap<>();
    }

    public static String get(String jsonFile, String index) {

        return (String) load(jsonFile).get(index);
    }

    public static String getMap(String jsonFile, String index) {

        Map<String, Object> map = (Map<String, Object>) load(jsonFile).get("session");

        for (String key : map.keySet()) {
            if (key.equals(index)) {
                return map.get(key).toString();
            }
        }

        return null;
    }

    public static int setConfig(String key, String value) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            // Certifique-se de que o diretório existe
            String projectDir = System.getProperty("user.dir");
            String configFile = projectDir + "/src" + CONFIG_FILE;

            try (FileOutputStream fileOutput = new FileOutputStream(configFile);
                    Writer writer = new OutputStreamWriter(fileOutput, "UTF-8")) {
                Map<String, String> configItem = new HashMap<>();
                configItem.put(key, value);
                gson.toJson(configItem, writer);
                return 1;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("ERROR ao salvar no config: " + ex.getMessage());
            return 0;
        }
    }
    
    public static String getConfig(String key) {
        Gson gson = new Gson();
        try (InputStream inputStream = Config.class.getResourceAsStream(Constants.getJsonFile("setting"));
                Reader reader = new InputStreamReader(inputStream, "UTF-8")) {
            Map<String, String> data = gson.fromJson(
                    reader, new TypeToken<Map<String, Object>>() {
            }.getType());
            if (data.get(key) != null) {
                return data.get(key);
            }
            return null;
        } catch (Exception e) {
            // e.printStackTrace(); // Para ajudar no debug
            System.out.println("Erro In Config: " + e.getMessage() + " - Linha de Erro: Config.java - 25");
        }

        return null;
    }

    public static void set(String key, Object item) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String projectDir = System.getProperty("user.dir");
        String cacheFilePath = projectDir + "/src" + LOCAL_STORAGE;

        try (FileOutputStream fileOutputStream = new FileOutputStream(cacheFilePath);
                Writer writer = new OutputStreamWriter(fileOutputStream, "UTF-8")) {

            localStorageData = new HashMap<>();
            localStorageData.put(key, item);

            gson.toJson(localStorageData, writer);
        } catch (IOException e) {
            System.out.println("ERROR ao salvar o cache: " + e.getMessage());
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

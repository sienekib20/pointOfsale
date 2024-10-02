package com.buesimples.posfx.database;

import java.util.List;
import java.util.Map;

import com.buesimples.posfx.database.api.Api;
import com.buesimples.posfx.utils.config.Config;
import com.buesimples.posfx.utils.hashing.Hash;
import com.buesimples.posfx.utils.json.JsonUtils;
import com.jfoenix.controls.JFXDialog;
import java.util.HashMap;

public class DatabaseHelpers {

    private static DatabaseHelpers instance = null;

    private static String url;

    private static Map<String, Object> localStorage;

    public DatabaseHelpers() {
        instance = this;
    }

    public static DatabaseHelpers getInstance() {
        if (instance == null) {
            new DatabaseHelpers();
        }
        return instance;
    }

    public static DatabaseHelpers build() {
        // url = InternetConnectionChecker.isConnected() ? Config.get("api") :
        // Config.get("url");
        url = Config.get("env", "url");

        return getInstance();
    }

    public boolean checkConnection() {

        return Api.getInstance().pass(url + "select?tabela=usuario", "GET");

    }

    /**
     *
     * @param tabela
     * @param requestMethod
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> select(String tabela, String requestMethod) {

        List<Map<String, Object>> data = Api.getInstance().get(
                url + "select?tabela=" + tabela,
                requestMethod);

        return data;
    }

    /**
     *
     * @param tabela
     * @param requestMethod
     * @param body
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> querySelect(String tabela, String requestMethod, String... body) {

        List<Map<String, Object>> data = Api.getInstance().post(
                url + "querySelect?tabela=" + tabela,
                requestMethod,
                body);

        return data;
    }

    // Método original com requestMethod como parâmetro
    /**
     *
     * @param requestMethod
     * @param raw
     * @return
     */
    public List<Map<String, Object>> querySelectData(String requestMethod, String... raw) {
        List<Map<String, Object>> data = Api.getInstance().post(
                url + "querySelectData",
                requestMethod,
                raw);
        return data;
    }

    public void insert(String table, String requestMethod, String... data) {
        List<Map<String, Object>> map = Api.getInstance().post(
                url + "insert?tabela=" + table,
                requestMethod,
                data);
    }

    public int lastInsertId(String tableName) {

        List<Map<String, Object>> data = select(tableName, "GET");
        Map<String, Object> lastMap = data.get(data.size() - 1);
        String idCol = "id" + tableName.substring(0, 1).toUpperCase() + tableName.substring(1).toLowerCase();

        int lastId = -1;

        for (String key : lastMap.keySet()) {

            lastId = (int) Double.parseDouble(lastMap.get(idCol).toString());
        }

        return lastId;

    }

    public void buildCache(String tableName, List<Map<String, Object>> map) {

    }

    public List<Map<String, Object>> queryWithCache(String tableName, String query) {
        // Primeiro, verifica se os dados estão no cache
        List<Map<String, Object>> cachedData = Config.getFromCache(tableName);
        if (cachedData != null) {
            System.out.println("Dados carregados do cache para a tabela: " + tableName);
            return cachedData;
        }

        // Se os dados não estão no cache, faz a consulta no banco de dados
        List<Map<String, Object>> data = querySelect(tableName, query);

        // Atualiza o cache com os novos dados
        Config.updateCache(tableName, data);

        return data;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getColumnValue(List<Map<String, Object>> map, String columnName) {
        if (map == null || columnName == null) {
            return null; // Validação básica de entrada
        }

        for (Map<String, Object> item : map) {
            if (item.containsKey(columnName)) {
                return (T) item.get(columnName); // Retorna o valor sem a necessidade de especificar o tipo
            }
        }

        return null; // Retorna null se a coluna não for encontrada
    }

    @SuppressWarnings("unchecked")
    public static <T> T getColumnValueFromMap(Map<String, Object> map, String columnName) {
        // Validação básica de entrada
        if (map == null || columnName == null) {
            return null;
        }

        // Verifica se o mapa contém a chave especificada
        if (map.containsKey(columnName)) {
            try {
                // Retorna o valor associado à chave, convertido para o tipo genérico T
                return (T) map.get(columnName);
            } catch (ClassCastException e) {
                // Trata a exceção de conversão de tipo, se ocorrer
                System.err.println("Erro de conversão de tipo: " + e.getMessage());
                return null;
            }
        }

        // Retorna null se a coluna não for encontrada
        return null;
    }

    public static JFXDialog.DialogTransition dialogTransition() {
        return JFXDialog.DialogTransition.valueOf("");
    }

    public String getTableValue(List<Map<String, Object>> tableMap, String colIdName, int whereId, String neededField) {
        for (Map<String, Object> map : tableMap) {
            // Tente converter como inteiro primeiro
            try {
                int tableId = Integer.parseInt(map.get(colIdName).toString());
                if (tableId == whereId) {
                    return map.get(neededField).toString();
                }
            } catch (NumberFormatException e) {
                // Caso não seja um inteiro, verifique se é um número decimal
                double tableId = Double.parseDouble(map.get(colIdName).toString());
                if (tableId == whereId) {
                    return map.get(neededField).toString();
                }
            }
        }
        return null;
    }

    public int gerarDocumento(
            int idTipoDoc,
            int idUsuario,
            int idEntidade,
            int idPrazoPagamento,
            int idSerie,
            String dataDoc,
            double entregue,
            double totalLiquido,
            double descontoGlobal,
            double total,
            double troco,
            double imposto) {

        Map<String, Object> data = new HashMap<>();

        data.put("idTipoDoc", idTipoDoc);
        data.put("idUsuario", idUsuario);
        data.put("idEntidade", idEntidade != 0 ? idEntidade : 1);
        data.put("idPrazoPagamento", idPrazoPagamento);
        data.put("idSerie", idSerie);
        data.put("dataDoc", dataDoc);
        data.put("entregue", entregue);
        data.put("totalLiquido", totalLiquido);
        data.put("descontoGlobal", descontoGlobal);
        data.put("total", total);
        data.put("troco", troco);
        data.put("imposto", imposto);

        String jsonString = JsonUtils.mapToJsonString(data);
        DatabaseHelpers.build().insert("documento", "POST", jsonString);
        return DatabaseHelpers.build().lastInsertId("documento");
    }

}

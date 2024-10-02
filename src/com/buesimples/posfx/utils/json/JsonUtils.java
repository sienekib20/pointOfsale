package com.buesimples.posfx.utils.json;

import java.util.Map;
import org.jfree.data.json.impl.JSONObject;

/**
 *
 * @author sienekib
 */

public class JsonUtils {

    public static String mapToJsonString(Map<String, Object> data) {
        JSONObject json = new JSONObject();

        // Percorre cada entrada no Map e insere no JSONObject
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();

            // Verifica se o valor é nulo, e trata como JSONObject.NULL
            if (value == null) {
                json.put(entry.getKey(), null);
            } else {
                json.put(entry.getKey(), value);
            }
        }

        // Retorna o JSONObject como string
        return json.toString();
    }

    public static String nonNullElse(Map<String, Object> o, String key, int need) {
        if (need == 0) {
            return o.get(key).toString() != null ? o.get(key).toString() : "";
        }
        return o.get(key).toString() != null ? o.get(key).toString() : "0.0";
    }
}
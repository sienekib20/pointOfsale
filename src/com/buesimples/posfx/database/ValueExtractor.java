package com.buesimples.posfx.database;

import java.util.Map;
import java.util.Optional;

public class ValueExtractor {

    public enum ValueType {
        INTEGER,
        STRING,
        DOUBLE
    }

    public static <T> Optional<T> getValueInMap(String field, ValueType type, Map<String, Object> in) {
        if (in == null || !in.containsKey(field)) {
            return Optional.empty();  // Retorna Optional vazio se o campo não existir
        }

        Object value = in.get(field);
        if (value == null) {
            return Optional.empty();  // Retorna Optional vazio se o valor for nulo
        }

        try {
            switch (type) {
                case INTEGER:
                    return Optional.of((T) Integer.valueOf(value.toString()));
                case STRING:
                    return Optional.of((T) value.toString());
                case DOUBLE:
                    return Optional.of((T) Double.valueOf(value.toString()));
                default:
                    return Optional.empty();  // Caso o tipo não seja reconhecido
            }
        } catch (NumberFormatException e) {
            // Retorna Optional vazio em caso de erro de conversão
            return Optional.empty();
        }
    }
}

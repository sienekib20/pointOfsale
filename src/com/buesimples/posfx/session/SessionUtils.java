package com.buesimples.posfx.session;

import com.buesimples.posfx.database.DatabaseHelpers;
import com.buesimples.posfx.utils.config.Config;
import com.buesimples.posfx.utils.hashing.Hash;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sienekib
 */
public class SessionUtils {

    private static Map<String, Object> localStorage;

    public static void createSessionStorage() {
        localStorage = new HashMap<>();
        localStorage.put("sessionToken", Hash.generateToken());
        localStorage.put("sessionId", DatabaseHelpers.getColumnValue(Config.getFromCache("usuario"), "idUsuario"));
        localStorage.put("sessionUsername", DatabaseHelpers.getColumnValue(Config.getFromCache("usuario"), "nome"));
        localStorage.put("sessionBI", DatabaseHelpers.getColumnValue(Config.getFromCache("usuario"), "bi"));
        localStorage.put("sessionUserId", DatabaseHelpers.getColumnValue(Config.getFromCache("usuario"), "nomeUsuario"));
        localStorage.put("sessionDate", currentTimestamp());
        Config.set("session", localStorage);
    }

    public static String currentTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        return formattedDate;
    }

}

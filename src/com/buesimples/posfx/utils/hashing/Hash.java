package com.buesimples.posfx.utils.hashing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

import com.buesimples.posfx.database.DatabaseHelpers;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Formatter;
import java.util.List;
import java.util.Map;

public class Hash {

    private static Hash instance = null;

    private static String content;

    public Hash() {
        instance = this;
    }

    public static Hash getInstance() {
        if (instance == null) {
            new Hash();
        }
        return instance;
    }

    /**
     * Verifica se uma senha em texto plano corresponde a uma senha com hash
     * BCrypt.
     *
     * @param plainPassword  Senha em texto plano.
     * @param hashedPassword Senha com hash BCrypt.
     * @return Verdadeiro se a senha corresponder, falso caso contrário.
     */
    public static boolean verify(String plainPassword, String hashedPassword) {
        if (hashedPassword.startsWith("$2y$")) {
            hashedPassword = hashedPassword.replaceFirst("\\$2y\\$", "\\$2a\\$");
        }
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    /**
     * Gera um hash BCrypt para uma senha em texto plano.
     *
     * @param plainPassword Senha em texto plano.
     * @return Hash BCrypt gerado para a senha.
     */
    public static String generateHash(String plainPassword) {
        // Gera o hash BCrypt para a senha fornecida
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Gera um token seguro e o hash BCrypt para o token.
     *
     * @return O token gerado.
     */
    public static String generateToken() {
        // Gerar um token aleatório. Aqui, estamos usando uma combinação de tempo atual
        // e um número aleatório.
        // Você pode usar uma abordagem mais robusta se necessário.
        String token = Long.toHexString(System.currentTimeMillis())
                + Long.toHexString(Double.doubleToLongBits(Math.random()));
        return generateHash(token); // Gera o hash para o token
    }

    /**
     * Verifica se um token corresponde ao hash BCrypt.
     *
     * @param token       O token em texto simples.
     * @param hashedToken O hash BCrypt do token.
     * @return Verdadeiro se o token corresponder, falso caso contrário.
     */
    public static boolean verifyToken(String token, String hashedToken) {
        return verify(token, hashedToken);
    }

    private String buildContent() {
        List<Map<String, Object>> __map__ = DatabaseHelpers.build().querySelect(
                "viewartigomodopagamentodocumento",
                "POST", "{\r\n" + //
                        "    \"idDocumento\": 355\r\n" + //
                        "}");
        String hashSaft = null;
        for (Map<String, Object> mm : __map__) {
            hashSaft = mm.get("hashSaft") != null ? mm.get("hashSaft").toString() : null;
        }

        List<Map<String, Object>> mapDoc = DatabaseHelpers.build().querySelect(
                "viewartigomodopagamentodocumento",
                "POST", "{\r\n" + //
                        "    \"idDocumento\": 356\r\n" + //
                        "}");
        String content = null;
        hashSaft = hashSaft != null ? ";" + hashSaft : ";";
        for (Map<String, Object> map : mapDoc) {
            String dataDoc = map.get("dataDoc") != null ? map.get("dataDoc").toString() : "";
            String codigoDocumento = map.get("codigoDocumento") != null ? map.get("codigoDocumento").toString() : "";
            String totalT = map.get("total") != null ? map.get("total").toString() : "0.0";
            int total = (int) Double.parseDouble(totalT);

            String[] dataParts = dataDoc.contains("T") ? dataDoc.split("T") : dataDoc.split(" ");
            String horas = dataParts[1];
            String[] horaParts = dataParts[1].contains("+") ? horas.split("\\+") : horas.split(" ");
            content = dataParts[0] + ";" + dataParts[0] + "T" + horaParts[0] + ";" + codigoDocumento + ";"
                    + String.valueOf(total) + hashSaft;

        }
        return content;
    }

    public String generateHashSaft() {
        try {
            // Passo PHP: $content = "Y-m-d;Y-m-dTH:m:s;codigoDoc;total;hash";

            // Passo PHP: $key = file_get_contents("../../saft/ChavePrivada.pem");
            PrivateKey privateKey = loadPrivateKeyPKCS1("/resources/hash/key/ChavePrivada.pem");

            // Passo PHP: openssl_sign($content, $assinatura, $key, OPENSSL_ALGO_SHA1);
            byte[] assinatura = sign(buildContent(), privateKey);

            // Passo PHP: $sha1 = bin2hex($assinatura);
            String sha1 = toHex(assinatura);

            // Passo PHP: $base64 = base64_encode($assinatura);
            String base64 = Base64.getEncoder().encodeToString(assinatura);

            // Imprimir resultados
            System.out.println("SHA1 Hash: " + sha1);
            System.out.println("Base64 Signature: " + base64);
            return sha1;

        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("ERROR: " + e.getMessage());
            return null;
        }
    }

    public static Hash buildHash(String timestamp, String codigoDoc, double total, String hash) {
        content = "";
        String separator = "";
        String end = ";" + codigoDoc + ";" + String.valueOf(total) + ";";
        end += (hash != null) ? hash : "";

        if (timestamp.contains("T")) {
            separator = "T";
        }

        if (timestamp.contains(" ")) {
            separator = " ";
        }

        String[] ts = timestamp.split(separator);
        String rest = "";
        content = ts[0];
        content += " ".equals(separator) ? "T" : separator;
        if (ts[1].contains("+")) {
            rest = ts[1].replace("+", " ");
        }
        content += ts[1].contains("+") ? rest.split(" ")[0] : ts[1];
        content += end;

        return getInstance();

    }

    // Passo PHP: Carregar chave privada do arquivo PEM
    private static PrivateKey loadPrivateKeyPKCS1(String resourcePath) throws Exception {
        try (InputStream keyStream = Hash.class.getResourceAsStream(resourcePath)) {
            if (keyStream == null) {
                throw new IOException("Chave privada não encontrada: " + resourcePath);
            }
            byte[] keyBytes = readInputStream(keyStream);
            String keyString = new String(keyBytes);
            keyString = keyString
                    .replaceAll("-----BEGIN RSA PRIVATE KEY-----", "")
                    .replaceAll("-----END RSA PRIVATE KEY-----", "")
                    .replaceAll("\\s+", ""); // Remove todos os espaços em branco

            byte[] decodedKey = Base64.getDecoder().decode(keyString);

            // Parse the decodedKey assuming it is PKCS#1 format
            BigInteger modulus = new BigInteger(1, Arrays.copyOfRange(decodedKey, 29, 157));
            BigInteger privateExponent = new BigInteger(1, Arrays.copyOfRange(decodedKey, 158, decodedKey.length));

            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, privateExponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }
    }

    private static byte[] readInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, bytesRead);
        }
        return buffer.toByteArray();
    }

    // Passo PHP: Assinar o conteúdo com a chave privada
    private static byte[] sign(String content, PrivateKey privateKey) throws Exception {
        // Signature signer = Signature.getInstance("SHA1withRSA");
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initSign(privateKey);
        signer.update(content.getBytes());
        return signer.sign();
    }

    // Passo PHP: Converter byte[] para hexadecimal
    private static String toHex(byte[] bytes) {
        try (Formatter formatter = new Formatter()) {
            for (byte b : bytes) {
                formatter.format("%02x", b);
            }
            return formatter.toString();
        }
    }

}

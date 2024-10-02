package com.buesimples.posfx.utils.hashing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Formatter;

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
     * @param plainPassword Senha em texto plano.
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
        // Gerar um token aleatório. Aqui, estamos usando uma combinação de tempo atual e um número aleatório.
        // Você pode usar uma abordagem mais robusta se necessário.
        String token = Long.toHexString(System.currentTimeMillis()) + Long.toHexString(Double.doubleToLongBits(Math.random()));
        return generateHash(token); // Gera o hash para o token
    }

    /**
     * Verifica se um token corresponde ao hash BCrypt.
     *
     * @param token O token em texto simples.
     * @param hashedToken O hash BCrypt do token.
     * @return Verdadeiro se o token corresponder, falso caso contrário.
     */
    public static boolean verifyToken(String token, String hashedToken) {
        return verify(token, hashedToken);
    }

    public void generateHashSaft() {
        try {
            // Passo PHP: $content = "Y-m-d;Y-m-dTH:m:s;codigoDoc;total;hash";

            // Passo PHP: $key = file_get_contents("../../saft/ChavePrivada.pem");
            PrivateKey privateKey = loadPrivateKey("/resources/hash/key/ChavePrivada.pem");

            // Passo PHP: openssl_sign($content, $assinatura, $key, OPENSSL_ALGO_SHA1);
            byte[] assinatura = sign(content, privateKey);

            // Passo PHP: $sha1 = bin2hex($assinatura);
            String sha1 = toHex(assinatura);

            // Passo PHP: $base64 = base64_encode($assinatura);
            String base64 = Base64.getEncoder().encodeToString(assinatura);

            // Imprimir resultados
            System.out.println("SHA1 Hash: " + sha1);
            System.out.println("Base64 Signature: " + base64);

        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("ERROR: " + e.getMessage());
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
    private static PrivateKey loadPrivateKey(String resourcePath) throws Exception {
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
            System.out.println("Chave privada decodificada: " + Arrays.toString(decodedKey));

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
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

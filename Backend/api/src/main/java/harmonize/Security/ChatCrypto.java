package harmonize.Security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ChatCrypto {
    private static final String TRANSFORMATION = "AES";
    private static final byte[] SALT = "harmonizePasswordPrivateKeyWrapperSalt".getBytes();

    public String wrap(String wrapper, String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, wrapperKeyGen(wrapper));
        return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext.getBytes()));
    }

    public String unwrap(String wrapper, String ciphertext) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, wrapperKeyGen(wrapper));
        return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)));
    }

    @AllArgsConstructor
    public class Keys {
        @Getter private String publicKey;
        @Getter private String privateKey;
    }
    
    public Keys generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return new Keys(
            Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()),
            Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())
        );
    }

    public String encrypt(String senderPrivateKey, String receiverPublicKey, String message) throws Exception {
        return encrypt(decodePublicKey(receiverPublicKey), encrypt(decodePrivateKey(senderPrivateKey), message));
    }

    public String decrypt(String receiverPrivateKey, String senderPublicKey, String data) throws Exception {
        return decrypt(decodePublicKey(senderPublicKey), decrypt(decodePrivateKey(receiverPrivateKey), data));
    }

    private String encrypt(Key key, String message) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedKey = rsaCipher.doFinal(secretKey.getEncoded());
        return Base64.getEncoder().encodeToString(encryptedKey) + ":" + Base64.getEncoder().encodeToString(encryptedMessage);
    }

    private String decrypt(Key key, String data) throws Exception {
        String[] parts = data.split(":");
        byte[] encryptedKey = Base64.getDecoder().decode(parts[0]);
        byte[] encryptedMessage = Base64.getDecoder().decode(parts[1]);
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedKey = rsaCipher.doFinal(encryptedKey);
        SecretKey secretKey = new SecretKeySpec(decryptedKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);
        return new String(decryptedMessage);
    }

    private static SecretKey wrapperKeyGen(String wrapper) throws Exception {
        return new SecretKeySpec(
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(
                    new PBEKeySpec(wrapper.toCharArray(), SALT, 65536, 256)
                )
            .getEncoded(), TRANSFORMATION);
    }

    private static PublicKey decodePublicKey(String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    private static PrivateKey decodePrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

}

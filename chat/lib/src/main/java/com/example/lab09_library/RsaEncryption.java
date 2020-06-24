package com.example.lab09_library;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class RsaEncryption {
    private KeyPair myKeyPair;
    private PublicKey encryptionKey;
    private Cipher cipher;

    public RsaEncryption() {
        try {
            generateRsaKeyPair();
            initCipher();
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public RsaEncryption(KeyPair myKeyPair) {
        this.myKeyPair = myKeyPair;
    }

    private void generateRsaKeyPair() throws NoSuchAlgorithmException{
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        myKeyPair = keyPairGen.generateKeyPair();
    }

    private void initCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    }

    public PublicKey getMyPublicKey() {
        return myKeyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return myKeyPair.getPrivate();
    }

    public PublicKey getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(PublicKey encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public byte[] encryptMessage(byte[] message) throws InvalidKeyException, IllegalBlockSizeException {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
            cipher.update(message);
            return cipher.doFinal();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decryptMessage(byte[] encryptedMessage) throws InvalidKeyException, IllegalBlockSizeException {
        try {
            cipher.init(Cipher.DECRYPT_MODE, myKeyPair.getPrivate());
             return new String(cipher.doFinal(encryptedMessage), StandardCharsets.UTF_8);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
